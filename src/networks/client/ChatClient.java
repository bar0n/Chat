package networks.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import networks.client.view.ChatFrame;

import org.apache.log4j.Logger;

public class ChatClient implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(ChatClient.class.getName());
	
	private Socket conn;
	
	private PrintWriter out;
	private Scanner userInput;
	private InetAddress host;
	private int port;
	private ChatFrame view;

	public ChatFrame getView() {
		return view;
	}

	public void setView(ChatFrame view) {
		this.view = view;
	}

	public ChatClient() {

	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void startClient() {
		try {
			this.conn = new Socket(host, port);
			
			out = new  PrintWriter(new BufferedOutputStream(conn.getOutputStream()));
			userInput = new Scanner( new BufferedInputStream( new BufferedInputStream(conn.getInputStream())));
			
		} catch (IOException e) {
			LOG.error("Unable to connect.", e);
		}
	}
	
	public InetAddress getHost() {
		return host;
	}

	public void setHost(String host) {
		try {
			this.host = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			
			LOG.error("Unable to resolve host: "+host, e);	
		}
	}

	public void run() {
			
		String message = null;
		try {
			while (true) {
				message = userInput.nextLine();
				view.addMessage(message);
			}
		} catch (Exception e) {
			LOG.info("Stop client", e);
			if (userInput!=null) userInput.close();
			if (out!=null) out.close();
			try {
				conn.close();
			} catch (IOException e1) {
				LOG.error("Unable to close client Socket.", e);				
			}
			conn=null; 
			view.readyToConnect();			 
		}
	}

	public void pushMessage(String string) {
		
		out.println(string);
		out.flush();
		
	}
	
}
