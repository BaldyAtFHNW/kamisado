package kamisado_server;

import java.util.Scanner;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Kamisado_Server_Controller{
	private Logger logger = Logger.getLogger("");
	private Kamisado_Server_Model model;
	private Kamisado_Server_View view;
	
	public Kamisado_Server_Controller(Kamisado_Server_Model model, Kamisado_Server_View view){
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
	        case "move":			processMove(json, plColor);
	        	break;
	        case "end":				processEnd(json, plColor);// can only be surrendering
        	break;
	        default: 				logger.warning("Invalid Type");
		}
	}
	
	@SuppressWarnings("unchecked")
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
		json_winner.put("reason", "win");
		
		JSONObject json_loser = new JSONObject();
		json_loser.put("type", "end");
		json_loser.put("won", false);
		json_loser.put("reason", "lost");
		
		char looserCol = (winnerCol == 'W') ? 'B' : 'W';
		model.send(json_winner.toString(), winnerCol);
		model.send(json_loser.toString(), looserCol);
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
		scanner.close();
		System.exit(0);
	}
	
}