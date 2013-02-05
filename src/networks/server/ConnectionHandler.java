package networks.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class ConnectionHandler implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(ConnectionHandler.class.getName());
	
	private Socket conn;
	private BlockingQueue<String> buffer;
	private Scanner in;
	private PrintWriter out;

	public ConnectionHandler(Socket conn, BlockingQueue<String> buffer) {
		this.conn = conn;
		this.buffer = buffer;
	}

	@Override
	public void run() {
		try {
			in = new Scanner( new BufferedInputStream(conn.getInputStream()));
			out = new  PrintWriter(new BufferedOutputStream(conn.getOutputStream()));
			send("Connected.");
			while (true) {
				String message = in.nextLine();
				buffer.add(message);
			}
		} catch (IOException e) {
			LOG.error("IO error", e);
		}
	}
	
	public void send(String message) {
		out.println(message);
		out.flush();
	}

}
