package web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public abstract class BasePage {

    @Value("${admin.page}")
    String mainPageUrl;

    @Autowired
    protected Page page;

    protected void navigateToMainPage() {
        navigateTo(mainPageUrl);
    }

    protected void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        page.navigate(url);
        page.waitForLoadState();
    }

    protected void refreshPage() {
        page.reload();
        page.waitForLoadState();
    }

    protected void clickOnElement(String locator) {
        getLocator(locator).click();
        page.waitForLoadState();
    }

    protected void doubleClickOnElement(String locator) {
        getLocator(locator).dblclick();
        page.waitForLoadState();
    }

    protected void doubleClickOnFirstElement(String locator) {
        getLocator(locator).first().dblclick();
        page.waitForLoadState();
    }

    protected void clickOnFirstElement(String locator) {
        getLocator(locator).first().click();
        page.waitForLoadState();
    }

    protected void pressKey(String locator, String key) {
        getLocator(locator).press(key);
    }

    protected void clearFieldAndFillText(String locator, String text) {
        clearField(locator);
        fillTextField(locator, text);
    }

    protected void fillTextField(String locator, String text) {
        getLocator(locator).fill(text);
    }

    protected void fillFirstTextField(String locator, String text) {
        getLocator(locator).first().fill(text);
    }

    protected void clearField(String locator) {
        getLocator(locator).clear();
    }

    protected String getPageTitle() {
        return page.title();
    }

    protected boolean isElementVisible(String locator) {
        Locator element = getLocator(locator);
        page.waitForLoadState();
        return element.isVisible();
    }

    protected void waitForTimeout(Double ms) {
        page.waitForTimeout(ms);
    }

    protected Locator getLocator(String selector) {
        return page.locator(selector);
    }

    protected Locator getLocatorByText(String selector) {
        return page.getByText(selector);
    }

    // Abstract method that specific pages must implement
    protected abstract String getPageUrl();
}
