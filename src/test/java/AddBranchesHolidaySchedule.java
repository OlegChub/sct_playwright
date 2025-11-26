import com.config.SctApplication;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import web.pages.ya.BranchPage;
import web.pages.ya.HolidaysAndDaysOffPopUp;
import web.pages.ya.SCTBranchesPage;
import web.pages.ya.YandexIDLoginPage;

import java.util.List;

import static enums.NewScheduleDates.*;

@SpringBootTest(classes = {SctApplication.class})
public class AddBranchesHolidaySchedule {


    @Autowired
    private Page page;

    @Autowired
    private YandexIDLoginPage yandexIDLoginPage;

    @Autowired
    private SCTBranchesPage sctBranchesPage;

    @Autowired
    private BranchPage branchPage;

    @Autowired
    private HolidaysAndDaysOffPopUp holidaysAndDaysOffPopUp;

    @Test
    @DisplayName("Add new opening hours")
    public void updateSCTShopsOnYaMaps() {
        String morningTime = "07:00";
        String eveningTime = "22:00";
        String midnightTime = "00:00";

        sctBranchesPage.openOrgBranchesPage();
        yandexIDLoginPage.loginWithYaID();
        page.waitForTimeout(20000);
        sctBranchesPage.openOrgBranchesPage();
        int pageCount = sctBranchesPage.getPaginationBtns().count();
        System.out.printf("Page total count: %d\n", pageCount);
        int shopCount = 1;
        int pagination = 2;
        while (pageCount-- > 0) {
            var branchesEndpoints = sctBranchesPage.getAllBranchEndpointsFromPage();
            System.out.printf("Total endpoints count: %d\n", branchesEndpoints.size());
            for (var branchEndpoint : branchesEndpoints) {
                page.navigate(" https://yandex.ru" + branchEndpoint);
                branchPage.assertBranchPageLoaded();
                if (branchPage.isEnableNotificationPopUpVisible()) branchPage.closeEnableNotificationPopUp();

                System.out.printf("%d. Shop address: %s\n", shopCount, branchPage.getBranchAddress());
                var branchOpeningHours = branchPage.getBranchOpeningHours();
                System.out.printf("Opening hours: %s\n", branchOpeningHours);
                branchPage.clickOnShowAllDaysOffBtn();
//                page.waitForTimeout(3000);
                int counter;
                if (branchOpeningHours.contains("круглосуточно")) {
                    counter = 3;
                } else {
                    counter = 6;
                }
                while (counter-- > 0) {
                    holidaysAndDaysOffPopUp.clickOnAddNewDayBtn();
                }
                Locator dateInputs = holidaysAndDaysOffPopUp.getDateInputs();
                Locator dayOffCheckboxes = holidaysAndDaysOffPopUp.getDayOffCheckboxes();
                Locator openFromInputs = holidaysAndDaysOffPopUp.getOpenFromInputs();
                Locator openTillInputs = holidaysAndDaysOffPopUp.getOpenTillInputs();

                fillOutDateInput(dateInputs, 0, DEC_31.getDate());
                openTillInputs.nth(0).fill(eveningTime);
                setShopIsClosed(dateInputs, 1, JAN_01.getDate(), dayOffCheckboxes);
                fillOutDateInput(dateInputs, 2, JAN_02.getDate());
                openFromInputs.nth(2).fill(morningTime);
                if (branchOpeningHours.equals("круглосуточно")) {
                    openFromInputs.nth(0).fill(midnightTime);
                    openTillInputs.nth(2).fill(midnightTime);
                } else {
                    openFromInputs.nth(0).fill("08:00");
                    openTillInputs.nth(2).fill(eveningTime);
                    fillOutDateInput(dateInputs, 3, JAN_03.getDate());
                    fillOutTimeInput(openFromInputs, openTillInputs, 3, morningTime, eveningTime);
                    fillOutDateInput(dateInputs, 4, JAN_04.getDate());
                    fillOutTimeInput(openFromInputs, openTillInputs, 4, morningTime, eveningTime);
                    fillOutDateInput(dateInputs, 5, JAN_05.getDate());
                    fillOutTimeInput(openFromInputs, openTillInputs, 5, morningTime, eveningTime);
                }
                holidaysAndDaysOffPopUp.clickOnCloseIcon();
                branchPage.clickOnSaveChangesBtn();
                shopCount++;
            }
            sctBranchesPage.openOrgBranchesPageWithPageNumber(pagination);
            pagination++;
        }
    }

