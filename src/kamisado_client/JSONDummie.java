package kamisado_client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class JSONDummie {
	private JSONObject obj;
	
	public JSONDummie(){
	
		obj = new JSONObject();
		obj.put("name", "Simon");
		obj.put("location", "Itingen");
		
		JSONArray list = new JSONArray();
		list.add("something");
		list.add("somethign2");
		list.add("somtehing3");
	
	}
	
	public JSONObject simulateInit(){
		obj.put("type", "init");
		obj.put("black", true);
		obj.put("start", true);
		
		return new JSONObject();
	}
	
	public JSONObject simulateMove(){
		obj.put("type", "move");
		obj.put("towerColor", "WBLUE");
		obj.put("xPos", 6);
		obj.put("yPos", 5);
		
		return new JSONObject();
	}
	
	public JSONObject simulateRequestMove(){
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
		
		return new JSONObject();
	}
	
	public JSONObject simulateEnd(){
		obj.put("type", "end");
		obj.put("won", true);
		obj.put("reason", "surrender");
		
		return new JSONObject();
	}
	
	public JSONObject simulateReset(){
		obj.put("type", "reset");
		obj.put("confirmed", false);
		
		return new JSONObject();
	}
}
