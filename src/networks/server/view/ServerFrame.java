package networks.server.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ServerFrame extends JFrame {
	
	private static final long serialVersionUID = -8508450805503580959L;
    JButton start = new JButton("Start");
    JButton stop = new JButton("Stop");
    TextArea log = new TextArea();
    public ServerFrame(){
    	
    	setLayout(new BorderLayout());
    	JPanel southPanel = new JPanel();
    	southPanel.setLayout(new FlowLayout());
    	setLocationRelativeTo(null);
    	
    	southPanel.add(stop);
    	southPanel.add(start);
    	log.setEditable(false);
    	add(log,"Center");
    	add(southPanel,"South");
    	
    	pack();
    }
    
   
    public void setState(boolean starting){
    	stop.setEnabled(!starting);
    	start.setEnabled(starting);
    }
    public void addStartListeners(ActionListener listener){
    	start.addActionListener(listener);
    	
    }
    public void addStopListener(ActionListener listener){
    	stop.addActionListener(listener);
    }
    public void addMessageToLog(String message){
    	log.append(message+"\r\n");
    }
}
