import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class server_base{
	public static void main(String[] args){
		ServerSocket ss;
		Socket client;
		
		try{
			ss = new ServerSocket(0);
			System.out.println("THE CHOOSEN PORT: " + ss.getLocalPort());
			client = ss.accept();
			System.out.println("Server local port: " + client.getLocalPort());
			System.out.println("Client connected port: " + client.getPort());
			Thread.sleep(5000);
			ss.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
}
}
