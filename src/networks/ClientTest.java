package networks;

import java.net.InetAddress;
import java.net.UnknownHostException;

import networks.client.ChatClient;

public class ClientTest {


	public static void main(String[] args) throws UnknownHostException {
		ChatClient server = new ChatClient(InetAddress.getLocalHost(),1000);
	}

}
