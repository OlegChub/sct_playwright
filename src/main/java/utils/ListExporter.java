package utils;

import entities.Element;
import entities.Item;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;

@UtilityClass
public class ListExporter {
    @SneakyThrows
    public void listToExcel(List<Item> dataList) {
        // Create a Workbook
        String filePath = Paths.get("src", "main", "resources", "outputs", "components.xlsx").toString();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        int rowIndex = 0; // Initialize row index
        for (Item item : dataList) {
            Row row1 = sheet.createRow(rowIndex); // Create a new row for each element
            Cell cell1 = row1.createCell(0); // Create a new cell in the current row
            cell1.setCellValue(item.getItemName());
            // Get the elements from the current object in dataList
            var elements = item.getElements(); // Assuming this returns a collection
            rowIndex++;
            // Write each element to a new row in the sheet
            for (var element : elements) {
                Row row = sheet.createRow(rowIndex); // Create a new row for each element
                Cell cell = row.createCell(0); // Create a new cell in the current row
                cell.setCellValue(format("%s - %s", element.getName(), element.getQuantity())); // Set the value of the cell
                rowIndex++; // Increment the row index for the next row
            }
            rowIndex++; //adds empty cell to separate bouquets
        }
        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }
    }

    @SneakyThrows
    public void listToCSV(List<Item> itemsList) {
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
}