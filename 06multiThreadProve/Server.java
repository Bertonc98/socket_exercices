import java.net.*;
import java.io.*;

public class Server{
	
	private static ServerSocket passive;
	
	public static void main(String[] args){
		try{
			passive = new ServerSocket(0);
			System.out.println("Server started: " + passive.getInetAddress() + " " + passive.getLocalPort());
			while(true){
				handleClient();
			}
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	private static void handleClient() throws IOException{
		Socket tmp = passive.accept();
		Thread t = new Handler(tmp);
		t.start();
	}
}
