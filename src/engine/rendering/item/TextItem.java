package engine.rendering.item;

import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import engine.rendering.Size;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class TextItem extends RenderableObject{

    private String displayText;
    private Font font;
    private Color color;
    private Color background_color;
    
    private int h_orientation;
    private int v_orientation;
    
    public TextItem(Renderer.Layer layer, float x, float y) {
        super(layer, x, y, 0, 0);
        
        font = new Font("Calibri", 0, 16);
        color = Color.BLACK;
    }

    public void setText(String text){
        displayText = text;
    }
    
    public String getText(){
        return displayText;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setBackground_color(Color background_color) {
        this.background_color = background_color;
    }

    public Color getColor() {
        return color;
    }

    public Color getBackground_color() {
        return background_color;
    }
    
    
    
    /**
     * 
     * @param horizontal_orientation 0 = left, 1 = center, 2 = right
     * @param vertical_orientation 0 = top, 1 = center, 2 = bottom
     */
    public void setOrientation(int horizontal_orientation, int vertical_orientation){
        this.h_orientation = horizontal_orientation;
        this.v_orientation = vertical_orientation;
    }
    
    public Font getFont(){
        return font;
    }
    
    public void setFont(Font font){
        this.font = font;
    }
    
    @Override
    public void draw(Graphics2D g, float x, float y) {
        int xOffset = 0;
        int yOffset = 0;
        
        if(displayText == null)
            displayText = "";
        
        Size size = new Size(g.getFontMetrics(font).stringWidth(displayText), g.getFontMetrics(font).getHeight()+2);
        
        switch (h_orientation){
            case 0: break;
            case 1: xOffset -= size.width/2; break;
            case 2: xOffset -= size.width; break;
        }
        switch (v_orientation){
            case 0: yOffset += size.height/2; break;
            case 1: yOffset += size.height/4; break;
            case 2: break;
        }
        
        if(background_color != null){
            g.setColor(background_color);
            g.fillRect((int)x + xOffset, (int)y + yOffset - size.height, size.width, size.height);
        }
        if(color == null)
            color = Color.BLACK;
        
        g.setFont(font);
        g.setColor(color);
        g.drawString(displayText, (int)x + xOffset, (int)y + yOffset);
        
    }

    @Override
    public boolean isTargetableFromMouse() {
        return false;
    }
    
}
