import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class client_base{
	public static void main(String[] args){
		if(args.length==1){
			int port = Integer.parseInt(args[0]);
			Socket client = new Socket();
			
			try{
				InetSocketAddress isa = new InetSocketAddress("localhost", port);
				client.connect(isa);
				System.out.println("Client port: " + client.getLocalPort());
				System.out.println("Server port: " + client.getPort());
				Thread.sleep(5000);
			}
			catch(UnknownHostException hue){
				hue.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else
			System.out.println("Use:\n./client_base #port");
	}
}
