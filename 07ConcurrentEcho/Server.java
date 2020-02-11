import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.io.*;
import java.util.*;

public class Server{
	
	public static ServerSocket passive;
	public static ArrayList<Socket> client = new ArrayList<>();
	public static InputStream in;
	public static OutputStream out;
	
	public static void main(String[] args){
		
		try{
			passive = new ServerSocket(0);
			System.out.println("Server on port: " + passive.getLocalPort());
		}
		catch(IOException ioe){
			System.out.println("Impossible open ServerSocket" + ioe);
		}
		
		//ciclo di gestione
		while(true){
			//Gestione connessioni
			try{
				passive.setSoTimeout(20);
				client.add(passive.accept());
			}
			catch(SocketTimeoutException ste){}
			catch(IOException ioe){
				System.out.println("Error accepting socket");
				return;
			}
			//Gestione client attivi
			if(client.size()>0){
				try{
					for(Socket curr:client){
						try{
							curr.setSoTimeout(30);
							handleRequest(curr);
						}
						catch(SocketTimeoutException ste){}
						catch(IOException ioe){
							if(ioe.getMessage().equals("closed"))
								System.out.println("Closed " + curr.getPort());
							else{
								System.out.println("Lost " + curr.getPort());
							}
						}
						
					}
			}catch(Exception e){}
			}
		}
	}
	
	public static void closeCon(Socket curr){
		try{
			curr.close();
		}
		catch(Exception e){}
	}
	public static void handleRequest(Socket curr) throws IOException{
		in = curr.getInputStream();
		out = curr.getOutputStream();
		String input = readMessage(curr);
		sendMessage(input);
		
	}
	
	private static String readMessage(Socket curr) throws IOException{
		byte buffer[] = new byte[100];
		int letti = in.read(buffer);

		if(letti<=0){
			client.remove(curr);
			throw new IOException("lost");	
		}
			
		if((new String(buffer, 0, letti)).equals(".")){
			client.remove(curr);
			throw new IOException("closed");
		}
		System.out.println(new String(buffer, 0, letti));
		return new String(buffer, 0, letti);
	}
	
	private static void sendMessage(String input) throws IOException{
		out.write(input.getBytes(), 0, input.length());
		return;
	}
	
}
