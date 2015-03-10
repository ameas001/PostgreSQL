/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Messenger {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Messenger
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Messenger (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Messenger

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException { 
      // creates a statement object 
      Statement stmt = this._connection.createStatement (); 
 
      // issues the query instruction 
      ResultSet rs = stmt.executeQuery (query); 
 
      /* 
       ** obtains the metadata object for the returned result set.  The metadata 
       ** contains row and column info. 
       */ 
      ResultSetMetaData rsmd = rs.getMetaData (); 
      int numCol = rsmd.getColumnCount (); 
      int rowCount = 0; 
 
      // iterates through the result set and saves the data returned by the query. 
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>(); 
      while (rs.next()){
          List<String> record = new ArrayList<String>(); 
         for (int i=1; i<=numCol; ++i) 
            record.add(rs.getString (i)); 
         result.add(record); 
      }//end while 
      stmt.close (); 
      return result; 
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       if(rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }
   
   public int executeQueryInt (String query, String val) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int id = 0;
       while ( rs.next() ) {
            id = rs.getInt(val);
       }
            
       stmt.close ();
       return id;
   }
   
   public List<String> executeQueryString (String query, String val) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       List<String> li = new ArrayList<String>();
       String id = "";
       while ( rs.next() ) {
            id = rs.getString(val);
            li.add(id);
            //System.out.print(id);
       }
       stmt.close ();
       return li;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current 
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();
	
	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Messenger.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if
      
      Greeting();
      Messenger esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Messenger object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Messenger (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. Add to contact list");
                System.out.println("2. Browse contact list");
                System.out.println("3. Delete from contact list");
                System.out.println("4. Write a new message");
                System.out.println("5. Browse chats");
                System.out.println("6. Add to blocked list");
                System.out.println("7. Browse blocked list");
                System.out.println("8. Delete from blocked list");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                   case 1: AddToContact(esql, authorisedUser); break;
                   case 2: ListContacts(esql, authorisedUser); break;
                   case 3: DeleteContact(esql, authorisedUser); break;
                   case 4: NewMessage(esql, authorisedUser); break;
                   case 5: ListChats(esql, authorisedUser); break;
                   case 6: AddToBlocked(esql, authorisedUser); break;
                   case 7: ListBlocked(esql, authorisedUser); break;
                   case 8: DeleteBlocked(esql, authorisedUser); break;
                   case 9: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main
  
   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user with privided login, passowrd and phoneNum
    * An empty block and contact list would be generated and associated with a user
    **/
   public static void CreateUser(Messenger esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         System.out.print("\tEnter user phone: ");
         String phone = in.readLine();

	 //Creating empty contact\block lists for a user
	 esql.executeUpdate("INSERT INTO USER_LIST(list_type) VALUES ('block')");
	 int block_id = esql.getCurrSeqVal("user_list_list_id_seq");
         esql.executeUpdate("INSERT INTO USER_LIST(list_type) VALUES ('contact')");
	 int contact_id = esql.getCurrSeqVal("user_list_list_id_seq");
         
	 String query = String.format("INSERT INTO USR (phoneNum, login, password, block_list, contact_list) VALUES ('%s','%s','%s',%s,%s)", phone, login, password, block_id, contact_id);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end
   
   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Messenger esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM Usr WHERE login = '%s' AND password = '%s'", login, password);
         int userNum = esql.executeQuery(query);
	 if (userNum > 0)
		return login;
     return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

   //1. Add to contact list
   public static void AddToContact(Messenger esql, String a_user){
      try{
        System.out.print("\tEnter user: ");
        String user = in.readLine();
        
        //Check if existing user
        String query = String.format("SELECT login FROM USR WHERE login = '%s'", user);
        int userNum = esql.executeQuery(query);
        if(userNum == 0){
        	System.out.print("\tNot a user\n");
        	return;
        }
        
        //check if user is in blocked list
        query = String.format("SELECT C.list_member FROM USER_LIST L, USR U, USER_LIST_CONTAINS C WHERE L.list_id = C.list_id AND U.block_list = L.list_id AND C.list_member = '%s' AND L.list_type = 'block' AND U.login = '%s'", user, a_user);
        userNum = esql.executeQuery(query);
        if(userNum > 0){
        	System.out.print("\tUser is in your blocked list. Cannot add to contacts until deleted from blocked list.\n");
        	return;
        }
        
        //Check if user is already in contact list
        query = String.format("SELECT C.list_member FROM USER_LIST L, USR U, USER_LIST_CONTAINS C WHERE L.list_id = C.list_id AND U.contact_list = L.list_id AND C.list_member = '%s' AND L.list_type = 'contact' AND U.login = '%s'", user, a_user);
        userNum = esql.executeQuery(query);
        if(userNum > 0){
        	System.out.print("\tUser is already in your contact list.\n");
        	return;
        }
        
        //Check it's not yourself
        if(user.trim().matches(a_user.trim())){
        	System.out.print("\tYou cannot add yourself!\n");
        	return;
       	}
        
        query = String.format("SELECT contact_list FROM USR WHERE login = '%s'", a_user);
        int contact_list = esql.executeQueryInt(query, "contact_list");
        //System.out.print(contact_list);
        query = String.format("INSERT INTO USER_LIST_CONTAINS (list_id, list_member) VALUES (%d, '%s')", contact_list, user);
        esql.executeUpdate(query);
        
        query = String.format("SELECT contact_list FROM USR WHERE login = '%s'", user);
        contact_list = esql.executeQueryInt(query, "contact_list");
        //System.out.print(contact_list);
        query = String.format("INSERT INTO USER_LIST_CONTAINS (list_id, list_member) VALUES (%d, '%s')", contact_list, a_user);
        esql.executeUpdate(query);
        System.out.print("\tContact added!\n");
      }
      catch(Exception e){
        System.err.println (e.getMessage ());
      }
   }//end
   
   //2. Browse contact list
   public static void ListContacts(Messenger esql, String a_user){
      try{
      	//String query = String.format("SELECT C.list_member FROM USR U, USER_LIST L, USER_LIST_CONTAINS C WHERE U.login = '%s' AND U.contact_list = L.list_id AND L.list_id = C.list_id AND L.list_type = 'contact'", a_user);
		System.out.print("\t---Contacts---\n");
		String query = String.format("SELECT contact_list FROM USR WHERE login = '%s'", a_user);
		
        int contact_list = esql.executeQueryInt(query, "contact_list");
        //query = String.format("SELECT list_member FROM USER_LIST_CONTAINS WHERE list_id = %d", contact_list);
        query = String.format("SELECT C.list_member FROM USR U, USER_LIST_CONTAINS C WHERE U.login = '%s' AND U.contact_list = C.list_id", a_user);
        List<String> contacts = new ArrayList<String>();
        contacts = esql.executeQueryString(query, "list_member");
      	for(int i = 0; i < contacts.size(); ++i){
      	    System.out.print(contacts.get(i).trim() + "\n");
      	}
      }
      catch(Exception e){
        System.err.println (e.getMessage ());
      }
   }//end
   
   //3. Delete from contact list
   public static void DeleteContact(Messenger esql, String a_user){
      try{
        System.out.print("\tEnter user: ");
        String user = in.readLine();
        	//delete from contact
        String query = String.format("SELECT contact_list FROM USR WHERE login = '%s'", a_user);
        int contact_list = esql.executeQueryInt(query, "contact_list");
        query = String.format("DELETE FROM USER_LIST_CONTAINS WHERE list_id = %d AND list_member = '%s'", contact_list, user);
        esql.executeUpdate(query);
        
        query = String.format("SELECT contact_list FROM USR WHERE login = '%s'", user);
        contact_list = esql.executeQueryInt(query, "contact_list");
        query = String.format("DELETE FROM USER_LIST_CONTAINS WHERE list_id = %d AND list_member = '%s'", contact_list, a_user);
        esql.executeUpdate(query);
        System.out.print("\tContact has been deleted from contact list!\n");
      }
      catch(Exception e){
        System.err.println (e.getMessage ());
      }
   }//end 
   
   //4. Write a new message
   public static void NewMessage(Messenger esql, String a_user){
      try{
      //esql.getCurrSeqVal("user_list_list_id_seq");
        System.out.print("\tWho do you want to send a message to: ");
        String user = in.readLine();
        
        System.out.print("\tEnter message: ");
        String msg = in.readLine();
        
        //CHAT
        //query = String.format("SELECT contact_list FROM USR WHERE login = '%s'", a_user);
        //int contact_list = esql.executeQueryInt(query, "contact_list");
        //System.out.print(contact_list);
        String query = String.format("INSERT INTO CHAT (chat_type, init_sender) VALUES ('%s', '%s')", "private", a_user);
        esql.executeUpdate(query);
        
        //CHAT_LIST
        query = String.format("SELECT chat_id FROM CHAT WHERE init_sender = '%s'", a_user);
        int chat_id = esql.executeQueryInt(query, "chat_id");
        //System.out.print(contact_list);
        query = String.format("INSERT INTO CHAT_LIST (chat_id, member) VALUES (%d, '%s')", chat_id, user);
        esql.executeUpdate(query);
        
        //MESSAGE
        
        //query = String.format("SELECT contact_list FROM USR WHERE login = '%s'", a_user);
        //int contact_list = esql.executeQueryInt(query, "contact_list");
        //System.out.print(contact_list);
        Date time = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        String begin = dateFormat.format(time);
        query = String.format("INSERT INTO MESSAGE ( msg_text, msg_timestamp, sender_login, chat_id) VALUES ('%s', '%s', '%s', %d)", msg, begin, a_user, chat_id);
        esql.executeUpdate(query);
        
        System.out.print("\tMessage sent!\n");
      }
      catch(Exception e){
        System.err.println (e.getMessage ());
      }
   }//end 
   
   //6. Browse chats
   public static void ListChats(Messenger esql, String a_user){
      try{
        System.out.print("\t----Chats----\n");
		String query = String.format("SELECT chat_id FROM CHAT WHERE init_sender = '%s'", a_user);
        int chat_id = esql.executeQueryInt(query, "chat_id");
        //query = String.format("SELECT list_member FROM USER_LIST_CONTAINS WHERE list_id = %d", contact_list);
        query = String.format("SELECT L.member FROM CHAT C, CHAT_LIST L WHERE C.chat_id = %d AND L.chat_id = C.chat_id", chat_id);
        List<String> contacts = new ArrayList<String>();
        contacts = esql.executeQueryString(query, "member");
      	for(String i : contacts){
      	    System.out.print(i);
      	    System.out.print("\t \n");
      	}
      	
      	query = String.format("SELECT chat_id FROM CHAT_LIST WHERE member = '%s'", a_user);
        chat_id = esql.executeQueryInt(query, "chat_id");
        //query = String.format("SELECT list_member FROM USER_LIST_CONTAINS WHERE list_id = %d", contact_list);
        query = String.format("SELECT C.init_sender FROM CHAT C, CHAT_LIST L WHERE L.chat_id = %d AND L.chat_id = C.chat_id", chat_id);
        List<String> contacts2 = new ArrayList<String>();
        contacts = esql.executeQueryString(query, "init_sender");
      	for(String i : contacts2){
      	    System.out.print(i);
      	    System.out.print("\t \n");
      	}
      }
      catch(Exception e){
        System.err.println (e.getMessage ());
      }
   }//end Query6
   
   //7. Add to blocked list
   public static void AddToBlocked(Messenger esql, String a_user){
      try{
        System.out.print("\tEnter user: ");
        String user = in.readLine();
        
        //Check if existing user
        String query = String.format("SELECT login FROM USR WHERE login = '%s'", user);
        int userNum = esql.executeQuery(query);
        if(userNum == 0){
        	System.out.print("\tNot a user\n");
        	return;
        }
        
        //check if user is in blocked list
        query = String.format("SELECT C.list_member FROM USER_LIST L, USR U, USER_LIST_CONTAINS C WHERE L.list_id = C.list_id AND U.block_list = L.list_id AND C.list_member = '%s' AND L.list_type = 'block' AND U.login = '%s'", user, a_user);
        userNum = esql.executeQuery(query);
        if(userNum > 0){
        	System.out.print("\tUser is already in your blocked list.\n");
        	return;
        }
        
        //Check it's not yourself
        if(user.trim().matches(a_user.trim())){
        	System.out.print("\tYou cannot block yourself!\n");
        	return;
       	}
        
        //Check if user is already in contact list
        query = String.format("SELECT C.list_member FROM USER_LIST L, USR U, USER_LIST_CONTAINS C WHERE L.list_id = C.list_id AND U.contact_list = L.list_id AND C.list_member = '%s' AND L.list_type = 'contact' AND U.login = '%s'", user, a_user);
        userNum = esql.executeQuery(query);
        if(userNum > 0){
        	System.out.print("\tUser is in your contact list.\n");
        	//delete from contact
        	query = String.format("SELECT contact_list FROM USR WHERE login = '%s'", a_user);
        	int contact_list = esql.executeQueryInt(query, "contact_list");
        	query = String.format("DELETE FROM USER_LIST_CONTAINS WHERE list_id = %d AND list_member = '%s'", contact_list, user);
        	esql.executeUpdate(query);
        	System.out.print("\tContact has been deleted from contact list!\n");
        	
        	query = String.format("SELECT block_list FROM USR WHERE login = '%s'", a_user);
        	int block_list = esql.executeQueryInt(query, "block_list");
        	query = String.format("INSERT INTO USER_LIST_CONTAINS (list_id, list_member) VALUES (%d, '%s')", block_list, user);
        	esql.executeUpdate(query);
        	System.out.print("\tContact added to blocked list!\n");
        	return;
        }
        
        query = String.format("SELECT block_list FROM USR WHERE login = '%s'", a_user);
        int block_list = esql.executeQueryInt(query, "block_list");
        query = String.format("INSERT INTO USER_LIST_CONTAINS (list_id, list_member) VALUES (%d, '%s')", block_list, user);
        esql.executeUpdate(query);
        System.out.print("\tContact added to blocked list!\n");
      }
      catch(Exception e){
        System.err.println (e.getMessage ());
      }
   }//end
   
   //8. Browse blocked list
   public static void ListBlocked(Messenger esql, String a_user){
      try{
      	System.out.print("\t---Blocked---\n");
		String query = String.format("SELECT block_list FROM USR WHERE login = '%s'", a_user);
		
        int block_list = esql.executeQueryInt(query, "block_list");
        //query = String.format("SELECT list_member FROM USER_LIST_CONTAINS WHERE list_id = %d", contact_list);
        query = String.format("SELECT C.list_member FROM USR U, USER_LIST_CONTAINS C WHERE U.login = '%s' AND U.block_list = C.list_id", a_user);
        List<String> contacts = new ArrayList<String>();
        contacts = esql.executeQueryString(query, "list_member");
      	for(String i : contacts){
      	    System.out.print(i);
      	    System.out.print("\t \n");
      	}
      }
      catch(Exception e){
        System.err.println (e.getMessage ());
      }
   }//end
   
   //9. Delete from blocked list
   public static void DeleteBlocked(Messenger esql, String a_user){
      try{
        System.out.print("\tEnter user: ");
        String user = in.readLine();
        	//delete from contact
        String query = String.format("SELECT block_list FROM USR WHERE login = '%s'", a_user);
        int block_list = esql.executeQueryInt(query, "block_list");
        query = String.format("DELETE FROM USER_LIST_CONTAINS WHERE list_id = %d AND list_member = '%s'", block_list, user);
        esql.executeUpdate(query);
        System.out.print("\tContact has been deleted from block list!\n");
      }
      catch(Exception e){
        System.err.println (e.getMessage ());
      }
   }//end 

}//end Messenger
