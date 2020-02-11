import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.io.*;
import java.util.*;



public class Server{
	
	private static ArrayList<Socket> client = new ArrayList<Socket>();
	private static ArrayList<Operation> operation = new ArrayList<Operation>();
	private static ServerSocket passive = null;
	private static final int MAX = 4;
	private static int num=0;
	
	public static void main(String[] args){
		int timeout = 10;
		int index=0;
		//ciclo infinito del server
		try{
			passive = new ServerSocket(0);
			System.out.println("Server attivo su indirizzo " + passive.getInetAddress() + " e porta " + passive.getLocalPort());
			while(true){
				index = 0;
				//Gestione accettazione
				try{
					connection(timeout);
				}
				catch(SocketTimeoutException ste){}
				catch(Exception e){
					e.printStackTrace();
				}
				
				//Gestione delle richieste
				while(index<num && client.size()>0){
					try{
						index = handleClient(index, timeout);						
					}
					catch(IOException ioe){
						ioe.printStackTrace();
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//Chiusura della socket del server e delle socket da lui generate
		finally{
			try{
				passive.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}


	private static void connection(int timeout) throws SocketTimeoutException, SocketException, IOException{
		passive.setSoTimeout(timeout);
		while(num<MAX){
			client.add(passive.accept());
			operation.add(new Operation());
			num++;
		}
	}

	private static int handleClient(int index, int timeout) throws IOException{
		//Salvo la socket e l'operazione corrente
		Socket currC = client.get(index);;
		Operation currOp=operation.get(index);
		InputStream in = currC.getInputStream();
		OutputStream out = currC.getOutputStream();
		int letti;
		double value;
		String input, ack;
		byte buffer[] = new byte[100];
		//Set timeout della socket corrente
		currC.setSoTimeout(timeout);
		
		try{
			//ciclo di lettura, esce con carattere terminatore, chiusura del canale o timeout
			while(true){
				try{
					letti = in.read(buffer);
				}
				catch(SocketException se){
					letti = -1;
				}
				//Canale chiuso
				if(letti<=0){
					System.out.println("Connessione persa");
					index = closeCon(index);
					break;
				}
				System.out.println(currC.getInetAddress() + "-" + currC.getPort());
				//Conversione a stringa
				input = new String(buffer, 0, letti);
				System.out.println("Received: " + input);
				//Carattere terminatore
				if(input.equals(".")){
					System.out.println("Connessione chiusa");
					index = closeCon(index);
					break;
				}
				
				//A seconda del punto a cui si è arrivati a gestire l'operazione si setta il
				//successivo parametro, se tutti settato ritorna il risultato
				if(!currOp.issetOp()){
					if(currOp.setOp(input))
						ack = "next";
					else
						ack = "wrong";
					
					out.write(ack.getBytes(), 0, ack.length());
					break;
				}
				if(!currOp.issetOp1()){
					try{
						value = Double.parseDouble(input);
						currOp.setOp1(value);
						System.out.println("Primo operando settato");
						ack="next";
						
					}
					catch(NumberFormatException nfe){
						//Se non è stato passato un double
						ack="wrong";
					}
					out.write(ack.getBytes(), 0, ack.length());
					break;
				}
				if(!currOp.issetOp2()){
					try{
						value = Double.parseDouble(input);
						if(currOp.setOp2(value)){
							System.out.println("Secondo operando settato");
							ack="next";
						}
						else
							ack="wrong";
					}
					catch(NumberFormatException nfe){
						//Se non è stato passato un double
						ack="wrong";
					}
					out.write(ack.getBytes(), 0, ack.length());
					if(ack.equals("wrong"))
						break;
					
				}
				//Solo se tutti gli elementi dell'operazione sono settati si invia il risultato e si passa al prossimo client
				if(currOp.issetOp2()){
					value = currOp.calculate();
					System.out.println("Invio risultato " + value);
					out.write(String.valueOf(value).getBytes(), 0, String.valueOf(value).length());
					currOp.reset();
					break;
				}			
			}
		}
		catch(SocketTimeoutException ste){}
		return ++index;
	}

	private static int closeCon(int index){
		client.remove(index);
		operation.remove(index);
		num--;
		return --index;
	}
}
