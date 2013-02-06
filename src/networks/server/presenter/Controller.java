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

public class Controller {
	private static final Logger LOG = Logger.getLogger(Controller.class
			.getName());

	private static final int MAX_CLIENTS = 10;
	private ServerFrame view;
	private boolean state;

	private int port = 56789;
	private ServerSocket server;

	private BlockingQueue<String> buffer = new LinkedBlockingQueue<String>();
	private Collection<ConnectionHandler> handlers = new ArrayBlockingQueue<ConnectionHandler>(
			port);

	private ExecutorService executor;

	public Controller(ServerFrame view) {
		
		this.view = view;
		state = true;
		view.setState(state);
		view.addMessageToLog("to start listen client push button start");
		registerListeners();
		executor = Executors.newFixedThreadPool(MAX_CLIENTS);
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
				stopServer();
				view.addMessageToLog("stoping server");

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
		}
		state =!state;
		view.setState(state);// now state is listen new client;
		view.addMessageToLog("Server started");
		
		new Thread(new MessageSender(buffer,handlers)).start();
		
		while (!state) {
			try {
				Socket client = server.accept();
				ConnectionHandler handler = new ConnectionHandler(client,
						buffer);
				handlers.add(handler);
				executor.execute(handler);
				view.addMessageToLog("new client attached , total clients: "
						+ handlers.size());
				LOG.info("accept new clients, total clients: "
						+ handlers.size());

			} catch (IOException e) {
				LOG.error("exceprion while listening client", e);
			}
		}

	}

	private void stopServer() {

	}

	private void createUnexpectedErrorHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				LOG.error("Unexpected exceprion", e);
			}
		});
	}
}
