package Application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import Application.BodyPanel.HandlerListener;
import Client.ClientListener;
import Client.ClientReply;
import Client.ClientListener.AlertaTelaListener;
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
	private static ArrayList<Usuario> allNameUsers;
	private static ArrayList<Message> allMessages;
	private static int targetId;

	public final static String hostServer = "localhost";
	public final static int portServer = 5056;

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

			// TODO: gambiarra temporaria
			ArrayList<Usuario> users = getUsersName();
			String names[] = new String[users.size()];
			for (int i = 0; i < users.size(); i++) {
				names[i] = users.get(i).getNome();
			}

			chatPanel.setNewChatWindow(names, users_status);
			break;
		case "main":
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

	public static void setUsersName(ArrayList<Usuario> users) {
		allNameUsers = users;
	}

	public static ArrayList<Usuario> getUsersName() {
		return allNameUsers;
	}

	public static ArrayList<Message> getMessages() {
		return allMessages;
	}

	public static void setMessages(ArrayList<Message> messages) {
		Collections.sort(messages, new Comparator<Message>() {
			public int compare(Message msg1, Message msg2) {
				return msg1.getDate().compareTo(msg2.getDate());
			}
		});
		
		allMessages = messages;
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

	public static int getIdUserByName(String name) {
		for (Usuario user : allNameUsers)
			if (user.getNome() == name)
				return user.getId();

		return -1;
	}
	
	public static void sendMessage(int idReceiver, String msg) {
		RequestResponseData reqRespData = new RequestResponseData(new Message(getUserSession().getId(), idReceiver, msg), MESSAGE);
		sendToServer(reqRespData);
	}

	public static void sendToServer(RequestResponseData reqRespData) {
		switch (reqRespData.getCommand()) {
		case AUTHENTICATE:
			ClientReply cr = new ClientReply(hostServer, portServer, reqRespData);
			ClientListener cl = new ClientListener(hostServer, portServer, cr.connect());
			cl.setAlertaTelaListener(chatPanel.handlerListener);
			new Thread(cr).start();
			new Thread(cl).start();
			break;
		
		case MESSAGE:
			new Thread(new ClientReply(hostServer, portServer, reqRespData)).start();
			break;

		default:
			break;
		}
	}
	
	public static void login(Usuario user) {
		// TODO: Verificar o pq que precisa desses setUserSession
		Core.setUserSession(user);
		Utils.setUSerSession();
		
		RequestResponseData reqRespData = new RequestResponseData(user, AUTHENTICATE);
		sendToServer(reqRespData);
	}
}
