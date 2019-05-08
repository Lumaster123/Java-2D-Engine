package engine.rendering.item;

import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class ComboBoxItem extends RenderableObject{

    protected int currentIndex;
    protected TextItem currentText;
    
    protected ArrayList<TextItem> textList;
    
    public ComboBoxItem(Renderer.Layer layer, float x, float y, float width, float height) {
        super(layer, x, y, width, height);
        
        currentIndex = -1;
                
        textList = new ArrayList<>();
    }

    @Override
    public void draw(Graphics2D g, float x, float y) {
        
    }

    @Override
    public boolean isTargetableFromMouse() {
        return true;
    }
    
}
