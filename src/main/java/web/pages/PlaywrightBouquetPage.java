package web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.Utils;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;


@Component
public class PlaywrightBouquetPage {
    private final Page page;

    private final String activityCheckbox = "//input[@name='ACTIVE' and @type='checkbox']";
    private final String alternativeCompositionItemInput = "//input[contains(@name,'PROP[43]')]";
    private final String compositionItemCode = "//input[contains(@id,'PROP[217]') and contains(@name,'VALUE')]";
    private final String compositionItemQuantity = "//span[@title='']/input";
    private final String saveInProgressSign = "//div[@class='adm-btn-load-img-green']";

    public PlaywrightBouquetPage(Page page) {
        this.page = page;
    }

    public PlaywrightBouquetPage assertBouquetPageDisplayed() {
        page.locator("//h1[@id='adm-title' and contains(text(),'Элемент: Добавление')]")
                .isVisible();
        return this;
    }

    public PlaywrightBouquetPage setBouquetName(String bouquetName) {
        String bouquetNameInput = "input[id='NAME']";
        page.locator(bouquetNameInput).clear();
        page.locator(bouquetNameInput).fill(bouquetName);
        return this;
    }

    public PlaywrightBouquetPage setBouquetSortingIndex() {
        String bouquetSortingInput = "input[name='SORT']";
        page.locator(bouquetSortingInput).clear();
        page.locator(bouquetSortingInput).fill(String.valueOf(Utils.generateRandomNumber()));
        return this;
    }

    public Locator compositionItemQuantityInputs() {
        return page.locator(compositionItemQuantity);
    }

    public PlaywrightBouquetPage setCompositionItemQuantity(String quantity) {
        page.locator(compositionItemQuantity).clear();
        page.locator(compositionItemQuantity).fill(quantity);
        return this;

    }

    public List<String> getCompositionItemNames() {
        return page.locator("//td//span[contains(@id,'sp_a7b07270b2fe491d006b9a26d689340c')]").allTextContents();
    }

    public Locator getAlternativeCompositionItemInputs() {
        return page.locator("//input[contains(@name,'PROP[43]')]");
    }

    public PlaywrightBouquetPage setAlternativeCompositionItem(String itemName) {
        page.locator(alternativeCompositionItemInput).clear();
        page.locator(alternativeCompositionItemInput).fill(itemName);
        return this;
    }

    public Locator getCompositionItemCodeInputs() {
        return page.locator(compositionItemCode);
    }

    public PlaywrightBouquetPage setCompositionItemCode(String itemCode) {
        page.locator(compositionItemCode).clear();
        page.locator(compositionItemCode).fill(itemCode);
        return this;
    }

    public PlaywrightBouquetPage uploadAdditionalPictures(Path pathToImage) {
        page.locator("//input[@type='file' and contains(@id,'bx_file_prop_51')]").setInputFiles(pathToImage);
        return this;
    }

    public PlaywrightBouquetPage uploadDetailPicture(Path pathToImage) {
        page.locator("//input[@id='bx_file_detail_picture_input']").setInputFiles(pathToImage);
        return this;
    }

    public PlaywrightBouquetPage addDetailedDescription(String text) {
        String descriptionField = "//textarea[@id='bxed_DETAIL_TEXT']";
        page.locator(descriptionField).clear();
        page.locator(descriptionField).fill(text);
        return this;
    }

    public PlaywrightBouquetPage clickOnInDetailTab() {
        page.locator("//span[@id='tab_cont_edit6']").click();
        return this;
    }

    public PlaywrightBouquetPage clickOnSectionsTab() {
        page.locator("//span[@id='tab_cont_edit2']").click();
        return this;
    }

    public PlaywrightBouquetPage clickOnTradeCatalogTab() {
        page.locator("//span[@id='tab_cont_edit10']").click();
        return this;
    }

    public PlaywrightBouquetPage clickOnParametersTab() {
        page.locator("//span[@id='view_tab_cat_edit3']").click();
        return this;
    }

    public PlaywrightBouquetPage setDefaultGoodsQuantity() {
        String quantityInput ="//input[@id='CAT_BASE_QUANTITY']";
        page.locator(quantityInput).clear();
        page.locator(quantityInput).fill(String.valueOf(999));
        return this;
    }

    public PlaywrightBouquetPage selectSectionByValue(String partialValue) {
//        Select select = new Select(page.locator("//select[@name='IBLOCK_SECTION[]']")));
//        Locator options = select.getOptions();
//        for (WebElement option : options) {
//            if (option.getText().contains(partialValue)) {
//                select.selectByVisibleText(option.getText());
//                System.out.println("Selected: " + option.getText());
//                break; // Exit the loop after selecting the first match
//            }
//        }
        return this;
    }

    @SneakyThrows
    public void addAlternativeCompositionItemInput(int currentInputsQuantity, int elementsToBeAddedQuantity) {
        int dif = currentInputsQuantity - elementsToBeAddedQuantity;
        Locator addBtn = page.locator("//tr[@id='tr_PROPERTY_43']");
        if (dif < 0) {
            int i = Math.abs(dif);
            while (i-- > 0) {
                addBtn.scrollIntoViewIfNeeded();
                sleep(500);
                page.locator("//tr[@id='tr_PROPERTY_43']//input[@value='Добавить']").click();
            }
        }
    }

    @SneakyThrows
    public void addCompositionItemInputs(int currentInputsQuantity, int elementsToBeAddedQuantity) {
        int dif = currentInputsQuantity - elementsToBeAddedQuantity;
        Locator addBtn = page.locator("//tr[@id='tr_PROPERTY_217']");
        if (dif < 0) {
            int i = Math.abs(dif);
            while (i-- > 0) {
                addBtn.scrollIntoViewIfNeeded();
                sleep(500);
                page.locator("//tr[@id='tr_PROPERTY_217']//input[@value='Добавить...']").click();
            }
        }
    }

    public PlaywrightBouquetPage addCompositionItemInput() {
        page.locator("//tr[@id='tr_PROPERTY_217']").scrollIntoViewIfNeeded();
        page.locator("//tr[@id='tr_PROPERTY_217']//input[@value='Добавить...']").click();
        return this;
    }

    public PlaywrightBouquetPage clearCodeInputOfFirstCompositionItem() {
        page.locator("//tr[@id='tr_PROPERTY_217']").scrollIntoViewIfNeeded();
        page.locator("//input[@value='239490']").clear();
        return this;
    }


    public void clickOnApplyBtn() {
        page.locator("//input[@id='apply']").click();
    }

    public void clickOnSaveBtn() {
        page.locator("//input[@id='save']").click();
        System.out.println("...Saving changes");
    }

    public void clickOnCancelBtn() {
        page.locator("//input[@id='dontsave']").click();
    }
}
