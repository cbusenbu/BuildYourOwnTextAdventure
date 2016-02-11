package edu.bsu.cs121.cwbusenburg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Player {
	private HashSet<Item> inventory = new HashSet<Item>();
	private HashSet<Room> roomsVisited = new HashSet<Room>();
	public Player() {
		
	}
	public void setInventory(HashSet<Item> inventory) {this.inventory = inventory;}
	public HashSet<Item> getInventory() {
		return inventory;
	}
	public void addToInventory(Item item) {
		inventory.add(item);
	}
	public void removeFromInventory(Item item){
		if (inventory.contains(item)){
			inventory.remove(item);
		}
		
	}
	public void addToRoomsVisited(Room room){
		roomsVisited.add(room);
	}
	public HashSet<Room> getRoomsVisited() {
		return roomsVisited;
	}
	public void printAvailableCommands(){
		if(!inventory.isEmpty()){
			for (Item items: inventory){
				System.out.println("DROP "+ items.getName());
			}
		}
	}
	public void setRoomsVisited(HashSet<Room> roomsVisited) {
		this.roomsVisited = roomsVisited;
	}
	public void printInventory() {
		if(inventory.isEmpty()){
			System.out.println("Sorry there are no items in your inventory.");
		}
		else{
			System.out.println("Items in Inventory:");
			for (Item items : inventory) {
				System.out.println(items.getName());
			}
		}
	}
	public void printCurrentScore(Room currentRoom){
		if(roomsVisited.contains(currentRoom)){
			System.out.println(roomsVisited.size()+ inventory.size());
		}
		else{
			System.out.println(roomsVisited.size()+inventory.size()+1);
		}

	}
	public int returnCurrentScore(Room currentRoom){
		return roomsVisited.size()+inventory.size();
	}
	
	

}
