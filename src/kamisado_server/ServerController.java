package kamisado_server;

import java.util.Scanner;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.application.Platform;

public class ServerController{
	private Logger logger = Logger.getLogger("");
	private ServerModel model;
	private ServerView view;
	
	public ServerController(ServerModel model, ServerView view){
		this.model = model;
		this.view = view;
		
		//testOnlyWithServer();
		
		model.newestMsgPlBlack.addListener( (o, oldValue, newValue) -> processMsg(newValue, 'B'));
		model.newestMsgPlWhite.addListener( (o, oldValue, newValue) -> processMsg(newValue, 'W'));
		model.newMsgGui.addListener( (o, oldValue, newValue) -> view.info.appendText(newValue + "\n"));
		view.startSrv.setOnAction((event)->{
			model.connectClients();
		});
		view.endSrv.setOnAction((event)->{
			view.stop();
		});
	
	}
	
	private void processMsg(String msg, char plColor) {
		JSONObject json = model.parseJSON(msg);
		String type = (String) json.get("type");
		
		switch (type) {
			case "introduction":	processIntroduction(json, plColor);
				break;
			case "chat":			processChatMsg(json, plColor);
				break;	
	        case "move":			processMove(json, plColor);
	        	break;
	        case "end":				processEnd(json, plColor);// can only be surrendering
        		break;
	        default: 				logger.warning("Invalid Type");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processIntroduction(JSONObject json, char plColor){
		String name = (String) json.get("name");
		if(plColor == 'B') {
			model.setNamePlB(name);
			view.info.appendText("Black Player's name: " + name +"\n");
		}else {
			model.setNamePlW(name);
			view.info.appendText("White Player's name: " + name + "\n");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processChatMsg(JSONObject json, char plColor){
		String msg = (String) json.get("msg");
		msg = plColor == 'B' ? model.namePlB + ": " + msg : model.namePlW + ": " + msg ;
		
		JSONObject newJSON = new JSONObject();
		newJSON.put("type", "chat");
		newJSON.put("msg", msg);
		
		model.send(newJSON.toString(), 'B');
		model.send(newJSON.toString(), 'W');
	}
	
	@SuppressWarnings("unchecked")
	private void processMove(JSONObject json, char plColor){
		char nextPlayer = plColor == 'B' ? 'W' : 'B';
		boolean wasBlocked;
		String movedTwrStr = (String) json.get("towerColor");
		TowerColor movedTwr = TowerColor.valueOf(movedTwrStr);
		Long x = (long) json.get("xPos");
		Long y = (long) json.get("yPos");
		int newXPos = x.intValue();
		int newYPos = y.intValue();
		
		String playerName = plColor == 'B' ? model.namePlB : model.namePlW;
		model.newMsgGui.set(playerName + " has moved " + movedTwrStr + " to " + newXPos + " " + newYPos);
		
		model.moveTower(movedTwr, newXPos, newYPos);
		FieldColor landedFieldCol = model.getFieldColor(newXPos, newYPos);
		TowerColor nextTwr = TowerColor.valueOf(nextPlayer + landedFieldCol.toString());
		
		JSONArray possibleMoves = model.getPossibleMoves(nextTwr);
		
		if(possibleMoves.isEmpty()) {	//Player is blocked
//			//Inform Players
//			String nameBlockedPl = plColor == 'B' ? model.namePlB : model.namePlW;
//			JSONObject newJSON = new JSONObject();
//			newJSON.put("type", "chat");
//			newJSON.put("msg", nameBlockedPl + " is blocked!");
//			//model.send(newJSON.toString(), 'B');							<------------ not implemented on client side, yet
//			//model.send(newJSON.toString(), 'W');							<------------ not implemented on client side, yet
			
			//Update move on blocked Players Gameboard
			JSONObject newJSON = new JSONObject();
			newJSON.put("type", "requestMove");
			newJSON.put("movedTower", movedTwrStr);
			newJSON.put("xPos", newXPos);
			newJSON.put("yPos", newYPos);
			newJSON.put("opponentBlocked", false);
			newJSON.put("playerBlocked", true);
			model.send(newJSON.toString(), nextPlayer);
			
			
			//Ask same Player for another move
			newJSON = new JSONObject();
			newJSON.put("type", "requestMove");
			Integer[] blockedTwrPos = model.getTowerPos(nextTwr);
			FieldColor blockedTwrFieldCol = model.getFieldColor(blockedTwrPos[0], blockedTwrPos[1]);
			nextTwr = TowerColor.valueOf(plColor + blockedTwrFieldCol.toString());
			newJSON.put("nextTower", nextTwr.toString());
			newJSON.put("possibleMoves", model.getPossibleMoves(nextTwr));
			newJSON.put("opponentBlocked", true);
			newJSON.put("playerBlocked", false);
			model.send(newJSON.toString(), plColor); //This message goes back to same player
			
		}else {							//Nobody blocked
			JSONObject newJSON = new JSONObject();
			newJSON.put("type", "requestMove");
			newJSON.put("movedTower", movedTwrStr);
			newJSON.put("xPos", newXPos);
			newJSON.put("yPos", newYPos);
			newJSON.put("nextTower", nextTwr.toString());
			newJSON.put("possibleMoves", possibleMoves);
			newJSON.put("opponentBlocked", false);
			newJSON.put("playerBlocked", false);
			model.send(newJSON.toString(), nextPlayer);
		}
		
		//Check if somebody won
		if(newYPos == 0 || newYPos == 7) {
			plrWon(plColor);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processEnd(JSONObject json, char surrenderer) {
		JSONObject json_winner = new JSONObject();
		json_winner.put("type", "end");
		json_winner.put("won", true);
		json_winner.put("reason", "surrender");
		
		JSONObject json_loser = new JSONObject();
		json_loser.put("type", "end");
		json_loser.put("won", false);
		json_loser.put("reason", "surrender");
		
		char winnerCol = (surrenderer == 'W') ? 'B' : 'W';
		model.send(json_winner.toString(), winnerCol);
		model.send(json_loser.toString(), surrenderer);
	}
	
	@SuppressWarnings("unchecked")
	private void plrWon(char winnerCol) {
		JSONObject json_winner = new JSONObject();
		json_winner.put("type", "end");
		json_winner.put("won", true);
		json_winner.put("reason", "ended");
		
		JSONObject json_loser = new JSONObject();
		json_loser.put("type", "end");
		json_loser.put("won", false);
		json_loser.put("reason", "ended");
		
		char looserCol = (winnerCol == 'W') ? 'B' : 'W';
		model.send(json_winner.toString(), winnerCol);
		model.send(json_loser.toString(), looserCol);
		
		Platform.exit();
	}
	
}