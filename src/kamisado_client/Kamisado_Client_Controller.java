package kamisado_client;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Logger;

import javafx.application.Platform;

public class Kamisado_Client_Controller {
	private Logger logger = Logger.getLogger("");
	final private Kamisado_Client_Model model;
	final private Kamisado_Client_View view;
	
	
	protected Kamisado_Client_Controller(Kamisado_Client_Model model, Kamisado_Client_View view){
		this.model = model;
		this.view = view;
		
		model.connectServer();
		model.send("Haaaai");
		model.send("Haaaai");
		model.send("Haaaai");
		model.send("Haaaai");
		model.send("Haaaai");
		model.send("Haaaai");
		model.send("Haaaai");
		model.send("Haaaai");
		model.send("Haaaai");
		model.send("Haaaai");
		
		// Example how to register for View events
		view.btnClick.setOnAction((event) -> {
			//Do Stuff
		});
		
		view.getStage().setOnCloseRequest((event)->{
			view.stop();
			Platform.exit();
		});
	}
	

	

	
}
