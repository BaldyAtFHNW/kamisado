package kamisado_client;

import javafx.application.Platform;

public class Kamisado_Client_Controller {
	final private Kamisado_Client_Model model;
	final private Kamisado_Client_View view;
	
	protected Kamisado_Client_Controller(Kamisado_Client_Model model, Kamisado_Client_View view){
		this.model = model;
		this.view = view;
		
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
