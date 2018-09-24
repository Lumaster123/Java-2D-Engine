package engine.window;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JPanel;

public abstract class WindowPanel extends JPanel{
    
    protected Window window;
    
    protected WindowPanel parent;
    
    public WindowPanel(Window window){
        super();
        this.window = window;
        setLayout(null);
    }
    
    public WindowPanel(Window window, WindowPanel parent){
        super();
        this.window = window;
        this.parent = parent;
        this.setSize(this.window.getContentPane().getWidth(), this.window.getContentPane().getHeight());
        setLayout(null);
    }
    
    public void initDefault(){
        window.addWindowPanel(this);
        clear();
        loadMenu();
    }
    
    protected void loadMenu(){
        
    }
    
    protected void clear(){
        this.removeAll();
    }
    
    public void update(){
        repaint();
        window.update();
    }
    
}
