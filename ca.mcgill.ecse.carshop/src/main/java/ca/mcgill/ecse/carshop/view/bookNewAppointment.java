package ca.mcgill.ecse.carshop.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;



import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import ca.mcgill.ecse.carshop.controller.CarShopController;

public class bookNewAppointment extends JFrame  {

	private static final long serialVersionUID = -4935740786641266412L;

	private JLabel errorMessage;
	private String error = "";
	
	private JComboBox<String> servicesDropDown;
	private JLabel serviceLabel;
	
	private JComboBox<String> optionalServiceDropDown;
	private JLabel optionalServiceLabel;
	
	private JLabel durationLabel;
	private JLabel durationTime;
	
	private JLabel timeLabelHour;
	private JLabel timeLabelMinute;
	
	private JLabel dateLabel;
	private JComboBox<Integer> yearDropDown;
	private JComboBox<Integer> monthDropDown;
	private JComboBox<Integer> dayDropDown;
	
	private JDatePickerImpl calendar;
	private JLabel selectDate;
	private JButton assignDate;
	private JLabel selectedDate;
	
	private JLabel timeLabel;
	private JLabel mainServiceLabel;
	private JTextField mainHoursField;
	private JTextField mainMinutesField;
	
	private JLabel optional1Label;
	private JTextField optional1HourField;
	private JTextField optional1MinuteField;
	
	private JLabel optional2Label;
	private JTextField optional2HourField;
	private JTextField optional2MinuteField;
	
	private JLabel optional3Label;
	private JTextField optional3HourField;
	private JTextField optional3MinuteField;
	
	private JLabel optional4Label;
	private JTextField optional4HourField;
	private JTextField optional4MinuteField;
	
	private JLabel[] optionalLabels;
	private JTextField[] optionalHourFields;
	private JTextField[] optionalMinuteFields;
	
	private JButton bookButton;
	
	private JButton addButton;
	private JButton removeButton;
	
	private ArrayList<String> addedServices = new ArrayList<>();
	
	//Constructor
	public bookNewAppointment() {
		super();
		this.setTitle("Book New Appointment");
		this.setBounds(230, 150, 850, 280);
		initComponents();
		refreshData();
	}
	
