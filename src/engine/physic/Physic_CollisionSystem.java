package engine.physic;

import engine.ThreadHandler;
import engine.Time;
import engine.components.Config;
import engine.components.entity.Entity;
import engine.console.ConsoleManager;
import java.util.ArrayList;

public class Physic_CollisionSystem implements Runnable {

    private Physic_Collision_blockVSblock bb;
    private Physic_Collision_blockVSentity be;
    private Physic_Collision_entityVSentity ee;
    
    private ArrayList<Object> objects;

    private ArrayList<Physic_WorldPart> worldParts;
    private ArrayList<Entity> entitys;

    public Physic_CollisionSystem() {
        bb = new Physic_Collision_blockVSblock();
        be = new Physic_Collision_blockVSentity();
        ee = new Physic_Collision_entityVSentity();
        
        objects = new ArrayList<>();

        worldParts = new ArrayList<>();
        entitys = new ArrayList<>();

        ThreadHandler.invoke(this);
    }

    public void addObject(Object object) {
        if (object instanceof Entity || object instanceof Physic_WorldPart) {
            this.objects.add(object);
            if(object instanceof Entity)
                entitys.add((Entity)object);
            if(object instanceof Physic_WorldPart)
                worldParts.add((Physic_WorldPart)object);
        } else {
            ConsoleManager.writeOnConsole("[Physic_CollisionSystem] ", "The Object can't have collision. It must be a part of an Entity or an Physic_WorldPart!");
        }
    }

    @Override
    public void run() {

        long timeStart = System.nanoTime();
        long timeEnd = 0;
        long sleep = 0;
        long usedTime = 0;

        while (true) {
            ArrayList<Object> list = (ArrayList<Object>) objects.clone();
            ArrayList<Entity> entitys = (ArrayList<Entity>) this.entitys.clone();
            ArrayList<Physic_WorldPart> worldParts = (ArrayList<Physic_WorldPart>) this.worldParts.clone();
            

            if(be.isFinished()){
                be.startRound(worldParts, entitys);
            }else{
                
            }
            

            timeEnd = System.nanoTime();

            usedTime = (timeEnd - timeStart) / 1000000;
            sleep = 1000 / Config.COLLISION_TICKS;

            if (usedTime >= sleep) {
                ConsoleManager.writeOnConsole("[CollisionSystem] ", "To slow!");
            } else {
                Time.sleep(sleep - usedTime);
            }
            timeStart = System.nanoTime();
        }
    }

}
