package engine.filesystem;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public abstract class ImageHandler {
    
    private static int h = 0;
    
    public static int fastScaleList_IndexCount = 0;
    public static HashMap<BufferedImage, Integer> fastScaleList_first = new HashMap<>();
    public static HashMap<Integer, Dimension> fastScaleList_second = new HashMap<>();
    public static HashMap<Integer, BufferedImage> fastScaleList_third = new HashMap<>();
    
    public ImageHandler(){
        
    }
    
    public static BufferedImage scaleImage(BufferedImage source, int width, int height){
        if(fastScaleList_first.containsKey(source) && fastScaleList_second.get(fastScaleList_first.get(source)).width == width && fastScaleList_second.get(fastScaleList_first.get(source)).height == height){
            return fastScaleList_third.get(fastScaleList_first.get(source));
        }
        Image i = source;
        i = i.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bimage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(i, 0, 0, null);
        bGr.dispose();
        
        fastScaleList_first.put(source, fastScaleList_IndexCount);
        fastScaleList_second.put(fastScaleList_IndexCount, new Dimension(width, height));
        fastScaleList_third.put(fastScaleList_IndexCount, bimage);
        fastScaleList_IndexCount++;
        
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
