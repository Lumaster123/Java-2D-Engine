package engine.components;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Keyboard extends Component implements KeyListener{

    private ArrayList<Integer> pressedKeys;
    private ArrayList<KeyChangedListener> listener;

    public Keyboard() {
        this.listener = new ArrayList<>();
        this.pressedKeys = new ArrayList<>();
    }
    
    public void addListener(KeyChangedListener listener){
        this.listener.add(listener);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(pressedKeys.contains(e.getKeyCode())){
           return; 
        }
        pressedKeys.add(e.getKeyCode());
        for(KeyChangedListener l : listener){
            l.keyChanged(e.getKeyCode(), true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!pressedKeys.contains(e.getKeyCode())){
            return;
        }
        pressedKeys.remove(new Integer(e.getKeyCode()));
        for(KeyChangedListener l : listener){
            l.keyChanged(e.getKeyCode(), false);
        }
    }

    public ArrayList<Integer> getPressedKeys() {
        return pressedKeys;
    }

    
    
    
    
}
