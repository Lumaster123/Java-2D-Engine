package engine.physic;

import engine.ThreadHandler;
import engine.Time;
import engine.components.Config;
import engine.console.ConsoleManager;
import java.util.ArrayList;

public class Physic_MovementSystem implements Runnable {

    private ArrayList<Physic_MoveListener> listener;

    public Physic_MovementSystem() {
        listener = new ArrayList<>();

        ThreadHandler.invoke(this);
    }

    public void addMoveListener(Physic_MoveListener listener) {
        this.listener.add(listener);
    }

    @Override
    public void run() {
        
        long timeStart = System.nanoTime();
        long timeEnd = 0;
        long sleep = 0;
        long usedTime = 0;
        
        while(true){
            ArrayList<Physic_MoveListener> list = (ArrayList<Physic_MoveListener>) listener.clone();
            for (Physic_MoveListener l : list) {
                l.move();
            }
            timeEnd = System.nanoTime();
            
            usedTime = (timeEnd - timeStart) / 1000000;
            sleep = 1000/Config.GAME_TICKS;
            
            if (usedTime >= sleep) {
                ConsoleManager.writeOnConsole("[MovementSystem] ", "To slow! "+usedTime);
            } else {
                Time.sleep(sleep - usedTime);
            }
            timeStart = System.nanoTime();
        }
    }

}
