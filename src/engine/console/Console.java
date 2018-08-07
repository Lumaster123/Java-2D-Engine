package engine.console;

import engine.filesystem.FileSystem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Console extends JFrame implements ActionListener{

    private int width = 800, height = width / 16 * 9;
    private int minWidth = 800, minHeight = minWidth / 16 * 9;
    
    private JPanel panel;
    private JTextArea textArea;
    private JScrollPane textOutput;
    private JTextField textInput;
    private JButton send;
    
    public String text;
    public ArrayList<String> permissions;
    
    public Console(String title) {
        super();
        
        text = "";
        permissions = new ArrayList<>();
        permissions.add("*");
        
        setFrameOptions(title);

    }

    private void setFrameOptions(String title){
        
        getContentPane().setPreferredSize(new Dimension(width, height));
        pack();
        
        minWidth = getWidth(); minHeight = getHeight();
        
        setIconImage(FileSystem.readInternImage("engine/res/icon_console.png"));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle(title);
        setLocationRelativeTo(null);
        
        setMinimumSize(new Dimension(minWidth, minHeight));
        
        initialize();
        
        setContentPane(panel);
        
        setLocationByPlatform(true);
        setVisible(true);
    }
    
    private void initialize(){
        
        panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(getContentPane().getWidth(), getContentPane().getHeight());
        
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(Color.WHITE);
        textArea.setEditable(false);
        
        textOutput = new JScrollPane(textArea);
        textOutput.setSize(panel.getWidth() - 13, panel.getHeight() - 48);
        textOutput.setLocation(6, 6);
        textOutput.createVerticalScrollBar();
        textOutput.setWheelScrollingEnabled(true);
        textOutput.setFocusable(false);
        panel.add(textOutput);
        
        textInput = new JTextField();
        textInput.setSize(panel.getWidth() - 82, 30);
        textInput.setLocation(6, panel.getHeight() - 42 + 6);
        textInput.setFont(new Font("Arial", Font.PLAIN, 16));
        textInput.setBackground(Color.WHITE);
        textInput.addActionListener(this);
        textInput.setFocusable(true);
        panel.add(textInput);
        
        send = new JButton();
        send.setSize(70,29);
        send.setLocation(panel.getWidth() - 82 + 6, panel.getHeight() - 42 + 6);
        send.setFont(new Font("Arial", Font.PLAIN, 12));
        send.setText("SEND");
        send.addActionListener(this);
        send.setFocusable(false);
        panel.add(send);
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] tmp = textInput.getText().split(" ");
        String[] args = new String[tmp.length+1];
        args[0] = "Console";
        for(int i = 1; i < args.length; i++)
            args[i] = tmp[i-1];
        ConsoleManager.runCommand(args);
        textInput.setText("");
    }
    
    public void updateTextArea(String text){
        this.text += text + "\n";
        textArea.setText(this.text);
    }
    
}
