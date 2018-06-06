package menu;

import java.util.Scanner;

import project1.MessageModel;
import project1.User;
import project1.User.Role;

public class Menu2 {
	private int action2 ;
	private User.Role userRoleMenu;
	public Menu1.Category category ; 


	public void showMenu2(Menu1.Category categoryMenu1, User.Role userRole) {
		category = categoryMenu1 ;
		this.userRoleMenu =userRole;

		System.out.println();
		System.out.println("-------------------------");
		System.out.println("---------MENU 2----------");
		System.out.println("---------"+ category+"---------");
		System.out.println("1. VIEW MESSAGE");

		if (userRoleMenu!= User.Role.simpleUser) {	// midUser- superUser - admin extra options
			System.out.println("2. EDIT");
			System.out.println("3. MOVE MESSAGE");
		}//end of if 

		if ((userRoleMenu== User.Role.superUser)||(userRoleMenu== User.Role.admin)) {	// superUser and admin extra option
			System.out.println("4. DELETE");
		}//end of if 

		System.out.println("0. BACK");
	}// end of showMenu2

	public boolean actionMenu2(User user) {	//method to execute the users action
		boolean terminateMenu2 = false;


		while (true) {		//the user chooses an option, and the program checks it 
			try {
				Scanner input = new Scanner(System.in);
				action2 = Integer.parseInt(input.nextLine());
				break;

			} catch (Exception e) {
				System.out.println("----Menu 2----");
				System.out.println(" Wrong action, try again");
			}

		}//end of while for input until success

		switch (action2) {

		case (0):		//Go to Menu1
			terminateMenu2 = true;
		break;

		case (1):		//View a message
			MessageModel.openMessage(user, category );
		break;

		case (2):		//edit a message
			if (userRoleMenu!= User.Role.simpleUser) {
				MessageModel.editMessage(user, category);
				break;
			}//end of if that checks the userRole

		case (3):		//Move to Trash or vise versa

			if (userRoleMenu!= User.Role.simpleUser) {
				MessageModel.moveMessage(user, category);				 	
				break;
			}//end of if that checks the userRole

		
		case (4):		//delete the message
			if ((userRoleMenu== User.Role.superUser)||(userRoleMenu== User.Role.admin)) {
				MessageModel.deleteMessage(user,category);
				break;
			}//end of if that checks the userRole

		//default if he gives a wrong input 
		default:
			System.out.println("Wrong Action, try again");

		}//end of switch 

		return terminateMenu2 ;
	}//end of actionMenu2

}// end of class Menu2
