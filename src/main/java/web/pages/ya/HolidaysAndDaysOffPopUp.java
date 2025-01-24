package web.pages.ya;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Component
public class HolidaysAndDaysOffPopUp {

    private Page page;
    private static final String HolidaysAndDaysOffPopUpTitle = "//div[@class='InfoModal-Title' and contains(text(),'нерабочие дни')]";

    public HolidaysAndDaysOffPopUp(Page page) {
        this.page = page;
    }

    public HolidaysAndDaysOffPopUp assertHolidaysAndDaysOffPopUpLoaded() {
        page.waitForLoadState();
        System.out.println("Holidays and days off pop-up is loaded");
        return this;
    }

    public HolidaysAndDaysOffPopUp clickOnAddNewDayBtn() {
        getBtnWithName("Добавить день").click();
        return this;
    }

    public Locator getDateInputs() {
        return page.locator("//input[@type='tel']");
    }

    public List<Integer> getIndexesOfDateInputsWithDate(Locator elements, String date) {
        List<Integer> intList = new ArrayList<>();
        for (int i = 0; i < elements.count(); i++) {
            if (elements.nth(i).getAttribute("value").equals(date)) {
                intList.add(i);
            }
        }
        return intList;
    }

    public Locator getOpenFromInputs() {
//        return getDayOpenHoursInputs("Открыто с");
        return page.locator("//span[text()='Открыто с']/ancestor::span/input");
    }

    public Locator getOpenTillInputs() {
//        return getDayOpenHoursInputs("Открыто до");
        return page.locator("//span[text()='Открыто до']/ancestor::span/input");

    }

    public Locator getDayOffCheckboxes() {
        return page.locator("//label[contains(@class,'ya-business-checkbox') and contains(@for,'holidaysCheckbox')]");
    }

    public HolidaysAndDaysOffPopUp clickOnCloseIcon() {
        page.locator("//div[contains(text(),'нерабочие дни')]/following-sibling::div").click();
        return this;
    }

    public HolidaysAndDaysOffPopUp clickOnSaveChangesBtn() {
        getBtnWithName("Сохранить изменения").click();
        return this;
    }

    private Locator getBtnWithName(String name) {
        return page.locator(format("//span[text()='%s']/ancestor::button", name));
    }

    private Locator getDayOpenHoursInputs(String name) {
        return page.locator(format("//span[text()=%s]/ancestor::span/input", name));
    }

}
