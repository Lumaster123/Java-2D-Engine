package engine.physic;

import engine.components.entity.Entity;
import java.util.ArrayList;

public class Physic_Collision_ObjectDirections {
    
    private ArrayList<Object> objects = new ArrayList<>();
    
    private ArrayList<Boolean[]> directions = new ArrayList<>();

    public Physic_Collision_ObjectDirections() {
        
    }
    
    public void addObject(Object object, boolean[] directions){
    
        if(object instanceof Physic_WorldPart || object instanceof Entity){
            
            objects.add(object);
            Boolean[] b = new Boolean[directions.length];
            for(int i = 0; i < b.length; i++){
                b[i] = directions[i];
            }
            this.directions.add(b);
            
        }
        
    }
    
    public Object getObject(int index){
        return objects.get(index);
    }
    
    public boolean[] getDirections(int index){
        boolean[] b = new boolean[directions.get(index).length];
        for(int i = 0; i < directions.get(index).length; i++){
            b[i] = directions.get(index)[i];
        }
        return b;
    }
    
    public int getLength(){
        return objects.size();
    }
    
}
