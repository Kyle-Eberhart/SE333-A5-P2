package playwrightLLM;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DePaulBookstoreLLMTest {

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
    void bookstoreFlowLLMTest() {
        page.navigate("https://depaul.bncollege.com/");
        page.waitForTimeout(5000);

        try {
            page.locator(".banner__component > a").click();
            page.waitForTimeout(2000);
        } catch (Exception ignored) {
        }

        // safer search box selector to avoid cookie banner search
        Locator searchBox = page.locator(
                "input[placeholder*='Search'], input[name*='search'], input[id*='search']"
        ).first();
        searchBox.click();
        searchBox.fill("earbuds");
        page.keyboard().press("Enter");

        page.waitForTimeout(7000);

        // Brand -> JBL
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
        page.waitForTimeout(1500);
        page.locator("#facet-brand label").filter(
                new Locator.FilterOptions().setHasText("JBL")
        ).first().click(new Locator.ClickOptions().setForce(true));

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
        page.locator("#facet-price label").filter(
                new Locator.FilterOptions().setHasText("Over $50")
        ).first().click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(7000);

        // Product
        Locator productLink = page.locator("a").filter(
                new Locator.FilterOptions().setHasText("JBL Quantum True Wireless")
        ).first();
        productLink.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        productLink.click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(6000);

        assertThat(page.locator("body")).containsText("JBL Quantum True Wireless");
        assertThat(page.locator("body")).containsText("Gaming");

        page.waitForTimeout(2000);

        // Add to cart
        Locator addToCartButton = page.locator("button, input, a").filter(
                new Locator.FilterOptions().setHasText("Add to Cart")
        ).first();
        addToCartButton.click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(7000);

        // Go to cart
        page.locator("a, button").filter(
                new Locator.FilterOptions().setHasText("Cart")
        ).first().click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(6000);

        assertThat(page.locator("body")).containsText("Shopping Cart");
        assertThat(page.locator("body")).containsText("JBL Quantum True Wireless");

        // Proceed to checkout
        Locator checkoutButton = page.locator("button, a").filter(
                new Locator.FilterOptions().setHasText("Proceed to Checkout")
        ).first();
        checkoutButton.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        checkoutButton.click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(7000);

        // Proceed as guest
        try {
            Locator guestButton = page.locator("button, a").filter(
                    new Locator.FilterOptions().setHasText("Proceed as Guest")
            ).first();

            if (guestButton.count() > 0) {
                guestButton.scrollIntoViewIfNeeded();
                page.waitForTimeout(1000);
                guestButton.click(new Locator.ClickOptions().setForce(true));
            } else {
                Locator guestFallback = page.locator("button, a").filter(
                        new Locator.FilterOptions().setHasText("Guest")
                ).first();
                guestFallback.scrollIntoViewIfNeeded();
                page.waitForTimeout(1000);
                guestFallback.click(new Locator.ClickOptions().setForce(true));
            }
        } catch (Exception e) {
            Locator guestFallback = page.locator("button, a").filter(
                    new Locator.FilterOptions().setHasText("Guest")
            ).first();
            guestFallback.scrollIntoViewIfNeeded();
            page.waitForTimeout(1000);
            guestFallback.click(new Locator.ClickOptions().setForce(true));
        }

        page.waitForTimeout(7000);

        // Contact info
        page.locator("input[name*='first'], input[id*='first']").first().fill("Kyle");
        page.locator("input[name*='last'], input[id*='last']").first().fill("Eberhart");
        page.locator("input[name*='email'], input[id*='email']").first().fill("kyle.test@example.com");
        page.locator("input[name*='phone'], input[id*='phone']").first().fill("3125551212");

        page.waitForTimeout(2000);

        // Continue to pickup
        Locator continueButton1 = page.locator("button:visible, a:visible").filter(
                new Locator.FilterOptions().setHasText("Continue")
        ).first();
        continueButton1.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        continueButton1.click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(7000);

        assertThat(page.locator("body")).containsText("Kyle");
        assertThat(page.locator("body")).containsText("Eberhart");

        // Continue to payment
        Locator continueButton2 = page.locator("button:visible, a:visible").filter(
                new Locator.FilterOptions().setHasText("Continue")
        ).first();
        continueButton2.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        continueButton2.click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(7000);

        assertThat(page.locator("body")).containsText("Payment");

        // Back to cart
        boolean returnedToCart = false;

        try {
            Locator backToCartButton = page.locator("button:visible, a:visible").filter(
                    new Locator.FilterOptions().setHasText("Back to Cart")
            ).first();

            if (backToCartButton.count() > 0) {
                backToCartButton.scrollIntoViewIfNeeded();
                page.waitForTimeout(1000);
                backToCartButton.click(new Locator.ClickOptions().setForce(true));
                page.waitForTimeout(7000);
                returnedToCart = page.locator("body").textContent().contains("Cart");
            }
        } catch (Exception e) {
        }

        if (!returnedToCart) {
            try {
                Locator cartButtonAgain = page.locator("a, button").filter(
                        new Locator.FilterOptions().setHasText("Cart")
                ).first();

                if (cartButtonAgain.count() > 0) {
                    cartButtonAgain.click(new Locator.ClickOptions().setForce(true));
                    page.waitForTimeout(7000);
                }
            } catch (Exception e) {
                page.goBack();
                page.waitForTimeout(5000);
            }
        }

        assertThat(page.locator("body")).containsText("JBL Quantum True Wireless");

        // Remove item
        boolean removed = false;

        try {
            Locator removeButton = page.locator("button, a, span, div").filter(
                    new Locator.FilterOptions().setHasText("Remove")
            ).first();
            if (removeButton.count() > 0) {
                removeButton.scrollIntoViewIfNeeded();
                page.waitForTimeout(1000);
                removeButton.click(new Locator.ClickOptions().setForce(true));
                removed = true;
            }
        } catch (Exception e) {
        }

        if (!removed) {
            try {
                Locator deleteButton = page.locator("button, a, span, div").filter(
                        new Locator.FilterOptions().setHasText("Delete")
                ).first();
                if (deleteButton.count() > 0) {
                    deleteButton.scrollIntoViewIfNeeded();
                    page.waitForTimeout(1000);
                    deleteButton.click(new Locator.ClickOptions().setForce(true));
                    removed = true;
                }
            } catch (Exception e) {
            }
        }

        if (!removed) {
            try {
                Locator removeItemButton = page.locator("button, a, span, div").filter(
                        new Locator.FilterOptions().setHasText("Remove item")
                ).first();
                if (removeItemButton.count() > 0) {
                    removeItemButton.scrollIntoViewIfNeeded();
                    page.waitForTimeout(1000);
                    removeItemButton.click(new Locator.ClickOptions().setForce(true));
                    removed = true;
                }
            } catch (Exception e) {
            }
        }

        if (!removed) {
            try {
                Locator trashButton = page.locator(
                        "[aria-label*='Remove'], [aria-label*='Delete'], button[title*='Remove'], button[title*='Delete']"
                ).first();
                if (trashButton.count() > 0) {
                    trashButton.scrollIntoViewIfNeeded();
                    page.waitForTimeout(1000);
                    trashButton.click(new Locator.ClickOptions().setForce(true));
                    removed = true;
                }
            } catch (Exception e) {
            }
        }

        page.waitForTimeout(5000);

        String bodyText = page.locator("body").textContent().toLowerCase();
        Assertions.assertTrue(
                bodyText.contains("empty") ||
                        bodyText.contains("your shopping cart is empty") ||
                        !bodyText.contains("jbl quantum true wireless"),
                "Cart did not appear to empty after removal."
        );
    }
}