import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.InetAddress;
import java.io.*;

public class Client_udp{
	public static void main(String[] args){
		if(args.length==2){
			try{
				String addr = args[0];
				int port = Integer.parseInt(args[1]);
				if(port<=0)
					throw new IllegalArgumentException("Invalid port number");
				
				DatagramSocket toS;
				InputStreamReader keyboard = new InputStreamReader(System.in);
				BufferedReader bf = new BufferedReader(keyboard);
				
				toS = new DatagramSocket();
				String tmp = bf.readLine();
				DatagramPacket message = new DatagramPacket(tmp.getBytes(), 0, tmp.length(), InetAddress.getByName(addr), port);
				toS.send(message);
				
			}
			catch(SocketException se){
				se.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else
			System.out.println("USE:\njava Client_udp address #port");
	}
}
