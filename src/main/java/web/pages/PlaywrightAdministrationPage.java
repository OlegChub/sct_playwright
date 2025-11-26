package web.pages;

import com.microsoft.playwright.Locator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED;
import static java.lang.String.format;


@Component
@Slf4j
public class PlaywrightAdministrationPage extends BasePage {

    private static final String ADMIN_HEADER_BTN = "//a[text()='Администрирование']";
    private static final String searchField = "input[name='FIND']";
    private static final String flowersH1 = "//h1[contains(text(),'Цветы')]";

    private static final String ADMIN_PAGE_TITLE = "Авторизация - Аспро: Максимум - интернет-магазин";
    private static final String URL = "https://www.souzcvettorg.ru/bitrix/admin/iblock_element_admin.php?IBLOCK_ID=6&type=collection&lang=ru&find_section_section=-1#authorize";
    private static final String NEW_COLLECTION_URL = "https://www.souzcvettorg.ru/bitrix/admin/iblock_element_admin.php?IBLOCK_ID=6&type=collection&lang=ru&find_section_section=3806&SECTION_ID=3806";
    private static final String CATALOG_URL = "https://www.souzcvettorg.ru/bitrix/admin/iblock_section_admin.php?IBLOCK_ID=6&type=collection&lang=ru&find_section_section=531&SECTION_ID=531&apply_filter=Y";
    private static final String bouquetAssemblyTimeField = "(//span[@class='main-grid-cell-content']/a[text()='%s']/ancestor::tr)[2]//input[contains(@name,'[PROPERTY_232]')]";
    private static final String errorWhileSavingPopUp = "//div[contains(@id,'popup-window-content')]//div[contains(text(),'Ошибка сохранения записи')]";

    public PlaywrightAdministrationPage openAdministrationPage() {
        navigateTo(URL);
        return this;
    }

    public PlaywrightAdministrationPage openNewCollectionSection() {
        navigateTo(NEW_COLLECTION_URL);
        return this;
    }

    public PlaywrightAdministrationPage openCatalogSection() {
        navigateTo(CATALOG_URL);
        return this;
    }

    public PlaywrightAdministrationPage openFlowersSection() {
        if (getPageTitle().equals(ADMIN_PAGE_TITLE)) {
            this.clickOnFlowersSubMenuWithText("Цветы");
            page.waitForLoadState();
        } else {
            System.out.println("Already in Flowers section");
        }
        return this;
    }

    public PlaywrightAdministrationPage assertAdministrationPageIsDisplayed() {
        isElementVisible(ADMIN_HEADER_BTN);
        return this;
    }

    public boolean assertFlowersSectionIsDisplayed() {
        return isElementVisible(flowersH1);
    }

    public PlaywrightAdministrationPage clickOnLeftMenuItemWithText(String text) {
        clickOnElement(format("//div[@class='adm-main-menu-item-text' and text()='%s']/ancestor::span", text));
        return this;
    }

    public PlaywrightAdministrationPage clickOnFlowersSubMenuWithText(String text) {
        clickOnElement(format("//div[@id='_global_menu_desktop']//span[text()='%s']", text));
        System.out.printf("Clicking on flowers submenu: %s\n", text);
        log.info("Clicking on flowers submenu: {} \n", text);
        return this;
    }

    public PlaywrightAdministrationPage searchItemByName(String searchText) {
        clearFieldAndFillText(searchField, searchText);
        System.out.printf("...Searching for %s%n", searchText);
        log.info("...Searching for {} %n", searchText);
        return this;
    }

    public PlaywrightAdministrationPage pressEnterOnSearchField() {
        pressKey(searchField, "Enter");
        return this;
    }

    @SneakyThrows
    public PlaywrightAdministrationPage clickSearchBtn() {
        clickOnElement("//span[@class='main-ui-item-icon main-ui-search']");
        return this;
    }

    public String getSearchResultsQuantity() {
        String totalItemsFound = "//span[contains(text(),'Всего:')]/following-sibling::span[contains(@class,'content-text')]";
        System.out.println("Items found: " + totalItemsFound);
        log.info("Items found: {}", totalItemsFound);
        return page.textContent(totalItemsFound);
    }

    public PlaywrightAdministrationPage assertSearchResultDisplayed(String name) {
        isElementVisible(format("(//a[@title='Перейти к редактированию' and contains(text(),'%s')])[1]", name));
        System.out.println("Result is displayed");
        log.info("Result is displayed");
        return this;
    }

