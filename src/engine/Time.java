package engine;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Time {
    
    public static double timeSlept = 0;
    
    public static void sleep(double ms){
        boolean end = false;
        long timestamp = System.nanoTime();
        long nano = (long)(ms * 1000000+timestamp);
        while(!end){
            if(System.nanoTime() > nano){
                timeSlept+=ms;
                return;
            }
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Time.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
//    public static void sleep(Thread thread,double ms){
//        boolean ready = false;
//        
//        
//        Thread tempThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int msInt = (int) ms;
//                int nsInt = (int)(ms-msInt)*1000000;
//                System.out.println("Insgesammt: "+ms+"\tms: "+msInt+" nsInt: "+nsInt);
//                try {
//                    Thread.sleep(msInt, nsInt);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//        tempThread.start();
//        
//        while(!ready){};
//        return;
//        
//    }
    
}
