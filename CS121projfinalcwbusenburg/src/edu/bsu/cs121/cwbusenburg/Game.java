package edu.bsu.cs121.cwbusenburg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;


public class Game {
	private static final HashMap<Room,Room> eastMap = new HashMap<Room,Room>();
	private static final HashMap<Room,Room> westMap = new HashMap<Room,Room>();
	private static final HashMap<Room,Room> northMap = new HashMap<Room,Room>();
	private static final HashMap<Room,Room> southMap = new HashMap<Room,Room>();
	
	//If anything needs to change, make sure to do it from this line 12 - 18
	private static final int[] eastMapIndicesArray = {5,2,3,5,0,8,-1,5,-1,8};//Indices Array are a primitive form of mapping, where the index is the room, and the value is the room to be places in the value
	private static final int[] westMapIndicesArray = {4,1,1,-1,6,0,4,-1,9,-1};
	private static final int[] northMapIndicesArray = {2,-1,-1,-1,-1,3,1,-1,5,-1};
	private static final int[] southMapIndicesArray = {7,1,0,5,-1,-1,-1,-1,-1,-1};
	private static final int NUMBEROFROOMS = 10; //the correct number of rooms will be created given this number
	private static final int NUMBEROFOBJECTS = 5; //the correct number of Items will be created given this number
	
	private static final HashMap<Integer,Integer> eastMapIndices = new HashMap<Integer,Integer>();
	private static final HashMap<Integer,Integer> westMapIndices = new HashMap<Integer,Integer>();
	private static final HashMap<Integer,Integer> northMapIndices = new HashMap<Integer,Integer>();
	private static final HashMap<Integer,Integer> southMapIndices = new HashMap<Integer,Integer>();
	
	private static final ArrayList<Item> itemList = new ArrayList<Item>();//{this will currently hold items in this assigned order{leg,wing,hair,eye,toe}
	private static final ArrayList<Room> rooms = new ArrayList<Room>();
	private static Player hero = new Player();
	private static boolean saveValue = false;
	private static Room currentRoom;

	static{
		saveValue = askUserForSavedGame();
		createMapIndices();
		generateRooms();
		generateItems();
		generatePlayer();
		generateDirectionalMaps();
		currentRoom = rooms.get(0);
	}
	public static void main(String[] args) {
		boolean winCondition = false;
		while(winCondition!=true){
			Room tempCompareRoom = currentRoom;
			printCurrentRoomDescription(currentRoom);
			while(currentRoom.equals(tempCompareRoom)){
				performNextAction(askForNextAction(),currentRoom);
				winCheck(currentRoom);
			}
		}


	}
	private static void winCheck(Room currentRoom){
		if(currentRoom.getRoomWinDescription()!=""){
			if(currentRoom.getObjects().size()>=NUMBEROFOBJECTS){
				System.out.println(currentRoom.getRoomWinDescription());
				System.out.println("You have won with a score of :"+hero.returnCurrentScore(currentRoom)+NUMBEROFOBJECTS);
				exitGame();
			}
		}
	}

