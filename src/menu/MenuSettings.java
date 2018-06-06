package menu;

import java.util.Scanner;

import project1.UserModel;

public class MenuSettings {
	private int actionSettings; 

	public void showSettings () {
		System.out.println("-------------------------");
		System.out.println("--------Settings---------");
		System.out.println ("1.New User");
		System.out.println("2. Change Role ");
		System.out.println("3. Delete User ");
		System.out.println("4. View Users ");
		System.out.println("0. BACK");

	}//end of method showMenu()

	public boolean actionSettings() {
		boolean terminateSettings = false;

		//the admin chooses an option, and the program check it
		while (true) {
			try {
				Scanner input = new Scanner(System.in);
				actionSettings = Integer.parseInt(input.nextLine());
				break;

			} catch (Exception e) {
				System.out.println("----Settings----");
				System.out.println(" Wrong action, try again");
			}
		}//end of while

		switch (actionSettings) {

		//Go Back to menu2 
		case (0):
			terminateSettings = true;
		break;

		//Create New User
		case (1):
			UserModel.newUser();
		break;
		
		//Change the role of user
		case (2):
			UserModel.changeRole();
		break;

		//Delete a User
		case (3):
			UserModel.deleteUser();
		break;
		
		case (4):
			UserModel.viewUsers();
		break;
		default:
			System.out.println("Wrong Action, try again");

		}//end of switch 
		return  terminateSettings;

	}//end of menu Settings

	
}//end of class Settings