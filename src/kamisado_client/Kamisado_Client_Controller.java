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
		
		model.newestMsg.addListener( (o, oldValue, newValue) -> processMsg(newValue));
		model.connectServer();
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
	
	private void processMsg(String msg) {
		logger.info(msg);
//		String msg = model.getNewMsg();
//		String type = new String();
//		JSONParser parser = new JSONParser();
//		
//		try {
//			Object obj = parser.parse(msg);
//			JSONObject json = (JSONObject) obj;
//			type = (String) json.get("type");
//			logger.info("Type: " + type);
//		} catch (Exception e) {
//			logger.warning(e.toString());
//			e.printStackTrace();
//		}
		
//		switch (type) {
//	        case "init":	logger.info("React to init");
//	        	break;
//	        case "move":	logger.info("React to move");//react to move
//	        	break;
//	        case "reset":	logger.info("React to reset");
//	        	break;
//	        default: logger.warning("Invalid Type");
//		}
		
	}
	
}
