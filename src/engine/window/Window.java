/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.window;

import engine.ThreadHandler;
import engine.Time;
import engine.filesystem.FileSystem;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author lukil
 */
public class Window extends JFrame {

    public enum Window_Size {

        FULL_SIZE,
        FULL_SCREEN,
        WINDOWED;

    }

    private int width = 800;
    private int height = width / 16 * 9;

    private int cw = 0;
    private int ch = 0;

    private int widthScreen = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private int heightScreen = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public Window_Size windowSize;

    private int winResizedWidth = 0;
    private int winResizedHeight = 0;

    private int widthBefore = 1;
    private int heightBefore = 1;

    private int defaultWidth = 1920;
    private int defaultHeight = 1080;

    private ArrayList<WindowResizedListener> windowResizedListener;
    
    public Window(Window_Size size, String title, String iconPath) {
        windowResizedListener = new ArrayList<>();
        windowSize = size;
        setFrameOptions(size, title, iconPath);

//        if(size != Window_Size.FULL_SCREEN)
//            checkMinimusSize();
//        windowListener();
    }

    private void setFrameOptions(Window_Size size, String title, String iconPath) {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();

        if (size == Window_Size.FULL_SCREEN) {
            setUndecorated(true);
            setResizable(false);
            if (gs.isFullScreenSupported()) {
                gs.setFullScreenWindow(this);
            } else {
                System.err.print(new Error("Your ScreenDevice doesn't support FullScreenWindow's!"));
            }

        } else if (size == Window_Size.FULL_SIZE) {
            getContentPane().setPreferredSize(new Dimension(widthScreen - 50, heightScreen - 100));
            setMinimumSize(new Dimension(WIDTH, HEIGHT));
            setUndecorated(false);
            setDefaultLookAndFeelDecorated(false);
            setExtendedState(MAXIMIZED_BOTH);
            pack();
        } else if (size == Window_Size.WINDOWED || size == null) {
            setUndecorated(false);
            setDefaultLookAndFeelDecorated(false);
            setResizable(true);
            getContentPane().setPreferredSize(new Dimension(width, height));
            pack();
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(title);
        setLayout(null);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        if(iconPath != null && !iconPath.equals("")){
            setIconImage(FileSystem.readInternImage(iconPath));  
        }else{
            setIconImage(null);  
        }
        

        winResizedWidth = getWidth();
        winResizedHeight = getHeight();

        Window window = this;
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ThreadHandler.invokeLater(2, new Runnable() {
                    @Override
                    public void run() {
//                        e.getComponent().setSize(e.getComponent().getWidth(), e.getComponent().getWidth() / 16 * 9);

//                        System.out.println(getWidth()+"\t"+widthBefore+"\t"+((double)getWidth()/(double)widthBefore));
//                        double w = (double)getWidth()/(double)widthBefore;
//                        double h = (double)getHeight()/(double)heightBefore;
//                        
//                        for(Renderable r : Renderer.getRenderList()){
//                            r.scaleEvent(w,h);
//                        }
//
//                        widthBefore = getWidth();
//                        heightBefore = getHeight();
                        defaultWidth = getContentPane().getWidth();
                        defaultHeight = getContentPane().getHeight();
                        
                        for(WindowResizedListener l : windowResizedListener){
                            l.windowResized(window);
                        }
                        
                    }
                });

            }

        });
        setLayout(null);

        setVisible(true);

        createBufferStrategy(2);

//        defaultWidth = getContentPane().getWidth();
//        defaultHeight = getContentPane().getHeight();
        System.out.println(defaultHeight);
    }

    public void update(){
//        repaint();
        ThreadHandler.invoke("windowUpdateRecursionKiller", new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
        Time.sleep(10);
        ThreadHandler.killThread("windowUpdateRecursionKiller");
//        setVisible(false);
//        setVisible(true);
    }
    
    public void addWindowPanel(WindowPanel panel){
        panel.setSize(getContentPane().getWidth(), getContentPane().getHeight());
        setContentPane(panel);
        
    }
    
    public int getDefaultWidth() {
        return defaultWidth;
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }

    public void addWindowResizedListener(WindowResizedListener listener){
        windowResizedListener.add(listener);
    }
    
    private void windowListener() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    System.out.println("Mouse: " + getMousePosition());

                }
            }
        });
        t.start();
    }

}
