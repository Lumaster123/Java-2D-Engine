package engine.rendering;

import engine.ThreadHandler;
import engine.Time;
import engine.components.Camera;
import engine.components.Config;
import engine.window.Overlay;
import engine.window.Window;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Renderer extends JPanel{

    public static ArrayList<RenderableObject> renderList = new ArrayList<>();

    private ArrayList<RenderableObject> list;
    
    private Window window;
    
//    private BufferedImage screen;
    
    private static Camera camera;
    private static boolean switchCamera = true;

    private int counter = 0;
    private boolean reset = true;
    private long second = 1000000000;
    private long start = 0;
    private int fps = 0;
    
    private long timeStart = 0;
    private long timeEnd = 0;
    
    private boolean isRepainting;
    private double prevSleepTime;
    
    public Renderer(Window window) {
        
        this.window = window;
        
        camera = new Camera(window, 0, 0);
        
//        screen = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        
        
        list = new ArrayList<>();
        
        ThreadHandler.invoke("Renderer", new Runnable() {
            @Override
            public void run() {
                
                while (true) {
                    
                    if (reset) {
                        counter = 0;
                        reset = false;
                        start = System.nanoTime();
                    }
                    
                    list = (ArrayList<RenderableObject>) renderList.clone();
                    switchCamera = false;
                    isRepainting = true;
                    repaint();
                    while(isRepainting){Time.sleep(0.00001);}
                    switchCamera = true;
                    
                    
                    if(Config.FPS_CURRENT_SETTING > 0){
                        long prevStart = timeStart;
                        timeStart = System.nanoTime();
                        prevSleepTime = Time.sleep(1000 / Config.FPS_CURRENT_SETTING + prevSleepTime - (((double)(System.nanoTime() - prevStart)) / 1000000));
                    }
                    
                    counter++;
                    if (System.nanoTime() - second > start) {
                        fps = counter;
                        reset = true;
                    }
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Graphics2D gdraw = (Graphics2D) gr;
        
//        screen = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g = (Graphics2D) screen.getGraphics();
        
        ArrayList<Overlay> overlays = new ArrayList<Overlay>();        

        
        
        for (Layer l : Layer.values()) {
            
            for (RenderableObject ob : list) {
                if (ob.getLayer() == l) {
                    AffineTransform transform = gdraw.getTransform();
                    if(ob instanceof Overlay){
                        overlays.add((Overlay) ob);
                    }else{
                        ob.draw(gdraw, ob.x-camera.x, ob.y-camera.y);
                    }
                    gdraw.setTransform(transform);
                }
            }
            
            for(Overlay o : overlays){
                o.draw(gdraw, o.x-camera.x, o.y-camera.y);
            }
            
            overlays.clear();
        }
        
        
        
//        if(window.windowSize == Window.Window_Size.WINDOWED){
//            screen = ImageHandler.scaleImage(screen, window.getContentPane().getWidth(), (window.getContentPane().getWidth())/16*9);
//        }
        
        
//        gdraw.drawImage(screen, 0, 0, null);
        
        if(Config.FPS_DISPLAY){
            int m = 1;
            gdraw.setColor(new Color(51,121,255,110));
            gdraw.fillRect(8, 6, 90*m, 15*m);
            gdraw.setColor(new Color(16,0,255,130));
            gdraw.drawRect(7, 5, 90*m+1, 15*m+1);
            gdraw.setColor(Color.BLACK);
            gdraw.setFont(new Font("Folio", Font.BOLD, 12));
            gdraw.drawString("FPS: " + fps, 10, 18);
        }
        
//        screen = ImageHandler.scaleImage(screen, 1920, 1080);
        isRepainting = false;
    }

    public static void setCamera(Camera camera){
        ThreadHandler.invoke(new Runnable() {
            @Override
            public void run() {
                while(!switchCamera){
                    Time.sleep(0.01);
                }
                Renderer.camera = camera;
            }
        });
    }
    
    public static ArrayList<RenderableObject> getRenderList() {
        return renderList;
    }

    public enum Layer {
        LAYER_0, LAYER_1, LAYER_2, LAYER_3, LAYER_4, LAYER_5, MENU, OPTIONS;
    }

}
