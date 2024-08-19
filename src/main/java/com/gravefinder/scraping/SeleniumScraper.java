package com.gravefinder.scraping;

/**
Import java utilities
 */
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.time.Duration;
/**
Import selenium web driver
 */
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
/**
 * Import the cemetery and memorial classes
 */
import com.gravefinder.model.Cemetery;
import com.gravefinder.model.Memorial;

/**
 * This class is used to scrape cemetery and memorial data from the Find a Grave
 */
public class SeleniumScraper {
    private WebDriver driver;
    private BrowserType browserType;
    private int cemeteryId;

    public enum BrowserType {
        EDGE,
        CHROME
    }

    /**
     * Constructor for SeleniumScraper
     * 
     * @param browserType can only be what is defined in the BrowserType enum (EDGE,
     *                    CHROME)
     * @param cemeteryId  is the FindAGrave cemetery id of the cemetery to scrape
     */
    public SeleniumScraper(BrowserType browserType, int cemeteryId) {
        this.setBrowserType(browserType);
        switch (browserType) {
            case EDGE:
                /**
                 * Set the path to the Edge WebDriver executable if it's not in the system PATH
                 * System.setProperty("webdriver.edge.driver", "path/to/msedgedriver.exe");
                 */
                driver = new EdgeDriver();
                break;
            case CHROME:
                /**
                 * Set the path to the Chrome WebDriver executable if it's not in the system
                 * PATH
                 * System.setProperty("webdriver.chrome.driver", "path/to/chromedriver.exe");
                 */
                driver = new ChromeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }
        this.setCemeteryId(cemeteryId);
    }

    /**
     * Setters and Getters
     */
    /**
     * Get the browser type as defined by the BrowserType enum (EDGE, CHROME)
     * 
     * @return
     */
    public BrowserType getBrowserType() {
        return browserType;
    }

    /**
     * Set the browser type
     * 
     * @param browserType the browser type to set as defined by the BrowserType enum
     *                    (EDGE, CHROME)
     */
    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    /**
     * Get the cemetery ID
     * 
     * @return the FindAGrave cemetery ID
     */
    public int getCemeteryId() {
        return cemeteryId;
    }

    /**
     * Set the FindAGrave cemetery ID
     * 
     * @param cemeteryId
     */
    public void setCemeteryId(int cemeteryId) {
        this.cemeteryId = cemeteryId;
    }

    /**
     * Scrape the cemetery data from the Find a Grave website
     * 
     * @param url the URL of the cemetery page
     * @return a Cemetery object containing the scraped data
     */
    public Cemetery scrapeCemetery(String url) {
        /**
         * Open the website
         */
        driver.get(url);

        /**
         * Extract the cemetery name and share link
         * This strips everything after the cemetery name
         */
        String cemeteryName = driver.getTitle().replace(" - Find a Grave Cemetery", "").trim();
        /**
         * Extract the canonical link from the page source
         */
        WebElement canonicalLink = driver.findElement(By.xpath("//link[@rel='canonical']"));
        /**
         * Extract the href attribute from the canonical link
         */
        String shareLink = canonicalLink.getAttribute("href");
        /**
         * Extract the latitude
         * Locate the span tag with title="Latitude:" and extract its value
         */
        WebElement latitudeSpan = driver.findElement(By.xpath("//span[@title='Latitude:']"));
        String latitude = latitudeSpan.getText();
        /**
         * Extract the longitude
         * Similarly, locate the span tag with title="Longitude:" and extract its value
         */
        WebElement longitudeSpan = driver.findElement(By.xpath("//span[@title='Longitude:']"));
        String longitude = longitudeSpan.getText();
        /**
         * Get the default photo to share
         * Extract the og:image meta tag content
         */
        WebElement ogImageMeta = driver.findElement(By.xpath("//meta[@property='og:image']"));
        String defaultPhoto = ogImageMeta.getAttribute("content");
        /**
         * Assign the extracted values to variables for the Cemetery object
         */
        String name = cemeteryName;
        String cemLatitude = latitude;
        String cemLongitude = longitude;
        String linkToShare = shareLink;
        String defaultPhotoToShare = defaultPhoto;
        /**
         * Extract the address locality and region
         * Locate the address tag and extract city, county, and state
         */
        WebElement addressLocalitySpan = driver.findElement(By.xpath("//span[@itemprop='addressLocality']"));
        String addressLocality = addressLocalitySpan.getText();
        String[] localityParts = addressLocality.split(", ");
        String cityName = localityParts[0];
        String countyName = localityParts.length > 1 ? localityParts[1] : "";

        WebElement addressRegionSpan = driver.findElement(By.xpath("//span[@itemprop='addressRegion']"));
        String stateName = addressRegionSpan.getText();

        /**
         * Create a new Cemetery object with the extracted data
         */
        Cemetery cemetery = new Cemetery(cemeteryId, name, cemLatitude, cemLongitude, cityName, countyName, stateName,
                linkToShare,
                defaultPhotoToShare, null, null);

        return cemetery;
    }

