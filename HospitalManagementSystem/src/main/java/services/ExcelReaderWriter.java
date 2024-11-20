/**
 * The ExcelReaderWriter class provides utility methods for reading and writing Excel files.
 * It supports both reading data from existing Excel files and writing data to new files
 * using the Apache POI library.
 * 
 * <p>Note: This class assumes the use of Excel files in the .xlsx format.</p>
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderWriter {

    /**
     * Writes a 2D list of string data to an Excel file.
     *
     * @param data     the 2D list containing rows of data to be written.
     * @param filePath the file path where the Excel file will be saved.
     * @throws IOException if an error occurs during file writing.
     */
    public static void write(List<List<String>> data, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        int rowNum = 0;
        for (List<String> rowData : data) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 0;
            for (String cellData : rowData) {
                Cell cell = row.createCell(cellNum++);
                cell.setCellValue(cellData);
            }
        }

        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }

    /**
     * Reads data from an Excel file and returns it as a 2D list of strings.
     *
     * @param filePath the file path of the Excel file to be read.
     * @return a 2D list containing rows of data from the Excel file.
     * @throws IOException if an error occurs during file reading.
     */
    public static List<List<String>> read(String filePath) throws IOException {
        List<List<String>> data = new ArrayList<>();

        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();
            List<String> rowData = new ArrayList<>();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                rowData.add(getCellValueAsString(cell));
            }
            data.add(rowData);
        }

        workbook.close();
        fis.close();

        return data;
    }

    /**
     * Converts the value of a cell to a string based on its type.
     *
     * @param cell the cell to be processed.
     * @return the string representation of the cell's value.
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return "Empty";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Format date and time as "d-MMM-yyyy h:mm:ss a"
                    SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy h:mm:ss a");
                    return dateFormat.format(cell.getDateCellValue());
                } else {
                    // Avoid scientific notation for large numbers
                    return String.format("%.0f", cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
