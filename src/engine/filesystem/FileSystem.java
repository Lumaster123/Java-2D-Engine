package engine.filesystem;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class FileSystem {
    
    private static FileSystem fs = new FileSystem();
    
    private static HashMap<String, BufferedImage> imageFastLoad = new HashMap<>();
    
    public FileSystem(){
        
    }
    
    public static InputStream getInternInputStream(String name){
        return fs.getClass().getClassLoader().getResourceAsStream(name);
    }
    
    public static BufferedImage readInternImage(String name){
        if(imageFastLoad.containsKey(name))
            return imageFastLoad.get(name);
        try {
            imageFastLoad.put(name, ImageIO.read(getInternInputStream(name)));
            return imageFastLoad.get(name);
        } catch (IOException | IllegalArgumentException ex) {
            throw new Error('"'+name+'"'+" does not exist!", ex);
        }
    }
    
    public static String readInternTextFile(String name){
        String str = "";
        StringBuilder buf = new StringBuilder();
        InputStreamReader is = new InputStreamReader(getInternInputStream(name));
        BufferedReader reader = new BufferedReader(is);
        if(is != null){
            try {
                while((str = reader.readLine()) != null){
                    buf.append(str + "\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            throw new Error("InputStreamReader from "+'"'+name+'"'+" is null!");
        }
        
        str = buf.toString();
        
        return str;
    }
    
    private static File decryptFile(File file){
        
        
        
        return null;
    }
    
}
