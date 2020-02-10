import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.*;
import java.util.*;

public class Server{
	public static void main(String[] args){
		//Ritardi indicizzati dal numero della stazione
		//array string ontenente stazione|ritardo
		HashMap<Integer, String[]> ritardi = new HashMap<>();
		//Mappo indirizzi_porta in Maiuscola
		HashMap<String, Character> id = new HashMap<>();
		
		char lettera = 'A';
		
		int dim = 100;
		byte buffer[] = new byte[dim];
		String tmp = null;
		String message[] = null;
		//Creazione socket
		DatagramSocket serv = null;
		try{
			serv = new DatagramSocket();
		}
		catch(SocketException se){
			se.printStackTrace();
			throw new IllegalArgumentException("Impossible open socket");
		}
		//Comunicazione indirizzo e porta
		System.out.println("Server started on: " + serv.getLocalAddress() + "/" + serv.getLocalPort());
		
		DatagramPacket dp = new DatagramPacket(buffer, 0, dim);
		while(true){
			try{
				//In attesa di ricezione 
				serv.receive(dp);
				
				message = (new String(dp.getData(), 0, dp.getLength())).split("@", 0);
				//se ha un solo campo è arrivato da un utente, che chiede solo il codice di un treno
				if(message.length==1){
					try{
						//Se contiene il carattere di terminazione solleva un'eccezione gestita dal catch
						int st = Integer.parseInt(message[0]);
						
						System.out.println("Richiesta: " + message[0] + " da " + dp.getAddress() + "/" + dp.getPort());
						String response;
						if(ritardi.containsKey(Integer.parseInt(message[0])))
							response = ritardi.get(Integer.parseInt(message[0]))[0] + 
															"@" + ritardi.get(Integer.parseInt(message[0]))[1];
						else
							response = "---@---";
						//Invio risposta
						dp = new DatagramPacket(response.getBytes(), 0, response.length(), dp.getAddress(), dp.getPort());
					}
					catch(NumberFormatException nfe){
						System.out.println(dp.getAddress() + "_" + dp.getPort() + " terminated");
					}
					finally{
						serv.send(dp);
					}
				}
				//Se ha 2 campi è da una stazione, un campo identificativo del treno e un campo per i minuti di ritardo
				else if(message.length==2){
					String station = dp.getAddress() + "_" +dp.getPort();
					if(!id.containsKey(station)){
						id.put(station, lettera);
						lettera++;
					}
					
					if(message[0].equals(".")){
						System.out.println("Station " + id.get(station).toString() + " terminated");
						continue;
					}
					//Inserirsco l'aggiornamento
					ritardi.put(Integer.parseInt(message[0]), new String[]{id.get(station).toString(), message[1]});
					
					//Creo e invio il pacchetto di confermata ricezione
					dp = new DatagramPacket("Received".getBytes(), 0, "Received".length(), dp.getAddress(), dp.getPort());
					serv.send(dp);
				}
				//altre casistiche sono di errore, verrà scartato il pacchetto, ma una conferma di ricezione sarà inviata
				//per non bloccare il mittente
				else{
					dp = new DatagramPacket("Wrong".getBytes(), 0, "Wrong".length(), dp.getAddress(), dp.getPort());
					serv.send(dp);
				}
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
