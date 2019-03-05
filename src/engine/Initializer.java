package engine;

import engine.components.Component;
import engine.components.KeyChangedListener;
import engine.components.Keyboard;
import engine.components.Mouse;
import engine.components.MouseListener;
import engine.connection.ConnectionSystem;
import engine.console.ConsoleManager;
import engine.filesystem.FileSystem;
import java.util.ArrayList;
import engine.physic.PhysicSystem;
import engine.rendering.Renderer;
import engine.sound.SoundSystem;
import engine.window.Window;

public class Initializer {

    private ArrayList<Component> list = new ArrayList<>();
    private static FileSystem fileSystem = null;
    private static ConsoleManager consoleManager = null;
    private static Window window = null;
    private Renderer renderer = null;
    private static Mouse mouse = null;
    private static Keyboard keyboard = null;
    private static SoundSystem soundSystem = null;
    private static PhysicSystem physicSystem = null;
    private ConnectionSystem connectionSystem = null;

    private float timestamp;
    private int initializerRunning;

    public Initializer() {

        timestamp = System.nanoTime();
        initializerRunning = 0;
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("[Engine] UsedThreads: " + ThreadHandler.usedThreads);
                System.out.println("[Engine] MaxActiveThreads: " + ThreadHandler.maxActiveThreads);
                System.out.println("[Engine] TimeSlept: " + Time.timeSlept + "ms");
            }
        });

    }

    public ArrayList<Component> getActiveComponents() {
        return list;
    }

    public void printInitializationDuration(){
        while (true) {
            if (initializerRunning == 0) {
                float tmp = System.nanoTime();
                tmp -= timestamp;
                tmp /= 1000000000f;
                ConsoleManager.writeOnConsole(null, "The program has been initialized in "+tmp+" seconds!");
                break;
            }
            Time.sleep(0.001);
        }
    }
    
    public void initializeFileSystem() {
        initializerRunning++;
        fileSystem = new FileSystem();
        ConsoleManager.writeOnConsole(null, "FileSystem has been initialized!");
        initializerRunning--;
    }

    public void initializePhysicSystem() {
        initializerRunning++;
        PhysicSystem.activatePhysicSystem();
        ConsoleManager.writeOnConsole(null, "PhysicSystem has been initialized!");
        initializerRunning--;
    }

    public void initializeSoundSystem() {
        initializerRunning++;
        soundSystem = new SoundSystem();
        ConsoleManager.writeOnConsole(null, "SoundSystem has been initialized!");
        initializerRunning--;
    }

    public void initializeMouse(Mouse mouse) {
        initializerRunning++;
        Initializer init = this;
        ThreadHandler.invoke(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (init.renderer != null) {
                        init.mouse = mouse;
                        init.window.getContentPane().addMouseListener(mouse);
                        init.window.getContentPane().addMouseMotionListener(mouse);
                        ConsoleManager.writeOnConsole(null, "Mouse has been initialized!");
                        initializerRunning--;
                        break;
                    }
                    Time.sleep(0.001);
                }
            }
        });
    }

    public void initializeKeyboard(Keyboard keyboard) {
        initializerRunning++;
        Initializer init = this;
        ThreadHandler.invoke(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (init.window != null) {
                        init.keyboard = keyboard;
                        init.window.addKeyListener(keyboard);
                        ConsoleManager.writeOnConsole(null, "Keyboard has been initialized!");
                        initializerRunning--;
                        break;
                    }
                    Time.sleep(0.001);
                }
            }
        });
    }

    public void initializeRenderer() {
        initializerRunning++;
        Initializer init = this;
        ThreadHandler.invoke(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (init.window != null) {
                        Renderer renderer = new Renderer(window);
                        init.window.setContentPane(renderer);
                        if(init.mouse != null){
                            init.window.getContentPane().addMouseListener(mouse);
                            init.window.getContentPane().addMouseMotionListener(mouse);
                        }
                        init.window.setVisible(true);
                        init.renderer = renderer;
                        ConsoleManager.writeOnConsole(null, "Renderer has been initialized!");
                        initializerRunning--;
                        break;
                    }
                    Time.sleep(0.001);
                }
            }
        });
    }

    public void initializeWindow(Window window) {
        initializerRunning++;
        this.window = window;
        ConsoleManager.writeOnConsole(null, "Window has been initialized!");
        initializerRunning--;
    }

    public void initializeConsole(ConsoleManager consoleManager) {
        initializerRunning++;
        this.consoleManager = consoleManager;
        ConsoleManager.writeOnConsole(null, "ConsoleManager has been initialized!");
        initializerRunning--;
    }

    public void initializeConsole() {
        initializerRunning++;
        this.consoleManager = new ConsoleManager(this);
        ConsoleManager.writeOnConsole(null, "ConsoleManager has been initialized!");
        initializerRunning--;
    }
    
    public void initializeConnectionSystem(){
        initializerRunning++;
        this.connectionSystem = new ConnectionSystem();
        ConsoleManager.writeOnConsole(null, "ConnectionSystem has been initialized!");
        initializerRunning--;
    }

    public PhysicSystem getPhysicSystem() {
        return physicSystem;
    }

    public SoundSystem getSoundSystem() {
        return soundSystem;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Window getWindow() {
        return window;
    }

    public ConsoleManager getConsoleManager() {
        return consoleManager;
    }
    
    public ConnectionSystem getConnectionSystem(){
        return connectionSystem;
    }

    public static void addKeyboardListener(KeyChangedListener listener){
        ThreadHandler.invoke("KeyboardListener_Wait", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (Initializer.keyboard != null) {
                        Initializer.keyboard.addListener(listener);
                        break;
                    }
                    Time.sleep(0.001);
                }
            }
        });
    }

    public static void addMouseListener(MouseListener listener){
        ThreadHandler.invoke("MouseListener_Wait", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (Initializer.mouse != null) {
                        Initializer.mouse.addListener(listener);
                        break;
                    }
                    Time.sleep(0.001);
                }
            }
        });
    }
    
}
