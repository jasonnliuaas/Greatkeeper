package com.newline.housekeeper.model;

import java.io.Serializable;

public class HouseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2980373752464049250L;
	
	private String saleid;
	private String room;
	private String title;
	private String price;
	private String address;
	private String category;
	private String description;
	private String photo;
	private String usablearea;
	
	public String getSaleid() {
		return saleid;
	}
	public void setSaleid(String saleid) {
		this.saleid = saleid;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getUsablearea() {
		return usablearea;
	}
	public void setUsablearea(String usablearea) {
		this.usablearea = usablearea;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
