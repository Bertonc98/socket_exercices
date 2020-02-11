import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;

public class Client{
	public static void main(String[] args){
		if(args.length==2){
			String serv_name = args[0];
			int port;
			try{
				port = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException nfe){
				throw new IllegalArgumentException("Inserire una porta valida");
			}
			if(port<=0)
				throw new IllegalArgumentException("Inserire una porta valida");
			
			//se i parametri sono validi inizia l'esecuzione del client
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			try{
				String input;
				byte buffer[] = new byte[100];
				boolean exit = false;
				int letti;
				Socket toS = new Socket(serv_name, port);
				InputStream in = toS.getInputStream();
				OutputStream out = toS.getOutputStream();
				int i=0;
				//Ciclo finchè non c'è carattere di uscita o un eccezione
				while(!exit){
					//Ciclo per input dell'operatore
					while(i<3 && !exit){
						switch(i){
							case 0: System.out.print("Operazione: ");break;
							case 1: System.out.print("Primo operando: ");break;
							default: System.out.print("Secondo operando: ");break;
						}
						
						input = bf.readLine();
						if(input.equals(".")){
							exit=true;
							break;
						}
						//Se non è carattere di uscita lo invio come operando
						out.write(input.getBytes(), 0, input.length());
						letti = in.read(buffer);
						if(letti<=0){
							throw new IOException();
						}
						if((new String(buffer, 0, letti)).equals("wrong")){
							switch(i){
								case 0: System.out.println("Operazione non valida");break;
								default: System.out.println("Operando non valido");break;
							}
							continue;
						}
						//Se l'input è stato validato dal server si passa al prossimo
						i++;
						if(i>2){
							System.out.println("Attesa risultato");
							letti = in.read(buffer);
							if(letti<=0){
								throw new IOException();
							}
							System.out.println("Risultato: " + (new String(buffer, 0, letti)));
							i=0;
							exit = false;						
						}
					}
					if(exit){
						out.write(".".getBytes(), 0, ".".length());
						break;
					}
				}
				toS.close();
			}
			catch(IOException ioe){
				System.out.println("Connessione persa");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			System.out.println("USE:\njava Client address #port");
		}
	}
}
