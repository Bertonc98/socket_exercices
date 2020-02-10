import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.io.*;

public class Client_mt{
		public static void main(String[] args){
				if(args.length==2){
					Socket toS;
					String message;
					String addr = args[0];
					int port = Integer.parseInt(args[1]);
					if(port<=0)
						throw new IllegalArgumentException("Invalid port");
						
					int letti;
					byte buffer[] = new byte[100];
					try{
						toS = new Socket();
						toS.connect(new InetSocketAddress(addr, port));
						InputStreamReader keyboard = new InputStreamReader(System.in);
						BufferedReader bf = new BufferedReader(keyboard);
						InputStream in = toS.getInputStream();
						OutputStream out = toS.getOutputStream();
						System.out.print("Insert mesage: ");
						while(!(message = bf.readLine()).equals("0")){
							
							if(message.equals(""))
								continue;
							out.write(message.getBytes(), 0, message.length());
							
							letti = in.read(buffer);
							if(letti <=0)
								throw new IllegalArgumentException("Connection lost");
							
							if(!(new String(buffer, 0, letti)).equals("ACK")){
								out.write("0".getBytes(), 0, "0".length());
								throw new IllegalArgumentException("Invalid Ack, close connection");
							}
							System.out.print("Insert mesage: ");
						}
						
					}
					catch(SocketException se){se.printStackTrace();}
					catch(IOException ioe){ioe.printStackTrace();}
					catch(Exception e){e.printStackTrace();}
				}
				else
					System.out.println("USE:\njava Client_mt address #port   //send '0' to end connection");
		}
}
