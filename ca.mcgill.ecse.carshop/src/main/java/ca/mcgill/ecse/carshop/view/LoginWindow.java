package ca.mcgill.ecse.carshop.view;

import ca.mcgill.ecse.carshop.controller.CarShopController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LoginWindow extends JFrame implements ActionListener, FocusListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7898050923914177629L;
	JButton btn_login, btn_register;
    JLabel laccount, lpassword;
    public static JTextField taccount;
    JTextField tpassword;

    public MainWindowOwner mainWindowOwner;
    public MainWindowCustomer  mainWindowCustomer;
    public MainWindowTechnician mainWindowTechnician;

    public LoginWindow() {
        super();
      
        this.setTitle("Carshop");
        this.setBounds(100, 80, 385, 260);
        //setResizable(false);
        Container p = getContentPane();
        p.setLayout(null);

        laccount = new JLabel("UserName :");
        laccount.setBounds(60, 75, 80, 20);
        p.add(laccount);

        taccount = new JTextField("Login with username");
        taccount.setBounds(130, 75, 150, 22);
        taccount.addActionListener(this);
        taccount.addFocusListener(this);
        p.add(taccount);

        lpassword = new JLabel("Password :");
        lpassword.setBounds(60, 120, 80, 22);
        p.add(lpassword);

        tpassword = new JPasswordField(8);
        tpassword.setBounds(130, 120, 150, 22);
        tpassword.setFocusable(true);
        p.add(tpassword);

        btn_register = new JButton("Register");
        btn_register.setBounds(225, 180, 100, 27);
        btn_register.addActionListener(this);
        p.add(btn_register);

        btn_login = new JButton("Login");
        btn_login.setBounds(105, 180, 100, 27);
        btn_login.addActionListener(this);
        p.add(btn_login);
        
        
       
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        CarShopController.setDateAndTime();
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btn_register) {
            try {
            	if(!CarShopController.getBusinessInfo().getName().equals("")) {
                CarShopController.createCustomer(taccount.getText(), tpassword.getText());
            	}else {
            		throw new RuntimeException("Must setup the business first");
            	}
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                taccount.setText(null);
                tpassword.setText(null);
            }
        }

        if (e.getSource() == btn_login) {

            if (taccount.getText().length() == 0 || tpassword.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "Username or password can not be empty", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                   if(!CarShopController.getBusinessInfo().getName().equals("") || (taccount.getText().equals("owner") && tpassword.getText().equals("owner"))) {
                	
	                    CarShopController.LogIn(taccount.getText(), tpassword.getText());
	                    LoginWindow.this.setVisible(false);
	                    
	                    if(CarShopController.getCurrentType().equals("owner")) {
	                    mainWindowOwner = new MainWindowOwner();
	                    mainWindowOwner.setVisible(true);
	                    }else if(CarShopController.getCurrentType().equals("technician")) {
		                        mainWindowTechnician = new MainWindowTechnician();
		                        mainWindowTechnician.setVisible(true);
		                }else if(CarShopController.getCurrentType().equals("customer")) {
		                        mainWindowCustomer = new MainWindowCustomer();
		                        mainWindowCustomer.setVisible(true);
		                }else {
		                    	throw new RuntimeException("Username/password not found");
		                }
                    }else {
                    	throw new RuntimeException("Must setup the business first");
                    }
//                    carMain mframe = new carMain();
//                    mframe.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    taccount.setText("");
                    tpassword.setText("");
                }
            }
        }
    }

    public void focusGained(FocusEvent e) {

        if (e.getSource() == taccount) {
            taccount.setText(null);
        }

        if (e.getSource() == tpassword) {
            tpassword.setText(null);
        }

    }

    public void focusLost(FocusEvent e) {
        // TODO Auto-generated method stub

    }
}
