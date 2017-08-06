package kamisado_server;

import java.net.InetAddress;
import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Kamisado_Server_View {
	private Logger logger = Logger.getLogger("");
	private Stage stage;
	private Kamisado_Server_Model model;
	
	public Button startSrv;
	public Button endSrv;
	public TextArea info;

	public Kamisado_Server_View(Stage stage, Kamisado_Server_Model model) {
		this.stage = stage;
		this.model = model;
		
		BorderPane root = new BorderPane();
		HBox topHBox = new HBox();
		
		startSrv = new Button("Start Server");
		endSrv = new Button("End Server");
		Label ip = new Label("Your IP: " + model.getIP());
		ip.setPadding(new Insets(5,0,0,5));;
		
		info = new TextArea();
		info.setEditable(false);
		
		Image img = new Image(getClass().getResourceAsStream("/shortyNBaldy.png"));
		ImageView imgView = new ImageView(img);
        imgView.setFitHeight(200);
        imgView.setFitWidth(600);
		
        topHBox.getChildren().addAll(startSrv, endSrv, ip);
        root.setTop(topHBox);
        root.setCenter(info);
		root.setBottom(imgView);		
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Kamisado by ShortyNBaldy - Server");
		stage.getIcons().add(new Image("/shortyNBaldy.png"));
		stage.setWidth(605);
		stage.setResizable(false);
	}

	public void start() {
		stage.show();
	}

	public void stop() {
		stage.hide();
	}
	
	public Stage getStage() {
		return stage;
	}
	
}
