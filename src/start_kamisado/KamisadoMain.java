package start_kamisado;

import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import kamisado_client.ClientController;
import kamisado_client.ClientModel;
import kamisado_client.ClientView;
import kamisado_client.ConnectGUI;
import kamisado_server.ServerController;
import kamisado_server.ServerModel;
import kamisado_server.ServerView;

public class KamisadoMain extends Application{
	private ServerModel srvModel;
	private ServerView srvView;
	private ServerController srvController;
	
	private ClientModel clientModel;
	private ClientView clientView;
	private ClientController clientController;
	
	private ConnectGUI connectGUI;
	
	String br = System.getProperty("line.separator");

	//Source: http://code.makery.ch/blog/javafx-dialogs-official/
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image("/shortyNBaldy.png"));
		alert.setTitle("Kamisado by ShortyNBaldy - FHWN Basel");
		alert.setHeaderText("What would you like to start?");
		alert.setGraphic(null);

		ButtonType startSrv = new ButtonType("Server");
		ButtonType startClient = new ButtonType("Client");
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(startSrv, startClient, cancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == startSrv){
			srvModel = new ServerModel();
			srvView = new ServerView(primaryStage, srvModel);
			srvController = new ServerController(srvModel, srvView);
			srvView.start();
		} else if (result.get() == startClient) {
			connectGUI = new ConnectGUI(primaryStage);
			connectGUI.connect.setOnAction((event) -> {startKamisadoClient(primaryStage);connectGUI.connect.setDisable(true);});
			connectGUI.txtName.setOnKeyPressed((event)->{
				if (event.getCode() == KeyCode.ENTER)  {
					startKamisadoClient(primaryStage);
					connectGUI.connect.setDisable(true);
		        }
			});
			connectGUI.txtIP.setOnKeyPressed((event)->{
				if (event.getCode() == KeyCode.ENTER)  {
					startKamisadoClient(primaryStage);
					connectGUI.connect.setDisable(true);
		        }
			});

		}else {
			Platform.exit();
		}
	}
	
	public void startKamisadoClient(Stage clientStage) {
		connectGUI.connected.setText("Pending for start");
		clientModel = new ClientModel(connectGUI.txtIP.getText(), connectGUI.txtName.getText());
		clientView = new ClientView(clientStage, clientModel);
		clientController = new ClientController(clientModel, clientView);
		clientView.start();  //fix, otherwise not JavaFX thread
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
