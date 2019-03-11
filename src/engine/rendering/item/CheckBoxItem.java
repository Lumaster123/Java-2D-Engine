package engine.rendering.item;

import engine.components.MouseListener;
import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import java.awt.Graphics2D;
import java.awt.Point;

public class CheckBoxItem extends RenderableObject implements MouseListener{

    private boolean checked;
    
    public CheckBoxItem(Renderer.Layer layer, float x, float y, float width, float height) {
        super(layer, x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g, float x, float y) {
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

}
