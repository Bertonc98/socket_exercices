import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.InetAddress;
import java.io.*;
import java.util.*;

public class Utente{
	public static void main(String[] args){
		if(args.length==2){
			if(Integer.parseInt(args[1])<=0)
				throw new IllegalArgumentException("Illegal port");
			DatagramSocket toS = null;
			try{
				toS = new DatagramSocket();
				System.out.println("Utente: " + toS.getLocalAddress() + "/" + toS.getLocalPort());
				BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
				DatagramPacket dp = null;
				byte buffer[] = new byte[100];
				String treno;
				while(true){
					System.out.print("Stazione desiderata: ");
					treno = bf.readLine();
					if(treno.equals(".")){
						dp = new DatagramPacket(".".getBytes(), 0, ".".length(), InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
						toS.send(dp);
						break;
					}
					else{
							try{
								int st = Integer.parseInt(treno);
								dp = new DatagramPacket(treno.getBytes(), 0, treno.length(), InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
								toS.send(dp);
								dp = new DatagramPacket(buffer, 100);
								toS.receive(dp);
								System.out.println(new String(dp.getData(), 0, dp.getLength()));
								String[] information = (new String(buffer, 0, dp.getLength())).split("@", 0);
								System.out.println("Treno " + treno + " ritardo di: " + information[1] + "' ultima rilevazione stazione " + information[0]);
							}
							catch(NumberFormatException nfe){
								System.out.println("Insert a valid station (1-9)");
							}
					}
				}
				
			}
			catch(IOException ioe){
					ioe.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}		
			finally{
				try{
					toS.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}	
		}
		else
			System.out.println("USE:\njava Utente address #port");
	}
}
