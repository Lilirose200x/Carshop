package ca.mcgill.ecse.carshop.view;

import ca.mcgill.ecse.carshop.application.CarShopApplication;
import ca.mcgill.ecse.carshop.controller.CarShopController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindowTechnician extends JFrame implements ActionListener, FocusListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 119067600534310717L;
	public static final String BACKGROUND_IMG = "";
    private ServicePage servicePage;
    private AccountWindow accountWindow;
    public static final JDesktopPane desktopPane = new JDesktopPane();
    private final JLabel bg = new JLabel();
    private JMenuBar bar;
    private JMenu accountMenu, garageMenu;
    public static JMenuItem logOutTab, myAccount;
    public static JMenuItem appointmentTab;
    public static JMenuItem garageTab;
    public static JMenuItem serviceTab;
    private UpdateGarageHours update;

    public MainWindowTechnician() {
        setResizable(false);
        setTitle("Welcome to Carshop");
        setBounds(200, 130, 800, 600);
        bar = new JMenuBar();
        bg.setBounds(0, 0, 0, 0);
        bg.setIcon(null);
        desktopPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(final ComponentEvent e) {
            }
        });
//        desktopPane.add(bg, new Integer(Integer.MIN_VALUE));
        add(desktopPane);
        accountWindow = new AccountWindow(this);
        servicePage = new ServicePage();


        desktopPane.add(servicePage);
        desktopPane.add(accountWindow);

        accountMenu = new JMenu("Account");
        logOutTab = new JMenuItem("LogOut");
        myAccount = new JMenuItem("MyAccount");
        accountMenu.add(logOutTab);
        accountMenu.add(myAccount);
        appointmentTab = new JMenuItem("Open Appointment");

        garageMenu = new JMenu("Garage Management");
        garageTab = new JMenuItem("Business Hours");
        garageMenu.add(garageTab);

        serviceTab = new JMenuItem("Open Service");

        setJMenuBar(bar);
        bar.add(accountMenu);
        bar.add(garageMenu);

        logOutTab.addActionListener(this);
        garageTab.addActionListener(this);
        serviceTab.addActionListener(this);
        appointmentTab.addActionListener(this);
        myAccount.addActionListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to logOut?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    setInvisible();
                    CarShopController.LogOut();
                    System.exit(0);
                }
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logOutTab) {
          setInvisible();
          CarShopController.LogOut();
          this.setVisible(false);
          CarShopApplication.loginWindow.setVisible(true);
        } else if (e.getSource() == myAccount) {
          setInvisible();
          accountWindow.setVisible(true);
          this.setSize(
              accountWindow.getWidth() + 12,
              accountWindow.getHeight() + accountMenu.getHeight() + bar.getHeight() + 14);
        } else if (e.getSource() == garageTab) {
          setInvisible();
          update = new UpdateGarageHours();
          update.setVisible(true);
        }
    }


    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    public void setInvisible(){
        accountWindow.setVisible(false);
    }
}
