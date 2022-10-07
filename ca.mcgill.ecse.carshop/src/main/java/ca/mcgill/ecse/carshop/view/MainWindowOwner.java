
package ca.mcgill.ecse.carshop.view;

import ca.mcgill.ecse.carshop.application.CarShopApplication;
import ca.mcgill.ecse.carshop.controller.CarShopController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindowOwner extends JFrame implements ActionListener, FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7711807329634784084L;
	public static final String BACKGROUND_IMG = "";
	private ServicePage servicePage;
	private BusinessPage businessPage;
	private AccountWindow accountWindow;
	public static final JDesktopPane desktopPane = new JDesktopPane();
	private final JLabel bg = new JLabel();
	private JMenuBar bar;
	private JMenu accountMenu, appointmentMenu, serviceMenu, businessMenu;
	public static JMenuItem logOutTab, myAccount;
	public static JMenuItem appointmentTab, appointmentTab2, businessTab;
	public static JMenuItem serviceTab;
	private AppointmentStartPage appManagement;
	private ViewAppointmentsPage viewApps;

	public MainWindowOwner() {
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
		businessPage = new BusinessPage();

		desktopPane.add(businessPage);
		desktopPane.add(servicePage);
		desktopPane.add(accountWindow);

		accountMenu = new JMenu("Account");
		logOutTab = new JMenuItem("LogOut");
		myAccount = new JMenuItem("MyAccount");
		accountMenu.add(logOutTab);
		accountMenu.add(myAccount);
		appointmentMenu = new JMenu("Appointment Management");
		appointmentTab = new JMenuItem("Today's Appointments");
		appointmentTab2 = new JMenuItem("Appointments by Day");
		appointmentMenu.add(appointmentTab);
		appointmentMenu.add(appointmentTab2);

		serviceMenu = new JMenu("Service Management");
		serviceTab = new JMenuItem("Open Service");
		serviceMenu.add(serviceTab);

		businessMenu = new JMenu("Business Management");
		businessTab = new JMenuItem("Open Business");
		businessMenu.add(businessTab);

		setJMenuBar(bar);
		bar.add(accountMenu);
		bar.add(businessMenu);
		bar.add(appointmentMenu);
		bar.add(serviceMenu);

		logOutTab.addActionListener(this);
		serviceTab.addActionListener(this);
		appointmentTab.addActionListener(this);
		appointmentTab2.addActionListener(this);
		myAccount.addActionListener(this);
		businessTab.addActionListener(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to logOut?", "Close Window?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
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
			this.setSize(accountWindow.getWidth() + 12,
					accountWindow.getHeight() + serviceMenu.getHeight() + bar.getHeight() + 14);

		} else if (e.getSource() == appointmentTab) {
			setInvisible();
			appManagement = new AppointmentStartPage();
			appManagement.setVisible(true);
		} else if (e.getSource() == appointmentTab2) {
			setInvisible();
			viewApps = new ViewAppointmentsPage();
			viewApps.setVisible(true);
		} else if (e.getSource() == serviceTab) {
			setInvisible();
			servicePage.setVisible(true);
			this.setSize(servicePage.getWidth() + 12,
					servicePage.getHeight() + serviceMenu.getHeight() + bar.getHeight() + 14);
		} else if (e.getSource() == businessTab) {
			setInvisible();
			businessPage.setVisible(true);
			this.setSize(businessPage.getWidth() + 12,
					businessPage.getHeight() + businessMenu.getHeight() + bar.getHeight() + 14);
		}
	}

	@Override
	public void focusGained(FocusEvent e) {

	}

	@Override
	public void focusLost(FocusEvent e) {

	}

	public void setInvisible() {
		servicePage.setVisible(false);
		accountWindow.setVisible(false);
		businessPage.setVisible(false);
	}
}
