package com.example.crawler;

import com.example.crawler.MainClasses.Player;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        //connect to DB
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/violet_descent_game", "root", "root");
    }

    public static boolean testConnection(){
        //if connection fails shows an error
        try {
            DBConnection.getConnection();
        }catch(Exception e){
            //show alert window
            showAlertWindow();
            return false;
        }
        return true;
    }

    public static ArrayList<String> getScores() {
        //return an array list of all the scores from the DB
        ArrayList<String> scores = new ArrayList<>();
        String query = "select player_name,score from leader_board order by score desc";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);) {
            //loop to retrieve all info, parse them and place them in the list
            int rank=1;
            while (resultSet.next()) {
                String player_name = resultSet.getString("player_name");
                int score = resultSet.getInt("score");
                scores.add(String.format("%3d",rank)+"  "+String.format("%-10s",player_name) + "  " + score);
                rank++;
            }

        } catch (SQLException e) {
            showAlertWindow();
        }
        return scores;
    }

    public static ArrayList<String> getSavedGames(){
        //return an array list of all saved games from DB
        ArrayList<String> savedGames = new ArrayList<>();
        String query = "select game_save_id,game_level,run_time,health from saved_games order by game_save_id desc";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);) {

            while (resultSet.next()) {
                int save_id = resultSet.getInt("game_save_id");
                int game_level = resultSet.getInt("game_level");
                double run_time = resultSet.getDouble("run_time");
                int  health = resultSet.getInt("health");
                savedGames.add(String.format("%3d",save_id)+"  "+String.format("%1d",health)+"  "+String.format("%2d",game_level)+"  "+String.format("%.1f",run_time)+" Sec");
            }

        } catch (SQLException e) {
            System.out.println("error ur data base is off ");
        }
            return savedGames;

    }

    public static void addGame(int game_level,double run_time,double health){
        //inserts a row containing sent data into the saved_games table
        try(Connection connection= getConnection();
            Statement statement= connection.createStatement();){

            String query ="insert into saved_games(game_level,run_time,health) values ("+game_level+","+run_time+","+health+")";
            statement.execute(query);

        } catch (SQLException e){
           showAlertWindow();
        }

    }

    public static void addScore(String player_name, int score) {
        //inserts a row containing sent data into the leader_board table
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement();) {

            String query ="insert into leader_board (player_name,score) values ('"+player_name+"',"+score+")";
            statement.execute(query);

        }catch(Exception e){
            showAlertWindow();
        }

    }

    public static void removeGame(int save_id) {
        //receives a game_save_id and deletes it's corresponding game
        try(Connection connection=getConnection();
            Statement statement= connection.createStatement();
            ){
            String query = "delete from saved_games where game_save_id="+save_id;
            statement.execute(query);

        }catch(SQLException e){
            showAlertWindow();
        }
    }

    public static void clearGames(){
        //clear all saved games
        try(Connection connection=getConnection();
            Statement statement= connection.createStatement();
        ){
            String query = "delete from saved_games";
            statement.execute(query);

        }catch(SQLException e){
            showAlertWindow();
        }
    }

    public static void clearLeaderBoard(){
        //clear leaderboard
        try(Connection connection=getConnection();
            Statement statement= connection.createStatement();
        ){
            String query = "delete from leader_board";
            statement.execute(query);

        }catch(SQLException e){
            showAlertWindow();
        }
    }

    public static void showAlertWindow(){
        //shows an alert window and customizes it
        Alert alertWindow = new Alert(Alert.AlertType.ERROR);
        alertWindow.initStyle(StageStyle.UNDECORATED);
        alertWindow.setHeaderText(null);
        alertWindow.setContentText("Problem connecting to database!");
        alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/UI/error.png")))));
        alertWindow.showAndWait();
    }

}