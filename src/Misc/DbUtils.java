package Misc;

import Server.DbConnection;

public class DbUtils {
   
  public static void insertMessage(int owner, int receiver, String message) {
    DbConnection.insertData("messages (message_type, message_owner, message_receiver)",  "'" + message + "', " + owner + ", " + receiver);
  }
  
  public static Object[][] getUserDialog (int userId, int targetId) {
    return DbConnection.getData("messages", "*", "(message_owner = " + userId + " AND message_receiver = " + targetId + ") OR (message_owner = " + targetId + " AND message_receiver = " + userId + ")");
  }
  
}
