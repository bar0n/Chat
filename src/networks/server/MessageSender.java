package networks.server;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class MessageSender implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(ConnectionHandler.class.getName());

	private BlockingQueue<String> buffer;
	private Collection<ConnectionHandler> handlers;
	
	public MessageSender(BlockingQueue<String> buffer,
			Collection<ConnectionHandler> handlers) {
		super();
		this.buffer = buffer;
		this.handlers = handlers;
	}

	@Override
	public void run() {
		while(true) {
			String message;
			try {
				message = buffer.take();
			} catch (InterruptedException e) {
				LOG.info("Message sender stoped");
				break;
			}
			for (ConnectionHandler handler : handlers) {
				handler.send(message);
			}
		}
	}

}
