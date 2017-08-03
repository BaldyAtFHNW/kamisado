package kamisado_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

public class Kamisado_Client_ServerThread implements Runnable{
	Logger logger = Logger.getLogger("");
	
	Kamisado_Client_Model model;
	Socket serverSocket;
	
	public Kamisado_Client_ServerThread(Kamisado_Client_Model model, Socket serverSocket){
		this.model = model;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run(){
		BufferedReader in;
		String msg;
		
		try{
			in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			while(true){
				msg = in.readLine();
				model.newMessage(msg);
			}
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
		}
	}
	
}