import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.InetAddress;
import java.io.*;
import java.util.*;

public class Stazione{
	public static void main(String[] args){
		if(args.length==2){
			if(Integer.parseInt(args[1])<=0)
				throw new IllegalArgumentException("Illegal port");
			DatagramSocket toS;
			DatagramPacket dp = null;
			try{
				toS = new DatagramSocket();
				InetAddress ia = InetAddress.getByName(args[0]);
				int port = Integer.parseInt(args[1]);
				System.out.println("Utente: " + toS.getLocalAddress() + "/" + toS.getLocalPort());
				BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
				byte buffer[] = new byte[100];
				String message;
				String tmp = null;
				int treno, ritardo;
				while(true){
					System.out.print("Insert train number: ");
					try{
						tmp = bf.readLine();
						treno = Integer.parseInt(tmp);
					}
					catch(NumberFormatException nfe){
						if(tmp.equals(".")){
							dp=new DatagramPacket((".@"+tmp).getBytes(), 0, (".@"+tmp).length(), ia, port);
							toS.send(dp);
							break;
						}
						else
							continue;
					}
					if(treno>9 || treno<1)
						continue;
					System.out.print("Insert minutes of late: ");
					ritardo = Integer.parseInt(bf.readLine());
					message = treno + "@" + ritardo;
					dp = new DatagramPacket(message.getBytes(), 0, message.length(), ia, port);
					toS.send(dp);
					dp = new DatagramPacket(buffer, 100);
					toS.receive(dp);
					System.out.println(new String(buffer, 0, dp.getLength()));					
				}
			}
			catch(SocketException se){
				se.printStackTrace();
			}
			catch(IOException ioe){
				ioe.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else
			System.out.println("USE:\njava Stazione address #port");
	}
}
