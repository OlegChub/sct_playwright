package web.pages.ya;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Component
public class ProfileNotFilledPopUp {

    private Page page;
    private String ProfileNotFilledItem = "//a[contains(@class,'company-fill-detailed-items') and contains(@href, '%s')]";

    public ProfileNotFilledPopUp(Page page) {
        this.page = page;
    }

    public ProfileNotFilledPopUp assertProfileNotFilledPopUpLoaded() {
        page.waitForLoadState();
        page.getByText("Профиль не заполнен").isVisible();
        System.out.println("Profile not filled pop-up is loaded");
        return this;
    }

    public ProfileNotFilledPopUp clickOnCheckPriceList() {
        page.click(format(ProfileNotFilledItem, "price-lists"));
        System.out.println("Clicked on price list");
        return this;
    }
}
