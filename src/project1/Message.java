package project1;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Message {
	private int messageId;
	private int receverId;
	private String receiverUsername;
	private String senderUsername;
	private int senderId;
	private LocalDateTime timeDate; 
	private String subject;
	private String message;
	
	public void setReceiverUsername(String receiverUsername) {
		this.receiverUsername = receiverUsername;
	}
	
	public String getReceiverUsername() {
		return receiverUsername;
	}
	
	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}
	
	public String getSenderUsername() {
		return senderUsername;
	}

	public void setTimeDate(LocalDateTime timeDate) {
		this.timeDate = timeDate;
	}

	public LocalDateTime getTimeDate() {
		return timeDate;
	}

	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public Message(int messageId, int senderId, int receiverId, String senderUsername, String receiverUsername, LocalDateTime timeDate, String subject, String message) {
		this.messageId = messageId;
		this.receverId = receiverId;
		this.senderId = senderId;
		this.timeDate = timeDate;
		this.subject = subject;
		this.message = message;
		this.senderUsername = senderUsername;
		this.receiverUsername = receiverUsername;
	}

	public Message(int messageId, int receverId, int senderId, String message) {
		//the message is given by the user
		Scanner input = new Scanner(System.in);
		this.message =  input.nextLine() ;

		try {
			int messageNo=0;
			messageNo = Integer.parseInt(input.nextLine());
			input.close();

		} catch (Exception e) {
			System.out.println("Wrong message, try again");
				}
		}
	
	
	public int getMessageId() {
		return messageId;
	}
	public void setId(int messageId) {
		this.messageId = messageId;
	}
	public int getReceverId() {
		return receverId;
	}
	public void setReceverId(int receverId) {
		this.receverId = receverId;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	
}//end of class Message
