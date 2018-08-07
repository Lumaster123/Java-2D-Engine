package engine.console;

public abstract class Command implements ICommand{
    
    protected String name;
    
    protected String permission;
    
    
    public Command(String name, String permission){
        this.name = name;
        this.permission = permission;
        
    }

    
}