    @Test
    @DisplayName("Add new opening hours")
    public void updateSCTShopsOpeningHoursOnYaMaps() {
        String sevenAM = "07:00";
        String eightAM = "08:00";
        String eveningTime = "22:00";
        String midnightTime = "00:00";
        int daysQuantityToBeAdded = 2;

        sctBranchesPage.openOrgBranchesPage();
        yandexIDLoginPage.loginWithYaID();
        page.waitForTimeout(20000);
        sctBranchesPage.openOrgBranchesPage();
        int pageCount = sctBranchesPage.getPaginationBtns().count();
        System.out.printf("Page total count: %d\n", pageCount);
        int shopCount = 1;
        int pagination = 3;
        List<String> paginationNumbers = sctBranchesPage.getPaginationNumbers();
        int maxPagination = Integer.parseInt(paginationNumbers.get(paginationNumbers.size() - 1));
        while (pagination <= maxPagination) {
            while (pageCount-- >= 0) {
                var branchesEndpoints = sctBranchesPage.getAllBranchEndpointsFromPage();
                System.out.printf("Total endpoints count: %d\n", branchesEndpoints.size());
                for (var branchEndpoint : branchesEndpoints) {
                    page.navigate(" https://yandex.ru" + branchEndpoint);
                    branchPage.assertBranchPageLoaded();
                    page.waitForTimeout(2000);
                    if (branchPage.isEnableNotificationPopUpVisible()) branchPage.closeEnableNotificationPopUp();

                    System.out.printf("%d. Shop address: %s\n", shopCount, branchPage.getBranchAddress());
                    var branchOpeningHours = branchPage.getBranchOpeningHours();
                    System.out.printf("Opening hours: %s\n", branchOpeningHours);
                    page.waitForTimeout(2000);
                    if (branchOpeningHours.contains("круглосуточно")) {
                        shopCount++;
                        System.out.println("Skipping opening hours editing");
                        continue;
                    }
                    branchPage.clickOnShowAllDaysOffBtn();
                    for (int i = daysQuantityToBeAdded; i > 0; i--) {
                        holidaysAndDaysOffPopUp.clickOnAddNewDayBtn();
                    }

                    Locator dateInputs = holidaysAndDaysOffPopUp.getDateInputs();
                    int dateInputsCount = holidaysAndDaysOffPopUp.getDateInputs().count();
                    Locator openFromInputs = holidaysAndDaysOffPopUp.getOpenFromInputs();
                    int openFromInputsCount = holidaysAndDaysOffPopUp.getOpenFromInputs().count();
                    Locator openTillInputs = holidaysAndDaysOffPopUp.getOpenTillInputs();
                    int openTillInputsCount = holidaysAndDaysOffPopUp.getOpenTillInputs().count();

                    System.out.println("Setting new opening hours for " + AUG_31.getDate());
                    fillOutDateInput(dateInputs, dateInputsCount - 2, AUG_31.getDate());
                    openFromInputs.nth(openFromInputsCount - 2).fill(eightAM);
                    openTillInputs.nth(openTillInputsCount - 2).fill(midnightTime);

                    System.out.println("Setting new opening hours for " + SEPT_01.getDate());
                    fillOutDateInput(dateInputs, dateInputsCount - 1, SEPT_01.getDate());
                    openFromInputs.nth(openFromInputsCount - 1).fill(sevenAM);
                    openTillInputs.nth(openTillInputsCount - 1).fill(eveningTime);
                    page.waitForTimeout(2000);

                    holidaysAndDaysOffPopUp.clickOnCloseIcon();
                    branchPage.clickOnSaveChangesBtn();
                    shopCount++;
                }
                pagination++;
                sctBranchesPage.openOrgBranchesPageWithPageNumber(pagination);
            }
        }
    }

    private void fillOutDateInput(Locator dateInputs, int inputIndex, String date) {
        dateInputs.nth(inputIndex).clear();
        dateInputs.nth(inputIndex).fill(date);
    }

    private void setShopIsClosed(Locator dateInputs, int inputIndex, String date, Locator dayOffCheckboxes) {
        dateInputs.nth(inputIndex).clear();
        dateInputs.nth(inputIndex).fill(date);
        if (!dayOffCheckboxes.nth(inputIndex).isChecked()) {
            dayOffCheckboxes.nth(inputIndex).click();
        }
    }

    private void fillOutTimeInput(Locator openFromInputs, Locator openTillInputs, int inputIndex, String openFrom, String openTill) {
        openFromInputs.nth(inputIndex).fill(openFrom);
        openTillInputs.nth(inputIndex).fill(openTill);
    }
}
