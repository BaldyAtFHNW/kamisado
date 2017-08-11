package kamisado_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientServerThread implements Runnable{
	Logger logger = Logger.getLogger("");
	
	private ClientModel model;
	private Socket serverSocket;
	
	public ClientServerThread(ClientModel model, Socket serverSocket){
		this.model = model;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run(){
		BufferedReader in;
		String msg;
		
		try{
			in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			while(model.running){
				msg = in.readLine();
				model.newestMsg.set(msg);
			}
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
		}
	}
}