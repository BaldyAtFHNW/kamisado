package kamisado_client;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import javafx.beans.property.SimpleStringProperty;

public class KamisadoClientModel {
	final public String ipAddress;
	final private int port = 50000;
	private Logger logger = Logger.getLogger("");
	private Socket serverSocket;
	
	protected SimpleStringProperty newestMsg = new SimpleStringProperty();

	final public String playerName;
	public boolean black;
	public String opponent;
	
	public KamisadoClientModel(String ip, String playerName) {
		this.ipAddress = ip;
		this.playerName = playerName;
	}
	
	public void connectServer(){
		try{
			this.serverSocket = new Socket(ipAddress, port);
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Server Not Found");
			alert.setHeaderText("Please check IP and start again.");
			alert.showAndWait();
			Platform.exit();
		}
		
		KamisadoClientServerThread server = new KamisadoClientServerThread(KamisadoClientModel.this, serverSocket);
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
	
	public JSONObject parseJSON(String msg){
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject();
		
		try {
			Object obj = parser.parse(msg);
			json = (JSONObject) obj;
		} catch (Exception e) {
			logger.warning(e.toString());
			e.printStackTrace();
		}
		return json;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
}