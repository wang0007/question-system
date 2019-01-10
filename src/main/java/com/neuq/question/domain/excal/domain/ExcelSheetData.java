package com.neuq.question.domain.excal.domain;

import lombok.Data;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangshyi
 * @date 2018/12/27  13:54
 */
@Data
public class ExcelSheetData<T> {

    private final Sheet sheet;

    private List<ExcelRowData<T>> rows;

    private List<T> data;

    private ExcelData<T> excelData;

    private Drawing<?> patriarch;

    public ExcelSheetData(Sheet sheet) {
        this.sheet = sheet;
        patriarch = sheet.createDrawingPatriarch();
    }

    public void parse() {

        rows = new ArrayList<>();
        Integer startRowIndex = excelData.getStartRowIndex();

        do {
            Row row = sheet.getRow(startRowIndex);

            if (row == null) {
                break;
            }

            ExcelRowData<T> rowData = new ExcelRowData<>();
            rowData.setRow(row);
            rowData.setExcelData(excelData);
            T data = rowData.parse();
            if (data != null) {
                rows.add(rowData);
            }
            startRowIndex++;
        } while (true);


    }
}

