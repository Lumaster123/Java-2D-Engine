package engine.console.commands;

import engine.console.Command;
import engine.console.ConsoleManager;

public class CMD_Say extends Command{
    
    public CMD_Say() {
        super("say", "command_engine_say");
    }

    @Override
    public String runCommand(String[] args) {
        String message = "";
        for(int i = 2; i < args.length; i++){
            message += args[i]+" ";
        }
        message = message.substring(0, message.length()-1);
        ConsoleManager.writeOnConsole(null, message);
        return null;
    }
    
}
