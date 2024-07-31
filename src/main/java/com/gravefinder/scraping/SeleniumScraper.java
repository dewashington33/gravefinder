package com.gravefinder.scraping;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class SeleniumScraper {
    private WebDriver driver;

    public SeleniumScraper() {
        // enum of supported browsers edge, chrome

        // Set the path to the Edge WebDriver executable if it's not in the system PATH
        // System.setProperty("webdriver.edge.driver", "path/to/msedgedriver.exe");

        // Initialize the Edge WebDriver
        driver = new EdgeDriver();
    }

    public void scrapeWebsite(String url) {
        // Open the website
        driver.get(url);

        // Perform scraping actions
        String pageTitle = driver.getTitle();
        System.out.println("Page title is: " + pageTitle);

        // Add more scraping logic here
    }

    public void close() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }

}
