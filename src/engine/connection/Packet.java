package engine.connection;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Packet implements Cloneable, Serializable{
    
    private InetAddress source;
    private ArrayList<InetAddress> routingTable;
    private InetAddress target;
        
    private String command_short;
    private String[] command;
    
    private ArrayList<Object> data;
    
    public Packet(String command){
        routingTable = new ArrayList<>();
        prepareCommand(command);
        data = new ArrayList<>();
    }
    
    public Packet(String command, ArrayList<Object> data){
        routingTable = new ArrayList<>();
        prepareCommand(command);
        this.data = new ArrayList<>();
        
        addData(data);
    }
    
    public void preparePacket(InetAddress source, InetAddress target){
        if(source != null && target != null){
            if(routingTable.size() >= 2 && routingTable.get(routingTable.size()-2).equals(source) && routingTable.get(routingTable.size()-1).equals(target)){
                Exception ex = new Exception("[ConnectionSystem - Packet] This packet was already sent! A packet can only be sent once to the same target!");
                ex.printStackTrace();
            }else{
                if(routingTable.size() >= 2 && routingTable.get(routingTable.size()-1).equals(source)){
                    routingTable.add(target);
                }else{
                    routingTable.add(source);
                    routingTable.add(target);
                }
                this.source = source;
                this.target = target;
            }
        }else{
            Exception ex = new Exception("[ConnectionSystem - Packet] Source and/or target can't be NULL!");
            ex.printStackTrace();
        }
    }
    
    public boolean isPacketReady(){
        if(routingTable.size() >= 2 && routingTable.get(routingTable.size()-2).equals(source) && routingTable.get(routingTable.size()-1).equals(target)){
            System.err.println("[ConnectionSystem - Packet] This packet was already sent! A packet can only be sent once to the same target!");
            return false;
        }
        return true;
    }
    
    private void prepareCommand(String command){
        if(command == null || command.equals("") || command.equals(" ")){
            Exception ex = new Exception("[ConnectionSystem - Packet] Command can't be nothing!");
            ex.printStackTrace();
        }
        String[] command_part = command.split(" ");
        command_short = command_part[0];
        this.command = command_part;
    }
    
    public void addData(Object object){
        data.add(object);
    }
    
    public void addData(Object[] objects){
        for (Object object : objects) {
            data.add(object);
        }
    }
    
    public void addData(ArrayList<Object> objects){
        for (Object object : objects) {
            data.add(object);
        }
    }
    
    public void clearData(){
        data.clear();
    }

    public ArrayList<Object> getData() {
        return data;
    }
    
    public ArrayList<InetAddress> getRoutingTable() {
        return routingTable;
    }

    public InetAddress getSource() {
        return source;
    }

    public InetAddress getTarget() {
        return target;
    }

    public String getCommand_short() {
        return command_short;
    }

    public String[] getCommand() {
        return command;
    }
    
    public Packet clone(){
        try {
            Packet packet = (Packet) super.clone();
            packet.command = command.clone();
            packet.data = (ArrayList<Object>)data.clone();
            packet.routingTable = (ArrayList<InetAddress>)routingTable.clone();
            
            return packet;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Packet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
