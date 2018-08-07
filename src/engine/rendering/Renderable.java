package engine.rendering;

import engine.components.Camera;
import java.awt.Graphics2D;

public interface Renderable {
    
    public void draw(Graphics2D g, float x, float y);
    
    
}
