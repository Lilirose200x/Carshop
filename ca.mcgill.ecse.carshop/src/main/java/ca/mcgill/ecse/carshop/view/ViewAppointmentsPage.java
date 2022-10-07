package ca.mcgill.ecse.carshop.view;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ca.mcgill.ecse.carshop.controller.CarShopController;


//@Author cpare
public class ViewAppointmentsPage extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5544634826530431986L;

	// UI elements
	private JLabel errorMessage;
	
	//Appointment elements
	private JComboBox<String> monthBox;
	private JComboBox<Integer> dayBox;
	private JButton getAppsButton;
	private JLabel title;
	private JLabel monthLabel;
	private JLabel dayLabel;
	private final String[] months = {"January", "February", "March","April","May","June","July","August","September","October","November","December"};
		
	public ViewAppointmentsPage() {
		super();
		this.setTitle("Carshop");
        this.setBounds(450, 350, 250, 180);
		initComponents();
	}
	
	public void initComponents() {
		Container p = getContentPane();
        p.setLayout(null);
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		
		title = new JLabel("", JLabel.CENTER);
		title.setText("View Appointments");
		title.setBounds(60, 5, 125, 15);
		p.add(title);
		
		//Months
		monthLabel = new JLabel("", JLabel.CENTER);
		monthLabel.setText("Month");
		monthLabel.setBounds(45, 25, 50, 15);
		p.add(monthLabel);
		
		monthBox = new JComboBox<String>(months);
		monthBox.setBounds(30, 50, 80, 20);
		p.add(monthBox);
		
		//Days
		dayLabel = new JLabel("", JLabel.CENTER);
		dayLabel.setText("Day");
		dayLabel.setBounds(160, 25, 30, 15);
		p.add(dayLabel);
		
		Integer[] days = new Integer[31];
		for (int i=0; i<31; i++) {
			days[i] = i+1;
		}
		dayBox = new JComboBox<Integer>(days);
		dayBox.setBounds(160, 50, 45, 20);
		p.add(dayBox);
		
		getAppsButton = new JButton();
		getAppsButton.setText("View");
		getAppsButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getAppsButtonActionPerformed(evt);
			}
		});
		getAppsButton.setBounds(90, 100, 70, 25);
		p.add(getAppsButton);
		
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	private void getAppsButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (((String) monthBox.getSelectedItem()).trim().equals("")) {
			JOptionPane.showMessageDialog(null, "A month needs to be selected", "Error", JOptionPane.ERROR_MESSAGE);
		}
		if (((Integer) dayBox.getSelectedItem())==null) {
			JOptionPane.showMessageDialog(null, "A day needs to be chosen", "Error", JOptionPane.ERROR_MESSAGE);
		}
		int day = (int) dayBox.getSelectedItem();
		int[] daysByMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 31, 31};
		int month = 0;
		for(int i=0; i<12;i++) {
			if(monthBox.getSelectedItem().equals(months[i])){
				month = i;
				break;
			}
		}
		if (day > daysByMonth[month]) {
			JOptionPane.showMessageDialog(null, "The month of "+months[month]+" has only "+daysByMonth[month]+" days.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		//prepare string for controller
		month++;
		String sDay = String.valueOf(day);
		String sMonth = String.valueOf(month);
		if (day<10) {
			sDay = "0"+sDay;
		}
		if (month<10) {
			sMonth = "0"+sMonth;
		}
		String input = "2021-"+sMonth+"-"+sDay;
		String[] appointments = CarShopController.getAppointmentsByDay(input);
		JFrame apps = new JFrame();
		apps.setTitle("Appointments on "+input);
		apps.setBounds(450, 350, 330, (appointments.length*17+70));
		int height = 17;
		for(String s:appointments) {
			JLabel temp = new JLabel(s);
			temp.setBounds(15, height, 125, 15);
			apps.add(temp);
			height+=17;
		}
		if (appointments.length==0) {
			JLabel temp = new JLabel("No Appointments on that day");
			temp.setBounds(20, 10, 125, 15);
			apps.add(temp);
		}
		apps.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		apps.setVisible(true);
	}
}
