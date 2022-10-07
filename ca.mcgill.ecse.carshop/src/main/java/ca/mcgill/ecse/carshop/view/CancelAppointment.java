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
public class CancelAppointment extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4359414648227605847L;

	// UI elements
	private JLabel errorMessage;

	private JLabel appointment;
	private JComboBox<String> apps;
	private JButton cancel;

	public CancelAppointment() {
		super();
		this.setTitle("Cancel");
		this.setBounds(400, 300, 500, 150);
		initComponents();
	}

	public void initComponents() {

		Container p = getContentPane();
		p.setLayout(null);
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);

		appointment = new JLabel("Appointments:");
		appointment.setBounds(200, 10, 150, 15);
		p.add(appointment);

		apps = new JComboBox<String>(CarShopController.getAppointments());
		apps.setBounds(20, 30, 400, 25);
		p.add(apps);

		cancel = new JButton("Cancel");
		cancel.setBounds(200, 75, 100, 35);
		p.add(cancel);

		cancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String s = (String) apps.getSelectedItem();
		String[] in = s.split(" - ");
		try {
			CarShopController.cancelAppointment(in[0], in[1], in[2]);
			apps.removeAllItems();
			for (String st : CarShopController.getAppointments()) {
				apps.addItem(st);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}
}
