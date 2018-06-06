package project1;

import java.security.KeyStore.ProtectionParameter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;



import dBase.ConnectionClass;
import menu.Menu1;
import writeToFile.WriteToFile;

public class MessageModel {

	public static void  newMessage(User user) {
		Scanner input = new Scanner(System.in);
		User receiver = null;

		do {
			System.out.println("");
			System.out.println("Give the receiver's username");
			System.out.println("");
			String receiverUserName = null;
			receiverUserName = input.nextLine().trim();
			receiver = UserModel.getUserByUserName(receiverUserName); //check if the receiver exists
		} while(receiver==null );

		String subject = null;
		do {
			System.out.println("");
			System.out.println("Give the subject");
			System.out.println("");
			subject = input.nextLine();
			if (subject!= null) {		//check if the subject is empty
				subject = subject.trim();
			}
		} while (subject == null || subject.isEmpty());

		String message = null;
		do {
			System.out.println("");
			System.out.println("Give the message");
			System.out.println("");
			message = input.nextLine();
			if (message!= null) {		//check if the message is empty
				message = message.trim();
			}
		} while (message == null || message.isEmpty());

		Connection conn = ConnectionClass.openConnection();
		PreparedStatement preparedStmt = null;
		try {
			String SQL = " INSERT INTO emailara.message(`receiverId`, `senderId`, `timeDate`, `subject`, `message`) "
					+ "VALUES (?,?,?,?,?)";
			preparedStmt= conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS); //return the auto-incremented messageId
			preparedStmt.setInt(1, receiver.getId());
			preparedStmt.setInt(2, user.getId());
			
			LocalDateTime time = LocalDateTime.now();
			
			preparedStmt.setTimestamp(3,  Timestamp.valueOf(time));
			preparedStmt.setString(4, subject );
			preparedStmt.setString(5, message );

			if(preparedStmt.executeUpdate() > 0) {
				ResultSet rs = preparedStmt.getGeneratedKeys();
				
				if(rs != null && rs.next()) {
					System.out.println("");
					System.out.println("**The message is sent**");
					System.out.println("");
					
					//create the message as an object and return it to the write it to a txt
					Message messageObj = new Message(rs.getInt(1), user.getId(), receiver.getId(), user.getUsername(), receiver.getUsername(), time, subject, message);
					WriteToFile.createMessageFile(messageObj);	//write the message in a txt				
				}
			} else {
				
			}		

		} catch (SQLException e) {
			System.out.println("");
			System.out.println("*an error occured*");
			System.out.println("");
			e.printStackTrace();
		} finally {
			ConnectionClass.closeConnection(conn);
			try {
				preparedStmt.close();
//				System.out.println("");
//				System.out.println("3. The statement has been cleared");
//				System.out.println("");

			} catch (SQLException e) {
				System.out.println("");
				System.out.println("3. The statement has not been cleared");
				System.out.println("");
				e.printStackTrace();
			}
		}
	}// close new message 

	public static boolean viewMessages(User user, Menu1.Category categoryMenu1) {
		boolean messagesExist= false;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		String senderORreceicer = null;
		String SQL = null;

		Connection conn = ConnectionClass.openConnection();
		try {
															//check the Menu category and the role of the user
			if (categoryMenu1 == Menu1.Category.Inbox) {
				senderORreceicer = "sender";
				if (user.getRole().equals(User.Role.admin)) {

					SQL =  
							"SELECT m.messageId , u.username, m.timeDate, m.subject " + 
									"from emailara.message as m " + 
									"inner join emailara.user as u on (m.senderId = u.userId) where trashreceiver = 0 "
									+ "and deletedreceiver = 0 ";

					preparedStmt= conn.prepareStatement(SQL);

				} 
				else {   //Inbox and not admin
					SQL =  
							"SELECT m.messageId , u.username, m.timeDate, m.subject " + 
									"from emailara.message as m " + 
									"inner join emailara.user as u on (m.senderId = u.userId) where trashreceiver = 0 and  deletedreceiver =0 "
									+ "and m.receiverId= ? ";

					preparedStmt= conn.prepareStatement(SQL);

					preparedStmt.setInt(1, user.getId());

				}				
			}
																//check the Menu category and the role of the user
			else if(categoryMenu1 == Menu1.Category.Trash) {

				senderORreceicer = "sender";
				if (user.getRole().equals(User.Role.admin)) {
					SQL =  
							"SELECT m.messageId , u.username, m.timeDate, m.subject " + 
									"from emailara.message as m " + 
									"inner join emailara.user as u on (m.senderId = u.userId) where (trashreceiver = 1 or trashsender = 1) and deletedreceiver = 0  ";
					preparedStmt= conn.prepareStatement(SQL);

				} 
				else {
							//Trash and not admin
					SQL =  
							"SELECT m.messageId , u.username, m.timeDate, m.subject " + 
									"from emailara.message as m " + 
									"inner join emailara.user as u on (m.senderId = u.userId) where "+
									" (( trashreceiver = 1 and m.receiverId = ?) or (m.senderId = ? and trashsender = 1 ) )";

					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setInt(1, user.getId());
					preparedStmt.setInt(2, user.getId());

				}
			}												//check the Menu category and the role of the user
			else if(categoryMenu1 == Menu1.Category.Sent){
				senderORreceicer = "receiver";

				if(user.getRole().equals(User.Role.admin)) {
					SQL =  
							"SELECT m.messageId , u.username, m.timeDate, m.subject " + 
									"from emailara.message as m " + 
									"inner join emailara.user as u on (m.receiverId = u.userId) where trashsender = 0 and deletedsender = 0 ";

					preparedStmt= conn.prepareStatement(SQL);

				} else
				{ 															//Sent and not admin
					SQL =  
							"SELECT m.messageId , u.username, m.timeDate, m.subject " + 
									"from emailara.message as m " + 
									"inner join emailara.user as u on (m.receiverId = u.userId) "
									+ "where trashsender = 0 and deletedsender = 0 and m.senderId = ? ";
					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setInt(1, user.getId());	

				} 
			}

			rs = preparedStmt.executeQuery();

			boolean printHeader = true;

			while (rs.next()) {

				if(printHeader) {
					printHeader = false;
					System.out.println("");
					System.out.println("-----------"+categoryMenu1 +"------------");
					System.out.println(" MessageId | " +senderORreceicer+ " | Time | Subject |");
				}				

				int messageId = rs.getInt("messageId");
				String actionUser = rs.getString("username");				
				Timestamp time = rs.getTimestamp("timeDate");
				String subject = rs.getString("subject");

				System.out.print(messageId+"|"+actionUser+"|"+time.toLocalDateTime().format(WriteToFile.formatter)+"|"+subject);
				System.out.println("");
				messagesExist= true;

			}//close while
			if(printHeader) {
				System.out.println("");
				System.out.println("There are no messages "); //na to xrisimipoihso meta kai kato gia na min zitaei ston xristi tipota na parei
				System.out.println("");
				messagesExist= false;
			}//if close

		} catch (SQLException e) {
			System.out.println("");
			System.out.println("*an error occured*");
			System.out.println("");
			e.printStackTrace();
			
		} finally {
			ConnectionClass.closeConnection(conn);
			try {
				preparedStmt.close();
//				System.out.println("");
//				System.out.println("3. The statement has been cleared");
//				System.out.println("");

			} catch (SQLException e) {
				System.out.println("");
				System.out.println("3. The statement has not been cleared");
				System.out.println("");
				e.printStackTrace();
			}
		}
		return messagesExist;
	} // close the viewInbox


	public static void openMessage(User user, Menu1.Category categoryMenu1) {
		boolean messagesExist = viewMessages(user, categoryMenu1);
		if (messagesExist == true) { //if there are no messages it doesn't have to ask you which message you want to open

			Scanner input = new Scanner(System.in);
			System.out.println("");
			System.out.println("Give the id of the message you want to open");
			System.out.println("");
			int messageId = 0;

			do {
				try {
					messageId = Integer.parseInt(input.nextLine());

				} catch (Exception e) {
					System.out.println("");
					System.out.println("Wrong action, try again");
					System.out.println("");
				}
			}
			while (messageId <= 0);

			Connection conn = ConnectionClass.openConnection();			
			PreparedStatement preparedStmt = null;
			ResultSet rs = null;
			String SQL = null;
			try {

				if (categoryMenu1 == Menu1.Category.Inbox) {
					if(user.getRole().equals(User.Role.admin)) {

						SQL =  
								"SELECT m.message " + 
										"from emailara.message as m " + 
										" where trashreceiver = 0 and deletedreceiver = 0 and m.messageId = ?  LIMIT 1 ";

						preparedStmt= conn.prepareStatement(SQL);
						preparedStmt.setInt(1, messageId);
					}
					else {//if(!user.getRole().equals(User.Role.admin)) {
						SQL =  "SELECT m.message " + 
								"from emailara.message as m " + 
								" where trashreceiver = 0 and deletedreceiver = 0 and m.messageId = ? and  m.receiverId= ?  LIMIT 1";

						preparedStmt= conn.prepareStatement(SQL);
						preparedStmt.setInt(1, messageId);
						preparedStmt.setInt(2, user.getId());

					}				
				}
				else if(categoryMenu1 == Menu1.Category.Trash) {
					if(user.getRole().equals(User.Role.admin)) {

						SQL =  
								"SELECT m.message " + 
										"from emailara.message as m " + 
										"where (trashreceiver = 1 or trashsender = 1) and deletedreceiver = 0  and m.messageId = ? LIMIT 1 ";

						preparedStmt= conn.prepareStatement(SQL);
						preparedStmt.setInt(1, messageId);

					}else
					{ //if(!user.getRole().equals(User.Role.admin)) {

						SQL  = "SELECT m.message " + 
								"from emailara.message as m " + 
								"where  (( trashreceiver = 1 and m.receiverId = ?) or (m.senderId = ? and trashsender = 1 )"
								+ " and m.messageId = ? and ( m.receiverId = ? or m.senderId = ?) ) LIMIT 1";

						preparedStmt= conn.prepareStatement(SQL);
						preparedStmt.setInt(1, user.getId());
						preparedStmt.setInt(2, user.getId());
						preparedStmt.setInt(3, messageId);
						preparedStmt.setInt(4, user.getId());
						preparedStmt.setInt(5, user.getId());
					}
				}
				
				else if(categoryMenu1 == Menu1.Category.Sent){
					if(user.getRole().equals(User.Role.admin)) {

						SQL =  "SELECT m.message " + 
								"from emailara.message as m " + 
								"where trashsender = 0 and deletedsender = 0 and messageId = ? LIMIT 1 ";								

						preparedStmt= conn.prepareStatement(SQL);
						preparedStmt.setInt(1, messageId);

					} else
					{  // if(!user.getRole().equals(User.Role.admin)) {
						SQL =  	"SELECT m.message " + 
								"from emailara.message as m " + 
								"where trashsender = 0 and deletedsender = 0 and m.senderId= ? and messageId = ? and m.senderId= ? LIMIT 1 ";

						preparedStmt= conn.prepareStatement(SQL);
						preparedStmt.setInt(1, user.getId());	
						preparedStmt.setInt(2, messageId);
						preparedStmt.setInt(3, user.getId());	

					} 
				}

				rs = preparedStmt.executeQuery();
				System.out.println("");

				if(rs.next()) {
					System.out.println("");
					System.out.println(rs.getString("message")); 
					System.out.println("");
				} else {
					System.out.println("");
					System.out.println("There is no message with this criterion");
					System.out.println("");
				}

			} catch (SQLException e) {
				System.out.println("");
				System.out.println("*There is no message with this id or an error accured*");
				System.out.println("");
				e.printStackTrace();
			} finally {
				ConnectionClass.closeConnection(conn);
				try {
					preparedStmt.close();
//					System.out.println("");
//					System.out.println("3. The statement has been cleared");
//					System.out.println("");

				} catch (SQLException e) {
					System.out.println("");
					System.out.println("3. The statement has not been cleared");
					System.out.println("");
					e.printStackTrace();
				}
			}
		}//if close 
	}// close the method that shows the menu

	
	public static void deleteMessage(User user, Menu1.Category categoryMenu1) {
		boolean messagesExist = viewMessages(user, categoryMenu1);
		if (messagesExist == true) {		//if the message doesn't exist it cant delete anything

			Scanner input = new Scanner(System.in);
			System.out.println("");
			System.out.println("Give the id of the message you want to delete");
			System.out.println("");

			int messageId = 0;

			do {
				try {
					messageId = Integer.parseInt(input.nextLine());

				} catch (Exception e) {
					System.out.println("");
					System.out.println("Wrong action, try again");
					System.out.println("");
				}
			}
			while (messageId == 0);

			Connection conn = ConnectionClass.openConnection();
			PreparedStatement preparedStmt = null;
			String SQL ; 
			try {
				if(user.getRole().equals(User.Role.admin)) {

					SQL =  
							"delete from emailara.message " + 
									" where messageId = ? ";
					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setInt(1, messageId);

				}

				else if  (categoryMenu1 == Menu1.Category.Inbox) { 					

					SQL =  
							"update emailara.message  " + 
									" set deletedreceiver = 1 "+
									"where trashreceiver = 0 and deletedreceiver = 0 and messageId = ? and  receiverId= ? ";		

					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setInt(1, messageId);
					preparedStmt.setInt(2, user.getId());
				}
				
				else if(categoryMenu1 == Menu1.Category.Trash) {

					SQL  = "update emailara.message " + 
							" set deletedreceiver = if (receiverId = ? , 1, deletedreceiver) , "
							+ "deletedsender = if (senderId = ? , 1, deletedsender) "+
							" where  (( trashreceiver = 1 and receiverId = ?) or (senderId = ? and trashsender = 1 )"
							+ " and messageId = ? )";								

					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setInt(1, user.getId());
					preparedStmt.setInt(2, user.getId());
					preparedStmt.setInt(3, user.getId());
					preparedStmt.setInt(4, user.getId());
					preparedStmt.setInt(5, messageId);

				}
				else if(categoryMenu1 == Menu1.Category.Sent){

					SQL =  "update emailara.message " + 
							" set deletedsender = 1 "+
							"where trashsender = 0 and deletedsender = 0 and senderId= ? and messageId = ?  ";

					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setInt(1, user.getId());	
					preparedStmt.setInt(2, messageId);
				}

				if(preparedStmt.executeUpdate() > 0) {
					System.out.println("");
					System.out.println("the message with Id" + messageId + " has been deleted");
					System.out.println("");				
				} else {
					System.out.println("");
					System.out.println("There were no messages with this criterion");
					System.out.println("");
				}

			} catch (SQLException e) {
				System.out.println("");
				System.out.println("*There is no message with this id or an error accured*");
				System.out.println("");
				e.printStackTrace();
			} finally {
				ConnectionClass.closeConnection(conn);
				try {
					preparedStmt.close();
//					System.out.println("");
//					System.out.println("3. The statement has been cleared");
//					System.out.println("");

				} catch (SQLException e) {
					System.out.println("");
					System.out.println("3. The statement has not been cleared");
					System.out.println("");
					e.printStackTrace();
				}
			}
		}//close if
	}// close the method deletes a message 

