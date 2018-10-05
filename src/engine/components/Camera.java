package engine.components;

import engine.ThreadHandler;
import engine.Time;
import engine.components.entity.Entity;
import engine.window.Window;

public class Camera {
    
    private Window window;
    
    public float x, y, width, height, speedX, speedY, togoX, togoY;
    private boolean working;
    
    private Entity entity;
    private boolean followEntity;
    
    public Camera(Window window, float x, float y){
        this.window = window;
        
        working = false;
        this.x = x;
        this.y = y;
        this.width = window.getContentPane().getWidth();
        this.height = window.getContentPane().getHeight();
        
        entity = null;
        followEntity = false;
        
    }
    
    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }
    
    public void updatePosition(float x, float y){
        this.x += x;
        this.y += y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    
    public void focusEntity(Entity entity, int leftBorderInPercent, int rightBorderInPercent, int topBorderInPercent, int bottomBorderInPercent){
        
        if(this.entity != null){
            followEntity = false;
            Time.sleep(0.1);
            this.entity = null;
        }
        
        this.entity = entity;
        followEntity = true;
        
        ThreadHandler.invoke(new Runnable() {

            @Override
            public void run() {
                
                while(true){
                    if(!followEntity){
                        break;
                    }
                    
//                    float rs = window.getDefaultWidth()*0.6f;
//                    float ls = window.getDefaultWidth()*0.2f;
//                    float us = window.getDefaultHeight()*0.2f;
//                    float ds = window.getDefaultHeight()*0.8f;
                    
                    float rs = window.getDefaultWidth()*rightBorderInPercent/100;
                    float ls = window.getDefaultWidth()*leftBorderInPercent/100;
                    float us = window.getDefaultHeight()*topBorderInPercent/100;
                    float ds = window.getDefaultHeight()*bottomBorderInPercent/100;
                    
                    float x = 0, y = 0;
                    
                    if((entity.getX()+entity.getWidth())>(rs+getX())){
                        x = (entity.getX()+entity.getWidth())-(rs+getX());
                    }else if((entity.getX())<(ls+getX())){
                        x = (entity.getX())-(ls+getX());
                    }
                    if((entity.getY()+entity.getHeight())>(ds+getY())){
                        y = (entity.getY()+entity.getHeight())-(ds+getY());
                    }else if((entity.getY())<(us+getY())){
                        y = (entity.getY())-(us+getY());
                    }
                    
                    
                    togoX = getX()+x;
                    togoY = getY()+y;
                    
                    slideDirectToWithPositionUpdate(20);
                    
                    
                }
                
                
                
            }
        });
        
        
    }
    
    private void slideDirectToWithPositionUpdate(double ms){
        if(working){
            throw new Error("Camera can only do one animation at a time!");
        }
        working = true;
        if(ms < 1){
            throw new Error("Use setPosition(float x, float y) instead when ms under 1!");
        }
        
        ThreadHandler.invoke(new Runnable() {
            @Override
            public void run() {
                int counter = 0;
                while(counter <= ms){
                    if(!working){
                        break;
                    }
                    
                    speedX = (float) ((togoX-getX())/ms);
                    speedY = (float) ((togoY-getY())/ms);
                    
                    updatePosition(speedX, speedY);
                    Time.sleep(1);
                    
                    counter++;
                }
                setPosition(x, y);
                working = false;
            }
        });
        while(working){Time.sleep(0.01);}
    }
    
    
    public void slideDirectTo(float x, float y, double ms){
        if(working){
            throw new Error("Camera can only do one animation at a time!");
        }
        working = true;
        if(ms < 1){
            throw new Error("Use setPosition(float x, float y) instead when ms under 1!");
        }
        final float sX = (float) ((x-this.x)/ms);
        final float sY= (float) ((y-this.y)/ms);
        final float xMax = this.x - x;
        final float yMax = this.y - y;
        boolean xUpTemp = true;
        boolean yUpTemp = true;
        if(this.x > x)
            xUpTemp = false;
        if(this.y > y)
            yUpTemp = false;
        final boolean xUp = xUpTemp;
        final boolean yUp = yUpTemp;
        ThreadHandler.invoke(new Runnable() {
            @Override
            public void run() {
                int counter = 0;
                while(counter <= ms){
                    if(!working){
                        break;
                    }
                    
                    if(xUp){
                        if(getX() + sX < xMax)
                            break;
                    }else{
                        if(getX() + sX > xMax)
                            break;
                    }
                    if(yUp){
                        if(getY() + sY < yMax)
                            break;
                    }else{
                        if(getY() + sY > yMax)
                            break;
                    }
                    updatePosition(sX, sY);
                    Time.sleep(1);
                    
                    counter++;
                }
                setPosition(x, y);
                working = false;
            }
        });
        while(working){Time.sleep(0.01);}
    }
    
    
    
}
