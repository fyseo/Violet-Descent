package com.example.crawler.MainClasses;

import com.example.crawler.GameController;
import javafx.scene.image.Image;


abstract public class Character extends Entity implements DynamicEntity {

    private double health;
    private double cooldown;
    private double fireRate;

    public Character(double posx, double posy, boolean alive, Image view, double health, double fireRate){
        super(posx, posy, alive, view);
        this.health = health;
        this.cooldown =0;
        this.fireRate = fireRate;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public double getFireRate() {
        return fireRate;
    }

    public void setFireRate(double fireRate) {
        this.fireRate = fireRate;
    }

    public void takeDamage(double damage) {
        //decrease health by given amount
        this.health -= damage;
        //update health view if character is an instance of player
        if(this instanceof Player)
            GameController.updateHearts(this.health);
        //handles non player entities death
        if (this.health <= 0)
        {
            if(!(this instanceof Player)) {
                this.setAlive(false);
            }
        }
    }

    public void shoot(double dirX,double dirY,double speed){
        //handles shooting mechanic depending on the shooter
        //by getting a vector in the direction of the target and updating on that
        double velX=0,velY=0;

        //cords depends on the image
        if(this instanceof Enemy e) {
            velX = dirX - 11 - this.getPosX();
            velY = dirY -30 -this.getPosY();

        }else if(this instanceof Player p) {
            velX = dirX -16 - this.getPosX();
            velY = dirY -30 -this.getPosY();

        }
        
        double distance= Math.sqrt(velX*velX + velY*velY);

        if(distance !=0){
             velX=velX/distance;
             velY=velY/distance;

        }

        velX=velX*speed;
        velY=velY*speed;


        if(this instanceof Enemy) {
            Projectile bullet = new Projectile(this.getPosX() + 11, this.getPosY() + 30, Projectile.getProjectileImages()[1], velX, velY, true);
            EntityManager.addEntity(bullet);
        } else if (this instanceof Player) {
            Projectile bullet = new Projectile(this.getPosX() + 11, this.getPosY() + 30, Projectile.getProjectileImages()[0], velX, velY, false);
            EntityManager.addEntity(bullet);
        }

    }

}

