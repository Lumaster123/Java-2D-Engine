package engine.window;

import javax.swing.JPanel;

public abstract class WindowPanel extends JPanel{
    
    private Window window;
    
    public WindowPanel(Window window){
        this.window = window;
        setLayout(null);
        window.addWindowPanel(this);
    }
    
    private void initDefault(){
        
        
    }
    
}
