package kamisado_client;

import java.io.OutputStreamWriter;
import java.net.Socket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;

/**
 * 
 * 	@author Simon Bieri
 * 	@date 2017-07-21

 *	This class mainly handles the communication with the server (sending messages)
 *	But also does some number crunching 
 *
 */
public class ClientModel {
	final public String ipAddress;
	final private int port = 50000;
	protected Socket serverSocket;
	
	protected SimpleStringProperty newestMsg = new SimpleStringProperty();
	protected SimpleStringProperty latestMove = new SimpleStringProperty();

	final public String playerName;
	public String opponentName;
	public boolean black;
	
	String br = System.getProperty("line.separator");
	
	protected boolean running = true;
	
	public ClientModel(String ip, String playerName) {
		this.ipAddress = ip;
		this.playerName = playerName;
	}
	
	/**
	 * Connects to the server
	 * @return void
	 */
	protected void connectServer(){
		try{
			this.serverSocket = new Socket(ipAddress, port);
		}catch(Exception e){
			e.printStackTrace();
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Server Not Found");
			alert.setHeaderText("Please check IP and start again.");
			alert.showAndWait();
			Platform.exit();
		}
		
		ClientServerThread server = new ClientServerThread(ClientModel.this, serverSocket);
		new Thread(server).start();	
	}

	/**
	 * Sends a string to the server (should always be a jsonObject)
	 * @param String message
	 * @return void
	 */
	protected void send(String msg){
		OutputStreamWriter out;
		try{
			out = new OutputStreamWriter(this.serverSocket.getOutputStream());
			out.write(msg + br);
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Builds the jsonObject of type "move" to the server
	 * @param String towerColor
	 * @param int xPos
	 * @param int yPos
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	protected void sendMove(String towerColor, int xPos, int yPos) {
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
	
	/**
	 * Builds the jsonObject of type "chat" to the server
	 * @param String chatMessage
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	protected void sendChatMsg(String msg) {
		JSONObject json = new JSONObject();
		json.put("type", "chat");
		json.put("msg", msg);
		
		this.send(json.toString());
	}
	
	/**
	 * Builds the jsonObject of type "leave" to the server
	 * This communication is being sent before a client is being closed
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	protected void sendLeave() {		
		JSONObject json = new JSONObject();
		json.put("type", "leave");
		this.send(json.toString());
	}
	
	/**
	 * Builds the jsonObject of type "end" to the server
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	protected void surrender() {		
		JSONObject json = new JSONObject();
		json.put("type", "end");
		json.put("reason", "surrender");
		this.send(json.toString());
	}
	
	/**
	 * Parses a jsonString with the simple json library
	 * @param String jsonObject.toString()
	 * @return jsonObject
	 */
	protected JSONObject parseJSON(String msg){
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject();
		
		try {
			Object obj = parser.parse(msg);
			json = (JSONObject) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * returns the players name
	 * @return String playerName
	 */
	protected String getPlayerName() {
		return playerName;
	}
	
	
	/**
	 * Replaces numbers from 0 to 7
	 * Needed for switching from XY koordinates from the server to the gridpane koordinates
	 * @param int number
	 * @return int number
	 */
	protected int turnUpsideDown(int number) {
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