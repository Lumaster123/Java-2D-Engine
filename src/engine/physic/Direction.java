package engine.physic;

public enum Direction {
    
    
    UP(0),
    DOWN(1),
    LEFT(2),
    RIGHT(3);

    private int i;
    
    Direction(int i) {
        this.i = i;
    }
    
    public int getValue(){
        return i;
    }
    
}
