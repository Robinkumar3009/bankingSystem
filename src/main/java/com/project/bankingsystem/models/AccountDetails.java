package com.project.bankingsystem.models;

public class AccountDetails {

	String name;
	String address;
	String contact;
	String dateOfBirth;
	String adharNo;
	String accountType ;
	Long balance;
	String status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getAdharNo() {
		return adharNo;
	}
	public void setAdharNo(String adharNo) {
		this.adharNo = adharNo;
	}
	
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public Long getBalance() {
		return balance;
	}
	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public AccountDetails(String name, String address, String contact, String dateOfBirth, String adharNo,
		 String accountType, Long balance, String status) {
		super();
		this.name = name;
		this.address = address;
		this.contact = contact;
		this.dateOfBirth = dateOfBirth;
		this.adharNo = adharNo;
		
		this.accountType = accountType;
		this.balance = balance;
		
		this.status = status;
	}
	public AccountDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
