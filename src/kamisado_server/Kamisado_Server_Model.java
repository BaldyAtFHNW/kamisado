package kamisado_server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.beans.property.SimpleStringProperty;

public class Kamisado_Server_Model{
	Logger logger = Logger.getLogger("");
	
	final int boardSize = 8;
	private FieldColor[][] gameboard;
	private TowerColor[][] towerPositions;;
	
	private Socket socketPlayerBlack;
	private Socket socketPlayerWhite;	
	
	protected SimpleStringProperty newestMsgPlBlack = new SimpleStringProperty();
	protected SimpleStringProperty newestMsgPlWhite = new SimpleStringProperty();
	
	public Kamisado_Server_Model(){
		//empty constructor can be deleted if not needed in the end
	}
	
	public void connectClients(){

		try{
			ServerSocket listener = new ServerSocket(50000, 10, null);
			
			this.socketPlayerBlack = listener.accept();
			logger.info("Player1 (black) connected");
			Kamisado_Server_ClientThread clientPlayerBlack = new Kamisado_Server_ClientThread(Kamisado_Server_Model.this, socketPlayerBlack, true);
			new Thread(clientPlayerBlack).start();			
			
			this.socketPlayerWhite = listener.accept();
			logger.info("Player2 (white) connected");
			Kamisado_Server_ClientThread clientPlayerWhite = new Kamisado_Server_ClientThread(Kamisado_Server_Model.this, socketPlayerWhite, false);
			new Thread(clientPlayerWhite).start();
			
			listener.close();
			
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
		}
	
	}
	
	public void initGame(){
		this.initGameBoard();
		this.initTowers();
		
		JSONObject initPlayerBlack = new JSONObject();
		initPlayerBlack.put("type", "init");
		initPlayerBlack.put("black", true);
		initPlayerBlack.put("start", true);
		
		JSONObject initPlayerWhite = new JSONObject();
		initPlayerWhite.put("type", "init");
		initPlayerWhite.put("black", false);
		initPlayerWhite.put("start", false);
		
		this.send(initPlayerBlack.toString(), 'B');
		this.send(initPlayerWhite.toString(), 'W');		
	}

	public void send(String msg, char plCol){
		OutputStreamWriter out;
		try {
			if(plCol == 'B'){
				out = new OutputStreamWriter(this.socketPlayerBlack.getOutputStream());
				out.write(msg + "\n");
				out.flush();
			}else if(plCol == 'W'){
				out = new OutputStreamWriter(this.socketPlayerWhite.getOutputStream());
				out.write(msg + "\n");
				out.flush();
			}else {
				logger.warning("Receiver does not exist!");
			}
		} catch (Exception e) {
			logger.info(e.toString());
			e.printStackTrace();
		}
	}
	
	public void printTowerPos(TowerColor towerColor) {
		Integer[] pos = this.getTowerPos(towerColor);
		logger.info(towerColor.toString() + " is located at: " + pos[0] + " " + pos[1]);
	}
	
	public Integer[] getTowerPos(TowerColor towerColor) {
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
	
	public void moveTower(TowerColor towerColor, int xPos, int yPos) {
		//logger.info("Moving " + towerColor.toString() + " to: " + xPos + " " + yPos);
		
		//Remove the tower from old position
		int x = getTowerPos(towerColor)[0];
		int y = getTowerPos(towerColor)[1];
		this.towerPositions[x][y] = null;

		//Put tower to new positions
		if(this.towerPositions[xPos][yPos] != null) {
			logger.warning("Field already taken!");
		}else {this.towerPositions[xPos][yPos] = towerColor;}
	}
	
	public FieldColor getFieldColor(int xPos, int yPos) {
		return this.gameboard[xPos][yPos];
	}
	
	public JSONArray getPossibleMoves(TowerColor towerColor) {
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
	
	public JSONObject parseJSON(String msg){
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
	
	public void initTowers(){
		
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
	
	public void initGameBoard(){
		
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

}