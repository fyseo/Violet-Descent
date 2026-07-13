package com.example.crawler.MainClasses;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import java.util.Objects;


public class Door extends Entity implements DynamicEntity {

    private boolean opened;
    private GameManager gameManager;

    private static final AudioClip doorSound = new AudioClip(Objects.requireNonNull(Door.class.getResource("/com/example/crawler/Audio/doorOpening.wav")).toString());
    private final static Image[] doorImages= {
            new Image(Objects.requireNonNull(Door.class.getResourceAsStream("/com/example/crawler/Images/Door/doorOpen.png"))),
            new Image(Objects.requireNonNull(Door.class.getResourceAsStream("/com/example/crawler/Images/Door/doorClosed.png")))
    };

    public Door(double posX, double posY, GameManager gameManager) {
        super(posX, posY, true, doorImages[1]);
        this.opened = false;
        this.gameManager = gameManager;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void update(double deltaTime) {
        //open door if level is done
        if(GameManager.getNoEnemies()<=0 && !this.opened)
            openDoor();

    }

    public void onCollision(Entity e) {
        //if a player collides with a door, and it is open move him to the next level and generate next level
        if(e instanceof Player p) {
            if(opened) {
                gameManager.setLevel(gameManager.getLevel() + 1);
                gameManager.generateLevel(gameManager.getLevel());
            }
        }
    }

    public void closeDoor(){
        //closes door and changes image
        this.setOpened(false);
        this.setImage(doorImages[1]);
        this.setTranslateXY(getPosX(),getPosY());
    }

    public void openDoor(){
        //opens door, changes image and plays sound
        this.setOpened(true);
        this.setImage(doorImages[0]);
        this.setTranslateXY(getPosX(),getPosY());
        Door.doorSound.play(0.1);

    }

    public static void spawnDoor(Door door){
        //spawns the door and adds it to the entity manager
        door.setTranslateXY(door.getPosX(),door.getPosY());
        EntityManager.addEntity(door);
    }

}
