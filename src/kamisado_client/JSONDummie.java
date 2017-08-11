package kamisado_client;

/**
 * 
 * 	@author Simon Bieri
 * 	@date 2017-07-21
 *	@deprecated
 *	
 *	This class was only used for testing. It simulated server communications before the server was actually ready.
 *
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class JSONDummie {	
	
	private JSONObject obj;
	
	public JSONDummie(){
		this.obj = new JSONObject();
	}
	
	public JSONObject getJSONObject(){
		return this.obj;
	}
	
	public void clearJSONObject(){
		this.obj.clear();
	}
	

	//Different Methods for filling the Object from here:
	
	@SuppressWarnings("unchecked")
	public void simulateInit(){
		obj.put("type", "init");
		obj.put("black", true);
		obj.put("start", true);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject simulateMove(){
		obj.put("type", "move");
		obj.put("towerColor", "WBLUE");
		obj.put("xPos", 6);
		obj.put("yPos", 5);
		
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public void simulateRequestMove(){
		obj.put("type", "requestMove");
		obj.put("movedTower", "WYellow");
		obj.put("xPos", 4);
		obj.put("yPos", 6);
		obj.put("nextTower", "WBlue");
		
		JSONObject a = new JSONObject();
		a.put("xPos", 4);
		a.put("yPos", 7);
		
		JSONObject b = new JSONObject();
		b.put("xPos", 5);
		b.put("yPos", 6);
		
		JSONObject c = new JSONObject();
		c.put("xPos", 6);
		c.put("yPos", 6);
		
		JSONObject d = new JSONObject();
		d.put("xPos", 7);
		d.put("yPos", 6);
		
		JSONArray possibleMoves = new JSONArray();
		possibleMoves.add(a);
		possibleMoves.add(b);
		possibleMoves.add(c);
		possibleMoves.add(d);
		
		obj.put("possibleMoves", possibleMoves);
	}
	
	@SuppressWarnings("unchecked")
	public void simulateEnd(){
		obj.put("type", "end");
		obj.put("won", true);
		obj.put("reason", "surrender");
	}
	
	@SuppressWarnings("unchecked")
	public void simulateReset(){
		obj.put("type", "reset");
		obj.put("confirmed", false);
	}
}
