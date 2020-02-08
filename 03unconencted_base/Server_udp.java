import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;

public class Server_udp{
	public static void main(String[] args){
		try{
			DatagramSocket toC = new DatagramSocket();
			System.out.println("PORT: " + toC.getLocalPort());
			byte buffer[] = new byte[100];
			DatagramPacket mex = new DatagramPacket(buffer, 100);
			toC.receive(mex);
			
			System.out.println(new String(buffer, 0, mex.getLength()));
			System.out.println("Message from: " + mex.getAddress() + " " + mex.getPort());
			
			toC.close();
		}
		catch(SocketException se){
			se.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
