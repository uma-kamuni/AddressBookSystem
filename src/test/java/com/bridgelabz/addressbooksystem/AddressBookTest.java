package com.bridgelabz.addressbooksystem;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bridgelabz.addressbooksystem.AddressBook.IOService;

public class AddressBookTest {

	static AddressBook addressBook = new AddressBook();

	@BeforeClass
	public static void createAddressBookObject() {
		addressBook.setAddressBookName("book1");
	}

	@Test
	public void givenDetails_ShouldAddToContactList() {

		ContactPerson person = new ContactPerson();

		String firstName = "Uma";
		String lastName = "Kamuni";
		String email = "uma@gmail.com";
		long phoneNumber = 938476387;
		String city = "Solapur";
		String state = "Maharashtra";
		long zipCode = 560043;
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setEmail(email);
		person.setPhoneNumber(phoneNumber);
		person.address.setCity(city);
		person.address.setState(state);
		person.address.setZip(zipCode);

		addressBook.addContact(firstName, person);

		firstName = "Navina";
		lastName = "Kamuni";
		email = "navina@gmail.com";
		phoneNumber = 847648253;
		city = "Pune";
		state = "Maharshtra";
		zipCode = 560043;
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setEmail(email);
		person.setPhoneNumber(phoneNumber);
		person.address.setCity(city);
		person.address.setState(state);
		person.address.setZip(zipCode);

		addressBook.addContact(firstName, person);

		int listSize = addressBook.contactList.size();
		Assert.assertEquals(2, listSize);

	}

	@Test
	public void given2Contacts_WhenWrittenToFile_ShouldMatchEntries() {
		AddressBookFileIO addressFileIO = new AddressBookFileIO();
		addressFileIO.writeToAddressBookFile("book1.txt", addressBook.contactList);
		addressFileIO.printData("book1.txt");
		long entries = addressFileIO.countEntries("book1.txt");
		Assert.assertEquals(2, entries);

	}

	@Test
	public void givenFile_WhenRead_ShouldReturnNumberOfEntries() {

		AddressBookFileIO addressFileIO = new AddressBookFileIO();
		List<String> entries = addressFileIO.readDataFromFile("book1.txt");
		long countEntries = entries.size();
		Assert.assertEquals(2, countEntries);
	}

	@Test
	public void givenContactInDB_WhenRetrieved_ShouldMatchContactCount() {

		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		List<ContactPerson> contactdetailsList = addressBookDirectory.readContactDetails(IOService.DB_IO);
		Assert.assertEquals(4, contactdetailsList.size());
	}

}