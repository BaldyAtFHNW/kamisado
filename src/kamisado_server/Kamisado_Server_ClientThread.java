package kamisado_server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Kamisado_Server_ClientThread implements Runnable{
	private Logger logger = Logger.getLogger("");
	Kamisado_Server_Model model;
	Socket clientSocket;
	boolean player1 = false;
	BufferedReader in;

	public Kamisado_Server_ClientThread(Kamisado_Server_Model model, Socket socket, boolean player1){
		this.model = model;
		this.clientSocket = socket;
		this.player1 = player1;
	}
	
	
	@Override
	public void run(){
		BufferedReader in;
		String msg;
		
		try{
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while(true){
				msg = in.readLine();
				if(player1 == true){
					model.newestMsgP1.set(msg);
				}else{
					model.newestMsgP2.set(msg);
				}
			}
		}catch(Exception e){
			logger.warning(e.toString() + e.getStackTrace());
			e.printStackTrace();
		}
	}
}