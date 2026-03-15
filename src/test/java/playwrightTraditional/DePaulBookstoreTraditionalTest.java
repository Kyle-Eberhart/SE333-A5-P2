package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DePaulBookstoreTraditionalTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void setUpAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    @AfterAll
    static void tearDownAll() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setRecordVideoDir(Paths.get("videos"))
                        .setRecordVideoSize(1280, 720)
        );
        page = context.newPage();
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void bookstoreFlowTest() {
        page.navigate("https://depaul.bncollege.com/");
        page.waitForTimeout(5000);

        try {
            page.locator(".banner__component > a").click();
            page.waitForTimeout(2000);
        } catch (Exception e) {
        }

        Locator searchBox = page.locator("input[placeholder*='Search'], input[name*='search'], input[id*='search']").first();
        searchBox.click();
        searchBox.fill("earbuds");
        page.keyboard().press("Enter");
        page.waitForTimeout(7000);

        // Brand -> JBL
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
        page.waitForTimeout(1500);
        page.locator("#facet-brand label").filter(new Locator.FilterOptions().setHasText("JBL")).first()
                .click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(6000);

        // Color -> Black
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("color")).click();
        page.waitForTimeout(2000);

        Locator blackOption = page.locator("label:visible").filter(
                new Locator.FilterOptions().setHasText("Black")
        ).first();

        blackOption.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        blackOption.click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(6000);

        // Price -> Over $50
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("price")).click();
        page.waitForTimeout(1500);
        page.locator("#facet-price label").filter(new Locator.FilterOptions().setHasText("Over $50")).first()
                .click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(7000);

        // Click exact product
        Locator productLink = page.locator("a").filter(
                new Locator.FilterOptions().setHasText("JBL Quantum True Wireless")
        ).first();

        productLink.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        productLink.click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(6000);

        // Light product page checks
        assertThat(page.locator("body")).containsText("JBL Quantum True Wireless");
        assertThat(page.locator("body")).containsText("Gaming");

        page.waitForTimeout(2000);

        // Add to cart
        Locator addToCartButton = page.locator("button, input, a").filter(
                new Locator.FilterOptions().setHasText("Add to Cart")
        ).first();

        addToCartButton.click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(7000);

        // Go straight to cart
        page.locator("a, button").filter(
                new Locator.FilterOptions().setHasText("Cart")
        ).first().click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(6000);

        // Verify cart page
        assertThat(page.locator("body")).containsText("Shopping Cart");
        assertThat(page.locator("body")).containsText("JBL Quantum True Wireless");
    }
}