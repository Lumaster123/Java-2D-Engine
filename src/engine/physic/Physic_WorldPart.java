package engine.physic;

public abstract class Physic_WorldPart {
    
    protected float x, y, width, height, airTime, speedX, speedY;
    protected int physic_mode, gravity;
    protected boolean inAir;

    public Physic_WorldPart(float x, float y, float width, float height, int physic_mode, int gravity) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.physic_mode = physic_mode;
        this.gravity = gravity;
        this.inAir = true;
        
        initialize();
    }
    
    private void initialize(){
        
        if(this instanceof Physic_MoveListener){
            PhysicSystem.addMoveListener((Physic_MoveListener)this);
        }
        if(gravity == 1){
            PhysicSystem.addObjectForGravitation(this);
        }
        if(physic_mode > 0 && physic_mode <= 3){
            PhysicSystem.addCollisionObject(this);
        }
        
        
        inAir = true;
        
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getGravity() {
        return gravity;
    }

    public int getPhysic_mode() {
        return physic_mode;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public boolean isInAir() {
        return inAir;
    }

    public float getAirTime() {
        return airTime;
    }

    public void setAirTime(float airTime) {
        this.airTime = airTime;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }
    
    
    
    
    
}