	private void initComponents() {
		
		//elements for error message
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		errorMessage.setBounds(20, 10, 1000, 25);
		
		Container p = getContentPane();
        p.setLayout(null);
        p.add(errorMessage);
		
		//initiallization of GUI elements
		
		
		servicesDropDown = new JComboBox<String>(new String[0]);
		servicesDropDown.setEditable(false);
		for(String s: CarShopController.getNonComboServices()) {
			servicesDropDown.addItem(s);
		}
		for(String s: CarShopController.getComboServicesName()) {
			servicesDropDown.addItem(s);
		}
		servicesDropDown.setBounds(10, 55, 100, 25);
		p.add(servicesDropDown);
		
		serviceLabel = new  JLabel();
		serviceLabel.setText("Services");
		serviceLabel.setBounds(20, 30, 70, 15);
		p.add(serviceLabel);
		
		
		//TODO (not in drawing)
		durationLabel = new JLabel();
		durationLabel.setText("Total duration: ");
		durationTime = new JLabel();
		
		dateLabel = new JLabel();
		dateLabel.setText("Date:");
		dateLabel.setBounds(350, 125, 35, 20);
		p.add(dateLabel);
		
		yearDropDown = new JComboBox<Integer>(new Integer[0]);
		for(int i = 2021; i < 2030; i++) {
			yearDropDown.addItem(i);
		}
		
		monthDropDown = new JComboBox<Integer>(new Integer[0]);
		for(int i = 1; i <13; i++) {
			monthDropDown.addItem(i);
		}
		
		dayDropDown = new JComboBox<Integer>(new Integer[0]);
		for(int i = 1; i < 29; i++) {
			dayDropDown.addItem(i);
		}
		dayDropDown.setEnabled(false);
		
		timeLabel = new JLabel();
		timeLabel.setText("Time:");
		timeLabel.setBounds(668, 32, 50, 15);
		p.add(timeLabel);
		
		timeLabelHour = new JLabel("Hour");
		timeLabelHour.setBounds(649, 52, 40, 15);
		p.add(timeLabelHour);
		
		timeLabelMinute = new JLabel("Minute");
		timeLabelMinute.setBounds(690, 52, 60, 15);
		p.add(timeLabelMinute);
		
		mainServiceLabel = new JLabel("Main");
//		mainServiceLabel.setVisible(false);
		mainServiceLabel.setBounds(510, 80, 150, 15);
		p.add(mainServiceLabel);
		
		optional1Label = new JLabel("Optional 1");
		optional2Label = new JLabel("Optional 2");
		optional3Label = new JLabel("Optional 3");
		optional4Label = new JLabel("Optional 4");
		
		optional1HourField = new JTextField();
		optional1MinuteField = new JTextField();
		
		optional2HourField = new JTextField();
		optional2MinuteField = new JTextField();
		
		optional3HourField = new JTextField();
		optional3MinuteField = new JTextField();
		
		optional4HourField = new JTextField();
		optional4MinuteField = new JTextField();
		
		optional1Label.setVisible(false);
		optional1HourField.setVisible(false);
		optional1MinuteField.setVisible(false);
		optional1Label.setBounds(510, 105, 150, 15);
		optional1MinuteField.setBounds(695, 103, 30, 20);
		optional1HourField.setBounds(650, 103, 30, 20);
		p.add(optional1Label);
		p.add(optional1HourField);
		p.add(optional1MinuteField);
		
		optional2Label.setVisible(false);
		optional2HourField.setVisible(false);
		optional2MinuteField.setVisible(false);
		optional2Label.setBounds(510, 130, 150, 15);
		optional2MinuteField.setBounds(695, 128, 30, 20);
		optional2HourField.setBounds(650, 128, 30, 20);
		p.add(optional2Label);
		p.add(optional2HourField);
		p.add(optional2MinuteField);
		
		optional3Label.setVisible(false);
		optional3HourField.setVisible(false);
		optional3MinuteField.setVisible(false);
		optional3Label.setBounds(510, 155, 150, 15);
		optional3MinuteField.setBounds(695, 153, 30, 20);
		optional3HourField.setBounds(650, 153, 30, 20);
		p.add(optional3Label);
		p.add(optional3HourField);
		p.add(optional3MinuteField);
		
		optional4Label.setVisible(false);
		optional4HourField.setVisible(false);
		optional4MinuteField.setVisible(false);
		optional4Label.setBounds(510, 180, 150, 15);
		optional4MinuteField.setBounds(695, 178, 30, 20);
		optional4HourField.setBounds(650, 178, 30, 20);
		p.add(optional4Label);
		p.add(optional4HourField);
		p.add(optional4MinuteField);
		
		optionalLabels = new JLabel[4];
		optionalLabels[0] = optional1Label;
		optionalLabels[1] = optional2Label;
		optionalLabels[2] = optional3Label;
		optionalLabels[3] = optional4Label;
		
		optionalHourFields = new JTextField[4];
		optionalHourFields[0] = optional1HourField;
		optionalHourFields[1] = optional2HourField;
		optionalHourFields[2] = optional3HourField;
		optionalHourFields[3] = optional4HourField;
		
		optionalMinuteFields = new JTextField[4];
		optionalMinuteFields[0] = optional1MinuteField;
		optionalMinuteFields[1] = optional2MinuteField;
		optionalMinuteFields[2] = optional3MinuteField;
		optionalMinuteFields[3] = optional4MinuteField;
		
		
		
		
		mainHoursField = new JTextField();
		mainHoursField.setVisible(false);
		mainHoursField.setBounds(650, 78, 30, 20);
		p.add(mainHoursField);
		
		mainMinutesField = new JTextField();
		mainMinutesField.setVisible(false);
		mainMinutesField.setBounds(695, 78, 30, 20);
		p.add(mainMinutesField);
		
		bookButton = new JButton();
		bookButton.setBackground(Color.green);
		bookButton.setForeground(Color.BLACK);
		bookButton.setText("Book");
		bookButton.setEnabled(false);
		bookButton.setBounds(340, 160, 120, 60);
		p.add(bookButton);
		
		SqlDateModel model = new SqlDateModel();
		Properties pr = new Properties();
		pr.put("text.today",  "Today");
		pr.put("text.month", "Month");
		pr.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, pr);
		calendar = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		calendar.setBounds(340, 55, 130, 35);
		p.add(calendar);
		
