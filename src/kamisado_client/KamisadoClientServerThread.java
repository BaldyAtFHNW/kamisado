package kamisado_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

public class KamisadoClientServerThread implements Runnable{
	Logger logger = Logger.getLogger("");
	
	private KamisadoClientModel model;
	private Socket serverSocket;
	
	public KamisadoClientServerThread(KamisadoClientModel model, Socket serverSocket){
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
				logger.info("Received: " + msg);
				model.newestMsg.set(msg);
			}
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
		}
	}
}