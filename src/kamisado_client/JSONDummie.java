package kamisado_client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class JSONDummie {
	
	/*
	Hey Liz
	This is the DummieClass I told you about
	Put an observer on the variable obj just below and simulate a server response 
	by calling one of the methods below (they then fill the obj variable)
	
	In case you'd like to try it, I've put a little test into "Kamisado_Client_Main"
	It creates this dummie and reads out the JSON-Object for "requestMove"
	
	Don't forget to call clearJSONObject() if you'd like to simulate several communications after each other
	*/
	
	
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
