package dBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import project1.Login;
import project1.User;

public class Tables {

	public Tables( ) {
	}

	public void createDB() {	// This method creates the DB emailara

		Connection conn = ConnectionClass.openConnection();
		Statement  stmt = null;

		try {
			stmt =  conn.createStatement();
			String SQL = "CREATE DATABASE IF NOT EXISTS emailara";  //SQL that creates the database
			stmt.executeUpdate(SQL);								//execute SQL
			System.out.println("**The database has been created or existed**");

		} catch (SQLException e) {
			System.out.println("*The database has not been created*");
			e.printStackTrace();
		}
		finally {

			ConnectionClass.closeConnection(conn);

			try {
				stmt.close();
				//				System.out.println("1. The statement has been cleared");
			} catch (SQLException e) {
				System.out.println("1. The statement has not been cleared");
				e.printStackTrace();
			}
		}
	} // close the createDB

	public void createTables() {	//Method that creates the tables 
		Connection conn = ConnectionClass.openConnection();
		Statement  stmt = null;

		try {
			stmt =  conn.createStatement();

			String SQL ="CREATE TABLE IF NOT EXISTS emailara.user \r\n" + 
					"(userId INTEGER(5) not NULL AUTO_INCREMENT, \r\n" + 
					"username VARCHAR(45), \r\n" + 
					"password VARCHAR(45),\r\n" + 
					"role VARCHAR(45),\r\n" + 
					"UNIQUE (username),\r\n" + 
					"PRIMARY KEY ( userId )) ;\r\n";

			stmt.executeUpdate(SQL);												//Executes the SQL 
			System.out.println("**The table user has been created or existed**");

			SQL ="CREATE TABLE IF NOT EXISTS emailara.message \r\n" + 							//SQL that creates the table message
					"								(messageId INTEGER(11) not NULL AUTO_INCREMENT, \r\n" + 
					"								receiverId INTEGER(5), \r\n" + 
					"								senderId INTEGER(5),\r\n" + 
					"								timeDate DATETIME,\r\n" + 
					"								subject VARCHAR(45), \r\n" + 
					"								message VARCHAR(250), \r\n" + 
					"								trashsender TINYINT(1) NOT NULL DEFAULT '0' , \r\n" +		
					"								trashreceiver TINYINT(1) NOT NULL DEFAULT '0' , \r\n" +								
					"								deletedreceiver TINYINT(1) NOT NULL DEFAULT '0' , \r\n" +								
					"								deletedsender TINYINT(1) NOT NULL DEFAULT '0' , \r\n" +								

					"								 PRIMARY KEY ( messageId ),\r\n" + 
					"					FOREIGN KEY (receiverId) REFERENCES user(userId),\r\n" + 
					"					FOREIGN KEY (senderId) REFERENCES user(userId));";

			stmt.executeUpdate(SQL);	//Executes the SQL 

			System.out.println("");
			System.out.println("**The table message has been created or existed**");
			System.out.println("");

		} catch (SQLException e) {
			System.out.println("");
			System.out.println("*The tables have not been created* ");
			System.out.println("");
			e.printStackTrace();
		}
		finally {
			ConnectionClass.closeConnection(conn);
			try {
				stmt.close();
				//				System.out.println("2. The statement has been cleared");

			} catch (SQLException e) {
				System.out.println("");
				System.out.println("2. The statement has not been cleared");
				System.out.println("");
				e.printStackTrace();
			}
		}// close the finally

	}// close the createTables


	public void createAdmin() {			//Method tha creates the admin
		Connection conn = ConnectionClass.openConnection();
		Statement stmt = null;

		try {
			stmt= conn.createStatement();

			String SQL = " INSERT  INTO emailara.user (username, password, role) "
					+ "VALUES ('admin', 'admin', 'admin');";

			stmt.executeUpdate(SQL);

			System.out.println("");
			System.out.println("**The admin User has been created**");
			System.out.println("");

		} catch (SQLException e) {
			System.out.println("");
			System.out.println("*The admin User exists or an error occured*");
			System.out.println("");
		}
		finally {
			ConnectionClass.closeConnection(conn);
			try {
				stmt.close();
				//				System.out.println("3. The statement has been cleared");

			} catch (SQLException e) {
				System.out.println("3. The statement has not been cleared");
				e.printStackTrace();
			}
		}//closes the finally
	}//close the createAdmin 

} // close the class