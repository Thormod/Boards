package com.thor.entities;

public class Account {
	public String email;
	public String name;
	public String actoken;
	
	public String getActoken() {
		return actoken;
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
	public void setActoken(String actoken) {
		this.actoken = actoken;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setName(String name) {
		this.name = name;
	}
}
