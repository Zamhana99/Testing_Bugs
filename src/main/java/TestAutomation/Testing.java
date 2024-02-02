package TestAutomation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;

public class Testing {
    private WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://academybugs.com/find-bugs/");
    }


    @Test
    public void testFilterWomenPant() {
        driver.get("https://academybugs.com/find-bugs/");
        WebElement selectItem = driver.findElement(By.id("ec_product_li_4481370"));
        selectItem.click();

        // Explicitly wait for the success message to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Scroll down to the bottom of the page
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

        // Click the element
        WebElement button = driver.findElement(By.xpath("//*[@id='menu5']"));
        button.click();

        WebElement resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='ec_product_image_3981370']/div[3]/h3/a")));

        String result = resultElement.getText();
        assertEquals("PROFESSIONAL SUIT", result);
    }


    @Test
    public void testSearchBar() {
        WebElement selectItem = driver.findElement(By.id("ec_product_li_4481370"));
        selectItem.click();

        WebElement searchBar = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/aside[2]/div/form/input[1]"));
        // Enter a search query
        searchBar.sendKeys("Boho Bangle Bracelet");

        // Press Enter to perform the search
       searchBar.sendKeys(Keys.ENTER);

        // Find the element that represents the search results
        WebElement searchResults = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[1]/main/article/div/section/ul/li/div/div[3]/h3/a"));

        // Verify that the search results are not empty
        String resultsText = searchResults.getText();
        // Assuming search result page has some text indicating results
        assertEquals("BOHO BANGLE BRACELET", resultsText);
    }


    @Test
    public void testAddToCartCount() {
        WebElement itemElement = driver.findElement(By.id("ec_product_li_4481370"));
        itemElement.click();

        WebElement quantityInput = driver.findElement(By.id("ec_quantity_5"));
        quantityInput.clear();
        quantityInput.sendKeys("5");

        WebElement addToCartButton = driver.findElement(By.cssSelector(".ec_details_add_to_cart input[type='submit']"));
        addToCartButton.click();

        WebElement cartQuantityElement = driver.findElement(By.cssSelector("div.ec_cart_widget_button span.ec_cart_items_total"));
        // Get the actual quantity value from the cart
        int actualQuantity = Integer.parseInt(cartQuantityElement.getText());
        // Assertion: Check if the actual quantity matches the expected quantity
        assertEquals(5, actualQuantity);
    }


    @Test
    public void testSortDropdownOnItemsPage() {
       //click on the dropdown element
        WebElement sortDropdown = driver.findElement(By.id("sortfield"));
        sortDropdown.click();

        // Select an option from the dropdown (Low - High)
        WebElement lowToHighOption = driver.findElement(By.xpath("//*[@id='sortfield']/option[2]"));
        lowToHighOption.click();

        // Wait for the page to sort the items
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-indicator")));

        // Find the list of items on the page
        List<WebElement> items = driver.findElements(By.cssSelector(".item"));

        // Create a list to store the item names
        List<String> itemNames = new ArrayList<>();
        for (WebElement item : items) {
            itemNames.add(item.getText());
        }

        // Make a list of the item names and sort it in ascending order
        List<String> expectedOrder = new ArrayList<>(itemNames);
        Collections.sort(expectedOrder);

        // Compare the actual order with the expected order
        boolean isSortedCorrectly = itemNames.equals(expectedOrder);

        // Assertion to check if items are sorted correctly
        assertTrue("Items are not sorted correctly", isSortedCorrectly);
    }


    @Test
    public void testTwitterShareButton() {
        WebElement product = driver.findElement(By.xpath("//*[@id='ec_product_image_effect_4481370']/a"));
        product.click();

        // Capture the current window handle
        String mainWindowHandle = driver.getWindowHandle();

        // Click the Twitter share button
        WebElement twitterShareButton = driver.findElement(By.xpath("//*[@id=\"post-1675\"]/div/section/div[1]/div[3]/div[2]/div[2]/a"));
        twitterShareButton.click();

        // Get all window handles
        Set<String> allWindowHandles = driver.getWindowHandles();

        // Switch to the new window or tab
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        // Verify that you are on Twitter's page
        String twitterPageURL = driver.getCurrentUrl();
        assertTrue("The Twitter share button did not navigate to Twitter's page.", twitterPageURL.startsWith("https://twitter.com/"));
    }


    @Test
    public void changeCurrency() {
        // Select a product by clicking on it
        WebElement product = driver.findElement(By.xpath("//*[@id='ec_product_image_effect_4481370']/a"));
        product.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-indicator")));

        WebElement currencyChange = driver.findElement(By.xpath("//*[@id='ec_currency_conversion']/option[2]"));
        currencyChange.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed

        boolean isClickSuccessful = false;
        try {
            // Attempt to perform the click operation
            WebElement element = driver.findElement(By.id("ec_details_add_to_cart"));
            element.click();

            // If the click operation didn't throw an exception, consider it successful
        } catch (Exception e) {
            assertTrue("Element is not clickable, the page might have crashed by changing currency", isClickSuccessful);
        }

    }


    @Test
    public void testAddToCart() {

        WebElement addToCartButton = driver.findElement(By.xpath("//*[@id='ec_add_to_cart_5']"));
        addToCartButton.click();

        // Explicitly wait for the success message to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement successMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ec_added_to_cart_5")));

        System.out.println("Verifying success message");
        String successMessage = successMessageElement.getText();
        assertEquals("CHECKOUT NOW", successMessage);
    }


    @Test
    public void viewManufacturer() {
        // Select a product by clicking on it
        WebElement product = driver.findElement(By.xpath("//*[@id='ec_product_image_effect_4481370']/a"));
        product.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement button = driver.findElement(By.xpath("//*[@id='manufacturer-bug']/a"));
        button.click();

        // Verify if the current page's URL contains "404" as an indication of landing on a 404 error page
        assertTrue("It's considered a bug if the page navigates to a 404 error page", driver.getCurrentUrl().contains("404"));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

}

