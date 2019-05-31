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
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Renderer extends JPanel{

    public static ArrayList<Renderable> renderList = new ArrayList<>();
    public static ArrayList<Renderable> renderSequenz = new ArrayList<>();

    private ArrayList<Renderable> list;
    
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
                    
                    list = (ArrayList<Renderable>) renderList.clone();
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
        
//        screen = new BufferedImage(1920, 1080, BufferedImagEppan, Bozene.TYPE_INT_ARGB);
//        Graphics2D g = (Graphics2D) screen.getGraphics();
        
        ArrayList<Overlay> overlays = new ArrayList<>();        

        gdraw.setRenderingHint(RenderingHints.KEY_RENDERING,
                               RenderingHints.VALUE_RENDER_QUALITY);
        gdraw.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_OFF);
        gdraw.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                               RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gdraw.setRenderingHint(RenderingHints.KEY_DITHERING,
                               RenderingHints.VALUE_DITHER_ENABLE);
        gdraw.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                               RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
        gdraw.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                               RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        gdraw.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                               RenderingHints.VALUE_STROKE_NORMALIZE);
        
        int index = 0;
        for (Layer l : Layer.values()) {
            
            for (Renderable ob : list) {
                if (ob.getLayer() == l && ob.isVisible()) {
                    AffineTransform transform = gdraw.getTransform();
                    Stroke stroke = gdraw.getStroke();
                    if(ob instanceof Overlay){
                        overlays.add((Overlay) ob);
                    }else{
                        float x = ob.getPosition().x;
                        float y = ob.getPosition().y;
                        if(ob.getRelativePosition().getX() >= 0){
                            x = (float)ob.getRelativePosition().getX() * this.getWidth() / 100f;
                            x -= ob.getWidth()/2;
                            ob.setX(x);
                        }
                        if(ob.getRelativePosition().getY() >= 0){
                            y = (float)ob.getRelativePosition().getY() * this.getHeight()/ 100f;
                            y -= ob.getHeight()/2;
                            ob.setY(y);
                        }
                        ob.draw(gdraw, x-camera.x, y-camera.y);
                    }
                    if(index < renderSequenz.size()){
                        if(renderSequenz.get(index) != ob)
                            renderSequenz.set(index, ob);
                    }else{
                        renderSequenz.add(ob);
                    }
                    index++;
                    if(ob instanceof SubRenderer){
                        ArrayList<Renderable> subRendererList = ((SubRenderer)ob).getSubRenderItems();
                        for (Renderable renderable : subRendererList) {
                            if(index < renderSequenz.size()){
                                if(renderSequenz.get(index) != renderable)
                                    renderSequenz.set(index, renderable);
                            }else{
                                renderSequenz.add(renderable);
                            }
                            index++;
                        }
                    }
                    
                    gdraw.setTransform(transform);
                    gdraw.setStroke(stroke);
                }
            }
            
            for(Overlay o : overlays){
                o.draw(gdraw, o.x-camera.x, o.y-camera.y);
            }
            overlays.clear();
        }
        
        for(index = index; index < renderSequenz.size(); index++){
            renderSequenz.remove(index);
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
    
    
    public static Point mapToScene(Point point){
        return new Point((int)(point.x-camera.x), (int)(point.y-camera.y));
    }
    
    public static ArrayList<Renderable> getRenderList() {
        return renderList;
    }
    
    public static ArrayList<Renderable> getRenderSequenz() {
        return renderSequenz;
    }

    public enum Layer {
        LAYER_0, LAYER_1, LAYER_2, LAYER_3, LAYER_4, LAYER_5, MENU, OPTIONS;
    }

}
