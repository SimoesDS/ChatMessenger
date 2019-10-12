package Application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Client.ClientListener;
import Client.ClientListener.AlertaTelaListener;
import Client.ClientReply;
import Communication.ICommands;
import Misc.DbUtils;
import Misc.Message;
import Misc.RequestResponseData;
import Misc.Usuario;
import Misc.Utils;

public class BodyPanel extends JPanel {

	private int conversations;
	private final int CHAT_HEIGHT = 76;
	HandlerListener handlerListener = new HandlerListener();

	BodyPanel(int width, int conversations) {
		this.conversations = conversations;
		this.setLayout(null);
	}

	public void setMainWindow(String names[], String lastMessages[], boolean status[]) {
		this.removeAll();
		this.setPreferredSize(new Dimension(350, this.conversations * CHAT_HEIGHT));

		int lastY = 0;
		for (int i = 0; i < this.conversations; i++) {
			int index = i;

			JPanel currPanel = new JPanel();
			currPanel.setLayout(null); // retirar
			currPanel.setBounds(0, lastY, 350, 75);
			currPanel.setBackground(new Color(224, 224, 224));

			Color statusColor = status[i] ? new Color(0, 255, 0) : new Color(255, 255, 255);
			JPanel statusPanel = new JPanel();
			statusPanel.setBounds(30, 25, 10, 10);
			statusPanel.setBackground(statusColor);

			JLabel nameLabel = new JLabel(names[i]);
			nameLabel.setBounds(50, 20, 100, 20);
			nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));

			JLabel lastMessageLabel = new JLabel(lastMessages[i]);
			lastMessageLabel.setBounds(52, 39, 300, 20);
			lastMessageLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12));

			currPanel.add(statusPanel);
			currPanel.add(nameLabel);
			currPanel.add(lastMessageLabel);

			currPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int targetId = Core.getIdUserByName(names[index]);

					Usuario user = new Usuario(); // TODO: USER JA DEVE ESTAR ERRADO
					user.setNome(names[index]); // TODO: TA ERRADO = AQUI PRECISA DAR UM JEITO DE PEGAR O USUARIO DESTINO
					user.setId(targetId);
