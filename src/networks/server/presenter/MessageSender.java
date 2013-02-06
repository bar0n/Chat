package networks.server.presenter;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class MessageSender implements Runnable {
	BlockingQueue<String> buffer;
	Collection<ConnectionHandler> handlers;
	private static final Logger LOG = Logger.getLogger(MessageSender.class
			.getName());

	public MessageSender(BlockingQueue<String> buffer,
			Collection<ConnectionHandler> handlers) {
		super();
		this.buffer = buffer;
		this.handlers = handlers;
	}


	@Override
	public void run() {
		try {
			while (true) {
				String message = buffer.take();
				for (ConnectionHandler client : handlers) {
					client.send(message);
				}
			}
		} catch ( InterruptedException e) {
			LOG.error("Intrerapted Error ", e);
			return;
		}
		
	}

}
