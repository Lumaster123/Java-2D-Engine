package engine.rendering;

import engine.rendering.Renderer.Layer;

public abstract class RenderableObject implements Renderable{
    
    protected Layer layer;
    
    protected float x, y;
    protected float width, height;

    public RenderableObject(Layer layer, float x, float y, float width, float height) {
        this.layer = layer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

//    public float getX() {
//        return x;
//    }
//
//    public float getY() {
//        return y;
//    }
//
//    public float getWidth() {
//        return width;
//    }
//
//    public float getHeight() {
//        return height;
//    }
    
    
    
    public Layer getLayer(){
        return layer;
    }
    
}
