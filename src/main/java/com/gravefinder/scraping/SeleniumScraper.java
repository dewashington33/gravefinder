package com.gravefinder.scraping;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

// import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
// import org.openqa.selenium.json.Json;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gravefinder.model.Cemetery;
import com.gravefinder.model.Memorial;

//import javafx.util.Duration;

import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;

// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
// import java.util.Map;
// import java.util.HashMap;
// import java.util.List;

public class SeleniumScraper {
    private WebDriver driver;
    private BrowserType browserType;
    private int cemeteryId;

    public enum BrowserType {
        EDGE,
        CHROME
    }

    public SeleniumScraper(BrowserType browserType, int cemeteryId) {
        this.setBrowserType(browserType);
        switch (browserType) {
            case EDGE:
                // Set the path to the Edge WebDriver executable if it's not in the system PATH
                // System.setProperty("webdriver.edge.driver", "path/to/msedgedriver.exe");
                driver = new EdgeDriver();
                break;
            case CHROME:
                // Set the path to the Chrome WebDriver executable if it's not in the system
                // PATH
                // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver.exe");
                driver = new ChromeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }
        this.setCemeteryId(cemeteryId);
    }

    // setter and getter
    public BrowserType getBrowserType() {
        return browserType;
    }

    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    public int getCemeteryId() {
        return cemeteryId;
    }

    public void setCemeteryId(int cemeteryId) {
        this.cemeteryId = cemeteryId;
    }

    public Cemetery scrapeCemetery(String url) {
        // Open the website
        driver.get(url);

        // Get the title
        String cemeteryName = driver.getTitle().replace(" - Find a Grave Cemetery", "").trim();
        WebElement canonicalLink = driver.findElement(By.xpath("//link[@rel='canonical']"));
        String shareLink = canonicalLink.getAttribute("href");

        // Locate the span tag with title="Latitude:" and extract its value
        WebElement latitudeSpan = driver.findElement(By.xpath("//span[@title='Latitude:']"));
        String latitude = latitudeSpan.getText();

        // Similarly, locate the span tag with title="Longitude:" and extract its value
        WebElement longitudeSpan = driver.findElement(By.xpath("//span[@title='Longitude:']"));
        String longitude = longitudeSpan.getText();

        // Get the default photo to share
        WebElement ogImageMeta = driver.findElement(By.xpath("//meta[@property='og:image']"));
        String defaultPhoto = ogImageMeta.getAttribute("content");

        // Extract required fields
        String name = cemeteryName;
        String cemLatitude = latitude;
        String cemLongitude = longitude;
        String linkToShare = shareLink;
        String defaultPhotoToShare = defaultPhoto;

        // Locate the address tag and extract city, county, and state
        WebElement addressLocalitySpan = driver.findElement(By.xpath("//span[@itemprop='addressLocality']"));
        String addressLocality = addressLocalitySpan.getText();
        String[] localityParts = addressLocality.split(", ");
        String cityName = localityParts[0];
        String countyName = localityParts.length > 1 ? localityParts[1] : "";

        WebElement addressRegionSpan = driver.findElement(By.xpath("//span[@itemprop='addressRegion']"));
        String stateName = addressRegionSpan.getText();

        // Create a Cemetery object
        Cemetery cemetery = new Cemetery(cemeteryId, name, cemLatitude, cemLongitude, cityName, countyName, stateName,
                linkToShare,
                defaultPhotoToShare, null, null);

        return cemetery;
    }

    public ArrayList<String> scrapeMemorialLinks(int cemeteryId) {
        // First have to get the cemetery name
        String url = "https://www.findagrave.com/cemetery/" + cemeteryId;
        driver.get(url);
        // Find the h1 element where the class starts with an itemprop attribute value
        // of "name" and extract the value of the element
        WebElement cemeteryName = driver.findElement(By.xpath("//h1[starts-with(@class, 'bio-name')]"));
        // Now build the URL for the memorial search page
        url = "https://www.findagrave.com/cemetery/" + cemeteryId + "/memorial-search?"
                + cemeteryName.getText().replace(" ", "+");
        driver.get(url);

        ArrayList<String> memorialLinks = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Find the settings button and click it
        WebElement settingsButton = driver.findElement(By.id("settingsOptions"));
        settingsButton.click();
        // now look for the id="condensedLists" and click it
        WebElement scrollLists = driver.findElement(By.id("scrollLists"));
        scrollLists.click();

        // while (true) {
        // try {
        // WebElement nextPageButton = driver.findElement(By.id("load-next-page"));

        // // Scroll the button into view
        // ((JavascriptExecutor)
        // driver).executeScript("arguments[0].scrollIntoView(true);", nextPageButton);

        // // Using JavaScript to click the button because it may be hidden
        // ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
        // nextPageButton);

        // // Wait for new content to load (e.g., a new 'memorial-item' element)
        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        // wait.until(ExpectedConditions
        // .presenceOfElementLocated(By.xpath("//div[contains(@class,
        // 'memorial-item')]")));

        // // Add a longer delay to allow the new content to load
        // Thread.sleep(5000);
        // } catch (NoSuchElementException e) {
        // System.out.println("Next page button not found, breaking the loop.");
        // break;
        // } catch (InterruptedException e) {
        // System.out.println("Thread was interrupted, breaking the loop.");
        // break;
        // } catch (WebDriverException e) {
        // System.out.println(
        // "Timeout waiting for the next page to load or other WebDriver error: " +
        // e.getMessage());
        // break;
        // } catch (Exception e) {
        // System.out.println("An unexpected error occurred: " + e.getMessage());
        // break;
        // }
        // }
        int previousItemCount = 0;

        while (true) {
            try {
                WebElement nextPageButton = driver.findElement(By.id("load-next-page"));

                // Scroll the button into view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextPageButton);

                // Using JavaScript to click the button because it may be hidden
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageButton);

                // Wait for new content to load (e.g., a new 'memorial-item' element)
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions
                        .presenceOfElementLocated(By.xpath("//div[contains(@class, 'memorial-item')]")));

                // Add a longer delay to allow the new content to load
                Thread.sleep(2000);

                // Check the number of 'memorial-item' elements
                List<WebElement> memorialItems = driver
                        .findElements(By.xpath("//div[contains(@class, 'memorial-item')]"));
                int currentItemCount = memorialItems.size();

                // Break the loop if the number of items does not increase
                if (currentItemCount <= previousItemCount) {
                    System.out.println("No new items found, breaking the loop.");
                    break;
                }

                // Update the previous item count
                previousItemCount = currentItemCount;
            } catch (NoSuchElementException e) {
                System.out.println("Next page button not found, breaking the loop.");
                break;
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted, breaking the loop.");
                break;
            } catch (WebDriverException e) {
                System.out.println(
                        "Timeout waiting for the next page to load or other WebDriver error: " + e.getMessage());
                break;
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                break;
            }
        }

        // Scroll to the top of the page
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");

        // Find all div elements with the class containing 'memorial-item'
        List<WebElement> memorialItems = driver.findElements(By.xpath("//div[contains(@class, 'memorial-item')]"));

        // Extract the href attribute from the anchor elements under these div elements
        for (WebElement item : memorialItems) {
            WebElement link = item.findElement(By.xpath(
                    ".//a[contains(@class, 'd-flex align-items-center flex-grow-1 text-decoration-none col-print-3')]"));
            String href = link.getAttribute("href");
            memorialLinks.add(href);
        }

        return memorialLinks;
    }

    public void close() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}