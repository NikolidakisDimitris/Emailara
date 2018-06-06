package project1;

import java.util.Scanner;

import dBase.Tables;
import menu.LoginMenu;

public class Main {

	public static void main(String[] args) {


		//Creates DB , Tables and the Admin 
		Tables table= new Tables();	
		table.createDB(); 
		table.createTables(); 
		table.createAdmin();  

		// Login Menu
		LoginMenu login = new LoginMenu(); 
		boolean terminate = false;
		while (terminate == false) { 
			login.showLogin();
			terminate = login.action(login.loginSelection());
		}
	}

}
