package com.gravefinder.scraping;

// import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
// import org.openqa.selenium.json.Json;

import com.gravefinder.model.Cemetery;

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

    public void close() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}