    public PlaywrightAdministrationPage searchByAlternativeCompositionItem(String itemName) {
        clickOnElement(searchField);
        String input = "input[name='PROPERTY_43']";
        clearFieldAndFillText(input, itemName);
        clickOnElement("//button[contains(@class,'main-ui-filter-find')]");
        System.out.printf("...Searching for %s%n", itemName);
        log.info("...Searching for {}%n", itemName);
        return this;
    }

    public PlaywrightAdministrationPage searchBySymbolicCode(String itemName) {
        clickOnElement(searchField);
        String input = "input[name='CODE']";
        clearFieldAndFillText(input, itemName);
        clickOnElement("//button[contains(@class,'main-ui-filter-find')]");
        System.out.printf("...Searching for %s%n", itemName);
        log.info("...Searching for {}%n", itemName);
        return this;
    }

    public Locator getCompositionCodeInputs() {
        return getLocator("//input[contains(@id,'[PROPERTY_217][n0]')]");

    }

    public Locator getCompositionQuantityInputs() {
        return getLocator("//input[contains(@name,'[PROPERTY_217][n0][DESCRIPTION]')]");
    }

    public Locator getAltCompositionNameInputs() {
        return getLocator("//input[contains(@name,'[PROPERTY_43][n0][VALUE]')]");
    }

    public Locator getNextPaginationNumbers() {
        return getLocator("//a[@class='main-ui-pagination-page']");
    }

    public PlaywrightAdministrationPage clickOnPaginationPageWithNumber(int num) {
        getLocator("//a[@class='main-ui-pagination-page']").scrollIntoViewIfNeeded();
        clickOnElement(format("(//a[@class='main-ui-pagination-page'])[%d]", num));
        return this;
    }

    public PlaywrightAdministrationPage clickOnPaginationNext() {
        page.locator("//a[@class='main-ui-pagination-page']").scrollIntoViewIfNeeded();
        clickOnElement("//a[contains(@class,'main-ui-pagination-next')]");
        return this;
    }

    public int getCurrentPageNumber() {
        page.locator("//a[@class='main-ui-pagination-page']").scrollIntoViewIfNeeded();
        String currentPageNumber = getLocator("//span[contains(@class,'main-ui-pagination-active')]").textContent();
        return Integer.parseInt(currentPageNumber);
    }

    public int getTotalPageNumber() {
        var paginationList = getLocator("//a[@class='main-ui-pagination-page']");
        var paginationListSize = paginationList.count();
        String pagTotalNumber = paginationList.nth(paginationListSize - 1).textContent();
        return Integer.parseInt(pagTotalNumber);
    }

    public PlaywrightAdministrationPage selectItem() {
        clickOnElement("//tr[contains(@title,'Двойной щелчок - Редактировать элемент')]");
        return this;
    }

    public List<String> getElementNamesList(Locator list) {
        return list.allTextContents();
    }

    public Locator getNameWebElementsList() {
        return getLocator("//td[4]//span[@class='main-grid-cell-content']/a[@title='Перейти к редактированию']");
    }

    public Locator getNameInputsList() {
        return getLocator("#NAME_control");
    }

    public Locator getDescriptionInputsList() {
        return getLocator("//textarea[contains(@name,'UF_POPULAR_CATEGORY_DESCR')]");
    }

    public PlaywrightAdministrationPage checkAll() {
        getLocator("//input[@type='checkbox' and contains(@title, 'Отметить все')]").first().click();
        return this;
    }

    public PlaywrightAdministrationPage selectProductWithName(String productName) {
        clickOnElement(format("//a[text()='%s']/ancestor::tr[contains(@class,'main-grid-row-body')]", productName));
        return this;
    }

    public PlaywrightAdministrationPage clickOnActionsBtn() {
        System.out.println("Click on Actions btn");
        log.info("Click on Actions btn");
        clickOnFirstElement("//span[contains(@id,'base_action_select_tbl_iblock')]");
        return this;
    }

    public PlaywrightAdministrationPage selectAction(String actionName) {
        System.out.println("Select action: " + actionName);
        log.info("Select action: {}", actionName);
        getLocatorByText(actionName).click();
        return this;
    }

    public PlaywrightAdministrationPage clickOnSectionsBtn() {
        clickOnElement("//span[contains(@id,'iblock_grid_action_tbl_iblock_element')]");
        return this;
    }

    public PlaywrightAdministrationPage selectSection(String sectionName) {
        System.out.println("Select section: " + sectionName);
        getLocatorByText(sectionName).click();
        return this;
    }

    public PlaywrightAdministrationPage checkItemToEdit() {
        clickOnElement("//input[contains(@id,'checkbox') and contains(@title,'Отметить для редактирования')]");
        return this;
    }

    public PlaywrightAdministrationPage checkItemWithNameToEdit(String itemName) {
        clickOnElement(format("(//span[@class='main-grid-cell-content']/a[text()='%s']/ancestor::tr)[2]//input[contains(@id,'checkbox')]", itemName));
        return this;
    }

