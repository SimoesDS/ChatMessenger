package Server;

import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Server.Server;

public class TelaServer extends JFrame implements ActionListener {

	private JTextField txtUserBD;
	private JTextField txtPasswordBD;
	private JTextField txtHostBD;
	private JTextField txtPorta;	

	private JButton btnON;

	private Server server;

	public TelaServer() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		setBounds(600, 300, 370, 300);
		setTitle("SChatServer");

		JLabel lblHostDB = new JLabel("Host DB:");
		lblHostDB.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHostDB.setFont(new Font("Dialog", Font.BOLD, 14));
		lblHostDB.setBounds(0, 25, 90, 15);
		getContentPane().add(lblHostDB);

		txtHostBD = new JTextField("db4free.net:3306");
		txtHostBD.setColumns(10);
		txtHostBD.setBounds(100, 23, 220, 20);
		getContentPane().add(txtHostBD);
		
		JLabel lblUserDB = new JLabel("Login:");
		lblUserDB.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUserDB.setFont(new Font("Dialog", Font.BOLD, 14));
		lblUserDB.setBounds(0, 50, 90, 15);
		getContentPane().add(lblUserDB);

		txtUserBD = new JTextField("schat7");
		txtUserBD.setColumns(10);
		txtUserBD.setBounds(100, 50, 220, 20);
		getContentPane().add(txtUserBD);
		
		JLabel lblPasswordDB = new JLabel("Senha:");
		lblPasswordDB.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPasswordDB.setFont(new Font("Dialog", Font.BOLD, 14));
		lblPasswordDB.setBounds(20, 75, 70, 15);
		getContentPane().add(lblPasswordDB);
		
		txtPasswordBD = new JPasswordField("-2*5.betzNeb$hc~");
		txtPasswordBD.setColumns(10);
		txtPasswordBD.setBounds(100, 75, 220, 20);
		getContentPane().add(txtPasswordBD);

		JLabel lblPorta = new JLabel("Porta:");
		lblPorta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPorta.setFont(new Font("Dialog", Font.BOLD, 14));
		lblPorta.setBounds(0, 130, 90, 15);
		getContentPane().add(lblPorta);

		txtPorta = new JTextField("5056");
		txtPorta.setColumns(10);
		txtPorta.setBounds(100, 130, 220, 20);
		txtPorta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent evt) {
				if(!Character.isDigit(evt.getKeyChar()))
					evt.consume();
				super.keyTyped(evt);
			}
		});
		getContentPane().add(txtPorta);

		btnON = new JButton("Ligar");
		btnON.setBounds(120, 200, 117, 25);
		btnON.addActionListener(this);
		getContentPane().add(btnON);
	}

	public void inicializar() {
		final int porta = Integer.parseInt(txtPorta.getText());
		

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					server = new Server(txtHostBD.toString(), txtUserBD.toString(), txtPasswordBD.toString(), porta);
					setComponentes(false);
					server.aguardaConexoes();
				} catch (IOException e) {
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnON))
			if (server == null)
				inicializar();
			else
				finalizar();
	}

	private void setComponentes(boolean estado) {
		this.txtHostBD.setEditable(estado);
		this.txtUserBD.setEditable(estado);
		this.txtPasswordBD.setEditable(estado);
		this.txtPorta.setEditable(estado);
		this.btnON.setText(estado ? "Ligar" : "Desligar");
	}

	public static void main(String[] args) {
		new TelaServer().setVisible(true);
	}

}
