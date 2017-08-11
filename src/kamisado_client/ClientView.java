package kamisado_client;

import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class ClientView {
	private Stage stage;
	private ClientModel model;
	GridPane gameBoard;

	public TextArea lastMoves;
	public TextArea chatArea;
	public TextField chatTxt;
	public Button chatBtn;
	
	public Label playerName;
	public Label opponentName;
	public Label playerScore;
	public Label opponentScore;
	public Button giveUpBtn;

	final int FIELD_SIZE = 70;
	final int STROKE_WIDTH = 10;
	final double HIGHLIGHT_WIDTH = 1.5;
	final double SCALEDOWNCIRCLES = 0.35;
	final double SCALEDOWNDIAMONDS = 0.5;

	String br = System.getProperty("line.separator");
	
	public ClientView(Stage stage, ClientModel model) {
		this.stage = stage;
		this.model = model;
	}
	
	public void initGame() {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				double txtAreaWidth = 380;
				
				HBox root = new HBox();
				VBox right = new VBox();
				gameBoard = new GridPane();
				HBox chat = new HBox();
				HBox topRight = new HBox();
				
				gameBoard.setStyle("-fx-background-color: black");
				gameBoard.setHgap(2);
				gameBoard.setVgap(2);
				
				GridPane scoreBoard = new GridPane();
				Label scores = new Label("Scores");
				playerName = new Label("You");
				opponentName = new Label("");
				playerScore = new Label("");
				opponentScore = new Label("");
				giveUpBtn = new Button("Give Up");
				
				scoreBoard.add(scores, 0, 0);
				scoreBoard.add(playerName, 0, 1);
				scoreBoard.add(opponentName, 0, 2);
				scoreBoard.add(playerScore, 1, 1);
				scoreBoard.add(opponentScore, 1, 2);
				
				scoreBoard.setHgap(5);
				scoreBoard.setVgap(5);
				scoreBoard.setPadding(new Insets(10,10,10,5));
				
				lastMoves = new TextArea();
				lastMoves.setEditable(false);
				lastMoves.setMaxSize(txtAreaWidth, 120);
				lastMoves.setMinSize(txtAreaWidth, 120);
				
				chatArea = new TextArea();
				chatArea.setEditable(false);
				chatArea.setMaxSize(txtAreaWidth, 347);
				chatArea.setMinSize(txtAreaWidth, 347);
				
				chatTxt = new TextField();
				chatTxt.setMaxWidth(300);
				chatTxt.setMinWidth(300);
				
				chatBtn = new Button("Send");
				chatBtn.setMaxWidth(80);
				chatBtn.setMinWidth(80);
				
				giveUpBtn.setTranslateX(220); 		//from: https://stackoverflow.com/questions/30641187/position-javafx-button-in-a-specific-location
				giveUpBtn.setTranslateY(45);
				
				topRight.getChildren().addAll(scoreBoard, giveUpBtn);
				chat.getChildren().addAll(chatTxt, chatBtn);
				right.getChildren().addAll(topRight, lastMoves, chatArea, chat);
				root.getChildren().addAll(gameBoard, right);
				
				createFields();
				createTowers();

				//Scene scene = new Scene(root, 980, 574);
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.setWidth(961);
				stage.setHeight(603);
				stage.setTitle("Kamisado by ShortyNBaldy - Client");
				stage.getIcons().add(new Image("/shortyNBaldy.png"));
			}
		});

	}
	
	public void restart() {
		hideAllTowers();
		createTowers();
	}

	public void moveTower(String towerToMove, int col, int row) {
			Platform.runLater(new Runnable(){
				@Override
				public void run() {					
					Node nodeToMove = null;		//Has to be initialized here.. node WILL be filled; no need to worry about a nullPointerException
					for (Node node : gameBoard.getChildren()) {
						if(node.getId() != null) {
							if(node.getId().equals(towerToMove)) {
								nodeToMove = node;
							}
						}
					}
					gameBoard.getChildren().remove(nodeToMove);
					gameBoard.add(nodeToMove, col, row);
					lastMoves.appendText(nodeToMove.getId() + " has been moved to: " + col + " " + row + br);
				}
			});
	}

	public void showPossibleMove(String towerToMove, int col, int row) {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {		
				if(model.black) {			//If black player, create rectangles
					Rectangle possTower = new Rectangle();
					possTower.setWidth(FIELD_SIZE * SCALEDOWNDIAMONDS);
					possTower.setHeight(FIELD_SIZE * SCALEDOWNDIAMONDS);
					possTower.getTransforms().add(new Rotate(45,possTower.getWidth()/2,(possTower.getHeight()/2)));
					possTower.setFill(Color.TRANSPARENT);
					possTower.setStroke(Color.rgb(200, 200, 200, 0.9));
					possTower.setId("imaginary");
					possTower.setStrokeWidth(STROKE_WIDTH);
					GridPane.setHalignment(possTower, HPos.CENTER);
					gameBoard.add(possTower, col, row);
					possTower.setOnMouseClicked((event) -> {
						hideAllMoves();
						moveTower(towerToMove, col, row);
						model.sendMove(towerToMove, col, row);
					});
				}else {						//If white player, create circles
					Circle possTower = new Circle(FIELD_SIZE * SCALEDOWNCIRCLES);
					possTower.setFill(Color.TRANSPARENT);
					possTower.setStroke(Color.rgb(200, 200, 200, 0.9));
					possTower.setId("imaginary");
					possTower.setStrokeWidth(STROKE_WIDTH);
					GridPane.setHalignment(possTower, HPos.CENTER);
					gameBoard.add(possTower, col, row);
					possTower.setOnMouseClicked((event) -> {
						hideAllMoves();
						moveTower(towerToMove, col, row);
						model.sendMove(towerToMove, col, row);
					});
				}
			}
		});
	}
	
	public void hideAllMoves() {
		for (Node node : gameBoard.getChildren()) {
			if(node.getId() != null) {
				if(node.getId().equals("imaginary")) {
					Platform.runLater(new Runnable(){
						@Override
						public void run() {
							gameBoard.getChildren().remove(node);
							}
						});
				}
			}
		}
	}
	
	public void hideAllTowers() {
		for (Node node : gameBoard.getChildren()) {
			if(node.getId() != null) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						gameBoard.getChildren().remove(node);
						}
					});
			}
		}
	}

	public void firstMove() {
		if (model.black) {
			activateTowers('B');
		} else {
			activateTowers('W');
		}
	}

	public void activateTowers(char playerCol) {
		for (Node node : gameBoard.getChildren()) {
			if(node.getId() != null) {
				if(node.getId().charAt(0) == playerCol) {
					String towerID = node.getId();
					int col = GridPane.getColumnIndex(node);
					int row = GridPane.getRowIndex(node);
					node.setOnMouseEntered((event)->{
						activateFirstMoveFields(towerID, col, row);
					});
					node.setOnMouseExited((event)->{
						hideAllMoves();
					});
					node.setOnMouseClicked((e)->{
						deActivateTowers(playerCol);
					});
				}
			}
		}
	}
	
	public void deActivateTowers(char playerCol) {
		for (Node node : gameBoard.getChildren()) {
			if(node.getId() != null) {
				if(node.getId().charAt(0) == playerCol) {
					node.setOnMouseEntered(null);
					node.setOnMouseExited(null);
				}
			}
		}
	}
	
	public void activateFirstMoveFields(String tower, final int towerCol, final int towerRow) {
		int col; int row;
		
		col = towerCol;
		row = towerRow;
		//activate left diagonal
		while(col >= 0  && row > 0 && col <= 7 && row <= 7) {
			col--; row--;
			if(col >= 0  && row > 0 && col <= 7 && row <= 7) {
				this.showPossibleMove(tower, col, row);
			}
		}
		
		col = towerCol;
		row = towerRow;
		//activate right diagonal
		while(col >= 0  && row > 0 && col <= 7 && row <= 7) {
			col++; row--;
			if(col >= 0  && row > 0 && col <= 7 && row <= 7) {
				this.showPossibleMove(tower, col, row);
			}
		}
		
		col = towerCol;
		row = towerRow;
		//activate horizontal
		while(col >= 0  && row > 0 && col <= 7 && row <= 7) {
			row--;
			if(col >= 0  && row > 0 && col <= 7 && row <= 7) {
				this.showPossibleMove(tower, col, row);
			}
		}
		
	}
	
	public void showEnd(boolean won, String reason) {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Kamisado by ShortyNBaldy - Client");
				alert.setHeaderText("");
				if(won) {
					ImageView winner = new ImageView(new Image (getClass().getResourceAsStream("minions.jpg")));
					alert.setGraphic(winner);
					if(reason.equals("surrender")) {
						alert.setContentText("You WON! Your opponent gave up.");
					}else {
						alert.setContentText("You WON! You reached the other side!");
					}
				}else {
					ImageView loser = new ImageView(new Image (getClass().getResourceAsStream("sad_minion.jpg")));
					alert.setGraphic(loser);
					if(reason.equals("surrender")) {
						alert.setContentText("You gave up...");
					}else {
						alert.setContentText("Sorry, you lost. Your opponent reached your baseline..");
					}
				}
				alert.showAndWait();
			}
		});
	}
	
	public void showDeadlock() {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Kamisado by ShortyNBaldy - Client");
				alert.setHeaderText("");
				ImageView loser = new ImageView(new Image (getClass().getResourceAsStream("sad_minion.jpg")));
				alert.setGraphic(loser);
				alert.setContentText("You guys are both blocked.. That's a deadlock.");
				alert.showAndWait();
			}
		});
	}
	
	public void showOpponentLeft() {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {			
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Kamisado by ShortyNBaldy - Client");
				alert.setHeaderText("Sorry, your opponent left.");
				alert.setContentText("The Application will close now.");
				alert.showAndWait();
				stop();
			}
		});	
	}
	
	public void giveUp() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Kamisado by ShortyNBaldy - Client");
		alert.setHeaderText("You are about to give up.");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    model.surrender();
		}
	}
	
	public void start() {
		stage.show();
	}

	public void stop() {
		//inform server
		if(model.serverSocket.isConnected()) {model.sendLeave();}
		
		//end thread
		model.running = false;
		
		//exit
		Platform.exit();
	}

	public Stage getStage() {
		return stage;
	}
	
	private void createTowers() {
		// create black towers and set them to the initial position
		
		Rectangle bOrange = new Rectangle();
		bOrange.setFill(Color.ORANGE);
		bOrange.setStroke(Color.BLACK);
		bOrange.setStrokeWidth(STROKE_WIDTH);
		bOrange.setId("BORANGE");
		bOrange.setWidth(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bOrange.setHeight(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bOrange.getTransforms().add(new Rotate(45,bOrange.getWidth()/2,(bOrange.getHeight()/2)));
		GridPane.setHalignment(bOrange, HPos.CENTER);
		if(model.black) {
			gameBoard.add(bOrange, 7, 7);
		}else {
			gameBoard.add(bOrange, 0, 0);
		}
		
		Rectangle bBlue = new Rectangle();
		bBlue.setFill(Color.BLUE);
		bBlue.setStroke(Color.BLACK);
		bBlue.setStrokeWidth(STROKE_WIDTH);
		bBlue.setId("BBLUE");
		bBlue.setWidth(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bBlue.setHeight(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bBlue.getTransforms().add(new Rotate(45,bBlue.getWidth()/2,(bBlue.getHeight()/2)));
		GridPane.setHalignment(bBlue, HPos.CENTER);
		if(model.black) {
			gameBoard.add(bBlue, 6, 7);
		}else {
			gameBoard.add(bBlue, 1, 0);
		}

		Rectangle bPurple = new Rectangle();
		bPurple.setFill(Color.PURPLE);
		bPurple.setStroke(Color.BLACK);
		bPurple.setStrokeWidth(STROKE_WIDTH);
		bPurple.setId("BPURPLE");
		bPurple.setWidth(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bPurple.setHeight(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bPurple.getTransforms().add(new Rotate(45,bPurple.getWidth()/2,(bPurple.getHeight()/2)));
		GridPane.setHalignment(bPurple, HPos.CENTER);
		if(model.black) {
			gameBoard.add(bPurple, 5, 7);
		}else {
			gameBoard.add(bPurple, 2, 0);
		}

		Rectangle bPink = new Rectangle();
		bPink.setFill(Color.PINK);
		bPink.setStroke(Color.BLACK);
		bPink.setStrokeWidth(STROKE_WIDTH);
		bPink.setId("BPINK");
		bPink.setWidth(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bPink.setHeight(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bPink.getTransforms().add(new Rotate(45,bPink.getWidth()/2,(bPink.getHeight()/2)));
		GridPane.setHalignment(bPink, HPos.CENTER);
		if(model.black) {
			gameBoard.add(bPink, 4, 7);
		}else {
			gameBoard.add(bPink, 3, 0);
		}

		Rectangle bYellow = new Rectangle();
		bYellow.setFill(Color.YELLOW);
		bYellow.setStroke(Color.BLACK);
		bYellow.setStrokeWidth(STROKE_WIDTH);
		bYellow.setId("BYELLOW");
		bYellow.setWidth(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bYellow.setHeight(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bYellow.getTransforms().add(new Rotate(45,bYellow.getWidth()/2,(bYellow.getHeight()/2)));
		GridPane.setHalignment(bYellow, HPos.CENTER);
		if(model.black) {
			gameBoard.add(bYellow, 3, 7);
		}else {
			gameBoard.add(bYellow, 4, 0);
		}

		Rectangle bRed = new Rectangle();
		bRed.setFill(Color.RED);
		bRed.setStroke(Color.BLACK);
		bRed.setStrokeWidth(STROKE_WIDTH);
		bRed.setId("BRED");
		bRed.setWidth(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bRed.setHeight(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bRed.getTransforms().add(new Rotate(45,bRed.getWidth()/2,(bRed.getHeight()/2)));
		GridPane.setHalignment(bRed, HPos.CENTER);
		if(model.black) {
			gameBoard.add(bRed, 2, 7);
		}else {
			gameBoard.add(bRed, 5, 0);
		}

		Rectangle bGreen = new Rectangle();
		bGreen.setFill(Color.GREEN);
		bGreen.setStroke(Color.BLACK);
		bGreen.setStrokeWidth(STROKE_WIDTH);
		bGreen.setId("BGREEN");
		bGreen.setWidth(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bGreen.setHeight(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bGreen.getTransforms().add(new Rotate(45,bGreen.getWidth()/2,(bGreen.getHeight()/2)));
		GridPane.setHalignment(bGreen, HPos.CENTER);
		if(model.black) {
			gameBoard.add(bGreen, 1, 7);
		}else {
			gameBoard.add(bGreen, 6, 0);
		}

		Rectangle bBrown = new Rectangle();
		bBrown.setFill(Color.BROWN);
		bBrown.setStroke(Color.BLACK);
		bBrown.setStrokeWidth(STROKE_WIDTH);
		bBrown.setId("BBROWN");
		bBrown.setWidth(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bBrown.setHeight(FIELD_SIZE * SCALEDOWNDIAMONDS);
		bBrown.getTransforms().add(new Rotate(45,bBrown.getWidth()/2,(bBrown.getHeight()/2)));
		GridPane.setHalignment(bBrown, HPos.CENTER);
		if(model.black) {
			gameBoard.add(bBrown, 0, 7);
		}else {
			gameBoard.add(bBrown, 7, 0);
		}

		// create white towers and set them to the initial position
		Circle wOrange = new Circle(FIELD_SIZE * SCALEDOWNCIRCLES);
		wOrange.setFill(Color.ORANGE);
		wOrange.setStroke(Color.BLACK);
		wOrange.setStrokeWidth(STROKE_WIDTH);
		wOrange.setId("WORANGE");
		GridPane.setHalignment(wOrange, HPos.CENTER);
		if(model.black) {
			gameBoard.add(wOrange, 0, 0);
		}else {
			gameBoard.add(wOrange, 7, 7);
		}

		Circle wBlue = new Circle(FIELD_SIZE * SCALEDOWNCIRCLES);
		wBlue.setFill(Color.BLUE);
		wBlue.setStroke(Color.BLACK);
		wBlue.setStrokeWidth(STROKE_WIDTH);
		wBlue.setId("WBLUE");
		GridPane.setHalignment(wBlue, HPos.CENTER);
		if(model.black) {
			gameBoard.add(wBlue, 1, 0);
		}else {
			gameBoard.add(wBlue, 6, 7);
		}

		Circle wPurple = new Circle(FIELD_SIZE * SCALEDOWNCIRCLES);
		wPurple.setFill(Color.PURPLE);
		wPurple.setStroke(Color.BLACK);
		wPurple.setStrokeWidth(STROKE_WIDTH);
		wPurple.setId("WPURPLE");
		GridPane.setHalignment(wPurple, HPos.CENTER);
		if(model.black) {
			gameBoard.add(wPurple, 2, 0);
		}else {
			gameBoard.add(wPurple, 5, 7);
		}

		Circle wPink = new Circle(FIELD_SIZE * SCALEDOWNCIRCLES);
		wPink.setFill(Color.PINK);
		wPink.setStroke(Color.BLACK);
		wPink.setStrokeWidth(STROKE_WIDTH);
		wPink.setId("WPINK");
		GridPane.setHalignment(wPink, HPos.CENTER);
		if(model.black) {
			gameBoard.add(wPink, 3, 0);
		}else {
			gameBoard.add(wPink, 4, 7);
		}

		Circle wYellow = new Circle(FIELD_SIZE * SCALEDOWNCIRCLES);
		wYellow.setFill(Color.YELLOW);
		wYellow.setStroke(Color.BLACK);
		wYellow.setStrokeWidth(STROKE_WIDTH);
		wYellow.setId("WYELLOW");
		GridPane.setHalignment(wYellow, HPos.CENTER);
		if(model.black) {
			gameBoard.add(wYellow, 4, 0);
		}else {
			gameBoard.add(wYellow, 3, 7);
		}

		Circle wRed = new Circle(FIELD_SIZE * SCALEDOWNCIRCLES);
		wRed.setFill(Color.RED);
		wRed.setStroke(Color.BLACK);
		wRed.setStrokeWidth(STROKE_WIDTH);
		wRed.setId("WRED");
		GridPane.setHalignment(wRed, HPos.CENTER);
		if(model.black) {
			gameBoard.add(wRed, 5, 0);
		}else {
			gameBoard.add(wRed, 2, 7);
		}

		Circle wGreen = new Circle(FIELD_SIZE * SCALEDOWNCIRCLES);
		wGreen.setFill(Color.GREEN);
		wGreen.setStroke(Color.BLACK);
		wGreen.setStrokeWidth(STROKE_WIDTH);
		wGreen.setId("WGREEN");
		GridPane.setHalignment(wGreen, HPos.CENTER);
		if(model.black) {
			gameBoard.add(wGreen, 6, 0);
		}else {
			gameBoard.add(wGreen, 1, 7);
		}

		Circle wBrown = new Circle(FIELD_SIZE * SCALEDOWNCIRCLES);
		wBrown.setFill(Color.BROWN);
		wBrown.setStroke(Color.BLACK);
		wBrown.setStrokeWidth(STROKE_WIDTH);
		wBrown.setId("WBROWN");
		GridPane.setHalignment(wBrown, HPos.CENTER);
		if(model.black) {
			gameBoard.add(wBrown, 7, 0);
		}else {
			gameBoard.add(wBrown, 0, 7);
		}
	}
	
	private void createFields() {
		Rectangle a = new Rectangle();
		a.setWidth(FIELD_SIZE);
		a.setHeight(FIELD_SIZE);
		a.setFill(Color.ORANGE);
		gameBoard.add(a, 0, 0);

		Rectangle b = new Rectangle();
		b.setWidth(FIELD_SIZE);
		b.setHeight(FIELD_SIZE);
		b.setFill(Color.BLUE);
		gameBoard.add(b, 1, 0);

		Rectangle c = new Rectangle();
		c.setWidth(FIELD_SIZE);
		c.setHeight(FIELD_SIZE);
		c.setFill(Color.PURPLE);
		gameBoard.add(c, 2, 0);

		Rectangle d = new Rectangle();
		d.setWidth(FIELD_SIZE);
		d.setHeight(FIELD_SIZE);
		d.setFill(Color.PINK);
		gameBoard.add(d, 3, 0);

		Rectangle e = new Rectangle();
		e.setWidth(FIELD_SIZE);
		e.setHeight(FIELD_SIZE);
		e.setFill(Color.YELLOW);
		gameBoard.add(e, 4, 0);

		Rectangle f = new Rectangle();
		f.setWidth(FIELD_SIZE);
		f.setHeight(FIELD_SIZE);
		f.setFill(Color.RED);
		gameBoard.add(f, 5, 0);

		Rectangle g = new Rectangle();
		g.setWidth(FIELD_SIZE);
		g.setHeight(FIELD_SIZE);
		g.setFill(Color.GREEN);
		gameBoard.add(g, 6, 0);

		Rectangle h = new Rectangle();
		h.setWidth(FIELD_SIZE);
		h.setHeight(FIELD_SIZE);
		h.setFill(Color.BROWN);
		gameBoard.add(h, 7, 0);

		// second row
		Rectangle i = new Rectangle();
		i.setWidth(FIELD_SIZE);
		i.setHeight(FIELD_SIZE);
		i.setFill(Color.RED);
		gameBoard.add(i, 0, 1);

		Rectangle j = new Rectangle();
		j.setWidth(FIELD_SIZE);
		j.setHeight(FIELD_SIZE);
		j.setFill(Color.ORANGE);
		gameBoard.add(j, 1, 1);

		Rectangle k = new Rectangle();
		k.setWidth(FIELD_SIZE);
		k.setHeight(FIELD_SIZE);
		k.setFill(Color.PINK);
		gameBoard.add(k, 2, 1);

		Rectangle l = new Rectangle();
		l.setWidth(FIELD_SIZE);
		l.setHeight(FIELD_SIZE);
		l.setFill(Color.GREEN);
		gameBoard.add(l, 3, 1);

		Rectangle m = new Rectangle();
		m.setWidth(FIELD_SIZE);
		m.setHeight(FIELD_SIZE);
		m.setFill(Color.BLUE);
		gameBoard.add(m, 4, 1);

		Rectangle n = new Rectangle();
		n.setWidth(FIELD_SIZE);
		n.setHeight(FIELD_SIZE);
		n.setFill(Color.YELLOW);
		gameBoard.add(n, 5, 1);

		Rectangle o = new Rectangle();
		o.setWidth(FIELD_SIZE);
		o.setHeight(FIELD_SIZE);
		o.setFill(Color.BROWN);
		gameBoard.add(o, 6, 1);

		Rectangle p = new Rectangle();
		p.setWidth(FIELD_SIZE);
		p.setHeight(FIELD_SIZE);
		p.setFill(Color.PURPLE);
		gameBoard.add(p, 7, 1);

		// THIRD ROW
		Rectangle r = new Rectangle();
		r.setWidth(FIELD_SIZE);
		r.setHeight(FIELD_SIZE);
		r.setFill(Color.GREEN);
		gameBoard.add(r, 0, 2);

		Rectangle s = new Rectangle();
		s.setWidth(FIELD_SIZE);
		s.setHeight(FIELD_SIZE);
		s.setFill(Color.PINK);
		gameBoard.add(s, 1, 2);

		Rectangle t = new Rectangle();
		t.setWidth(FIELD_SIZE);
		t.setHeight(FIELD_SIZE);
		t.setFill(Color.ORANGE);
		gameBoard.add(t, 2, 2);

		Rectangle u = new Rectangle();
		u.setWidth(FIELD_SIZE);
		u.setHeight(FIELD_SIZE);
		u.setFill(Color.RED);
		gameBoard.add(u, 3, 2);

		Rectangle v = new Rectangle();
		v.setWidth(FIELD_SIZE);
		v.setHeight(FIELD_SIZE);
		v.setFill(Color.PURPLE);
		gameBoard.add(v, 4, 2);

		Rectangle w = new Rectangle();
		w.setWidth(FIELD_SIZE);
		w.setHeight(FIELD_SIZE);
		w.setFill(Color.BROWN);
		gameBoard.add(w, 5, 2);

		Rectangle x = new Rectangle();
		x.setWidth(FIELD_SIZE);
		x.setHeight(FIELD_SIZE);
		x.setFill(Color.YELLOW);
		gameBoard.add(x, 6, 2);

		Rectangle y = new Rectangle();
		y.setWidth(FIELD_SIZE);
		y.setHeight(FIELD_SIZE);
		y.setFill(Color.BLUE);
		gameBoard.add(y, 7, 2);

		// fourth

		Rectangle aa = new Rectangle();
		aa.setWidth(FIELD_SIZE);
		aa.setHeight(FIELD_SIZE);
		aa.setFill(Color.PINK);
		gameBoard.add(aa, 0, 3);

		Rectangle bb = new Rectangle();
		bb.setWidth(FIELD_SIZE);
		bb.setHeight(FIELD_SIZE);
		bb.setFill(Color.PURPLE);
		gameBoard.add(bb, 1, 3);

		Rectangle cc = new Rectangle();
		cc.setWidth(FIELD_SIZE);
		cc.setHeight(FIELD_SIZE);
		cc.setFill(Color.BLUE);
		gameBoard.add(cc, 2, 3);

		Rectangle dd = new Rectangle();
		dd.setWidth(FIELD_SIZE);
		dd.setHeight(FIELD_SIZE);
		dd.setFill(Color.ORANGE);
		gameBoard.add(dd, 3, 3);

		Rectangle ee = new Rectangle();
		ee.setWidth(FIELD_SIZE);
		ee.setHeight(FIELD_SIZE);
		ee.setFill(Color.BROWN);
		gameBoard.add(ee, 4, 3);

		Rectangle ff = new Rectangle();
		ff.setWidth(FIELD_SIZE);
		ff.setHeight(FIELD_SIZE);
		ff.setFill(Color.GREEN);
		gameBoard.add(ff, 5, 3);

		Rectangle gg = new Rectangle();
		gg.setWidth(FIELD_SIZE);
		gg.setHeight(FIELD_SIZE);
		gg.setFill(Color.RED);
		gameBoard.add(gg, 6, 3);

		Rectangle hh = new Rectangle();
		hh.setWidth(FIELD_SIZE);
		hh.setHeight(FIELD_SIZE);
		hh.setFill(Color.YELLOW);
		gameBoard.add(hh, 7, 3);

		// eight row
		Rectangle ii = new Rectangle();
		ii.setWidth(FIELD_SIZE);
		ii.setHeight(FIELD_SIZE);
		ii.setFill(Color.ORANGE);
		gameBoard.add(ii, 7, 7);

		Rectangle jj = new Rectangle();
		jj.setWidth(FIELD_SIZE);
		jj.setHeight(FIELD_SIZE);
		jj.setFill(Color.BLUE);
		gameBoard.add(jj, 6, 7);

		Rectangle kk = new Rectangle();
		kk.setWidth(FIELD_SIZE);
		kk.setHeight(FIELD_SIZE);
		kk.setFill(Color.PURPLE);
		gameBoard.add(kk, 5, 7);

		Rectangle ll = new Rectangle();
		ll.setWidth(FIELD_SIZE);
		ll.setHeight(FIELD_SIZE);
		ll.setFill(Color.PINK);
		gameBoard.add(ll, 4, 7);

		Rectangle mm = new Rectangle();
		mm.setWidth(FIELD_SIZE);
		mm.setHeight(FIELD_SIZE);
		mm.setFill(Color.YELLOW);
		gameBoard.add(mm, 3, 7);

		Rectangle nn = new Rectangle();
		nn.setWidth(FIELD_SIZE);
		nn.setHeight(FIELD_SIZE);
		nn.setFill(Color.RED);
		gameBoard.add(nn, 2, 7);

		Rectangle oo = new Rectangle();
		oo.setWidth(FIELD_SIZE);
		oo.setHeight(FIELD_SIZE);
		oo.setFill(Color.GREEN);
		gameBoard.add(oo, 1, 7);

		Rectangle pp = new Rectangle();
		pp.setWidth(FIELD_SIZE);
		pp.setHeight(FIELD_SIZE);
		pp.setFill(Color.BROWN);
		gameBoard.add(pp, 0, 7);

		// seventh row
		Rectangle qq = new Rectangle();
		qq.setWidth(FIELD_SIZE);
		qq.setHeight(FIELD_SIZE);
		qq.setFill(Color.RED);
		gameBoard.add(qq, 7, 6);

		Rectangle rr = new Rectangle();
		rr.setWidth(FIELD_SIZE);
		rr.setHeight(FIELD_SIZE);
		rr.setFill(Color.ORANGE);
		gameBoard.add(rr, 6, 6);

		Rectangle ss = new Rectangle();
		ss.setWidth(FIELD_SIZE);
		ss.setHeight(FIELD_SIZE);
		ss.setFill(Color.PINK);
		gameBoard.add(ss, 5, 6);

		Rectangle tt = new Rectangle();
		tt.setWidth(FIELD_SIZE);
		tt.setHeight(FIELD_SIZE);
		tt.setFill(Color.GREEN);
		gameBoard.add(tt, 4, 6);

		Rectangle uu = new Rectangle();
		uu.setWidth(FIELD_SIZE);
		uu.setHeight(FIELD_SIZE);
		uu.setFill(Color.BLUE);
		gameBoard.add(uu, 3, 6);

		Rectangle vv = new Rectangle();
		vv.setWidth(FIELD_SIZE);
		vv.setHeight(FIELD_SIZE);
		vv.setFill(Color.YELLOW);
		gameBoard.add(vv, 2, 6);

		Rectangle ww = new Rectangle();
		ww.setWidth(FIELD_SIZE);
		ww.setHeight(FIELD_SIZE);
		ww.setFill(Color.BROWN);
		gameBoard.add(ww, 1, 6);

		Rectangle xx = new Rectangle();
		xx.setWidth(FIELD_SIZE);
		xx.setHeight(FIELD_SIZE);
		xx.setFill(Color.PURPLE);
		gameBoard.add(xx, 0, 6);

		// sixth
		Rectangle aaa = new Rectangle();
		aaa.setWidth(FIELD_SIZE);
		aaa.setHeight(FIELD_SIZE);
		aaa.setFill(Color.GREEN);
		gameBoard.add(aaa, 7, 5);

		Rectangle bbb = new Rectangle();
		bbb.setWidth(FIELD_SIZE);
		bbb.setHeight(FIELD_SIZE);
		bbb.setFill(Color.PINK);
		gameBoard.add(bbb, 6, 5);

		Rectangle ccc = new Rectangle();
		ccc.setWidth(FIELD_SIZE);
		ccc.setHeight(FIELD_SIZE);
		ccc.setFill(Color.ORANGE);
		gameBoard.add(ccc, 5, 5);

		Rectangle ddd = new Rectangle();
		ddd.setWidth(FIELD_SIZE);
		ddd.setHeight(FIELD_SIZE);
		ddd.setFill(Color.RED);
		gameBoard.add(ddd, 4, 5);

		Rectangle eee = new Rectangle();
		eee.setWidth(FIELD_SIZE);
		eee.setHeight(FIELD_SIZE);
		eee.setFill(Color.PURPLE);
		gameBoard.add(eee, 3, 5);

		Rectangle fff = new Rectangle();
		fff.setWidth(FIELD_SIZE);
		fff.setHeight(FIELD_SIZE);
		fff.setFill(Color.BROWN);
		gameBoard.add(fff, 2, 5);

		Rectangle ggg = new Rectangle();
		ggg.setWidth(FIELD_SIZE);
		ggg.setHeight(FIELD_SIZE);
		ggg.setFill(Color.YELLOW);
		gameBoard.add(ggg, 1, 5);

		Rectangle hhh = new Rectangle();
		hhh.setWidth(FIELD_SIZE);
		hhh.setHeight(FIELD_SIZE);
		hhh.setFill(Color.BLUE);
		gameBoard.add(hhh, 0, 5);

		// fifth

		Rectangle iii = new Rectangle();
		iii.setWidth(FIELD_SIZE);
		iii.setHeight(FIELD_SIZE);
		iii.setFill(Color.PINK);
		gameBoard.add(iii, 7, 4);

		Rectangle jjj = new Rectangle();
		jjj.setWidth(FIELD_SIZE);
		jjj.setHeight(FIELD_SIZE);
		jjj.setFill(Color.PURPLE);
		gameBoard.add(jjj, 6, 4);

		Rectangle kkk = new Rectangle();
		kkk.setWidth(FIELD_SIZE);
		kkk.setHeight(FIELD_SIZE);
		kkk.setFill(Color.BLUE);
		gameBoard.add(kkk, 5, 4);

		Rectangle lll = new Rectangle();
		lll.setWidth(FIELD_SIZE);
		lll.setHeight(FIELD_SIZE);
		lll.setFill(Color.ORANGE);
		gameBoard.add(lll, 4, 4);

		Rectangle mmm = new Rectangle();
		mmm.setWidth(FIELD_SIZE);
		mmm.setHeight(FIELD_SIZE);
		mmm.setFill(Color.BROWN);
		gameBoard.add(mmm, 3, 4);

		Rectangle nnn = new Rectangle();
		nnn.setWidth(FIELD_SIZE);
		nnn.setHeight(FIELD_SIZE);
		nnn.setFill(Color.GREEN);
		gameBoard.add(nnn, 2, 4);

		Rectangle ooo = new Rectangle();
		ooo.setWidth(FIELD_SIZE);
		ooo.setHeight(FIELD_SIZE);
		ooo.setFill(Color.RED);
		gameBoard.add(ooo, 1, 4);

		Rectangle ppp = new Rectangle();
		ppp.setWidth(FIELD_SIZE);
		ppp.setHeight(FIELD_SIZE);
		ppp.setFill(Color.YELLOW);
		gameBoard.add(ppp, 0, 4);
	}

}
