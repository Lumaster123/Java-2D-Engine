package engine.components.world;

import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import engine.window.Window;

public abstract class World extends RenderableObject{
    
    protected Window window;
    
    public World(Window window, float width, float height, Renderer.Layer layer) {
        super(layer, 0, 0, width, height);
        this.width = width;
        this.height = height;
        this.window = window;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public Window getWindow() {
        return window;
    }
    
    
    
    
}
