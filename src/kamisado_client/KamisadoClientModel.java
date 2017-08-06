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
	
	@SuppressWarnings("unchecked")
	public void sendMove(String towerColor, int xPos, int yPos) {
		if(this.black) { //this is the black client
			xPos = turnUpsideDown(xPos);
		}else {
			yPos = turnUpsideDown(yPos);
		}
		
		JSONObject json = new JSONObject();
		json.put("type", "move");
		json.put("towerColor", towerColor);
		json.put("xPos", xPos);
		json.put("yPos", yPos);
		
		this.send(json.toString());
	}
	
	@SuppressWarnings("unchecked")
	public void surrender() {		
		JSONObject json = new JSONObject();
		json.put("type", "end");
		this.send(json.toString());
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
	
	//Changing number values for applying coordinates onto the grid
	public int turnUpsideDown(int number) {
		switch (number) {
		case 0 : number = 7;
			break;
		case 1 : number = 6;
			break;
		case 2 : number = 5;
			break;
		case 3 : number = 4;
			break;
		case 4 : number = 3;
			break;
		case 5 : number = 2;
			break;
		case 6 : number = 1;
			break;
		case 7 : number = 0;
			break;
		default : number = -1;
			break;
		}
	return number;
	}
	
}