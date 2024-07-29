package com.gravefinder;

// import com.gravefinder.model.Cemetery;
import com.gravefinder.model.Memorial;

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

        // Cemetery cemetery = new Cemetery(33862, "Evergreen Cemetery", "32.45537",
        // "-83.73634", "Perry", "Houston",
        // "Georgia", "https://www.findagrave.com/cemetery/33862/evergreen-cemetery",
        // "https://images.findagrave.com/icons2/fg-logo-square.png");
        // System.out.println(cemetery);
        Memorial memorial = new Memorial();
        memorial.setMemorialId(1);
        memorial.setCemeteryId(1);
        memorial.setFirstName("John");
        memorial.setLastName("Doe");
        memorial.setBirthYear(1990);
        memorial.setDeathYear(2020);
        memorial.setDeathMonth(1);
        memorial.setDeathDay(1);
        memorial.setDefaultLinkToShare("https://www.findagrave.com/memorial/1/john-doe");
        memorial.setDefaultPhotoToShare("https://images.findagrave.com/photos/2020/1/1/1_1_1_1.jpg");

        System.out.println(memorial);
        launch(args);
    }
}