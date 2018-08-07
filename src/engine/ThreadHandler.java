package engine;

public abstract class ThreadHandler {

    public static int maxActiveThreads = 0;
    public static int activeThreads = 0;
    public static int usedThreads = 0;

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
        tempThread.start();
    }

}