		selectDate = new JLabel("Select Date:");
		selectDate.setBounds(350, 30, 150, 15);
		p.add(selectDate);
		
		assignDate = new JButton("Assign");
		assignDate.setBounds(355, 90, 90, 25);
		p.add(assignDate);
		
		selectedDate = new JLabel();
		selectedDate.setText("");
		selectedDate.setVisible(false);
		selectedDate.setBounds(390, 125, 100, 20);
		p.add(selectedDate);
		
		optionalServiceDropDown = new JComboBox<String>(new String[0]);
		optionalServiceDropDown.setEnabled(false);
		optionalServiceDropDown.setBounds(150, 55, 150, 25);
		p.add(optionalServiceDropDown);
		
		optionalServiceLabel = new JLabel("Optional Service");
		optionalServiceLabel.setBounds(160, 25, 150, 25);
		p.add(optionalServiceLabel);
		
		addButton = new JButton();
		addButton.setText("Add");
		addButton.setEnabled(false);
		addButton.setBounds(150, 95, 60, 20);
		p.add(addButton);
		
		removeButton = new JButton();
		removeButton.setText("Remove");
		removeButton.setEnabled(false);
		removeButton.setBounds(210, 95, 85, 20);
		p.add(removeButton);
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		
		//listeners
		servicesDropDown.addActionListener(
	            new java.awt.event.ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent evt) {
	                  servicesDropDownActionPerformed(evt);
	                }
	              });
		
		addButton.addActionListener(
	            new java.awt.event.ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent evt) {
	                  addButtonActionPerformed(evt);
	                }
	              });
		
		removeButton.addActionListener(
	            new java.awt.event.ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent evt) {
	                  removeButtonActionPerformed(evt);
	                }
	              });
		
		bookButton.addActionListener(
	            new java.awt.event.ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent evt) {
	                  bookButtonActionPerformed(evt);
	                }
	              });
		
		assignDate.addActionListener(
	            new java.awt.event.ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent evt) {
	                  assignDateActionPerformed(evt);
	                }
	              });
		
	}
	
	private void refreshData() {
		//error
		errorMessage.setText(error);
		if(error == null || error.length() == 0) {
			if(servicesDropDown.getSelectedItem() != null) {
				addButton.setEnabled(true);
				removeButton.setEnabled(true);
				mainServiceLabel.setText((String)servicesDropDown.getSelectedItem());
				mainServiceLabel.setVisible(true);
				mainHoursField.setVisible(true);
				mainMinutesField.setVisible(true);
				mainHoursField.setText("");
				mainMinutesField.setText("");
				optionalServiceDropDown.setEnabled(true);
				optionalServiceDropDown.removeAllItems();
				optionalServiceDropDown.addItem("");
				for(String s: CarShopController.getOptionalServicesName((String)servicesDropDown.getSelectedItem())){
					optionalServiceDropDown.addItem(s);
				}
			}
			
			for(int i = 0; i < 4; i++) {
				if(i < addedServices.size()) {
					optionalLabels[i].setText(addedServices.get(i));
					optionalLabels[i].setVisible(true);
					optionalMinuteFields[i].setVisible(true);
					optionalHourFields[i].setVisible(true);

					optionalMinuteFields[i].setText("");
					optionalHourFields[i].setText("");
				}
				else {
					optionalLabels[i].setVisible(false);
					optionalMinuteFields[i].setVisible(false);
					optionalHourFields[i].setVisible(false);
				}
				
			}
			
			if(servicesDropDown.getSelectedItem() != null &&
					selectedDate.getText() != "" &&
					mainHoursField.getText() != "" &&
					mainMinutesField.getText() != "" &&
					(!optional1Label.isEnabled() || (optional1HourField.getText()!= "" && optional1MinuteField.getText()!="")) &&
					(!optional2Label.isEnabled() || (optional2HourField.getText()!= "" && optional2MinuteField.getText()!="")) &&
					(!optional3Label.isEnabled() || (optional3HourField.getText()!= "" && optional3MinuteField.getText()!="")) &&
					(!optional4Label.isEnabled() || (optional4HourField.getText()!= "" && optional4MinuteField.getText()!=""))){
				bookButton.setEnabled(true);
			}
			
			
			
			
		}
		//pack();
	}
	
	private void servicesDropDownActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		addedServices.clear();
		refreshData();
	}
	
	private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		if(optionalServiceDropDown.getSelectedItem() == null) {
			error = "No optional service selected";
		}
		else if(addedServices.size() == 4) {
			error = "More then 4 optional services not currently supported";
		}
		else {
			addedServices.add((String)optionalServiceDropDown.getSelectedItem());
			//create label and textfield for time (can do in refreshData())
		}
		refreshData();
	}
	
	private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		if(optionalServiceDropDown.getSelectedItem() == null) {
			error = "No optional service selected";
		}
		else if(!addedServices.contains(optionalServiceDropDown.getSelectedItem())) {
			error = "The service you are trying to remove has not been selected first";
		}
		else {
			addedServices.remove(optionalServiceDropDown.getSelectedItem());
		}
		refreshData();
	}
	
	private void bookButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		try {
			String username = CarShopController.getCurrentUser().getUsername();
			String date = ((Date)calendar.getModel().getValue()).toString();
			String serviceName = (String)servicesDropDown.getSelectedItem();
			String optServices = "";
			boolean aCellIsEmpty = false;
			for(String s: addedServices) {
				optServices += ","+s;
			}
			String startTimeString = "";
			if(mainHoursField.getText().equals("") || mainMinutesField.getText().equals("") || selectedDate.getText().equals("")) {
				aCellIsEmpty = true;
			}
			if(Integer.parseInt(mainHoursField.getText()) < 0 || Integer.parseInt(mainHoursField.getText()) >= 24 ||
					Integer.parseInt(mainMinutesField.getText()) < 0 || Integer.parseInt(mainMinutesField.getText()) >= 60) {
				error += "Please enter valid hours";
			}
			startTimeString += mainHoursField.getText() + ":" + mainMinutesField.getText();
			for(int i = 0; i < addedServices.size(); i++) {
				if(optionalHourFields[i].getText() == "" || optionalMinuteFields[i].getText() == "") {
					aCellIsEmpty = true;
				}
				else if(Integer.parseInt(optionalHourFields[i].getText()) >= 24 || Integer.parseInt(optionalHourFields[i].getText()) < 0 ||
						Integer.parseInt(optionalHourFields[i].getText()) < 0 || Integer.parseInt(optionalHourFields[i].getText()) >= 59) {
					error += "Please enter valid hours";
				}
				startTimeString += "," + optionalHourFields[i].getText() + ":" + optionalMinuteFields[i].getText();
			}
			
			Date currentDate = new Date(System.currentTimeMillis());
			Time currentTime = new Time(System.currentTimeMillis());
			
			
			if(!aCellIsEmpty && error.equals("")) {
				if(((String)servicesDropDown.getSelectedItem()).toLowerCase().contains("combo")) {
					CarShopController.makeServiceComboAppointment(username, date, serviceName, optServices, startTimeString, currentTime, currentDate);
					addedServices.clear();
					JOptionPane.showMessageDialog(null, "Appointment successfully booked", "Success", JOptionPane.INFORMATION_MESSAGE);
					addedServices.clear();
				}
				else {
					CarShopController.makeServiceAppointment(username, date, serviceName, startTimeString, currentTime, currentDate);
					JOptionPane.showMessageDialog(null, "Appointment successfully booked", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else {
				if(aCellIsEmpty)
					error += "Cells needs to be filled";
				errorMessage.setVisible(true);
			}
			
			
			//initComponents();
				
		}catch(Exception e) {
			error = e.getMessage();
		}
		refreshData();

		
	}
	
	private void assignDateActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		if(!calendar.getModel().getValue().toString().equals("")){
			
		
		selectedDate.setText(((Date)calendar.getModel().getValue()).toString());
		selectedDate.setVisible(true);
		refreshData();
		}
	}
}