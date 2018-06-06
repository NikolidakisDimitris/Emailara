package project1;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

import dBase.ConnectionClass;
import project1.User;
import project1.User.Role;

public class UserModel {

	public static void newUser() {
		Scanner input = new Scanner(System.in);

		String	username=null ;
		do {		
			System.out.println("Give the username");
			username = input.nextLine().trim();
		} while (username==null || (username != null && username.isEmpty()) ); //check if the username is empty

		String	password = null;
		do {
			System.out.println("Give the password");
			password = input.nextLine().trim();
		} while (password == null||(password != null && password.isEmpty()) ); //check if the password is empty

		User.Role userRole = null;
		String	role =null;
		do {
			System.out.println("");
			System.out.println("Give the role");
			System.out.println("");

			role = input.nextLine().trim();

			userRole = User.cascadeRole(role);
			if (userRole !=null && (userRole.equals(User.Role.admin)) ) {
				System.out.println("");
				System.out.println("You can't create another admin");
				System.out.println("");
				userRole = null;
			}

		} while (userRole ==null); //check if the role is correct and not empty

		Connection conn = ConnectionClass.openConnection();
		PreparedStatement preparedStmt = null;

		try {
			String SQL = " INSERT  INTO emailara.user (username, password, role) "
					+ "VALUES (?, ?, ?)";
			preparedStmt= conn.prepareStatement(SQL);
			preparedStmt.setString(1, username );
			preparedStmt.setString(2, password );
			preparedStmt.setString(3, role );
			preparedStmt.executeUpdate();

			System.out.println("");
			System.out.println("**The user has been created**");
			System.out.println("");

		} catch (SQLIntegrityConstraintViolationException e) {	
			System.out.println("");
			System.out.println("*The user exists*");
			System.out.println("");

		} catch (SQLException e) {
			System.out.println("");
			System.out.println("*an error occured*");
			System.out.println("");
			e.printStackTrace();
		} finally {
			ConnectionClass.closeConnection(conn);
			try {
				preparedStmt.close();
				//				System.out.println("3. The statement has been cleared");

			} catch (SQLException e) {
				System.out.println("");
				System.out.println("3. The statement has not been cleared");
				System.out.println("");
				e.printStackTrace();
			}
		}
	}// close the method newUser

	//check if the username exists and return the whole user
	public static User getUserByUserName(String username) {

		Connection conn = ConnectionClass.openConnection();
		ResultSet rs = null;
		PreparedStatement preparedStmt = null;

		//select the user 
		String SQL ="select * from emailara.user where username = ? limit 1";

		try {
			preparedStmt = conn.prepareStatement(SQL);
			preparedStmt.setString(1, username );
			rs = preparedStmt.executeQuery();

			if (rs.next()) {	// creates the object user
				String tmpRole = rs.getString("role");
				User.Role tmtRole1 = User.cascadeRole(tmpRole);	 //cascades the type of the role
				User user = new User(rs.getInt("userId"), rs.getString("username"), rs.getString("password"), tmtRole1);
				return user;
			}// close if

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionClass.closeConnection(conn);

			try {
				preparedStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;

	}// close the method getUserByUserName


	public static void deleteUser(){
		Scanner input = new Scanner(System.in);
		viewUsers();

		User deleteUser =null;
		do {
			System.out.println("");
			System.out.println("Give the username you want to delete");
			System.out.println("");
			String dUser = input.nextLine().trim();
			deleteUser = getUserByUserName(dUser); //check if the username is typed correctly
		} while (deleteUser == null);		

		if (deleteUser.getRole().equals(User.Role.admin)) {
			System.out.println("");
			System.out.println("You can't delete the admin");
			System.out.println("");

		}else {

			Connection conn = ConnectionClass.openConnection();
			PreparedStatement preparedStmt = null;

			String SQL ="delete from emailara.user " + 
					"where username = ?  ";
			try {
				preparedStmt = conn.prepareStatement(SQL);
				preparedStmt.setString(1, deleteUser.getUsername() );
				preparedStmt.executeUpdate();	
				System.out.println("");
				System.out.println("**The user has been deleted**");
				System.out.println("");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				ConnectionClass.closeConnection(conn);

				try {
					preparedStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} // close the else . Not to be able to delete the admin
	} // close the deleteUser

	public static void viewUsers(){
		Connection conn = ConnectionClass.openConnection();
		Statement stmt = null;
		ResultSet rs = null;

		String SQL ="select * from emailara.user ";
		try {
			stmt = conn.createStatement();
			rs= stmt.executeQuery(SQL);
			System.out.println("-----------USERS------------");
			System.out.println("userId"+"|"+"username"+"|"+"role"+"|");

			while (rs.next()){

				int userId = rs.getInt("userId");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String role = rs.getString("role");

				System.out.print(userId+"|"+username+"|"+role+"|");
				System.out.println("");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally {
			ConnectionClass.closeConnection(conn);
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}//close finally

	} //close the viewUsers

	public static void changeRole() {

		Scanner input = new Scanner(System.in);
		viewUsers();

		User changeRoleUser =null;
		String CUser;
		
		do {
			System.out.println("");
			System.out.println("Give the username of the user you want to change");
			System.out.println("");
			CUser = input.nextLine().trim();
			changeRoleUser = getUserByUserName(CUser);	//check the username is tyoed correctly
		} while (changeRoleUser == null);		
		System.out.println("the username is "+ changeRoleUser.getUsername());

		if (changeRoleUser.getRole().equals(User.Role.admin)) {
			System.out.println("");
			System.out.println("You can't change the role of the admin user");
			System.out.println("");

		}else {		//All the other users apart from the admin can change role

			String role = null;
			do {
				System.out.println("");
				System.out.println("Give the role you want to assign to the user : "+changeRoleUser.getUsername());
				System.out.println("");
				role = input.nextLine().trim();
				
			}while (User.cascadeRole(role) == null);

			//The admin can not change role
			if (role == "admin") {
				System.out.println("Only one admin can exist in this programm");
			}
			else { 		//Only one admin is allowed in the programm

				Connection conn = ConnectionClass.openConnection();
				PreparedStatement preparedStmt = null;

				String SQL ="update emailara.user " + 
						"set role = ? " + 
						"where username = ? ";
				try {

					preparedStmt = conn.prepareStatement(SQL);
					preparedStmt.setString(1, role );
					preparedStmt.setString(2, CUser );
					preparedStmt.executeUpdate();			
					System.out.println("");
					System.out.println("**The role of the user "+changeRoleUser.getUsername()+" has been changed**");
					System.out.println("");
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				finally {
					ConnectionClass.closeConnection(conn);

					try {
						preparedStmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}// closes the else statement admin is not allowed to be assigned to any user
		} // closes the else . Not to change the role od the admin
	}// close the method changeRole()

}//close the class UserModel




