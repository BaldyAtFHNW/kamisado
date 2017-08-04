package kamisado_server;

import java.util.logging.Logger;

public class Kamisado_Server_Controller{
	private Logger logger = Logger.getLogger("");
	Kamisado_Server_Model model;
	private boolean running = true;
	
	public Kamisado_Server_Controller(Kamisado_Server_Model model){
		this.model = model;
		
		model.newestMsgP1.addListener( (o, oldValue, newValue) -> processMsgP1(newValue));
		model.newestMsgP2.addListener( (o, oldValue, newValue) -> processMsgP2(newValue));
		model.connectClients();
		//model.send("This is Player1", true);
		//model.send("This is Player2", false);

		
		model.initGame();
		//init the game and afterwards only react
		
	}
	
	
	private void processMsgP1(String msg){		
		logger.info(msg);
	}
	
	private void processMsgP2(String msg){		
		logger.info(msg);
	}
	
}