package engine.rendering.item;

import engine.filesystem.FileSystem;
import engine.filesystem.ImageHandler;
import engine.rendering.RenderableObject;
import engine.rendering.Renderer;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageItem extends RenderableObject {

    private BufferedImage img;
    
    public ImageItem(Renderer.Layer layer, float x, float y, float width, float height, String img) {
        super(layer, x, y, width, height);
        
        this.img = ImageHandler.scaleImage(FileSystem.readInternImage(img), (int)width, (int)height);
        this.width = this.img.getWidth();
        this.height = this.img.getHeight();
    }

    @Override
    public void draw(Graphics2D g, float x, float y) {
        AffineTransform trans = g.getTransform();
        trans.rotate(getRotationValue(), x+width/2, y+height/2);
        g.drawImage(img, (int)x, (int)y, null);
    }

    @Override
    public boolean isTargetableFromMouse() {
        return false;
    }
    
}
