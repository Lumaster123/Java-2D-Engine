package engine.components;

import java.awt.event.KeyEvent;

public interface KeyChangedListener {
    
    /**
     * 
     * @param keyCode       get the Key with KeyEvent.VK_
     * @param keyState      when it is pressed it is true
     */
    public void keyChanged(int keyCode, boolean keyState);
 
    public void keyTyped(KeyEvent e);
    
}
