package engine.rendering;

import engine.rendering.Renderer.Layer;
import java.awt.Point;

public abstract class RenderableObject implements Renderable{
    
    protected Layer layer;
    
    protected float x, y;
    protected float width, height;
    protected float rotation;

    public RenderableObject(Layer layer, float x, float y, float width, float height) {
        this.layer = layer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isInObject(Point point){
        return point.x >= x && point.x <= x+width && point.y >= y && point.y <= y+height;
    }
    
    public Layer getLayer(){
        return layer;
    }
    
}
