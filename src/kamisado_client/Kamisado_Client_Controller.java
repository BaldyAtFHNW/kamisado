package kamisado_client;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.application.Platform;

public class Kamisado_Client_Controller {
	private Logger logger = Logger.getLogger("");
	final private Kamisado_Client_Model model;
	final private Kamisado_Client_View view;
	private boolean running = true;	
	
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
		
		model.newestMsg.addListener( (o, oldValue, newValue) -> processMsg(newValue));
		model.connectServer();

	}
	
	private void processMsg(String msg) {
		JSONObject json = model.parseJSON(msg);
		String type = (String) json.get("type");
		
		switch (type) {
	        case "init":			processInit(json);
	        	break;
	        case "requestMove":		processRequestMove(json);
	        	break;
	        case "end":				processEnd(json);
	    		break;
	        case "reset":			processReset(json);
	        	break;
	        default: 				logger.warning("Invalid Type");
		}
	}
	
	private void processInit(JSONObject json) {
		model.black = (boolean) json.get("black");
		if((boolean) json.get("start")) {
			view.firstMove();
		}
	}
	
	private void processRequestMove(JSONObject json){
		
	}
	
	private void processEnd(JSONObject json){
		
	}
	
	private void processReset(JSONObject json){
		
	}	
}
