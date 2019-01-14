package com.neuq.question.support;

import com.neuq.question.error.ECIOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author wangshyi
 * @since 2018/7/19 14:09
 */
@Slf4j
public class SignInRecordExporter {

    private String title;

    private List<String> rowNames;

    private List<List<Object>> dataList;

    public SignInRecordExporter(String title, List<String> rowNames, List<List<Object>> dataList) {
        this.title = title;
        this.rowNames = rowNames;
        this.dataList = dataList;
    }

    /**
     * Excel当前数据行数(将要写入数据的索引数)
     */
    private int currentRowNum = 0;

    private static final int DEFAULT_COLUMN_SIZE = 30;

    private Workbook workbook;

    /**
     * 将列表导出成excel
     */
    public byte[] exportExcel() {
        exportExcelTitle(title, rowNames);

        exportExcelData(title, dataList);

        return saveAsByte();
    }

    /**
     * 导出文件列名
     *
     * @param title    标题
     * @param rowNames 列名
     */
    private void exportExcelTitle(String title, List<String> rowNames) {

        workbook = new XSSFWorkbook();
        //生成一个表格
        Sheet sheet = workbook.createSheet(title);
        Row row = sheet.createRow(currentRowNum);
        for (int i = 0; i < rowNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(new XSSFRichTextString(rowNames.get(i)));
        }

        currentRowNum++;

    }

    /**
     * 导出数据
     *
     * @param title    标题
     * @param dataList 数据
     */
    private void exportExcelData(String title, List<List<Object>> dataList) {

        Sheet sheet = workbook.getSheet(title);
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
        sheet.createFreezePane(0, 1);
        for (List<Object> dataRow : dataList) {
            Row row = sheet.createRow(currentRowNum);
            for (int i = 0; i < dataRow.size(); i++) {
                Cell cell = row.createCell(i);
                Object dataObj = dataRow.get(i);
                if (dataObj != null) {
                    setCellValue(cell, dataObj);
                } else {
                    cell.setCellValue("");
                }
            }
            currentRowNum++;
        }

    }

    /**
     * 将date转换为特定时间格式
     *
     * @param date 日期
     * @return 格式化日期
     */
    private String getDateFormat(Date date) {

        String pattern = "yyyy-MM-dd HH:mm:ss";
        Locale locale = LocaleContextHolder.getLocale();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
        return simpleDateFormat.format(date);
    }

    /**
     * 设置单元格的值
     *
     * @param cell    单元格
     * @param dataObj 对象信息
     */
    private void setCellValue(Cell cell, Object dataObj) {
        if (dataObj instanceof Integer) {
            cell.setCellValue(Integer.parseInt(dataObj.toString()));
            cell.setCellType(CellType.NUMERIC);
        } else if (dataObj instanceof Double) {
            cell.setCellValue(Double.parseDouble(dataObj.toString()));
            cell.setCellType(CellType.NUMERIC);
        } else if (dataObj instanceof Long) {
            cell.setCellValue(Long.parseLong(dataObj.toString()));
            cell.setCellType(CellType.STRING);
        } else if (dataObj instanceof Date) {
            cell.setCellValue(getDateFormat((Date) dataObj));
            cell.setCellType(CellType.STRING);
        } else if (dataObj instanceof String) {
            cell.setCellValue(dataObj.toString());
            cell.setCellType(CellType.STRING);
        }
    }

    /**
     * 保存为byte[]
     *
     * @return byte数组
     */
    private byte[] saveAsByte() {
        byte[] bytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            bytes = outputStream.toByteArray();

        } catch (IOException e) {
            throw new ECIOException("SignInRecordExporter saveAsByte() error:{}", e);
        }

        return bytes;
    }

}