    /**
     * Scrape the memorial URL links from the Find a Grave website
     * 
     * @return an ArrayList of memorial URL links
     */
    public ArrayList<String> scrapeMemorialLinks() {
        /**
         * First have to get the cemetery name
         * Build the URL for the cemetery page
         */
        String url = "https://www.findagrave.com/cemetery/" + cemeteryId;
        driver.get(url);
        /**
         * Find the h1 element where the class starts with an itemprop attribute value
         * of "name" and extract the value of the element
         */
        WebElement cemeteryName = driver.findElement(By.xpath("//h1[starts-with(@class, 'bio-name')]"));
        // Now build the URL for the memorial search page
        url = "https://www.findagrave.com/cemetery/" + cemeteryId + "/memorial-search?"
                + cemeteryName.getText().replace(" ", "+");
        driver.get(url);

        ArrayList<String> memorialLinks = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        /**
         * Find the settings button and click it
         */
        WebElement settingsButton = driver.findElement(By.id("settingsOptions"));
        settingsButton.click();
        /**
         * Find the condensedLists button and click it
         */
        WebElement scrollLists = driver.findElement(By.id("scrollLists"));
        scrollLists.click();

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

        // Wait for all div elements with the class containing 'memorial-item' to be
        // present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        wait.until(ExpectedConditions
                .presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'memorial-item')]")));

        // Find all div elements with the class containing 'memorial-item'
        List<WebElement> memorialItems = driver.findElements(By.xpath("//div[contains(@class, 'memorial-item')]"));

        // Extract the href attribute from the anchor elements under these div elements
        // Counter for items without anchor elements
        int noAnchorElementCount = 0;
        for (int i = 0; i < memorialItems.size(); i++) {
            try {
                // Re-fetch the list of memorial items
                memorialItems = driver.findElements(By.xpath("//div[contains(@class, 'memorial-item')]"));
                WebElement item = memorialItems.get(i);

                // Re-fetch the anchor element within the memorial-item div
                List<WebElement> links = item.findElements(By.tagName("a"));
                if (!links.isEmpty()) {
                    WebElement link = links.get(0);
                    wait.until(ExpectedConditions.visibilityOf(link));
                    String href = link.getAttribute("href");
                    memorialLinks.add(href);
                } else {
                    noAnchorElementCount++;
                }
            } catch (NoSuchElementException e) {
                System.out.println("Anchor element not found in memorial-item: " + e.getMessage());
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale element reference: " + e.getMessage());
                // Re-fetch the element and retry
                i--; // Decrement the counter to retry the current item
            }
        }

        // Print the total count of items without anchor elements
        if (noAnchorElementCount > 0) {
            System.out.println("No anchor element found in " + noAnchorElementCount + " memorial-item(s).");
        }

        return memorialLinks;
    }

    /**
     * Scrape the memorial data from the Find a Grave website
     * 
     * @return
     */
    public ArrayList<Memorial> scrapeMemorials() {
        /**
         * Scrape the memorial links
         * Calling the scrapeMemorialLinks method will return an ArrayList of memorial
         * URL links
         */
        ArrayList<String> memorialLinks = scrapeMemorialLinks();
        /**
         * Create an ArrayList to store the Memorial objects
         */
        ArrayList<Memorial> memorials = new ArrayList<>();
        /**
         * Loop through the memorial links and go to each link to extract the memorial
         * data
         */
        for (String link : memorialLinks) {
            driver.get(link);
            /**
             * Extract the memorial ID from the button element with class 'copy-id'
             */
            String memorialId = null;
            try {
                WebElement memorialIdElement = driver.findElement(By.className("copy-id"));
                memorialId = memorialIdElement.getAttribute("data-id");
            } catch (NoSuchElementException e) {
                System.out.println("Memorial ID element not found.");
            }
            int memorialIdInt = memorialId != null ? Integer.parseInt(memorialId) : -1;
            /**
             * Extract the memorial name from the element with id 'bio-name'
             */
            String memorialName = null;
            try {
                WebElement memorialNameElement = driver.findElement(By.id("bio-name"));
                memorialName = memorialNameElement.getText();
            } catch (NoSuchElementException e) {
                System.out.println("Memorial name element not found.");
            }
            /**
             * Get the last name on the memorial by splitting the full name and taking the
             * last part
             */
            String[] nameParts = memorialName != null ? memorialName.split(" ") : new String[] {};
            String lastName = nameParts.length > 0 ? nameParts[nameParts.length - 1] : "";
            /**
             * Get the first name by joining all parts except the last one
             */
            String firstName = "";
            for (int i = 0; i < nameParts.length - 1; i++) {
                firstName += nameParts[i] + " ";
            }
            /**
             * Extract the memorial birth date from the <time> tag with id 'birthDateLabel'
             */
            String birthDate = null;
            try {
                WebElement birthDateElement = driver.findElement(By.id("birthDateLabel"));
                birthDate = birthDateElement.getText();
            } catch (NoSuchElementException e) {
                System.out.println("Birth date element not found.");
            }
            /**
             * Extract the memorial death date from the <time> tag with id 'deathDateLabel'
             */
            String deathDate = null;
            try {
                WebElement deathDateElement = driver.findElement(By.id("deathDateLabel"));
                deathDate = deathDateElement.getText();
            } catch (NoSuchElementException e) {
                System.out.println("Death date element not found.");
            }
            /**
             * Extract the default photo to share
             * Find the main photo element and extract the src attribute of the img tag
             */
            String defaultPhotoToShare = null;
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
                WebElement mainPhotoElement = wait
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='main-photo']")));
                if (mainPhotoElement != null) {
                    WebElement profileImageElement = mainPhotoElement.findElement(By.tagName("img"));
                    defaultPhotoToShare = profileImageElement.getAttribute("src");
                }
            } catch (Exception e) {
                System.out.println("An error occurred while trying to find the main photo element.");
                e.printStackTrace();
                System.out.println("Current URL: " + driver.getCurrentUrl());
                System.out.println("Page Source: " + driver.getPageSource());
            }
            /**
             * Create a new Memorial object with the extracted data
             */
            Memorial memorial = new Memorial(memorialIdInt, cemeteryId, firstName.trim(), lastName, birthDate,
                    deathDate, link, defaultPhotoToShare);
            memorials.add(memorial);
        }
        return memorials;

    }

    public void close() {
        /**
         * Close the browser
         * 
         */
        if (driver != null) {
            driver.quit();
        }
    }
}