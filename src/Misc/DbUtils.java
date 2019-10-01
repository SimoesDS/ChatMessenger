package Misc;

import Server.DbConnection;

public class DbUtils {
  
  public static Object[][] getUserDialog (int userId, int targetId) {
    return DbConnection.getData("messages", "*", "(message_owner = " + userId + " AND message_receiver = " + targetId + ") OR (message_owner = " + targetId + " AND message_receiver = " + userId + ")");
  }
  
}
