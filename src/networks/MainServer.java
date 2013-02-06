package networks;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import networks.server.presenter.ChatServer;
import networks.server.view.ServerFrame;

public class MainServer {

	public static void main(String[] args) {
		
		final ServerFrame view =  new ServerFrame();
		@SuppressWarnings("unused")
		ChatServer controller = new ChatServer(view);
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {				
				view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				view.setVisible(true);
				
			}
		});
	}

}
