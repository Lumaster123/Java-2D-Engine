package engine.physic;

import engine.ThreadHandler;
import engine.Time;
import engine.components.Config;
import engine.components.entity.Entity;
import engine.console.ConsoleManager;
import java.util.ArrayList;

public class Physic_GravitySystem implements Runnable{
    
     private ArrayList<Object> objects;
     
     private final float g = 1.05f;

    public Physic_GravitySystem() {
        objects = new ArrayList<>();

        ThreadHandler.invoke(this);
    }

    public void addEntity(Object object) {
        this.objects.add(object);
    }

    @Override
    public void run() {
        
        long timeStart = System.nanoTime();
        long timeEnd = 0;
        long sleep = 0;
        float usedTime = 0;
        
        while(true){
            long timestamp = System.nanoTime();
            ArrayList<Object> list = (ArrayList<Object>) objects.clone();
            for (Object o : list) {
                if(o instanceof Entity){
                    Entity e = (Entity) o;
                    if(e.isInAir()){
//                        float update = (float)(e.getSpeedY()+(0.5*g*e.getAirTime()));
                        float update = 0.1f;
                        if(e.getSpeedY() > 50){
                            
                        }else{
                            e.setSpeedY(e.getSpeedY()+update);
                        }
                        
    //                    ConsoleManager.writeOnConsole("[GravitySystem] ", ""+update);
                        e.setAirTime(e.getAirTime()+0.001f);
                    }else{
                        e.setSpeedY(0);
                    }
                }else if(o instanceof Physic_WorldPart){
                    Physic_WorldPart w = (Physic_WorldPart) o;
                    if(w.isInAir()){
    //                    float update = (float)(e.getSpeedY()+(0.5*g*e.getAirTime()));
                        float update = 0.2f;
                        w.setSpeedY(update);
    //                    ConsoleManager.writeOnConsole("[GravitySystem] ", ""+update);
                        w.setAirTime(w.getAirTime()+0.001f);
                    }else{
                        w.setSpeedY(0);
                    }
                }else{
                    System.out.println("Not Gravity compatible!");
                }
            }
            timeEnd = System.nanoTime();
            
            usedTime = ((float)timeEnd - (float)timeStart) / 1000000f;
            sleep = 1000/Config.GRAVITY_TICKS;
            
            if (usedTime >= sleep) {
                ConsoleManager.writeOnConsole("[GravitySystem] ", "To slow! "+usedTime);
            } else {
                Time.sleep(sleep - usedTime);
            }
            timeStart = System.nanoTime();
        }
    }

    
}
