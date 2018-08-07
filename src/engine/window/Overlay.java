package engine.window;

import engine.rendering.RenderableObject;
import engine.rendering.Renderer.Layer;

public abstract class Overlay extends RenderableObject{

    public Overlay(Layer layer, float x, float y, float width, float height){
        super(layer, x, y, width, height);
        
    }
  
}
