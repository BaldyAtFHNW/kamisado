package kamisado_client;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.application.Platform;

public class KamisadoClientController {
	private Logger logger = Logger.getLogger("");
	final private KamisadoClientModel model;
	final private KamisadoClientView view;
	private boolean running = true;	
	
	public KamisadoClientController(KamisadoClientModel model, KamisadoClientView view){
		this.model = model;
		this.view = view;
		
		// Example how to register for View events
		view.btnClick.setOnAction((event) -> {
			//Do Stuff
		});
		
		view.giveUp.setOnAction((event)->{
			model.surrender();
		
	});
		view.getStage().setOnCloseRequest((event)->{
			view.stop();
			Platform.exit();
		});		
		
		model.newestMsg.addListener( (o, oldValue, newValue) -> processMsg(newValue));
		logger.info(model.ipAddress);
		model.connectServer();
		sendNameToServer();

	}
	
	public void sendNameToServer() {
		JSONObject json = new JSONObject();
		json.put("type", "introduction");
		json.put("name", model.getPlayerName());
		
		model.send(json.toString());
	}
	
	private void processMsg(String msg) {
		JSONObject json = model.parseJSON(msg);
		String type = (String) json.get("type");
		
		switch (type) {
	        case "init":			processInit(json);
	        	break;
	        case "requestMove":		processRequestMove(json);
	        	break;
	        case "end":				processEnd(json);
	    		break;
	        //case "reset":			processReset(json);     <------------ not needed anymore
	        //	break;
	        default: 				logger.warning("Invalid Type");
		}
	}
	
	private void processInit(JSONObject json) {
		model.black = (boolean) json.get("black");
		model.opponent = (String) json.get("opponent");
		if((boolean) json.get("start")) {
			view.firstMove();
		}
	}
	
	private void processRequestMove(JSONObject json){
		//Display oppononent's move
		String movedTower = (String) json.get("movedTower");
		int newXPos = (int) json.get("xPos");
		int newYPos = (int) json.get("yPos");
		if(model.black) { //this is the black client
			newXPos = model.turnUpsideDown(newXPos);
		}else {
			newYPos = model.turnUpsideDown(newYPos);
		}
		
		//view.moveTower(movedTower, newXPos, newYPos);									<--------- does not exist yet
		
		//Highlight possible moves ----- (Highlight fields and make them clickable)
		JSONArray jsonArray = (JSONArray) json.get("possibleMoves");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = jsonArray.iterator();
		
		while(iterator.hasNext()) {
			JSONObject possibleMove = iterator.next();
			int xPos = (int) possibleMove.get("xPos");
			int yPos = (int) possibleMove.get("yPos");
			if(model.black) { //this is the black client
				xPos = model.turnUpsideDown(xPos);
			}else {
				yPos = model.turnUpsideDown(yPos);
			}
			//view.highlightField(xPos, yPos);											<--------- does not exist yet
		}
	}
	
	private void processEnd(JSONObject json){
		boolean won = (boolean) json.get("won");
		String reason = (String) json.get("reason");
		
		view.showEnd(won, reason);
	}
	
}
