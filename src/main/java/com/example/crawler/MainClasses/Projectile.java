package com.example.crawler.MainClasses;

import javafx.scene.image.Image;
import java.util.Objects;

public class Projectile extends Entity implements DynamicEntity {

    private double velX;
    private double velY;
    private boolean isEnemyBullet;
    private static final Image[] projectileImages= {
            new Image(Objects.requireNonNull(Projectile.class.getResourceAsStream("/com/example/crawler/Images/Player/playerBullet.gif"))),
            new Image(Objects.requireNonNull(Projectile.class.getResourceAsStream("/com/example/crawler/Images/Enemy/enemyBullet.gif")))
    };

    public Projectile(double posX, double posY, Image image, double velX, double velY, boolean isEnemyBullet) {
        super(posX, posY, true, image);
        this.velX = velX;
        this.velY = velY;
        this.isEnemyBullet=isEnemyBullet;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public boolean isEnemyBullet() {
        return isEnemyBullet;
    }

    public void setEnemyBullet(boolean enemyBullet) {
        isEnemyBullet = enemyBullet;
    }

    public static Image[] getProjectileImages(){
        return projectileImages;
    }

    @Override
    public void update(double deltaTime) {
        //update bullets
        this.setPosX(this.getPosX() + (velX * deltaTime));
        this.setPosY(this.getPosY() + (velY * deltaTime));

        setTranslateXY(this.getPosX(), this.getPosY());

    }

    @Override
    public void onCollision(Entity e) {
        //kill bullet if it hits anything
        if(e instanceof Wall||e instanceof Door){
            this.setAlive(false);
        }
    }
}
