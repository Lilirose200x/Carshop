package ca.mcgill.ecse.carshop.view;

import ca.mcgill.ecse.carshop.application.CarShopApplication;
import ca.mcgill.ecse.carshop.controller.CarShopController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AccountWindow extends JInternalFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6724431677711578031L;
	
	// for account
    private JLabel label_password, label_username;
    private JTextField text_username, text_password;
    private JLabel nameLabel;
    // buttons
    private JButton deleteButton, editButton;
    // data
    JLabel errorMess;
    private String error = "";
    private JFrame main;
    public AccountWindow(JFrame main) {
        super("My Account", true, true, true);
        this.main = main;
        initComponents();
        refreshData();
    }

    private void initComponents() {
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        // label
        errorMess = new JLabel();
        errorMess.setForeground(Color.CYAN);
        nameLabel = new JLabel(" I am Owner ");
        Border bord = BorderFactory.createLineBorder(Color.RED, 1);
        nameLabel.setBorder(bord);
        nameLabel.setVisible(false);
        label_username = new JLabel("Username");
        label_password = new JLabel("Password");
        // field
        text_password = new JTextField();
        text_password.setEnabled(false);
        text_password.setDisabledTextColor(Color.GRAY);
        text_username = new JTextField();
        text_username.setEnabled(false);
        text_username.setDisabledTextColor(Color.GRAY);
        // button
        deleteButton = new JButton();
        deleteButton.setText("Delete");
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(Color.RED);
        editButton = new JButton();
        editButton.setText("Edit");
        if (CarShopController.isCustomer()){
            deleteButton.setVisible(true);
        }
        else{
            deleteButton.setVisible(false);
        }
        if (CarShopController.getCurrentUser().getUsername().equals("owner")) {
            nameLabel.setVisible(true);
        }
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("acc Page");

        // layout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout
                        .createSequentialGroup()
                        .addGroup(
                                layout
                                        .createParallelGroup()
                                        .addComponent(nameLabel, GroupLayout.Alignment.CENTER)
                                        .addComponent(errorMess)
                                        .addGroup(
                                                layout
                                                        .createSequentialGroup()
                                                        .addComponent(label_username)
                                                        .addComponent(text_username, 400, 400, 400))
                                        .addGroup(
                                                layout
                                                        .createSequentialGroup()
                                                        .addComponent(label_password)
                                                        .addComponent(text_password, 400, 400, 400))
                                        .addGroup(
                                                layout.createSequentialGroup().addComponent(editButton).addComponent(deleteButton))));

        layout.setVerticalGroup(
                layout
                        .createParallelGroup()
                        .addGroup(
                                layout
                                        .createSequentialGroup()
                                        .addComponent(nameLabel)
                                        .addComponent(errorMess)
                                        .addGroup(
                                                layout
                                                        .createParallelGroup()
                                                        .addComponent(text_username)
                                                        .addComponent(label_username))
                                        .addGroup(
                                                layout
                                                        .createParallelGroup()
                                                        .addComponent(label_password)
                                                        .addComponent(text_password))
                                        .addGroup(
                                                layout.createParallelGroup().addComponent(deleteButton).addComponent(editButton))));

        layout.linkSize(SwingConstants.VERTICAL, label_username, text_username);
        layout.linkSize(SwingConstants.VERTICAL, label_password, text_username);
        pack();

        editButton.addActionListener(
                new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        editButtonActionPerformed(evt);
                    }
                });
        deleteButton.addActionListener(
                new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        deleteButtonActionPerformed(evt);
                    }
                });
    }

    private void editButtonActionPerformed(ActionEvent evt) {
        error = "";
        if (editButton.getText().equals("Edit")){
            editButton.setText("Save");
            text_username.setEnabled(true);
            if (!CarShopController.isCustomer()) text_username.setEnabled(false);
            text_password.setEnabled(true);
        }
        else{
            try{
                editButton.setText("Edit");
                CarShopController.updateAccount(text_username.getText(), text_password.getText());
                text_username.setEnabled(false);
                text_password.setEnabled(false);
            } catch (Exception e) {
                error = e.getMessage();
            }
            refreshData();
        }
    }

    private void deleteButtonActionPerformed(ActionEvent evt) {
        error = "";
        try{
            if (JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this account? All future appointments will be canceled", "Close Window?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                CarShopController.deleteCustomer();
                if (main instanceof MainWindowTechnician)
                    ((MainWindowTechnician)main).setInvisible();
                if (main instanceof MainWindowOwner)
                    ((MainWindowOwner)main).setInvisible();
                if (main instanceof MainWindowCustomer)
                    ((MainWindowCustomer)main).setInvisible();
                main.setVisible(false);
                CarShopApplication.loginWindow.setVisible(true);
            }
        }
        catch(Exception e) {
            error = e.getMessage();
        }
    }

    private void refreshData() {
        errorMess.setText(error);
        text_username.setText(CarShopController.getCurrentUser().getUsername());
        text_password.setText(CarShopController.getCurrentUser().getPassword());
    }
}