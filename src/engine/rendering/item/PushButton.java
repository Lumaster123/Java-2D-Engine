package engine.rendering.item;

import engine.ThreadHandler;
import engine.components.MouseListener;
import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

public class PushButton extends RenderableObject implements MouseListener{

    private Runnable run;
    private TextItem text;
    
    public PushButton(Renderer.Layer layer, float x, float y, float width, float height, String text) {
        super(layer, x, y, width, height);
        
        this.text = new TextItem(layer, x, y);
        this.text.setOrientation(1, 1);
        this.text.setText(text);
    }
    
    public void setRunnable(Runnable runnable){
        run = runnable;
    }

    public Font getFont(){
        return text.getFont();
    }
    
    public void setFont(Font font){
        text.setFont(font);
    }
    
    @Override
    public void draw(Graphics2D g, float x, float y) {
        g.setColor(Color.lightGray);
        g.fillRect((int)x, (int)y, (int)width, (int)height);
        g.setColor(Color.white);
        g.fillRect((int)x+3, (int)y+3, (int)width-6, (int)height-6);
        text.draw(g, x+width/2, y+height/2);
    }

    @Override
    public void clicked(RenderableObject target, Point position, int button) {
        if(target instanceof PushButton){
            PushButton t = (PushButton)target;
            if(t == this){
                if(run != null)
                    ThreadHandler.invoke("PushButton-Event", run);
            }
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
    public boolean isTargetableFromMouse() {
        return true;
    }

    
}
