package networks.server;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class ChatServer {

	private static final Logger LOG = Logger.getLogger(ChatServer.class.getName());

	private int port;
	private ServerSocket server;
	private boolean shutdown;
	private BlockingQueue<String> buffer = new LinkedBlockingQueue<String>();
	private Collection<ConnectionHandler> handlers = new LinkedBlockingQueue<ConnectionHandler>();

	public ChatServer(int port) {
		this.port = port;
		createUnexpectedErrorHandler();
		startServer();
	}

	private void createUnexpectedErrorHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				LOG.error("Unexpected exceprion", e);
			}
		});
	}

	private void startServer() {
		try {
			LOG.info("Starting server on port " + port);
			server = new ServerSocket(port);
			LOG.info("Server started");
		} catch (IOException e) {
			LOG.error("Unable start server on port " + port, e);
		}
		
		MessageSender msender = new MessageSender(buffer, handlers); 
		Thread mthread = new Thread(msender);
		mthread.start();

		while (!shutdown) {
			Socket conn = null;
			try {
				conn = server.accept();
				LOG.info("New connection");
			} catch (IOException e) {
				LOG.error("Connection not accepted", e);
			}
			ConnectionHandler handler = new ConnectionHandler(conn, buffer);
			handlers.add(handler);
			Thread thread = new Thread(handler);
			thread.start();
		}

	}


}
