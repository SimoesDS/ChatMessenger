package Application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Misc.Usuario;

public class HeaderPanel extends JPanel implements IApplication{
	
  public KillClientListener killClientListener;
  
  private JPanel logoPanel;
  private JLabel logoLabel;
  private JPanel logOutPanel;
  private JLabel logOutLabel;
  private JPanel newChatPanel;
  private JLabel newChatLabel;
  private JPanel turnBackPanel;
  private JLabel turnBackLabel;
  
  HeaderPanel (int width, int height) {
    this.setSize(width, height);
    this.setBackground(new Color(102, 130, 113));
    this.setLayout(null);
    
    this.logoLabel = new JLabel("ChatMessenger");
    
    this.logoPanel = new JPanel();
    this.logoPanel.setLayout(null);
    
    this.logOutLabel = new JLabel(new ImageIcon(getClass().getResource("/imgs/logout.png")));
    this.logOutLabel.setBounds(0, 0, 25, 25);
    
    this.logOutPanel = new JPanel();
    this.logOutPanel.setLayout(null);
    this.logOutPanel.addMouseListener(new ListenerMouseAdapter());
    this.logOutPanel.add(logOutLabel);
    
    this.newChatLabel = new JLabel(new ImageIcon(getClass().getResource("/imgs/newChat.png")));
    this.newChatLabel.setBounds(0, 0, 25, 25);
    
    this.newChatPanel = new JPanel();
    this.newChatPanel.add(newChatLabel);
    this.newChatPanel.addMouseListener(new ListenerMouseAdapter());
    
    this.turnBackLabel = new JLabel(new ImageIcon(getClass().getResource("/imgs/turnBack.png")));
    this.turnBackLabel.setBounds(0, 0, 25, 25);
    
    this.turnBackPanel = new JPanel();
    this.turnBackPanel.addMouseListener(new ListenerMouseAdapter());
    this.turnBackPanel.add(turnBackLabel);
  }
  
  public void setMainWindow () {
    this.removeAll();
    logoPanel.removeAll();
    
    logoLabel.setBounds(0, 0, 220, 40);
    logoLabel.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 26));
    logoLabel.setForeground(new Color(232, 232, 232));
    
    logoPanel.setBounds(15, 30, 220, 40);
    logoPanel.setBackground(new Color(102, 130, 113));
    
    newChatPanel.setLayout(null);
    newChatPanel.setBounds(252, 37, 25, 25);
    newChatPanel.setBackground(new Color(102, 130, 113));

    logOutPanel.setLayout(null);
    logOutPanel.setBounds(287, 37, 25, 25);
    logOutPanel.setBackground(new Color(102, 130, 113));     
     
    logoPanel.add(logoLabel);
    this.add(logoPanel);
    this.add(newChatPanel);
    this.add(logOutPanel);

  }
  
  public void setNewChatWindow () {
  	this.removeAll();
    logoPanel.removeAll();
    
    logoLabel.setBounds(0, 0, 220, 40);
    logoLabel.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 26));
    logoLabel.setForeground(new Color(232, 232, 232));
    
    logoPanel.setBounds(15, 30, 220, 40);
    logoPanel.setBackground(new Color(102, 130, 113));
    
    turnBackPanel.setLayout(null);
    turnBackPanel.setBounds(252, 37, 25, 25);
    turnBackPanel.setBackground(new Color(102, 130, 113));

    logOutPanel.setLayout(null);
    logOutPanel.setBounds(287, 37, 25, 25);
    logOutPanel.setBackground(new Color(102, 130, 113));
    
    logoPanel.add(logoLabel);
    this.add(logoPanel);
    this.add(turnBackPanel);
    this.add(logOutPanel);
  }
  
  public void setDialogWindow (String name) {
    this.removeAll();

    JPanel namePanel = new JPanel();
    namePanel.setLayout(null);
    namePanel.setBounds(30, 35, 215, 28);
    namePanel.setBackground(new Color(102, 130, 113));

    JLabel nameLabel = new JLabel(name);
    nameLabel.setBounds(0, 0, 215, 28);
    nameLabel.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 26));
    nameLabel.setForeground(new Color(232, 232, 232));
    

    turnBackPanel.setLayout(null);
    turnBackPanel.setBounds(252, 37, 25, 25);
    turnBackPanel.setBackground(new Color(102, 130, 113));

    logOutPanel.setLayout(null);
    logOutPanel.setBounds(287, 37, 25, 25);
    logOutPanel.setBackground(new Color(102, 130, 113));

    namePanel.add(nameLabel);
    
    this.add(namePanel);
    this.add(turnBackPanel);
    this.add(logOutPanel);
  }
    
  public void setLoginWindow () {
    this.removeAll();
    logoPanel.removeAll();
    
    logoPanel.setBounds(0, 90, this.getWidth(), 125);
    logoPanel.setBackground(new Color(102, 130, 113));
    
    logoLabel.setFont(new Font("Times New Roman", Font.ITALIC, 40));
    logoLabel.setHorizontalAlignment(JLabel.CENTER);
    logoLabel.setForeground(Color.white);
    logoLabel.setBounds(0, 0, this.getWidth(), 125);
    
    JLabel sloganLabel = new JLabel("Com você em todos os lugares.");
    sloganLabel.setFont(new Font("Times New Roman", Font.ITALIC, 18));
    sloganLabel.setHorizontalAlignment(JLabel.CENTER);
    sloganLabel.setForeground(Color.white);
    sloganLabel.setBounds(0, 90, this.getWidth(), 20);
    
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
  
  private class ListenerMouseAdapter extends MouseAdapter{
  	@Override
  	public void mouseClicked(MouseEvent e) {
  		if(e.getSource() == logOutPanel) {
  			Core.updateApplication(WINDOW_LOGIN);
  			Core.logout();
  		} else if(e.getSource() == newChatPanel) 
  			Core.updateApplication(WINDOW_NEWCHAT);
  		else if(e.getSource() == turnBackPanel) 
  			Core.updateApplication(WINDOW_MAIN);
  	}
  	
  	@Override
  	public void mouseEntered(MouseEvent e) {
  		setCursor(new Cursor(Cursor.HAND_CURSOR));
  	}
  	
  	@Override
  	public void mouseExited(MouseEvent e) {
  		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
  	}
  }
}
