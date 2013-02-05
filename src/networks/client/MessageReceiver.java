package networks.client;

import java.util.Scanner;

public class MessageReceiver implements Runnable {
	
	private Scanner in;

	public MessageReceiver(Scanner in) {
		this.in = in;
	}

	@Override
	public void run() {
		String message = null;
		while(true) {
			message = in.nextLine();
			System.out.println(message);
		}
	}

}
