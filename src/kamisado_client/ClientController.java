package kamisado_client;

import java.util.Iterator;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;

public class ClientController {
	private Logger logger = Logger.getLogger("");
	final private ClientModel model;
	final private ClientView view;
	
	String br = System.getProperty("line.separator");
	
	public ClientController(ClientModel model, ClientView view){
		this.model = model;
		this.view = view;
		
		model.newestMsg.addListener( (o, oldValue, newValue) -> processMsg(newValue));
		model.connectServer();
		sendNameToServer();
	}
	
	@SuppressWarnings("unchecked")
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
	        case "chat":			processChatMsg(json);
        		break;
	        case "requestMove":		processRequestMove(json);
	        	break;
	        case "end":				processEnd(json);
	    		break;
	        case "leave":			processLeave();
    			break;
	        default: 				logger.warning("Invalid Type");
		}
	}
	
	private void processInit(JSONObject json) {
		model.black = (boolean) json.get("black");
		model.start = (boolean) json.get("start");
		model.opponent = (String) json.get("opponent");
		Long playerScore = (long) json.get("score");
		Long opponentScore = (long) json.get("op_score");
		
		view.initGame();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.warning(e.toString());
			e.printStackTrace();
		}
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				view.playerScore.setText(playerScore.toString());
				view.opponentScore.setText(opponentScore.toString());
				view.opponentName.setText((String) json.get("opponent"));
			}
		});
		
		view.lastMoves.appendText("You are playing against " + model.opponent + br);
		if(model.black) {
			view.lastMoves.appendText("You are in charge of the Black Towers" + br);
		}else {
			view.lastMoves.appendText("You are in charge of the White Towers" + br);
		}
		if(model.start) {
			view.firstMove();
			view.lastMoves.appendText("You have the first Move" + br);
		}
		
		view.chatBtn.setOnAction((event)->{
			model.sendChatMsg(view.chatTxt.getText());
			view.chatTxt.setText("");
		});
		
		view.chatTxt.setOnKeyPressed((event)->{
			if (event.getCode() == KeyCode.ENTER)  {
				model.sendChatMsg(view.chatTxt.getText());
				view.chatTxt.setText("");
	        }
		});
		view.getStage().setOnCloseRequest((event)->{
			model.sendLeave();
			view.stop();
		});
		view.giveUp.setOnAction((event)->{
			model.surrender();
		});
	}
	
	private void processChatMsg(JSONObject json){
		view.chatArea.appendText((String)json.get("msg") + br);
	}
	
	private void processRequestMove(JSONObject json){
		boolean opponentBlocked = (boolean) json.get("opponentBlocked");
		boolean playerBlocked = (boolean) json.get("playerBlocked");		
		
		//Display Opponent's move
		if(!opponentBlocked) { 			//Only if other player was not blocked
			String movedTower = (String) json.get("movedTower");
			Long x = (long) json.get("xPos");
			Long y = (long) json.get("yPos");
			int newXPos = x.intValue();
			int newYPos = y.intValue();
			
			if(model.black) { //this is the black client
				newXPos = model.turnUpsideDown(newXPos);
			}else {
				newYPos = model.turnUpsideDown(newYPos);
			}
			view.moveTower(movedTower, newXPos, newYPos);
		}else {System.out.println("Opponent blocked!!!");}
		
		//Display Possible Moves
		if(!playerBlocked) {				//Only if this player himself is not blocked
			String towerToMove = (String) json.get("nextTower");
			JSONArray jsonArray = (JSONArray) json.get("possibleMoves");
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while(iterator.hasNext()) {
				JSONObject possibleMove = iterator.next();
				Long possX = (long) possibleMove.get("xPos");
				Long possY = (long) possibleMove.get("yPos");
				int xPos = possX.intValue();
				int yPos = possY.intValue();
				if(model.black) { //this is the black client
					xPos = model.turnUpsideDown(xPos);
				}else {
					yPos = model.turnUpsideDown(yPos);
				}
				view.showPossibleMove(towerToMove, xPos, yPos);
			}
			view.lastMoves.appendText("Your Turn!" + br);
		}
	}
	
	private void processEnd(JSONObject json){
		boolean won = (boolean) json.get("won");
		String reason = (String) json.get("reason");
		if(won) {
			view.lastMoves.appendText("You WON!");
		}else {
			view.lastMoves.appendText("You lost..");
		}
		view.showEnd(won, reason);
	}
	
	private void processLeave() {
		view.showOpponentLeft();
	}
}
