import com.config.SctApplication;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import enums.Region;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utils.Exporter;
import utils.FileUtils;
import web.pages.PlaywrightAdminLoginPage;
import web.pages.PlaywrightAdministrationPage;
import web.pages.ya.EditProductModal;
import web.pages.ya.GoodsAndServicesPage;
import web.pages.ya.YandexIDLoginPage;

import java.util.*;


import static enums.Region.MOSCOW;
import static enums.Region.MOSCOW_REGION;
import static java.lang.Thread.sleep;

@SpringBootTest(classes = {SctApplication.class})
public class YaGoodsAndServices {

    private final String pathToProductsNamesCsv = "..\\sct_playwright\\src\\main\\resources\\outputs\\productsNames.csv";
    private final String pathToProductNamesAndPricesFromSCTXlsx = "..\\sct_playwright\\src\\main\\resources\\outputs\\productNamesAndPricesFromSCT.xlsx";

    @Autowired
    private Page page;

    @Autowired
    private YandexIDLoginPage yandexIDLoginPage;

    @Autowired
    private GoodsAndServicesPage goodsAndServicesPage;

    @Autowired
    private EditProductModal editProductModal;

    @Autowired
    private PlaywrightAdminLoginPage loginPage;

    @Autowired
    private PlaywrightAdministrationPage administrationPage;

    @Test
    @SneakyThrows
    @DisplayName("Get products information from yandex goods & services")
    public void getProductsInfoFromYaGoodsAndServices() {
        Map<String, String> productNamesAndPrices = new HashMap<>();
        List<Object> productsNames = new ArrayList<>();

        goToGoodsAndServicesPage(MOSCOW_REGION);

        Locator paginationLocators = goodsAndServicesPage.getPaginationLocators();
        int paginationLocatorsCount = paginationLocators.count();
        int productsCounter = 1;
        int nameMatching = 0;
        for (int i = 0; i <= paginationLocatorsCount; i++) {
            sleep(3000);
            Locator allProductsLocators = goodsAndServicesPage.getAllProductsLocators();
            for (int j = 0; j < allProductsLocators.count(); j++) {
                System.out.printf("%d. ", productsCounter);
                String productItemName = goodsAndServicesPage.getProductItemNameFromProductsList(allProductsLocators, j)
                        .trim();
                String productItemPrice = goodsAndServicesPage.getProductItemPriceFromProductsList(allProductsLocators, j)
                        .replaceAll("\\s", "").trim();
                productNamesAndPrices.put(productItemName, productItemPrice);
                if (productsNames.contains(productItemName)) {
                    nameMatching++;
                    System.out.println("Repeated products: " + productItemName);
                    continue;
                }
                productsNames.add(productItemName);
                productsCounter++;
            }
            if (i != paginationLocatorsCount) {
                goodsAndServicesPage.goToPageNumber(paginationLocators, i);
            }
        }
        System.out.println("Map size: " + productNamesAndPrices.size());
        System.out.println("List size: " + productsNames.size());
        System.out.println("Repeated products count: " + nameMatching);
        Exporter.exportMapToExcel(productNamesAndPrices, "productNamesAndPricesFromYa");
//        Exporter.exportListToExcel(productsNames, "productsNames");
        Exporter.listToCSV(productsNames, "productsNames");
//        Helper.printMapAsMapOf(productNamesAndPrices);
//        System.out.println("List with products names: ");
//        Helper.printListAsArraysAsList(productsNames);

//        sleep(5000);
//        String productNameFromList = productsNames.get(1);
//        goodsAndServicesPage.clickOnProductWithName(productNameFromList);
//        String productNameActual = editProductModal.assertEditProductModalLoaded().getProductName();
//        assertThat(productNameFromList).isEqualTo(productNameActual);
//        editProductModal.setNewProductPrice(testPrice);
//        sleep(25000);
    }

