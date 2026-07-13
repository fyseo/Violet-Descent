package com.example.crawler.MainClasses;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Entity {
    private double posX, posY;
    private boolean alive;
    private ImageView imageView;
    private Image image;

    public  Entity(double posX, double posY, boolean alive, Image image){
        this.posX = posX;
        this.posY = posY;
        this.alive = alive;
        this.image = image;
        this.imageView=new ImageView(image);
        this.setTranslateXY(posX, posY);
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        //sets both image and image view
        this.image = image;
        this.imageView.setImage(image);
    }

    public void setTranslateXY(double x, double y){
        //method to move entity view with given x y cords
        if (this.imageView != null) {
            this.imageView.setTranslateX(x);
            this.imageView.setTranslateY(y);
        }
    }

    public boolean isCollidingWithEntity(Entity other){
        //collision logic implemented by checking images intersection
        double myLeft = this.getPosX();
        double myTop = this.getPosY();
        double myRight = this.getPosX()+this.getImage().getWidth();
        double myBottom = this.getPosY()+this.getImage().getHeight();

        double otherLeft = other.getPosX();
        double otherTop = other.getPosY();
        double otherRight = other.getPosX()+other.getImage().getWidth();
        double otherBottom = other.getPosY()+other.getImage().getHeight();

        return (myLeft < otherRight) && (myTop < otherBottom) && (myRight > otherLeft) && (myBottom > otherTop);
    }

}
