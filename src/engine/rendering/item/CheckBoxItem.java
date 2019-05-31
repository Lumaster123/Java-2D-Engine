package engine.rendering.item;

import engine.components.MouseListener;
import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;

public class CheckBoxItem extends RenderableObject implements MouseListener{

    private boolean checked;
    
    public CheckBoxItem(Renderer.Layer layer, float x, float y, float width, float height) {
        super(layer, x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g, float x, float y) {
        g.setColor(Color.LIGHT_GRAY);
        g.fill3DRect((int)x, (int)y, (int)width, (int)height, true);
        g.setColor(Color.white);
        g.fillRect((int)x+3, (int)y+3, (int)width-6, (int)height-6);
        
        if(isChecked()){
            Stroke oldStroke = g.getStroke();
            Object oldAntialiasing = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(Color.red);
            g.drawLine((int)x+9, (int)y+9, (int)x+(int)width-9, (int)y+(int)height-9);
            g.drawLine((int)x+9, (int)y+(int)height-9, (int)x+(int)width-9, (int)y+9);
            g.setStroke(oldStroke);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing);
        }
    }
    
    public void setChecked(boolean checked){
        this.checked = checked;
    }

    public void toggle(){
        setChecked(!isChecked());
    }
    
    public boolean isChecked() {
        return checked;
    }
    
    @Override
    public boolean isTargetableFromMouse() {
        return true;
    }

    @Override
    public void clicked(RenderableObject target, Point position, int button) {
        if(target instanceof CheckBoxItem && target == this){
            CheckBoxItem item = (CheckBoxItem) target;
            item.toggle();
        }
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
