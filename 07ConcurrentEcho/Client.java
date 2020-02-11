import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;

public class Client{
	
	private static Socket toS;
	private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
	private static InputStream in;
	private static OutputStream out;
	
	public static void main(String[] args){
		if(args.length!=2){
			System.out.println("USE:\njava Client address #port");
			return;
		}
		int port;
		try{
			port = Integer.parseInt(args[1]);
			if(port<=0)
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe){
			System.out.println("Use a valid port");
			return;
		}
		
		//Se la porta è valida si prova a creare la socket
		try{
			toS = new Socket(args[0], port);
			while(true){
				in = toS.getInputStream();
				out= toS.getOutputStream();
				
				sendMessage();
				readMessage();
			}
		}
		catch(UnknownHostException uhe){
			System.out.println("Impossible open socket with this server");
			return;
		}
		catch(SocketException se){
			System.out.println("Error opening socket");
		}
		catch(IOException ioe){
			if(ioe.getMessage().equals("closed"))
				System.out.println("Connection closed");
			else
				System.out.println("Connection error");
		}
		finally{
			closeCon();
		}
	}
	
	private static void sendMessage() throws IOException{
		String input = bf.readLine();
		
		if(input.equals(".")){
			out.write(input.getBytes(), 0, input.length());
			throw new IOException("closed");
		}
		out.write(input.getBytes(), 0, input.length());
		return;
	}
	
	private static void readMessage() throws IOException{
		byte buffer[] = new byte[100];
		int letti = in.read(buffer);
		if(letti<=0)
			throw new IOException("lost");
		System.out.println("Echoed: " + new String(buffer, 0, letti));
		return;
	}
	
	private static void closeCon(){
		try{
			toS.close();
		}
		catch(Exception e){}
	}
}
