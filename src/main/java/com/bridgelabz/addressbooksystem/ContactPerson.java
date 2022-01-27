package com.bridgelabz.addressbooksystem;

import com.opencsv.bean.CsvBindByName;

public class ContactPerson {

	@CsvBindByName(column = "First Name")
	private String firstName;

	@CsvBindByName(column = "Last Name")
	private String lastName;

	@CsvBindByName(column = "Email")
	private String email;

	@CsvBindByName(column = "Phone Number")
	private long phoneNumber;

	public Address address = new Address();

	public ContactPerson(String firstName, String lastName, String email, long phoneNumber, String city, String state,
			long zip) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address.setCity(city);
		this.address.setState(state);
		this.address.setZip(zip);
	}

	public ContactPerson() {
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {

		return "First Name - " + firstName + ", Last Name - " + lastName + ", Phone Number - " + phoneNumber
				+ ", Email - " + email + ", City - " + address.getCity() + ", State - " + address.getState()
				+ ", Zip Code - " + address.getZip();
	}
}