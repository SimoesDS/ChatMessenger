package Application;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Client.ClientListener.AlertaTelaListener;
import Communication.ICommands;
import Communication.RequestResponseData;
import Misc.Message;
import Misc.Usuario;

public class BodyPanel extends JPanel {

	private final int CHAT_HEIGHT = 76;
	HandlerListener handlerListener = new HandlerListener();
	private JLabel invalidLabel = null;

	BodyPanel(int width) {
		this.setLayout(null);
	}

	public void setMainWindow(ArrayList<Object[]> messagesPrevil) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.removeAll();
		this.setPreferredSize(new Dimension(350, messagesPrevil.size() * CHAT_HEIGHT));
		Core.setTargetId(-1);

		if (messagesPrevil.size() == 0) {
			JLabel noConversation = new JLabel("Não há conversas existentes");
			noConversation.setBounds(50, 20, 100, 20);
			noConversation.setForeground(new Color(160, 160, 160));
			noConversation.setBounds(76, 80, 200, 30);
			noConversation.setFont(new Font("Times New Roman", Font.BOLD, 16));

			this.add(noConversation);
		}

		int lastY = 0;

		for (int i = 0; i < messagesPrevil.size(); i++) {
			int index = i;

			String name = (String) messagesPrevil.get(index)[0];
			Message lastMessage = (Message) messagesPrevil.get(index)[1];
			boolean status = (boolean) messagesPrevil.get(index)[2];
			int targetId = Core.getIdUserByName(name);

			JPanel currPanel = new JPanel();
			currPanel.setLayout(null); // retirar
			currPanel.setBounds(0, lastY, 350, 75);
			currPanel.setBackground(new Color(224, 224, 224));

			Color statusColor = status == true ? new Color(0, 255, 0) : new Color(255, 255, 255);
			JPanel statusPanel = new JPanel();
			statusPanel.setBounds(30, 25, 10, 10);
			statusPanel.setBackground(statusColor);

			JLabel nameLabel = new JLabel(name);
			nameLabel.setBounds(50, 20, 240, 20);
			nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
			
			JLabel lastMessageLabel = new JLabel(lastMessage.getMessage());
			lastMessageLabel.setBounds(52, 39, 240, 20);
			lastMessageLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12));

			JPanel circleNotify = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					Graphics2D g2 = (Graphics2D) g;
					g.setColor(new Color(102, 130, 113));
					g2.fill(new Ellipse2D.Double(0, 0, 10, 10));
				}
			};

			circleNotify.setBounds(300, 31, 10, 10);
			boolean notifyNewMessage = Core.getListNewMSGFromUsers().contains(targetId);
			circleNotify.setVisible(notifyNewMessage);
			currPanel.add(statusPanel);
			currPanel.add(nameLabel);
			currPanel.add(lastMessageLabel);
			currPanel.add(circleNotify);

			currPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Core.removeNewMSGFromUser(targetId);
					Usuario user = new Usuario(targetId, name);
					Core.buildDialogWindow(user);
					Core.setBottomScrollPosition();
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					currPanel.setBackground(new Color(232, 232, 232));
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					currPanel.setBackground(new Color(224, 224, 224));
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			});

			lastY += 76;

			this.add(currPanel);
		}
	}

	public void setNewChatWindow() {
		ArrayList<Usuario> users = Core.getUsersName();
		this.removeAll();
		this.setPreferredSize(new Dimension(350, users.size() * CHAT_HEIGHT));
		Core.setTargetId(-1);

		int lastY = 0;
		for (int i = 0; i < users.size(); i++) {
			int index = i;

			String name = users.get(index).getNome();
			int targetId = users.get(index).getId();
			boolean status = (boolean) users.get(index).isOnline();

			JPanel currPanel = new JPanel();
			currPanel.setLayout(null); // retirar
			currPanel.setBounds(0, lastY, 350, 49);
			currPanel.setBackground(new Color(224, 224, 224));

			Color statusColor = status ? new Color(0, 255, 0) : new Color(255, 255, 255);
			JPanel statusPanel = new JPanel();
			statusPanel.setBounds(30, 20, 10, 10);
			statusPanel.setBackground(statusColor);

			JLabel nameLabel = new JLabel(name);
			nameLabel.setBounds(50, 15, 240, 20);
			nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));

			currPanel.add(statusPanel);
			currPanel.add(nameLabel);

			currPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Core.buildDialogWindow(users.get(index));
					Core.setTargetId(targetId);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					currPanel.setBackground(new Color(232, 232, 232));
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					currPanel.setBackground(new Color(224, 224, 224));
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			});

			lastY += 50;

			this.add(currPanel);
		}
	}

	public void setDialogWindow(int targetId) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		Core.setTargetId(targetId);
		this.removeAll();
		ArrayList<Message> messages = Core.getMessagesInvolvesTarget(targetId);

		int lastY = 5;
		for (Message msg : messages) {
			boolean isMe = msg.getIdSender() != targetId;
			String labelInfo[] = Core.insertStringBreak(msg.getMessage());
			this.addMessageballoon(isMe, labelInfo[0], Integer.parseInt(labelInfo[1]), lastY);
			lastY += Integer.parseInt(labelInfo[1]) + 5;
		}

		if (lastY < 290) {
			lastY = 290;
		}
		this.createInputArea(lastY, targetId);
		this.setPreferredSize(new Dimension(350, lastY + 78));
	}

	public void addMessageballoon(boolean isMe, String message, int balloonHeight, int lastY) {
		Color panelColor = isMe ? new Color(151, 209, 115) : new Color(210, 214, 207);
		int panelX = isMe ? 130 : 12;

		JPanel currMessagePanel = new JPanel(null);
		currMessagePanel.setBounds(panelX, lastY, 190, balloonHeight);
		currMessagePanel.setBackground(panelColor);

		JLabel currMessageLabel = new JLabel(message);
		currMessageLabel.setBounds(5, 0, 180, balloonHeight);

		currMessagePanel.add(currMessageLabel);

		this.add(currMessagePanel);
	}

	public void createInputArea(int lastY, int targetId) {
		JTextArea messageContainer = new JTextArea();
		messageContainer.setBounds(20, lastY + 5, 250, 60);
		messageContainer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
		messageContainer.setLineWrap(true);

		JLabel sendIconLabel = new JLabel(new ImageIcon(getClass().getResource("/imgs/send.png")));
		sendIconLabel.setBounds(282, lastY + 20, 34, 30);

		sendIconLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (messageContainer.getText().length() > 0) {
					Core.sendMessage(targetId, messageContainer.getText());
					Core.addChatMessage(messageContainer.getText(), "out");
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});

		this.add(messageContainer);
		this.add(sendIconLabel);
	}

	public void removeInputArea() {
		if (String.valueOf(this.getComponent(this.getComponentCount() - 2).getClass()).matches(".*JTextArea")) {
			this.remove(this.getComponent(this.getComponentCount() - 2));
			this.remove(this.getComponent(this.getComponentCount() - 1));
			this.setPreferredSize(new Dimension(350, this.getPreferredSize().height - 78));
		}
	}

	public int getBallonsTotalHeight() {
		int height = 0;
		for (int i = 0; i < this.getComponentCount() - 2; i++) {
			height += this.getComponent(i).getHeight() + 5;
		}

		return height;
	}

	public void setLoginWindow() {
		this.removeAll();

		JLabel usernameLabel = new JLabel("Login: ");

		JPanel loginButton = new JPanel(null);
		JTextField passwordField = new JPasswordField();

		usernameLabel.setBounds(55, 16, 70, 30);
		usernameLabel.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 20));

		JTextField usernameField = new JTextField();
		usernameField.setBounds(138, 20, 150, 25);
		usernameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				removeErrorMsg();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					passwordField.setText("");
					passwordField.requestFocus();
				}
				super.keyPressed(e);
			}
		});

		JLabel passwordLabel = new JLabel("Senha: ");
		passwordLabel.setBounds(47, 51, 78, 30);
		passwordLabel.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 20));
		
		passwordField.setBounds(138, 55, 150, 25);
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				removeErrorMsg();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (usernameField.getText().length() > 0 && passwordField.getText().length() > 0) {
						Usuario user = new Usuario(usernameField.getText(), passwordField.getText());
						Core.login(user);
					} else {
						showErrorMsg("Login ou senha inválidos");
					}
				}
				super.keyPressed(e);
			}
		});

		loginButton.setBounds(56, 95, 232, 30);
		loginButton.setBackground(new Color(109, 142, 122));

		JLabel buttonMessage = new JLabel("Entrar");
		buttonMessage.setBounds(85, 0, 116, 30);
		buttonMessage.setFont(new Font("Arial", Font.ROMAN_BASELINE, 22));
		buttonMessage.setForeground(new Color(242, 242, 242));

		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (usernameField.getText().length() > 0 && passwordField.getText().length() > 0) {
					Usuario user = new Usuario(usernameField.getText(), passwordField.getText());
					Core.login(user);
				} else {
					showErrorMsg("Login ou senha inválidos");
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				loginButton.setBackground(new Color(102, 130, 113));
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				loginButton.setBackground(new Color(109, 142, 122));
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});

		loginButton.add(buttonMessage);

		this.add(usernameLabel);
		this.add(usernameField);
		this.add(passwordLabel);
		this.add(passwordField);
		this.add(loginButton);
	}

	public void showErrorMsg(String message) {
		this.invalidLabel = new JLabel(message);
		this.invalidLabel.setBounds(97, 125, 170, 20);
		this.invalidLabel.setForeground(new Color(178, 3, 3));
		this.invalidLabel.setVisible(true);

		this.add(this.invalidLabel);
		this.revalidate();
		this.repaint();
	}

	public void removeErrorMsg() {
		if (this.invalidLabel != null) {
			this.invalidLabel.setText("");
		}
	}

	class HandlerListener implements AlertaTelaListener, ICommands, IApplication {
		private boolean statusObj[];

		@Override
		public void AlertaTela(RequestResponseData reqRespData) {
			switch (reqRespData.getCommand()) {
			case AUTHENTICATED:
				Core.setUserSession(reqRespData.getUser());
				Core.setAllMessages(reqRespData.getAllMessages());
				Core.setUsersName(reqRespData.getAllContacts());
				Core.updateApplication(WINDOW_MAIN);
				System.out.println(" Seja bem vindo " + Core.getUserSession().getNome() + "!!");
				break;

			case LOGGED:
				showErrorMsg("Usuário já está logado!");
				break;

			case MESSAGE:
				System.out.println("Recebeu a mensagem de: " + reqRespData.getIdSender());
				Core.addMessage(reqRespData.getMessage());

				if (Core.getTargetId() == reqRespData.getMessage().getIdSender())
					Core.addChatMessage(reqRespData.getMsg(), "in");
				else
					Core.addNewMSGFromUser(reqRespData.getMessage().getIdSender());
				break;
			case UNREGISTERED:
				showErrorMsg("Login ou senha inválidos");
				break;

			case STATUS:
				Core.setStatusOfUser(reqRespData.getUser());
				break;
			default:
				break;
			}

			if (Core.getCurrWindowStyle() != WINDOW_LOGIN)
				Core.updateApplication(Core.getCurrWindowStyle());
		}
	}
}