    public PlaywrightAdministrationPage clickOnActionDropdown() {
        clickOnElement("//span[contains(@id, 'base_action_select') and contains(@class,'main-dropdown')]");
        return this;
    }

    public PlaywrightAdministrationPage clickOnRemoveFromSection() {
        clickOnElement("//span[@data-value='asd_remove']");
        return this;
    }

    public PlaywrightAdministrationPage clickOnApplyBtn() {
        clickOnElement("//button[@id='apply_button_control']");
        System.out.println("Applying changes...");
        log.info("Applying changes...");
        return this;
    }

    @SneakyThrows
    public PlaywrightAdministrationPage clickOnSaveButton() {
        clickOnElement("#grid_save_button_control");
        System.out.println("Saving changes...");
        log.info("Saving changes...");
        return this;
    }

    public PlaywrightAdministrationPage clickOnCancelButton() {
        clickOnElement("#grid_cancel_button_control");
        return this;
    }

    public PlaywrightAdministrationPage editCheckedItems() {
        clickOnElement("#grid_edit_button_control");
        System.out.println("Editing mode ON");
        log.info("Editing mode ON");
        return this;
    }

    public PlaywrightAdministrationPage replaceComposition(String oldId, String newId) {
        var locatorPattern = "//td//input[contains(@name,'PROPERTY_217') and @value='%s']";
        clearFieldAndFillText(format(locatorPattern, oldId), newId);
        System.out.println("Substituting component ID");
        log.info("Substituting component ID");
        return this;
    }

    public boolean isEditMenuBarDisplayed() {
        System.out.println("Checking Edit button is displayed");
        log.info("Checking Edit button is displayed");
        return isElementVisible("main-grid-control-panel-row");
    }

    public PlaywrightAdministrationPage clickOnEditFirstItemBurgerMenu() {
        clickOnElement("//a[@class='main-grid-row-action-button']");
        return this;
    }

    public PlaywrightAdministrationPage clickOnBurgerMenuElementWithText(String text) {
        clickOnElement(format("//span[@class='menu-popup-item-text' and text()='%s']", text));
        return this;
    }

    public PlaywrightAdministrationPage copyFirstItem() {
        clickOnElement("//a[@class='main-grid-row-action-button']");
        return selectPopUpMenuItem("Копировать");
    }

    public PlaywrightAdministrationPage copyElementWithName(String name) {
        clickOnElement(format("(//span[@class='main-grid-cell-content']/a[text()='%s']/ancestor::tr)[2]//a[@class='main-grid-row-action-button']", name));
        clickOnElement("//span[@class='menu-popup-item-text' and text()='Копировать']");
        return this;
    }

    public PlaywrightAdministrationPage setBouquetAssemblyTime(String bouquetName, String data) {
        clearFieldAndFillText(bouquetAssemblyTimeField, bouquetName);
        return this;
    }

    public PlaywrightAdministrationPage setBouquetAssemblyTime(String data) {
        page.waitForLoadState(DOMCONTENTLOADED);
        String timeField = "//input[contains(@name,'[PROPERTY_232]')]";
        clearFieldAndFillText(timeField,data);
        System.out.println("Setting assembly time...");
        return this;
    }

    public String getBouquetPrice() {
        page.waitForLoadState(DOMCONTENTLOADED);
        System.out.println("Getting price ...");
        return page.inputValue("//input[contains(@name,'CATALOG_PRICE') and contains(@name,'[1]') and @type='number']");
    }

    public PlaywrightAdministrationPage deactivateFirstItem() {
        page.locator((flowersH1)).scrollIntoViewIfNeeded();
        return selectPopUpMenuItem("Деактивировать");
    }

    public boolean isErrorWhileSavingPopupDisplayed() {
        return page.locator(errorWhileSavingPopUp).first().isVisible();
    }

    public boolean isNothingFoundDisplayed() {
        return isElementVisible("//div[text()='Ничего не найдено']");
    }

    public boolean isLoaderDisplayed() {
        return isElementVisible("main-ui-loader main-ui-show");
    }

    public PlaywrightAdministrationPage closeErrorWhileSavingPopup() {
        clickOnElement("//span[text()='Закрыть']");
        return this;
    }

    public PlaywrightAdministrationPage selectPopUpMenuItem(String menuItem) {
        clickOnElement("//a[@class='main-grid-row-action-button']");
        clickOnElement(format("//span[@class='menu-popup-item-text' and text()='%s']", menuItem));
        return this;
    }

    @Override
    protected String getPageUrl() {
        return URL;
    }
}
