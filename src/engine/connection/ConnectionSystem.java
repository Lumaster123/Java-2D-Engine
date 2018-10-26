package engine.connection;

import engine.console.ConsoleManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionSystem {
    
    public enum ConnectionState{
        NOT_CONNECTED,
        CONNECTING,
        CONNECTED,
        TIME_OUT,
        FATAL_ERROR,
        CONNECTION_REFUSED;
    }
    
    private String prefix = "[ConnectionSystem] ";
    
    private int server_port;
    private InetAddress server_address;
    private int timeout;
    
    private ConnectionState state;
    
    private Socket client;
    
    public ConnectionSystem(){
        try {
            state = ConnectionState.NOT_CONNECTED;
            server_port = 38475;
            this.server_address = InetAddress.getLocalHost();
            client = new Socket();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ConnectionSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initializeClient(String inetAddress, int port, int timeout_ms){
        if(state == ConnectionState.CONNECTION_REFUSED || state == ConnectionState.FATAL_ERROR || state == ConnectionState.TIME_OUT){
            ConsoleManager.writeOnConsole(prefix, "The Connection is damaged! You have to resolve the problem frist!");
        }else if(state == ConnectionState.CONNECTED || state == ConnectionState.CONNECTING){
            ConsoleManager.writeOnConsole(prefix, "The Connection is active! You have to stop the connection first!");
        }else if(state == ConnectionState.NOT_CONNECTED){
            try {
                server_address = InetAddress.getByName(inetAddress);
                server_port = port;
                timeout = timeout_ms;
            } catch (UnknownHostException ex) {
                Logger.getLogger(ConnectionSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean connect(int attempts){
        
        for(int i = 0; i < attempts; i++){
            
            startConnection();
            if(state == ConnectionState.CONNECTED){
                return true;
            }else if(state == ConnectionState.CONNECTION_REFUSED || state == ConnectionState.FATAL_ERROR || state == ConnectionState.TIME_OUT){
                resolveProblem();
            }else{
                stopConnection();
            }
            
        }
        state = ConnectionState.TIME_OUT;
        ConsoleManager.writeOnConsole(prefix, "Could not connect in "+attempts+" attempts!");
        return false;
    }
    
    public void startConnection(){
        if(state == ConnectionState.CONNECTION_REFUSED || state == ConnectionState.FATAL_ERROR || state == ConnectionState.TIME_OUT){
            ConsoleManager.writeOnConsole(prefix, "The Connection is damaged! You have to resolve the problem frist!");
        }else if(state == ConnectionState.CONNECTED || state == ConnectionState.CONNECTING){
            ConsoleManager.writeOnConsole(prefix, "The Connection is active! You have to stop the connection first!");
        }else if(state == ConnectionState.NOT_CONNECTED){
            try {
                state = ConnectionState.CONNECTING;
                client = new Socket(server_address, server_port);
                state = ConnectionState.CONNECTED;
            } catch (IOException ex) {
                state = ConnectionState.FATAL_ERROR;
                ConsoleManager.writeOnConsole(prefix, "The Connection could not be created! Cannot open a connection without a server at the server_address and server_port!");
            }
        }
    }
    
    public void stopConnection(){
        if(state == ConnectionState.CONNECTION_REFUSED || state == ConnectionState.FATAL_ERROR || state == ConnectionState.TIME_OUT){
            ConsoleManager.writeOnConsole(prefix, "The Connection is damaged! Cannot close a connection thas is damaged!");
        }else if(state == ConnectionState.CONNECTED || state == ConnectionState.CONNECTING){
            try {
                client.close();
                client = null;
                state = ConnectionState.NOT_CONNECTED;
            } catch (IOException ex) {
                Logger.getLogger(ConnectionSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(state == ConnectionState.NOT_CONNECTED){
            ConsoleManager.writeOnConsole(prefix, "The Connection is not active! Cannot close a connection thas is already closed!");
        }
    }
    
    
    public void resolveProblem(){
        if(state == ConnectionState.CONNECTION_REFUSED || state == ConnectionState.FATAL_ERROR || state == ConnectionState.TIME_OUT){
            try {
                if(client != null){
                    client.close();
                    client = null;
                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            state = ConnectionState.NOT_CONNECTED;
            
            return;
        }
        ConsoleManager.writeOnConsole(prefix, "The ConnectionState is not an error and cannot be resolved!");
    }

    public ConnectionState getState() {
        return state;
    }
    
    
    
}
