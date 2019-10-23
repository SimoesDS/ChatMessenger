package Application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import Application.HeaderPanel.KillClientListener;
import Client.ClientListener;
import Client.ClientReply;
import Communication.ICommands;
import Misc.Message;
import Misc.RequestResponseData;
import Misc.Usuario;

public class Core implements ICommands {

	private static BodyPanel chatPanel;
	private static HeaderPanel headerPanel;
	private static JFrame mainFrame;
	private static JScrollPane scroll;
	private static String currentWindowStyle;

	private static Usuario currUser;
	private static ArrayList<Usuario> allNameUsers;
	private static ArrayList<Message> allMessages;
	private static ArrayList<Integer> listNewMSGFromUsers = new ArrayList<Integer>();
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
		chatPanel = new BodyPanel(350);
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
			chatPanel.setNewChatWindow();
			break;
		case "main":
			scroll.setBounds(0, 100, 345, 372);
			chatPanel.setMainWindow(Core.getPreviewData());
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
			String[] balloonData = Core.insertStringBreak(message);
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
			updateApplication("main");
		}
	}

	public static void setBottomScrollPosition() {
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}

	public static void setTopScrollPosition() {
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMinimum());
	}

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
	
	public static void setStatusOfUser(Usuario user) {
		for (Usuario usr : getUsersName()) {
			if (user.getId() == usr.getId())
				if(user.isOnline())
					usr.setOnline();
				else
					usr.setOffline();
		}
	}

	public static ArrayList<Message> getAllMessages() {
		return allMessages;
	}

	public static void setAllMessages(ArrayList<Message> messages) {
		allMessages = messages;
	}

	public static void addMessage(Message messages) {
		allMessages.add(messages);
	}

	public static void setTargetId(int id) {
		targetId = id;
	}
	
	public static int getTargetId() {
		return targetId;
	}

	public static String getCurrWindowStyle() {
		return currentWindowStyle;
	}

	public static int getIdUserByName(String name) {
		for (Usuario user : allNameUsers)
			if (user.getNome() == name)
				return user.getId();

		return -1;
	}

	public static ArrayList<Message> getMessagesInvolvesTarget(int idReceiver) {
		ArrayList<Message> involvedMsg = new ArrayList<Message>();
		for (Message message : getAllMessages())
			if (message.getIdReceiver() == idReceiver || message.getIdSender() == idReceiver)
				involvedMsg.add(message);

		return involvedMsg;
	}

	public static void sendMessage(int idReceiver, String msg) {
		RequestResponseData reqRespData = new RequestResponseData(new Message(getUserSession().getId(), idReceiver, msg),
				MESSAGE);
		sendToServer(reqRespData);
	}

	public static void sendToServer(RequestResponseData reqRespData) {
		switch (reqRespData.getCommand()) {
		case AUTHENTICATE:
			ClientReply cr = new ClientReply(hostServer, portServer, reqRespData);
			ClientListener cl = new ClientListener(cr.connect());
			cl.setAlertaTelaListener(chatPanel.handlerListener);
			new Thread(cr).start();
			new Thread(cl).start();
			break;

		case MESSAGE:
			new Thread(new ClientReply(hostServer, portServer, reqRespData)).start();
			addMessage(reqRespData.getMessage());
			if(reqRespData.getMessage().getIdSender() != targetId)
				addNewMSGFromUser(reqRespData.getMessage().getIdSender());
			break;
		case LOGOUT:
			//headerPanel.killClientListener.kill(Core.getUserSession());
			new Thread(new ClientReply(hostServer, portServer, reqRespData)).start();
			break;
		default:
			break;
		}
	}

	public static void login(Usuario user) {
		// TODO: Verificar o pq que precisa desses setUserSession
		Core.setUserSession(user);

		RequestResponseData reqRespData = new RequestResponseData(user, AUTHENTICATE);
		sendToServer(reqRespData);
	}
	
	public static void logout() {
		sendToServer(new RequestResponseData(getUserSession(), LOGOUT));
	}
	
	public static void setKillClientListener(KillClientListener killListener) {
		headerPanel.setKillClientListener(killListener);
	}
	
	public static ArrayList<Integer> getListNewMSGFromUsers() {
		return listNewMSGFromUsers;
	}
	
	public static void addNewMSGFromUser(int idUser) {
		if(!listNewMSGFromUsers.contains(idUser))
			listNewMSGFromUsers.add(idUser);
	}
	
	public static void removeNewMSGFromUser(int idUser) {
		for (int i = 0; i < listNewMSGFromUsers.size(); i++) {
			if(listNewMSGFromUsers.get(i) == idUser) {
				listNewMSGFromUsers.remove(i);
				break;
			}
		}
	}

	public static ArrayList<Object[]> getPreviewData() {
		ArrayList<Usuario> usersArr = getUsersName();
		ArrayList<Message> messages = getAllMessages();
		ArrayList<Object[]> messagesPrevil = new ArrayList<>();

		if (messages.size() > 0) {
			for (int i = 0; i < usersArr.size(); i++) {
				int qtdMsgs = getMessagesInvolvesTarget(usersArr.get(i).getId()).size();
				if (qtdMsgs > 0) {
					Message lastMessage = getMessagesInvolvesTarget(usersArr.get(i).getId()).get(qtdMsgs - 1);
					messagesPrevil.add(new Object[] { usersArr.get(i).getNome(), lastMessage.getMessage(), usersArr.get(i).isOnline() });
				}
			}
		}

		return messagesPrevil;
	}
	
	public static String[] insertStringBreak(String string) {
		String aux = "";
		String returnTarget[] = new String[2];
		int heightPlus = 22;

		Pattern p = Pattern.compile(".{20,30}(\\s)");
		Matcher m = p.matcher(string);
		while (m.find()) {
			aux += m.group() + "<br />";
			heightPlus += 13;
		}

		aux += "<html>" + string.replace(aux.replaceAll("<br />", ""), "") + "</html>";
		if (string.length() > 20 && m.groupCount() == 1)
			heightPlus += 9;

		returnTarget[0] = aux;
		returnTarget[1] = String.valueOf(heightPlus);

		return returnTarget;
	}
	
	
}
