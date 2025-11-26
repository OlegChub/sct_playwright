import com.config.SctApplication;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import entities.Item;
import helper.Helper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utils.Exporter;
import utils.FileUtils;
import web.listeners.ScreenshotOnFailureExtension;
import web.pages.PlaywrightAdminLoginPage;
import web.pages.PlaywrightAdministrationPage;
import web.pages.PlaywrightBouquetPage;
import web.pages.PlaywrightSearchElementPage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static constants.Constants.COMPOSITION_FILE_PATH;
import static constants.Constants.SCT_PATH;
import static java.lang.String.format;

@SpringBootTest(classes = {SctApplication.class})
@ExtendWith(ScreenshotOnFailureExtension.class) // Register the extension
public class BouquetCreation_playwright {

    @Autowired
    private PlaywrightAdminLoginPage loginPage;

    @Autowired
    private Page page;

    @Autowired
    private PlaywrightAdministrationPage administrationPage;

    @Autowired
    private PlaywrightBouquetPage bouquetPage;

    @Test
    @DisplayName("Add new bouquets using composition names")
    public void addNewBouquetUsingCompositionNames_Playwright() {
        String imagesFolderPath = SCT_PATH + "Октябрь 25";
        var itemsList = FileUtils.readExcelAndMatchCompositionsWithStemsCSV(COMPOSITION_FILE_PATH);
        loginPage.login();
        administrationPage
                .assertAdministrationPageIsDisplayed()
                .openFlowersSection();
        int folderWithImagesStartIndex = 1; // Assuming that the folder with photos for the first bouquet has name "1"
        int itemsListStartIndex = folderWithImagesStartIndex - 1; // Default is 0. Set other if want to start not from first item from Excel file.
        // Changing itemsListStartIndex, folderWithImagesStartIndex needed to be changed too!
        for (int i = itemsListStartIndex; i < itemsList.size(); i++) {
            String itemName = itemsList.get(i).getItemName();
            System.out.printf("Item name: %s%n", itemName);
            String basicBouquetName = Helper.getBasicBouquet(itemName);
            System.out.printf("Basic bouquet type: %s%n", basicBouquetName);
            administrationPage
                    .searchItemByName(basicBouquetName)
                    .clickSearchBtn();
            administrationPage.copyElementWithName(basicBouquetName);
            // Adding name and sorting
            fillOutBouquetNameAndSorting(itemsList.get(i));
            // Set bouquet dimensions
            setBouquetDimensions(itemsList.get(i));
            Locator altInputs = bouquetPage.getAlternativeCompositionItemInputs();
            int elementsQuantity = itemsList.get(i).getElements().size();
            // Adding composition
            int counter = 0;
            for (int j = 0; j < elementsQuantity; j++) {
                insertAlterElementName(altInputs, itemsList.get(i), j);
                bouquetPage.clearCodeInputOfFirstCompositionItem();
                addNewCompositionFieldUsingName(itemsList.get(i), j);
                bouquetPage.compositionItemQuantityInputs().nth(counter).fill(getElementQuantity(itemsList.get(i), j));
                counter++;
            }
            uploadAdditionalImages(imagesFolderPath, folderWithImagesStartIndex);
            page.waitForTimeout(3500);
            bouquetPage
                    .clickOnInDetailTab()
                    .addDetailedDescription(itemsList.get(i).getItemDescription());
            uploadDetailImage(imagesFolderPath, folderWithImagesStartIndex);
            page.waitForTimeout(3500);
            bouquetPage
                    .clickOnTradeCatalogTab()
                    .clickOnParametersTab()
                    .setDefaultGoodsQuantity();
            page.waitForTimeout(3000);
            bouquetPage.clickOnSaveBtn();
            waitTillLoadingIsFinished();
            System.out.printf("#%d %s has been saved\n", folderWithImagesStartIndex, itemName);
            folderWithImagesStartIndex++;
        }
        page.waitForTimeout(3000);
    }

    @Test
    @DisplayName("Add new NON-bouquet goods")
    public void addNewNonBouquetGoods() {
        String imagesFolderPath = SCT_PATH + "Апрель 11 шоколад";
        String compositionFilePath = SCT_PATH + "choco.xlsx";
        String basicGoodsName = "Шоколад basic";
        var itemsList = FileUtils.readOneColumnOfExcelFile(1, compositionFilePath);
        var itemsPricesList = FileUtils.readOneColumnOfExcelFile(2, compositionFilePath);

        loginPage.login();
        administrationPage
                .assertAdministrationPageIsDisplayed()
                .openFlowersSection();
        int folderWithImagesStartIndex = 1; // Assuming that the folder with photos for the first bouquet has name "1"
        int itemsListStartIndex = folderWithImagesStartIndex - 1; // Default is 0. Change folderWithImagesStartIndex if want to start not from first item from Excel file.
        for (int i = itemsListStartIndex; i < itemsList.size(); i++) {
            String itemName = itemsList.get(i);
            System.out.printf("Item name: %s%n", itemName);
            System.out.printf("Basic bouquet type: %s%n", basicGoodsName);
            administrationPage
                    .searchItemByName(basicGoodsName)
                    .clickSearchBtn();
            administrationPage.copyElementWithName(basicGoodsName);
            // Adding name and sorting
            bouquetPage
                    .assertBouquetPageDisplayed()
                    .setBouquetName(itemsList.get(i));
            page.waitForTimeout(2000);
            bouquetPage
                    .clickOnInDetailTab();
            uploadFirstImageFromFolderAsDetailImage(imagesFolderPath, folderWithImagesStartIndex);
            page.waitForTimeout(2000);
            bouquetPage
                    .clickOnTradeCatalogTab()
                    .setGoodsPrice(itemsPricesList.get(i))
                    .clickOnParametersTab()
                    .setDefaultGoodsQuantity();
            page.waitForTimeout(2000);
            bouquetPage.clickOnSaveBtn();
            waitTillLoadingIsFinished();
            System.out.printf("#%d %s has been saved\n", folderWithImagesStartIndex, itemName);
            folderWithImagesStartIndex++;
        }
        page.waitForTimeout(3000);
    }

