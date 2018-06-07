package project1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import dBase.ConnectionClass;
import project1.User.Role;

public class Login {
	String userName ; 
	String password ; 
	User.Role userRole;

	public Login() {
		//The user gives his credentials
		Scanner input = new Scanner(System.in);
		System.out.println("dose re to onoma sou");
		this.userName = input.nextLine();
		System.out.println("dose re to kodiko sou");
		this.password = input.nextLine();
		this.userRole = User.Role.admin;

		validation(userName, password);// try to validate the credentials
	}//end of constructor

	//Method to validate the credentials
	public void validation(String username, String password) {

		Connection conn = ConnectionClass.openConnection();
		ResultSet rs = null;
		PreparedStatement preparedStmt = null;

		//select the user 
		String SQL ="select * from emailara.user where username = ? and password = ? limit 1";

		try {
			preparedStmt = conn.prepareStatement(SQL);
			preparedStmt.setString(1, username );
			preparedStmt.setString(2, password );
			rs = preparedStmt.executeQuery();

			if (rs.next()) {
				String tmpRole = rs.getString("role");
				User.Role tmtRole1 = User.castingRole(tmpRole);	
				User user = new User(rs.getInt("userId"), rs.getString("username"), rs.getString("password"), tmtRole1);
				user.showMenu();
			}
			else {
				System.out.println("Wrong password or username");
			}
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
		}//end of finally
	}

}//end of login()