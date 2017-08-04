package kamisado_server;

import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Kamisado_Server_Model{
	Logger logger = Logger.getLogger("");
	
	final int boardSize = 8;
	FieldColor[][] gameboard;
	TowerColor[][] towerPositions;;
	
	private Socket socketPlayer1;
	private Socket socketPlayer2;	
	
	private ArrayList<String> msgsPlayer1 = new ArrayList<String>();
	private ArrayList<String> msgsPlayer2 = new ArrayList<String>();
	
	public Kamisado_Server_Model(){
		//empty constructor can be deleted if not needed in the end
	}
	
	public void connectClients(){

		try{
			ServerSocket listener = new ServerSocket(50000, 10, null);
			
			this.socketPlayer1 = listener.accept();
			logger.info("Player1 connected");
			Kamisado_Server_ClientThread clientPlayer1 = new Kamisado_Server_ClientThread(Kamisado_Server_Model.this, socketPlayer1, true);
			new Thread(clientPlayer1).start();			
			
			this.socketPlayer2 = listener.accept();
			logger.info("Player2 connected");
			Kamisado_Server_ClientThread clientPlayer2 = new Kamisado_Server_ClientThread(Kamisado_Server_Model.this, socketPlayer2, false);
			new Thread(clientPlayer2).start();
			
			listener.close();
			
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
		}
	
	}
	
	public void newMsgPlayer1(String msg){
		this.msgsPlayer1.add(msg);
	}
	
	public void newMsgPlayer2(String msg){
		this.msgsPlayer2.add(msg);
	}
	
	public String getMsgPlayer1(){
		if (msgsPlayer1.size() == 0){
			return null;
		}else{
			String msg = msgsPlayer1.get(0);
			msgsPlayer1.remove(0);
			return msg;
		}
	}
	
	public String getMsgPlayer2(){
		if (msgsPlayer2.size() == 0){
			return null;
		}else{
			String msg = msgsPlayer2.get(0);
			msgsPlayer2.remove(0);
			return msg;
		}
	}
	
	public boolean msgPendingPlayer1(){
		if (msgsPlayer1.size() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean msgPendingPlayer2(){
		if (msgsPlayer2.size() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	public void sendToPl1(String msg){
		logger.info("Sending to p1");
		OutputStreamWriter out;
		try{
			out = new OutputStreamWriter(this.socketPlayer1.getOutputStream());
			out.write(msg + "\n");
			out.flush();
		}catch(Exception e){
			
		}
	}
	
	public void sendToPl2(String msg){
		logger.info("Sending to p2");
		OutputStreamWriter out;
		try{
			out = new OutputStreamWriter(this.socketPlayer2.getOutputStream());
			out.write(msg + "\n");
			out.flush();
		}catch(Exception e){
			
		}
	}

	/*public void send(String msg, int player){
		try{
			if(player == 1){
				logger.info("Sending to p1");
				this.outpStrPl1.write(msg + "\n");
				this.outpStrPl1.flush();
			}else if(player == 2){
				logger.info("Sending to p2");
				this.outpStrPl2.write(msg + "\n");
				this.outpStrPl2.flush();
			}
		}catch(Exception e){
			logger.warning(e.toString());
			e.printStackTrace();
		}
	}*/
	
	public void initTowers(){
		
		this.towerPositions = new TowerColor[boardSize][boardSize];
		
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