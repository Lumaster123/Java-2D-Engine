package engine.rendering.item;

import engine.components.MouseListener;
import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class ComboBoxItem extends RenderableObject implements MouseListener{

    protected int currentIndex;
    protected TextItem currentText;
    protected Font font;
    
    protected int displayIndex;
    protected int maxDisplay;
    
    protected boolean isOpen;
    
    protected ArrayList<TextItem> textList;
    
    public ComboBoxItem(Renderer.Layer layer, float x, float y, float width, float height) {
        super(layer, x, y, width, height);
        
        currentIndex = -1;
        font = new Font("Calibri", 0, 16);
        isOpen = false;
                
        displayIndex = -1;
        maxDisplay = 3;
        
        textList = new ArrayList<>();
    }

    @Override
    public void draw(Graphics2D g, float x, float y) {
        
        g.setColor(Color.darkGray);
        g.fillRect((int)x, (int)y, (int)width, (int)height);
        g.setColor(new Color(210, 210, 210));
        g.fillRect((int)x+1, (int)y+1, (int)width-2, (int)height-2);
        AffineTransform transform = g.getTransform();
        if(isOpen)
            g.rotate(Math.toRadians(90), (x+width-2-height/4-10), (y+height/4+height/2*0.5));
        g.setColor(Color.gray);
        Stroke oldStroke = g.getStroke();
        Object oldAntialiasing = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        g.drawLine((int)(x+width-2-height/2-10), (int)(y+height/4+height/2*0.25), (int)(x+width-2-height/4-10), (int)(y+height/4+height/2*0.75));
        g.drawLine((int)(x+width-2-height/4-10), (int)(y+height/4+height/2*0.75), (int)(x+width-2-10), (int)(y+height/4+height/2*0.25));
        g.setStroke(oldStroke);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing);
        g.setTransform(transform);
        
        if(currentText != null){
            currentText.setFont(font);
            currentText.setOrientation(0, 1);
            currentText.setHeight(height);
            currentText.draw(g, x+5, y+height/2);
        }
        
        if(isOpen){
            y += 1;
            
            if(maxDisplay >= textList.size()){
                for(int i = 1; i <= textList.size(); i++){
                    g.setColor(Color.darkGray);
                    g.fillRect((int)(x), (int)(y+height*i), (int)width, (int)height);
                    g.setColor(new Color(210, 210, 210));
                    g.fill3DRect((int)(x)+1, (int)(y+height*i)+1, (int)width-2, (int)height-2, true);

                    if(textList.get(i-1) != null){
                        textList.get(i-1).setFont(font);
                        textList.get(i-1).setOrientation(0, 1);
                        textList.get(i-1).setHeight(height);
                        textList.get(i-1).draw(g, x+5, y+height*(i)+height/2);
                    }
                }
            }else{
                if(displayIndex < 0)
                    displayIndex = 0;
                int counter = 0;
                for(int i = displayIndex+1; i <= textList.size() && counter < maxDisplay; i++){
                    g.setColor(Color.darkGray);
                    g.fillRect((int)(x), (int)(y+height*i), (int)width-10, (int)height);
                    g.setColor(new Color(210, 210, 210));
                    g.fill3DRect((int)(x)+1, (int)(y+height*i)+1, (int)width-2-10, (int)height-2, true);

                    if(textList.get(i-1) != null){
                        textList.get(i-1).setFont(font);
                        textList.get(i-1).setOrientation(0, 1);
                        textList.get(i-1).setHeight(height);
                        textList.get(i-1).draw(g, x+5, y+height*(i)+height/2);
                    }
                    counter++;
                }
            }
            
            
            // @todo implement scrolling through
             
            
        }
        
    }

    public void setCurrentIndex(int index){
        if(index < textList.size()){
            currentIndex = index;
            currentText = textList.get(index);
            displayIndex = index;
        }
    }

    public void setDisplayIndex(int displayIndex) {
        this.displayIndex = displayIndex;
    }
    
    public Font getFont(){
        return font;
    }
    
    public void setFont(Font font){
        this.font = font;
    }
    
    public void setMaxDisplay(int count){
        maxDisplay = count;
    }
    
    public void setTextList(String[] textList){
        clearTextList();
        for (String text : textList) {
            addToTextList(text);
        }
    }
    
    public void setTextList(ArrayList<String> textList){
        clearTextList();
        for (String text : textList) {
            addToTextList(text);
        }
    }
    
    public void addToTextList(String text){
        TextItem item = new TextItem(this.getLayer(), 0, 0);
        item.setText(text);
        item.setFont(font);
        item.setVisible(false);
        textList.add(item);
    }
    
    public void removeFromTextList(int index){
        textList.remove(index);
    }
    
    public void removeFromTextList(String text){
        ArrayList<TextItem> removeList = new ArrayList<>();
        for (TextItem textItem : textList) {
            if(textItem.getText().equals(text))
                removeList.add(textItem);
        }
        for (TextItem textItem : removeList) {
            removeList.remove(textItem);
        }
    }
    
    public void clearTextList(){
        textList.clear();
    }
    
    @Override
    public boolean isTargetableFromMouse() {
        return true;
    }

    @Override
    public void clicked(RenderableObject target, Point position, int button) {
        if(target == this && button == MouseEvent.BUTTON1){
            if(isOpen){
                isOpen = false;
            }else{
                isOpen = true;
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
    public void mouseWheelMoved(int amount) {
    }
    
}
