package kamisado_client;

import java.util.logging.Logger;
import org.json.simple.JSONObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

public class PendingForStartGUI {
	private Logger logger = Logger.getLogger("");
	private Stage stage;

	public PendingForStartGUI(Stage stage) {
		this.stage = stage;
		
		HBox root = new HBox();
		Label plsWait = new Label("Pending for 2nd Player - please wait");
		
		root.getChildren().add(plsWait);
		
		Scene scene = new Scene(root, 800, 800);
		stage.setScene(scene);
		stage.setTitle("Kamisado by ShortyNBaldy - Client");
		stage.getIcons().add(new Image("/shortyNBaldy.png"));
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