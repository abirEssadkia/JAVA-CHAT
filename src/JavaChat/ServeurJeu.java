package JavaChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurJeu extends Thread{
	private boolean isActive=true;
	private int nombreClients=0;
	private int nombreSecret;
	private boolean fin;
	private String gagnant;
	public static void main(String[] args) {
		new ServeurJeu().start();
		
	}
	@Override
	public void run(){
		try {
			ServerSocket serverSocket = new ServerSocket(1234);
			nombreSecret = new Random().nextInt(1000);
			
			while(isActive) {
				Socket socket = serverSocket.accept();
				++nombreClients;
				new Conversation(socket, nombreClients).start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class Conversation extends Thread{
		private Socket socketClient;
		private int numero;
		
		public Conversation(Socket socketClient, int numero) {
			this.socketClient = socketClient;
			this.numero = numero;
		}
		@Override
		public void run() {
			try {
				InputStream inputStream = socketClient.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(isr);
				
				PrintWriter pw = new PrintWriter(socketClient.getOutputStream(),true);
				String ipClient=socketClient.getRemoteSocketAddress().toString();
				pw.println("BienVenue "+numero);
				System.out.println("Connecté"+numero+", Ip="+ipClient);
				pw.println("Devinez le nombre secret....?");
				
				while(true) {
					String req=br.readLine();
					int nombre=0;
					boolean correctFormatRequest=false;
					try {
						nombre = Integer.parseInt(req);
						correctFormatRequest=true;
					}
					catch(NumberFormatException e) {
						correctFormatRequest=false;
					}
					if(correctFormatRequest) {
						System.out.println("Cleint"+ipClient+"Tentative avec le nombre"+nombre); 
						if(fin==false) {
							if(nombre>nombreSecret) {
								pw.println("Votre nombre est superieur au nombre secret");
								
							}else if(nombre<nombreSecret) {
								pw.println("Votre nombre est inferieur au nombre secret");
							} else {
								pw.print("BRAVO, vous avez gagné");
								gagnant=ipClient;
								System.out.println("BRAVO au gagnant, IP Client est:"+ipClient);
								fin=true;
							}
							
						}else {
							pw.println("Jeu terminé, le gagnant est:"+gagnant);
						}
					}
					else {
						pw.println("Format de nombre incorrect!");
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
