package engine.rendering;

import engine.Initializer;
import engine.components.KeyChangedListener;
import engine.components.MouseListener;
import engine.rendering.Renderer.Layer;
import java.awt.Point;
import java.awt.geom.Point2D;

public abstract class RenderableObject implements Renderable{
    
    protected RenderableObject parent;
    
    protected Layer layer;
    
    protected float relativeX, relativeY;
    protected float x, y;
    protected float width, height;
    protected float rotation;

    protected boolean isVisible;
    
    protected boolean drawCentered;
    
    public RenderableObject(Layer layer, float x, float y, float width, float height) {
        this.layer = layer;
        relativeX = -1;
        relativeY = -1;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        isVisible = true;
        
        activateListener();
    }

    private void activateListener(){
        if(this instanceof MouseListener){
            Initializer.addMouseListener((MouseListener)this);
        }
        if(this instanceof KeyChangedListener){
            Initializer.addKeyboardListener((KeyChangedListener)this);
        }
    }
    
    public void setParent(RenderableObject parent){
        this.parent = parent;
    }

    public RenderableObject getParent() {
        return parent;
    }
    
    public boolean isInObject(Point point){
        float x = getPosition().x, y = getPosition().y;
        return point.x >= x && point.x <= x+width && point.y >= y && point.y <= y+height;
    }
    
    @Override
    public Layer getLayer(){
        return layer;
    }
    
    public void setLayer(Layer layer){
        this.layer = layer;
    }
    
    @Override
    public Point getPosition(){
        float x = this.x, y = this.y;
        if(parent != null){
            x += parent.getPosition().x;
            y += parent.getPosition().y;
        }
        return new Point((int)x, (int)y);
    }
    
    

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }
    
    public static float toRotation(float degree){
        if(degree >= 0 && degree < 360){
            return degree;
        }else{
            float calc = degree - (((int)(degree / 360)) * 360);
            if(calc >= 0)
                return calc;
            else
                return 360 + calc;
        }
    }
    
    public void rotate(float degree){
        degree = toRotation(degree);
        if(rotation + degree >= 360)
            rotation = rotation + degree - 360;
        else if(rotation + degree < 0)
            rotation = rotation + degree + 360;
        else
            rotation += degree;
    }

    public void setRotation(float rotation) {
        this.rotation = toRotation(rotation);
    }

    public float getRotation() {
        return rotation;
    }
    
    protected double getRotationValue(){
        return Math.toRadians(rotation);
    }
    
    public int toInt(float f){
        return (int)f;
    }
    
    public int toInt(double d){
        return (int)d;
    }
    
    @Override
    public Point2D getRelativePosition() {
        return new Point2D() {
            @Override
            public double getX() {
                return relativeX;
            }

            @Override
            public double getY() {
                return relativeY;
            }

            @Override
            public void setLocation(double x, double y) {
                relativeX = (float) x;
                relativeY = (float) y;
            }
        };
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public void setVisible(boolean visible){
        isVisible = visible;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }


    
    
}
