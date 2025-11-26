package web.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Slf4j
@Component
public class PlaywrightSearchElementPage extends BasePage {

    public PlaywrightSearchElementPage(Page page) {
        super.page = page;
    }

    public PlaywrightSearchElementPage findElementById(String elementId) {
        String startId = "//input[@name='filter_id_start']";
        String endId = "//input[@name='filter_id_end']";
        System.out.printf("Using element id: %s%n", elementId);
        log.info("Using element id: {}%n", elementId);
        clearFieldAndFillText(startId, elementId);
        clearFieldAndFillText(endId, elementId);
        clickOnElement("//input[@value='Найти']");
        return this;
    }

    public PlaywrightSearchElementPage findElementByName(String name) {
        String nameInput = "//input[@name='filter_name']";
        clearFieldAndFillText(nameInput, name);
        clickOnElement("//input[@value='Найти']");
        System.out.printf("... Searching for element: %s%n", name);
        log.info("... Searching for element: {}%n", name);
        return this;
    }

    public PlaywrightSearchElementPage selectFoundedElement() {
        clickOnElement("//input[@value='Выбрать']");
        System.out.println("Element has been selected");
        return this;
    }

    public PlaywrightSearchElementPage selectFirstElementUsingDoubleClick() {
        doubleClickOnElement("(//tr[@class='adm-list-table-row'])[1]");
        System.out.println("First element has been selected");
        return this;
    }

    public PlaywrightSearchElementPage selectElementWithNameUsingDoubleClick(String elementName) {
        doubleClickOnFirstElement(format("(//td[text()='%s'])", elementName));
        System.out.printf("%s has been selected\n", elementName);
        return this;
    }

    public void getTitleOfNewPage() {
        page.onPopup(newPage -> {
            // Interact with the new page
            newPage.waitForLoadState(LoadState.DOMCONTENTLOADED);
            System.out.println("Title of New Page: " + newPage.title());// Wait for the new page to load
//            PlaywrightSearchElementPage searchElementPage1 = new PlaywrightSearchElementPage(newPage);
            newPage.close();
        });
    }

    @Override
    protected String getPageUrl() {
        return "";
    }
}
