package engine.components.entity;

import engine.components.Keyboard;
import engine.components.world.World;
import engine.physic.PhysicSystem;
import engine.physic.Physic_CollisionListener;
import engine.physic.Physic_MoveListener;
import engine.rendering.Renderer;
import java.awt.Point;

public abstract class Entity extends engine.rendering.RenderableObject implements Physic_CollisionListener{
    
    private static int STATIC_ID = 1000;
    protected final int id;
    
    protected World world;
    
    protected Keyboard keyboard;
    
    protected float speedX, speedY;
    
    protected float airTime;
    protected boolean inAir;
    
    protected int collision;
    protected boolean gravity;
    
    public float ex,ey;
    
    public float physicWidth, physicHeight;
    
    public Entity(World world, float x, float y, float width, float height, Renderer.Layer layer) {
        super(layer, x, y, width, height);
        
        id = STATIC_ID;
        STATIC_ID++;
        
        this.world = world;
        
        airTime = 0;
        inAir = false;
        
        this.collision = PhysicSystem.COLLISION_MODE_NONE;
        this.gravity = PhysicSystem.GRAVITY_OFF;
        
        initialize();
    }
    
    
    public Entity(World world, float x, float y, float width, float height, Renderer.Layer layer, Keyboard keyboard) {
        super(layer, x, y, width, height);
        
        id = STATIC_ID;
        STATIC_ID++;
        
        this.world = world;
        
        this.keyboard = keyboard;
        
        this.collision = PhysicSystem.COLLISION_MODE_NONE;
        this.gravity = PhysicSystem.GRAVITY_OFF;
        
        initialize();
    }
    
   
    public Entity(World world, float x, float y, float width, float height, Renderer.Layer layer, int collision_mode, boolean gravity) {
        super(layer, x, y, width, height);
        
        id = STATIC_ID;
        STATIC_ID++;
        
        this.world = world;
        
        airTime = 0;
        inAir = false;
        
        collision = collision_mode;
        this.gravity = gravity;
        
        initialize();
    }
    
    
    public Entity(World world, float x, float y, float width, float height, Renderer.Layer layer, Keyboard keyboard, int collision_mode, boolean gravity) {
        super(layer, x, y, width, height);
        
        id = STATIC_ID;
        STATIC_ID++;
        
        this.world = world;
        
        this.keyboard = keyboard;
        
        collision = collision_mode;
        this.gravity = gravity;
        
        initialize();
    }
    
    private void initialize(){
        
        if(this instanceof Physic_MoveListener){
            PhysicSystem.addMoveListener((Physic_MoveListener)this);
        }
        if(gravity){
            PhysicSystem.addObjectForGravitation(this);
            inAir = true;
        }
        if(collision > 0 && collision <= 3){
            PhysicSystem.addCollisionObject(this);
        }
        
        
        
    }

    public Point getCenter(){
        return new Point((int)(x+width/2), (int)(y+height/2));
    }
    
    public void setPhysicHeight(float physicHeight) {
        this.physicHeight = physicHeight;
    }

    public void setPhysicWidth(float physicWidth) {
        this.physicWidth = physicWidth;
    }

    public float getPhysicHeight() {
        return physicHeight;
    }

    public float getPhysicWidth() {
        return physicWidth;
    }
    
    
    public void setKeyboard(Keyboard keyboard){
        this.keyboard = keyboard;
    }

    public void setAirTime(float airTime) {
        this.airTime = airTime;
    }

    public float getAirTime() {
        return airTime;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public boolean isInAir() {
        return inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    
    
}
