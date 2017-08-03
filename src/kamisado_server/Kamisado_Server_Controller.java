package kamisado_server;

import java.util.logging.Logger;

public class Kamisado_Server_Controller{
	private Logger logger = Logger.getLogger("");
	private Kamisado_Server_Model model;
	
	public Kamisado_Server_Controller(Kamisado_Server_Model model){
		this.model = model;
		
		
		model.connectClients();
		model.initGameBoard();
		model.initTowers();

		
		model.sendToPl1("To Player 1");
		model.sendToPl2("To Player 2");
		
		model.sendToPl1("To Player 1, again");
		model.sendToPl2("To Player 2, again");
		
		//init the game and afterwards only react

		while(true){
			if(model.msgPendingPlayer1()){
				processMessage(model.getMsgPlayer1(), 1);
			}else if(model.msgPendingPlayer2()){
				processMessage(model.getMsgPlayer2(), 2);
			}
		}
		
	}
	
	
	private void processMessage(String msg, int i){
		logger.info("Player" + i + ": " + msg);
	}

	
	
	
	
	
}