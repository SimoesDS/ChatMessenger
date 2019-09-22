package Server;

import java.sql.Statement;

import Application.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
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
		

	public static void openConnection() throws SQLException {
		conn = DriverManager.getConnection(URL, USER, PASSWORD);
	}

	private static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Invalid query");
		}
	}

	public Usuario getUserByID(int userID) {
		return new Usuario();
	}

	public static Usuario userLogin(String userName, String password) {
		final String sql = "SELECT * FROM users WHERE user_name = ? and user_password = ?";
		try {
			openConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, userName);
			st.setString(2, password);
			ResultSet rs = st.executeQuery();

			if (!rs.next())
				return null;

			return new Usuario(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("user_login"),
					rs.getString("user_password"), true);

		} catch (SQLException e) {
			// TODO Registrar no logger
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}

}
