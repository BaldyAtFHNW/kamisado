package kamisado_server;

import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * 	@author Simon Bieri
 * 	@date 2017-08-11
 *	Communication with the clients and numbercrunching is executed within the model.
 *
 */
public class ServerModel{
	Logger logger = Logger.getLogger("");
	String br = System.getProperty("line.separator");
	
	final int boardSize = 8;
	private FieldColor[][] gameboard;
	private TowerColor[][] towerPositions;;
	
	ServerSocket listener;
	protected Socket socketPlB;
	protected Socket socketPlW;
	
	protected String namePlB;
	protected String namePlW;

	protected SimpleStringProperty newestMsgPlBlack = new SimpleStringProperty();
	protected SimpleStringProperty newestMsgPlWhite = new SimpleStringProperty();
	
	protected int scoreB = 0;
	protected int scoreW = 0;
	
	protected SimpleStringProperty newMsgGui = new SimpleStringProperty();
	
	protected boolean running = true;
	
	/**
	 * Connects two clients and initalizes the game as soon as done
	 * @return void
	 */
	public void connectClients(){
		newMsgGui.set("Server started\nPending for players - Please wait");
		try{
			listener = new ServerSocket(50000, 10, null);
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						//Connect 1st Player
						socketPlB = listener.accept();
						newMsgGui.set("Player1 (black) connected: " + socketPlB.toString());
						ServerClientThread clientPlayerBlack = new ServerClientThread(ServerModel.this, socketPlB, true);
						new Thread(clientPlayerBlack).start();
						
						//Connect 2nd Player
						socketPlW = listener.accept();
						newMsgGui.set("Player2 (white) connected: " + socketPlW.toString());
						ServerClientThread clientPlayerWhite = new ServerClientThread(ServerModel.this, socketPlW, false);
						new Thread(clientPlayerWhite).start();
						
						listener.close();
						
						Thread.sleep(2000); //Let first the introductions arrive
					}catch(Exception e){
						logger.warning(e.toString());
						e.printStackTrace();
					}
					
					Random random = new Random(); //from: https://stackoverflow.com/questions/8878015/return-true-or-false-randomly
					char firstMove;
					if(random.nextBoolean()) {
						firstMove = 'W';
					}else {
						firstMove = 'B';
					}
					initGame(firstMove); //Start game by sending initialization messages to both clients
				}
			};
			Thread t = new Thread(r, "ServerSocket");
			t.start();
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if a tower is blocked
	 * @param TowerColor tower
	 * @return boolean isBlocked
	 */
	private boolean towerIsBlocked(TowerColor tower) {
		boolean blocked = false;
		if (this.getPossibleMoves(tower).isEmpty()){
			blocked = true;
		}
		return blocked;
	}
	
	/**
	 * Returns the next tower which is being moved according to the game rules
	 * @param int xPos
	 * @param int yPos
	 * @param char nextPlayer
	 * @return towerColor tower
	 */
	private TowerColor getNextTower(int xPos, int yPos, char nextPlayer) {
		FieldColor landedFieldCol = this.getFieldColor(xPos, yPos);
		TowerColor nextTwr = TowerColor.valueOf(nextPlayer + landedFieldCol.toString());
		return nextTwr;
	}
	
	/**
	 * Returns the next tower which is actually playable, taking blocked towers into account
	 * Checks if there are loops of blocked towers to take deadlocks into account. returns null if deadlock
	 * @param int xPos
	 * @param int yPos
	 * @param char nextPlayer
	 * @return towerColor tower
	 */
	protected TowerColor getNextPlayableTower(int xPos, int yPos, char nextPlayer) {
		TowerColor nextTower = getNextTower(xPos, yPos, nextPlayer);
		ArrayList<String> blockedTowers = new ArrayList<String>();
		while(towerIsBlocked(nextTower)) {
			if(!blockedTowers.isEmpty()) {
				for(String bt : blockedTowers) {
					if(bt.equals(nextTower.toString())) {
						return null;
					}
				}
			}
			
			//Add new blocked tower to array
			blockedTowers.add(nextTower.toString());
			
			//Get Next Tower
			System.out.println("Tower " + nextTower.toString() + " is blocked");
			Integer[] nxtTwrPos = this.getTowerPos(nextTower);
			if(nextPlayer == 'B') {nextPlayer = 'W';}else {nextPlayer = 'B';}
			nextTower = getNextTower(nxtTwrPos[0], nxtTwrPos[1], nextPlayer);
		}
		return nextTower;
	}
	
	/**
	 * Starts the game
	 * @param char firstPlayer
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	protected void initGame(char firstMove){
		this.initGameBoard();
		this.initTowers();
		
		JSONObject initPlayerBlack = new JSONObject();
		initPlayerBlack.put("type", "init");
		initPlayerBlack.put("black", true);
		initPlayerBlack.put("opponent", namePlW);
		initPlayerBlack.put("score", this.scoreB);
		initPlayerBlack.put("op_score", this.scoreW);
		
		JSONObject initPlayerWhite = new JSONObject();
		initPlayerWhite.put("type", "init");
		initPlayerWhite.put("black", false);
		initPlayerWhite.put("opponent", namePlB);
		initPlayerWhite.put("score", this.scoreW);
		initPlayerWhite.put("op_score", this.scoreB);
		
		//Set the first move
		if(firstMove == 'B') {
			initPlayerBlack.put("start", true);
			initPlayerWhite.put("start", false);
			this.newMsgGui.set("Game started - Black Player has got the first move");
		}else {
			initPlayerBlack.put("start", false);
			initPlayerWhite.put("start", true);
			this.newMsgGui.set("Game started - White Player has got the first move");
		}
		
		this.send(initPlayerBlack.toString(), 'B');
		this.send(initPlayerWhite.toString(), 'W');
	}
	
	/**
	 * restarts the game
	 * @param char firstPlayer
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	protected void restartGame(char firstMove){
		this.initGameBoard();
		this.initTowers();
		
		JSONObject initPlayerBlack = new JSONObject();
		initPlayerBlack.put("type", "restart");
		initPlayerBlack.put("score", this.scoreB);
		initPlayerBlack.put("op_score", this.scoreW);
		
		JSONObject initPlayerWhite = new JSONObject();
		initPlayerWhite.put("type", "restart");
		initPlayerWhite.put("score", this.scoreW);
		initPlayerWhite.put("op_score", this.scoreB);
		
		//Set the first move
		if(firstMove == 'B') {
			initPlayerBlack.put("start", true);
			initPlayerWhite.put("start", false);
			this.newMsgGui.set("Game restarted - Black Player has got the first move");
		}else {
			initPlayerBlack.put("start", false);
			initPlayerWhite.put("start", true);
			this.newMsgGui.set("Game restarted - White Player has got the first move");
		}
		
		this.send(initPlayerBlack.toString(), 'B');
		this.send(initPlayerWhite.toString(), 'W');
	}

	/**
	 * sends a message to a client depending on the char
	 * @param String jsonString
	 * @param char toWhichPlayer
	 * @return void
	 */
	protected void send(String msg, char plCol){
		OutputStreamWriter out;
		try {
			if(plCol == 'B'){
				out = new OutputStreamWriter(this.socketPlB.getOutputStream());
				out.write(msg + br);
				out.flush();
			}else if(plCol == 'W'){
				out = new OutputStreamWriter(this.socketPlW.getOutputStream());
				out.write(msg + br);
				out.flush();
			}else {
				logger.warning("Receiver does not exist!");
			}
		} catch (Exception e) {
			logger.info(e.toString());
			e.printStackTrace();
		}
	}
	
	protected void printTowerPos() {
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				if(this.towerPositions[x][y] != null) {
						logger.info(this.towerPositions[x][y].toString() + " is located at: " + x + " " + y);
				}
			}
		}
	}
	
	protected Integer[] getTowerPos(TowerColor towerColor) {
		Integer[] towerPos = new Integer[2];
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				if(this.towerPositions[x][y] != null) {
					if(this.towerPositions[x][y].toString().equals(towerColor.toString())) {
						towerPos[0] = x;
						towerPos[1] = y;
					}
				}
			}
		}
		return towerPos;
	}
	
	protected void moveTower(TowerColor towerColor, int xPos, int yPos) {		
		//Remove the tower from old position
		Integer[] pos = this.getTowerPos(towerColor);
		int x = pos[0];
		int y = pos[1];
		this.towerPositions[x][y] = null;

		//Put tower to new position
		if(this.towerPositions[xPos][yPos] != null) {
			logger.warning("Field already taken!");
		}else {this.towerPositions[xPos][yPos] = towerColor;}
	}
	
	protected FieldColor getFieldColor(int xPos, int yPos) {
		return this.gameboard[xPos][yPos];
	}
	
	@SuppressWarnings("unchecked")
	protected JSONArray getPossibleMoves(TowerColor towerColor) {
		Integer[] currentPos = this.getTowerPos(towerColor);
		char color = towerColor.toString().charAt(0); //Either W or B
		int currX = currentPos[0];
		int currY = currentPos[1];
		
		JSONArray possibleMoves = new JSONArray();
		//get LEFT diagonals
		int x = currX; int y = currY;
		do {
			if(color == 'W') {x--; y++;}else {x--; y--;} 	//White and Black Player's moves have to be calculated differently
			if(x < 0 || x > 7 || y < 0 || y > 7) {;break;} 	//Otherwise out of Gameboard
			if(this.towerPositions[x][y] == null) {
				JSONObject move = new JSONObject();
				move.put("xPos", x);
				move.put("yPos", y);
				possibleMoves.add(move);
			}
		}while(this.towerPositions[x][y] == null);

		//get RIGHT diagonals
		x = currX; y = currY;
		do {
			if(color == 'W') {x++; y++;}else {x++; y--;}	//White and Black Player's moves have to be calculated differently
			if(x < 0 || x > 7 || y < 0 || y > 7) {;break;} 	//Otherwise out of Gameboard
			if(this.towerPositions[x][y] == null) {
				JSONObject move = new JSONObject();
				move.put("xPos", x);
				move.put("yPos", y);
				possibleMoves.add(move);
			}
		}while(this.towerPositions[x][y] == null);
		
		//get HORIZONTALS
		x = currX; y = currY;
		do {
			if(color == 'W') {y++;}else {y--;}				//White and Black Player's moves have to be calculated differently
			if(x < 0 || x > 7 || y < 0 || y > 7) {;break;} 	//Otherwise out of Gameboard
			if(this.towerPositions[x][y] == null) {
				JSONObject move = new JSONObject();
				move.put("xPos", x);
				move.put("yPos", y);
				possibleMoves.add(move);
			}
		}while(this.towerPositions[x][y] == null);
		
		return possibleMoves;
	}
	
	protected JSONObject parseJSON(String msg){
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject();
		
		try {
			Object obj = parser.parse(msg);
			json = (JSONObject) obj;
		} catch (Exception e) {
			logger.warning(e.toString());
			e.printStackTrace();
		}
		return json;
	}
	
	protected void initTowers(){
		
		this.towerPositions = new TowerColor[boardSize][boardSize];
		//Set all positions to null
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				this.towerPositions[x][y] = null;
			}
		}
		
		//Put the towers on the gameboard
		
		//white player tower positions
		this.towerPositions[0][0] = TowerColor.WBROWN;
		this.towerPositions[1][0] = TowerColor.WGREEN;
		this.towerPositions[2][0] = TowerColor.WRED;
		this.towerPositions[3][0] = TowerColor.WYELLOW;
		this.towerPositions[4][0] = TowerColor.WPINK;
		this.towerPositions[5][0] = TowerColor.WPURPLE;
		this.towerPositions[6][0] = TowerColor.WBLUE;
		this.towerPositions[7][0] = TowerColor.WORANGE;
		
		//black player tower positions
		this.towerPositions[0][7] = TowerColor.BORANGE;
		this.towerPositions[1][7] = TowerColor.BBLUE;
		this.towerPositions[2][7] = TowerColor.BPURPLE;
		this.towerPositions[3][7] = TowerColor.BPINK;
		this.towerPositions[4][7] = TowerColor.BYELLOW;
		this.towerPositions[5][7] = TowerColor.BRED;
		this.towerPositions[6][7] = TowerColor.BGREEN;
		this.towerPositions[7][7] = TowerColor.BBROWN;
	}
	
	protected void initGameBoard(){
		this.gameboard = new FieldColor[boardSize][boardSize];
		
		this.gameboard[0][0] = FieldColor.BROWN;
		this.gameboard[1][0] = FieldColor.GREEN;
		this.gameboard[2][0] = FieldColor.RED;
		this.gameboard[3][0] = FieldColor.YELLOW;
		this.gameboard[4][0] = FieldColor.PINK;
		this.gameboard[5][0] = FieldColor.PURPLE;
		this.gameboard[6][0] = FieldColor.BLUE;
		this.gameboard[7][0] = FieldColor.ORANGE;
		
		this.gameboard[0][1] = FieldColor.PURPLE;
		this.gameboard[1][1] = FieldColor.BROWN;
		this.gameboard[2][1] = FieldColor.YELLOW;
		this.gameboard[3][1] = FieldColor.BLUE;
		this.gameboard[4][1] = FieldColor.GREEN;
		this.gameboard[5][1] = FieldColor.PINK;
		this.gameboard[6][1] = FieldColor.ORANGE;
		this.gameboard[7][1] = FieldColor.RED;
		
		this.gameboard[0][2] = FieldColor.BLUE;
		this.gameboard[1][2] = FieldColor.YELLOW;
		this.gameboard[2][2] = FieldColor.BROWN;
		this.gameboard[3][2] = FieldColor.PURPLE;
		this.gameboard[4][2] = FieldColor.RED;
		this.gameboard[5][2] = FieldColor.ORANGE;
		this.gameboard[6][2] = FieldColor.PINK;
		this.gameboard[7][2] = FieldColor.GREEN;
		
		this.gameboard[0][3] = FieldColor.YELLOW;
		this.gameboard[1][3] = FieldColor.RED;
		this.gameboard[2][3] = FieldColor.GREEN;
		this.gameboard[3][3] = FieldColor.BROWN;
		this.gameboard[4][3] = FieldColor.ORANGE;
		this.gameboard[5][3] = FieldColor.BLUE;
		this.gameboard[6][3] = FieldColor.PURPLE;
		this.gameboard[7][3] = FieldColor.PINK;
		
		this.gameboard[0][4] = FieldColor.PINK;
		this.gameboard[1][4] = FieldColor.PURPLE;
		this.gameboard[2][4] = FieldColor.BLUE;
		this.gameboard[3][4] = FieldColor.ORANGE;
		this.gameboard[4][4] = FieldColor.BROWN;
		this.gameboard[5][4] = FieldColor.GREEN;
		this.gameboard[6][4] = FieldColor.RED;
		this.gameboard[7][4] = FieldColor.YELLOW;
		
		this.gameboard[0][5] = FieldColor.GREEN;
		this.gameboard[1][5] = FieldColor.PINK;
		this.gameboard[2][5] = FieldColor.ORANGE;
		this.gameboard[3][5] = FieldColor.RED;
		this.gameboard[4][5] = FieldColor.PURPLE;
		this.gameboard[5][5] = FieldColor.BROWN;
		this.gameboard[6][5] = FieldColor.YELLOW;
		this.gameboard[7][5] = FieldColor.BLUE;
		
		this.gameboard[0][6] = FieldColor.RED;
		this.gameboard[1][6] = FieldColor.ORANGE;
		this.gameboard[2][6] = FieldColor.PINK;
		this.gameboard[3][6] = FieldColor.GREEN;
		this.gameboard[4][6] = FieldColor.BLUE;
		this.gameboard[5][6] = FieldColor.YELLOW;
		this.gameboard[6][6] = FieldColor.BROWN;
		this.gameboard[7][6] = FieldColor.PURPLE;
		
		this.gameboard[0][7] = FieldColor.ORANGE;
		this.gameboard[1][7] = FieldColor.BLUE;
		this.gameboard[2][7] = FieldColor.PURPLE;
		this.gameboard[3][7] = FieldColor.PINK;
		this.gameboard[4][7] = FieldColor.YELLOW;
		this.gameboard[5][7] = FieldColor.RED;
		this.gameboard[6][7] = FieldColor.GREEN;
		this.gameboard[7][7] = FieldColor.BROWN;
	}
	
	protected String getNamePlB() {
		return namePlB;
	}

	protected void setNamePlB(String namePlB) {
		this.namePlB = namePlB;
	}

	protected String getNamePlW() {
		return namePlW;
	}

	protected void setNamePlW(String namePlW) {
		this.namePlW = namePlW;
	}
	
	protected String getIP(){
		String ip = "Couldn't get IP - sorry";
        try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.warning(e.toString());
			e.printStackTrace();
		}
        return ip;
	}

}