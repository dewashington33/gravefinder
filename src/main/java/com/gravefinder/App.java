package com.gravefinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gravefinder.model.Cemetery;
import com.gravefinder.model.Memorial;
import com.gravefinder.model.Photo;
import com.gravefinder.scraping.SeleniumScraper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/graveFinderMain.fxml"));
        // Scene scene = new Scene(root);
        // primaryStage.setScene(scene);
        primaryStage.setTitle("Gravefinder");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) {
        // Houston Baptist ID is 34627
        // Evergreen Cemetery ID is 33862
        // SeleniumScraper scraper = new
        // SeleniumScraper(SeleniumScraper.BrowserType.EDGE, 34627);
        // ArrayList<String> memorialLinks = scraper.scrapeMemorialLinks();
        // System.out.println(memorialLinks);

        // Scrape memorials
        SeleniumScraper scraper = new SeleniumScraper(SeleniumScraper.BrowserType.EDGE, 34627);
        ArrayList<Memorial> memorials = scraper.scrapeMemorials();
        System.out.println(memorials);

        // Cemetery cemetery =
        // scraper.scrapeCemetery("https://www.findagrave.com/cemetery/33862");
        // System.out.println(cemetery);
        // scraper.close();
        // Photo photo1 = new Photo(15982645,
        // "https://images.findagrave.com/photos/2009/66/CEM46924123_123653303505.jpg",
        // "-",
        // "08 Mar 2009", "Photo");
        // Photo photo2 = new Photo(15982645,
        // "https://images.findagrave.com/photos/2009/66/CEM46924123_123653303505.jpg",
        // "-", "08 Mar 2009", "Photo");

        // // Photo photo2 = new Photo(2620437,
        // //
        // "https://images.findagrave.com/photos/2006/45/CEM46603620_114004788700.jpg",
        // // "- This old cemetery has beautiful large trees draped in spanish moss.",
        // "15
        // // Feb 2006", "Photo");
        // List<Photo> photos = Arrays.asList(photo1, photo2);
        // Cemetery cemetery = new Cemetery(33862, "Evergreen Cemetery", "32.45537",
        // "-83.73634", "Perry", "Houston",
        // "Georgia", "https://www.findagrave.com/cemetery/33862/evergreen-cemetery",
        // "https://images.findagrave.com/icons2/fg-logo-square.png", photos, null);
        // System.out.println(cemetery);

        // Memorial memorial = new Memorial();
        // memorial.setMemorialId(1);
        // memorial.setCemeteryId(1);
        // memorial.setFirstName("John");
        // memorial.setLastName("Doe");
        // memorial.setBirthYear(1990);
        // memorial.setDeathYear(2020);
        // memorial.setDeathMonth(1);
        // memorial.setDeathDay(1);
        // memorial.setDefaultLinkToShare("https://www.findagrave.com/memorial/1/john-doe");
        // memorial.setDefaultPhotoToShare("https://images.findagrave.com/photos/2020/1/1/1_1_1_1.jpg");

        // System.out.println(memorial);

        launch(args);
    }
}