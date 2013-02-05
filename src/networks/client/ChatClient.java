package networks.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class ChatClient {
	
	private static final Logger LOG = Logger.getLogger(ChatClient.class.getName());
	
	private Socket conn;
	private Scanner in;
	private PrintWriter out;
	private Scanner userInput;
	
	public ChatClient(InetAddress host, int port) {
		try {
			this.conn = new Socket(host, port);
			in = new Scanner( new BufferedInputStream(conn.getInputStream()));
			out = new  PrintWriter(new BufferedOutputStream(conn.getOutputStream()));
			userInput = new Scanner( new BufferedInputStream(System.in));
			run();
		} catch (IOException e) {
			LOG.error("Unable to connect.", e);
		}
		
	}
	
	private void run() {
		MessageReceiver receiver = new MessageReceiver(in);
		Thread thread = new Thread(receiver);
		thread.start();
		
		
		String message = null;
		while(true) {
			message = userInput.nextLine();
			out.println(message);
			out.flush();
		}
	}
	
}
