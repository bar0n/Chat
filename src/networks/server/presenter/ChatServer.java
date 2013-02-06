package networks.server.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import networks.server.view.ServerFrame;

import org.apache.log4j.Logger;

public class ChatServer {
	private static final Logger LOG = Logger.getLogger(ChatServer.class.getName());

	private static final int MAX_CLIENTS = 10;
	private ServerFrame view;
	private boolean state;

	private int port = 56789;
	private ServerSocket server;

	private BlockingQueue<String> buffer = new LinkedBlockingQueue<String>();
	private Collection<ConnectionHandler> handlers = new ArrayBlockingQueue<ConnectionHandler>(
			port);

	private ExecutorService executor;

	public ChatServer(ServerFrame view) {

		this.view = view;
		state = true;
		view.setState(state);
		view.addMessageToLog("to start listen client push button start");
		registerListeners();		
		createUnexpectedErrorHandler();
	}

	private void registerListeners() {
		view.addStartListeners(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable thread = new Runnable() {

					@Override
					public void run() {
						startServer();

					}
				};
				new Thread(thread).start();

			}

		});
		view.addStopListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Runnable thread = new Runnable() {

					@Override
					public void run() {
						stopServer();

					}
				};
				new Thread(thread).start();

			}

		});
	}

	private void startServer() {
		try {
			LOG.info("starting server with port: " + port);
			server = new ServerSocket(port);
			LOG.info("Server started");
		} catch (IOException e) {
			 LOG.error("Error starting Server", e);
			 return;
		}
		state = !state;
		view.setState(state);// now state is listen new client;
		view.addMessageToLog("Server started");

		new Thread(new MessageSender(buffer, handlers)).start();
		
		executor = Executors.newFixedThreadPool(MAX_CLIENTS);
		
		while (!state) {
			
				Socket client=null;
				try {
					client = server.accept();
				} catch (IOException e) {
					LOG.error("Connection not accepted", e);					
					continue;
				}
				
				
				ConnectionHandler handler = new ConnectionHandler(client, buffer, handlers);
				
				handlers.add(handler);
				executor.execute(handler);
				view.addMessageToLog("new client attached , total clients: "
						+ handlers.size());
				 LOG.info("accept new clients, total clients: "+ handlers.size());
			
		}
	}

	private void stopServer() {
		state = !state;
		System.out.println("server stoped");
		for (ConnectionHandler handler : handlers) {
			handler.stop();
		}
		handlers.clear();
		executor.shutdownNow();
		
		try {
			server.close();
		} catch (IOException e) {

			LOG.error("server colosing  ", e);
			
		}
		view.setState(state);
		view.addMessageToLog("server stoped");
	}

	private void createUnexpectedErrorHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				LOG.error("uncaught Exception ", e);
				
			}
		});
	}
}
