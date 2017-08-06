package kamisado_client;

import javafx.scene.Scene;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Kamisado_Client_View {
	private Logger logger = Logger.getLogger("");
	private Stage stage;
	private Kamisado_Client_Model model;
	protected Button btnClick;
	
	public Kamisado_Client_View(Stage stage, Kamisado_Client_Model model){
		this.stage = stage;
		this.model = model;
		
		//View Stuff goes here
		btnClick = new Button(); //Only an example for showing in the controller how to register for events
		
		BorderPane root = new BorderPane();
		Label one = new Label (" one ");
		Label two = new Label (" two ");
		Label three = new Label (" three ");
		Label four = new Label (" four ");
		Label five = new Label (" five ");
		root.setTop(one);
		root.setBottom(two);
		root.setLeft(three);
		root.setRight(four);
		root.setCenter(five);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(
                getClass().getResource("layouts.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle(" K A M I S A D O ");
		
	}
	
	public void firstMove(){
		//implement the first move here
		if(model.black) {
			logger.info("First Move Here! I am the Black player");
		}else {
			logger.info("First Move Here! I am the White player");
		}
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