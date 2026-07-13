package com.example.crawler.MainClasses;

import com.example.crawler.GameController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;


public class GameManager {

    private static final ImageView floor = new ImageView(new Image(Objects.requireNonNull(GameController.class.getResourceAsStream("/com/example/crawler/Images/Floor/floor.png"))));
    private static final ImageView shadows = new ImageView(new Image(Objects.requireNonNull(GameController.class.getResourceAsStream("/com/example/crawler/Images/UI/shadows.png"))));
    private static final double Width = 1300, Height =700;
    private static boolean gameStatus,isPaused;
    private static double time;
    private static int noEnemies,noKills;
    private static Group root;
    private static GameController gameController;
    private static AnimationTimer timer;
    private static GameManager runningInstance;
    private int level;
    private Player player;
    private Door door;
    private Scene gameScene;
    private EntityManager entityManager;
    private HashSet<String> keys;
    private Parent HUD;

    public GameManager() throws IOException {

        runningInstance=this;
        EntityManager.clearGameEntities();
        GameManager.isPaused=false;
        GameManager.gameStatus=false;
        this.gameScene=null;
        this.entityManager=new EntityManager();
        this.keys=new HashSet<>();

        this.level=1;
        GameManager.noKills=0;
        GameManager.time =0.0;
        GameManager.noEnemies=0;

        GameManager.root=new Group();
        FXMLLoader hudLoader= new FXMLLoader(getClass().getResource("/com/example/crawler/FXML/HUD.fxml"));
        this.HUD=hudLoader.load();
        GameManager.gameController=hudLoader.getController();
        GameManager.root.getChildren().addAll(HUD,floor,shadows);

        this.door=new Door(1248,300,this);
        this.player=new Player(keys,50,315,3);
    }

    public GameManager(int level,double time,double health)throws IOException{

        runningInstance=this;
        EntityManager.clearGameEntities();
        GameManager.isPaused=false;
        GameManager.gameStatus=false;
        this.gameScene=null;
        this.entityManager=new EntityManager();
        this.keys=new HashSet<>();

        this.level=level;
        GameManager.noKills=GameManager.calculateTotalEnemiesKilled(level);
        GameManager.time =time;
        GameManager.noEnemies=0;

        GameManager.root=new Group();
        FXMLLoader hudLoader= new FXMLLoader(getClass().getResource("/com/example/crawler/FXML/HUD.fxml"));
        this.HUD=hudLoader.load();
        GameManager.gameController=hudLoader.getController();
        GameManager.root.getChildren().addAll(HUD,floor,shadows);

        this.door=new Door(1248,300,this);
        this.player=new Player(keys,50,315,3);
        this.player.takeDamage(this.player.getMaxHealth()-health);

    }

    public static double getWidth(){return Width;}

    public static double getHeight(){return Height;}

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public static Group getRoot() {
        return GameManager.root;
    }

    public static int getNoKills() {
        return GameManager.noKills;
    }

    public static void  setNoKills(int noKills) {
        GameManager.noKills = noKills;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Player getPlayer() {
        return player;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public static boolean isGameStatus() {
        return gameStatus;
    }

    public static void setGameStatus(boolean gameStatus) {
        GameManager.gameStatus = gameStatus;

    }

    public static int getNoEnemies() {
        return noEnemies;
    }

    public static void setNoEnemies(int noEnemies) {
        GameManager.noEnemies = noEnemies;
    }

    public static void setIsPaused(boolean isPaused) {
        GameManager.isPaused = isPaused;
    }

    public static double getTime() {
        return time;
    }

    public static GameManager getRunningInstance() {
        return runningInstance;
    }

    public static void stopTimer(){
        //stops the running timer
        timer.stop();
    }

    public static void gameLoop(GameManager m, Scene s) throws IOException {
        //set the game scene and start the game and timer
        m.setGameScene(s);
        m.startGame();
        timer = new AnimationTimer() {
            //keep track of time
            long lastTime = 0;

            @Override
            public void handle(long now) {
                //if game is off end game and load game over fxml
                if (!GameManager.isGameStatus()) {
                    this.stop();
                    try {
                        FXMLLoader newSceneLoader = new FXMLLoader(getClass().getResource("/com/example/crawler/FXML/GameOver.fxml"));
                        Parent gameOverRoot = newSceneLoader.load();
                        GameController c= newSceneLoader.getController();
                        //show score on game over scene
                        c.updateGameOverScore(calculateScore(m));
                        GameManager.getRoot().getChildren().add(gameOverRoot);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //update player image to dead
                    m.getPlayer().getImageView().setImage(Player.getPlayerImages()[4][0]);
                    return;
                }
                //doesn't update entities if timer is paused(nothing moves)
                if(GameManager.isPaused){
                    lastTime = now;
                    return;
                }

                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                //calculate the delta of time to update accordingly
                double deltatime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                //update all entities using entity manager(polymorphism)
                m.getEntityManager().update(deltatime);

            }

        };
        timer.start();

    }

    public void startGame() {
        //constructs the game
        EntityManager.addEntity(this.player);
        Wall.spawnMainWalls();
        Door.spawnDoor(door);

        //handles layering the images
        GameManager.shadows.toFront();
        this.HUD.toFront();
        GameManager.floor.toBack();
        generateLevel(level);

    }

    public void     generateLevel(int level) {
        //spawns all the needed entities for a level and resets values to be reset, then sets game status to true
        if(level%5==0)
            this.player.heal();
        this.door.closeDoor();
        this.player.setPosX(100);
        this.player.setPosY(315);
        this.player.setTranslateXY(this.player.getPosX(), this.player.getPosY());
        EntityManager.removeAllEntities();
        Enemy.generateEnemies(this.level,this.player);
        GameManager.noEnemies=level-(((int)(level/5))*4);
        GameManager.setGameStatus(true);
        
    }

    public Scene createGameScene() throws IOException {
        //builds the game scene and sets its key and mouse events
        Scene scene=new Scene(root, Width, Height);
        scene.setOnKeyPressed(e -> {
            keys.add(e.getCode().toString());
        });
        scene.setOnKeyReleased(e -> keys.remove(e.getCode().toString()));
        scene.setOnMousePressed(e ->{
            keys.add(e.getButton().toString());
            this.getPlayer().setMouseX(e.getX());
            this.getPlayer().setMouseY(e.getY());
        });
        scene.setOnMouseReleased(e -> keys.remove(e.getButton().toString()));
        //pass the scene to the game loop to start
        try {
            GameManager.gameLoop(this, scene);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return scene;
    }

    public static int calculateScore(GameManager game){
        // calculates the score using set weights for actions
        int roomScore = game.level*50;
        int killScore = GameManager.noKills*20;
        //gets a time ratio to decrease the score when finishing slow
        double timeRatio=(game.level*10)/GameManager.time;
        if (timeRatio > 1) timeRatio=1;
        int timeScore = (int)(100*timeRatio);

        return timeScore+killScore+roomScore;
    }

    public static int calculateTotalEnemiesKilled(int level){
        //calculates the number of enemies using level for loading game
        int totalEnemiesKilled=0;
        for(int i=1; i<level; i++){
            totalEnemiesKilled+=(i-((i/5)*4));
        }
        return totalEnemiesKilled;
    }

    public static void elapsedTime(double deltaTime) {
        //elapsed time used for the timer label
        GameManager.time +=deltaTime;
        gameController.updateTimer(time);

    }

}
