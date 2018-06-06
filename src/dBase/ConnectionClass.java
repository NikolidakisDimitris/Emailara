package dBase;

import java.sql.*;


public class ConnectionClass {	
	private static final String URL ="jdbc:mysql://localhost:3306/?serverTimezone=UTC"  ;
	private static final String USER = "userName" ;
	private static final String PASS ="password";
	 
	
	public ConnectionClass() {
	}
	
	//This method opens the connection
	public static Connection openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		}
		
		try {
			return DriverManager.getConnection(URL, USER, PASS);
		} catch (SQLException e) {

			System.out.println("You didn't connect to the DB");		
			e.printStackTrace();
			
		}
		return null;
	}
	
	//This method closes the connection
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
//			System.out.println("The connection has been terminated");
		} catch (SQLException e) {
			System.out.println("The connection hasn't been terminated");
			e.printStackTrace();
		}
	}
}

