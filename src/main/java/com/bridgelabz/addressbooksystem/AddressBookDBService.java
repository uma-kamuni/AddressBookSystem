package com.bridgelabz.addressbooksystem;

import java.sql.Connection;import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {

	private Connection getConnection() throws SQLException {

		String jdbcURL = "jdbc:mysql://localhost:3306/addressbookservice?useSSL=false";
		String userName = "root";
		String password = "@Sanaya1444";
		Connection connection;

		System.out.println("Connecting to the database : " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection is Succcessfully Established!! " + connection);

		return connection;
	}

	private List<ContactPerson> getContactDetails(ResultSet resultSet) {

		List<ContactPerson> contactList = new ArrayList<>();

		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("ID");
				String type = resultSet.getString("Type");
				String fName = resultSet.getString("First_Name");
				String lName = resultSet.getString("Last_Name");
				String address = resultSet.getString("Address");
				String city = resultSet.getString("City");
				String state = resultSet.getString("State");
				int zip = resultSet.getInt("Zipcode");
				String phoneNo = String.valueOf(resultSet.getLong("Phone_Number"));
				String email = resultSet.getString("Email_Id");
				contactList.add(new ContactPerson());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	/**
	 * Purpose : To read the person info from the database
	 *
	 * @return the contact details
	 */
	public List<ContactPerson> readContactDetails() {

		String sqlStatement = "SELECT * FROM ADDRESSBOOK";
		List<ContactPerson> contactsList = new ArrayList<>();

		try (Connection connection = getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			contactsList = getContactDetails(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactsList;
	}
}