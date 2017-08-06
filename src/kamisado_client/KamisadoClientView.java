package kamisado_client;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class KamisadoClientView {
	private Logger logger = Logger.getLogger("");
	private Stage stage;
	private KamisadoClientModel model;
	protected Button btnClick;
	protected Button resetGame;
	protected Button giveUp;
	protected ComboBox<String> language;
	protected Label moveWait;
	protected BorderPane root;
	protected GridPane gameBoard;
	protected int oldLocationCol;
	protected int newLocationCol;
	protected int oldLocationRow;
	protected int newLocationRow;
	//protected Circle wBrown;
	
	public static final int FIELD_SIZE = 70;
	public static final int WIDTH = 10;
	public static final double HIGHLIGHT_WIDTH = 1.5;
	public static final double SCALEDOWN = 0.42;

	
	public KamisadoClientView(Stage stage, KamisadoClientModel model){
		this.stage = stage;
		this.model = model;
		
		
	
		//View Stuff goes here
		btnClick = new Button(" Testing "); //Only an example for showing in the controller how to register for events
		
		root = new BorderPane();
		gameBoard = new GridPane(); // Kamisado game board
	//	gameBoard.setGridLinesVisible(true);
		//gameBoard.getColumnConstraints().addAll( new ColumnConstraints( FIELD_SIZE ), new ColumnConstraints( FIELD_SIZE ), new ColumnConstraints( FIELD_SIZE ) );
	
		HBox upperScreen = new HBox(); // upper part of the game screen for buttons
		
		root.setTop(upperScreen); 
		resetGame = new Button("ResetGame");
		giveUp = new Button("Give Up");
		language = new ComboBox<String>(); // drop-down menu to change language in the game
		language.getItems().addAll("DE", "EN");
		moveWait = new Label("Move / Wait");
		upperScreen.getChildren().addAll(resetGame, giveUp, moveWait, language, btnClick);
		
				
	// first row
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
		e.setFill(Color.GOLD);
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
		
		// second  row
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
		n.setFill(Color.GOLD);
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
		x.setFill(Color.GOLD);
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
		hh.setFill(Color.GOLD);
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
				mm.setFill(Color.GOLD);
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
				
		// seventh  row
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
				vv.setFill(Color.GOLD);
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
				gameBoard.add(ddd,4, 5);
				
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
				ggg.setFill(Color.GOLD);
				gameBoard.add(ggg, 1, 5);
				
				Rectangle hhh = new Rectangle();
				hhh.setWidth(FIELD_SIZE);
				hhh.setHeight(FIELD_SIZE);
				hhh.setFill(Color.BLUE);
				gameBoard.add(hhh, 0, 5);
					
		//fifth
				
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
				ppp.setFill(Color.GOLD);
				gameBoard.add(ppp, 0, 4);
				
// black towers		
				

		Circle bOrange = new Circle(FIELD_SIZE * SCALEDOWN);
		bOrange.setFill(Color.ORANGE);
		bOrange.setStroke(Color.BLACK);
		bOrange.setStrokeWidth(WIDTH);
		bOrange.setId("BORANGE");
		gameBoard.add(bOrange, 0, 0);
		
		Circle bBlue = new Circle(FIELD_SIZE * SCALEDOWN);
		bBlue.setFill(Color.BLUE);
		bBlue.setStroke(Color.BLACK);
		bBlue.setStrokeWidth(WIDTH);
		bBlue.setId("BBLUE");
		gameBoard.add(bBlue, 1, 0);
		
		Circle bPurple = new Circle(FIELD_SIZE * SCALEDOWN);
		bPurple.setFill(Color.PURPLE);
		bPurple.setStroke(Color.BLACK);
		bPurple.setStrokeWidth(WIDTH);
		bPurple.setId("BPURPLE");
		gameBoard.add(bPurple, 2, 0);
		
		Circle bPink = new Circle(FIELD_SIZE * SCALEDOWN);
		bPink.setFill(Color.PINK);
		bPink.setStroke(Color.BLACK);
		bPink.setStrokeWidth(WIDTH);
		bPink.setId("BPINK");
		gameBoard.add(bPink, 3, 0);
		
		Circle bYellow = new Circle(FIELD_SIZE * SCALEDOWN);
		bYellow.setFill(Color.GOLD);
		bYellow.setStroke(Color.BLACK);
		bYellow.setStrokeWidth(WIDTH);
		bYellow.setId("BYELLOW");
		gameBoard.add(bYellow, 4, 0);
				
		Circle bRed = new Circle(FIELD_SIZE * SCALEDOWN);
		bRed.setFill(Color.RED);
		bRed.setStroke(Color.BLACK);
		bRed.setStrokeWidth(WIDTH);
		bRed.setId("BRED");
		gameBoard.add(bRed,5, 0);
		
		Circle bGreen = new Circle(FIELD_SIZE * SCALEDOWN);
		bGreen.setFill(Color.GREEN);
		bGreen.setStroke(Color.BLACK);
		bGreen.setStrokeWidth(WIDTH);
		bGreen.setId("BGREEN");
		gameBoard.add(bGreen, 6, 0);
		
		Circle bBrown = new Circle(FIELD_SIZE * SCALEDOWN);
		bBrown.setFill(Color.BROWN);
		bBrown.setStroke(Color.BLACK);
		bBrown.setStrokeWidth(WIDTH);
		bBrown.setId("BBROWN");
		gameBoard.add(bBrown, 7, 0);
		
	//white towers
		Circle wOrange = new Circle(FIELD_SIZE * SCALEDOWN);
		wOrange.setFill(Color.ORANGE);
		wOrange.setStroke(Color.WHITE);
		wOrange.setStrokeWidth(WIDTH);
		wOrange.setId("WORANGE");
		gameBoard.add(wOrange, 7, 7);
		
		Circle wBlue = new Circle(FIELD_SIZE * SCALEDOWN);
		wBlue.setFill(Color.BLUE);
		wBlue.setStroke(Color.WHITE);
		wBlue.setStrokeWidth(WIDTH);
		wBlue.setId("WBLUE");
		gameBoard.add(wBlue, 6, 7);
		
		Circle wPurple = new Circle(FIELD_SIZE * SCALEDOWN);
		wPurple.setFill(Color.PURPLE);
		wPurple.setStroke(Color.WHITE);
		wPurple.setStrokeWidth(WIDTH);
		wPurple.setId("WBLUE");
		gameBoard.add(wPurple, 5, 7);
		
		Circle wPink = new Circle(FIELD_SIZE * SCALEDOWN);
		wPink.setFill(Color.PINK);
		wPink.setStroke(Color.WHITE);
		wPink.setStrokeWidth(WIDTH);
		wPink.setId("WPINK");
		gameBoard.add(wPink, 4, 7);
		
		Circle wYellow= new Circle(FIELD_SIZE * SCALEDOWN);
		wYellow.setFill(Color.GOLD);
		wYellow.setStroke(Color.WHITE);
		wYellow.setStrokeWidth(WIDTH);
		wYellow.setId("WYELLOW");
		gameBoard.add(wYellow, 3, 7);
				
		Circle wRed = new Circle(FIELD_SIZE * SCALEDOWN);
		wRed.setFill(Color.RED);
		wRed.setStroke(Color.WHITE);
		wRed.setStrokeWidth(WIDTH);
		wRed.setId("WRED");
		gameBoard.add(wRed,  2, 7);
		
		Circle wGreen = new Circle(FIELD_SIZE * SCALEDOWN);
		wGreen.setFill(Color.GREEN);
		wGreen.setStroke(Color.WHITE);
		wGreen.setStrokeWidth(WIDTH);
		wGreen.setId("WGREEN");
		gameBoard.add(wGreen, 1, 7);
		
		Circle wBrown = new Circle(FIELD_SIZE * SCALEDOWN);
		wBrown.setFill(Color.BROWN);
		wBrown.setStroke(Color.WHITE);
		wBrown.setStrokeWidth(WIDTH);
		wBrown.setId("WBROWN");
		gameBoard.add(wBrown, 0, 7);
				
		root.setCenter(gameBoard);
		
		Label lblBottom = new Label("Bottom");
		root.setBottom(lblBottom);
		Label lblLeft = new Label("Left");
		root.setLeft(lblLeft);
		Label lblRight = new Label("Right");
		root.setRight(lblRight);

		
				
		root.setCenter(gameBoard);
				
		//add functionality to the reset button
		
		resetGame.setOnAction((event)->{
		//	if(gameBoard.getID.equals("BBROWN"));
				
		while(gameBoard.getId() != null) {
			
		}
			//oldLocationCol = gameBoard.getColumnIndex(wBrown);
			//oldLocationRow = gameBoard.getRowIndex(wBrown);
			//gameBoard.setColumnIndex(bOrange, 5);
			//gameBoard.setRowIndex(bOrange, 2);
			
					
			
			
		});
		
		btnClick.setOnAction((event)->{
				h.setStroke(Color.AQUA);
				h.setStrokeWidth(HIGHLIGHT_WIDTH);
			
		});
		
	
		
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(
                getClass().getResource("layouts.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Kamisado by ShortyNBaldy - Client");
		stage.getIcons().add(new Image("/shortyNBaldy.png"));
		
	}

		public void moveTower(String movedTower, int newXPos, int newYPos) {
			//gameBoard.getId().equalsTo().
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