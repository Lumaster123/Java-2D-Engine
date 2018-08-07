package engine.filesystem;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public abstract class ImageHandler {
    
    private static int h = 0;
    
    public ImageHandler(){
        
    }
    
    public static BufferedImage scaleImage(BufferedImage source, int width, int height){
        Image i = source;
        i = i.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bimage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(i, 0, 0, null);
        bGr.dispose();
        return bimage;
    }
    
    public static BufferedImage scaleImage(BufferedImage source, double multiple){
        Image i = source;
        i = i.getScaledInstance((int)(source.getWidth() * multiple),
                                (int)(source.getHeight() * multiple),
                                Image.SCALE_SMOOTH);
        BufferedImage bimage = new BufferedImage(i.getWidth(null),
                                                 i.getHeight(null),
                                                 BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(i, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    
    
}
