package utils;

import entities.*;
import helper.CompositionIdentifier;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

@UtilityClass
public class FileUtils {

    public List<String> readExcelFile(String filePath, int startRow, int startColumn) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
        List<String> list = new ArrayList<>();
        for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell cell = row.getCell(startColumn);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    list.add(getCellValueAsString(cell));
                }
            }
        }
        workbook.close();
        fileInputStream.close();
        return list;
    }

    @SneakyThrows
    public List<Item> readExcelAndMatchCompositionsWithStemsCSV(String excelFilePath) {
        List<Item> items = new ArrayList<>();
        Item currentItem = null;
//        String filePath = "..\\sct\\src\\main\\resources\\files\\stems.csv"; // Path to CSV file with stems
        String filePath = "C:\\Users\\Selecty\\Downloads\\sct\\stemsLocal.csv"; // Path to CSV file with stems
        List<String> stems = FileUtils.readCsv(filePath);

        FileInputStream fis = new FileInputStream(new File(excelFilePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // skip first row with column names
            }
            Cell elementItemName = row.getCell(1);
            Cell elementItemDescription = row.getCell(2);
            if (elementItemName != null && elementItemName.getCellType() != CellType.BLANK) {
                String name = elementItemName.getStringCellValue().trim();
                String description = elementItemDescription.getStringCellValue().trim();
                var height = (int) row.getCell(6).getNumericCellValue();
                var width = (int) row.getCell(7).getNumericCellValue();
                var length = (int) row.getCell(8).getNumericCellValue();
                var weight = row.getCell(9).getNumericCellValue();
                var assemblyTime = (int) row.getCell(10).getNumericCellValue();

                currentItem = new Item(name, description);
                currentItem.setHeight(height);
                currentItem.setWidth(width);
                currentItem.setLength(length);
                currentItem.setWeight(weight);
                currentItem.setAssemblyTime(assemblyTime);

                System.out.printf("%dх%dх%d см | %.1f кг | %d мин\n", height, width, length, weight, assemblyTime);
                items.add(currentItem);
            }
            Cell elementCodeCell = row.getCell(3);
            if (elementCodeCell != null && elementCodeCell.getCellType() != CellType.BLANK) {
                Cell elementNameCell = row.getCell(4);
                Cell quantityCell = row.getCell(5);

                int elementCode = (int) elementCodeCell.getNumericCellValue();
                String elementName = elementNameCell.getStringCellValue().trim();
                String matchedElementName = CompositionIdentifier.findBestMatch(CompositionIdentifier
                        .correctCompositionSpelling(elementName), stems);
                double quantity = quantityCell.getNumericCellValue();
                String quantityAsString = String.valueOf(quantity).replace(".0", "").trim();
                Element element = new Element(elementCode, matchedElementName, quantityAsString);
                currentItem.addElement(element);
            }
        }

        return items;
    }

    @SneakyThrows
//    public Map<String, Integer> readExcelFileWithTwoColumns(String excelFilePath) {
    public Map<String, String> readExcelFileWithTwoColumns(int startCell, String excelFilePath) {
//        Map<String, Integer> items = new HashMap<>();
        Map<String, String> items = new HashMap<>();

        FileInputStream fis = new FileInputStream(new File(excelFilePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
        for (Row row : sheet) {
            Cell elementItemName = row.getCell(startCell);
            Cell elementItemQuantity = row.getCell(startCell+1);
            if (elementItemName != null && elementItemName.getCellType() != CellType.BLANK) {
                String name = elementItemName.getStringCellValue().trim();
//                int quantity = (int) elementItemQuantity.getNumericCellValue();
                var quantity = elementItemQuantity.getStringCellValue().trim();
                items.put(name, quantity);
            }
        }

        return items;
    }

    @SneakyThrows
    public List<String> readCsv(String filePath) {
        List<String> items = new ArrayList<>();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        while ((line = br.readLine()) != null) {
            items.add(line);
        }
        return items;
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
