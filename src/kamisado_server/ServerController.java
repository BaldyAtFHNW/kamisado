package kamisado_server;

import java.util.Random;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javafx.application.Platform;

public class ServerController{
	private Logger logger = Logger.getLogger("");
	private ServerModel model;
	private ServerView view;
	String br = System.getProperty("line.separator");
	
	public ServerController(ServerModel model, ServerView view){
		this.model = model;
		this.view = view;
		
		model.newestMsgPlBlack.addListener( (o, oldValue, newValue) -> processMsg(newValue, 'B'));
		model.newestMsgPlWhite.addListener( (o, oldValue, newValue) -> processMsg(newValue, 'W'));
		model.newMsgGui.addListener( (o, oldValue, newValue) -> view.info.appendText(newValue + "\n"));
		view.startSrv.setOnAction((event)->{
			model.connectClients();
			view.startSrv.setDisable(true);
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
	        case "end":				processEnd(json, plColor);	// can only be surrendering
        		break;
	        case "leave":			processLeave();				//doesn't need any inputs
    			break;
	        default: 				logger.warning("Invalid Type");
		}
	}
	
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
	private void processMove(JSONObject json, char currentPlayer){
		char nextPlayer = currentPlayer == 'B' ? 'W' : 'B';
		String movedTwrStr = (String) json.get("towerColor");
		TowerColor movedTwr = TowerColor.valueOf(movedTwrStr);
		Long x = (long) json.get("xPos");
		Long y = (long) json.get("yPos");
		int newXPos = x.intValue();
		int newYPos = y.intValue();
		
		if(newYPos == 0 || newYPos == 7) {	//Somebody Won, no need to calculate next moves
			plrWon(currentPlayer);
		}else {								//Nobody won yet, go on
			String playerName = currentPlayer == 'B' ? model.namePlB : model.namePlW;
			model.newMsgGui.set(playerName + " has moved " + movedTwrStr + " to " + newXPos + " " + newYPos);
			model.moveTower(movedTwr, newXPos, newYPos);
			FieldColor landedFieldCol = model.getFieldColor(newXPos, newYPos);
			TowerColor nextTwr = TowerColor.valueOf(nextPlayer + landedFieldCol.toString());
			JSONArray possibleMoves = model.getPossibleMoves(nextTwr);
			
			JSONObject newJSON = new JSONObject();				
			if(possibleMoves.isEmpty()) {			//next player is blocked
				//Update boolean for blocked player
				if(currentPlayer == 'B') {
					model.WPlBlocked = true;
				}else {
					model.BPlBlocked = true;
				}
				
				//Update move on blocked Players Gameboard
				newJSON = new JSONObject();
				newJSON.put("type", "requestMove");
				newJSON.put("movedTower", movedTwrStr);
				newJSON.put("xPos", newXPos);
				newJSON.put("yPos", newYPos);
				newJSON.put("opponentBlocked", false);
				newJSON.put("playerBlocked", true);
				model.send(newJSON.toString(), nextPlayer);
				
				if(model.BPlBlocked && model.WPlBlocked) {     //<----- Deadlock, do not ask for another move
					deadlock();
				}else {
					//Ask same Player for another move
					newJSON = new JSONObject();
					newJSON.put("type", "requestMove");
					Integer[] blockedTwrPos = model.getTowerPos(nextTwr);
					FieldColor blockedTwrFieldCol = model.getFieldColor(blockedTwrPos[0], blockedTwrPos[1]);
					nextTwr = TowerColor.valueOf(currentPlayer + blockedTwrFieldCol.toString());
					newJSON.put("nextTower", nextTwr.toString());
					newJSON.put("possibleMoves", model.getPossibleMoves(nextTwr));
					newJSON.put("opponentBlocked", true);
					newJSON.put("playerBlocked", false);
					model.send(newJSON.toString(), currentPlayer); //This message goes back to same player
				}
			}else {	//Next player not blocked
				//Update boolean for not blocked player
				if(currentPlayer == 'B') {
					model.WPlBlocked = false;
				}else {
					model.BPlBlocked = false;
				}
				
				newJSON = new JSONObject();
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
		}
		System.out.println("Diamond Player blocked: " + model.BPlBlocked + " " + "Circle Player blocked: " + model.WPlBlocked);
	}
	
	@SuppressWarnings("unchecked")
	private void deadlock() {
		JSONObject json_deadlock = new JSONObject();
		json_deadlock.put("type", "end");
		json_deadlock.put("won", false);
		json_deadlock.put("reason", "deadlock");
		
		model.send(json_deadlock.toString(), 'B');
		model.send(json_deadlock.toString(), 'W');
		
		//Wait for 5seconds before restart
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Random random = new Random(); //from: https://stackoverflow.com/questions/8878015/return-true-or-false-randomly
		char firstMove;
		if(random.nextBoolean()) {
			firstMove = 'W';
		}else {
			firstMove = 'B';
		}
		model.restartGame(firstMove); //Start game by sending initialization messages to both clients
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
		
		char winnerCol;
		if(surrenderer == 'B') { 	//Black Player won
			winnerCol = 'W';
			model.scoreW ++;
		}else {						//White Player won
			winnerCol = 'B';
			model.scoreB ++;
		}
		
		model.send(json_winner.toString(), winnerCol);
		model.send(json_loser.toString(), surrenderer);
		
		//Wait for 5seconds before restart
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.initGame(surrenderer);
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
		if(winnerCol == 'B') { 	//Black Player won
			looserCol = 'W';
			model.scoreB ++;
		}else {					//White Player won
			looserCol = 'B';
			model.scoreW ++;
		}
		
		model.send(json_winner.toString(), winnerCol);
		model.send(json_loser.toString(), looserCol);
		
		//Wait for 5seconds before restart
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.restartGame(looserCol);
	}
	
	private void processLeave() {
		//Update GUI
		model.newMsgGui.set("One of the Players left and the game got aborted." + br + "The server application will close in 10 secs.");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Stop View
		view.stop();
	}
	
}