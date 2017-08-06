package kamisado_client;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class numberCrunchingTest {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		JSONArray jsonArray = new JSONArray();
		
		JSONObject wbrown = new JSONObject();
		wbrown.put("tower", "wbrown");
		wbrown.put("xPos", 0);
		wbrown.put("yPos", 0);
		jsonArray.add(wbrown);
		
		JSONObject wgreen = new JSONObject();
		wgreen.put("tower", "wgreen");
		wgreen.put("xPos", 1);
		wgreen.put("yPos", 0);
		jsonArray.add(wgreen);
		
		JSONObject wred = new JSONObject();
		wred.put("tower", "wred");
		wred.put("xPos", 2);
		wred.put("yPos", 0);
		jsonArray.add(wred);
		
		JSONObject wyellow = new JSONObject();
		wyellow.put("tower", "wyellow");
		wyellow.put("xPos", 3);
		wyellow.put("yPos", 0);
		jsonArray.add(wyellow);
		
		JSONObject wpink = new JSONObject();
		wpink.put("tower", "wpink");
		wpink.put("xPos", 4);
		wpink.put("yPos", 0);
		jsonArray.add(wpink);
		
		JSONObject wpurple = new JSONObject();
		wpurple.put("tower", "wpurple");
		wpurple.put("xPos", 5);
		wpurple.put("yPos", 0);
		jsonArray.add(wpurple);
		
		JSONObject wblue = new JSONObject();
		wblue.put("tower", "wblue");
		wblue.put("xPos", 6);
		wblue.put("yPos", 0);
		jsonArray.add(wblue);
		
		JSONObject worange = new JSONObject();
		worange.put("tower", "worange");
		worange.put("xPos", 7);
		worange.put("yPos", 0);
		jsonArray.add(worange);
		
		JSONObject borange = new JSONObject();
		borange.put("tower", "borange");
		borange.put("xPos", 0);
		borange.put("yPos", 7);
		jsonArray.add(borange);
		
		JSONObject bblue = new JSONObject();
		bblue.put("tower", "bblue");
		bblue.put("xPos", 1);
		bblue.put("yPos", 7);
		jsonArray.add(bblue);
		
		JSONObject bpurple = new JSONObject();
		bpurple.put("tower", "bpurple");
		bpurple.put("xPos", 2);
		bpurple.put("yPos", 7);
		jsonArray.add(bpurple);
		
		JSONObject bpink = new JSONObject();
		bpink.put("tower", "bpink");
		bpink.put("xPos", 3);
		bpink.put("yPos", 7);
		jsonArray.add(bpink);
		
		JSONObject byellow = new JSONObject();
		byellow.put("tower", "byellow");
		byellow.put("xPos", 4);
		byellow.put("yPos", 7);
		jsonArray.add(byellow);
		
		JSONObject bred = new JSONObject();
		bred.put("tower", "bred");
		bred.put("xPos", 5);
		bred.put("yPos", 7);
		jsonArray.add(bred);
		
		JSONObject bgreen = new JSONObject();
		bgreen.put("tower", "bgreen");
		bgreen.put("xPos", 6);
		bgreen.put("yPos", 7);
		jsonArray.add(bgreen);
		
		JSONObject bbrown = new JSONObject();
		bbrown.put("tower", "bbrown");
		bbrown.put("xPos", 7);
		bbrown.put("yPos", 7);
		jsonArray.add(bbrown);
		
		Iterator<JSONObject> iterator = jsonArray.iterator();
		
		//Give out unchanged numbers
//		while(iterator.hasNext()) {
//			JSONObject json = iterator.next();
//			String tower = (String) json.get("tower");
//			int x = (int) json.get("xPos");
//			int y = (int) json.get("yPos");
//			System.out.println(tower + " at " + x + " " + y);
//		}
		
		//Give out numbers changed for black player to grid
		while(iterator.hasNext()) {
			JSONObject json = iterator.next();
			String tower = (String) json.get("tower");
			int x = crunchNumbers((int) json.get("xPos"));
			int y = (int) json.get("yPos");
			System.out.println(tower + " at " + x + " " + y);
		}
		
		//Give out numbers changed for white player to grid
//		while(iterator.hasNext()) {
//			JSONObject json = iterator.next();
//			String tower = (String) json.get("tower");
//			int x = (int) json.get("xPos");
//			int y = crunchForBlackPlayer((int) json.get("yPos"));
//			System.out.println(tower + " at " + x + " " + y);
//		}
		
	}
	
	public static int crunchNumbers(int number){
		switch (number) {
			case 0 : number = 7;
				break;
			case 1 : number = 6;
				break;
			case 2 : number = 5;
				break;
			case 3 : number = 4;
				break;
			case 4 : number = 3;
				break;
			case 5 : number = 2;
				break;
			case 6 : number = 1;
				break;
			case 7 : number = 0;
				break;
			default : number = -1;
				break;
		}
		return number;		
	}
}
