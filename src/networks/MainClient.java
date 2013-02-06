package networks;

import java.net.UnknownHostException;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import networks.client.ChatClient;
import networks.client.ChatFrame;

public class MainClient {

	public static void main(String[] args) throws UnknownHostException {
		
		ChatClient server = new ChatClient();
		final ChatFrame view = new ChatFrame(server);
		server.setView(view);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				view.setVisible(true);

			}
		});

	}

}
