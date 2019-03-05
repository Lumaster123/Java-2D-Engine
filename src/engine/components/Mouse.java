package engine.components;

import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class Mouse extends Component implements java.awt.event.MouseListener, MouseMotionListener{

    private Point previousePoint;
    private ArrayList<Integer> pressedKeys;
    private ArrayList<MouseListener> listener;
    
    public Mouse(){
        pressedKeys = new ArrayList<>();
        listener = new ArrayList<>();
    }
    
    public void addListener(MouseListener listener){
        this.listener.add(listener);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        ArrayList<RenderableObject> list = Renderer.getRenderList();
        for(int i = list.size()-1; i >= 0; i--){
            if(list.get(i).isInObject(e.getPoint())){
                for (MouseListener l : listener) {
                    l.clicked(list.get(i), e.getPoint(), e.getButton());
                }
                return;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!pressedKeys.contains(e.getButton())){
            pressedKeys.add(e.getButton());
        }
        for (MouseListener l : listener) {
            l.buttonChanged(e.getButton(), true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(pressedKeys.contains(e.getButton())){
            pressedKeys.remove(new Integer(e.getButton()));
        }
        for (MouseListener l : listener) {
            l.buttonChanged(e.getButton(), false);
        }
        mouseClicked(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MouseListener l : listener) {
            l.mouseMoved(previousePoint, e.getPoint());
        }
        previousePoint = e.getPoint();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        for (MouseListener l : listener) {
            l.mouseMoved(previousePoint, e.getPoint());
        }
        previousePoint = e.getPoint();
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    
    
}
