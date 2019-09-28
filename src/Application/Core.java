package Application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import Client.ClientReply;
import Communication.ICommands;
import Misc.Message;
import Misc.RequestResponseData;
import Misc.Usuario;
import Misc.Utils;

public class Core implements ICommands {

	private static BodyPanel chatPanel;
	private static HeaderPanel headerPanel;
	private static JFrame mainFrame;
	private static JScrollPane scroll;
	private static boolean users_status[] = { true, false, true, false, true, true };
	private static String currentWindowStyle;

	private static Usuario currUser;
	private static String allNameUsers[];
	private static ArrayList<Usuario> allNameUsersArr;
	private static Object allMessages[];
	private static ArrayList<Message> allMessagesArr;
	private static int targetId;

	public final static String hostServer = "localhost";
	private final static int portServer = 5056;

	public static void initializeApp() {
		mainFrame = new JFrame("SChat");
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		int x = (int) (dimension.getWidth() - 350) / 2;
		int y = (int) (dimension.getHeight() - 500) / 2;

		mainFrame.setBounds(x, y, 350, 500);
		mainFrame.setLayout(null);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		headerPanel = new HeaderPanel(350, 320);
		chatPanel = new BodyPanel(350, 2);
		scroll = new JScrollPane(chatPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scroll.getVerticalScrollBar().setUnitIncrement(5);
		scroll.setBounds(0, 320, 345, 300);

		mainFrame.add(headerPanel);
		mainFrame.add(scroll);

		Core.updateApplication("login");
	}

	public static void buildDialogWindow(Usuario userTarget) {
		currentWindowStyle = "dialog";
		scroll.setBounds(0, 100, 345, 372);
		chatPanel.setDialogWindow(userTarget.getId());
		headerPanel.setDialogWindow(userTarget.getNome());
		mainFrame.repaint();
		mainFrame.revalidate();
	}

	public static void updateApplication(String style) {
		currentWindowStyle = style;
		switch (style) {
		case "newChat":
			scroll.setBounds(0, 100, 345, 372);
			chatPanel.setNewChatWindow(getUsersName(), users_status);
			break;
		case "main":
			// TODO: try temporario
			try {
				ArrayList<Object[]> data = Utils.getPreviewData();
				String prUsers[] = new String[data.size()];
				String prMessages[] = new String[data.size()];
				
				for (int j = 0; j < data.size(); j++) {
					prUsers[j] = (String) ((Object[]) data.get(j))[0];
					prMessages[j] = (String) ((Object[]) data.get(j))[1];
				}

				scroll.setBounds(0, 100, 345, 372);
				chatPanel.setConversations(prUsers.length);
				chatPanel.setMainWindow(prUsers, prMessages, users_status);
				headerPanel.setMainWindow();
				headerPanel.setSize(350, 100);
				setTopScrollPosition();
			} catch (Exception e) {
				e.getStackTrace();
			}
			break;
		case "login":
			scroll.setBounds(0, 320, 345, 152);
			chatPanel.setPreferredSize(new Dimension(345, 149));
			chatPanel.setLoginWindow();
			headerPanel.setLoginWindow();
			headerPanel.setSize(350, 320);
			break;
		default:
			System.out.println("WINDOW STYLE NOT FOUND");
		}

		mainFrame.repaint();
		mainFrame.revalidate();
	}

	public static void addChatMessage(String message, String way) {
		if (currentWindowStyle == "dialog") {
			String[] balloonData = Utils.insertStringBreak(message);
			int BalloonHeights = chatPanel.getBallonsTotalHeight();
			int balloonCurrHeight = Integer.parseInt(balloonData[1]);

			boolean isMe = (way == "out") ? true : false;

			chatPanel.removeInputArea();
			chatPanel.addMessageballoon(isMe, balloonData[0], balloonCurrHeight, BalloonHeights + 5);
			chatPanel.createInputArea(BalloonHeights < 290 - balloonCurrHeight ? 290 : BalloonHeights + balloonCurrHeight + 5,
					targetId);

			chatPanel.setPreferredSize(new Dimension(350, BalloonHeights + balloonCurrHeight + 80));

			mainFrame.revalidate();
			mainFrame.repaint();
			setBottomScrollPosition();
		} else {
			try {
				Thread.sleep(1000);
				updateApplication("main");
			} catch (InterruptedException ex) {
				Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public static void setBottomScrollPosition() {
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}

	public static void setTopScrollPosition() {
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMinimum());
	}

	// METHODS RELATED TO USER_DATA
	public static void setUserSession(Usuario user) {
		currUser = user;
	}

	public static Usuario getUserSession() {
		return currUser;
	}

	public static void setUsersName(Object users[]) {
		allNameUsers = new String[users.length];
		for (int i = 0; i < users.length; i++) {
			allNameUsers[i] = (String) users[i];
		}
	}

	public static void setUsersNameArr(ArrayList<Usuario> users) {
		allNameUsersArr = users;
	}

	public static String[] getUsersName() {
		return allNameUsers;
	}

	public static ArrayList<Usuario> getUsersNameArr() {
		return allNameUsersArr;
	}

	public static void setMessages(Object msg[]) {
		allMessages = msg;
	}

	public static Object[] getMessages() {
		return allMessages;
	}

	public static ArrayList<Message> getMessagesArr() {
		return allMessagesArr;
	}

	public static void setMessagesArr(ArrayList<Message> messageArr) {
		allMessagesArr = messageArr;
	}

	public static void setAnotherUsersStatus(boolean status[]) {
		users_status = status;
	}

	public static boolean[] getAnotherUsersStatus(boolean status[]) {
		return users_status;
	}

	public static int getTargetId() {
		return targetId;
	}

	public static void setTargetId(int id) {
		targetId = id;
	}

	public static String getCurrWindowStyle() {
		return currentWindowStyle;
	}

	public static void setCurrWindowStyle(String s) {
		currentWindowStyle = s;
	}

	public static BodyPanel getBodyInstance() {
		return chatPanel;
	}

	public static HeaderPanel getHeaderInstance() {
		return headerPanel;
	}

	public static JScrollPane getScrollInstance() {
		return scroll;
	}

	public static JFrame getMainFrameInstance() {
		return mainFrame;
	}

	public static boolean[] getUsersStatusInfo() {
		return users_status;
	}

	public static void replyToServer(RequestResponseData requestResponseData) {
		switch (requestResponseData.getCommand()) {
		case MESSAGE:
			new Thread(new ClientReply(hostServer, portServer, requestResponseData)).start();
			break;

		default:
			break;
		}

	}
}
