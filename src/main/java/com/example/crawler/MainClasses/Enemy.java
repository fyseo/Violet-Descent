package com.example.crawler.MainClasses;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Character{

    private Player target;
    private double bulSpeed;
    private final double knockback=20;

    private static final AudioClip shootingSound = new AudioClip(Objects.requireNonNull(Enemy.class.getResource("/com/example/crawler/Audio/enemyShoot.wav")).toString());

    public static final Image[] enemyImages ={
            new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Enemy/enemy.png")))
    };

    public Enemy(double posX, double posY, double maxHealth, double fireRate, Player target, double bulSpeed) {
        super(posX, posY, true ,enemyImages[0], maxHealth, fireRate);
        this.target = target;
        this.bulSpeed = bulSpeed;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public double getBulSpeed() {
        return bulSpeed;
    }

    public void setBulSpeed(double bulSpeed) {
        this.bulSpeed = bulSpeed;
    }

    public double getKnockback() {
        return knockback;
    }

    @Override
    public void onCollision(Entity e) {
        //take damage if enemy is hit by player bullet and delete bullet
        if(e instanceof Projectile p && ! p.isEnemyBullet()){
            this.takeDamage(1);

            EntityManager.removeEntity(p);
        }

    }

    @Override
    public void update(double deltaTime) {
        //set enemy position
        this.setTranslateXY(this.getPosX(),this.getPosY());

        //make enemy look towards player
        if(target.getPosX()>this.getPosX()){
            this.getImageView().setScaleX(-1);
        }else if(target.getPosX()<this.getPosX()){
            this.getImageView().setScaleX(1);
        }
        //shooting with cooldown
        if(this.getCooldown() <= 0) {
            //play sound and shoot bullet, then set cooldown
            Enemy.shootingSound.play(0.05);
            shoot(target.getImageView().getBoundsInParent().getCenterX(), target.getImageView().getBoundsInParent().getCenterY(),this.bulSpeed);
            this.setCooldown(this.getFireRate());
        }

        setCooldown(getCooldown() - deltaTime);
    }

    public static void generateEnemies(int level,Player player) {
        //handles enemy generation
        //the bounds where the enemy can generate
        double xSpawnMax=GameManager.getWidth()-Wall.getRightWall().getImage().getWidth()-85;//to be adjusted///////////////limit no enemies spawn///////
        double ySpawnMax=GameManager.getHeight()-Wall.getBottomWall().getImage().getHeight()-88;
        double xSpawnMin=Wall.getLeftWall().getImage().getWidth()+300;
        double ySpawnMin=Wall.getTopWall().getImage().getHeight()+25;

        ArrayList<Enemy> levelEnemies=new ArrayList<>();
        //repeat for the number of enemies needed
        for(int i=0;i<level-(((int)(level/5))*4);i++) {
            //loops till enemy is spawned (cant be more than 100 enemies)
            if(i>=100) break;
            while (true) {
                boolean isEmpty = true;

                double randX = ThreadLocalRandom.current().nextDouble(xSpawnMin, xSpawnMax);
                double randY = ThreadLocalRandom.current().nextDouble(ySpawnMin, ySpawnMax);
                //create temp enemy with rand x and y
                Enemy current = new Enemy(randX, randY, (int)((level * 0.2)+1), Math.exp(-0.05 * level), player, Math.exp(0.3*level) + 400);
                current.setTranslateXY(randX, randY);

                for (Entity e : EntityManager.getEntities()) {

                    if (e instanceof Enemy && current.isCollidingWithEntity(e)) {
                        isEmpty = false;
                        break;
                    }

                }

                if(isEmpty) {
                    for(Entity e : levelEnemies) {
                        //if the generated enemy collides with a run the loop again
                        if(current.isCollidingWithEntity(e)) {
                            isEmpty = false;
                            break;
                        }
                    }
                }

                if (isEmpty) {
                    EntityManager.addEntity(current);
                    levelEnemies.add(current);
                    break;
                }
            }
        }
    }
}
