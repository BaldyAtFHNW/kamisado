package kamisado_client;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUI_test extends Application{
	
	private KamisadoClientModel clientModel;
	private KamisadoClientView clientView;
	private KamisadoClientController clientController;

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage){
		clientModel = new KamisadoClientModel("127.0.0.1", "Simon");
		clientModel.black = false;
		clientView = new KamisadoClientView(primaryStage, clientModel);
		//clientController = new KamisadoClientController(clientModel, clientView);
		clientView.start();
		
		
	}

}
