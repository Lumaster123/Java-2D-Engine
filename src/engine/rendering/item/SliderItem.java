
package engine.rendering.item;

import engine.components.MouseListener;
import engine.rendering.Renderable;
import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import engine.rendering.SubRenderer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SliderItem extends RenderableObject implements MouseListener, SubRenderer{

    protected double from, to, step;
    protected boolean useSteps;
    
    protected boolean showFromToValue, showValue, showSlideSteps;
    
    protected DragItem dragItem;
    protected TextItem minValue, maxValue, currentValue;
    
    
    public SliderItem(Renderer.Layer layer, float x, float y, float width, float height) {
        super(layer, x, y, width, height);
        
        from = 0;
        to = 0;
        step = 0;
        useSteps = false;
        showFromToValue = false;
        showValue = false;
        
        dragItem = new DragItem(layer, 0, 0, 0, 0);
        dragItem.setParent(this);
        dragItem.setMin(height*0.25f);
        dragItem.setMax(width-height*0.75f);
        dragItem.setX(dragItem.getX()+dragItem.getWidth()*0.5f);
        dragItem.setHeight(height*0.5f);
        dragItem.setWidth(height*0.5f);
        dragItem.setX(dragItem.getX()-dragItem.getWidth()*0.5f);
        dragItem.setY(height*0.25f);
        
        minValue = new TextItem(layer, 0, 0);
        minValue.setParent(this);
        maxValue = new TextItem(layer, 0, 0);
        maxValue.setParent(this);
        currentValue = new TextItem(layer, 0, 0);
        currentValue.setParent(this);
    }

    
    @Override
    public void draw(Graphics2D g, float x, float y) {
        
//        g.setColor(Color.red);
//        g.fillOval(toInt(x), toInt(y), toInt(width), toInt(height));
        
        Object oldAntialiasing = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // slide line
        g.setColor(Color.gray);
        g.setStroke(new BasicStroke(height*0.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(toInt(x+height*0.5f), toInt(y+height*0.5f), toInt(x+width-height*0.5f), toInt(y+height*0.5f));
        
        // slide steps
        if(step > 0){
            g.setColor(Color.lightGray);
            for(float i = x+height*0.5f-height*0.1f/2; i <= x+width-height*0.5f-height*0.1f/2+0.01f; i += ((x+width-height*0.5f-height*0.1f/2)-(x+height*0.5f-height*0.1f/2))/(to-from)){
                g.fillOval(toInt(i), toInt(y+height*0.5-height*0.1f/2), toInt(height*0.1f), toInt(height*0.1f));
                
            }
            
        }
        
        // slide min max display
        minValue.setOrientation(1, 2);
        maxValue.setOrientation(1, 2);
        minValue.setText(from+"");
        maxValue.setText(to+"");
        minValue.setX(height*0.5f);
        minValue.setY(height*0.25f);
        maxValue.setX(width-height*0.5f);
        maxValue.setY(height*0.25f);
        if(showFromToValue){
            minValue.draw(g, Renderer.mapToScene(minValue.getPosition()).x, Renderer.mapToScene(minValue.getPosition()).y);
            maxValue.draw(g, Renderer.mapToScene(maxValue.getPosition()).x, Renderer.mapToScene(maxValue.getPosition()).y);
        }
            
        // dragItem value display
        currentValue.setOrientation(1, 2);
        currentValue.setText(getValue()+"");
        currentValue.setX(dragItem.getX()+dragItem.getWidth()/2);
        currentValue.setY(0);
        if(showValue){
            currentValue.draw(g, Renderer.mapToScene(currentValue.getPosition()).x, Renderer.mapToScene(currentValue.getPosition()).y);
        }
        
        
        
        // drag item
        dragItem.setMin(height*0.25f);
        dragItem.setMax(width-height*0.75f);
        dragItem.setX(dragItem.getX()+dragItem.getWidth()*0.5f);
        dragItem.setHeight(height*0.5f);
        dragItem.setWidth(height*0.5f);
        dragItem.setX(dragItem.getX()-dragItem.getWidth()*0.5f);
        dragItem.setY(height*0.25f);
        dragItem.draw(g, x+dragItem.getX(), y+dragItem.getY());
        
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing);
    }
    
    public void setupSlider(double from, double to, double step, double value, boolean useSteps){
        this.from = from;
        this.to = to;
        this.step = step;
        setValue(value);
        this.useSteps = useSteps;
        
    }

    public RenderableObject getHoverItem(){
            return dragItem.item;
        }
    @Override
    public ArrayList<Renderable> getSubRenderItems() {
        ArrayList<Renderable> list = new ArrayList<>();
        list.add(dragItem);
        return list;
    }
    
    private class DragItem extends RenderableObject implements MouseListener{

        protected boolean isHold;
        protected boolean isFocused;
        protected float min, max;
        
        public DragItem(Renderer.Layer layer, float x, float y, float width, float height) {
            super(layer, x, y, width, height);
            
            isHold = false;
            isFocused = false;
        }

        @Override
        public void draw(Graphics2D g, float x, float y) {
            g.setColor(Color.green);
            g.fillOval(toInt(x), toInt(y), toInt(width), toInt(height));
        }

        @Override
        public float getX() {
            return super.getX(); 
        }

        @Override
        public float getY() {
            return super.getY(); 
        }

        public void setMin(float min) {
            this.min = min;
            checkAndFixMinMax();
        }

        public void setMax(float max) {
            this.max = max;
            checkAndFixMinMax();
        }
        
        public void checkAndFixMinMax(){
            Point p = Renderer.mapToScene(new Point(toInt(x), toInt(y)));
            if(p.x < min)
                setX(min);
            else if(p.x > max)
                setX(max);
        }
        
        @Override
        public boolean isTargetableFromMouse() {
            return true;
        }

        public void setHeight(float height) {
            this.height = height;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        @Override
        public void clicked(RenderableObject target, Point position, int button) {
        }

        @Override
        public void buttonChanged(int button, boolean pressed) {
            if(button == MouseEvent.BUTTON1){
                isHold = pressed;
                if(!pressed)
                    isFocused = false;
            }
        }

        @Override
        public void mouseMoved(Point prevPosition, Point position) {
            if(isFocused){
//                if(x+(position.x-prevPosition.x) < min)
//                    setX(min);
//                else if(x+(position.x-prevPosition.x) > max)
//                    setX(max);
//                else
//                    setX(x+(position.x-prevPosition.x));
                Point p = Renderer.mapToScene(position);
                p.x -= Renderer.mapToScene(parent.getPosition()).x;
                p.x -= width/2;
                if(p.x < min)
                    setX(min);
                else if(p.x > max)
                    setX(max);
                else
                    setX(p.x);
            }
        }

        public RenderableObject item;
        public RenderableObject getHoverItem(){
            return item;
        }
        
        @Override
        public void mouseHover(RenderableObject target, Point prevPosition, Point position) {
            item = target;
            if(target == this && isHold){
                isFocused = true;
            }
        }

        @Override
        public void mouseWheelMoved(int amount) {
        }
  
        
        
    }
        
    public void setValue(double value) {
        value = (value-from)*100/(to-from);
        float var = (float) (value*(dragItem.max-dragItem.min)/100);
        dragItem.setX((float)var+dragItem.min);
    }

    public double getValue() {
        double value = (dragItem.getX()-dragItem.min)*100/(dragItem.max-dragItem.min);
        value = (to-from)*value/100+from;
        if(useSteps){
            ArrayList<Double> list = new ArrayList<>();
            for(double i = from; i <= to; i+=step){
                list.add(i);
            }
            double distance = Math.abs(value-list.get(0));
            int index = 0;
            for(int i = 1; i < list.size(); i++){
                if(Math.abs(value-list.get(i)) < distance){
                    distance = Math.abs(value-list.get(i));
                    index = i;
                }
            } 
            value = list.get(index);
        }
        return value;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }
    
    public void setFromTo(double from, double to){
        this.from = from;
        this.to = to;
    }

    public void setUseSteps(boolean useSteps) {
        this.useSteps = useSteps;
    }

    public boolean isUseSteps() {
        return useSteps;
    }
    
    public void showFromToValue(boolean show){
        showFromToValue = show;
    }
    
    public void showValue(boolean show){
        showValue = show;
    }
    
    public void showSlideSteps(boolean show){
        showSlideSteps = show;
    }

    @Override
    public boolean isTargetableFromMouse() {
        return true;
    }

    @Override
    public void clicked(RenderableObject target, Point position, int button) {
    }

    @Override
    public void buttonChanged(int button, boolean pressed) {
    }

    @Override
    public void mouseMoved(Point prevPosition, Point position) {
    }

    @Override
    public void mouseHover(RenderableObject target, Point prevPosition, Point position) {
    }

    @Override
    public void mouseWheelMoved(int amount) {
    }

    
}
