package Application;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Misc.RequestResponseData;
import Misc.Usuario;

public class HeaderPanel extends JPanel {
	
  public KillClientListener killClientListener;
  
  HeaderPanel (int width, int height) {
    this.setSize(width, height);
    this.setBackground(new Color(102, 130, 113));
    this.setLayout(null);
  }
  
  public void setMainWindow () {
    this.removeAll();
    
    JPanel logoPanel = new JPanel();
    JPanel newChatPanel = new JPanel();
    JPanel logOutPanel = new JPanel();
    
    logoPanel.setLayout(null);
    logoPanel.setBounds(30, 25, 100, 50);
    logoPanel.setBackground(new Color(102, 130, 113));
    
    JLabel logoLabel = new JLabel("SChat");
    logoLabel.setBounds(0, 15, 100, 20);
    logoLabel.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 26));
    logoLabel.setForeground(new Color(232, 232, 232));
    
    
    newChatPanel.setLayout(null);
    newChatPanel.setBounds(252, 37, 25, 25);
    newChatPanel.setBackground(new Color(102, 130, 113));
    
    JLabel newChatLabel = new JLabel(new ImageIcon(getClass().getResource("/imgs/newChat.png")));
    newChatLabel.setBounds(0, 0, 25, 25);
    
    newChatPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Core.updateApplication("newChat");
      }

      public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
      }

      public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    });

    logOutPanel.setLayout(null);
    logOutPanel.setBounds(287, 37, 25, 25);
    logOutPanel.setBackground(new Color(102, 130, 113));
    
    JLabel logOutLabel = new JLabel(new ImageIcon(getClass().getResource("/imgs/logout.png")));
    logOutLabel.setBounds(0, 0, 25, 25);
    
    logOutLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Core.updateApplication("login");
        Core.logout();
      }
      
      public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
      }
        
      public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    });
     
    logoPanel.add(logoLabel);
    newChatPanel.add(newChatLabel);
    logOutPanel.add(logOutLabel);
    
    this.add(logoPanel);
    this.add(newChatPanel);
    this.add(logOutPanel);
  }
  
  public void setNewChatWindow () {
    this.removeAll();
    
    JPanel logoPanel = new JPanel();
    JPanel turnBackPanel = new JPanel();
    JPanel logOutPanel = new JPanel();
    
    logoPanel.setLayout(null);
    logoPanel.setBounds(30, 15, 100, 50);
    logoPanel.setBackground(new Color(102, 130, 113));
    
    JLabel logoLabel = new JLabel("SChat");
    logoLabel.setBounds(0, 15, 100, 40);
    logoLabel.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 26));
    logoLabel.setForeground(new Color(232, 232, 232));
    
    
    turnBackPanel.setLayout(null);
    turnBackPanel.setBounds(252, 37, 25, 25);
    turnBackPanel.setBackground(new Color(102, 130, 113));
    
    JLabel turnBackLabel = new JLabel(new ImageIcon(getClass().getResource("/imgs/turnBack.png")));
    turnBackLabel.setBounds(0, 0, 25, 25);
    
    turnBackPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Core.updateApplication("main");
      }
      
      public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
      }
        
      public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    });

    logOutPanel.setLayout(null);
    logOutPanel.setBounds(287, 37, 25, 25);
    logOutPanel.setBackground(new Color(102, 130, 113));
    
    JLabel logOutLabel = new JLabel(new ImageIcon(getClass().getResource("/imgs/logout.png")));
    logOutLabel.setBounds(0, 0, 25, 25);
    
    logOutLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Core.updateApplication("login");
      }
      
      public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
      }
        
      public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    });

    logoPanel.add(logoLabel);
    turnBackPanel.add(turnBackLabel);
    logOutPanel.add(logOutLabel);
    
    this.add(logoPanel);
    this.add(turnBackPanel);
    this.add(logOutPanel);
  }
  
    public void setDialogWindow (String name) {
    this.removeAll();
    
    JPanel namePanel = new JPanel();
    JPanel turnBackPanel = new JPanel();
    JPanel logOutPanel = new JPanel();
    
    namePanel.setLayout(null);
    namePanel.setBounds(30, 15, 100, 50);
    namePanel.setBackground(new Color(102, 130, 113));
    
    JLabel nameLabel = new JLabel(name);
    nameLabel.setBounds(0, 15, 100, 40);
    nameLabel.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 26));
    nameLabel.setForeground(new Color(232, 232, 232));
    
    
    turnBackPanel.setLayout(null);
    turnBackPanel.setBounds(252, 37, 25, 25);
    turnBackPanel.setBackground(new Color(102, 130, 113));
    
    JLabel turnBackLabel = new JLabel(new ImageIcon(getClass().getResource("/imgs/turnBack.png")));
    turnBackLabel.setBounds(0, 0, 25, 25);
    
    turnBackPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Core.updateApplication("main");
      }
      
      public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
      }
        
      public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    });

    logOutPanel.setLayout(null);
    logOutPanel.setBounds(287, 37, 25, 25);
    logOutPanel.setBackground(new Color(102, 130, 113));
    
    JLabel logOutLabel = new JLabel(new ImageIcon(getClass().getResource("/imgs/logout.png")));
    logOutLabel.setBounds(0, 0, 25, 25);
    
    logOutLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Core.updateApplication("login");
      }
      
      public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
      }
        
      public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    });

    namePanel.add(nameLabel);
    turnBackPanel.add(turnBackLabel);
    logOutPanel.add(logOutLabel);
    
    this.add(namePanel);
    this.add(turnBackPanel);
    this.add(logOutPanel);
  }
    
  public void setLoginWindow () {
    this.removeAll();
    
    JPanel logoPanel = new JPanel(null);
    logoPanel.setBounds(50, 90, 246, 125);
    logoPanel.setBackground(new Color(102, 130, 113));
    
    JLabel logoLabel = new JLabel("SChat");
    logoLabel.setFont(new Font("Times New Roman", Font.ITALIC, 56));
    logoLabel.setHorizontalAlignment(JLabel.CENTER);
    logoLabel.setForeground(Color.white);
    logoLabel.setBounds(0, 0, 246, 125);
    
    JLabel sloganLabel = new JLabel("Com você em todos os lugares.");
    sloganLabel.setFont(new Font("Times New Roman", Font.ITALIC, 18));
    sloganLabel.setHorizontalAlignment(JLabel.CENTER);
    sloganLabel.setForeground(Color.white);
    sloganLabel.setBounds(0, 40, 246, 125);
   
//    TERMINAR TELA DE LOGIN
    
    logoPanel.setVisible(true);
    logoPanel.add(logoLabel);
    logoPanel.add(sloganLabel);
    this.add(logoPanel);
  }

  public interface KillClientListener {
    void kill(Usuario user);
  }

  public void setKillClientListener(KillClientListener killClientListener) {
    this.killClientListener = killClientListener;
  }
}
