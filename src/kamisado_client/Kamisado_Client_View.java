package kamisado_client;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Kamisado_Client_View {
	private Stage stage;
	private Kamisado_Client_Model model;
	protected Button btnClick;
	
	protected Kamisado_Client_View(Stage stage, Kamisado_Client_Model model){
		this.stage = stage;
		this.model = model;
		
		//View Stuff goes here
		btnClick = new Button(); //Only an example for showing in the controller how to register for events
		
		
	}
	
	public void start(){
		stage.show();
	}
	
	public void stop(){
		stage.hide();
	}
	
	public Stage getStage(){
		return stage;
	}
	
}
