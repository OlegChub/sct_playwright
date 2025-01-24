package web.pages.ya;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public class SCTBranchesPage {

    @Value("${ya.org.url}")
    private String url;

    private Page page;
    private static final String BRANCHES_CONTENT = "div.ChainBranches-Content";

    public SCTBranchesPage(Page page) {
        this.page = page;
    }

    public SCTBranchesPage openOrgBranchesPage() {
        page.navigate(url);
        page.waitForLoadState();
        System.out.printf("Go to page: %s\n", url);
        return this;
    }

    public SCTBranchesPage openOrgBranchesPageWithPageNumber(int num) {
        String urlWithPage = url + format("?page=%d", num);
        page.navigate(urlWithPage);
        page.waitForLoadState();
        System.out.printf("Go to page: %s\n", urlWithPage);
        return this;
    }

    public SCTBranchesPage assertBranchesPageLoaded() {
        page.waitForLoadState();
        System.out.println("Org branches page is loaded");
        return this;
    }

    public List<String> getAllBranchEndpointsFromPage() {
        page.waitForLoadState();
        Locator elements = page.locator("//a[contains(@class,'CompanyRowInList-DataIcon_type_verification')]");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < elements.count(); i++) {
            String href = elements.nth(i).getAttribute("href");
            list.add(href.replace("?show-fill-profile=true", ""));
        }
        return list;
    }

    public List<String> buildFullBranchUrls(List<String> endpoints) {
        return endpoints.stream().map(el -> "https://yandex.ru" + el).collect(Collectors.toList());
    }

    public List<String> getNotUpdatedBranches() {
        page.waitForLoadState();
        Locator elements = page.locator("//a[contains(@class,'CompanyRowInList-DataIcon_type_verification') and not(contains(@class,'CompanyRowInList-DataIcon_active'))]");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < elements.count(); i++) {
            String href = elements.nth(i).getAttribute("href");
            list.add(href);
        }
        return list;
    }

    public List<String> getHrefAttributes(List<WebElement> list) {
        return list.stream().map(el -> el.getAttribute("href")).collect(Collectors.toList());
    }

    public SCTBranchesPage clickOnNextPageBtn() {
        page.locator("//span[text()='Вперед']/ancestor::button").click();
        return this;
    }

    public boolean isNextPageBtnVisible() {
        return page.locator("//span[text()='Вперед']/ancestor::button").isVisible();
    }

    public Locator getPaginationBtns() {
        page.waitForLoadState();
        return page.locator("//span[not(text()='Вперед')]/ancestor::button[@class='Button2 Pagination-Button']");
    }
}
