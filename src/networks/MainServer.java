package networks;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import networks.server.presenter.Controller;
import networks.server.view.ServerFrame;

public class MainServer {

	public static void main(String[] args) {
		
		final ServerFrame view =  new ServerFrame();
		@SuppressWarnings("unused")
		Controller controller = new Controller(view);
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {				
				view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				view.setVisible(true);
				
			}
		});
	}

}
