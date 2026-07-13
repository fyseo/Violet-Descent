package com.example.crawler;

import com.example.crawler.MainClasses.GameManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GameController {

    private static GameController instance;
    private final static MediaPlayer menuMusic= new  MediaPlayer(new Media(Objects.requireNonNull(GameController.class.getResource("/com/example/crawler/Audio/menu.wav")).toString()));
    private final static MediaPlayer gameMusic= new  MediaPlayer(new Media(Objects.requireNonNull(GameController.class.getResource("/com/example/crawler/Audio/game.wav")).toString()));

    private final static Image[] HeartsImages={
            new Image(Objects.requireNonNull(GameController.class.getResourceAsStream("/com/example/crawler/Images/Hearts/0hearts.png"))),
            new Image(Objects.requireNonNull(GameController.class.getResourceAsStream("/com/example/crawler/Images/Hearts/1hearts.png"))),
            new Image(Objects.requireNonNull(GameController.class.getResourceAsStream("/com/example/crawler/Images/Hearts/2hearts.png"))),
            new Image(Objects.requireNonNull(GameController.class.getResourceAsStream("/com/example/crawler/Images/Hearts/3hearts.png")))
    };

    @FXML
    private Label TimerLabel;

    @FXML
    private ImageView HeartsImage;

    @FXML
    private Button StartButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private Button LoadGameButton;

    @FXML
    private Button SaveButton;

    @FXML
    private ListView<String> LeaderboardList;

    @FXML
    private ListView<String> SavedGamesList;

    @FXML
    private Label ScoreLabel;

    @FXML
    private TextField NameTextField;

    @FXML
    private Button ClearGames;

    @FXML
    private Button ClearLeaderboard;


    @FXML
    public void initialize(){

        if(StartButton!=null) {// check if we're in the menu scene
            gameMusic.stop();
            menuMusic.setVolume(0.2);
            menuMusic.setCycleCount(MediaPlayer.INDEFINITE);
            menuMusic.play();
        }else if(HeartsImage!=null) {// check if we're in the game scene
            instance = this;
            menuMusic.stop();
            gameMusic.setVolume(0.05);
            gameMusic.setCycleCount(MediaPlayer.INDEFINITE);
            gameMusic.play();
        }
    }

    @FXML
    protected void onStartGameClick(ActionEvent mouseclick) throws IOException {
        //create new game and set the scene
        GameManager currentGame=new GameManager();
        Scene scene = currentGame.createGameScene();
        Stage stage = (Stage)((Node)mouseclick.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    protected void setOnContinueButtonClick(ActionEvent mouseclick) throws IOException{
        //load fxml file and get its controller
        FXMLLoader newSceneLoader = new FXMLLoader(getClass().getResource("/com/example/crawler/FXML/SavedGames.fxml"));
        Parent root = newSceneLoader.load();
        GameController controller = newSceneLoader.getController();
        //tests connection
        if(!DBConnection.testConnection()) return;
        //loads the saved games table from DB
        ArrayList<String> savedGames=DBConnection.getSavedGames();
        controller.SavedGamesList.getItems().addAll(savedGames);
        //load scene from fxml file
        Scene LeaderboardScene = new Scene(root, GameManager.getWidth(),GameManager.getHeight());
        Stage stage = (Stage)((Node)mouseclick.getSource()).getScene().getWindow();
        stage.setScene(LeaderboardScene);
        stage.show();
    }

    @FXML
    protected void setOnLeaderboardButtonClick(ActionEvent mouseclick) throws IOException{
        //load leaderboard fxml file and its controller
        FXMLLoader newSceneLoader = new FXMLLoader(getClass().getResource("/com/example/crawler/FXML/Leaderboard.fxml"));
        Parent root = newSceneLoader.load();
        GameController controller = newSceneLoader.getController();
        //tests connection
        if(!DBConnection.testConnection()) return;
        //loads scores table from DB
        ArrayList<String> scores=DBConnection.getScores();
        controller.LeaderboardList.getItems().addAll(scores);
        //load scene from fxml file
        Scene LeaderboardScene = new Scene(root, GameManager.getWidth(),GameManager.getHeight());
        Stage stage = (Stage)((Node)mouseclick.getSource()).getScene().getWindow();
        stage.setScene(LeaderboardScene);
        stage.show();
    }

    @FXML
    protected  void onExitClick(ActionEvent mouseclick) throws  IOException{
        //closes the stage
        Node source = (Node) mouseclick.getSource();
        Stage stage= (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onMainMenuClick(ActionEvent mouseclick) throws IOException{
        //stop current game
        GameManager.setGameStatus(false);
        //load main menu fxml file and create a scene from it
        FXMLLoader newSceneLoader = new FXMLLoader(getClass().getResource("/com/example/crawler/FXML/MainMenu.fxml"));
        Scene mainMenuScene = new Scene(newSceneLoader.load(), GameManager.getWidth(),GameManager.getHeight());
        Stage stage = (Stage)((Node)mouseclick.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
        stage.show();

    }

    @FXML
    protected void onSavedGamesMouseClick(Event mouseclick) throws IOException{
        //when selecting a row in saved game list enable buttons if cell not null
        if(SavedGamesList.getSelectionModel().getSelectedItem()!=null) {
            LoadGameButton.setDisable(false);
            DeleteButton.setDisable(false);
        }
    }

    @FXML
    protected void onLoadGameButtonClick(ActionEvent mouseclick) throws IOException{
        //load the game data from the selected row
        String selectedGame = SavedGamesList.getSelectionModel().getSelectedItem();
        String stats=selectedGame.substring(5).trim();
        String[] gameInfo=stats.split("\\s+");
        //create a game from the loaded game data using parametric constructor
        GameManager continueGame = new GameManager(Integer.parseInt(gameInfo[1]),Double.parseDouble(gameInfo[2]),Double.parseDouble(gameInfo[0]));
        //create the scene containing the game
        Scene scene = continueGame.createGameScene();
        Stage  stage = (Stage)((Node)mouseclick.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    protected void onDeleteButtonClick(ActionEvent mouseclick) throws IOException{
        //gets the save id of the selected row and removes it
        String save_id = SavedGamesList.getSelectionModel().getSelectedItem().substring(0,3).trim();
        SavedGamesList.getItems().remove(SavedGamesList.getSelectionModel().getSelectedItem());
        DBConnection.removeGame(Integer.parseInt(save_id));

        SavedGamesList.getSelectionModel().clearSelection();
        LoadGameButton.setDisable(true);
        DeleteButton.setDisable(true);
    }

    @FXML
    protected void onPlayAgainClick(ActionEvent mouseclick) throws IOException {
        //stops the old timer and creates a new game instance
        GameManager.stopTimer();
        GameManager currentGame=new GameManager();
        //create new game scene
        Scene scene = currentGame.createGameScene();
        Stage stage = (Stage)((Node)mouseclick.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    protected void setPauseButtonClick(ActionEvent mouseclick) throws IOException {
        //pauses the game timer and loads the pause fxml file
        GameManager.setIsPaused(true);
        FXMLLoader newSceneLoader = new FXMLLoader(getClass().getResource("/com/example/crawler/FXML/PauseMenu.fxml"));
        GameManager.getRoot().getChildren().add(newSceneLoader.load());
        GameController controller = newSceneLoader.getController();
        //enables the save button if DB connected
        try {
            DBConnection.getConnection();
            controller.SaveButton.setDisable(false);
        }catch(Exception e) {
            controller.SaveButton.setDisable(true);
        }
    }

    @FXML
    protected void setOnResumeButtonClick(ActionEvent mouseclick) throws IOException{
        //loops through the parents to remove the pause menu
        Node pauseMenuRoot = ((Node)mouseclick.getSource()).getParent();
        while (pauseMenuRoot.getParent() != GameManager.getRoot()) {
            pauseMenuRoot = pauseMenuRoot.getParent();
        }
        GameManager.getRoot().getChildren().remove(pauseMenuRoot);
        //unpause the game
        GameManager.setIsPaused(false);
    }

    @FXML
    protected void onSaveGameButtonClick(ActionEvent mouseclick) throws IOException{
        //saves the game and disables the button
        SaveButton.setDisable(true);
        SaveButton.setText("Saved!");
        DBConnection.addGame(
                GameManager.getRunningInstance().getLevel(),GameManager.getTime(),
                GameManager.getRunningInstance().getPlayer().getHealth()
        );
    }

    @FXML
    protected void onSubmitButtonClick(ActionEvent mouseclick) throws IOException{
        //if the format is right,save the score
        if(NameTextField.getText().isEmpty()||NameTextField.getText().length()>10){
            Alert alertWindow = new Alert(Alert.AlertType.INFORMATION);
            alertWindow.setTitle("Invalid Name");
            alertWindow.setHeaderText(null);
            alertWindow.setContentText("Please enter a name between 1 and 10 characters.");
            alertWindow.showAndWait();

        }else{
            if(!DBConnection.testConnection()) return;
            //save highScore to DB
            DBConnection.addScore(NameTextField.getText(), Integer.parseInt(ScoreLabel.getText()));
            //load main menu fxml file
            FXMLLoader newSceneLoader = new FXMLLoader(getClass().getResource("/com/example/crawler/FXML/MainMenu.fxml"));
            Scene mainMenuScene = new Scene(newSceneLoader.load(), GameManager.getWidth(),GameManager.getHeight());
            Stage stage = (Stage)((Node)mouseclick.getSource()).getScene().getWindow();
            stage.setScene(mainMenuScene);
            stage.show();
        }
    }

    @FXML
    protected void onClearGamesButtonClick(ActionEvent mouseclick) throws IOException {
        DBConnection.clearGames();
        ClearGames.setText("Cleared!");
        ClearGames.setDisable(true);
    }

    @FXML
    protected void onClearLeaderBoardButtonClick(ActionEvent mouseclick) throws IOException {
        DBConnection.clearLeaderBoard();
        ClearLeaderboard.setText("Cleared!");
        ClearLeaderboard.setDisable(true);
    }

    @FXML
    protected void onSettingsButtonClick(ActionEvent mouseclick) throws IOException {
        FXMLLoader newSceneLoader = new FXMLLoader(getClass().getResource("/com/example/crawler/FXML/Settings.fxml"));
        Parent root = newSceneLoader.load();
        //tests connection
        if(!DBConnection.testConnection()) return;
        //load scene from fxml file
        Scene SettingsScene = new Scene(root, GameManager.getWidth(),GameManager.getHeight());
        Stage stage = (Stage)((Node)mouseclick.getSource()).getScene().getWindow();
        stage.setScene(SettingsScene);
        stage.show();
    }

    public void updateTimer(double time){
        //updates the timer label
        if(TimerLabel!=null)
            TimerLabel.setText(String.format("%.1f",time));
    }

    public static void updateHearts(double currentHealth){
        //updates the hearts view
        instance.HeartsImage.setImage(HeartsImages[(int)currentHealth]);
    }

    public void updateGameOverScore(int currentScore){
        //updates the score label
        if(ScoreLabel!=null)
            ScoreLabel.setText(""+currentScore);
    }

}
