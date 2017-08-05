package kamisado_server;

import java.util.Scanner;
import java.util.logging.Logger;

import org.json.simple.JSONArray;

public class Kamisado_Server_Controller{
	private Logger logger = Logger.getLogger("");
	Kamisado_Server_Model model;
	
	public Kamisado_Server_Controller(Kamisado_Server_Model model){
		this.model = model;
		
		testOnlyWithServer();
		
		model.newestMsgPlBlack.addListener( (o, oldValue, newValue) -> processMsgPlBlack(newValue));
		model.newestMsgPlWhite.addListener( (o, oldValue, newValue) -> processMsgPlWhite(newValue));
		model.connectClients();
		
		model.initGame();
		//init the game and afterwards only react
	}
	
	private void processMsgPlBlack(String msg){
		logger.info(msg);
	}
	
	private void processMsgPlWhite(String msg){
		logger.info(msg);
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
				// TODO Auto-generated catch block
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