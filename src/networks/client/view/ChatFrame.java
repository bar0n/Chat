package networks.client.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import networks.client.ChatClient;

public class ChatFrame extends JFrame {
	

	private static final long serialVersionUID = -6187886105727910452L;
	ChatClient server;
	JTextField userName = new JTextField();
	JTextField serverAddress = new JTextField("192.168.1.149");
	JTextField serverPort = new JTextField("56789");
	JButton connect = new JButton("Connect");
	JButton send = new JButton("Send");
	TextArea chatField = new TextArea();
	JTextField messageField = new JTextField();
	public ChatFrame(ChatClient server) throws HeadlessException {

		this.server = server;

		userName.setColumns(6);
		serverAddress.setColumns(8);
		serverPort.setColumns(4);
		setLayout(new BorderLayout());
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		
		southPanel.add(messageField);
		messageField.setColumns(50);
		southPanel.add(send);
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout());
		
		northPanel.add(new JLabel("User: "));	
		northPanel.add(userName);
		
		northPanel.add(new JLabel("Server: "));
		northPanel.add(serverAddress);
		northPanel.add(new JLabel("port: "));
		northPanel.add(serverPort);
		northPanel.add(connect);
		setLocationRelativeTo(null);				
		chatField.setEditable(false);
		
		JScrollPane sp = new JScrollPane(chatField);	
		
		add(sp, "Center");
		add(southPanel, "South");
		add(northPanel, "North");
		pack();
		
		addListeners();
	}
	
	private void addListeners() {
		PushButtonListener pushButtonListener = new PushButtonListener();
		send.addActionListener(pushButtonListener);
		messageField.addActionListener(pushButtonListener);
		connect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				connect();
				
			}
			
		});
	}
	
	
	private void connect() {
		int port = Integer.parseInt(serverPort.getText());
		server.setPort(port);
		server.setHost(serverAddress.getText());
		server.startClient();		
		new Thread(server).start();
		connect.setEnabled(false);
		userName.setEditable(false);
		serverAddress.setEditable(false);
		serverPort.setEditable(false);
	}

	public void readyToConnect(){
		connect.setEnabled(true);
		userName.setEditable(true);
		serverAddress.setEditable(true);
		serverPort.setEditable(true);
	}
	
	public class PushButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Message"+messageField.getText());
			server.pushMessage(userName.getText()+":"+messageField.getText());
		}

	}
	public void addMessage(String message){
		chatField.append(message+"\r\n");
	}


}
