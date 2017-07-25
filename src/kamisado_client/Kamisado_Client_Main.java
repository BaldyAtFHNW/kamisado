package kamisado_client;

import javafx.*;
import javafx.application.Application;

public class Kamisado_Client_Main extends Application {
	private Kamisado_Client_View view;
	private Kamisado_Client_Controller controller;
	
	
	public static void main(String[] args) {
		launch(args);

	}
	
	@Override
	public void start(Stage primaryStage){
		view = new Kamisado_Client_View(primaryStage);
		controller = new Kamisado_Client_Congroller(view);
		view.start();
	}

	@Override
	public void stop(){
		if (view != null){
			view.stop();
		}
	}
	
}




