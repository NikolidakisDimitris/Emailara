package menu;

import java.util.Scanner;

import project1.MessageModel;
import project1.User;
import project1.User.Role;

public class Menu1 {
	private int action ; 
	private User.Role userRoleMenu1;
	public enum Category {New_Message, Inbox,Sent, Trash 
	}//end of enum

User user= null;

public Menu1( User user) {
		this.userRoleMenu1 = user.getRole();
		this.user= user;
	}//end of constructor
	
	public void showMenu1() {  // method that shows the menu1
		System.out.println("-------------------------");
		System.out.println("---------MENU 1----------");
		System.out.println("1. NEW MESSAGE");
		System.out.println("2. INBOX");
		System.out.println("3. SENT");
		System.out.println("4. TRASH");

		if (userRoleMenu1== User.Role.admin) {		//This is an option just for the admin
			System.out.println("5. SETTINGS");
		}
		
		System.out.println("0. LOGOUT");
	}// end of showMenu()

	
	public int menuSelection () {	//the user select an option
		while (true) {
			Scanner input = new Scanner(System.in);
			try {
				action = Integer.parseInt(input.nextLine());

				break;
			} catch (Exception e) {
				System.out.println("----Menu 1 ----");
				System.out.println("Menu 1 Wrong action, try again");
			}

		}// end of while
		return action;
	}//end of menuSelection()

	
	public Category category(int action) {	//Takes the category of menu1
		Category categoryMenu1 =null;
		
		switch (action) {
		
		case(2): 
			categoryMenu1 = Category.Inbox; 
		break;
		
		case(3): 
			categoryMenu1 = Category.Sent;
		break;
		
		case(4): 
			categoryMenu1 = Category.Trash;
		break;
		}
		return categoryMenu1;
	}// end of method category()

	

	public boolean actionMenu1(int action) {	//method to execute the users action
		boolean terminate = false;
		Category categoryMenu1 = category(action);
		
		switch (action) {
		
		case (0):		//He logged out 
			System.out.println("See you again  , bye bye");
		terminate = true;
		break;
		
	
		case (1):{			//we want to sent a message, call menu 2 
			MessageModel.newMessage(this.user);
			break;}//end of case 1

		
		case (2):{		//we want to see the inbox
			Menu2 menu2 = new Menu2();
			boolean terminateMenu2 = false;
			while (terminateMenu2 == false) {		//loop to show menu 2 until the user closes it
				menu2.showMenu2(categoryMenu1, userRoleMenu1);
				terminateMenu2 = menu2.actionMenu2(user);
			}
			break;}//end of case 2

		
		case (3):{			//we want to see the inbox
			Menu2 menu2 = new Menu2();
			boolean terminateMenu2 = false;
			while (terminateMenu2 == false) {		//loop to show menu 2 until the user closes it
				menu2.showMenu2(categoryMenu1, userRoleMenu1);
				terminateMenu2 = menu2.actionMenu2(user);
			}
			break;}//end of case 3

		
		case (4):{		//the user want to see the trash 
			Menu2 menu2 = new Menu2();	
			boolean terminateMenu2 = false;
			while (terminateMenu2 == false) {		//loop to show menu 2 until the user closes it
				menu2.showMenu2(categoryMenu1,userRoleMenu1);
				terminateMenu2 = menu2.actionMenu2(user);
			}
			break;}//end of case 4

		//call setting, only admin can call settings
		case (5):{
			User.Role role = null;
			role =this.userRoleMenu1;

			if (role== User.Role.admin){
				System.out.println("Settings");
				MenuSettings settings = new MenuSettings();
				boolean terminateSettings = false;
				while (terminateSettings == false) {
					settings.showSettings();
					terminateSettings = settings.actionSettings();
				}
				break;
			}
		} //end of case 5

		
		default:		//if the user gives a wrong input 
			System.out.println("");
			System.out.println("Wrong action, try again ");
			System.out.println("");

		}// end of switch
		return terminate;
	}// end of method actionMenu1()
	
	
}//end of class Menu1
