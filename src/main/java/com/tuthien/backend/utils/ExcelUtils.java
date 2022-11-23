package com.tuthien.backend.utils;

import org.apache.poi.ss.usermodel.*;
import org.w3c.dom.css.RGBColor;

import javax.swing.border.Border;

public class ExcelUtils {

    public static CellStyle getStyleHeader(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        CellStyle border = ExcelUtils.getBorder(wb);
        cellStyle.cloneStyleFrom(border);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font boldFont = ExcelUtils.getBoldFont(wb);
        boldFont.setColor(IndexedColors.WHITE.index);
        cellStyle.setFont(boldFont);
        cellStyle.setFillForegroundColor(IndexedColors.GREEN.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    public static CellStyle getBorder(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.index);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.index);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.index);
        return cellStyle;
    }

    public static Font getBoldFont(Workbook wb) {
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        return font;
    }
}
