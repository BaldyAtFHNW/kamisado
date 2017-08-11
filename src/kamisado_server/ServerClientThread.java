package kamisado_server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerClientThread implements Runnable{
	private Logger logger = Logger.getLogger("");
	ServerModel model;
	Socket clientSocket;
	boolean black = false;
	BufferedReader in;

	public ServerClientThread(ServerModel model, Socket socket, boolean black){
		this.model = model;
		this.clientSocket = socket;
		this.black = black;
	}
	
	@Override
	public void run(){
		BufferedReader in;
		String msg;
		
		try{
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while(model.running){
				msg = in.readLine();
				if(black == true){
					model.newestMsgPlBlack.set(msg);
				}else{
					model.newestMsgPlWhite.set(msg);
				}
			}
		}catch(Exception e){
			logger.warning(e.toString() + e.getStackTrace());
			e.printStackTrace();
		}
	}
}