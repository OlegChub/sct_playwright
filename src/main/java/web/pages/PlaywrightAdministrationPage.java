package web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;


@Component
public class PlaywrightAdministrationPage {
    private final Page page;

    private static final String ADMIN_HEADER_BTN = "//a[text()='Администрирование']";
    private static final String searchField = "input[name='FIND']";
    private static final String flowersH1 = "//h1[contains(text(),'Цветы')]";

    private static final String ADMIN_PAGE_TITLE = "Авторизация - Аспро: Максимум - интернет-магазин";
    private static final String URL = "https://www.souzcvettorg.ru/bitrix/admin/iblock_element_admin.php?IBLOCK_ID=6&type=collection&lang=ru&find_section_section=-1#authorize";
    private static final String NEW_COLLECTION_URL = "https://www.souzcvettorg.ru/bitrix/admin/iblock_element_admin.php?IBLOCK_ID=6&type=collection&lang=ru&find_section_section=3806&SECTION_ID=3806";
    private static final String CATALOG_URL = "https://www.souzcvettorg.ru/bitrix/admin/iblock_section_admin.php?IBLOCK_ID=6&type=collection&lang=ru&find_section_section=531&SECTION_ID=531&apply_filter=Y";

    public PlaywrightAdministrationPage(Page page) {
        this.page = page;
    }

    public PlaywrightAdministrationPage openAdministrationPage() {
        page.navigate(URL);
        return this;
    }

    public PlaywrightAdministrationPage openNewCollectionSection() {
        page.navigate(NEW_COLLECTION_URL);
        return this;
    }

    public PlaywrightAdministrationPage openCatalogSection() {
        page.navigate(CATALOG_URL);
        return this;
    }

    public PlaywrightAdministrationPage openFlowersSection() {
        if (page.title().equals(ADMIN_PAGE_TITLE)) {
            this.clickOnFlowersSubMenuWithText("Цветы");
            page.waitForLoadState();
        } else {
            System.out.println("Already in Flowers section");
        }
        return this;
    }

    public PlaywrightAdministrationPage assertAdministrationPageIsDisplayed() {
        page.locator(ADMIN_HEADER_BTN).isVisible();
        return this;
    }

    public boolean assertFlowersSectionIsDisplayed() {
        return page.locator(flowersH1).isVisible();
    }

    public PlaywrightAdministrationPage clickOnLeftMenuItemWithText(String text) {
        page.locator(format("//div[@class='adm-main-menu-item-text' and text()='%s']/ancestor::span", text)).click();
        return this;
    }

    public PlaywrightAdministrationPage clickOnFlowersSubMenuWithText(String text) {
        page.locator(format("//div[@id='_global_menu_desktop']//span[text()='%s']", text)).click();
        System.out.printf("Clicking on flowers submenu: %s\n", text);
        return this;
    }

    public PlaywrightAdministrationPage searchItemByName(String searchText) {
        page.locator(searchField).clear();
        page.locator(searchField).fill(searchText);
        System.out.printf("...Searching for %s%n", searchText);
        return this;
    }

    public PlaywrightAdministrationPage clickSearchBtn() {
        page.locator("//span[@class='main-ui-item-icon main-ui-search']").click();
        return this;
    }

    public PlaywrightAdministrationPage assertSearchResultDisplayed(String name) {
        page.locator((format("(//a[@title='Перейти к редактированию' and contains(text(),'%s')])[1]", name))).isVisible();
        System.out.println("Result is displayed");
        return this;
    }

    public PlaywrightAdministrationPage searchByAlternativeCompositionItem(String itemName) {
        page.locator(searchField).click();
        String input = "input[name='PROPERTY_43']";
        page.locator(input).clear();
        page.locator(input).fill(itemName);
        page.locator("//button[contains(@class,'main-ui-filter-find')]").click();
        System.out.printf("...Searching for %s%n", itemName);
        return this;
    }

    public PlaywrightAdministrationPage searchBySymbolicCode(String itemName) {
        page.locator(searchField).click();
        String input = "input[name='CODE']";
        page.locator(input).clear();
        page.locator(input).fill(itemName);
        page.locator("//button[contains(@class,'main-ui-filter-find')]").click();
        System.out.printf("...Searching for %s%n", itemName);
        return this;
    }

    public Locator getCompositionCodeInputs() {
        return page.locator("//input[contains(@id,'[PROPERTY_217][n0]')]");

    }

    public Locator getCompositionQuantityInputs() {
        return page.locator("//input[contains(@name,'[PROPERTY_217][n0][DESCRIPTION]')]");
    }

    public Locator getAltCompositionNameInputs() {
        return page.locator("//input[contains(@name,'[PROPERTY_43][n0][VALUE]')]");
    }

