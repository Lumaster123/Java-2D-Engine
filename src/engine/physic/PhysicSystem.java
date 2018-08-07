package engine.physic;

import engine.Time;
import engine.components.entity.Entity;
import engine.console.ConsoleManager;

public class PhysicSystem {

    public static final int COLLISION_MODE_NONE = 0;
    public static final int COLLISION_MODE_ENTITY = 1;
    public static final int COLLISION_MODE_BLOCK = 2;
    public static final int COLLISION_MODE_ENTITY_AND_BLOCK = 3;
    
    public static final boolean GRAVITY_ON = true;
    public static final boolean GRAVITY_OFF = false;
    
    private static String prefix = "[PhysicSystem] ";
    
    private static Physic_MovementSystem movementSystem;
    
    private static Physic_GravitySystem gravitySystem;
    
    private static Physic_CollisionSystem collisionSystem;
    
    
    private static boolean started = false;
    
    public static void activatePhysicSystem(){
        if(started)
            return;
        
        movementSystem = new Physic_MovementSystem();
        
        gravitySystem = new Physic_GravitySystem();
        
        collisionSystem = new Physic_CollisionSystem();
        
        started = true;
    }
    
    public static void addMoveListener(Physic_MoveListener listener){
        if(!started){
            ConsoleManager.writeOnConsole(prefix, "PhysicSystem has not been activated!");
            return;
        }
        movementSystem.addMoveListener(listener);
    }
    
    public static void addObjectForGravitation(Object object){
        if(!started){
            ConsoleManager.writeOnConsole(prefix, "PhysicSystem has not been activated!");
            return;
        }
        gravitySystem.addEntity(object);
    }

    public static void addCollisionObject(Object object) {
        if(!started){
            ConsoleManager.writeOnConsole(prefix, "PhysicSystem has not been activated!");
            return;
        }
        collisionSystem.addObject(object);
    }
    
    
}
