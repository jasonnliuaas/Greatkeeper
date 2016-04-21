package com.newline.housekeeper.model;

import java.io.Serializable;
import java.util.List;

public class CountryBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -974659980893268637L;
	
	private String countryid;
	private String countryname;
	private List<CityBean> cities;
	public String getCountryid() {
		return countryid;
	}
	public void setCountryid(String countryid) {
		this.countryid = countryid;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public List<CityBean> getCities() {
		return cities;
	}
	public void setCities(List<CityBean> cities) {
		this.cities = cities;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
