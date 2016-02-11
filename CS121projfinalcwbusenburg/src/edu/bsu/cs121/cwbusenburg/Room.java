package edu.bsu.cs121.cwbusenburg;

import java.util.HashSet;

public class Room {
	private String description = null;
	private HashSet<Item> objects = new HashSet<Item>();
	private String roomDescriptionAll = null;
	private String roomDescriptionPreAction = null;
	private String roomDescriptionPostAction = null;
	private String roomDescriptionFirst = null;
	private String roomWinDescription = null;
	private Item originalObject;
	private int NUMBEROFITEMS;
	private Player player;

	public Room(int numberOfObjects) {
		this.NUMBEROFITEMS = numberOfObjects;
	}
	public void setOriginalObject(Item originalObject) {
		this.originalObject = originalObject;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setObjects(HashSet<Item> objects) {
		this.objects = objects;
	}
	public String getRoomWinDescription() {
		return roomWinDescription;
	}
	public void setRoomDescriptionAll(String roomDescriptionAll) {
		this.roomDescriptionAll = roomDescriptionAll;
	}
	public void setRoomDescriptionPreAction(String roomDescriptionPreAction) {
		this.roomDescriptionPreAction = roomDescriptionPreAction;
	}
	public void setRoomDescriptionPostAction(String roomDescriptionPostAction) {
		this.roomDescriptionPostAction = roomDescriptionPostAction;
	}
	public void setRoomDescriptionFirst(String roomDescriptionFirst) {
		this.roomDescriptionFirst = roomDescriptionFirst;
	}
	public void setRoomWinDescription(String roomWinDescription) {
		this.roomWinDescription = roomWinDescription;
	}
	public HashSet<Item> getObjects() {
		return objects;
	}
	public void addObjectToRoom(Item objectToAdd) {
		objects.add(objectToAdd);
	}
	public void removeObjectFromRoom(Item objectToRemove){
		if (objects.contains(objectToRemove)){
			objects.remove(objectToRemove);
		}
	}
	public void printAvailableCommands(){
		if(!objects.isEmpty()){
			for(Item items :objects){
				System.out.println("TAKE "+items.getName());
			}
		}


	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Room room = (Room) o;

		if (!description.equals(room.description)) return false;
		if (!roomDescriptionAll.equals(room.roomDescriptionAll)) return false;
		if (!roomDescriptionPreAction.equals(room.roomDescriptionPreAction)) return false;
		if (!roomDescriptionPostAction.equals(room.roomDescriptionPostAction)) return false;
		if (!roomDescriptionFirst.equals(room.roomDescriptionFirst)) return false;
		return roomWinDescription.equals(room.roomWinDescription);

	}
	public String getRoomDescription(Player hero) {
		String stringToPrint = roomDescriptionAll;
		if (!hero.getRoomsVisited().contains(this)) {
			stringToPrint += roomDescriptionFirst;
		}
		if (hero.getRoomsVisited().contains(this)){
			stringToPrint+= description;
		}
		if (this.objects.contains(originalObject)) {
			stringToPrint += roomDescriptionPreAction;
		} else {
			stringToPrint += roomDescriptionPostAction;
		}
		if (this.objects.size() >= NUMBEROFITEMS) {
			stringToPrint += roomWinDescription;
		}
		return stringToPrint;
	}
}
