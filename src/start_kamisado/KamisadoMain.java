package start_kamisado;

import java.util.Optional;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import kamisado_client.Kamisado_Client_Controller;
import kamisado_client.Kamisado_Client_Model;
import kamisado_client.Kamisado_Client_View;
import kamisado_server.Kamisado_Server_Controller;
import kamisado_server.Kamisado_Server_Model;
import kamisado_server.Kamisado_Server_View;

public class KamisadoMain extends Application{
	private Kamisado_Server_Model srvModel;
	private Kamisado_Server_View srvView;
	private Kamisado_Server_Controller srvController;
	
	private Kamisado_Client_Model clientModel;
	private Kamisado_Client_View clientView;
	private Kamisado_Client_Controller clientController;

	//Copied from: http://code.makery.ch/blog/javafx-dialogs-official/
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image("/shortyNBaldy.png"));
		alert.setTitle("Kamisado by ShortyNBaldy - Software Engineering");
		alert.setHeaderText("What would you like to start?");

		ButtonType startSrv = new ButtonType("Server");
		ButtonType buttonTypeTwo = new ButtonType("Client");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(startSrv, buttonTypeTwo, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == startSrv){
			srvModel = new Kamisado_Server_Model();
			srvView = new Kamisado_Server_View(primaryStage, srvModel);
			srvController = new Kamisado_Server_Controller(srvModel, srvView);
			srvView.start();
		} else if (result.get() == buttonTypeTwo) {
			clientModel = new Kamisado_Client_Model();
			clientView = new Kamisado_Client_View(primaryStage, clientModel);
			clientController = new Kamisado_Client_Controller(clientModel, clientView);
			clientView.start();
		}else {
			System.out.println("One");
		}
	}
	
	@Override
	public void stop() {
		if(srvView != null) {
			srvView.stop();
		}
		if(clientView != null) {
			clientView.stop();
		}
	}
	
}
