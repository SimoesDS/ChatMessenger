package Server;

import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.CommunicationException;

import com.mysql.cj.jdbc.exceptions.SQLError;

import Misc.Message;
import Misc.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {
	private static final String URL = "jdbc:mysql://db4free.net:3306/schat7";
	private static final String USER = "schat7";
	private static final String PASSWORD = "-2*5.betzNeb$hc~";

	private static Connection conn;

	public static void insertData(String table, String values) {
		try {
			openConnection();
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO " + table + " VALUES (" + values + ")");
		} catch (SQLException e) {
			System.out.println("SQLException in insertData()");
		}
		closeConnection();
	}

	public static int getTableRowsLength(String table) {
		try {
			openConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT count(*) FROM messages");
			int total = 0;
			while (rs.next()) {
				total = rs.getInt(1);
			}
			closeConnection();
			return total;
		} catch (SQLException e) {
			System.out.println("SQLException in rowsLength");
		}
		return 0;
	}

	public static Object[][] getData(String table, String target, String where) {
		try {
			openConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT " + target + " FROM " + table + " WHERE " + where);
			rs.last();
			int rsRowCount = rs.getRow();
			rs.beforeFirst();
			Object result[][] = new Object[rsRowCount][rs.getMetaData().getColumnCount()];

			
			if (rsRowCount == 0)
				return result;

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
		} catch (SQLException e) {
			System.out.println("SQLException in getData()"); // TODO: Deve registrar no logger
		}
		
		return new Object[0][0];
	}
		

	private static void openConnection() throws SQLException {
		conn = DriverManager.getConnection(URL, USER, PASSWORD);
	}

	private static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Invalid query");
		}
	}
	
	public static Object[] login(Usuario userTemp) {
		final String sqlUser = "SELECT * FROM users WHERE user_name = ? and user_password = ?";
		final String sqlMessages = "SELECT * FROM messages WHERE message_owner = ? OR message_receiver = ? order by message_date desc";
		final String sqlAllNameUsers = "SELECT user_id, user_name FROM users where user_id <> ?";

		Object data[] = new Object[5];
		try {
			openConnection();

			PreparedStatement st = conn.prepareStatement(sqlUser);
			st.setString(1, userTemp.getNomeLogin());
			st.setString(2, userTemp.getSenha());
			ResultSet rs = st.executeQuery();

			if (!rs.next())
				return null;

			Usuario user = new Usuario(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("user_login"),
					rs.getString("user_password"), true);

			st = conn.prepareStatement(sqlMessages);
			st.setInt(1, user.getId());
			st.setInt(2, user.getId());
			rs = st.executeQuery();

			rs.last();
			int rsRowCount = rs.getRow();
			rs.beforeFirst();
			Object messages[][] = new Object[rsRowCount][rs.getMetaData().getColumnCount()];
			ArrayList<Message> messagesArr = new ArrayList<Message>();
			
			while (rs.next()) {
				int resultRow = rs.getRow() - 1; 
				messages[resultRow][0] = rs.getInt("message_id");
				messages[resultRow][1] = rs.getString("message_type");
				messages[resultRow][2] = rs.getInt("message_owner");
				messages[resultRow][3] = rs.getInt("message_receiver");
				messages[resultRow][4] = rs.getDate("message_date");
				messagesArr.add(new Message(rs.getInt("message_owner"),
						rs.getInt("message_receiver"),
						rs.getString("message_type"),
						rs.getDate("message_date")));
			}
			
			st = conn.prepareStatement(sqlAllNameUsers);
			st.setInt(1, user.getId());
			rs = st.executeQuery();

			rs.last();
			rsRowCount = rs.getRow();
			rs.beforeFirst();
			ArrayList<Usuario> contactsArr = new ArrayList<Usuario>();
			Object contacts[] = new Object[rsRowCount];

			while (rs.next()) {
				contacts[rs.getRow() - 1] = rs.getString("user_name");
				contactsArr.add(new Usuario(rs.getInt("user_id"), rs.getString("user_name"), true));
			}
			
			data[0] = user;
			data[1] = contacts;
			data[2] = messages;
			data[3] = contactsArr;
			data[4] = messagesArr;
			
			return data;
		} catch (SQLException e) {
			// TODO Registrar no logger
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}

}
