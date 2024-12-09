package web.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.springframework.stereotype.Component;

import static java.lang.String.format;


@Component
public class PlaywrightSearchElementPage {

    private final Page page;

    public PlaywrightSearchElementPage(Page page) {
        this.page = page;
    }

    public PlaywrightSearchElementPage findElementById(String elementId) {
        String startId = "//input[@name='filter_id_start']";
        String endId = "//input[@name='filter_id_end']";
        System.out.printf("Using element id: %s%n", elementId);
        page.locator(startId).clear();
        page.locator(startId).fill(elementId);
        page.locator(endId).clear();
        page.locator(endId).fill(elementId);
        page.locator("//input[@value='Найти']").click();
        return this;
    }

    public PlaywrightSearchElementPage findElementByName(String name) {
        String nameInput = "//input[@name='filter_name']";
        page.locator(nameInput).clear();
        page.locator(nameInput).fill(name);
        page.locator("//input[@value='Найти']").click();
        System.out.printf("... Searching for element: %s%n", name);
        return this;
    }

    public PlaywrightSearchElementPage selectFoundedElement() {
        page.locator("//input[@value='Выбрать']").click();
        System.out.println("Element has been selected");
        return this;
    }

    public PlaywrightSearchElementPage selectFirstElementUsingDoubleClick() {
        page.locator("(//tr[@class='adm-list-table-row'])[1]").dblclick();
        System.out.println("First element has been selected");
        return this;
    }

    public PlaywrightSearchElementPage selectElementWithNameUsingDoubleClick(String elementName) {
        page.locator(format("(//td[text()='%s'])", elementName)).first().dblclick();
        System.out.printf("%s has been selected\n", elementName);
        return this;
    }

    public void getTitleOfNewPage() {
        page.onPopup(newPage -> {
            // Interact with the new page
            newPage.waitForLoadState(LoadState.DOMCONTENTLOADED);
            System.out.println("Title of New Page: " + newPage.title());// Wait for the new page to load
            PlaywrightSearchElementPage searchElementPage1 = new PlaywrightSearchElementPage(newPage);
            newPage.close();
        });
    }
}
