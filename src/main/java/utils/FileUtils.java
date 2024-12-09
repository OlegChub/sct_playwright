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
        String filePath = "..\\sct\\src\\main\\resources\\files\\stems.csv"; // Path to CSV file with stems
        List<String> stems = FileUtils.readCsv(filePath);

        FileInputStream fis = new FileInputStream(new File(excelFilePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
        for (Row row : sheet) {
            Cell elementItemName = row.getCell(1);
            Cell elementItemDescription = row.getCell(2);
            if (elementItemName != null && elementItemName.getCellType() != CellType.BLANK) {
                String name = elementItemName.getStringCellValue().trim();
                String description = elementItemDescription.getStringCellValue().trim();
//                var weight = elementItemWeight.getNumericCellValue();
//                var height = (int) elementItemHeight.getNumericCellValue();
//                var width = (int) elementItemWidth.getNumericCellValue();
                currentItem = new Item(name, description);
//                currentItem.setWeight(weight);
//                currentItem.setHeight(height);
//                currentItem.setWidth(width);
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
            Cell elementItemWeight = row.getCell(6);
            if (elementItemWeight != null && elementItemWeight.getCellType() != CellType.BLANK) {
                Cell elementItemHeight = row.getCell(7);
                Cell elementItemWidth = row.getCell(8);
                var weight = elementItemWeight.getStringCellValue();
                var height = elementItemHeight.getStringCellValue();
                var width = elementItemWidth.getStringCellValue();
                currentItem.setWeight(weight);
                currentItem.setHeight(height);
                currentItem.setWidth(width);
                System.out.printf("%s кг | %s %s см\n", weight, height, width);
            }
        }

        return items;
    }

    @SneakyThrows
    public Map<String, Integer> readExcelFileWithTwoColumns(String excelFilePath) {
        Map<String, Integer> items = new HashMap<>();

        FileInputStream fis = new FileInputStream(new File(excelFilePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
        for (Row row : sheet) {
            Cell elementItemName = row.getCell(0);
            Cell elementItemQuantity = row.getCell(1);
            if (elementItemName != null && elementItemName.getCellType() != CellType.BLANK) {
                String name = elementItemName.getStringCellValue().trim();
                int quantity = (int) elementItemQuantity.getNumericCellValue();
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
