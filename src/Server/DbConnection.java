package Server;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {
  private static final String DRIVER = "com.mysql.jdbc.Driver";
  //private static final String URL = "jdbc:mysql://localhost:3306/aps4db";
  //private static final String USER = "root";
  //private static final String PASSWORD = "root";
  private static final String URL = "jdbc:mysql://db4free.net:3306/ccp68aps72";
  private static final String USER = "ccp68aps7cdrv2";
  private static final String PASSWORD = "HhC$K#@:G%&f.kF";
  
  private static Connection conn;
  private static Object result[][];
   
  public static void insertData(String table, String values) {
    openConnection();
    try {
      Statement st = conn.createStatement();
      st.executeUpdate("INSERT INTO " + table + " VALUES (" + values + ")");
    }
    catch(SQLException e) {System.out.println("SQLException in insertData()");}
    closeConnection();
  }
  
  public static int getTableRowsLength (String table) {
    try {
      openConnection();
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("SELECT count(*) FROM messages");
      int total = 0;
      while(rs.next()) {
        total = rs.getInt(1);
      }
      closeConnection();
      return total;
    } catch(SQLException e) {System.out.println("SQLException in rowsLength");}
    return 0;
  }
  
  public static Object[][] getData (String table, String target, String where) {
    try {
      openConnection();
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("SELECT " + target + " FROM " + table + " WHERE " + where);
      rs.last(); int rsRowCount = rs.getRow(); rs.beforeFirst();
      Object result[][] = new Object[rsRowCount][rs.getMetaData().getColumnCount()];  

      if (rsRowCount == 0) {
        closeConnection();
        return result;
      }
      int resultRow = 0;
      while (rs.next()) {
        switch (table) {
          case "users":
            result[resultRow][0] = rs.getInt("user_id");
            result[resultRow][1] = rs.getString("user_name");
            result[resultRow][2] = rs.getString("user_login");
            result[resultRow][3] = rs.getString("user_password");
            break;
          case "messages":
            result[resultRow][0] = rs.getInt("message_id");
            result[resultRow][1] = rs.getString("message_type");
            result[resultRow][2] = rs.getString("message_owner");
            result[resultRow][3] = rs.getString("message_receiver");
            result[resultRow][4] = rs.getString("message_date");
            break;
          case "dialogs":
            result[resultRow][0] = rs.getInt("dialog_id");
            result[resultRow][1] = rs.getInt("dialog_owner");
            result[resultRow][2] = rs.getInt("dialog_receiver");
            result[resultRow][3] = rs.getInt("message_id");
            result[resultRow][4] = rs.getDate("date_info");
            break;
          default:
            System.out.println("TABLE NOT FOUND: " + table);
        }
        resultRow++;
      }
      closeConnection();
      return result;
    }
    catch(SQLException e) {System.out.println("SQLException in getData()");}
    return result;
  }
    
  public static void openConnection() {
    try {
      //Class.forName(DRIVER);
      conn = DriverManager.getConnection(URL,USER,PASSWORD);  
    }
   // catch(ClassNotFoundException e) {System.out.println("Class does not exists");}
    catch(SQLException e) {e.printStackTrace();}
  }
  
  private static void closeConnection() {
    try {
      conn.close();
    } 
    catch(SQLException e) {System.out.println("Invalid query");}
  }
}