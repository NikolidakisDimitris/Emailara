package project1;

import menu.Menu1;

public class User {
	private int userId ;
	private String username ;
	private String password;
	public enum Role {
		simpleUser, midUser, superUser, admin
	};//end of enum Role
	private Role role;
	
	public User(int id, String name, String pass, Role role) {
		this.userId = id;
		this.username = name;
		this.password = pass;
		this.role = role;
	}//end of the constructor

	public User (String name, String pass, Role role) {
		this.username= name;
		this.password= pass;
		this.role= role;
	}

	public int getId() {
		return userId;
	}

	public void setId(int id) {
		this.userId = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
				//cascade the type of the role because in the DB is a String and here an enum. 
	public static User.Role castingRole(String tmp) {
		User.Role role =null;
		switch (tmp) {
		
		case "simpleUser":
		 role = User.Role.simpleUser;
			break;
			
		case "midUser":
			 role = User.Role.midUser;
				break;
			
		case "superUser":
			 role = User.Role.superUser;
				break;
			
		case "admin":
			 role = User.Role.admin;
				break;
				
		default:
			System.out.println("Wrong role, try again"); //check if the role typed is valid according to the enum Role
			return null;	
		}		
		return role;
	}
	
	//Method to create the menu for the user
	public void showMenu() {
		Menu1 menu1 = new Menu1(User.this);
		boolean terminate = false;
		while (terminate == false) {
			menu1.showMenu1();
			terminate = menu1.actionMenu1(menu1.menuSelection());
		}	
	}// end of showMenu()
	
}// end of userRole
