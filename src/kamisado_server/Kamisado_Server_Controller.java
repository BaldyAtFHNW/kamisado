package kamisado_server;

import java.util.logging.Logger;

public class Kamisado_Server_Controller{
	private Logger logger = Logger.getLogger("");
	Kamisado_Server_Model model;
	private boolean running = true;
	
	public Kamisado_Server_Controller(Kamisado_Server_Model model){
		this.model = model;
		
		
		model.connectClients();
		model.send("Hi, Player1", true);
		model.send("Hi, Player2", false);
		
		//init the game and afterwards only react

		while(running){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				logger.info(e.toString());
				e.printStackTrace();
			}
			if(model.msgPendingPlayer1()){
				processMessage(model.getMsgPlayer1(), true);
			}else if(model.msgPendingPlayer2()){
				processMessage(model.getMsgPlayer2(), false);
			}
		}
		
	}
	
	
	private void processMessage(String msg, boolean player1){		
		logger.info(player1 ? "Player1: " + msg : "Player2: " + msg);
	}
	
}