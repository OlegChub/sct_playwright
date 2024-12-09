package web.pages;

import com.microsoft.playwright.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class PlaywrightAdminLoginPage{

    @Value("${admin.page}")
    private String url;

    @Value("${admin.login}")
    private String userName;

    @Value("${admin.pass}")
    private String password;

    private final Page page;

    private static final String USERNAME_INPUT = "input[name='USER_LOGIN']";
    private static final String PASSWORD_INPUT = "input[name='USER_PASSWORD']";
    private static final String SUBMIT_BTN = "input[name='Login']";

    public PlaywrightAdminLoginPage(Page page) {
        this.page = page;
    }

    private PlaywrightAdminLoginPage openAdminLoginPage() {
        page.navigate(url);
        return this;
    }

    private PlaywrightAdminLoginPage assertAuthFormLoaded() {
        page.locator("form[name='form_auth']").isVisible();
        return this;
    }

    private PlaywrightAdminLoginPage insertUserName() {
        page.locator(USERNAME_INPUT).first().fill(userName);
        return this;
    }

    private PlaywrightAdminLoginPage insertPassword() {
        page.locator(PASSWORD_INPUT).first().fill(password);
        return this;
    }

    private PlaywrightAdminLoginPage submitLogin() {
        page.locator(SUBMIT_BTN).first().click();
        return this;
    }

    public void login() {
        openAdminLoginPage().assertAuthFormLoaded().insertUserName().insertPassword().submitLogin();
    }

}