//          System.out.println("USER AQUI DENTRO DO DIALOG: " + targetId);
					Core.buildDialogWindow(user); // TODO: Objeto com as conversas ja estara no cliente
					Core.setBottomScrollPosition();
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					currPanel.setBackground(new Color(232, 232, 232));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					currPanel.setBackground(new Color(224, 224, 224));
				}
			});

			lastY += 76;

			this.add(currPanel);
		}
	}

	public void setNewChatWindow(String names[], boolean status[]) {
		this.removeAll();
		this.setPreferredSize(new Dimension(350, names.length * CHAT_HEIGHT));

		int lastY = 0;
		for (int i = 0; i < names.length; i++) {
			int index = i;

			if (names[i] != null) {
				JPanel currPanel = new JPanel();
				currPanel.setLayout(null); // retirar
				currPanel.setBounds(0, lastY, 350, 75);
				currPanel.setBackground(new Color(224, 224, 224));

				Color statusColor = status[i] ? new Color(0, 255, 0) : new Color(255, 255, 255);
				JPanel statusPanel = new JPanel();
				statusPanel.setBounds(30, 25, 10, 10);
				statusPanel.setBackground(statusColor);

				JLabel nameLabel = new JLabel(names[i]);
				nameLabel.setBounds(50, 20, 100, 20);
				nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));

				currPanel.add(statusPanel);
				currPanel.add(nameLabel);

				currPanel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						Usuario user = new Usuario();
						user.setNome(names[index]); // TA ERRADO = AQUI PRECISA DAR UM JEITO DE PEGAR O USUARIO DESTINO
						user.setId(Core.getIdUserByName(names[index]));
						Core.buildDialogWindow(user); // TODO: Conversas já estarão no cliente
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						currPanel.setBackground(new Color(232, 232, 232));
					}

					@Override
					public void mouseExited(MouseEvent e) {
						currPanel.setBackground(new Color(224, 224, 224));
					}
				});

				lastY += 76;

				this.add(currPanel);
			}
		}
	}

	public void setDialogWindow(int targetId) {
		Core.setTargetId(targetId);
		this.removeAll();
		//String messagesData[][] = Misc.Utils.getEntireDialog(targetId);
		ArrayList<Message> messages = Core.getMessages();

		int lastY = 5;
		for (Message msg : messages) {
			boolean isMe = msg.getIdSender() != targetId;
			String labelInfo[] = Utils.insertStringBreak(msg.getMessage());
			this.addMessageballoon(isMe, labelInfo[0], Integer.parseInt(labelInfo[1]), lastY);
			lastY += Integer.parseInt(labelInfo[1]) + 5;
		}
		
		/*int lastY = 5;
		for (int i = 0; i < messagesData.length; i++) {
			boolean isMe = Integer.parseInt(messagesData[i][0]) != targetId;
			String labelInfo[] = Utils.insertStringBreak(messagesData[i][1]);
			this.addMessageballoon(isMe, labelInfo[0], Integer.parseInt(labelInfo[1]), lastY);
			lastY += Integer.parseInt(labelInfo[1]) + 5;
		}*/

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
		});

		this.add(messageContainer);
		this.add(sendIconLabel);
	}

	public void removeInputArea() {
//    if (this.getComponentCount() - 2 > 0) {
		if (String.valueOf(this.getComponent(this.getComponentCount() - 2).getClass()).matches(".*JTextArea")) {
			this.remove(this.getComponent(this.getComponentCount() - 2));
			this.remove(this.getComponent(this.getComponentCount() - 1));
			this.setPreferredSize(new Dimension(350, this.getPreferredSize().height - 78));
		}
//    }
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
		usernameLabel.setBounds(55, 16, 70, 30);
		usernameLabel.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 20));

		JTextField usernameField = new JTextField();
		usernameField.setBounds(138, 20, 150, 25);

		JLabel passwordLabel = new JLabel("Senha: ");
		passwordLabel.setBounds(55, 51, 70, 30);
		passwordLabel.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 20));

		JTextField passwordField = new JPasswordField();
		passwordField.setBounds(138, 55, 150, 25);

		JPanel loginButton = new JPanel(null);
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
			}

			@Override
			public void mouseExited(MouseEvent e) {
				loginButton.setBackground(new Color(109, 142, 122));
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
		JLabel invalidEntries = new JLabel(message);
		invalidEntries.setBounds(97, 125, 150, 20);
		//85, 0, 116, 30
		invalidEntries.setForeground(new Color(178, 3, 3));
		invalidEntries.setVisible(true);

		this.add(invalidEntries);
		this.revalidate();
		this.repaint();
	}

	public void setConversations(int value) {
		this.conversations = value;
	}

	public int getConversations() {
		return this.conversations;
	}

	class HandlerListener implements AlertaTelaListener, ICommands {
		@Override
		public void AlertaTela(RequestResponseData reqRespData) {
			switch (reqRespData.getCommand()) {
			case AUTHENTICATED:
				Core.setUserSession(reqRespData.getUser());
				Core.setMessages(reqRespData.getAllMessages());
				Core.setUsersName(reqRespData.getAllContacts());
				Utils.setUSerSession();
				Core.updateApplication("main");
				break;

			case LOGGED:
				showErrorMsg("Usuário já está logado!");
				break;

			case MESSAGE:
				System.out.println("BodyPanel recebeu a mensagem de: " + reqRespData.getIdSender());
				Core.addChatMessage(reqRespData.getMessage(), "in");
				break;
			case UNREGISTERED:
				showErrorMsg("Login ou senha inválidos");
				break;

			default:
				break;
			}

		}

	}
}
