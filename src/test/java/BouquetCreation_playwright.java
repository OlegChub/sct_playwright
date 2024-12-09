import com.config.SctApplication;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import entities.Item;
import helper.Helper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utils.FileUtils;
import utils.ListExporter;
import web.pages.PlaywrightAdminLoginPage;
import web.pages.PlaywrightAdministrationPage;
import web.pages.PlaywrightBouquetPage;
import web.pages.PlaywrightSearchElementPage;

import java.nio.file.Paths;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

@SpringBootTest(classes = {SctApplication.class})
public class BouquetCreation_playwright {

    @Autowired
    private PlaywrightAdminLoginPage loginPage;

    @Autowired
    private Browser browser;

    @Autowired
    private Page page;

    @Autowired
    private PlaywrightAdministrationPage administrationPage;

    @Autowired
    private PlaywrightBouquetPage bouquetPage;

    @Test
    @SneakyThrows
    @DisplayName("Add new bouquets using composition names")
    public void addNewBouquetUsingCompositionNames_Playwright() {
        String imagesFolderPath = "C:\\Users\\Selecty\\Downloads\\sct\\Декабрь 02";
        String compositionFilePath = "C:\\Users\\Selecty\\Downloads\\sct\\bouquets.xlsx";
        var itemsList = FileUtils.readExcelAndMatchCompositionsWithStemsCSV(compositionFilePath);
        loginPage.login();
        administrationPage
                .assertAdministrationPageIsDisplayed()
                .openFlowersSection();
        int folderWithImagesStartIndex = 1; // Assuming that the folder with photos for the first bouquet has name "1"
        int itemsListStartIndex = 0; // Default is 0. Set other if want to start not from first item from Excel file.
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
            sleep(2000);
            bouquetPage
                    .clickOnInDetailTab()
                    .addDetailedDescription(itemsList.get(i).getItemDescription());
            uploadDetailImage(imagesFolderPath, folderWithImagesStartIndex);
            bouquetPage
                    .clickOnTradeCatalogTab()
                    .clickOnParametersTab()
                    .setDefaultGoodsQuantity();
            sleep(3000);
            bouquetPage.clickOnSaveBtn();
            folderWithImagesStartIndex++;
//            sleep(3000);
            System.out.printf("%s has been saved%n", itemName);
        }
        browser.close();
    }

    @Test
    @DisplayName("Check composition matching")
    public void checkCompositionMatching() {
        String compositionFilePath = "C:\\Users\\Selecty\\Downloads\\sct\\bouquets.xlsx";
        var itemsList = FileUtils.readExcelAndMatchCompositionsWithStemsCSV(compositionFilePath);
        ListExporter.listToCSV(itemsList);
    }

    private void addNewCompositionFieldUsingName(Item item, int elementIndex) {
        Page popup = page.waitForPopup(() -> {
            bouquetPage.addCompositionItemInput();
        });
        PlaywrightSearchElementPage searchElementPage = new PlaywrightSearchElementPage(popup);
        popup.waitForLoadState();
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

    private void insertAlterElementName(Locator altInputs, Item item, int elementIndex) {
        altInputs.nth(elementIndex).clear();
        altInputs.nth(elementIndex).fill(getElementName(item, elementIndex));
    }

    private void uploadAdditionalImages(String pathToFolder, int folderNumber) {
        for (int i = 4; i > 1; i--) {
            String imagePath = format("%s\\%d\\4 (%s).jpg", pathToFolder, folderNumber, i);
            System.out.printf("Uploading additional image: %s%n", imagePath);
            bouquetPage.uploadAdditionalPictures(Paths.get(imagePath));
        }
    }

    private void uploadDetailImage(String pathToFolder, int folderNumber) {
        String imagePath = format("%s\\%d\\4 (1).jpg", pathToFolder, folderNumber);
        System.out.printf("Uploading detail image: %s%n", imagePath);
        bouquetPage.uploadDetailPicture(Paths.get(imagePath));
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
}
