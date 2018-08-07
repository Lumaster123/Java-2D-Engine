package engine.physic;

import engine.ThreadHandler;
import engine.Time;
import engine.components.entity.Entity;
import java.util.ArrayList;

public class Physic_Collision_blockVSentity implements Runnable {

    private ArrayList<Physic_WorldPart> worldParts;
    private ArrayList<Entity> entitys;

    private boolean finished, running, start;

    public Physic_Collision_blockVSentity() {

        ThreadHandler.invoke(this);

    }

    @Override
    public void run() {
        start = false;
        finished = true;
        running = false;

        while (true) {
            if (start) {
                start = false;
                finished = false;
                running = true;

                for (Entity e : entitys) {
                    Physic_Collision_ObjectDirections od = new Physic_Collision_ObjectDirections();

                    float entityX = 0, entityY;

                    entityX = e.getX();
//                    float abstandWeltCameraUnten = e.getWorld().get
                    entityY = e.getWorld().getWindow().getContentPane().getHeight()-(e.getY());
                    entityY += e.getPhysicHeight()/4;
//                    System.out.println("Gravity entity y: "+entityY+"\twindow: "+e.getWorld().getWindow().getDefaultHeight());
                    
                    e.ex = e.getX();
                    e.ey = entityY;

                    int mode = 0;

                    for (Physic_WorldPart w : worldParts) {
                        boolean[] directions = new boolean[4];

                        if ((entityY - e.getPhysicHeight() / 2 - 1) < (w.getY() + w.getHeight())) {
                            directions[Direction.DOWN.getValue()] = true;
                        } else {
                            directions[Direction.DOWN.getValue()] = false;
                        }
                        if ((entityY + e.getPhysicHeight() / 2 - 6) > (w.getY())) {
                            directions[Direction.UP.getValue()] = true;
                        } else {
                            directions[Direction.UP.getValue()] = false;
                        }
                        if ((entityX + e.getPhysicWidth() + 1) > (w.getX())) {
                            directions[Direction.RIGHT.getValue()] = true;
                        } else {
                            directions[Direction.RIGHT.getValue()] = false;
                        }
                        if ((entityX - 1) < (w.getX() + w.getWidth())) {
                            directions[Direction.LEFT.getValue()] = true;
                        } else {
                            directions[Direction.LEFT.getValue()] = false;
                        }
                        

//                        System.out.println("UP: "+directions[0] + "\tDOWN: " + directions[1] + "\tLEFT: " + directions[2] + "\tRIGHT: " + directions[3]);
                        od.addObject(w, directions);

                    }

                    boolean isInAir = true;

                    for (int i = 0; i < od.getLength(); i++) {
                        boolean[] dir = od.getDirections(i);
                        Physic_WorldPart w = (Physic_WorldPart) od.getObject(i);

                        double ak = (entityX + e.getPhysicWidth() / 2) - (w.getX() + w.getWidth() / 2);
                        double gk = (entityY) - (w.getY() + w.getHeight() / 2);

                        double degree = Math.toDegrees(Math.atan(Math.abs(gk) / Math.abs(ak)));

                        if (ak < 0 && gk >= 0) {
                            degree = 180 - degree;
                        } else if (ak < 0 && gk < 0) {
                            degree = 180 + degree;
                        } else if (ak >= 0 && gk < 0) {
                            degree = 360 - degree;
                        }

                        if (degree >= 50 && degree < 130) {
                            mode = 1;
                        } else if (degree >= 130 && degree < 230) {
                            mode = 2;
                        } else if (degree >= 230 && degree < 310) {
                            mode = 3;
                        } else if ((degree >= 310 && degree <= 360) || (degree >= 0 && degree < 50)) {
                            mode = 4;
                        }

                        if (dir[Direction.DOWN.getValue()]
                                && dir[Direction.LEFT.getValue()]
                                && dir[Direction.RIGHT.getValue()]
                                && dir[Direction.UP.getValue()]) {

                            if (mode == 1) {

                                e.setInAir(false);
                                e.setAirTime(0);
                                float distance = (entityY - e.getHeight() / 2 - 1) - (w.getY() + w.getHeight());

                                if (distance < -20) {
                                    e.setY((float) (e.getY()) - 10f);
                                } else if (distance < -10) {
                                    e.setY((float) (e.getY()) - 6.5f);
                                } else if (distance < -4) {
                                    e.setY((float) (e.getY()) - 3.2f);
                                } else if (distance < -2) {
                                    e.setY((float) (e.getY()) - 1.3f);
                                } else if (distance < -0.7f) {
                                    e.setY((float) (e.getY()) - 0.2f);
                                }
                                
                                isInAir = false;

                            } else if (mode == 2) {

                                float distance = (entityX + e.getPhysicWidth()) - (w.getX());
                                
                                if (distance > 1) {
                                    e.setX(e.getX() - distance - 1);
                                }

                            }

                            if (mode == 3) {

                                float distance = (entityY + e.getPhysicHeight() / 2 - 6) - (w.getY());
                                
                                e.setY((float) (e.getY()) + distance);
                                
                                
                                
                            }

                            if (mode == 4) {

                                float distance = (entityX) - (w.getX() + w.getWidth());
                                
                                if(distance < -1){
                                    e.setX(e.getX() - distance );
                                }

                            }
                            
                            if(mode == 1){
                                e.collision(w, Direction.DOWN);
                            }else if(mode == 2){
                                e.collision(w, Direction.RIGHT);
                            }else if(mode == 2){
                                e.collision(w, Direction.UP);
                            }else if(mode == 2){
                                e.collision(w, Direction.LEFT);
                            }
                            
                            
                        } else {
                            mode = 0;
                        }
                        
                    }

                    if (isInAir && mode == 0) {
                        e.setInAir(true);
                    }

                }

                finished = true;
                running = false;

            }

            Time.sleep(0.01);

        }
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isRunning() {
        return running;
    }

    public void startRound(ArrayList<Physic_WorldPart> worldParts, ArrayList<Entity> entitys) {
        this.worldParts = worldParts;
        this.entitys = entitys;
        start = true;
    }

}
