package Server;

import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Server.Server;

public class TelaServer extends JFrame implements ActionListener {

  private JTextField txtHost;
  private JTextField txtPorta;

  private JButton btnON;

  private Server server;

  public TelaServer() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);//Fecha somente a tela, se fosse EXIT_ON_CLOSE seria o mesmo que usar System.exit().
    getContentPane().setLayout(null);
    setLocationRelativeTo(null);
    setResizable(false);
    setBounds(600, 300, 250, 300);
    setTitle("Servevidor Chat");

    btnON = new JButton("Ligar");
    btnON.setBounds(70, 95, 117, 25);
    btnON.addActionListener(this);
    getContentPane().add(btnON);

    JLabel lblHost = new JLabel("Host:");
    lblHost.setHorizontalAlignment(SwingConstants.RIGHT);
    lblHost.setFont(new Font("Dialog", Font.BOLD, 14));
    lblHost.setBounds(12, 25, 70, 15);
    getContentPane().add(lblHost);

    txtHost = new JTextField("127.0.0.1");
    txtHost.setColumns(10);
    txtHost.setBounds(85, 23, 140, 20);
    getContentPane().add(txtHost);

    JLabel lblPorta = new JLabel("Porta:");
    lblPorta.setHorizontalAlignment(SwingConstants.RIGHT);
    lblPorta.setFont(new Font("Dialog", Font.BOLD, 14));
    lblPorta.setBounds(12, 52, 70, 15);
    getContentPane().add(lblPorta);

    txtPorta = new JTextField("5056");
    txtPorta.setColumns(10);
    txtPorta.setBounds(85, 51, 140, 20);
    getContentPane().add(txtPorta);
  }

  public void inicializar() {
    final int porta = Integer.parseInt(txtPorta.getText());
    final int maxConexoes = 30;
    String host = txtHost.getText();

    new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          server = new Server(porta, maxConexoes);
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
    if (e.getSource().equals(btnON)) {
      if (server == null) {
        inicializar();
      } else {
        finalizar();
      }
    }

  }

  private void setComponentes(boolean estado) {
    this.txtHost.setEditable(estado);
    this.txtPorta.setEditable(estado);
    this.btnON.setText(estado ? "Ligar" : "Desligar");
  }

  public static void main(String[] args) {
    new TelaServer().setVisible(true);
  }

}
