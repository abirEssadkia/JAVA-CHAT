package JavaChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurMT extends Thread{
	private boolean isActive=true;
	private int nombreClients=0;
	public static void main(String[] args) {
		new ServeurMT().start();
		
	}
	@Override
	public void run(){
		try {
			ServerSocket serverSocket = new ServerSocket(1234);
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
				pw.println("BienVenue, Vous Etes le Client Numéro"+numero);
				System.out.println("Connexion du client Numéro"+numero+", Ip="+ipClient);
				
				while(true) {
					String req= br.readLine();
					String reponse="Length="+req.length();
					pw.println(reponse);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
