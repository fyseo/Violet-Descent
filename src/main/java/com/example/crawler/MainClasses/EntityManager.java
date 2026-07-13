package com.example.crawler.MainClasses;

import javafx.scene.Group;
import java.util.ArrayList;

public class EntityManager {

    private static ArrayList<Entity> entities=new ArrayList<>();
    private static ArrayList<Entity> queue=new ArrayList<>();

    public static ArrayList<Entity> getEntities() {
        return entities;
    }

    public static void addEntity(Entity e){
        //add entity to queue list and adds its image view to root(scene)
        queue.add(e);
        GameManager.getRoot().getChildren().add(e.getImageView());
    }

    public static void removeEntity(Entity e){
        //if entity instance of enemy dec no enemies and inc no kills
        if(e instanceof Enemy) {
            GameManager.setNoEnemies(GameManager.getNoEnemies()-1);
            GameManager.setNoKills(GameManager.getNoKills()+1);
        }
        //set entity alive to false(kills entity later)
        e.setAlive(false);

        //removes entity view to prevent more collision before entity is removed from entity list
        if (e.getImageView().getParent() instanceof Group root) {
            root.getChildren().remove(e.getImageView());
        }
    }

    public static void removeAllEntities(){
        // removes enemies and bullets (used when generating a new level)
        for(Entity x: entities){
            if(x instanceof Projectile || x instanceof  Enemy){
                EntityManager.removeEntity(x);
            }
        }
    }

    public static void clearGameEntities(){
        //clears ALL entities completely from entity manager(no exclusion)
        entities.clear();
        queue.clear();
    }

    public void update(double deltaTime) {
        //nested loop to check all entities against each other for collision
        for(Entity a:entities){
            //only continue if entity is alive
            if (!a.isAlive())
                continue;
            //update the entity if it implements dynamic entity
            if(a instanceof DynamicEntity dyn)
                dyn.update(deltaTime);

            //second loop
            for(Entity b:entities){
                //only continue if entity is alive
                if(a==b || !b.isAlive()) continue;

                //if first entity is colliding with second entity and the first entity
                // implements dynamic entity call its collision method
                if(a.isCollidingWithEntity(b)) {
                    if(a instanceof DynamicEntity dyn)
                        dyn.onCollision(b);
                }
            }
            //if entity is dead remove it
            if(!a.isAlive()) {
                EntityManager.removeEntity(a);
            }

        }
        //updates timer for the timer label
        GameManager.elapsedTime(deltaTime);
        //remove it from main entity list (do it here to prevent ghosting)
        entities.removeIf(e -> !e.isAlive());
        entities.addAll(queue);
        queue.clear();

    }

}
