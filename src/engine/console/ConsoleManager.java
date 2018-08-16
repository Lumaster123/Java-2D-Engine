package engine.console;

import engine.Initializer;
import engine.console.commands.CMD_Say;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ConsoleManager {
    
    private Initializer initializer;
    
    private static Console console;
    
    private static ArrayList<String> textArea;
    private static HashMap<String,Command> commands;
    
    private static boolean bool_isReady = false;
    private static boolean bool_sendNoConsoleMessage = false;
    
    public ConsoleManager(Initializer initializer){
        this.initializer = initializer;
        commands = new HashMap<String,Command>();
        
        bool_isReady = true;
        
        textArea = new ArrayList<>();
        
        console = new Console("NEW Super Mario - Console");
        
        initializeEngineCommands();
        
    }
    
    
    public void creatingBasicCommands(){
        
        addCommand(new CMD_Say());
        
    }
    
    
    
    public static void runCommand(String[] args){
        if(!isReady())
            return;
        
        if(args.length == 0)
            return;
        
        String message = "";
        for(String s : args)
            message += s+" ";
        message = message.substring(0, message.length()-1);
        int cut = 0;
        for(int i = 0; i < message.length(); i++){
            if(message.charAt(i) == ' '){
                cut = i+1;
                break;
            }
        }
        message = message.substring(cut, message.length());
        
        writeOnConsole("[CMD] ", message);
        
        Command command;
        if(commands.containsKey(args[1])){
            command = commands.get(args[1]);
        }else{
            writeOnConsole(null, "The command does not exist!");
            return;
        }
        
        
        
        System.out.println(message);
        String afterMessage = command.runCommand(args);
        if(afterMessage != null && !afterMessage.equals("")){
            writeOnConsole("[CMD] ", afterMessage);
        }
    }
    
    public static void writeOnConsole(String prefix, String message){
        if(!isReady())
            return;
        if(prefix == null)
            prefix = "[Engine] ";
        addTextToList(getTimeStamp()+prefix+message);
    }
    
    private static String getTimeStamp(){
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ").format(Calendar.getInstance().getTime());
    }
    
    private static void addTextToList(String message){
        textArea.add(message);
        console.updateTextArea(message);
    }
    
    public static void addCommand(Command command){
        commands.put(command.name, command);
    }
    
    
    private static boolean isReady(){
        if(bool_isReady)
            return true;
        if(!bool_sendNoConsoleMessage){
            System.out.println("[ConsoleManager] The console hasn't been initialized, messages are only visible in the in-build-console!");
            bool_sendNoConsoleMessage = true;
        }
        return false;
    }    

    private void initializeEngineCommands() {
        addCommand(new CMD_Say());
        
        
    }
}
