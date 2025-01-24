import com.config.SctApplication;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utils.FileUtils;
import web.pages.PlaywrightAdminLoginPage;
import web.pages.PlaywrightAdministrationPage;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = {SctApplication.class})
public class SetBouquetsAssemblyTime {

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
//            if (bouquets.contains(bouquet)) {
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
//        }
    }

    private void waitTillLoadingIsFinished() {
        var isSavingInProgress = administrationPage.isLoaderDisplayed();
        while (isSavingInProgress) {
            page.waitForTimeout(500);
            isSavingInProgress = administrationPage.isLoaderDisplayed();
        }
    }
}
