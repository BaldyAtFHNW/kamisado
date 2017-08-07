package start_kamisado;

import java.util.Optional;

import com.sun.media.jfxmedia.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import kamisado_client.KamisadoClientController;
import kamisado_client.KamisadoClientModel;
import kamisado_client.KamisadoClientView;
import kamisado_client.KamisadoConnectGUI;
import kamisado_server.KamisadoServerController;
import kamisado_server.KamisadoServerModel;
import kamisado_server.KamisadoServerView;

public class KamisadoMain extends Application{
	private KamisadoServerModel srvModel;
	private KamisadoServerView srvView;
	private KamisadoServerController srvController;
	
	private KamisadoConnectGUI connectGUI;
	private KamisadoClientModel clientModel;
	private KamisadoClientView clientView;
	private KamisadoClientController clientController;

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
			srvModel = new KamisadoServerModel();
			srvView = new KamisadoServerView(primaryStage, srvModel);
			srvController = new KamisadoServerController(srvModel, srvView);
			srvView.start();
		} else if (result.get() == startClient) {
			connectGUI = new KamisadoConnectGUI(primaryStage);
			connectGUI.connect.setOnAction((event) -> {
				primaryStage.close();
				Stage clientStage = new Stage();
				clientModel = new KamisadoClientModel(connectGUI.txtIP.getText(), connectGUI.txtName.getText());
				clientView = new KamisadoClientView(clientStage, clientModel);
				clientController = new KamisadoClientController(clientModel, clientView);
				clientView.start();
			});
		}else {
			Platform.exit();
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
	
//	private void getIPNNameNStartClient(Stage stage) {
//		BorderPane root = new BorderPane();
//		GridPane grid = new GridPane();
//		
//		Image img = new Image(getClass().getResourceAsStream("/shortyNBaldy.png"));
//		ImageView imgView = new ImageView(img);
//        imgView.setFitHeight(200);
//        imgView.setFitWidth(600);
//		
//		Label lblName = new Label("Your Name: ");
//		Label lblIP = new Label("Server's IP: ");
//		
//		TextField txtName = new TextField();
//		TextField txtIP = new TextField();
//		
//		Button connect = new Button("Connect");
//		
//		grid.add(lblName, 0, 0);
//		grid.add(lblIP, 0, 1);
//		grid.add(txtName, 1, 0);
//		grid.add(txtIP, 1, 1);
//		grid.add(connect, 2, 1);
//		
//		grid.setHgap(10);
//		grid.setVgap(10);
//		grid.setPadding(new Insets(20,10,10,150));
//		
//		root.setCenter(grid);
//		root.setBottom(imgView);
//		
//		Scene scene = new Scene(root);
//		
//		stage.setScene(scene);
//		stage.setTitle("Kamisado by ShortyNBaldy - Client");
//		stage.getIcons().add(new Image("/shortyNBaldy.png"));
//		stage.setWidth(605);
//		stage.setResizable(false);
//		stage.show();
//		
//		connect.setOnAction((event) -> {
//			stage.close();
//			Stage clientStage = new Stage();
//			
//			clientModel = new KamisadoClientModel(txtIP.getText(), txtName.getText());
//			clientView = new KamisadoClientView(clientStage, clientModel);
//			clientController = new KamisadoClientController(clientModel, clientView);
//			clientView.start();
//		});
//	}
	
}
