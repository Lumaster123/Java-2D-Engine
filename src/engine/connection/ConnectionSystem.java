package engine.connection;

import engine.ThreadHandler;
import engine.Time;
import engine.console.ConsoleManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
    
    private ConnectionState state;
    
    private Socket client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    
    private ArrayList<Packet> receivedPacketList;
    private ArrayList<Packet> sendingPacketList;
    
    public ConnectionSystem(){
        receivedPacketList = new ArrayList<>();
        sendingPacketList = new ArrayList<>();
        try {
            state = ConnectionState.NOT_CONNECTED;
            server_port = 38475;
            this.server_address = InetAddress.getLocalHost();
            client = new Socket();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ConnectionSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        startStreamCycle();
    }
    
    public void initializeClient(String inetAddress, int port){
        if(state == ConnectionState.CONNECTION_REFUSED || state == ConnectionState.FATAL_ERROR || state == ConnectionState.TIME_OUT){
            ConsoleManager.writeOnConsole(prefix, "The Connection is damaged! You have to resolve the problem frist!");
        }else if(state == ConnectionState.CONNECTED || state == ConnectionState.CONNECTING){
            ConsoleManager.writeOnConsole(prefix, "The Connection is active! You have to stop the connection first!");
        }else if(state == ConnectionState.NOT_CONNECTED){
            try {
                server_address = InetAddress.getByName(inetAddress);
                server_port = port;
            } catch (UnknownHostException ex) {
                Logger.getLogger(ConnectionSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean connect(int attempts){
        
        for(int i = 0; i < attempts; i++){
            
            startConnection();
            if(state == ConnectionState.CONNECTED){
                try {
                    inputStream = new ObjectInputStream(client.getInputStream());
                    outputStream = new ObjectOutputStream(client.getOutputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
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
    
    public boolean sendPacket(Packet packet){
        if(packet == null){
            Exception ex = new Exception("[ConnectionSystem] Cannot accept a NULL-Packet!");
            ex.printStackTrace();
            return false;
        }
        packet.preparePacket(client.getLocalAddress(), client.getInetAddress());
        if(packet.isPacketReady()){
            sendingPacketList.add(packet);
            return true;
        }else{
            ConsoleManager.writeOnConsole(prefix, "An error occurred while preparing the Packet!");
            return false;
        }
    }
    
    public Packet receivePacket(){
        if(receivedPacketList.isEmpty()){
            while(receivedPacketList.isEmpty()){Time.sleep(0.1);}
        }
        Packet retPacket = receivedPacketList.get(0);
        receivedPacketList.remove(0);
        return retPacket;
    }
    
    private void startStreamCycle(){
        ThreadHandler.invoke("ConnectionSystemReadStreamThread", new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(state == ConnectionState.CONNECTED && client != null){
                        readInputStream();
                    }
                    Time.sleep(0.1);
                }
            }
        });
        ThreadHandler.invoke("ConnectionSystemWriteStreamThread", new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(state == ConnectionState.CONNECTED && client != null){
                        if(sendingPacketList.size() > 0){
                            writeOutputStream();
                        }
                    }
                }
            }
        });
    }
    
    private void readInputStream(){
        if(inputStream == null)
            return;
        
        try {
            Object input = inputStream.readObject();
            if(input instanceof Packet){
                receivedPacketList.add((Packet)input);
                System.out.println("received an Packet");
            }else{
                ConsoleManager.writeOnConsole(prefix, "Received an object, which is not a Packet!");
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionSystem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void writeOutputStream(){
        if(outputStream == null)
            return;
        
        Object obj = sendingPacketList.get(0);
        try {
            outputStream.writeObject(obj);
            System.out.println("sent the packet");
            sendingPacketList.remove(0);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
