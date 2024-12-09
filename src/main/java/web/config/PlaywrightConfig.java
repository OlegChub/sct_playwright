package web.config;

import com.microsoft.playwright.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaywrightConfig {

    @Value("${browser}")
    String browser;

    @Bean
    public Playwright playwright() {
        return Playwright.create();
    }

    @Bean
    public Browser browser(Playwright playwright) {
        switch (browser) {
            case "firefox":
                return playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            case "webkit":
                return playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
            case "chrome":
            default:
                return playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }
    }

    @Bean
    public BrowserContext browserContext(Browser browser) {
        return browser.newContext();
    }

    @Bean
    public Page page(BrowserContext browserContext) {
        return browserContext.newPage();
    }
}

