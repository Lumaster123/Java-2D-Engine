package engine.rendering;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

public interface Renderable {
    
    public void draw(Graphics2D g, float x, float y);
    public Point getPosition();
    public Point2D getRelativePosition();
    public Renderer.Layer getLayer();
    public void setX(float x);
    public void setY(float y);
    
    public boolean isTargetableFromMouse();
    
}
