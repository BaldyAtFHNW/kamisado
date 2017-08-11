package kamisado_client;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ConnectGUI {
	public Button connectBtn;
	public TextField txtName;
	public TextField txtIP;
	public Label connected;
	
	public ConnectGUI(Stage stage) {
		BorderPane root = new BorderPane();
		GridPane grid = new GridPane();
		
		Image img = new Image(getClass().getResourceAsStream("/shortyNBaldy.png"));
		ImageView imgView = new ImageView(img);
        imgView.setFitHeight(200);
        imgView.setFitWidth(600);
        
		connectBtn = new Button("Connect");
		
		Label lblName = new Label("Your Name: ");
		Label lblIP = new Label("Server's IP: ");
		
		txtName = new TextField("Your Name");
		txtIP = new TextField("127.0.0.1");
		
		connected = new Label("");
		connected.setTextFill(Color.GREEN);
		
		grid.add(lblName, 0, 0);
		grid.add(lblIP, 0, 1);
		grid.add(txtName, 1, 0);
		grid.add(txtIP, 1, 1);
		grid.add(connectBtn, 2, 1);
		grid.add(connected, 3, 1);
		
		grid.setHgap(10);
		grid.setVgap(15);
		grid.setPadding(new Insets(20,10,10,150));
		
		root.setCenter(grid);
		root.setBottom(imgView);
		
		Scene scene = new Scene(root);
		
		stage.setScene(scene);
		stage.setTitle("Kamisado by ShortyNBaldy - Client");
		stage.getIcons().add(new Image("/shortyNBaldy.png"));
		stage.setWidth(605);
		stage.setResizable(false);
		stage.show();
	}
}
