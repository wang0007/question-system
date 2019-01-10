package com.neuq.question.domain.excal.domain;

import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * @author wangshyi
 * @date 2018/12/27  13:38
 */

@Data
public class ExcelCellData {

    private Cell cell;

    private CellValue cellValue;

    private Object value;

    /**
     * 批注信息
     */
    private String comment;

    public void addComment(CreationHelper creationHelper, Drawing<?> patriarch) {

        ClientAnchor anchor = creationHelper.createClientAnchor();
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
        anchor.setCol1(cell.getColumnIndex() + 1);
        anchor.setRow1(cell.getRowIndex() + 1);
        anchor.setCol2(cell.getColumnIndex() + 3);
        anchor.setRow2(cell.getRowIndex() + 3);
        anchor.setDx1(100);
        anchor.setDx2(1000);
        anchor.setDy1(100);
        anchor.setDy2(1000);


        Comment cellComment = patriarch.createCellComment(anchor);

        cellComment.setString(creationHelper.createRichTextString(comment));

        cellComment.setVisible(false);
        cellComment.setColumn(cell.getColumnIndex());
        cellComment.setRow(cell.getRowIndex());

        cell.setCellComment(cellComment);
    }

    public void parse(FormulaEvaluator evaluator) {

        cellValue = evaluator.evaluate(cell);

        if (cellValue == null) {
            return;
        }

        switch (cellValue.getCellTypeEnum()) {
            case STRING:
                value = cellValue.getStringValue().trim();
                break;
            case BOOLEAN:
                value = cellValue.getBooleanValue();
                break;
            case NUMERIC:
                processNumericCell();
                break;
            case FORMULA:
            case _NONE:
            case ERROR:
            case BLANK:
            default:
                value = null;
        }

    }

    private void processNumericCell() {

        if (cell instanceof XSSFCell) {
            value = ((XSSFCell) cell).getRawValue().trim();
            return;
        }

        if (cell instanceof HSSFCell) {
            cell.setCellType(CellType.STRING);
            value = cell.getStringCellValue().trim();
            return;
        }

    }

}
