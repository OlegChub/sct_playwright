package web.listeners;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) {
            // get the Spring ApplicationContext from the JUnit context
            ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
            // retrieve the Page bean from Spring's context
            Page page = applicationContext.getBean(Page.class);
            String testName = context.getTestMethod().get().getName();
            if (page == null) {
                log.error("Page instance is null, cannot take screenshot");
                return;
            }
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String screenshotPath = String.format("screenshots/%s_%s.png", testName, timestamp);
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
            log.info("Screenshot saved to: {}", screenshotPath);
        }
    }
}