    public Locator getNextPaginationNumbers() {
        return page.locator("//a[@class='main-ui-pagination-page']");
    }

    public PlaywrightAdministrationPage clickOnPaginationPageWithNumber(int num) {
        page.locator("//a[@class='main-ui-pagination-page']").scrollIntoViewIfNeeded();
        page.locator(format("(//a[@class='main-ui-pagination-page'])[%d]", num)).click();
        return this;
    }

    public PlaywrightAdministrationPage clickOnPaginationNext() {
        page.locator("//a[@class='main-ui-pagination-page']").scrollIntoViewIfNeeded();
        page.locator("//a[contains(@class,'main-ui-pagination-next')]").click();
        return this;
    }

    public int getCurrentPageNumber() {
        page.locator("//a[@class='main-ui-pagination-page']").scrollIntoViewIfNeeded();
        String currentPageNumber = page.locator("//span[contains(@class,'main-ui-pagination-active')]").textContent();
        return Integer.parseInt(currentPageNumber);
    }

    public int getTotalPageNumber() {
        var paginationList = page.locator("//a[@class='main-ui-pagination-page']");
        var paginationListSize = paginationList.count();
        String pagTotalNumber = paginationList.nth(paginationListSize - 1).textContent();
        return Integer.parseInt(pagTotalNumber);
    }

    public PlaywrightAdministrationPage selectItem() {
        page.locator("//tr[contains(@title,'Двойной щелчок - Редактировать элемент')]").click();
        return this;
    }

    public List<String> getElementNamesList(Locator list) {
        return list.allTextContents();
    }

    public Locator getNameWebElementsList() {
        return page.locator(
                "//td[4]//span[@class='main-grid-cell-content']/a[@title='Перейти к редактированию']");
    }

    public Locator getNameInputsList() {
        return page.locator("#NAME_control");
    }

    public Locator getDescriptionInputsList() {
        return page.locator("//textarea[contains(@name,'UF_POPULAR_CATEGORY_DESCR')]");
    }

    public PlaywrightAdministrationPage checkAll() {
        page.locator(flowersH1).scrollIntoViewIfNeeded();
        page.locator("//input[@type='checkbox' and contains(@title, 'Отметить все')]").click();
        return this;
    }

    public PlaywrightAdministrationPage checkItemToEdit() {
        page.locator("//input[contains(@id,'checkbox') and contains(@title,'Отметить для редактирования')]").click();
        return this;
    }

    public PlaywrightAdministrationPage clickOnActionDropdown() {
        page.locator("//span[contains(@id, 'base_action_select') and contains(@class,'main-dropdown')]").click();
        return this;
    }

    public PlaywrightAdministrationPage clickOnRemoveFromSection() {
        page.locator("//span[@data-value='asd_remove']").click();
        return this;
    }

    public PlaywrightAdministrationPage clickOnApplyBtn() {
        page.locator("//button[@id='apply_button_control']").click();
        return this;
    }

    public PlaywrightAdministrationPage clickOnSaveButton() {
        page.locator("#grid_save_button_control").click();
        return this;
    }

    public PlaywrightAdministrationPage clickOnCancelButton() {
        page.locator("#grid_cancel_button_control").click();
        return this;
    }

    public PlaywrightAdministrationPage editCheckedItems() {
        page.locator("#grid_edit_button_control").click();
        return this;
    }

    public PlaywrightAdministrationPage clickOnEditFirstItemBurgerMenu() {
        page.locator("//a[@class='main-grid-row-action-button']").click();
        return this;
    }

    public PlaywrightAdministrationPage clickOnBurgerMenuElementWithText(String text) {
        page.locator(format("//span[@class='menu-popup-item-text' and text()='%s']", text)).click();
        return this;
    }

    public PlaywrightAdministrationPage copyFirstItem() {
        page.locator("//a[@class='main-grid-row-action-button']").click();
        return selectPopUpMenuItem("Копировать");
    }

    public PlaywrightAdministrationPage copyElementWithName(String name) {
        page.locator(format("(//span[@class='main-grid-cell-content']/a[text()='%s']/ancestor::tr)[2]//a[@class='main-grid-row-action-button']", name)).click();
        page.locator("//span[@class='menu-popup-item-text' and text()='Копировать']").click();
        return this;
    }

    public PlaywrightAdministrationPage deactivateFirstItem() {
        page.locator((flowersH1)).scrollIntoViewIfNeeded();
        return selectPopUpMenuItem("Деактивировать");
    }

    public PlaywrightAdministrationPage selectPopUpMenuItem(String menuItem) {
        page.locator("//a[@class='main-grid-row-action-button']").click();
        page.locator(format("//span[@class='menu-popup-item-text' and text()='%s']", menuItem)).click();
        return this;
    }

}
