import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.io.*;
import java.util.*;

public class Server{
	public static void main(String[] args){
		int timeout = 2000;
		int MAX = 4;
		int letti;
		ArrayList<Socket> client = new ArrayList<Socket>();
		ArrayList<Operation> operation = new ArrayList<Operation>();
		ServerSocket passive = null;
		int num=0;
		int index=0;
		Socket currC;
		Operation currOp;
		InputStream in;
		OutputStream out;
		String input, ack;
		byte buffer[] = new byte[100];
		double value;
		
		//ciclo infinito del server
		try{
			passive = new ServerSocket(0);
			System.out.println("Server attivo su indirizzo " + passive.getInetAddress() + " e porta " + passive.getLocalPort());
		while(true){
			index = 0;
			//Gestione accettazione
			try{
				passive.setSoTimeout(timeout);
				while(num<MAX){
					client.add(passive.accept());
					operation.add(new Operation());
					num++;
				}
			}
			catch(SocketTimeoutException ste){
				if(num>0)
					System.out.print("");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			//Gestione delle richieste
			while(index<num){
				try{
						//Salvo la socket e l'operazione corrente
						currC=client.get(index);
						currOp=operation.get(index);
						in = currC.getInputStream();
						out = currC.getOutputStream();
						//Set timeout della socket corrente
						currC.setSoTimeout(timeout);
						System.out.println(currC.getInetAddress() + "-" + currC.getPort());
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
									client.remove(index);
									operation.remove(index);
									num--;
									index--;
									break;
								}
								
								input = new String(buffer, 0, letti);
								System.out.println("Received: " + input);
								//Carattere terminatore
								if(input.equals(".")){
									System.out.println("Connessione chiusa");
									client.remove(index);
									operation.remove(index);
									num--;
									index--;
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
									continue;
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
									continue;
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
										continue;
									
								}
								
								if(currOp.issetOp2()){
									value = currOp.calculate();
									System.out.println("Invio risultato " + value);
									out.write(String.valueOf(value).getBytes(), 0, String.valueOf(value).length());
								}			
						}
					}
					catch(SocketTimeoutException ste){
						System.out.print("Prossimo client: ");
					}
					index++;
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
		finally{
			try{
				passive.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
