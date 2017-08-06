package kamisado_client;

import javafx.scene.Scene;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Kamisado_Client_View {
	private Logger logger = Logger.getLogger("");
	private Stage stage;
	private Kamisado_Client_Model model;
	protected Button btnClick;
	
	protected Kamisado_Client_View(Stage stage, Kamisado_Client_Model model){
		this.stage = stage;
		this.model = model;
		
		//View Stuff goes here
		btnClick = new Button(); //Only an example for showing in the controller how to register for events
		
		BorderPane root = new BorderPane();
		GridPane gameBoard = new GridPane(); // Kamisado game board
		root.setCenter(gameBoard);
		
		HBox upperScreen = new HBox(); // upper part of the game screen for buttons
		root.setTop(upperScreen); 
		Button resetGame = new Button("ResetGame");
		Button giveUp = new Button("Give Up");
		ComboBox<String> language = new ComboBox<String>(); // drop-down menu to change language in the game
		language.getItems().addAll("DE", "EN");
		Label moveWait = new Label("Move / Wait");
		upperScreen.getChildren().addAll(resetGame, giveUp, moveWait, language);
		
		for (int row = 0; row < 8; row++){
			for (int col = 0; col < 8; col++){
				Label lblTemp = new Label(row + " , " + col);
				gameBoard.add(lblTemp, col, row);
			}
		}
		Label lblBottom = new Label("Bottom");
		root.setBottom(lblBottom);
		Label lblLeft = new Label("Left");
		root.setLeft(lblLeft);
		Label lblRight = new Label("Right");
		root.setRight(lblRight);

		
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