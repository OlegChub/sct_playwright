import com.config.SctApplication;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import web.pages.ya.BranchPage;
import web.pages.ya.HolidaysAndDaysOffPopUp;
import web.pages.ya.SCTBranchesPage;
import web.pages.ya.YandexIDLoginPage;

import static enums.NewScheduleDates.*;
import static java.lang.Thread.sleep;

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
    @SneakyThrows
    @DisplayName("Add new opening hours")
    public void updateSCTShopsOnYaMaps() {
        String morningTime = "10:00";
        String eveningTime = "20:00";
        String midnightTime = "00:00";

        sctBranchesPage.openOrgBranchesPage();
        yandexIDLoginPage.loginWithYaID();
        sleep(20000);
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
//                sleep(3000);
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
