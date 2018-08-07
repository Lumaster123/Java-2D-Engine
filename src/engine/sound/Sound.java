package engine.sound;

import engine.ThreadHandler;
import engine.Time;
import engine.components.Config;
import engine.filesystem.FileSystem;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public abstract class Sound implements Volume, Runnable {

    public static int MODE_NOT_READY = 1;
    public static int MODE_LOADING = 2;
    public static int MODE_STANDBY = 3;
    public static int MODE_RUNNING = 4;
    public static int MODE_START = 5;
    public static int MODE_STOP = 6;
    public static int MODE_PAUSE = 7;

    private float volume;
    private float maxVolume;
    private float minVolume;
    private float percentVolume = -1;

    private String path;

    private AudioInputStream stream;
    private Clip clip;
    private FloatControl vol;

    private int mode;
    private boolean loop;

    public Sound(String path) {
        this.path = path;

        mode = MODE_NOT_READY;
        loop = false;

        volumeListener();

        ThreadHandler.invoke(this);
    }

    public void loadSound() {

        try {

            mode = MODE_LOADING;
            stream = AudioSystem.getAudioInputStream(FileSystem.getInternInputStream(path));
            clip = AudioSystem.getClip();
            clip.open(stream);
            vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            minVolume = vol.getMinimum()+30;
            maxVolume = vol.getMaximum();
            mode = MODE_STANDBY;

        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        while (true) {

            if (mode == MODE_START) {
                mode = MODE_RUNNING;
                clip.start();
            }

            if (mode == MODE_STOP) {
                mode = MODE_PAUSE;
                clip.stop();
            }
            
            if(loop){
                if(clip.getFramePosition() >= clip.getFrameLength()-5){
                    loadSound();
                    vol.setValue(volume);
                    start();
                }
            }
            

            Time.sleep(1);
        }
    }

    public void start() {
        while (true) {
            if (mode == MODE_STANDBY || mode == MODE_PAUSE) {
                mode = MODE_START;
                return;
            }
            Time.sleep(1);
        }
    }

    public void stop() {
        while (true) {
            if (mode == MODE_RUNNING) {
                mode = MODE_STOP;
                return;
            }
            Time.sleep(1);
        }
    }

    public void loop(boolean loop) {
        this.loop = loop;
    }

    private void volumeListener() {
        ThreadHandler.invoke(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (mode == MODE_NOT_READY || mode == MODE_LOADING) {
                    } else if (Config.VOLUME != percentVolume) {
                        if (Config.VOLUME <= Config.VOLUME_MAX && Config.VOLUME >= Config.VOLUME_MIN) {
                            percentVolume = getVolumeFromConfig();

                            if(percentVolume == 0){
                                volume = -75;
                            }else{
                                volume = (-minVolume + maxVolume) / 100 * percentVolume + minVolume;
                            }
                            vol.setValue(volume);
                            System.out.println(volume);

                        } else {
                            Config.VOLUME = percentVolume;
                        }
                    }
                    Time.sleep(1);
                }
            }
        });

    }

}
