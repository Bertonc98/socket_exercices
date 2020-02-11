import java.net.Socket;
import java.net.InetAddress;
import java.net.*;
import java.io.*;

public class Handler extends Thread{
	
	private Socket toC;
	
	public Handler(Socket clientSocket){
		this.toC=clientSocket;
		System.out.println("Accepted: " + toC.getInetAddress() + "-" + toC.getPort());
	}
	
	public void run(){
		String echo;
		try{
			while(true){
				echo = this.handleInput();
				this.handleOutput(echo);
			}
		}
		catch(IOException ioe){
			if(ioe.getMessage().equals("Connection lost"))
				System.out.println("Connection error, closing " + toC.getInetAddress() + "-" + toC.getPort());
			else
				System.out.println("Connection closed by " + toC.getInetAddress() + "-" + toC.getPort());
		}
		finally{
			this.closeSocket();
			return;
		}
		
	}
	
	private String handleInput() throws IOException{
		byte buffer[] = new byte[100];
		int letti;		
		String input;
		InputStream in = toC.getInputStream();
		letti = in.read(buffer);
		if(letti<=0)
			throw new IOException("Connection lost");
		input = new String(buffer, 0, letti);
		if(input.equals("."))
			throw new IOException("Connection closed");
		return input;		
	}
	
	private void handleOutput(String echo) throws IOException{
		OutputStream out = this.toC.getOutputStream();
		out.write(echo.getBytes(), 0, echo.length());
		return;
	}
	
	private void closeSocket(){
		try{
			toC.close();
		}
		catch(IOException ioe){}
	}
}
