import java.net.*;
import java.io.*;

public class Client{
	
	private static Socket toS;
	private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
	private static OutputStream out;
	private static InputStream in;
	
	public static void main(String[] args){
		if(args.length!=2){
			System.out.println("USE:\njava Client address #port");
			return;
		}
		
		int port;
		try{
			port = Integer.parseInt(args[1]);
			if(port<=0)
				throw new NumberFormatException("Invalid port");
		}
		catch(NumberFormatException nfe){
			System.out.println("Invalid port, must be a positive number");
			return;
		}
		
		try{
			toS = new Socket(args[0], port);
						
			out=toS.getOutputStream();
			in=toS.getInputStream();
			while(true){
				sendMessage();
				receiveMessage();
			}
		}
		catch(IOException ioe){
			if(!ioe.getMessage().equals("Connection close"))
				System.out.println("Connection lost");
		}
		finally{
			closeCon();
			return;
		}
	}
	
	private static void sendMessage() throws IOException{
		String input = bf.readLine();
		out.write(input.getBytes(), 0, input.length());
		if(input.equals(".")){
			System.out.println("Connection close");
			throw new IOException("Connection close");
		}
		return;
	}
	
	private static void receiveMessage() throws IOException{
		byte buffer[] = new byte[100];
		int letti = in.read(buffer);
		
		if(letti<=0)
			throw new IOException();
		
		System.out.println("Echoed: " + new String(buffer, 0, letti));
		return;
	}
	
	private static void closeCon(){
		try{
			toS.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
