import com.config.SctApplication;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utils.FileUtils;
import web.pages.PlaywrightAdminLoginPage;
import web.pages.PlaywrightAdministrationPage;

import java.util.List;
import java.util.Map;

import static constants.Constants.SCT_PATH;

@SpringBootTest(classes = {SctApplication.class})
public class BouquetsParamsHandling {

    @Autowired
    private PlaywrightAdminLoginPage loginPage;

    @Autowired
    private Browser browser;

    @Autowired
    private Page page;

    @Autowired
    private PlaywrightAdministrationPage administrationPage;

    @AfterEach
    public void shutDown() {
        if (page != null) page.close();
        if (browser != null) browser.close();
    }

    @Test
    @DisplayName("Set assembly time to bouquets")
    public void setBouquetsAssemblyTime() {
        String filePath = "C:\\Users\\Selecty\\Downloads\\assembly time\\50-2.xlsx";
        Map<String, String> map = FileUtils.readExcelFileWithTwoColumns(0, filePath);

        System.out.println("Bouquets quantity: " + map.size());
        map.forEach((key, value) -> System.out.printf("map.put(\"%s\", \"%s\");\n", key, value));

        int count = 1;
        loginPage.login();
        administrationPage
                .assertAdministrationPageIsDisplayed()
                .openFlowersSection();
        for (var item : map.entrySet()) {
            System.out.println("Count number: " + count);
            String bouquet = item.getKey();
            administrationPage
                    .searchItemByName(bouquet)
                    .clickSearchBtn();
            page.waitForTimeout(1500);
            waitTillLoadingIsFinished();
            if (administrationPage.isNothingFoundDisplayed()) {
                System.out.printf("Not found: %s\n", bouquet);
                count++;
                continue;
            }
            administrationPage.checkAll();
            administrationPage.editCheckedItems()
                    .setBouquetAssemblyTime(item.getValue())
                    .clickOnSaveButton();
            page.waitForTimeout(1500);
            if (administrationPage.isErrorWhileSavingPopupDisplayed()) {
                administrationPage.closeErrorWhileSavingPopup();
                System.out.println("!!! Error to save " + bouquet);
            }
            waitTillLoadingIsFinished();
            count++;
        }
    }

    @Test
    @DisplayName("Add section to bouquets")
    public void addSectionToBouquet() {
        var actionName = "добавить привязку к разделу";
        var sectionName = " . Экспресс-доставка";
        String filePath = SCT_PATH + "list.csv";
        List<String> listWithBouquets = FileUtils.readCsv(filePath);

        System.out.println("Bouquets quantity: " + listWithBouquets.size());
        listWithBouquets.forEach(System.out::println);

        int count = 1;
        loginPage.login();
        administrationPage
                .assertAdministrationPageIsDisplayed()
                .openFlowersSection();
        for (var bouquet : listWithBouquets) {
            System.out.printf("%d. %s\n", count, bouquet);
            administrationPage
                    .searchItemByName(bouquet)
                    .clickSearchBtn();
            page.waitForTimeout(1500);
            waitTillLoadingIsFinished();
            if (administrationPage.isNothingFoundDisplayed()) {
                System.out.printf("Not found: %s\n", bouquet);
                count++;
                continue;
            }
            administrationPage
                    .selectProductWithName(bouquet)
                    .clickOnActionsBtn()
                    .selectAction(actionName)
                    .clickOnSectionsBtn()
                    .selectSection(sectionName);
            administrationPage.clickOnApplyBtn();

            page.waitForTimeout(2000);
            waitTillLoadingIsFinished();
            count++;
        }
    }

    @Test
    @DisplayName("Change bouquet component")
    public void changeBouquetComponent() {
        var componentIdToBeChanged = "239417";
        var newComponentId = "239615";
        String filePath = SCT_PATH + "oasis-brick.csv";
        List<String> listWithBouquets = FileUtils.readCsv(filePath);
        System.out.println("Bouquets quantity: " + listWithBouquets.size());

        int counter = 1;
        loginPage.login();
        administrationPage
                .assertAdministrationPageIsDisplayed()
                .openFlowersSection();
        page.waitForTimeout(2000);
        for (var bouquet : listWithBouquets) {
            System.out.printf("%d. %s\n", counter, bouquet);
            administrationPage
                    .searchItemByName(bouquet)
                    .clickSearchBtn();
            page.waitForTimeout(2000);
            waitTillLoadingIsFinished();
            if (administrationPage.isNothingFoundDisplayed()) {
                System.out.printf("Not found: %s\n", bouquet);
                counter++;
                continue;
            }
            administrationPage
                    .selectProductWithName(bouquet)
                    .editCheckedItems();
            page.waitForTimeout(1000);

            administrationPage
                    .replaceComposition(componentIdToBeChanged, newComponentId);
            page.waitForTimeout(500);

            administrationPage.clickOnSaveButton();

            page.waitForTimeout(3000);
            waitTillLoadingIsFinished();
            counter++;
        }
    }

    private void waitTillLoadingIsFinished() {
        var isSavingInProgress = administrationPage.isLoaderDisplayed();
        while (isSavingInProgress) {
            page.waitForTimeout(500);
            isSavingInProgress = administrationPage.isLoaderDisplayed();
        }
    }
}
