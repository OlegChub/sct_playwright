package web.pages.ya;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class BranchPage {

    private Page page;

    public BranchPage(Page page) {
        this.page = page;
    }

    public BranchPage assertBranchPageLoaded() {
        page.waitForLoadState();
        System.out.println("Branch page is loaded");
        return this;
    }

    public String getBranchAddress() {
        return page.locator("//div[@class='sc-sidebar-ya-business-company-plate__geo-info']/div").textContent();
    }

    public String getBranchOpeningHours() {
        return page.locator("//span[text()='Время работы']/ancestor::span/input").getAttribute("value");
    }

    public BranchPage clickOnShowAllDaysOffBtn() {
        page.locator("//div[@id='holidays']/following-sibling::div/button").click();
        return this;
    }

    public BranchPage clickOnAddNewOpenHoursBtn() {
        getBtnWithName("Добавить режим работы").click();
        return this;
    }

    public BranchPage clickOnSaveChangesBtn() {
        getBtnWithName("Сохранить изменения").click();
        return this;
    }

    public boolean isEnableNotificationPopUpVisible() {
        return page.locator("div.Modal-Content").isVisible();
    }

    public BranchPage closeEnableNotificationPopUp() {
        page.click("span.Modal-Close");
        return this;
    }

    private Locator getBtnWithName(String name) {
        return page.locator(format("//span[text()='%s']/ancestor::button", name));
    }



}
