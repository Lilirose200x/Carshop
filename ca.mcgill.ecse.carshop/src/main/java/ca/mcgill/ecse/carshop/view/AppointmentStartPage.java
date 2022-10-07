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
public class AppointmentStartPage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2529719454563426659L;


	// UI elements
	private JLabel errorMessage;
	

	// Appointment elements
	private JComboBox<String> appointmentsListBox;
	private JLabel chooseApp;
	private JButton startButton;
	private JButton endButton;
	private JButton noShowButton;

	public AppointmentStartPage() {
		super();
		this.setTitle("Appointment Management");
        this.setBounds(400, 300, 400, 210);
		initComponents();
	}

	private void initComponents() {
        Container p = getContentPane();
        p.setLayout(null);
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		

		// appointment list
		appointmentsListBox = new JComboBox<String>(CarShopController.getAppointmentsByDay(""));
		appointmentsListBox.setBounds(80, 60, 250, 20);
		p.add(appointmentsListBox);
		
		//App label
		chooseApp = new JLabel("", JLabel.CENTER);
		chooseApp.setText("Choose Appointment");
		chooseApp.setBounds(120, 25, 150, 15);
		p.add(chooseApp);
		
		//Start Button
		startButton = new JButton();
		startButton.setText("Start");
		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addStartButtonActionPerformed(evt);
			}
		});
		startButton.setBounds(30, 120, 80, 20);
		p.add(startButton);
		
		//End Button
		endButton = new JButton();
		endButton.setText("End");
		endButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addEndButtonActionPerformed(evt);
			}
		});
		endButton.setBounds(133, 120, 60, 20);
		p.add(endButton);
		
		//No-ShowButton
		noShowButton = new JButton();
		noShowButton.setText("Register No-Show");
		noShowButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addNoShowButtonActionPerformed(evt);
			}
		});
		noShowButton.setBounds(220, 120, 140, 20);
		p.add(noShowButton);
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	private void addStartButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (appointmentsListBox.getSelectedItem()==null) {
			JOptionPane.showMessageDialog(null, "Appointment needed to be chosen", "Error", JOptionPane.ERROR_MESSAGE);
		}else {
		String[] temp = ((String) appointmentsListBox.getSelectedItem()).split(" - ");
		String name = temp[1];
		String date = CarShopController.getDateString();
		String time = temp[0];
		try {
			if(!CarShopController.startAppointment(name, date, time)) {
				JOptionPane.showMessageDialog(null, "Appointment already started", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		appointmentsListBox.removeAllItems();
		for (String st : CarShopController.getAppointmentsByDay("")) {
			appointmentsListBox.addItem(st);
		}}
	}
	private void addEndButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (appointmentsListBox.getSelectedItem()==null) {
			JOptionPane.showMessageDialog(null, "Appointment needed to be chosen", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
		String[] temp = ((String) appointmentsListBox.getSelectedItem()).split(" - ");
		String name = temp[1];
		String date = CarShopController.getDateString();
		String time = temp[0];
		try {
			if (!CarShopController.appointmentDone(name, date, time)) {
				JOptionPane.showMessageDialog(null, "Cannot end an appointment that has not started", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		appointmentsListBox.removeAllItems();
		for (String st : CarShopController.getAppointmentsByDay("")) {
			appointmentsListBox.addItem(st);
		}}
	}
	 
	private void addNoShowButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (appointmentsListBox.getSelectedItem()==null) {
			JOptionPane.showMessageDialog(null, "Appointment needed to be chosen", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
		String[] temp = ((String) appointmentsListBox.getSelectedItem()).split(" - ");
		String name = temp[1];
		String date = CarShopController.getDateString();
		String time = temp[0];
		try {
			if(!CarShopController.noShow(name, date, time)) {
				JOptionPane.showMessageDialog(null, "Can not register a no-show for an appointment already started", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		appointmentsListBox.removeAllItems();
		for (String st : CarShopController.getAppointmentsByDay("")) {
			appointmentsListBox.addItem(st);
		}}
	}
}
