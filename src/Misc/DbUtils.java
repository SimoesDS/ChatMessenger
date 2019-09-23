package Misc;

import Server.DbConnection;
import Application.Usuario;
import java.util.Arrays;

public class DbUtils {
   
  public static void insertMessage(int owner, int receiver, String message) {
    DbConnection.insertData("messages (message_type, message_owner, message_receiver)",  "'" + message + "', " + owner + ", " + receiver);
  }
  
  
  public static Object[] validateUserAccess (String userName, String password) {
    try { return DbConnection.getData("users", "*", "user_login = '" + userName + "' AND user_password = '" + password + "'")[0]; } 
    catch(ArrayIndexOutOfBoundsException e) { return new Object[0]; }
  }
  
  public static Object[][] getDifferentUsers (int id) {
    return DbConnection.getData("users", "*", "user_id <> " + id); 
  }
  
  public static Object[] findUserByName (String name) {
    return DbConnection.getData("users", "*", "user_name = '" + name + "'")[0];
  }
  
  public static Object[] findUserById (int id) {
    return DbConnection.getData("users", "*", "user_id = '" + id + "'")[0];
  }
  
  public static Object[][] getUserDialog (int userId, int targetId) {
    return DbConnection.getData("messages", "*", "(message_owner = " + userId + " AND message_receiver = " + targetId + ") OR (message_owner = " + targetId + " AND message_receiver = " + userId + ")");
  }
  
  public static Object[][] getEnvolvedUserMessage (int userId) {
    return DbConnection.getData("messages", "*", "message_owner = " + userId + " OR message_receiver = " + userId);
  }
  
  public static Object[][] getDirectionedUserMessage (int targetId, int owner) {
    return DbConnection.getData("messages", "*", "message_owner = " + owner + " AND message_receiver = " + targetId);
  }
  
  public static Usuario selectIdAsUsuario (int idUsuario) {
    Usuario usuario = new Usuario();
    Object obj[] =  DbConnection.getData("users", "*", "user_id = '" + idUsuario + "'")[0];
    usuario.setId((int) obj[0]);
    usuario.setNome((String) obj[1]);
    usuario.setNomeLogin((String) obj[2]);
    usuario.setSenha((String) obj[3]);
    return usuario;
  }
  
  public static Usuario selectLoginAsUsuario (String login) {
    Object obj[][] =  DbConnection.getData("users", "*", "user_login = '" + login + "'");
    if (obj.length > 0) {
      Usuario usuario = new Usuario();
      usuario.setId((Integer.parseInt(String.valueOf(obj[0][0]))));
      usuario.setNome((String) obj[0][1]);
      usuario.setNomeLogin((String) obj[0][2]);
      usuario.setSenha((String) obj[0][3]);
      return usuario;    }
    return null;
  }
  
}
