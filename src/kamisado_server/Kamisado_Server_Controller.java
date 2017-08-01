package kamisado_server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Kamisado_Server_Controller {
	private Logger logger = Logger.getLogger("");
	Kamisado_Server_Model model;
	
	public Kamisado_Server_Controller(Kamisado_Server_Model model){
		this.model = model;
		
		
		model.connectClients();
	};
	
}