    @Test
    @DisplayName("Check composition matching")
    public void checkCompositionMatching() {
        var itemsList = FileUtils.readExcelAndMatchCompositionsWithStemsCSV(COMPOSITION_FILE_PATH);
        itemsList.forEach(System.out::println);
//      Exporter.itemListToCSV(itemsList);
        Exporter.listToCSV(itemsList, "components");
//      System.out.println(FileUtils.readOneColumnOfExcelFile(1, compositionFilePath));
    }

    @Test
    public void findRepeatingElements() {
        String compositionFilePath = SCT_PATH + "stems_to_edition.xlsx";
        List<String> list = FileUtils.readOneColumnOfExcelFile(0, compositionFilePath);
        Set<String> set = new HashSet<>();
        Set<String> resultingSet = new HashSet<>();

        for (String item : list) {
            if (set.contains(item)) {
                resultingSet.add(item);
            } else {
                set.add(item);
            }
        }
        resultingSet.forEach(System.out::println);
        Exporter.listToCSV(List.of(resultingSet), "repetitions");
    }

    private void addNewCompositionFieldUsingName(Item item, int elementIndex) {
        Page popup = page.waitForPopup(() -> bouquetPage.addCompositionItemInput());
        popup.waitForLoadState();
        PlaywrightSearchElementPage searchElementPage = new PlaywrightSearchElementPage(popup);
        String elementName = getElementName(item, elementIndex);
        searchElementPage.findElementByName(elementName);
        searchElementPage.selectElementWithNameUsingDoubleClick(elementName);
        popup.close();
    }

    private void fillOutBouquetNameAndSorting(Item item) {
        String itemName = item.getItemName();
        bouquetPage
                .assertBouquetPageDisplayed()
                .setBouquetName(itemName)
                .setBouquetSortingIndex();
        System.out.printf("Adding sorting index and bouquet name: %s%n", itemName);
    }

    private void fillOutBouquetNameAndSorting(String itemName) {
        bouquetPage
                .assertBouquetPageDisplayed()
                .setBouquetName(itemName)
                .setBouquetSortingIndex();
        System.out.printf("Adding sorting index and bouquet name: %s%n", itemName);
    }

    private void insertAlterElementName(Locator altInputs, Item item, int elementIndex) {
        altInputs.nth(elementIndex).clear();
        altInputs.nth(elementIndex).fill(getElementName(item, elementIndex));
    }

    private void setBouquetDimensions(Item item) {
//        Height	Width	Length	Weight	Assembly time
        int height = item.getHeight();
        int width = item.getWidth();
        double weight = item.getWeight();
        int length = item.getLength();
        int assemblyTime = item.getAssemblyTime();

        if (height > 0) {
            bouquetPage.setBouquetHeight(String.valueOf(height));
        }
        if (width > 0) {
            bouquetPage.setBouquetWidth(String.valueOf(width));
        }
        if (length > 0) {
            bouquetPage.setBouquetLength(String.valueOf(length));
        }
        if (weight > 0) {
            bouquetPage.setBouquetWeight(String.valueOf(weight));
        }
        if (assemblyTime > 0) {
            bouquetPage.setBouquetAssemblyTime(String.valueOf(assemblyTime));
        }
    }

    private void uploadAdditionalImages(String pathToFolder, int folderNumber) {
        for (int i = 4; i > 1; i--) {
            String imagePath = format("%s\\%d\\1 (%s).jpg", pathToFolder, folderNumber, i);
            System.out.printf("Uploading additional image: %s%n", imagePath);
            bouquetPage.uploadAdditionalPictures(Paths.get(imagePath));
        }
    }

    private void uploadDetailImage(String pathToFolder, int folderNumber) {
        String imagePath = format("%s\\%d\\1 (1).jpg", pathToFolder, folderNumber);
        System.out.printf("Uploading detail image: %s%n", imagePath);
        bouquetPage.uploadDetailPicture(Paths.get(imagePath));
    }

    @SneakyThrows
    private void uploadFirstImageFromFolderAsDetailImage(String pathToFolder, int folderNumber) {
        String imagePath = format("%s\\%d", pathToFolder, folderNumber);
        // List the files in the directory
        Path[] imageFiles = Files.list(Paths.get(imagePath))
                .toArray(Path[]::new);
        // Check if there are at least two images
        if (imageFiles.length >= 2) {
            // Get the first image (assuming they are .jpg)
            Path firstImage = imageFiles[0];
            System.out.println("First image found: " + firstImage);
            bouquetPage.uploadDetailPicture(firstImage);
        } else {
            System.out.println("Not enough image files found in the directory.");
        }
    }

    private String getElementQuantity(Item item, int elementIndex) {
        String quantity = item.getElements().get(elementIndex).getQuantity();
        System.out.printf("Element quantity is: %s%n", quantity);
        return quantity;
    }

    private String getElementName(Item item, int elementIndex) {
        String name = item.getElements().get(elementIndex).getName();
        System.out.printf("Using element: %s%n", name);
        return name;
    }

    private void waitTillLoadingIsFinished() {
        var isSavingInProgress = administrationPage.isLoaderDisplayed();
        while (isSavingInProgress) {
            page.waitForTimeout(500);
            isSavingInProgress = administrationPage.isLoaderDisplayed();
        }
    }
}
