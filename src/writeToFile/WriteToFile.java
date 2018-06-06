package writeToFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

import project1.Message;

public class WriteToFile {
	
	//create a formater for the timeStamp given by the DB
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public static void createMessageFile(Message message) {
		
		if(message != null) {
			
			Path filesFolder = Paths.get("SavedUserMessages");
			
			try {
				//folder to save the txt files
				Files.createDirectories(filesFolder);
				
				
				//the name of the file and the path
				Path messageFilePath = filesFolder.resolve(String.format("%d.txt", message.getMessageId()));
				
				StringBuilder sb = new StringBuilder();
				
				sb.append(String.format("Date: %s", message.getTimeDate().format(WriteToFile.formatter)));
				sb.append(System.lineSeparator());
				
				sb.append(String.format("Sender: %s", message.getSenderUsername()));
				sb.append(System.lineSeparator());
				
				sb.append(String.format("Receiver: %s", message.getReceiverUsername()));
				sb.append(System.lineSeparator());
				
				sb.append(String.format("Subject: %s", message.getSubject()));
				sb.append(System.lineSeparator());
				
				sb.append(String.format("Message: %s", message.getMessage()));
				sb.append(System.lineSeparator());
								
				//write the message in file
				Files.write(messageFilePath, sb.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}//close the WriteToFile()

