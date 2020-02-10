import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.io.*;

public class Server_mt{
		public static void main(String[] args){
				ServerSocket passive = null;
				Socket tmp;
				
				try{
					passive = new ServerSocket(0);
					System.out.println("SERVER OPEN ON PORT: " + passive.getLocalPort());
					
					while(true){
						tmp = passive.accept();
						Thread t = new Handler_mt(tmp);
						t.start();
					}
				}
				catch(SocketException se){se.printStackTrace();}
				catch(Exception e){e.printStackTrace();}
				finally{
					try{passive.close();}
					catch(Exception e){e.printStackTrace();}
				}
		}
}
