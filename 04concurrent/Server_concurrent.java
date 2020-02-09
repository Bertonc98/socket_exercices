import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.*;
import java.util.*;

public class Server_concurrent{
		public static void main(String[] args){
			if(args.length==1){
				//Declared here to permit "close" in finally block
				ServerSocket passive = null;
				try{
					//Start of server passive port
					passive = new ServerSocket(0);
					System.out.println("SERVER STARTED ON PORT: " + passive.getLocalPort());
					int maxC = Integer.parseInt(args[0]);
					int numC = 0;
					int index = 0;
					ArrayList<Socket> clients = new ArrayList<Socket>(maxC);
					InputStream in;
					OutputStream out;
					byte buffer[] = new byte[100];
					int letti = 0;
					String message;
					//Infinite cycle to handle request
					while(true){
						//Handle of connect
							try{
								passive.setSoTimeout(5000);
								while(numC<maxC){
									clients.add(passive.accept());
									numC++;
								}
								if(numC==maxC)
									System.out.println("Max number of clients achieved");
							}
							catch(SocketTimeoutException ste){
								System.out.println("Timeout accept time");}
							catch(IOException ioe){
								ioe.printStackTrace();}
							finally{
								System.out.println("Start handling request");}
						
						//Handle of request
							
							while(numC>0){
									try{
										in = clients.get(index).getInputStream();
										out = clients.get(index).getOutputStream();
										clients.get(index).setSoTimeout(1000);
										while(true){
											letti = in.read(buffer);
											
											if(letti<=0)
												throw new Exception("ConnectionLost");
											message = new String(buffer, 0, letti);
											if(message.equals("0")){
												clients.get(index).close();
												clients.remove(index);
												numC--;
												break;
											}
											System.out.println("|><|: " + message);
											out.write("ACK".getBytes(), 0, "ACK".length());
										}
										
									}
									catch(SocketTimeoutException ste){
										System.out.println("Timeout client " + index);}
									catch(Exception e){
										e.printStackTrace();
										try{
											clients.get(index).close();
											clients.remove(index);
											numC--;
										}
										catch(Exception ex){
											ex.printStackTrace();}
									}
									index = numC!=0 ? (index+1)%numC : 0;
							}
							System.out.println("Handling connections");
					}
				}
				catch(IOException ioe){
					ioe.printStackTrace();}
				finally{
					try{passive.close();}
					catch(IOException ioe){
						ioe.printStackTrace();}
					}
			}
			else
				System.out.println("USE:\njava Server_concurrent #max_client");
		}
}
