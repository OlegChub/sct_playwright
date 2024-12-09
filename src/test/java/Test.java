import com.config.SctApplication;
import com.microsoft.playwright.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(classes = {SctApplication.class})
public class Test {
    @Value("${admin.page}")
    private String url;

    @Value("${admin.login}")
    private String userName;

    @Value("${admin.pass}")
    private String password;

    @org.junit.jupiter.api.Test
    public void test() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );
            Page.ScreenshotOptions screenshotOptions = new Page.ScreenshotOptions();
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate(url);
            assertThat(page.locator("form[name='form_auth']")).isVisible();
            Locator loginInput = page.locator("input[name='USER_LOGIN']").first();
            loginInput.fill(userName);
            page.screenshot(new Page.ScreenshotOptions()
                    .setFullPage(true)
                    .setPath(Paths.get("./screenshots/scrnshotWithMask.png"))
                    .setMask(List.of(loginInput)).setMaskColor("red"));
            page.locator("input[name='USER_PASSWORD']").first().fill(password);
            page.click("input[name='Login']");
            assertThat(page).hasTitle("Панель управления - Аспро: Максимум - интернет-магазин");
            Locator locatorToMask = page.locator("span.adm-global_menu_aspro");
            locatorToMask.scrollIntoViewIfNeeded();
            page.screenshot(screenshotOptions.setFullPage(false).setPath(Paths.get("./screenshots/scrnshot.png")));

            browser.close();
        }

    }
}
