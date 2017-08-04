package kamisado_client;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Kamisado_Client_Model {
	final private String ipAddress = "127.0.0.1";
	final private int port = 50000;
	private Logger logger = Logger.getLogger("");
	private Socket serverSocket;
	
	protected SimpleStringProperty newestMsg = new SimpleStringProperty();
	
	public boolean black;
	
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
	
//	public void processMsg(String msg) {
//		String type = new String();
//		JSONParser parser = new JSONParser();
//		
//		try {
//			Object obj = parser.parse(msg);
//			JSONObject json = (JSONObject) obj;
//			type = (String) json.get("type");
//			logger.info("Type: " + type);
//			
//			switch (type) {
//		        case "init":			processInit(json);
//		        	break;
//		        case "requestMove":		processRequestMove(json);
//		        	break;
//		        case "end":				processEnd(json);
//	        		break;
//		        case "reset":			processReset(json);
//		        	break;
//		        default: 				logger.warning("Invalid Type");
//			}
//			
//		} catch (Exception e) {
//			logger.warning(e.toString());
//			e.printStackTrace();
//		}
//	}
	

}