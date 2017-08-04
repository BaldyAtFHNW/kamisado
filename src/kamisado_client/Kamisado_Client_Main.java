package kamisado_client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.application.Application;
import javafx.stage.Stage;

public class Kamisado_Client_Main extends Application {
	private Kamisado_Client_Model model;
	private Kamisado_Client_View view;
	private Kamisado_Client_Controller controller;
	
	
	public static void main(String[] args) {
		launch(args);

	}
	
	@Override
	public void start(Stage primaryStage){
		model = new Kamisado_Client_Model();
		view = new Kamisado_Client_View(primaryStage, model);
		controller = new Kamisado_Client_Controller(model, view);
		view.start();
		
		/*
		JSONDummie test = new JSONDummie();
		test.simulateRequestMove();
		JSONObject obj = test.getJSONObject();
		
		System.out.println("Type: " 		+ obj.get("type"));
		System.out.println("MovedTower: " 	+ obj.get("movedTower"));
		System.out.println("xPos: " 		+ obj.get("xPos"));
		System.out.println("yPos: " 		+ obj.get("yPos"));
		System.out.println("nextTower: " 	+ obj.get("nextTower"));
		
		System.out.println("Possible Moves: ");
		JSONArray possibleMoves = (JSONArray) obj.get("possibleMoves");
		Iterator<JSONObject> iterator = possibleMoves.iterator();
		
		while(iterator.hasNext()){
			JSONObject possibleMove = new JSONObject(iterator.next());
			System.out.println("xPos: " + possibleMove.get("xPos") + " yPos: " + possibleMove.get("yPos"));
		}
		*/

	}

	@Override
	public void stop(){
		if (view != null){
			view.stop();
		}
	}
	
}




