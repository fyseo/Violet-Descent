package com.example.crawler;

import com.example.crawler.MainClasses.GameManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;
import java.io.IOException;
import java.util.Objects;

import javafx.scene.input.MouseEvent;

public class MainGame extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        //creates the stage and loads main menu fxml
        FXMLLoader newSceneLoader = new FXMLLoader(getClass().getResource("/com/example/crawler/FXML/MainMenu.fxml"));
        Scene mainMenuScene = new Scene(newSceneLoader.load(), GameManager.getWidth(),GameManager.getHeight());
        //set all the stage aspects like icons and cursor
        Image icon32 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/crawler/Images/Icons/icon32.png")));
        Image icon64 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/crawler/Images/Icons/icon64.png")));
        stage.getIcons().addAll(icon32, icon64);
        //create two cursors 1 for clicked and one for normal
        Image cursorImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/crawler/Images/Pointer/bighair.png")));
        Image cursorImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/crawler/Images/Pointer/smallhair.png")));
        ImageCursor cursor1 = new ImageCursor(cursorImage1,cursorImage1.getWidth()/2,cursorImage1.getHeight()/2);
        ImageCursor cursor2 = new ImageCursor(cursorImage2,cursorImage2.getWidth()/2,cursorImage2.getHeight()/2);
        stage.setTitle("Violet Descent");
        stage.initStyle(StageStyle.UNDECORATED);


        //if mouse is pressed changes it
        stage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setCursor(cursor1);

                newScene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                    newScene.setCursor(cursor2);
                });

                newScene.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
                    newScene.setCursor(cursor1);
                });
            }
        });

        stage.setScene(mainMenuScene);
        stage.show();
    }

}
