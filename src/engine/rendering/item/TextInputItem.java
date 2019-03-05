package engine.rendering.item;

import engine.Initializer;
import engine.components.KeyChangedListener;
import engine.components.MouseListener;
import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

public class TextInputItem extends RenderableObject implements MouseListener, KeyChangedListener{

    protected TextItem textItem;
    
    protected boolean hasFocus;
    protected String background_text;
    protected String text;
    protected Font font;
    protected Color color;
    
    
    public TextInputItem(Renderer.Layer layer, float x, float y, float width, float height) {
        super(layer, x, y, width, height);
        
        Initializer.addKeyboardListener(this);
        Initializer.addMouseListener(this);
        
        text = "";
        textItem = new TextItem(layer, x, y);
        textItem.setFont(new Font("Times new Roman", 1, 24));
        
        hasFocus = false;
    }

    @Override
    public void draw(Graphics2D g, float x, float y) {
        if(text == "" && hasFocus)
            textItem.setText(text);
        g.setColor(Color.LIGHT_GRAY);
        g.fill3DRect((int)x, (int)y, (int)width, (int)height, true);
        g.setColor(Color.white);
        g.fill3DRect((int)x+2, (int)y+2, (int)width-4, (int)height-4, true);
        textItem.draw(g, x +2, y+2);
        
    }

    @Override
    public void clicked(RenderableObject target, Point position, int button) {
        if(target instanceof TextInputItem){
            if(target == this){
                hasFocus = true;
                return;
            }
        }
        hasFocus = false;
    }

    @Override
    public void buttonChanged(int button, boolean pressed) {
        
    }

    @Override
    public void mouseMoved(Point prevPosition, Point position) {
        
    }

    @Override
    public void keyChanged(int keyCode, boolean keyState) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(!hasFocus)
            return;
        if(e.getKeyChar()== KeyEvent.VK_BACK_SPACE){
            if(e.isControlDown()){
                text = "";
            }else if(text.length() > 0){
                text = text.substring(0, text.length()-1);
            }
        }else{
            char c = e.getKeyChar();
            if(c != KeyEvent.CHAR_UNDEFINED)
                text += c;
        }
    }
    
    
    public void setBackground_text(String background_text) {
        this.background_text = background_text;
    }

    public String getBackground_text() {
        return background_text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    
    
}