    @Test
    @DisplayName("Get products prices from sct website")
    public void getProductPricesFromSCT() {
//        String pathToProductsNamesCsv = "..\\sct\\src\\main\\resources\\outputs\\productsNames.csv"; // Path to CSV file with bouquets names
        // Path to CSV file with bouquets names
        List<String> bouquetsNames = FileUtils.readCsv(pathToProductsNamesCsv);
        Map<String, String> productNamesAndPrices = new HashMap<>();
        int count = 0;

        loginPage.login();
        administrationPage
                .assertAdministrationPageIsDisplayed()
                .openFlowersSection();
        for (var bouquet : bouquetsNames) {
            System.out.println("Count number: " + count);
            administrationPage
                    .searchItemByName(bouquet)
                    .clickSearchBtn();
            page.waitForTimeout(1500);
            waitTillLoadingIsFinished();
            checkSearchResult();
            administrationPage.checkAll();
            administrationPage.editCheckedItems();
            page.waitForTimeout(1500);
            waitTillLoadingIsFinished();
            productNamesAndPrices.put(bouquet, proceedPrice(administrationPage.getBouquetPrice()));
            System.out.printf("Added item: %s - %s\n", bouquet, productNamesAndPrices.get(bouquet));
            count++;
        }
        Exporter.exportMapToExcel(productNamesAndPrices, "productNamesAndPricesFromSCT");
    }

    @Test
    @DisplayName("Update products prices to yandex goods & services")
    public void updateProductsPricesToYaGoodsAndServices() {
        List<String> productsNames = FileUtils.readCsv(pathToProductsNamesCsv);
        Map<String, String> productNamesAndPrices = FileUtils.readExcelFileWithTwoColumns(1, pathToProductNamesAndPricesFromSCTXlsx);
        int productsNamesQuantity = productsNames.size();
        int productsNamesAndPricesQuantity = productNamesAndPrices.size();
        System.out.println("List elements number: " + productsNamesQuantity);
        System.out.println("Map elements number: " + productsNamesAndPricesQuantity);
//        if (productsNamesQuantity != productsNamesAndPricesQuantity) {
//            throw new RuntimeException("productsNames and productNamesAndPrices are not equal");
//        }
        productNamesAndPrices.forEach((key, value) -> System.out.printf("%s - %s\n", key, value));

//        goToGoodsAndServicesPage(MOSCOW_REGION);
        goToGoodsAndServicesPage(MOSCOW);
        Locator paginationLocators = goodsAndServicesPage.getPaginationLocators();
        int pagination = 0;
        int productsCounter = 1;
        for (String product : productsNames) {
            System.out.println("Products counter: " + productsCounter);
            goodsAndServicesPage.assertGoodsAndServicesPageLoaded();
            String currentProductPrice = goodsAndServicesPage.getProductPrice(product);
            String newProductPrice = productNamesAndPrices.get(product);
            if (!currentProductPrice.equals(newProductPrice)) {
                goodsAndServicesPage.clickOnProductWithName(product);
//                page.waitForTimeout(3000);
                editProductModal.assertEditProductModalLoaded();
                System.out.printf("New product price: %s\n", newProductPrice);
                editProductModal.setNewProductPrice(newProductPrice);
                page.waitForTimeout(5000);
                editProductModal.clickOnCancelChangesBtn();
//                editProductModal.clickOnSaveChangesBtn();
            } else {
                System.out.printf("Price on item %s has not changed: %s=%s\n", product, currentProductPrice, newProductPrice);
            }
            productsCounter++;
            if (productsCounter % 30 == 0) {
                goodsAndServicesPage.goToPageNumber(paginationLocators, pagination);
                pagination++;
            }
        }

    }

    private void goToGoodsAndServicesPage(Region region) {
        goodsAndServicesPage.openGoodsAndServicesMainPage();
        yandexIDLoginPage.loginWithYaID();
        page.waitForTimeout(5000);
        goodsAndServicesPage.assertGoodsAndServicesPageLoaded();
        goodsAndServicesPage
                .clickOnAllRegionsBtn()
                .selectRegionByText(region.getRegion())
                .assertGoodsAndServicesPageLoaded();
        goodsAndServicesPage.assertRegionIsSelected(region.getRegion());
    }

    private void waitTillLoadingIsFinished() {
        var isSavingInProgress = administrationPage.isLoaderDisplayed();
        while (isSavingInProgress) {
            page.waitForTimeout(500);
            isSavingInProgress = administrationPage.isLoaderDisplayed();
        }
    }

    private String proceedPrice(String price) {
        return price.replace(".00", "");
    }

    private void checkSearchResult() {
        if (Integer.parseInt(administrationPage.getSearchResultsQuantity()) > 1) {
            throw new RuntimeException("Found more than 2 products");
        }
    }
}
