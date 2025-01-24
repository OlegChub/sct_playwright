package web.pages.ya;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Component
public class EditProductModal {

    private Page page;
    private String priceInputselector = "span.ProductModal-Input_type_price input";

    public EditProductModal(Page page) {
        this.page = page;
    }

    public EditProductModal assertEditProductModalLoaded() {
        page.waitForLoadState();
        assertThat(page.locator("div.ProductModal-Title")).hasText("Редактировать товар");
        System.out.println("Edit goods modal loaded");
        return this;
    }

    public String getProductName() {
        String productName = page.locator("span.ProductModal-Input_type_title input").getAttribute("value");
        System.out.printf("Product name: %s\n", productName);
        return productName;
    }

    public String getProductPrice() {
        return page.inputValue(priceInputselector).replace(" ", "").trim();
    }

    public void setNewProductPrice(String newPrice) {
//        page.locator(priceInputselector).fill(newPrice);
        ElementHandle inputElement = page.querySelector(priceInputselector);
        inputElement.fill(newPrice);
        System.out.printf("Setting new price: %s\n", newPrice);
    }

    public EditProductModal clickOnSaveChangesBtn() {
        page.click("button.ProductModal-Button_type_submit");
        System.out.println("Clicked on 'Save changes' btn");
        return this;
    }

    public EditProductModal clickOnCancelChangesBtn() {
        page.click("button.ProductModal-Button_type_cancel");
        System.out.println("Clicked on 'Cancel changes' btn");
        return this;
    }

}
