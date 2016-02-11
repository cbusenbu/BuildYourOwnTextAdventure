package edu.bsu.cs121.cwbusenburg;

public class Item {
	private String name;
	private String description;
	private int itemLocation;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Item item = (Item) o;

		if (!name.equals(item.name)) return false;
		return description.equals(item.description);

	}

	public int getItemLocation() {

		return itemLocation;
	}

	public void setItemLocation(int itemLocation) {
		this.itemLocation = itemLocation;
	}

	public Item(){
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString(){return (name+ " "+description);}

	///Still need to create a way to read the textFile address into some sort of String.
	
	

}
