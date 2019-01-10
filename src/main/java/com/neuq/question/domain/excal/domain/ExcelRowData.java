package com.neuq.question.domain.excal.domain;

/**
 * @author wangshyi
 * @date 2018/12/27  13:37
 */


import com.neuq.question.error.ECImplementationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Slf4j
public class ExcelRowData<T> {

    private Row row;

    private List<ExcelCellData> cells;

    /**
     * 数据是否有效
     */
    private Boolean dataValid = true;

    private T data;

    private ExcelData<T> excelData;

    public void markAsInvalid() {
        final CellStyle rowStyle = excelData.getWorkbook().createCellStyle();
        rowStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (ExcelCellData cell : cells) {
            cell.getCell().setCellStyle(rowStyle);
        }
    }

    public T parse() {

        cells = new ArrayList<>();

        int index = 0;
        do {
            Cell cell = row.getCell(index);
            index++;

//            if (cell == null) {
//                continue;
//            }

            ExcelCellData cellData = new ExcelCellData();
            cellData.setCell(cell);
            cells.add(cellData);
        } while (index <= excelData.getColumnIndexFileNames().size());

        return buildObj();
    }

    private T buildObj() {

        Class<T> clazz = excelData.getClazz();

        Map<Integer, String> fieldNames = excelData.getColumnIndexFileNames();

        try {
            data = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ECImplementationException("Exception when new instance in excel data parse phase", e);
        }

        int size = cells.size();

        AtomicBoolean initialized = new AtomicBoolean(false);
        fieldNames.forEach((k, v) -> {

            if (size - 1 < k) {
                return;
            }

            ExcelCellData excelCellData = cells.get(k);
            excelCellData.parse(excelData.getEvaluator());

            Object value = excelCellData.getValue();
            if (value == null) {
                return;
            }

            if (value instanceof String && StringUtils.isBlank(value.toString())) {
                return;
            }

            try {
                BeanUtils.copyProperty(data, v, value);
                initialized.set(true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ECImplementationException("Exception when new instance in excel data parse phase", e);
            }
        });

        if (!initialized.get()) {
            return null;
        }

        return data;
    }

}
