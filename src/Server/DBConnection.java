package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import Communication.RequestResponseData;
import Misc.Message;
import Misc.Usuario;

public class DBConnection {
	private String url = "";
	private String user = "";
	private String password = "";
	private Connection conn;
	
	public DBConnection(String url, String user, String password) {
		this.url = "jdbc:mysql://" + url;
		this.user = user;
		this.password = password;
	}
	
	public boolean testConn() {
		try {
			openConnection();
			closeConnection();
			return true;			
		} catch (SQLException e) {
			e.getStackTrace();
			return false;
		}
	}

	public int saveMessage(RequestResponseData reqRespData) {
		final String sqlIsertMsg = "INSERT INTO messages (message_type, message_owner, message_receiver) VALUES (?, ?, ?)";

		try {
			openConnection();
			PreparedStatement ps = conn.prepareStatement(sqlIsertMsg);
			ps.setString(1, reqRespData.getMsg());
			ps.setInt(2, reqRespData.getIdSender());
			ps.setInt(3, reqRespData.getIdReceiver());

			return ps.executeUpdate();
		} catch (SQLException e) {
			e.getStackTrace();
		} finally {
			closeConnection();
		}
		return -1;
	}

	private void openConnection() throws SQLException {
		conn = DriverManager.getConnection(url, user, password);
	}

	private void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public RequestResponseData login(Usuario userTemp) {
		final String sqlUser = "SELECT * FROM users WHERE user_login = ? and user_password = ?";
		final String sqlMessages = "SELECT * FROM messages WHERE message_owner = ? OR message_receiver = ? order by message_date";
		final String sqlAllNameUsers = "SELECT user_id, user_name FROM users where user_id <> ? order by user_name";

		try {
			openConnection();

			PreparedStatement st = conn.prepareStatement(sqlUser);
			st.setString(1, userTemp.getNomeLogin());
			st.setString(2, userTemp.getSenha());
			ResultSet rs = st.executeQuery();

			if (!rs.next())
				return null;

			Usuario user = new Usuario(rs.getInt("user_id"), rs.getString("user_name"), true);

			st = conn.prepareStatement(sqlMessages);
			st.setInt(1, user.getId());
			st.setInt(2, user.getId());
			rs = st.executeQuery();

			ArrayList<Message> messages = new ArrayList<Message>();

			while (rs.next()) {
				Date data = new Date(rs.getTimestamp("message_date").getTime());
				messages.add(new Message(rs.getInt("message_owner"), rs.getInt("message_receiver"),
						rs.getString("message_type"), data));
			}

			st = conn.prepareStatement(sqlAllNameUsers);
			st.setInt(1, user.getId());
			rs = st.executeQuery();

			ArrayList<Usuario> contacts = new ArrayList<Usuario>();

			while (rs.next())
				contacts.add(new Usuario(rs.getInt("user_id"), rs.getString("user_name")));

			return new RequestResponseData(user, contacts, messages);
		} catch (SQLException e) {
			// TODO Registrar no logger
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}

}
