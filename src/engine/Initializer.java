package engine;

import engine.components.Component;
import engine.components.Keyboard;
import engine.components.Mouse;
import engine.console.ConsoleManager;
import engine.filesystem.FileSystem;
import java.util.ArrayList;
import engine.physic.PhysicSystem;
import engine.rendering.Renderer;
import engine.sound.SoundSystem;
import engine.window.Window;

public class Initializer {

    private ArrayList<Component> list = new ArrayList<>();
    private FileSystem fileSystem = null;
    private ConsoleManager consoleManager = null;
    private Window window = null;
    private Renderer renderer = null;
    private Mouse mouse = null;
    private Keyboard keyboard = null;
    private SoundSystem soundSystem = null;
    private PhysicSystem physicSystem = null;

    private float timestamp;

    public Initializer() {

        timestamp = System.nanoTime();
        
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
        float tmp = System.nanoTime();
        tmp -= timestamp;
        tmp /= 1000000000f;
        ConsoleManager.writeOnConsole(null, "The program has been initialized in "+tmp+" seconds!");
    }
    
    public void initializeFileSystem() {
        fileSystem = new FileSystem();
        ConsoleManager.writeOnConsole(null, "FileSystem has been initialized!");
    }

    public void initializePhysicSystem() {
        PhysicSystem.activatePhysicSystem();
        ConsoleManager.writeOnConsole(null, "PhysicSystem has been initialized!");
    }

    public void initializeSoundSystem() {
        soundSystem = new SoundSystem();
        ConsoleManager.writeOnConsole(null, "SoundSystem has been initialized!");
    }

    public void initializeMouse(Mouse mouse) {
        Initializer init = this;
        ThreadHandler.invoke(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (init.window != null) {
                        init.mouse = mouse;
                        init.window.addMouseListener(mouse);
                        ConsoleManager.writeOnConsole(null, "Mouse has been initialized!");
                        break;
                    }
                    Time.sleep(0.001);
                }
            }
        });
    }

    public void initializeKeyboard(Keyboard keyboard) {
        Initializer init = this;
        ThreadHandler.invoke(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (init.window != null) {
                        init.keyboard = keyboard;
                        init.window.addKeyListener(keyboard);
                        ConsoleManager.writeOnConsole(null, "Keyboard has been initialized!");
                        break;
                    }
                    Time.sleep(0.001);
                }
            }
        });
    }

    public void initializeRenderer() {
        Initializer init = this;
        ThreadHandler.invoke(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (init.window != null) {
                        Renderer renderer = new Renderer(window);
                        init.window.setContentPane(renderer);
                        init.window.setVisible(true);
                        ConsoleManager.writeOnConsole(null, "Renderer has been initialized!");
                        break;
                    }
                    Time.sleep(0.001);
                }
            }
        });
    }

    public void initializeWindow(Window window) {
        this.window = window;
        ConsoleManager.writeOnConsole(null, "Window has been initialized!");
    }

    public void initializeConsole(ConsoleManager consoleManager) {
        this.consoleManager = consoleManager;
        ConsoleManager.writeOnConsole(null, "Initializer has been initialized!");
    }

    public void initializeConsole() {
        this.consoleManager = new ConsoleManager(this);
        ConsoleManager.writeOnConsole(null, "Initializer has been initialized!");
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

}
