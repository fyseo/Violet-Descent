package com.example.crawler.MainClasses;

import javafx.scene.image.Image;
import java.util.Objects;

public class Wall extends Entity {
    private static Wall topWall,bottomWall,leftWall,rightWall;
    private final static Image[] wallImages ={
            new Image(Objects.requireNonNull(Wall.class.getResourceAsStream("/com/example/crawler/Images/Walls/topWall.png")),1300,50,true,true),
            new Image(Objects.requireNonNull(Wall.class.getResourceAsStream("/com/example/crawler/Images/Walls/bottomWall.png")),1300,50,true,true),
            new Image(Objects.requireNonNull(Wall.class.getResourceAsStream("/com/example/crawler/Images/Walls/leftWall.png")),50,700,true,true),
            new Image(Objects.requireNonNull(Wall.class.getResourceAsStream("/com/example/crawler/Images/Walls/rightWall.png")),50,700,true,true),

    };

    public Wall(double posX, double posY, boolean alive, Image image) {
        super(posX, posY, alive, image);
    }

    public static Wall getTopWall() {
        return topWall;
    }

    public static void setTopWall(Wall topWall) {
        Wall.topWall = topWall;
    }

    public static Wall getBottomWall() {
        return bottomWall;
    }

    public static void setBottomWall(Wall bottomWall) {
        Wall.bottomWall = bottomWall;
    }

    public static Wall getLeftWall() {
        return leftWall;
    }

    public static void setLeftWall(Wall leftWall) {
        Wall.leftWall = leftWall;
    }

    public static Wall getRightWall() {
        return rightWall;
    }

    public static void setRightWall(Wall rightWall) {
        Wall.rightWall = rightWall;
    }

    public static void spawnMainWalls(){
        //spawn boarder walls
        topWall=new Wall(0,0,true,wallImages[0]);
        bottomWall=new Wall(0,650,true,wallImages[1]);
        leftWall=new Wall(0,0,true,wallImages[2]);
        rightWall=new Wall(1250,0,true,wallImages[3]);

        topWall.setTranslateXY(topWall.getPosX(), topWall.getPosY());
        bottomWall.setTranslateXY(bottomWall.getPosX(), bottomWall.getPosY());
        leftWall.setTranslateXY(leftWall.getPosX(), leftWall.getPosY());
        rightWall.setTranslateXY(rightWall.getPosX(), rightWall.getPosY());

        EntityManager.addEntity(topWall);
        EntityManager.addEntity(bottomWall);
        EntityManager.addEntity(leftWall);
        EntityManager.addEntity(rightWall);

    }

}
