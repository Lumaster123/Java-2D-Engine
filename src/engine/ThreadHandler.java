package engine;

import java.util.HashMap;

public abstract class ThreadHandler {

    public static int maxActiveThreads = 0;
    public static int activeThreads = 0;
    public static int usedThreads = 0;

    private static HashMap<String, Thread> threadMap = new HashMap<>();
    
    public static void invokeLater(double ms, Runnable runnable) {
        activeThreads++;
        usedThreads++;
        if (activeThreads > maxActiveThreads)
            maxActiveThreads = activeThreads;
        Thread tempThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Time.sleep(ms);
                runnable.run();
                activeThreads--;
            }
        });
        tempThread.setName("Thread-"+usedThreads);
        threadMap.put(tempThread.getName(), tempThread);
        tempThread.start();
    }
    
    public static void invokeLater(String name, double ms, Runnable runnable) {
        activeThreads++;
        usedThreads++;
        if (activeThreads > maxActiveThreads)
            maxActiveThreads = activeThreads;
        Thread tempThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Time.sleep(ms);
                runnable.run();
                activeThreads--;
            }
        });
        tempThread.setName(name);
        threadMap.put(tempThread.getName(), tempThread);
        tempThread.start();
    }

    public static void invoke(Runnable runnable) {
        activeThreads++;
        usedThreads++;
        if (activeThreads > maxActiveThreads)
            maxActiveThreads = activeThreads;
        Thread tempThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                activeThreads--;
            }
        });
        tempThread.setName("Thread-"+usedThreads);
        threadMap.put(tempThread.getName(), tempThread);
        tempThread.start();
    }
    
    public static void invoke(String name, Runnable runnable) {
        activeThreads++;
        usedThreads++;
        if (activeThreads > maxActiveThreads)
            maxActiveThreads = activeThreads;
        Thread tempThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                activeThreads--;
            }
        });
        tempThread.setName(name);
        threadMap.put(tempThread.getName(), tempThread);
        tempThread.start();
    }

    public static void killThread(String name){
        try{
            if(threadMap.containsKey(name) && threadMap.get(name).isAlive()){
                threadMap.get(name).stop();
                threadMap.remove(name);
            }
        }catch(Exception ex){
            
        }
    }
    
}
