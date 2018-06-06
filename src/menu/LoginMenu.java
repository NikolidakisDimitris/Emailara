package menu;

import java.util.Scanner;

import project1.Login;

public class LoginMenu {
	private int action ; 

	public LoginMenu() {
	}

	public void showLogin() {  
		System.out.println("-------------------------");
		System.out.println("---------LOGIN----------");
		System.out.println("1. LOGIN");
		System.out.println("0. EXIT");
	}

	public int loginSelection () {

		while (true) {
			Scanner input = new Scanner(System.in);
			try {											//check if an integer is given
				action = Integer.parseInt(input.nextLine());
				break;
			} catch (Exception e) {
				System.out.println("--------LOGIN--------");
				System.out.println("Wrong action, try again");
			}

		}// end of while
		return action;
	}//end of menuSelection()

	public boolean action(int action) {
		boolean terminate = false;

		switch (action) {

		//He exits 
		case (0):
			System.out.println("Exit...");
		terminate = true;
		break;

		// We want to login
		case (1):{

			Login login = new Login();
			terminate = false;
			break;}//end of case 1

		default :
			System.out.println("Wrong choice, try again");
		}
		return terminate;
	} // close the loginAction()
	
}//close loginMenu