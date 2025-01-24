package web.pages.ya;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static java.lang.String.format;

@Component
public class GoodsAndServicesPage {

    private Page page;

    public GoodsAndServicesPage(Page page) {
        this.page = page;
    }

    public GoodsAndServicesPage openGoodsAndServicesMainPage() {
        page.navigate("https://yandex.ru/sprav/chain/7115973/price-lists");
        return this;
    }

    public void assertGoodsAndServicesPageLoaded() {
        page.waitForLoadState();
        assertThat(page.locator("h2.Heading")).hasText("Товары и услуги региона");
        System.out.println("Goods and Services page loaded");
    }

    public void assertRegionIsSelected(String regionName) {
        page.waitForLoadState();
        assertThat(page.locator("span.PriceListsPage-Button span.Button2-Text")).hasText(regionName);
        System.out.println("Selected region: " + regionName);
    }

    public GoodsAndServicesPage clickOnAllRegionsBtn() {
        page.waitForLoadState();
        page.click("//button[@role='listbox']");
        System.out.println("Clicked on 'All regions' btn");
        return this;
    }

    public GoodsAndServicesPage selectRegionByText(String regionName) {
        page.getByText(regionName).click();
        System.out.printf("Selected region: %s\n", regionName);
        return this;
    }

    public Locator getAllProductsLocators() {
        page.waitForLoadState();
        Locator productsLocator = page.locator("div.Product");
        System.out.printf("Products quantity: %d\n", productsLocator.count());
        return productsLocator;
    }

    public String getProductPrice(String productName) {
        page.waitForLoadState();
        String locator =
                format("//div[contains(@class, 'Product-Item_type_title') and normalize-space(text()) = '%s']/following-sibling::div[contains(@class, 'Product-Item_type_price')]", productName);
        String currentPrice = page.textContent(locator).replaceAll("[\\s₽]+", "").trim();
        System.out.printf("Current goods price: %s - %s\n", productName, currentPrice);
        page.waitForTimeout(3000);
        return currentPrice;
    }

    public String getProductItemNameFromProductsList(Locator productsLocators, int num) {
        String productName = productsLocators.nth(num).locator("div.Product-Item_type_title").textContent();
        System.out.printf("Product name: %s\n", productName);
        return productName;
    }

    public String getProductItemPriceFromProductsList(Locator productsLocators, int num) {
        String productPrice = productsLocators.nth(num).locator("div.Product-Item_type_price").textContent();
        String cleanedPrice = productPrice.replaceAll("[^\\d\\s]", "").trim();
        System.out.printf("Product price: %s\n", cleanedPrice);
        return cleanedPrice;
    }

    public List<String> getListWithAllProductsNames() {
        Locator productNameslocator = page.locator("div.Product-Item_type_title");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < productNameslocator.count(); i++) {
            list.add(productNameslocator.nth(i).textContent());
        }
        return list;
    }

    public List<String> getAllProductPrices() {
        Locator productPriceslocator = page.locator("div.Product-Item_type_price");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < productPriceslocator.count(); i++) {
            list.add(productPriceslocator.nth(i).textContent());
        }
        return list;
    }

    public Locator getPaginationLocators() {
        return page.locator("//button[@class='Button2 Pagination-Button']");
    }

    public GoodsAndServicesPage clickOnProductWithName(String name) {
        page.getByText(name).click();
        return this;
    }

    public GoodsAndServicesPage goToPageNumber(Locator paginationLocators, int pageNum) {
        paginationLocators.nth(pageNum).click();
        System.out.printf("Go to page #%d\n", pageNum);
        return this;
    }

    public boolean isDataIsActualBtnVisible() {
        page.waitForLoadState();
        System.out.println("Clicked on price list");
        return page.locator("button.PriceListsPage-ActualizeButton").isVisible();
    }

    public GoodsAndServicesPage clickOnDataIsActualBtn() {
        page.click("button.PriceListsPage-ActualizeButton");
        System.out.println("Clicked on 'Data is actual' btn");
        return this;
    }

}