//Moves the messages to trash and vise- versa
	public static void moveMessage(User user, Menu1.Category categoryMenu1) {
		boolean messagesExist = viewMessages(user, categoryMenu1);
		if (messagesExist == true) {	//if there are no messages you cant move anything

			Scanner input = new Scanner(System.in);
			System.out.println("");
			System.out.println("Give the id of the message you want to move");
			System.out.println("");

			int messageId = 0;

			do {
				try {
					messageId = Integer.parseInt(input.nextLine());

				} catch (Exception e) {
					System.out.println("");
					System.out.println("Wrong action, try again");
					System.out.println("");
				}
			}
			while (messageId == 0);

			Connection conn = ConnectionClass.openConnection();
			PreparedStatement preparedStmt = null;
			String SQL = null;

			try {

				if(user.getRole().equals(User.Role.admin)) {

					SQL =  
							"update emailara.message "
									+ "set trashsender = if (trashsender = 0, 1, 0 ), trashreceiver = if (trashreceiver = 0 , 1 , 0 )   " + 
									" where messageId = ? ";
					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setInt(1, messageId);

				} else if  (categoryMenu1 == Menu1.Category.Inbox){

					SQL =  
							"update emailara.message  " + 
									" set trashreceiver = 1 "+
									"where trashreceiver = 0 and deletedreceiver = 0 and messageId = ? and  receiverId= ? ";		

					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setInt(1, messageId);
					preparedStmt.setInt(2, user.getId());
				} 
				
				else if (categoryMenu1 == Menu1.Category.Trash) {

					SQL  = "update emailara.message " + 
							" set trashreceiver = if (receiverId = ? , 0, trashreceiver) , "+
							" trashsender = if (senderId = ? , 0, trashsender)  "+
							" where  (( receiverId = ? and deletedreceiver = 0 and trashreceiver = 1) or (senderId = ? and deletedsender = 0 and trashsender = 1)"
							+ " and messageId = ? )";								

					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setInt(1, user.getId());
					preparedStmt.setInt(2, user.getId());
					preparedStmt.setInt(3, user.getId());
					preparedStmt.setInt(4, user.getId());
					preparedStmt.setInt(5, messageId);
				}
				
				else if(categoryMenu1 == Menu1.Category.Sent){
					
					SQL =  "update emailara.message " + 
							" set trashsender = 1 "+
							"where trashsender = 0 and deletedsender = 0 and senderId= ? and messageId = ? ";

					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setInt(1, user.getId());
					preparedStmt.setInt(2, messageId );	
				} 
								
				if(preparedStmt.executeUpdate() > 0) {
					System.out.println("");
					System.out.println("the message with Id" + messageId + " has been moved");
					System.out.println("");				
				} else {
					System.out.println("");
					System.out.println("There were no messages with this criterion");
					System.out.println("");
				}

			} catch (SQLException e) {
				System.out.println("");
				System.out.println("*There is no message with this id or an error accured*");
				System.out.println("");
				e.printStackTrace();
			} finally {
				ConnectionClass.closeConnection(conn);
				try {
					preparedStmt.close();
//					System.out.println("");
//					System.out.println("3. The statement has been cleared");
//					System.out.println("");

				} catch (SQLException e) {
					System.out.println("");
					System.out.println("3. The statement has not been cleared");
					System.out.println("");
					e.printStackTrace();
				}
			}
		}//closes the if 
	}// close the method deletes a message


	public static void editMessage(User user, Menu1.Category categoryMenu1) {
		boolean messagesExist = viewMessages(user, categoryMenu1);
		
		if (messagesExist == true) { //check if there are any messages , otherwise it doesn't get to the edit message
			Scanner input = new Scanner(System.in);
			System.out.println("");
			System.out.println("Give the id of the message you want to edit");
			System.out.println("");
			String SQL= null;

			int messageId = 0;

			do {
				try {
					messageId = Integer.parseInt(input.nextLine()); 

				} catch (Exception e) {
					System.out.println("");
					System.out.println("Wrong action, try again");
					System.out.println("");
				}
			}
			while (messageId == 0);

			Connection conn = ConnectionClass.openConnection();
			PreparedStatement preparedStmt = null;

			System.out.println("");
			System.out.println("Write the new message");
			System.out.println("");

			String newMessage = "";
			do {
				newMessage = input.nextLine().trim();
			}while (newMessage == null ||(newMessage != null && newMessage.isEmpty()));

			try {

				if(user.getRole().equals(User.Role.admin)) {

					SQL =  
							"update emailara.message "
									+ "set message = ?  " + 
									" where messageId = ? ";
					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setString(1, newMessage);
					preparedStmt.setInt(2, messageId);
				} 
				
				else if  (categoryMenu1 == Menu1.Category.Inbox){

					SQL =  
							"update emailara.message  " + 
									" set message = ? "+
									"where trashreceiver = 0 and deletedreceiver = 0 and messageId = ? and  receiverId= ? ";		

					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setString(1, newMessage);
					preparedStmt.setInt(2, messageId);
					preparedStmt.setInt(3, user.getId());
				} 
				
				else if (categoryMenu1 == Menu1.Category.Trash) {

					SQL  = "update emailara.message " + 
							" set message = ?  "+
							" where  (( receiverId = ? and deletedreceiver = 0 and trashreceiver = 1 ) or (senderId = ? and deletedsender = 0 and trashsender = 1 )"
							+ " and messageId = ? )";								

					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setString(1, newMessage);
					preparedStmt.setInt(2, user.getId());
					preparedStmt.setInt(3, user.getId());
					preparedStmt.setInt(4, messageId);
				}
				
				else if(categoryMenu1 == Menu1.Category.Sent){

					SQL =  "update emailara.message " + 
							" set message = ? "+
							"where trashsender = 0 and deletedsender = 0 and senderId= ? and messageId = ? ";

					preparedStmt= conn.prepareStatement(SQL);
					preparedStmt.setString(1, newMessage);
					preparedStmt.setInt(2, user.getId());
					preparedStmt.setInt(3, messageId );	
				}

				if(preparedStmt.executeUpdate() > 0) {
					System.out.println("");
					System.out.println("the message with Id" + messageId + " has been edited");
					System.out.println("");				
					
											//fetch data from DB to overwrite the txt file
					SQL = "select m.*, "
						+ "(select u.username from emailara.user as u where u.userId = m.receiverId) as receiverUsername, "
						+ "(select u.username from emailara.user as u where u.userId = m.senderId) as senderUsername"
						+ " from emailara.message as m"
						+ " where m.messageId = ? ";
					
					PreparedStatement stm = conn.prepareStatement(SQL);
					stm.setInt(1, messageId);
					ResultSet rs = stm.executeQuery();
					
					if(rs != null && rs.next()) {	//creates the message as an object in order to overwrite the txt
						Message messageObj = new Message(rs.getInt("messageId"), rs.getInt("receiverId"), rs.getInt("senderId"), rs.getString("receiverUsername"), rs.getString("senderUsername"), rs.getTimestamp("timeDate").toLocalDateTime(), rs.getString("subject"), rs.getString("message") );
						WriteToFile.createMessageFile(messageObj); //overwrite the txt
					}					
					
				} else {
					System.out.println("");
					System.out.println("There were no messages with this criterion");
					System.out.println("");
				}				

			} catch (SQLException e) {
				System.out.println("");
				System.out.println("*There is no message with this id or an error accured*");
				System.out.println("");
				e.printStackTrace();
			} finally {
				ConnectionClass.closeConnection(conn);
				try {
					preparedStmt.close();
//					System.out.println("");
//					System.out.println("3. The statement has been cleared");
//					System.out.println("");

				} catch (SQLException e) {
					System.out.println("");
					System.out.println("3. The statement has not been cleared");
					System.out.println("");
					e.printStackTrace();
				}
			}
		}//close the if statement
	}// close the method deletes a message
}
