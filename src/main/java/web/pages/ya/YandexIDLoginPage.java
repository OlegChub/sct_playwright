package web.pages.ya;

import com.microsoft.playwright.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class YandexIDLoginPage {

    @Value("${ya.login}")
    private String userName;
    @Value("${ya.pass}")
    private String password;

    private Page page;
    private static final String USERNAME_INPUT = "#passp-field-login";
    private static final String PASSWORD_INPUT = "#passp-field-passwd";
    private static final String ENTER_BTN = "button#passp\\:sign-in";

    public YandexIDLoginPage(Page page) {
        this.page = page;
    }

    public YandexIDLoginPage assertYaIDFormLoaded() {
        page.locator("//form[@data-t='page:form']").isVisible();
        System.out.println("Yandex ID form is loaded");
        return this;
    }

    public YandexIDLoginPage insertUserName() {
        page.locator(USERNAME_INPUT).fill(userName);
        System.out.printf("Login %s was sent", userName);
        return this;
    }

    public YandexIDLoginPage insertPassword() {
        page.locator(PASSWORD_INPUT).fill(password);
        System.out.printf("Password %s was sent", password);
        return this;
    }

    public YandexIDLoginPage clickEnterBtn() {
        page.locator(ENTER_BTN).first().click();
        System.out.println("Enter btn was clicked");
        return this;
    }

    public YandexIDLoginPage loginWithYaID() {
        assertYaIDFormLoaded().insertUserName().clickEnterBtn().insertPassword().clickEnterBtn();
        return this;
    }

}
