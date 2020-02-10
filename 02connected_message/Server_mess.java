import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.io.*;
import java.util.*;

public class Server_mess{
	public static void main(String[] args){
		ServerSocket ss;
		Socket client;
		byte buffer[] = new byte[100];
		int letti;
		
		try{
			ss = new ServerSocket(0);
			System.out.println("Server open on port: " + ss.getLocalPort());
			client = ss.accept();
			InputStream fromC = client.getInputStream();
			letti = fromC.read(buffer);
			if(letti<=0)
				System.out.println("Connection lost");
			else{
				StringTokenizer splitted = new StringTokenizer(new String(buffer, 0, letti), "@");
				while(splitted.hasMoreTokens())
					System.out.print(splitted.nextToken() + " ");
				System.out.println();
			}
			
			ss.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
