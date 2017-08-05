package kamisado_server;

import java.util.Scanner;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Kamisado_Server_Controller{
	private Logger logger = Logger.getLogger("");
	Kamisado_Server_Model model;
	
	public Kamisado_Server_Controller(Kamisado_Server_Model model){
		this.model = model;
		
		testOnlyWithServer();
		
		model.newestMsgPlBlack.addListener( (o, oldValue, newValue) -> processMsg(newValue, 'B'));
		model.newestMsgPlWhite.addListener( (o, oldValue, newValue) -> processMsg(newValue, 'W'));
		model.connectClients();
		
		model.initGame();
		//init the game and afterwards only react
	}
	
	private void processMsg(String msg, char plColor) {
		JSONObject json = model.parseJSON(msg);
		String type = (String) json.get("type");
		
		switch (type) {
	        case "move":			processMove(json, plColor);
	        	break;
	        case "end":				processEnd(json, plColor);// for surrender
        	break;
	        default: 				logger.warning("Invalid Type");
		}
	}
	
	private void processMove(JSONObject json, char plColor){
		String movedTwrStr = (String) json.get("towerColor");
		TowerColor movedTwr = TowerColor.valueOf(movedTwrStr);
		int newXPos = (int) json.get("xPos");
		int newYPos = (int) json.get("yPos");
		
		model.moveTower(movedTwr, newXPos, newYPos);
		FieldColor landedFieldCol = model.getFieldColor(newXPos, newYPos);
		TowerColor nextTwr = TowerColor.valueOf(plColor + landedFieldCol.toString());
		
		JSONArray possibleMoves = model.getPossibleMoves(nextTwr);
		
		JSONObject newJSON = new JSONObject();
		newJSON.put("type", "requestMove");
		newJSON.put("movedTower", movedTwr.toString());
		newJSON.put("xPos", newXPos);
		newJSON.put("yPos", newYPos);
		newJSON.put("nextTower", nextTwr.toString());
		newJSON.put("possibleMoves", possibleMoves);
		
		model.send(newJSON.toString(), plColor);
		
		//Check if somebody won
		if(newYPos == 0 || newYPos == 7) {
			plrWon(plColor);
		}
	}
	
	private void plrWon(char winnerCol) {
		JSONObject json_winner = new JSONObject();
		json_winner.put("type", "end");
		json_winner.put("won", true);
		json_winner.put("reason", "win");
		
		JSONObject json_looser = new JSONObject();
		json_looser.put("type", "end");
		json_looser.put("won", false);
		json_looser.put("reason", "lost");
		
		char looserCol = (winnerCol == 'W') ? 'B' : 'W';
		model.send(json_winner.toString(), winnerCol);
		model.send(json_looser.toString(), looserCol);
	}
	
	private void testOnlyWithServer() {
		model.initTowers();
		model.initGameBoard();		

		int x;
		int y;
		Scanner scanner = new Scanner(System.in);
		System.out.println("First Tower: ");
		String firstTower = scanner.nextLine();
		TowerColor movingTower = TowerColor.valueOf(firstTower);
		TowerColor nextTower; FieldColor landedField;
		System.out.println("Possible Moves: " + model.getPossibleMoves(TowerColor.valueOf(firstTower)));
		
		System.out.println("New xPos: ");
		x = scanner.nextInt();
		
		System.out.println("New yPos: ");
		y = scanner.nextInt();
		
		String in = "start";

		
		while(!in.equals("stop")) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.warning(e.toString());
				e.printStackTrace();
			}
			
			char color = movingTower.toString().charAt(0);
			char nextColor = movingTower.toString().charAt(0) == 'B' ? 'W' : 'B';
			
			if(y == 0 || y == 7 ) {System.out.println(color + " Player won!!"); break;}
			
			model.moveTower(movingTower, x, y);
			landedField = model.getFieldColor(x, y);
			nextTower = TowerColor.valueOf(nextColor + landedField.toString());
			
			System.out.println("Next Tower: " + nextTower);
			System.out.println("Possible Moves: " + model.getPossibleMoves(nextTower));
			
			System.out.println("New xPos: ");
			x = scanner.nextInt();
			
			System.out.println("New yPos: ");
			y = scanner.nextInt();
			
			movingTower = nextTower;			
		}
		System.exit(0);
	}
	
}