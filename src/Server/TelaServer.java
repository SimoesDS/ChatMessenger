package Server;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Application.Core;
import Misc.Usuario;

public class TelaServer extends JFrame {

	private JTextField txtUserBD;
	private JTextField txtPasswordBD;
	private JTextField txtHostBD;
	private JTextField txtPorta;
	private JLabel txtMessageNotify;
	private JPanel btnON_OFF;
	private JLabel buttonMessage = new JLabel();

	private Server server;

	public TelaServer() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		setBounds(600, 300, 370, 400);
		setTitle("ServerMessenger");

		JPanel headerPanel = new JPanel();
		headerPanel.setSize(this.getWidth(), 100);
		headerPanel.setBackground(new Color(102, 130, 113));
		headerPanel.setLayout(null);
		getContentPane().add(headerPanel);

		JLabel logoLabel = new JLabel("ServerMessenger");
		logoLabel.setFont(new Font("Times New Roman", Font.ITALIC, 30));
		logoLabel.setHorizontalAlignment(JLabel.CENTER);
		logoLabel.setForeground(Color.white);
		logoLabel.setBounds(35, 35, 300, 30);
		headerPanel.add(logoLabel);

		JLabel lblHostDB = new JLabel("Host DB:");
		lblHostDB.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHostDB.setFont(new Font("Dialog", Font.BOLD, 14));
		lblHostDB.setBounds(0, 125, 90, 15);
		getContentPane().add(lblHostDB);

		txtHostBD = new JTextField("simoes-pc:3306/aps7");
		txtHostBD.setColumns(10);
		txtHostBD.setBounds(100, 125, 220, 20);
		getContentPane().add(txtHostBD);

		JLabel lblUserDB = new JLabel("Login:");
		lblUserDB.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUserDB.setFont(new Font("Dialog", Font.BOLD, 14));
		lblUserDB.setBounds(0, 150, 90, 15);
		getContentPane().add(lblUserDB);

		txtUserBD = new JTextField("aps7");
		txtUserBD.setColumns(10);
		txtUserBD.setBounds(100, 150, 220, 20);
		getContentPane().add(txtUserBD);

		JLabel lblPasswordDB = new JLabel("Senha:");
		lblPasswordDB.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPasswordDB.setFont(new Font("Dialog", Font.BOLD, 14));
		lblPasswordDB.setBounds(20, 175, 70, 15);
		getContentPane().add(lblPasswordDB);

		txtPasswordBD = new JPasswordField("Qa$3!adUYad");
		txtPasswordBD.setColumns(10);
		txtPasswordBD.setBounds(100, 175, 220, 20);
		getContentPane().add(txtPasswordBD);

		JLabel lblPorta = new JLabel("Porta:");
		lblPorta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPorta.setFont(new Font("Dialog", Font.BOLD, 14));
		lblPorta.setBounds(0, 200, 90, 15);
		getContentPane().add(lblPorta);

		txtPorta = new JTextField("5056");
		txtPorta.setColumns(10);
		txtPorta.setBounds(100, 200, 220, 20);
		txtPorta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent evt) {
				if (!Character.isDigit(evt.getKeyChar()))
					evt.consume();
				super.keyTyped(evt);
			}
		});
		getContentPane().add(txtPorta);

		buttonMessage = new JLabel("Ligar");
		buttonMessage.setFont(new Font("Arial", Font.ROMAN_BASELINE, 22));
		buttonMessage.setForeground(new Color(242, 242, 242));

		btnON_OFF = new JPanel();
		btnON_OFF.setBounds(70, 280, 232, 40);
		btnON_OFF.setBackground(new Color(109, 142, 122));
		btnON_OFF.addMouseListener(new ButtonMouseAdapter());
		btnON_OFF.add(buttonMessage);
		getContentPane().add(btnON_OFF);

		txtMessageNotify = new JLabel();
		getContentPane().add(txtMessageNotify);
	}

	private void inicializar() {
		final int porta = Integer.parseInt(txtPorta.getText());

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					server = new Server(txtHostBD.getText(), txtUserBD.getText(), txtPasswordBD.getText(), porta);
					setComponentes(false);
					showMessageStatusConn(1);
					if (server.testConnDB()) {
						showMessageStatusConn(2);
						server.aguardaConexoes();
					} else {
						server = null;
						showMessageStatusConn(3);
						setComponentes(true);
					}
				} catch (IOException e) {
					e.getStackTrace();
				} finally {
					finalizar();
				}

			}
		}).start();
	}

	private void finalizar() {
		if (server != null) {
			try {
				server.close();
				server = null;
				setComponentes(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setComponentes(boolean estado) {
		this.txtHostBD.setEditable(estado);
		this.txtUserBD.setEditable(estado);
		this.txtPasswordBD.setEditable(estado);
		this.txtPorta.setEditable(estado);
		this.buttonMessage.setText(estado ? "Ligar" : "Desligar");
		this.txtMessageNotify.setVisible(!estado);
	}

	private void showMessageStatusConn(int status) {

		switch (status) {
		case 1:
			txtMessageNotify.setForeground(new Color(0, 0, 0));
			txtMessageNotify.setText("Fazendo conexão com o banco de dados...");
			txtMessageNotify.setBounds(40, 320, 300, 30);
			break;
		case 2:
			txtMessageNotify.setForeground(new Color(0, 128, 0));
			txtMessageNotify.setText("Conexão estabelecida com sucesso!!");
			txtMessageNotify.setBounds(65, 320, 300, 30);
			break;
		case 3:
			txtMessageNotify.setForeground(new Color(128, 0, 0));
			txtMessageNotify.setText("Falha na conexão com o banco!!");
			txtMessageNotify.setBounds(80, 320, 300, 30);

			break;

		default:
			break;
		}
	}

	public static void main(String[] args) {
		new TelaServer().setVisible(true);
	}

	private class ButtonMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource().equals(btnON_OFF))
				if (server == null)
					inicializar();
				else
					finalizar();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (e.getSource().equals(btnON_OFF)) {
				btnON_OFF.setBackground(new Color(102, 130, 113));
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getSource().equals(btnON_OFF)) {
				btnON_OFF.setBackground(new Color(109, 142, 122));
			}
		}
	}

}
