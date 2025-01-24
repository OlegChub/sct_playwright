package utils;

import entities.Element;
import entities.Item;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@UtilityClass
public class Exporter {
    @SneakyThrows
    public void exportListToExcel(List<Object> dataList, String fileName) {
        // Create a Workbook
        String filePath = Paths.get("./src/main/resources/outputs/" + fileName + ".xlsx").toString();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("List data");
        int counter = 1;
        // Fill the sheet with data from the map
        int rowNum = 0; // Start from the second row
        for (var item : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(counter);
            row.createCell(1).setCellValue(item.toString());
            counter++;
        }
        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            System.out.println("Excel file created successfully at: " + filePath);
        } finally {
            workbook.close();
        }
    }

    @SneakyThrows
    public void itemListToCSV(List<Item> itemsList) {
        String filePath = Paths.get("src", "main", "resources", "outputs", "components.csv").toString();
        // Create a FileWriter
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write each element to the CSV file
            for (var item : itemsList) {
                writer.append(item.getItemName());
                writer.append('\n');
                for (Element element : item.getElements()) {
                    writer.append(format("%s - %s", element.getName(), element.getQuantity())); // Write the element
                    writer.append('\n');
                }
            }
        }
    }

    @SneakyThrows
    public void listToCSV(List<?> list, String fileName) {
        String filePath = Paths.get("./src/main/resources/outputs/" + fileName + ".csv").toString();
        // Create a FileWriter
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write each element to the CSV file
            for (var item : list) {
                if (item instanceof Item) {
                    writer.append(((Item) item).getItemName());
                    writer.append('\n');
                    for (Element element : ((Item) item).getElements()) {
                        writer.append(format("%s - %s", element.getName(), element.getQuantity())); // Write the element
                        writer.append('\n');
                    }
                } else {
                    writer.append(item.toString());
                    writer.append('\n');
                }
            }
            System.out.println("CSV file created successfully at: " + filePath);
        }
    }

    @SneakyThrows
    public void exportMapToExcel(Map<String, String> dataMap, String fileName) {
        String filePath = Paths.get("src", "main", "resources", "outputs", fileName + ".xlsx").toString();
        // Create a workbook and a sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");
        int counter = 1;
        // Fill the sheet with data from the map
        int rowNum = 0;
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(counter);
            row.createCell(1).setCellValue(entry.getKey());
            row.createCell(2).setCellValue(entry.getValue());
            counter++;
        }
        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            System.out.println("Excel file created successfully at: " + filePath);
        } finally {
            workbook.close();
        }
    }
}