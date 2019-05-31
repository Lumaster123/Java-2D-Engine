package engine.components;

import engine.rendering.Renderable;
import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

public class Mouse extends Component implements java.awt.event.MouseListener, MouseMotionListener, MouseWheelListener{

    private Point previousePoint;
    private ArrayList<Integer> pressedKeys;
    private ArrayList<MouseListener> listener;
    
    public Mouse(){
        pressedKeys = new ArrayList<>();
        listener = new ArrayList<>();
        
        previousePoint = new Point(0, 0);
    }
    
    public void addListener(MouseListener listener){
        this.listener.add(listener);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
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
        ArrayList<Renderable> list = (ArrayList<Renderable>) Renderer.getRenderSequenz().clone();
        for(int i = list.size()-1; i >= 0; i--){
            if(list.get(i) instanceof RenderableObject && ((RenderableObject)list.get(i)).isInObject(e.getPoint()) && ((RenderableObject)list.get(i)).isTargetableFromMouse() && ((RenderableObject)list.get(i)).isVisible()){
                for (MouseListener l : listener) {
                    l.clicked(((RenderableObject)list.get(i)), e.getPoint(), e.getButton());
                }
                return;
            }
        }
        for (MouseListener l : listener) {
            l.clicked(null, e.getPoint(), e.getButton());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MouseListener l : listener) {
            l.mouseMoved(previousePoint, e.getPoint());
        }
        ArrayList<Renderable> list = (ArrayList<Renderable>) Renderer.getRenderSequenz().clone();
        for(int i = list.size()-1; i >= 0; i--){
            if(list.get(i) instanceof RenderableObject && ((RenderableObject)list.get(i)).isInObject(e.getPoint()) && ((RenderableObject)list.get(i)).isTargetableFromMouse() && ((RenderableObject)list.get(i)).isVisible()){
                for (MouseListener l : listener) {
                    l.mouseHover(((RenderableObject)list.get(i)), previousePoint, e.getPoint());
                }
                previousePoint = e.getPoint();
                return;
            }
        }
        for (MouseListener l : listener) {
            l.mouseHover(null, previousePoint, e.getPoint());
        }
        previousePoint = e.getPoint();
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        for (MouseListener l : listener) {
            l.mouseMoved(previousePoint, e.getPoint());
        }
        ArrayList<Renderable> list = (ArrayList<Renderable>) Renderer.getRenderSequenz().clone();
        for(int i = list.size()-1; i >= 0; i--){
            if(list.get(i) instanceof RenderableObject && ((RenderableObject)list.get(i)).isInObject(e.getPoint()) && ((RenderableObject)list.get(i)).isTargetableFromMouse() && ((RenderableObject)list.get(i)).isVisible()){
                for (MouseListener l : listener) {
                    l.mouseHover(((RenderableObject)list.get(i)), previousePoint, e.getPoint());
                }
                previousePoint = e.getPoint();
                return;
            }
        }
        for (MouseListener l : listener) {
            l.mouseHover(null, previousePoint, e.getPoint());
        }
        previousePoint = e.getPoint();
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        for (MouseListener l : listener) {
            l.mouseWheelMoved(e.getWheelRotation());
        }
    }

    

    
    
}
