package com.neuq.question.domain.excal.domain;



import com.neuq.question.error.ECIOException;
import com.neuq.question.error.ECIllegalArgumentException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.UnsupportedFileFormatException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author wangshyi
 * @date 2018/12/27  11:23
 */
@Data
@Slf4j
public class ExcelData<T> {

    private static final ExcelDataForkJoinPool threadPool = new ExcelDataForkJoinPool(16);

    private final byte[] source;

    /**
     * 需要导入数据的sheet列表
     */
    private final List<Integer> sheetIndexes;

    /**
     * 数据起始行,即从第几行开始有效
     */
    private final Integer startRowIndex;

    /**
     * 数据类
     */
    private final Class<T> clazz;

    /**
     * 每一列对应的字段,如果为null,表示为无意义列
     */
    private final Map<String, Integer> fieldNameIndexes;

    private Map<Integer, String> columnIndexFileNames;

    private Workbook workbook;

    private FormulaEvaluator evaluator;

    private CreationHelper creationHelper;

    private List<ExcelSheetData<T>> sheets;


    public ExcelData(byte[] source, List<Integer> sheetIndexes, Integer startRowIndex, Class<T> clazz,
                     Map<String, Integer> fieldNameIndexes) {
        this.source = source;
        this.sheetIndexes = sheetIndexes;
        this.startRowIndex = startRowIndex;
        this.clazz = clazz;
        this.fieldNameIndexes = fieldNameIndexes;

        columnIndexFileNames = new HashMap<>();
        fieldNameIndexes.forEach((k, v) -> columnIndexFileNames.put(v, k));
    }

    /**
     * 列举所有的行数据
     *
     * @return 行数据列表
     */
    public List<ExcelRowData<T>> listAllData() {

        parse();

        return sheets.stream()
                .map(sheet -> {
                    try {
                        return sheet.getRows();
                    } catch (Exception e) {
                        log.error("Exception when process data", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public void processData(Consumer<ExcelRowData<T>> excelRowDataConsumer) {

        parse();

        sheets.forEach(sheet -> {
            try {
                sheet.getRows().forEach(excelRowDataConsumer);
            } catch (Exception e) {
                log.error("Exception when process data", e);
            }
        });
    }

    public void parallelProcessData(Consumer<ExcelRowData<T>> excelRowDataConsumer) {
        parse();

        sheets.forEach(sheet -> {
            try {
                threadPool.submit(() -> sheet.getRows().forEach(excelRowDataConsumer));
            } catch (Exception e) {
                log.error("Exception when process data", e);
            }
        });
    }


    public void parse() {

        if (workbook != null) {
            return;
        }

        buildWorkbook();

        evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        creationHelper = workbook.getCreationHelper();

        sheets = new ArrayList<>(sheetIndexes.size());

        for (Integer sheetIndex : sheetIndexes) {
            Sheet sheet;
            try {
                sheet = workbook.getSheetAt(sheetIndex);
            } catch (IllegalArgumentException e) {
                throw new ECIllegalArgumentException("parse excel sheet failed", e);
            }

            ExcelSheetData<T> sheetData = new ExcelSheetData<>(sheet);
            sheetData.setExcelData(this);
            sheetData.parse();
            sheets.add(sheetData);
        }

    }

    private void buildWorkbook() {
        try {
            workbook = new HSSFWorkbook(new ByteArrayInputStream(source));
            return;
        } catch (IOException e) {
            throw new ECIOException("excel create error", e);
        } catch (UnsupportedFileFormatException e) {
            //ignore
        }

        try {
            workbook = new XSSFWorkbook(new ByteArrayInputStream(source));
            return;
        } catch (IOException e) {
            throw new ECIOException("excel create error", e);
        } catch (UnsupportedFileFormatException e) {
            throw new ECIllegalArgumentException("unsupported file format ", e);
        }
    }


    public byte[] exportResult() {

        sheets.forEach(sheet -> {

            sheet.getRows().stream().filter(row -> !row.getDataValid())
                    .forEach(row -> markInvalidRow(sheet, row));

        });

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            workbook.write(stream);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new ECIOException("Exception when export workbook result", e);
        }
    }

    private void markInvalidRow(ExcelSheetData<T> sheet, ExcelRowData<T> row) {
        try {
            row.markAsInvalid();
            row.getCells().stream()
                    .filter(cell -> StringUtils.isNotBlank(cell.getComment()))
                    .forEach(cell -> cell.addComment(creationHelper, sheet.getPatriarch()));
        } catch (Exception e) {
            log.error("mark invalid excel row failed", e);
        }
    }


    public boolean isXlsxFormat() {
        return getWorkbook() instanceof XSSFWorkbook;
    }

}
