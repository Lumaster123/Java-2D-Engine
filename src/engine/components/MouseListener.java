package engine.components;

import engine.rendering.RenderableObject;
import java.awt.Point;

public interface MouseListener {
    
    public void clicked(RenderableObject target, Point position, int button);
    public void buttonChanged(int button, boolean pressed);
    
    public void mouseMoved(Point prevPosition, Point position);
    public void mouseHover(RenderableObject target, Point prevPosition, Point position);
    
    
}
