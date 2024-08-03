package com.gravefinder.scraping;

import java.util.ArrayList;
import java.util.List;

// import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
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

    public ArrayList<Memorial> scrapeMemorials(int cemeteryId) {
        String url = "https://www.findagrave.com/cemetery/" + cemeteryId + "/memorial-search";
        driver.get(url);

        ArrayList<Memorial> memorials = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        int previousSize = 0;
        int currentSize = 0;

        do {
            previousSize = currentSize;

            // Scroll down to load more records
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            // Wait for new elements to load
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.xpath(
                            "//a[contains(@class, 'd-flex align-items-center flex-grow-1 text-decoration-none col-print-3')]"),
                    previousSize));

            // Find all anchor tags with the specified class
            List<WebElement> memorialLinks = driver.findElements(By.xpath(
                    "//a[contains(@class, 'd-flex align-items-center flex-grow-1 text-decoration-none col-print-3')]"));

            currentSize = memorialLinks.size();

            for (int i = previousSize; i < currentSize; i++) {
                try {
                    // Re-locate the elements after navigating back
                    memorialLinks = driver.findElements(By.xpath(
                            "//a[contains(@class, 'd-flex align-items-center flex-grow-1 text-decoration-none col-print-3')]"));

                    WebElement link = memorialLinks.get(i);

                    // Scroll the element into view
                    js.executeScript("arguments[0].scrollIntoView(true);", link);

                    // Extract the href attribute
                    String href = link.getAttribute("href");

                    // Parse the memorialId from the href
                    String memorialId = href.split("/memorial/")[1].split("/")[0];
                    int memorialIdInt = Integer.parseInt(memorialId);
                    System.out.println("Memorial ID: " + memorialIdInt);

                    // Click on the link
                    link.click();

                    // You can add code here to scrape the memorial details after clicking the link
                    // For now, we'll just create a Memorial object with the memorialId
                    // Memorial memorial = new Memorial(memorialIdInt, cemeteryId, "", "", 0, 0, 0,
                    // 0, "", "");
                    // memorials.add(memorial);

                    // Navigate back to the search results page
                    driver.navigate().back();

                    // Wait for the page to load and elements to be available
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                            "//a[contains(@class, 'd-flex align-items-center flex-grow-1 text-decoration-none col-print-3')]")));
                } catch (StaleElementReferenceException e) {
                    // Handle the exception by re-locating the elements
                    memorialLinks = driver.findElements(By.xpath(
                            "//a[contains(@class, 'd-flex align-items-center flex-grow-1 text-decoration-none col-print-3')]"));
                    i--; // Retry the current index
                } catch (ElementClickInterceptedException e) {
                    // Handle the exception by scrolling the element into view and retrying
                    WebElement link = memorialLinks.get(i);
                    js.executeScript("arguments[0].scrollIntoView(true);", link);
                    i--; // Retry the current index
                }
            }
        } while (currentSize > previousSize);

        return memorials;
    }

    public void close() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}