import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;

public class Client_concurrent{
	public static void main(String[] args){
		if(args.length==2){
			Socket serv = null;
			int port = Integer.parseInt(args[1]);
			int letti;
			InputStreamReader keyboard = new InputStreamReader(System.in);
			BufferedReader bf = new BufferedReader(keyboard);
			String message;
			byte buffer[] = new byte[100];
			if(port<=0)
				throw new IllegalArgumentException("Invalid port");
			try{
				InetAddress addr = InetAddress.getByName(args[0]);
				serv = new Socket();
				serv.connect(new InetSocketAddress(addr, port));
				OutputStream toS = serv.getOutputStream();
				InputStream fromS = serv.getInputStream();
				while(!(message = bf.readLine()).equals(".")){						
					toS.write(message.getBytes(), 0, message.length());
					letti = fromS.read(buffer);
					if(letti<=0)
						throw new IllegalArgumentException("Connection lost");
					if(!(new String(buffer, 0, letti)).equals("ACK"))
						throw new IllegalArgumentException("Wrong ack");
				}			
			}
			catch(UnknownHostException hue){
				hue.printStackTrace();}
			catch(SocketException se){
				se.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();}
			finally{
				try{serv.close();}
				catch(Exception e){e.printStackTrace();}
			}
		}
		else
			System.out.println("USE:\njava Client_concurrent address #port");
	}
}