	private static void performNextAction(String nextAction,Room currentRoom){

		String nextActionLower = nextAction.toLowerCase();

		switch(nextActionLower){
			case "north":
			case "go north":
			case "move north":moveNorth();
				break;
			case "south":
			case "go south":
			case "move south":moveSouth();
				break;
			case "east":
			case "go east":
			case "move east":moveEast();
				break;
			case "west":
			case "go west":
			case "move west":moveWest();
				break;
			case "help":printAvailableCommands();
				break;
			case "score": hero.printCurrentScore(currentRoom);
				break;
			case "look": System.out.println(currentRoom.getRoomDescription(hero));
				break;
			case "inventory": hero.printInventory();
				break;
			/*case "save":saveGame();
				break;
			*/
			case "exit":exitGame();
			default:
				if(nextActionLower.contains("take")){
					takeItemFromCurrentRoom(nextActionLower);
				}
				else if(nextActionLower.contains("drop")){
					dropItemIntoCurrentRoom(nextActionLower);
				}
				else {
					System.out.println("Sorry that is not a valid command, please try again.");
				}
		}
	}
	private static void takeItemFromCurrentRoom(String nextAction){
		nextAction = nextAction.replace("take", "");
		nextAction = nextAction.trim();
		if(!currentRoom.getObjects().isEmpty()){
			for(Item items:currentRoom.getObjects()){
				if(items.getName().equalsIgnoreCase(nextAction)){
					hero.addToInventory(items);
					currentRoom.removeObjectFromRoom(items);
				}
				else{
					System.out.println("That item is not in this room, or may not even exist");
				}
			}
		}
		else{
			System.out.println("There are no items in this room");
		}

	}
	private static void dropItemIntoCurrentRoom(String nextAction) {
		nextAction = nextAction.replace("drop", "");
		nextAction = nextAction.trim();
		if (!hero.getInventory().isEmpty()) {
			HashSet<Item> inventory = hero.getInventory();
			Item tempItem = null;
			for (Item items : inventory) {

				if (items.getName().equalsIgnoreCase(nextAction)) {
					currentRoom.addObjectToRoom(items);
					tempItem = items;
				}
			}
			if (tempItem != null) {
				hero.removeFromInventory(tempItem);
			}
			else {
				System.out.println("That item is not in your inventory, or may not even exist");
				System.out.println("Please Try Again.");
			}
		}
		else {
			System.out.println("There are no items in your inventory.");
			System.out.println("Please Try Again.");
		}
	}
	private static void printAvailableCommands(){
		System.out.println("Currently Available Commands:");
		currentRoom.printAvailableCommands();
		hero.printAvailableCommands();
		if(northMap.containsKey(currentRoom)){
			System.out.println("NORTH, GO NORTH, MOVE NORTH");
		}
		if(southMap.containsKey(currentRoom)){
			System.out.println("SOUTH, GO SOUTH, MOVE SOUTH");
		}
		if(eastMap.containsKey(currentRoom)){
			System.out.println("EAST, GO EAST, MOVE EAST");
		}
		if(westMap.containsKey(currentRoom)){
			System.out.println("WEST, GO WEST, MOVE WEST");
		}
		System.out.println("INVENTORY");
		System.out.println("LOOK");
		System.out.println("HELP");
		System.out.println("SCORE");
		//System.out.println("SAVE");
		System.out.println("EXIT");
	}
	private static void exitGame(){
		//Scanner kb = new Scanner(System.in);
		//System.out.println("Would you like to save your game?(y for yes),(n for no)");
		//String gameSave = kb.nextLine();
		/*
		if(gameSave.equalsIgnoreCase("y")){
			saveGame();
		}
		else if (gameSave.equalsIgnoreCase("n")){
		*/
			System.out.println("Thanks for Playing!");
			System.exit(0);
		/*
		else {
			System.out.println("That is not a correct value, please try again");
			exitGame();
		}
		*/

	}
/*
	private static void saveGame(){

	}
	*/
	private static void moveNorth(){
		if (northMap.containsKey(currentRoom)){
			hero.addToRoomsVisited(currentRoom);
			currentRoom = northMap.get(currentRoom);

		}
		else{
			System.out.println("This room does not have a room to the North. Please try again");
		}

	}
	private static void moveSouth(){
		if (southMap.containsKey(currentRoom)){
			hero.addToRoomsVisited(currentRoom);
			currentRoom = southMap.get(currentRoom);
		}
		else{
			System.out.println("This room does not have a room to the South. Please try again");
		}

	}
	private static void moveEast(){
		if (eastMap.containsKey(currentRoom)){
			hero.addToRoomsVisited(currentRoom);
			currentRoom = eastMap.get(currentRoom);
		}
		else{
			System.out.println("This room does not have a room to the East. Please try again");
		}

	}
	private static void moveWest(){
		if (westMap.containsKey(currentRoom)){
			hero.addToRoomsVisited(currentRoom);
			currentRoom = westMap.get(currentRoom);
		}
		else{
			System.out.println("This room does not have a room to the West. Please try again");
		}

	}
	private static String askForNextAction(){
		Scanner kb = new Scanner(System.in);
		System.out.println("What would you like to do?");
		return kb.nextLine();
	}
	private static void printCurrentRoomDescription(Room currentRoom){
		System.out.println(currentRoom.getRoomDescription(hero));
	}
	private static boolean askUserForSavedGame(){
		System.out.println("Welcome to your text-based adventure.");
		System.out.println("Would you like to start a new game?");
		System.out.println("(N for New Game)");
		Scanner kb = new Scanner(System.in);
		String input = kb.next();
		if (input.equalsIgnoreCase("n")){
			return false;
		}
		/*
		else if(input.equalsIgnoreCase("s")){
			return true;
		}*/
		else{
			System.out.println("Sorry, that is an invalid input, please try again.");
			return askUserForSavedGame();
		}

	}
	private static void generatePlayer(){
		parsePlayerXML((saveValue) ? "playerSave.xml" : "player.xml");
	}
	private static void parsePlayerXML(String fileLocation){
		File xmlFile = new File(fileLocation);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document document = null;
		try {
			document = builder.parse(xmlFile);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		NodeList nList = document.getElementsByTagName("player");
		for (int i = 0; i <NUMBEROFOBJECTS;i++){
			Node nNode = nList.item(0);

			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element eElement = (Element) nNode;

				if(!eElement.getElementsByTagName("itemIndex").item(i).getTextContent().equals("-1")){
					HashSet<Item> tempInventory = hero.getInventory();
					tempInventory.add(itemList.get(Integer.parseInt(eElement.getElementsByTagName("itemIndex").item(i).getTextContent())));
					hero.setInventory(tempInventory);
				}




			}
		}

	}
	private static void parseItemsXML(String fileLocation){
		File xmlFile = new File(fileLocation);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document document = null;
		try {
			document = builder.parse(xmlFile);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		NodeList nList = document.getElementsByTagName("Item");
		for (int i = 0; i <nList.getLength();i++){
			Node nNode = nList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element eElement = (Element) nNode;
				int tempIndex = Integer.parseInt(eElement.getAttribute("id"));

				itemList.get(tempIndex).setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
				itemList.get(tempIndex).setName(eElement.getElementsByTagName("Name").item(0).getTextContent());
				itemList.get(tempIndex).setItemLocation((Integer.parseInt(eElement.getElementsByTagName("roomLocation").item(0).getTextContent())));
			}
		}

	}
	private static void parseRoomsXML(String fileLocation){
		File xmlFile = new File(fileLocation);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document document = null;
		try {
			document = builder.parse(xmlFile);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		NodeList nList = document.getElementsByTagName("room");
		for (int i = 0; i <nList.getLength();i++){
			Node nNode = nList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element eElement = (Element) nNode;
				int tempIndex = Integer.parseInt(eElement.getAttribute("id"));

				rooms.get(tempIndex).setRoomDescriptionAll(eElement.getElementsByTagName("roomdescriptionall").item(0).getTextContent());
				rooms.get(tempIndex).setRoomWinDescription(eElement.getElementsByTagName("roomWindescription").item(0).getTextContent());
				rooms.get(tempIndex).setRoomDescriptionPostAction(eElement.getElementsByTagName("roomdescriptionpostaction").item(0).getTextContent());
				rooms.get(tempIndex).setRoomDescriptionPreAction(eElement.getElementsByTagName("roomdescriptionpreaction").item(0).getTextContent());
				rooms.get(tempIndex).setRoomDescriptionFirst(eElement.getElementsByTagName("roomdescriptionfirst").item(0).getTextContent());
				rooms.get(tempIndex).setDescription(eElement.getElementsByTagName("roomdescription").item(0).getTextContent());

			}
		}

	}
	private static void generateItems(){//current number of Item instances stored into an arraylist for easy access
		for (int i = 0; i <NUMBEROFOBJECTS ; i++){
			itemList.add(new Item());
			
		}
		parseItemsXML((saveValue) ? "ItemsSave.xml" : "Items.xml");
		addItemsToStartingRooms();
	}
	private static void addItemsToStartingRooms(){
		for ( Item item: itemList){
			HashSet<Item> temp = rooms.get(item.getItemLocation()).getObjects();
			temp.add(item);
			rooms.get(item.getItemLocation()).setOriginalObject(item);
			rooms.get(item.getItemLocation()).setObjects(temp);
		}
	}
	private static void generateRooms(){
		for( int i = 0; i < NUMBEROFROOMS; i++){
			rooms.add(new Room(NUMBEROFOBJECTS));
		}
		parseRoomsXML("rooms.xml");
	}
	private static void generateDirectionalMaps(){
		generateDirectionalMap(eastMap,eastMapIndices);
		generateDirectionalMap(westMap,westMapIndices);
		generateDirectionalMap(northMap,northMapIndices);
		generateDirectionalMap(southMap,southMapIndices);
	}
	private static void generateDirectionalMap(HashMap<Room,Room> newMap,HashMap<Integer,Integer> newMapIndices ){
		for(HashMap.Entry<Integer,Integer> index : newMapIndices.entrySet()){
			newMap.put(rooms.get(index.getKey()),rooms.get(index.getValue()));
		}
	}
	private static void createMapIndices(){
		createDirectionalMapIndices(eastMapIndices,eastMapIndicesArray);
		createDirectionalMapIndices(westMapIndices,westMapIndicesArray);
		createDirectionalMapIndices(northMapIndices,northMapIndicesArray);
		createDirectionalMapIndices(southMapIndices,southMapIndicesArray);
		
	}
	private static void createDirectionalMapIndices(HashMap <Integer,Integer> directionalMapIndices,int[] directionalMapIndicesArray ){
		
		for( int i = 0 ; i < NUMBEROFROOMS; i++){
			if( directionalMapIndicesArray[i] != -1){
				directionalMapIndices.put(i, directionalMapIndicesArray[i]);
			}
		}
		
		
	}
}

