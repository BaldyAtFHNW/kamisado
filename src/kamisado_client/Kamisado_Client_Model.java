package kamisado_client;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Kamisado_Client_Model {
	final private String ipAddress = "127.0.0.1";
	final private int port = 50000;
	private Logger logger = Logger.getLogger("");
	private Socket serverSocket;
	private ArrayList<String> msgsServer = new ArrayList<String>();
	
	
	public void connectServer(){
		try{
			this.serverSocket = new Socket(ipAddress, port);
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
		}
		
		Kamisado_Client_ServerThread server = new Kamisado_Client_ServerThread(Kamisado_Client_Model.this, serverSocket);
		new Thread(server).start();
		
		logger.info(serverSocket.isConnected() ? "Connected" : "Not Connected");
		logger.info(serverSocket.isBound() ? "Bound" : "Not Bound");
		
	}
	
	public String getSocketStatus(){
		return "InputShutdown: " + (this.serverSocket.isInputShutdown() ? "True" : "False") + " OutputShutdown: " + (this.serverSocket.isOutputShutdown() ? "True" : "False");
	}
	
	public void newMessage(String msg){
		logger.info("RECEIVED SOMETHING");
		this.msgsServer.add(msg);
	}
	
	public String getMsgServer(){
		
		if(msgsServer.size() == 0){
			return null;
		}else{
			String msg = msgsServer.get(0);
			msgsServer.remove(0);
			return msg;
		}
	}
	
	public boolean msgPendingServer(){
		if (this.msgsServer.size() == 0){
			return false;
		}else{
			return true;
		}
	}
		
	public void send(String msg){
		OutputStreamWriter out;
		try{
			out = new OutputStreamWriter(this.serverSocket.getOutputStream());
			out.write(msg + "\n");
			out.flush();
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
		}
	}

}