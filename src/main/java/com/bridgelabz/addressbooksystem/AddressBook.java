package com.bridgelabz.addressbooksystem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AddressBook implements AddressBookIF {

	Scanner scannerObject = new Scanner(System.in);
	public Map<String, ContactPerson> contactList = new HashMap<String,ContactPerson>();
	public static HashMap<String, ArrayList<ContactPerson>> personByCity  = new HashMap<String, ArrayList<ContactPerson>>();
	public static HashMap<String, ArrayList<ContactPerson>> personByState = new HashMap<String, ArrayList<ContactPerson>>();
	public String addressBookName;
	public boolean isPresent = false;
	
	public enum IOService {
		CONSOLE_IO, FILE_IO
	}
	
	public String getAddressBookName() {
		return addressBookName;
	}

	public void setAddressBookName(String addressBookName) {
		this.addressBookName = addressBookName;
	}
	
	public ArrayList<ContactPerson> getContact() {
		return new ArrayList<ContactPerson>(contactList.values());
	}


	@Override
	public void operation() {

		boolean moreChanges = true;
		do {

			System.out.println("\nChoose the operation you want to perform");
			System.out.println(
					"1.Add To Address Book\n2.Edit Existing Entry\n3.Delete Contact\n4.Display Address book\n5.Display Sorted Address Book By Custom Criteria\n6.Write To File\n7.Read Form File\n8.Write Data To CSV File\n9.Read Data From CSV File\n10.Write Data To JSON\n11.Read Data From JSON\n12.Exit Address book System");

			switch (scannerObject.nextInt()) {
			case 1:
				createContactPerson(scannerObject);
				break;
				
			case 2:
				editPerson();
				break;
				
			case 3:
				deletePerson();
				break;
				
			case 4:
				writeToAddressBookFile(IOService.CONSOLE_IO);
				break;
				
			case 5:
				System.out.println("What Criteria Do You Want Address Book To Be Sorted In ?");
				System.out.println("1.FirstName\n2.City\n3.State\n4.Zip Code");
				int sortingChoice = scannerObject.nextInt();
				sortAddressBook(sortingChoice);
				break;
				
			case 6: 
				writeToAddressBookFile(IOService.FILE_IO);
				break;
				
			case 7:
				readDataFromFile(IOService.FILE_IO);
				break;
				
			case 8:
				try {
                    writeDataToCSV();
                }catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                    e.printStackTrace();
                }
				break;
				
			case 9:
				try {
                    readDataFromCSV();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                break;
                
			case 10:
				try {
					writeDataToJson();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
				break;
				
			case 11:
				try {
					readDataFromJson();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
				break;
				
			case 12:
				moreChanges = false;
				System.out.println("Exiting Address Book: "+this.getAddressBookName()+" !");

			}

		} while (moreChanges);
	}
	
	@Override
	public void createContactPerson(Scanner scannerObject) {
		
		ContactPerson person = new ContactPerson();
		
		System.out.println("Enter First Name: ");
		String firstName = scannerObject.next();
		
		contactList.entrySet().stream().forEach(entry -> {
			if(entry.getKey().equals(firstName.toLowerCase())) {
				System.out.println("Contact Already Exists");
				isPresent = true;
				return;
			}
		});
		
		if(isPresent == false) {
			
			System.out.println("Enter Last Name: ");
			String lastName = scannerObject.next();
			
			System.out.println("Enter Phone Number: ");
			long phoneNumber = scannerObject.nextLong();
			
			System.out.println("Enter Email: ");
			String email = scannerObject.next();
			
			System.out.println("Enter City: ");
			String city = scannerObject.next();
			
			System.out.println("Enter State: ");
			String state = scannerObject.next();
			
			System.out.println("Enter Zip Code: ");
			long zipCode = scannerObject.nextLong();
			
			person.setFirstName(firstName);
			person.setLastName(lastName);
			person.setPhoneNumber(phoneNumber);
			person.setEmail(email);
			person.setCity(city);
			person.setState(state);
			person.setZip(zipCode);
			addPersonToCity(person);
			addPersonToState(person);
			
			addContact(firstName, person);
		}
	}

	@Override
	public void addContact(String firstName, ContactPerson person) {
		
			contactList.put(firstName.toLowerCase(), person);

	}
	
	@Override	
	public void addPersonToCity(ContactPerson contact) {
		if (personByCity.containsKey(contact.getCity())) {
			personByCity.get(contact.getCity()).add(contact);
		}
		else {
			ArrayList<ContactPerson> cityList = new ArrayList<ContactPerson>();
			cityList.add(contact);
			personByCity.put(contact.getCity(), cityList);
		}
	}
	
	@Override
	public void addPersonToState(ContactPerson contact) {
		if (personByState.containsKey(contact.getState())) {			
			personByState.get(contact.getState()).add(contact);
		}
		else {
			ArrayList<ContactPerson> stateList = new ArrayList<ContactPerson>();
			stateList.add(contact);
			personByState.put(contact.getState(), stateList);
		}
	}
	
	@Override
	public void editPerson() {
		
		ContactPerson person = new ContactPerson();

		System.out.println("Enter the first name:");
		String firstName = scannerObject.next();
		
		if(contactList.containsKey(firstName)) {
			person = contactList.get(firstName);
			
			System.out.println("\nChoose the attribute you want to change:");
			System.out.println("1.Last Name\n2.Phone Number\n3.Email\n4.City\n5.State\n6.ZipCode");
			int choice = scannerObject.nextInt();

			switch (choice) {
			case 1:
				System.out.println("Enter the correct Last Name :");
				String lastName = scannerObject.next();
				person.setLastName(lastName);
				break;
			case 2:
				System.out.println("Enter the correct Phone Number :");
				long phoneNumber = scannerObject.nextLong();
				person.setPhoneNumber(phoneNumber);
				break;
			case 3:
				System.out.println("Enter the correct Email Address :");
				String email = scannerObject.next();
				person.setEmail(email);
				break;
			case 4:
				System.out.println("Enter the correct City :");
				String city = scannerObject.next();
				person.setCity(city);
				break;
			case 5:
				System.out.println("Enter the correct State :");
				String state = scannerObject.next();
				person.setState(state);
				break;
			case 6:
				System.out.println("Enter the correct ZipCode :");
				long zip = scannerObject.nextLong();
				person.setZip(zip);
				break;
			}
		}
		else {
			System.out.println("Book Does Not Exist");
		}
		
		
	}

	@Override
	public void deletePerson() {

		System.out.println("Enter the first name of the person to be deleted");
		String firstName = scannerObject.next();
		if(contactList.containsKey(firstName)) {
			contactList.remove(firstName);
			System.out.println("Successfully Deleted");
		}
		else {
			System.out.println("Contact Not Found!");
		}
		
	}

	@Override
	public void displayContents() {
		
		System.out.println("----- Contents of the Address Book "+this.getAddressBookName()+" -----");
		for (String eachContact : contactList.keySet()) {
			ContactPerson person = contactList.get(eachContact);
			System.out.println(person);
		}
		System.out.println("-----------------------------------------");

	}
	
	@Override
	public void printSortedList(List<ContactPerson> sortedContactList) {
		System.out.println("------ Sorted Address Book "+this.getAddressBookName()+" ------");
		Iterator iterator = sortedContactList.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
			System.out.println();
		}
		System.out.println("-----------------------------------------");
	}
	
	@Override
	public void sortAddressBook(int sortingChoice) {
		List<ContactPerson> sortedContactList;
		
		switch(sortingChoice) {
		
			case 1: sortedContactList = contactList.values().stream()
					.sorted((firstperson, secondperson) -> firstperson.getFirstName().compareTo(secondperson.getFirstName()))
					.collect(Collectors.toList());
					printSortedList(sortedContactList);
					break;
				
			case 2: sortedContactList = contactList.values().stream()
					.sorted((firstperson, secondperson) -> firstperson.getCity().compareTo(secondperson.getCity()))
					.collect(Collectors.toList());
					printSortedList(sortedContactList);
					break;
				
			case 3: sortedContactList = contactList.values().stream()
					.sorted((firstperson, secondperson) -> firstperson.getState().compareTo(secondperson.getState()))
					.collect(Collectors.toList());
					printSortedList(sortedContactList);
					break;
				
			case 4: sortedContactList = contactList.values().stream()
					.sorted((firstperson, secondperson) -> Long.valueOf(firstperson.getZip()).compareTo(Long.valueOf(secondperson.getZip())))
					.collect(Collectors.toList());
					printSortedList(sortedContactList);
					break;
		}
				
	}
	
	@Override
	public void writeToAddressBookFile(IOService ioService) {
		if(ioService.equals(IOService.CONSOLE_IO))
			displayContents();
		
		else if(ioService.equals(IOService.FILE_IO)) {
			String bookName = this.getAddressBookName();
			String fileName = bookName+".txt";
			new AddressBookFileIO().writeToAddressBookFile(fileName, contactList);
		}
	}
	
	public void printData(IOService fileIo) {
		String bookName = this.getAddressBookName();
		String fileName = bookName+".txt";
		if(fileIo.equals(IOService.FILE_IO)) new AddressBookFileIO().printData(fileName);
	}


	public long countEntries(IOService fileIo) {
		
		String bookName = this.getAddressBookName();
		String fileName = bookName+".txt";
		if(fileIo.equals(IOService.FILE_IO)) 
			return new AddressBookFileIO().countEntries(fileName);
		
		return 0;
	}
	
	@Override
	public List<String> readDataFromFile(IOService fileIo) {
		
		List<String> employeePayrollFromFile = new ArrayList<String>();
		if(fileIo.equals(IOService.FILE_IO)) {
			System.out.println("Employee Details from payroll-file.txt");
			String bookName = this.getAddressBookName();
			String fileName = bookName+".txt";
			employeePayrollFromFile = new AddressBookFileIO().readDataFromFile(fileName);
			
		}
		return employeePayrollFromFile;
	}
	
	@Override
	public void writeDataToCSV() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
		
		String fileName = "./"+this.getAddressBookName()+"Contacts.csv";
        try (Writer writer = Files.newBufferedWriter(Paths.get(fileName));) {
        	
            StatefulBeanToCsvBuilder<ContactPerson> builder = new StatefulBeanToCsvBuilder<>(writer);
            StatefulBeanToCsv<ContactPerson> beanWriter = builder.build();
            ArrayList<ContactPerson> listOfContacts= contactList.values().stream().collect(Collectors.toCollection(ArrayList::new));
            beanWriter.write(listOfContacts);
            writer.close();
            System.out.println("Written To CSV Successfully !");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	@Override
    public <CsvValidationException extends Throwable> void readDataFromCSV() throws IOException, CsvValidationException {
    	
    	String fileName = "./"+this.getAddressBookName()+"Contacts.csv";
        try (Reader reader = Files.newBufferedReader(Paths.get(fileName));
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();){
        	
            String[] nextRecord;
            try {
				while ((nextRecord = csvReader.readNext()) != null) {
				    System.out.println("First Name = " + nextRecord[2]);
				    System.out.println("Last Name = " + nextRecord[3]);
				    System.out.println("City = " + nextRecord[0]);
				    System.out.println("State = " + nextRecord[5]);
				    System.out.println("Email = " + nextRecord[1]);
				    System.out.println("Phone Number = " + nextRecord[4]);
				    System.out.println("Zip Code = " + nextRecord[6]);
				    System.out.println("\n");
				}
			} catch (com.opencsv.exceptions.CsvValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
	@Override
	public void writeDataToJson() throws IOException {
		
		String fileName = "./" + this.getAddressBookName() + "Contacts.json";
		Path filePath = Paths.get(fileName);
		Gson gson = new Gson();
		String json = gson.toJson(contactList.values());
		FileWriter writer = new FileWriter(String.valueOf(filePath));
		writer.write(json);
		writer.close();

	}

	@Override
    public void readDataFromJson() throws IOException {
    	
        ArrayList<ContactPerson> contactList;
        String fileName = "./"+this.getAddressBookName()+"Contacts.json";
        Path filePath = Paths.get(fileName);
        
        try (Reader reader = Files.newBufferedReader(filePath)) {
            Gson gson = new Gson();
            contactList = new ArrayList<>(Arrays.asList(gson.fromJson(reader, ContactPerson[].class)));
            for (ContactPerson contact : contactList) {
            	System.out.println("{");
                System.out.println("Firstname : " + contact.getFirstName());
                System.out.println("Lastname : " + contact.getLastName());
                System.out.println("City : " + contact.getCity());
                System.out.println("State : " + contact.getState());
                System.out.println("Zip Code : " + contact.getZip());
                System.out.println("Phone number : " + contact.getPhoneNumber());
                System.out.println("Email : " + contact.getEmail());
                System.out.println("}\n");
            }
        }
    }

}