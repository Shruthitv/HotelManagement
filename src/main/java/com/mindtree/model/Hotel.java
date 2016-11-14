package com.mindtree.model;

public class Hotel {
	
	private int id;
	private String hotelName;
	private String city;
	private int noOfRoomsAvailable;
	private int price;
	
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getNoOfRoomsAvailable() {
		return noOfRoomsAvailable;
	}
	public void setNoOfRoomsAvailable(int noOfRoomsAvailable) {
		this.noOfRoomsAvailable = noOfRoomsAvailable;
	}
}
