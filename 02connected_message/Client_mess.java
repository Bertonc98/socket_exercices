import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.*;
import java.util.*;

public class Client_mess{
	public static void main(String[] args){
		if(args.length==2){
			Socket server = new Socket();
			InputStreamReader keyboard = new InputStreamReader(System.in);
			BufferedReader bf = new BufferedReader(keyboard);
			String tmp;
			StringBuilder support = new StringBuilder();
			String message;
			try{
				while(!(tmp=bf.readLine()).equals(".") || support.length()<=0){
					support.append(tmp);
					support.append("@");
				}				
				message = support.toString().substring(0, support.length()-1);
				System.out.println(message);
				
				InetSocketAddress isa = new InetSocketAddress(args[0] , Integer.parseInt(args[1]));
				server.connect(isa);
				OutputStream out = server.getOutputStream();
				out.write(message.getBytes(), 0, message.length());
			}
			catch(Exception e){
				e.printStackTrace();}
		}
		else
			System.out.println("USE:\njava Client_mess address #port");
	}
}
