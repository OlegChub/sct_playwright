package web.pages;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class PlaywrightAdminLoginPage extends BasePage {

    @Value("${admin.page}")
    private String url;

    @Value("${admin.login}")
    private String userName;

    @Value("${admin.pass}")
    private String password;

//    private final Page page;

    private static final String USERNAME_INPUT = "input[name='USER_LOGIN']";
    private static final String PASSWORD_INPUT = "input[name='USER_PASSWORD']";
    private static final String SUBMIT_BTN = "input[name='Login']";

//    public PlaywrightAdminLoginPage(Page page) {
//        this.page = page;
//    }

    private PlaywrightAdminLoginPage openAdminLoginPage() {
        navigateTo(url);
        return this;
    }

    private PlaywrightAdminLoginPage assertAuthFormLoaded() {
        Assertions.assertTrue(isElementVisible("form[name='form_auth']"));
        return this;
    }

    private PlaywrightAdminLoginPage insertUserName() {
        fillFirstTextField(USERNAME_INPUT, userName);
        return this;
    }

    private PlaywrightAdminLoginPage insertPassword() {
        fillFirstTextField(PASSWORD_INPUT, password);
        return this;
    }

    private PlaywrightAdminLoginPage submitLogin() {
        clickOnFirstElement(SUBMIT_BTN);
        return this;
    }

    public void login() {
        openAdminLoginPage().assertAuthFormLoaded().insertUserName().insertPassword().submitLogin();
    }

    @Override
    protected String getPageUrl() {
        return url;
    }
}
