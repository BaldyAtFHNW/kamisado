package kamisado_client;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;

public class Kamisado_Client_Model {
	final private String ipAddress = "127.0.0.1";
	final private int port = 50000;
	private Logger logger = Logger.getLogger("");
	private Socket serverSocket;
	
	protected SimpleStringProperty newestMsg = new SimpleStringProperty();
	
	public void connectServer(){
		try{
			this.serverSocket = new Socket(ipAddress, port);
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
		}
		
		Kamisado_Client_ServerThread server = new Kamisado_Client_ServerThread(Kamisado_Client_Model.this, serverSocket);
		new Thread(server).start();		
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