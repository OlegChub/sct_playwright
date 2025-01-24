import com.config.SctApplication;
import com.microsoft.playwright.Page;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import web.pages.ya.*;

import static java.lang.Thread.sleep;

@SpringBootTest(classes = {SctApplication.class})
public class UpdateSCTShopsOnYaMaps {

    @Autowired
    private Page page;

    @Autowired
    private YandexIDLoginPage yandexIDLoginPage;

    @Autowired
    private SCTBranchesPage sctBranchesPage;

    @Autowired
    private BranchPage branchPage;

    @Autowired
    private ProfileNotFilledPopUp profileNotFilledPopUp;

    @Autowired
    private GoodsAndServicesPage goodsAndServicesPage;

    @Test
    @SneakyThrows
    @DisplayName("Make shops price lists actual")
    public void updatePriceListOfSCTBranchesOnYaMaps() {
        sctBranchesPage.openOrgBranchesPage();
        yandexIDLoginPage.loginWithYaID();
        sleep(10000);
        int pageCount = sctBranchesPage.getPaginationBtns().count();
        System.out.printf("Page total count: %d\n", pageCount);
        int shopCount = 1;
        int pagination = 2;
        sctBranchesPage.openOrgBranchesPageWithPageNumber(pagination);
        while (pageCount-- > 0) {
            var branchesEndpoints = sctBranchesPage.getNotUpdatedBranches();
            branchesEndpoints.forEach(System.out::println);
            System.out.printf("Total endpoints count: %d\n", branchesEndpoints.size());
            for (var branchEndpoint : branchesEndpoints) {
                page.navigate(" https://yandex.ru" + branchEndpoint);
                branchPage.assertBranchPageLoaded();
                System.out.printf("%d. Shop address: %s\n", shopCount, branchPage.getBranchAddress());
                profileNotFilledPopUp.assertProfileNotFilledPopUpLoaded();
                if (branchPage.isEnableNotificationPopUpVisible()) branchPage.closeEnableNotificationPopUp();
                profileNotFilledPopUp.clickOnCheckPriceList();
                if (goodsAndServicesPage.isDataIsActualBtnVisible()) {
                    goodsAndServicesPage.clickOnDataIsActualBtn();
                }
                shopCount++;
            }
            sctBranchesPage.openOrgBranchesPageWithPageNumber(pagination);
            pagination++;
        }
    }
}
