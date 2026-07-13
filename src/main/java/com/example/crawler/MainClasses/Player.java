package com.example.crawler.MainClasses;

import com.example.crawler.GameController;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import java.util.HashSet;
import java.util.Objects;

public class Player extends Character{

    private double mouseX, mouseY, maxHealth;
    private static final double playerBulletSpeed=1200, speed=500, fireRate=0.25,stepRate=0.17,animationRate=0.11;
    private HashSet<String> keys;
    private int animationCount,stepCount;
    private double animationCooldown,stepCooldown;


    private static final AudioClip healingSound = new AudioClip(Objects.requireNonNull(Player.class.getResource("/com/example/crawler/Audio/healing.wav")).toString());
    private static final AudioClip deathSound = new AudioClip(Objects.requireNonNull(Player.class.getResource("/com/example/crawler/Audio/gameover.wav")).toString());
    private static final AudioClip shootingSound = new AudioClip(Objects.requireNonNull(Player.class.getResource("/com/example/crawler/Audio/playerShoot.wav")).toString());
    private static final AudioClip[] footStepSounds = {
            new AudioClip(Objects.requireNonNull(Player.class.getResource("/com/example/crawler/Audio/step1.wav")).toString()),
            new AudioClip(Objects.requireNonNull(Player.class.getResource("/com/example/crawler/Audio/step2.wav")).toString())
    };


    private static final Image[][] playerImages = {
            {
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerRight.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerRight1.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerRight.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerRight2.png")))
            },
            {
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerLeft.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerLeft1.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerLeft.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerLeft2.png")))
            },
            {
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerUp.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerUp1.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerUp.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerUp2.png")))
            },
            {
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerDown.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerDown1.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerDown.png"))),
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerDown2.png")))
            },
            {
                new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/com/example/crawler/Images/Player/playerDead.png")))
            }

    };

    public Player(HashSet<String> keys , double posx, double posy, double maxHealth) {
        super(posx, posy, true,playerImages[0][1],maxHealth,fireRate);
        this.maxHealth=maxHealth;
        this.mouseX = 0;
        this.mouseY = 0;
        this.keys = keys;
        this.animationCount=0;
        this.stepCount=0;
        this.stepCooldown=0;
    }

    public double getPlayerBulletSpeed() {
        return playerBulletSpeed;
    }

    public double getMouseX() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public double getSpeed() {
        return speed;
    }

    public HashSet<String> getKeys() {
        return keys;
    }

    public void setKeys(HashSet<String> keys) {
        this.keys = keys;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public static Image[][] getPlayerImages(){
        return playerImages;
    }

    public void heal(){
        //heals player, updates hear view and plays sound
        this.setHealth(this.getMaxHealth());
        GameController.updateHearts(this.getMaxHealth());
        healingSound.play(1);
    }

    public static AudioClip getShootingSound() {
        return shootingSound;
    }

    public static AudioClip[] getFootStep() {
        return footStepSounds;
    }

    @Override
    public void update(double deltaTime){
        double dx=0,dy=0;
        //update all cooldowns
        this.animationCooldown-=deltaTime;
        this.stepCooldown-=deltaTime;
        this.setCooldown(this.getCooldown() - deltaTime);

        //handles player movement
        if (animationCount >=4) animationCount =0;

        if (keys.contains("W")) {
            if(animationCooldown<=0) {
                this.setImage(playerImages[2][animationCount]);
                animationCount++;
                animationCooldown=animationRate;
                if (animationCount >=4) animationCount =0;
            }

            dy -= 1;
        }
        if (keys.contains("A")) {
            if(animationCooldown<=0) {
                this.setImage(playerImages[1][animationCount]);
                animationCount++;
                animationCooldown=animationRate;
                if (animationCount >=4) animationCount =0;
            }
            dx-=1;
        }
        if (keys.contains("S")){
            if(animationCooldown<=0) {
                this.setImage(playerImages[3][animationCount]);
                animationCount++;
                animationCooldown=animationRate;
                if (animationCount >=4) animationCount =0;
            }
            dy+=1;

        }
        if (keys.contains("D")){
            if(animationCooldown<=0) {
                this.setImage(playerImages[0][animationCount]);
                animationCount++;
                animationCooldown=animationRate;
                if (animationCount >=4) animationCount =0;
            }
            dx+=1;
        }
        //player movement sound
        if((dx!=0||dy!=0)){

            if(stepCooldown<=0) {
                footStepSounds[stepCount].stop();
                footStepSounds[stepCount].play(0.05);
                stepCount = (stepCount + 1) % footStepSounds.length;
                this.stepCooldown = Player.stepRate;
            }

        }

        //handle player shooting
        if (keys.contains("PRIMARY") && this.getCooldown() <= 0){
            Player.getShootingSound().play(0.05);
            shoot(mouseX,mouseY,this.getPlayerBulletSpeed());
            this.setCooldown(this.getFireRate());

        }

        //calculate movement from keys input
        double distance= Math.sqrt(dx*dx + dy*dy);

        if(distance !=0){
            dx=dx/distance;
            dy=dy/distance;

        }

        this.setPosX(this.getPosX() + dx * deltaTime*this.getSpeed());
        this.setPosY(this.getPosY() + dy * deltaTime*this.getSpeed());

        //no outbound check(dont hit walls)
        if(this.getPosY()<50) this.setPosY(50);//top wall
        if(this.getPosX()<50) this.setPosX(50);//left wall
        if(this.getPosY()>580) this.setPosY(580);//bottom wall
        if(this.getPosX()>1213) this.setPosX(1213);//right wall
        //final move
        this.setTranslateXY(this.getPosX(), this.getPosY());

    }

    @Override
    public void onCollision(Entity e){

        //stop if player is dead
        if(!e.isAlive())
            return;

        //if a player collides with an enemy bullet take damage
        if(e instanceof Projectile p && p.isEnemyBullet()){

            this.takeDamage(1);
            //kill player if his health is 0
            if(this.getHealth()<=0) {
                GameManager.setGameStatus(false);
                Player.deathSound.play(0.5);
            }

            EntityManager.removeEntity(p);

        }
        //if a player collides with an enemy take damage and take knock back
        if (e instanceof Enemy em) {
           this.takeDamage(1);
            if (this.getHealth() <= 0) {
                //kill
                GameManager.setGameStatus(false);
                Player.deathSound.play(0.5);
            }
            if (this.getPosX() < em.getPosX()) {
                this.setPosX(this.getPosX() - em.getKnockback());
            } else {
                this.setPosX(this.getPosX() + em.getKnockback());
            }

            if (this.getPosY() < em.getPosY()) {
                this.setPosY(this.getPosY() - em.getKnockback());
            } else {
                this.setPosY(this.getPosY() + em.getKnockback());
            }
        }
    }
}
