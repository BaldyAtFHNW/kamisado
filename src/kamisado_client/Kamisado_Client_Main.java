package kamisado_client;

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
	}

	@Override
	public void stop(){
		if (view != null){
			view.stop();
		}
	}
	
}




