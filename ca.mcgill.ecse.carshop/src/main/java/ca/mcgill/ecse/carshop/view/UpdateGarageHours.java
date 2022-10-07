package ca.mcgill.ecse.carshop.view;

import java.awt.Color;
import java.awt.Container;

import javax.swing.*;

import ca.mcgill.ecse.carshop.controller.CarShopController;

//@Author cpare
public class UpdateGarageHours extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -239244286263834838L;

	private JLabel errorMessage;

	private JComboBox<String> days;
	private JLabel selectDay;
	private JLabel timeLabel;
	private JLabel startTimeLabel;
	private JLabel endTimeLabel;
	private JTextField startTime;
	private JTextField endTime;
	private JButton view;
	private JButton update;

	// Constructor
	public UpdateGarageHours() {
		super();
		this.setTitle("Set Hours");
		this.setBounds(300, 200, 250, 150);
		initComponents();
	}

	public void initComponents() {
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);

		Container p = getContentPane();
		p.setLayout(null);

		selectDay = new JLabel("Select Day");
		selectDay.setBounds(20, 10, 100, 15);
		p.add(selectDay);

		days = new JComboBox<String>(
				new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" });
		days.setBounds(10, 36, 80, 20);
		p.add(days);

		timeLabel = new JLabel("Time (XX:XX)");
		timeLabel.setBounds(108, 10, 90, 15);
		p.add(timeLabel);

		startTimeLabel = new JLabel("Start");
		startTimeLabel.setBounds(112, 30, 50, 15);
		p.add(startTimeLabel);

		endTimeLabel = new JLabel("End");
		endTimeLabel.setBounds(162, 30, 50, 15);
		p.add(endTimeLabel);

		startTime = new JTextField();
		startTime.setBounds(110, 50, 40, 20);
		p.add(startTime);

		endTime = new JTextField();
		endTime.setBounds(155, 50, 40, 20);
		p.add(endTime);

		view = new JButton("View");
		view.setBounds(112, 80, 90, 20);
		p.add(view);

		update = new JButton("Update");
		update.setBounds(10, 80, 90, 20);
		p.add(update);

		view.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				viewActionPerformed(evt);
			}
		});

		update.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateActionPerformed(evt);
			}
		});

		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	private void viewActionPerformed(java.awt.event.ActionEvent evt) {
		String[] hours = CarShopController.getGarageHours();
		JFrame businessHours = new JFrame();
		businessHours.setTitle("Garage Hours");
		businessHours.setBounds(400, 300, 200, 300);
		JLabel label = new JLabel("Garage Opening Hours:");
		label.setBounds(20, 10, 140, 15);
		businessHours.add(label);
		for (int j = 0; j < 7; j++) {
			JLabel temp = new JLabel();
			JLabel start = new JLabel("");
			JLabel end = new JLabel("");
			switch (j) {
			case 0:
				temp.setText("Monday");
				temp.setBounds(10, 30, 70, 15);
				for (int i = 0; i < hours.length; i += 3) {
					if (hours[i].equals(temp.getText())) {
						start.setText(hours[i + 1].substring(0, 5));
						end.setText(hours[i + 2].substring(0, 5));
						break;
					}
				}
				start.setBounds(90, 30, 60, 15);
				end.setBounds(140, 30, 60, 15);
				break;
			case 1:
				temp.setText("Tuesday");
				temp.setBounds(10, 50, 70, 15);
				for (int i = 0; i < hours.length; i += 3) {
					if (hours[i].equals(temp.getText())) {
						start.setText(hours[i + 1].substring(0, 5));
						end.setText(hours[i + 2].substring(0, 5));
						break;
					}
				}
				start.setBounds(90, 50, 60, 15);
				end.setBounds(140, 50, 60, 15);
				break;
			case 2:
				temp.setText("Wednesday");
				temp.setBounds(10, 70, 70, 15);
				for (int i = 0; i < hours.length; i += 3) {
					if (hours[i].equals(temp.getText())) {
						start.setText(hours[i + 1].substring(0, 5));
						end.setText(hours[i + 2].substring(0, 5));
						break;
					}
				}
				start.setBounds(90, 70, 60, 15);
				end.setBounds(140, 70, 60, 15);
				break;
			case 3:
				temp.setText("Thursday");
				temp.setBounds(10, 90, 70, 15);
				for (int i = 0; i < hours.length; i += 3) {
					if (hours[i].equals(temp.getText())) {
						start.setText(hours[i + 1].substring(0, 5));
						end.setText(hours[i + 2].substring(0, 5));
						break;
					}
				}
				start.setBounds(90, 90, 60, 15);
				end.setBounds(140, 90, 60, 15);
				break;
			case 4:
				temp.setText("Friday");
				temp.setBounds(10, 110, 70, 15);
				for (int i = 0; i < hours.length; i += 3) {
					if (hours[i].equals(temp.getText())) {
						start.setText(hours[i + 1].substring(0, 5));
						end.setText(hours[i + 2].substring(0, 5));
						break;
					}
				}
				start.setBounds(90, 110, 60, 15);
				end.setBounds(140, 110, 60, 15);
				break;
			case 5:
				temp.setText("Saturday");
				temp.setBounds(10, 130, 70, 15);
				for (int i = 0; i < hours.length; i += 3) {
					if (hours[i].equals(temp.getText())) {
						start.setText(hours[i + 1].substring(0, 5));
						end.setText(hours[i + 2].substring(0, 5));
						break;
					}
				}
				start.setBounds(90, 130, 60, 15);
				end.setBounds(140, 130, 60, 15);
				break;
			case 6:
				temp.setText("Sunday");
				temp.setBounds(10, 150, 70, 15);
				for (int i = 0; i < hours.length; i += 3) {
					if (hours[i].equals(temp.getText())) {
						start.setText(hours[i + 1].substring(0, 5));
						end.setText(hours[i + 2].substring(0, 5));
						break;
					}
				}
				start.setBounds(90, 150, 60, 15);
				end.setBounds(140, 150, 60, 15);
				break;
			}
			businessHours.add(temp);
			businessHours.add(start);
			businessHours.add(end);
		}
		businessHours.setVisible(true);
		businessHours.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	private void updateActionPerformed(java.awt.event.ActionEvent evt) {
		if (startTime.getText().trim().equals("") || endTime.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "A start time and end time need to be written", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		String day = (String) days.getSelectedItem();
		String[] temp = CarShopController.getGarageBusinessHour(day);
		try {
			if (temp[0].equals("")) {
				CarShopController.addNewGarageOpeningHours(day, startTime.getText(), endTime.getText(), CarShopController.getTechnicianType());
			} else {
				CarShopController.removeGarageOpeningHours(day, temp[0], temp[1], CarShopController.getTechnicianType());
				CarShopController.addNewGarageOpeningHours(day, startTime.getText(), endTime.getText(), CarShopController.getTechnicianType());
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		startTime.setText("");
		endTime.setText("");
	}

}
