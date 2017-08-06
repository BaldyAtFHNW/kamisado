package kamisado_server;

import javafx.application.Application;
import javafx.stage.Stage;

public class Kamisado_Server_Main extends Application{
	private Kamisado_Server_Model model;
	private Kamisado_Server_View view;
	private Kamisado_Server_Controller controller;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage){
		this.model = new Kamisado_Server_Model();
		this.view = new Kamisado_Server_View(primaryStage, model);
		this.controller = new Kamisado_Server_Controller(model, view);
		this.view.start();
	}
	
	@Override
	public void stop() {
		if(view != null) {
			view.stop();
		}
	}
	
}