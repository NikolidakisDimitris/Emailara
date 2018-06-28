<h1>Emailara or the "Greatest Email"</h1>

Java email console Application

To run this application you have to follow the next path (..\emailara\src\dBase)  to find the "ConnectionClass .java” file to complete the below mentioned data:

a) Your MySQL server URL
b) Your Username
c) Your password

You also have to import as an external JAR file the MySQL connector available on the MySQL official web site. </br>The driver is the following: mysql-connector-java-5.1.46-bin.jar


Application Functionality :

The application creates the necessary database and tables if they don’t exist. 

The application creates automatically the admin user, who can create other users. The admin user is unique and he can’t be deleted.

The admin can see all the registered users, he can delete them, or to change the role of each user. 

The available roles for the users are:

simpleUser: He can send messages to the other users. He can also see his inbox and his sent folder.

midUser: He has the simpleUser’s functionality, and also he edit his messages and move them to thrash, and from trash back to sent/inbox.

superUser: He has the midUser’s functionality, but he can also delete his messages. 

The messages are also been written in a .txt file. 
