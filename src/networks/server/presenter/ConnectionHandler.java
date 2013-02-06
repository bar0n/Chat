package networks.server.presenter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class ConnectionHandler implements Runnable{
	private static final Logger LOG = Logger.getLogger(ConnectionHandler.class.getName());
	private Socket client;
	private BlockingQueue<String> buffer; 
	private boolean running;
	private PrintWriter out;
	private Scanner in;
	public ConnectionHandler(Socket client, BlockingQueue<String> buffer) {
		
		this.client = client;
		this.buffer = buffer;
		running = true;
		
	}

	public void send(String message) {
		out.println(message);
		out.flush();
	}
	@Override
	public void run() {
		LOG.info("running client");
		try {
			in = new Scanner(new BufferedInputStream(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
			send("conected:");
			while (running) {
				String nextLine = in.nextLine();
				buffer.offer(nextLine);
			}
		} catch (IOException e) {
			LOG.error("Unexpected exceprion", e);
		}
		
	}

}
