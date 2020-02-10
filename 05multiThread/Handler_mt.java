import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.io.*;

public class Handler_mt extends Thread{
	private Socket toC;
	
	public Handler_mt(Socket client){
		toC = client;
		System.out.println("New Thread start to manage: " + toC.getInetAddress() + "/" + toC.getPort());
	}
	
	public void run(){
		byte buffer[] = new byte[100];
		int letti;
		InputStream in = null;
		OutputStream out = null;
		try{
			in = toC.getInputStream();
			out = toC. getOutputStream();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		String message;
		try{
			while(true){
				letti = in.read(buffer);
				if(letti<=0)
					break;
				message = new String(buffer, 0, letti);
				if(message.equals("0"))
					break;
				
				System.out.println("|><|: " + message);
				toC.setSoTimeout(3000);
				out.write("ACK".getBytes(), 0, "ACK".length());
				System.out.println("HERE");
				toC.setSoTimeout(0);
			}
		}
		catch(SocketException se){se.printStackTrace();}
		catch(IOException ioe){ioe.printStackTrace();}
		catch(Exception e){e.printStackTrace();}
		finally{
			System.out.println(toC.getInetAddress() + "/" + toC.getPort() + " closed");
			try{
				toC.close();
			}catch(Exception e){e.printStackTrace();}
			return;
		}
	}
}
