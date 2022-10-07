package ca.mcgill.ecse.carshop.view;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import ca.mcgill.ecse.carshop.controller.CarShopController;
import ca.mcgill.ecse.carshop.controller.TOBusiness;

public class BusinessPage  extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8534369555145676170L;
	private JLabel errorMessage;
	private JLabel Name;
	private JLabel Address;
	private JLabel Email;
	private JLabel Phone;
	private JLabel BusinessHours;
	private JLabel Vacations;
	private JLabel Holidays;
	
	private JLabel Monday;
	private JLabel Tuesday;
	private JLabel Wednesday;
	private JLabel Thursday;
	private JLabel Friday;
	private JLabel Saturday;
	private JLabel Sunday;
	
	private JTextField Mondaystart;
	private JTextField Tuesdaystart;
	private JTextField Wednesdaystart;
	private JTextField Thursdaystart;
	private JTextField Fridaystart;
	private JTextField Saturdaystart;
	private JTextField Sundaystart;
	private JTextField Mondayend;
	private JTextField Tuesdayend;
	private JTextField Wednesdayend;
	private JTextField Thursdayend;
	private JTextField Fridayend;
	private JTextField Saturdayend;
	private JTextField Sundayend;
	 private JButton Update2;
	    private JButton Clear2;
	
	
	private JLabel CurrentName;
	private JLabel CurrentAddress;
	private JLabel CurrentEmail;
	private JLabel CurrentPhone;
	
	private JLabel CurrentNameStringLabel;
	private JLabel CurrentAddressStringLabel;
	private JLabel CurrentPhoneStringLabel;
	private JLabel CurrentEmailStringLabel;
	private JLabel CurrentVacation;
	private JLabel CurrentHoliday;
	
    private JTextField NameField;
    private JTextField AddressField;
    private JTextField EmailField;
    private JTextField PhoneField;
    private JButton Update;
    private JButton Clear;
    
    private JTextField VacationStart;
    private JTextField VacationStartDate;
    private JTextField VacationEnd;
    private JTextField VacationEndDate;
    private JButton setVacation;
    
    private JTextField HolidayStart;
    private JTextField HolidayStartDate;
    private JTextField HolidayEnd;
    private JTextField HolidayEndDate;
    private JButton setHoliday;
    
    //data element
    private String CurrentNameString = "";
    private String CurrentAddressString = "";
    private String CurrentPhoneString = "";
    private String CurrentEmailString = "";
    
    private String CurrentVacationString = "";
    private String CurrentHolidayString = "";
    
    private String CurrentMonday = "";
    private String CurrentTuesday = "";
    private String CurrentWednesday = "";
    private String CurrentThursday = "";
    private String CurrentFriday="";
    private String CurrentSaturday = "";
    private String CurrentSunday = "";
    
    private String CurrentMondayend = "";
    private String CurrentTuesdayend = "";
    private String CurrentWednesdayend = "";
    private String CurrentThursdayend = "";
    private String CurrentFridayend="";
    private String CurrentSaturdayend = "";
    private String CurrentSundayend = "";
    private String error = "";
	
	  public BusinessPage() {
		    super("Business", false,false,false,false);
		    initComponents();
		    refreshData();
		  }
	  
	  private void initComponents() {
		   ((BasicInternalFrameUI)this.getUI()).setNorthPane(null);
		   errorMessage = new JLabel();
		   errorMessage.setForeground(Color.RED);
		   errorMessage.setText(error);
		   
		   Name = new JLabel();
		   Name.setText("Name:");
		   
		   BusinessHours = new JLabel();
		   BusinessHours.setText("Business Hours:");
		   
		   Vacations = new JLabel();
		   Vacations.setText("Vacations:");
		   
		   Holidays = new JLabel();
		   Holidays.setText("Holidays");
		   
		   Address = new JLabel();
		   Address.setText("Address:");
		   
		   Email = new JLabel();
		   Email.setText("Email:");
		   
		   Phone = new JLabel();
		   Phone.setText("Phone:");
		   
		   CurrentName = new JLabel();
		   CurrentName.setText("Name:");
		   
		   CurrentAddress = new JLabel();
		   CurrentAddress.setText("Address:");
		   
		   CurrentEmail = new JLabel();
		   CurrentEmail.setText("Email:");
		   
		   CurrentPhone = new JLabel();
		   CurrentPhone.setText("Phone:");
		   
		   Monday = new JLabel();
		   Monday.setText("Monday:");
		   
		   Tuesday = new JLabel();
		   Tuesday.setText("Tuesday:");
		   
		   Wednesday = new JLabel();
		   Wednesday.setText("Wednesday:");
		   
		   Thursday = new JLabel();
		   Thursday.setText("Thursday:");
		   
		   Friday = new JLabel();
		   Friday.setText("Friday:");
		   
		   Saturday = new JLabel();
		   Saturday.setText("Saturday:");
		   
		   Sunday = new JLabel();
		   Sunday.setText("Sunday:");
		   
		   NameField = new JTextField();
		   EmailField = new JTextField();
		   PhoneField = new JTextField();
           AddressField = new JTextField();
           
           Mondaystart = new JTextField();
           Mondaystart.setText(CurrentMonday);
       	   Tuesdaystart= new JTextField();
       	   Tuesdaystart.setText(CurrentTuesday);
           Wednesdaystart = new JTextField();
           Wednesdaystart.setText(CurrentWednesday);
       	   Thursdaystart= new JTextField();
       	   Thursdaystart.setText(CurrentThursday);
           Fridaystart= new JTextField();
           Fridaystart.setText(CurrentFriday);
       	   Saturdaystart= new JTextField();
       	   Saturdaystart.setText(CurrentSaturday);
       	   Sundaystart= new JTextField();
       	   Sundaystart.setText(CurrentSunday);
       	   
       	   Mondayend= new JTextField();
           Mondayend.setText(CurrentMondayend);
       	   Tuesdayend= new JTextField();
       	   Tuesdayend.setText(CurrentTuesdayend);
       	   Wednesdayend= new JTextField();
       	   Wednesdayend.setText(CurrentWednesdayend);
       	   Thursdayend= new JTextField();
       	   Thursdayend.setText(CurrentThursdayend);
           Fridayend= new JTextField();
           Fridayend.setText(CurrentFridayend);
       	   Saturdayend= new JTextField();
       	   Saturdaystart.setText(CurrentSaturdayend);
       	   Sundayend= new JTextField();
       	   Sundayend.setText(CurrentSundayend);
       	   
       	  HolidayStart = new JTextField();
       	  HolidayStart.setText("Start Time");
          HolidayStartDate = new JTextField();
          HolidayStartDate.setText("Start Date");
          HolidayEnd = new JTextField();
          HolidayEnd.setText("End Time");
          HolidayEndDate = new JTextField();
          HolidayEndDate.setText("End Date");
          
          VacationStart = new JTextField();
          VacationStart.setText("Start Time");
          VacationStartDate = new JTextField();
          VacationStartDate.setText("Start Date");
          VacationEnd = new JTextField();
          VacationEnd.setText("End Time");
          VacationEndDate = new JTextField();
          VacationEndDate.setText("End Date");
		   
		   CurrentNameStringLabel = new JLabel();
		   CurrentNameStringLabel.setText(CurrentNameString);
		   
		   CurrentAddressStringLabel = new JLabel();
		   CurrentAddressStringLabel.setText(CurrentAddressString);
		   
		   CurrentPhoneStringLabel = new JLabel();
		   CurrentPhoneStringLabel.setText(CurrentPhoneString);
		   
		   CurrentEmailStringLabel = new JLabel();
		   CurrentEmailStringLabel.setText(CurrentEmailString);
		   
		   CurrentVacation = new JLabel();
		   CurrentVacation.setText(CurrentVacationString);
		   
		   CurrentHoliday = new JLabel();
		   CurrentHoliday.setText(CurrentHolidayString);
		   
		   Update = new JButton();
		   Update.setText("Update");
		   
		   Clear = new JButton();
		   Clear.setText("Clear");
		   
		   Update2 = new JButton();
		   Update2.setText("Update");
		   
		   Clear2 = new JButton();
		   Clear2.setText("Clear");
		   
		   setVacation = new JButton();
		   setVacation.setText("Set");
		   
		   setHoliday = new JButton();
		   setHoliday.setText("Set");
		   
		   
		   setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		   setTitle("Business Page");
		    

		    Update.addActionListener(
		            new java.awt.event.ActionListener() {
		              public void actionPerformed(java.awt.event.ActionEvent evt) {
		                UpdateActionPerformed(evt);
		              }
		            });
		    
		    Clear.addActionListener(
		            new java.awt.event.ActionListener() {
		              public void actionPerformed(java.awt.event.ActionEvent evt) {
		                ClearActionPerformed(evt);
		              }
		            });
		    Update2.addActionListener(
		            new java.awt.event.ActionListener() {
		              public void actionPerformed(java.awt.event.ActionEvent evt) {
		                Update2ActionPerformed(evt);
		              }
		            });
		    
		    Clear2.addActionListener(
		            new java.awt.event.ActionListener() {
		              public void actionPerformed(java.awt.event.ActionEvent evt) {
		                Clear2ActionPerformed(evt);
		              }
		            });
		    setVacation.addActionListener(
		            new java.awt.event.ActionListener() {
		              public void actionPerformed(java.awt.event.ActionEvent evt) {
		                setVacationActionPerformed(evt);
		              }
		            });
		    setHoliday.addActionListener(
		            new java.awt.event.ActionListener() {
		              public void actionPerformed(java.awt.event.ActionEvent evt) {
		                setHolidayActionPerformed(evt);
		              }
		            });
		    
		   // horizontal line elements
		    JSeparator horizontalLineTop = new JSeparator();
		    JSeparator horizontalLineTop2 = new JSeparator();
		    JSeparator horizontalLineTop3 = new JSeparator();
		    JSeparator horizontalLineTop4 = new JSeparator();
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
				                 .addComponent(errorMessage)
				                 .addComponent(horizontalLineTop)
				                 .addGroup(
				                		 layout
				                		.createSequentialGroup()
						    			.addGroup(
						    					layout
						    					 .createParallelGroup()
				                                 .addComponent(CurrentName)
				                                 .addComponent(Name)
				                                 .addComponent(Update)
						    					)
						    			.addGroup(
						    					layout
						    					 .createParallelGroup()
				                                 .addComponent(CurrentNameStringLabel)
				                                 .addComponent(NameField,200,200,400)
				                                 .addComponent(Clear)
						    					)
						    			.addGroup(
						    					layout
						    					.createParallelGroup()
						    					.addComponent(CurrentAddress)
						    					.addComponent(Address)
						    					)
						    			.addGroup(
						    					layout
						    					.createParallelGroup()
						    					.addComponent(CurrentAddressStringLabel)
						    					.addComponent(AddressField,200,200,400)
						    					)
						    			.addGroup(
						    					layout
						    					.createParallelGroup()
						    					.addComponent(CurrentPhone)
						    					.addComponent(Phone)
						    					)
						    			.addGroup(
						    					layout
						    					.createParallelGroup()
						    					.addComponent(CurrentPhoneStringLabel)
						    					.addComponent(PhoneField,200,200,400)
						    					)
						    			.addGroup(
						    					layout
						    					.createParallelGroup()
						    					.addComponent(CurrentEmail)
						    					.addComponent(Email)
						    					)
						    			.addGroup(
						    					layout
						    					.createParallelGroup()
						    					.addComponent(CurrentEmailStringLabel)
						    					.addComponent(EmailField,200,200,400)
				    					)
				    			)
				                .addComponent(horizontalLineTop2)
				                .addComponent(BusinessHours)
				                .addGroup(
				                		layout
				                		.createSequentialGroup()
				                		.addGroup(
				                			layout
					                		.createParallelGroup()
					                		.addComponent(Monday)
					                		.addComponent(Tuesday)
					                		.addComponent(Wednesday)
					                		.addComponent(Thursday)
					                		.addComponent(Friday)
					                		.addComponent(Saturday)
					                		.addComponent(Sunday)
					                		)
				                		.addGroup(
					                			layout
						                		.createParallelGroup()
						                		.addComponent(Mondaystart)
						                		.addComponent(Tuesdaystart)
						                		.addComponent(Wednesdaystart)
						                		.addComponent(Thursdaystart)
						                		.addComponent(Fridaystart)
						                		.addComponent(Saturdaystart)
						                		.addComponent(Sundaystart)
						                		)
				                		.addGroup(
					                			layout
						                		.createParallelGroup()
						                		.addComponent(Mondayend)
						                		.addComponent(Tuesdayend)
						                		.addComponent(Wednesdayend)
						                		.addComponent(Thursdayend)
						                		.addComponent(Fridayend)
						                		.addComponent(Saturdayend)
						                		.addComponent(Sundayend)
						                		)
				                		)
				                .addGroup(
				                		layout
				                		.createSequentialGroup()
				                		.addComponent(Update2)
				                		.addComponent(Clear2)
				                		)
				                .addComponent(horizontalLineTop3)
				                .addGroup(
				                		layout
				                		.createSequentialGroup()
				                		.addComponent(Vacations)
				                		.addComponent(CurrentVacation)
				                		
				                		)
				                .addGroup(
				                		layout
				                		.createSequentialGroup()
				                		.addComponent(setVacation)
				                		.addComponent(VacationStart)
				                		.addComponent(VacationStartDate)
				                		.addComponent(VacationEnd)
				                		.addComponent(VacationEndDate)
				                		)
				                .addComponent(horizontalLineTop4)
				                .addGroup(
				                		layout
				                		.createSequentialGroup()
				                		.addComponent(Holidays)
				                		.addComponent(CurrentHoliday)
				                		)
				                .addGroup(
				                		layout
				                		.createSequentialGroup()
				                		.addComponent(setHoliday)
				                		.addComponent(HolidayStart)
				                		.addComponent(HolidayStartDate)
				                		.addComponent(HolidayEnd)
				                		.addComponent(HolidayEndDate)
				                		)
				               
				                
		    			)
		    			);
		    
		    layout.setVerticalGroup(
		    		layout
		    			.createSequentialGroup()
		    			.addComponent(errorMessage)
		    			.addGroup(
		    					layout
		    					 .createParallelGroup()
                                 .addComponent(CurrentName)
                                 .addComponent(CurrentNameStringLabel)
                               	 .addComponent(CurrentAddress)
                             	 .addComponent(CurrentAddressStringLabel)
                             	 .addComponent(CurrentPhone)
                             	 .addComponent(CurrentPhoneStringLabel)
                             	 .addComponent(CurrentEmail)
                             	 .addComponent(CurrentEmailStringLabel)
                             	
		    					)
		    			.addComponent(horizontalLineTop)
		    			.addGroup(
		    					layout
		    					 .createParallelGroup()
                                 .addComponent(Name)
                                 .addComponent(NameField)
                                 .addComponent(Address)
                                 .addComponent(AddressField)
                                 .addComponent(Phone)
                              	 .addComponent(PhoneField)
                                 .addComponent(Email)
                                 .addComponent(EmailField)
		    					)
		    			.addGroup(
		    					layout
		    					.createParallelGroup()
		    					.addComponent(Update)
		    					.addComponent(Clear)
		    					)
		    			  .addComponent(horizontalLineTop2)
		    			  .addComponent(BusinessHours)
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(Monday)
		    					  .addComponent(Mondaystart)
		    					  .addComponent(Mondayend)
			    					)
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(Tuesday)
		    					  .addComponent(Tuesdaystart)
		    					  .addComponent(Tuesdayend)
			    					)
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(Wednesday)
		    					  .addComponent(Wednesdaystart)
		    					  .addComponent(Wednesdayend)
			    					)
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(Thursday)
		    					  .addComponent(Thursdaystart)
		    					  .addComponent(Thursdayend)
			    					)
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(Friday)
		    					  .addComponent(Fridaystart)
		    					  .addComponent(Fridayend)
			    					)
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(Saturday)
		    					  .addComponent(Saturdaystart)
		    					  .addComponent(Saturdayend)
			    					)
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(Sunday)
		    					  .addComponent(Sundaystart)
		    					  .addComponent(Sundayend)
			    					)
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(Update2)
		    					  .addComponent(Clear2)
		    					  )
		    			  .addComponent(horizontalLineTop3)
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(Vacations)
		    					  .addComponent(CurrentVacation)
		    					  )
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(setVacation)
			                	  .addComponent(VacationStart)
			                      .addComponent(VacationStartDate)
			                	  .addComponent(VacationEnd)
			                	  .addComponent(VacationEndDate)
		    					  )
		    			  .addComponent(horizontalLineTop4)
		    			  .addGroup(
		    					  layout
		    					  .createParallelGroup()
		    					  .addComponent(Holidays)
		    					  .addComponent(CurrentHoliday)
		    					  )
		    			  .addGroup(
			                		layout
			                		.createParallelGroup()
			                		.addComponent(setHoliday)
			                		.addComponent(HolidayStart)
			                		.addComponent(HolidayStartDate)
			                		.addComponent(HolidayEnd)
			                		.addComponent(HolidayEndDate)
			                		)
		    			);
		    pack();
	  }
	  
	  
	  
	  private void UpdateActionPerformed(java.awt.event.ActionEvent evt) {
		        error = "";
		    	String name = NameField.getText();
		    	String address = AddressField.getText();
		    	String email = EmailField.getText();
		    	String phone = PhoneField.getText();
		    	
		      try {
		    	  if(CurrentNameString.equals("")) {
		    		  if(name.equals("") | address.equals("") 
			    			  | phone.equals("") | email.equals("")) {
			    		  throw new RuntimeException("Cannot set business info with empty slots");
			    	  }else {
			    		  CarShopController.setupBusinessInfo(name, address, phone, email);
			    	  }
		    	  }else if(name.equals("") && address.equals("") 
		    			  && phone.equals("") && email.equals("")) {
		    		  throw new RuntimeException("Cannot update with no slots entered");
		    	  }
		    	  
		    	  if(!name.equals("")) {
		    		  CurrentNameString = name;
		    		  CarShopController.updateBusiness(name, CurrentAddressString, CurrentPhoneString, CurrentEmailString);
		    	  }
		    	  if(!address.equals("")) {
		    		  CurrentAddressString = address;
		    		  CarShopController.updateBusiness(CurrentNameString, address, CurrentPhoneString, CurrentEmailString);
		    	  }
		    	  if(!email.equals("")) {
		    		  CurrentEmailString = email;
		    		  CarShopController.updateBusiness(CurrentNameString, CurrentAddressString, CurrentPhoneString, email);
		    	  }
		    	  if(!phone.equals("")) {
		    		  CurrentPhoneString = phone;
		    		  CarShopController.updateBusiness(CurrentNameString, CurrentAddressString, phone, CurrentEmailString);
		    	  }
		    	  refreshData();
		    	  
		      } catch (Exception e) {
		        error = e.getMessage();
		        refreshData();
		      }
		 
	  }
	  
	  private void Update2ActionPerformed(java.awt.event.ActionEvent evt) {
		   error = "";
	       try { 
	    	  TOBusiness business = CarShopController.getBusinessInfo();
	    	  
	    	  if(!business.getName().equals("")) {
	    		  
		    	  if(!Mondaystart.getText().trim().equals("") && !Mondayend.getText().trim().equals("")) {
			    	  if(business.getStartMap().isEmpty() || !business.getStartMap().containsKey("Monday")) {
			    		  CarShopController.addBusinessHour("Monday", Mondaystart.getText(), Mondayend.getText());
			    	  }else {
			    		  CarShopController.updateBusinessHour("Monday",business.getStartMap().get("Monday"),"Monday", Mondaystart.getText(), Mondayend.getText());
			    	  }
		    	  }
		    	  
		    	  if(!Tuesdaystart.getText().trim().equals("") && !Tuesdayend.getText().trim().equals("")) {
			    	  if(business.getStartMap().isEmpty() || !business.getStartMap().containsKey("Tuesday")) {
			    		  CarShopController.addBusinessHour("Tuesday", Tuesdaystart.getText(), Tuesdayend.getText());
			    	  }else {
			    		  CarShopController.updateBusinessHour("Tuesday",business.getStartMap().get("Tuesday"),"Tuesday", Tuesdaystart.getText(), Tuesdayend.getText());    		  
			    	  }
		    	  }
		    	  
		    	  if(!Wednesdaystart.getText().trim().equals("") && !Wednesdayend.getText().trim().equals("")) {
			    	  if(business.getStartMap().isEmpty()|| !business.getStartMap().containsKey("Wednesday")) {
			    		  CarShopController.addBusinessHour("Wednesday", Wednesdaystart.getText(), Wednesdayend.getText());
			    	  }else {
			    		  CarShopController.updateBusinessHour("Wednesday",business.getStartMap().get("Wednesday"),"Wednesday", Wednesdaystart.getText(), Wednesdayend.getText());
			    	  }
		    	  }
		    	  
		    	  if(!Thursdaystart.getText().trim().equals("") && !Thursdayend.getText().trim().equals("")) {
			    	  if(business.getStartMap().isEmpty() || !business.getStartMap().containsKey("Thursday")) {
			    		  CarShopController.addBusinessHour("Thursday", Thursdaystart.getText(), Thursdayend.getText());
			    	  }else {
			    		  CarShopController.updateBusinessHour("Thursday",business.getStartMap().get("Thursday"),"Thursday", Thursdaystart.getText(), Thursdayend.getText());	  
			    	  }
		    	  }
		    	  if(!Fridaystart.getText().trim().equals("") && !Fridayend.getText().trim().equals("")) {
			    	  if(business.getStartMap().isEmpty()|| !business.getStartMap().containsKey("Friday")) {
			    		  CarShopController.addBusinessHour("Friday", Fridaystart.getText(), Fridayend.getText());
			    	  }else {
			    		  CarShopController.updateBusinessHour("Friday",business.getStartMap().get("Friday"),"Friday", Fridaystart.getText(), Fridayend.getText());  
			    	  }
		    	  }
		    	  if(!Saturdaystart.getText().trim().equals("") && !Saturdayend.getText().trim().equals("")) {
			    	  if(business.getStartMap().isEmpty() || !business.getStartMap().containsKey("Saturday")) {
			    		  CarShopController.addBusinessHour("Saturday", Saturdaystart.getText(), Saturdayend.getText());
			    	  }else {
			    		  CarShopController.updateBusinessHour("Saturday",business.getStartMap().get("Saturday"),"Saturday", Saturdaystart.getText(), Saturdayend.getText());	  
			    	  }
		    	  }
		    	  if(!Sundaystart.getText().trim().equals("") && !Sundayend.getText().trim().equals("")) {
			    	  if(business.getStartMap().isEmpty() || !business.getStartMap().containsKey("Sunday")) {
			    		  CarShopController.addBusinessHour("Sunday", Sundaystart.getText(), Sundayend.getText());
			    	  }else {
			    		  CarShopController.updateBusinessHour("Sunday",business.getStartMap().get("Sunday"),"Sunday", Sundaystart.getText(), Sundayend.getText()); 
			    	  }
		    	  }
	    	  }else {
	    		  throw new RuntimeException("Create business before changing businessHours");
	    	  }
	    	  refreshData();
	    	  
	      } catch (Exception e) {
	    	System.out.println(e.getMessage());
	        error = e.getMessage();
	        refreshData();
	      }
	 
	  }
	  
	  private void setVacationActionPerformed(java.awt.event.ActionEvent evt) {
		  try {
			  if(!VacationStart.getText().equals("Start Time") && !VacationStartDate.getText().equals("Start Date")
					  && !VacationEnd.getText().equals("End Time") && !VacationEndDate.getText().equals("End Date")) {
			  CarShopController.addVacationSlot(VacationStartDate.getText(),VacationStart.getText(), VacationEndDate.getText(), VacationEnd.getText());
			  }else {
				  
				  throw new RuntimeException("All slots of Vacation must be filled");
			  }
			  refreshData();
		  }catch(Exception e) {
			    System.out.println(e.getMessage());
		        error = e.getMessage();
		        refreshData();
		  }
		  
	  }
	  private void setHolidayActionPerformed(java.awt.event.ActionEvent evt) {
		  try {
			  if(!HolidayStart.getText().equals("Start Time") && !HolidayStartDate.getText().equals("Start Date")
					  && !HolidayEnd.getText().equals("End Time") && !HolidayEndDate.getText().equals("End Date")) {
			  
			  CarShopController.addHolidaySlot(HolidayStartDate.getText(),HolidayStart.getText(), HolidayEndDate.getText(), HolidayEnd.getText());
			  
			  }else {
				  throw new RuntimeException("All slots of Holiday must be filled");		  
			  }
			  refreshData();
		  }catch(Exception e) {
			  System.out.println(e.getMessage());
		        error = e.getMessage();
		        refreshData();
		  }
	  }
	  private void ClearActionPerformed(java.awt.event.ActionEvent evt) {
		    	  NameField.setText("");
				  EmailField.setText("");
				  PhoneField.setText("");
				  AddressField.setText("");
				  pack();
		 
	  }
	  
	  //NOT SHURE ABOUT THIS
	  private void Clear2ActionPerformed(java.awt.event.ActionEvent evt) {
		  TOBusiness business = CarShopController.getBusinessInfo();
		  if(business.getStartMap() != null) {
			  Mondaystart.setText("");
			  Tuesdaystart.setText("");
			  Thursdaystart.setText("");
			  Wednesdaystart.setText("");
			  Fridaystart.setText("");
			  Saturdaystart.setText("");
			  Sundaystart.setText("");
			  }
			  if(business.getEndMap() != null) {
			  Mondayend.setText("");
			  Tuesdayend.setText("");
			  Thursdayend.setText("");
			  Wednesdayend.setText("");
			  Fridayend.setText("");
			  Saturdayend.setText("");
			  Sundayend.setText("");
			  }
		pack();
 
}
	  
	  private void refreshData() {
		  errorMessage.setText(error);
		  
		  TOBusiness business = CarShopController.getBusinessInfo();
		  
		  CurrentNameString = business.getName();
		  CurrentAddressString = business.getAddress();
		  CurrentPhoneString = business.getPhone();
		  CurrentEmailString = business.getEmail();
		  CurrentNameStringLabel.setText(CurrentNameString);
		  CurrentAddressStringLabel.setText(CurrentAddressString);
		  CurrentPhoneStringLabel.setText(CurrentPhoneString);
		  CurrentEmailStringLabel.setText(CurrentEmailString);
		  
		  CurrentVacationString = business.getVacation();
		  CurrentHolidayString = business.getHoliday();
		  
		   CurrentVacation.setText(CurrentVacationString);
		   CurrentHoliday.setText(CurrentHolidayString);
		  

       	  HolidayStart.setText("Start Time");
          HolidayStartDate.setText("Start Date");
          HolidayEnd.setText("End Time");
          HolidayEndDate.setText("End Date");
          
          VacationStart.setText("Start Time");
          VacationStartDate.setText("Start Date");
          VacationEnd.setText("End Time");
          VacationEndDate.setText("End Date");
          
          
		  if(business.getStartMap() != null) {
		  if(!business.getStartMap().isEmpty()) {
			  if(business.getStartMap().containsKey("Monday")) {
			  Mondaystart.setText(business.getStartMap().get("Monday"));
			  }
			  if(business.getStartMap().containsKey("Tuesday")) {
			  Tuesdaystart.setText(business.getStartMap().get("Tuesday"));
			  }
			  if(business.getStartMap().containsKey("Wednesday")) {
			  Wednesdaystart.setText(business.getStartMap().get("Wednesday"));
			  }
			  if(business.getStartMap().containsKey("Thursday")) {
			  Thursdaystart.setText(business.getStartMap().get("Thursday"));
			  }
			  if(business.getStartMap().containsKey("Friday")) {
			  Fridaystart.setText(business.getStartMap().get("Friday"));
			  }
			  if(business.getStartMap().containsKey("Saturday")) {
			  Saturdaystart.setText(business.getStartMap().get("Saturday"));
			  }
			  if(business.getStartMap().containsKey("Sunday")) {
			  Sundaystart.setText(business.getStartMap().get("Sunday"));
			  }
		  }
		  }
		  if(business.getEndMap() != null) {
		  if(!business.getEndMap().isEmpty()) {
			  if(business.getEndMap().containsKey("Monday")) {
			  Mondayend.setText(business.getEndMap().get("Monday"));
			  }
			  if(business.getEndMap().containsKey("Tuesday")) {
			  Tuesdayend.setText(business.getEndMap().get("Tuesday"));
	  		  }
	  	      if(business.getEndMap().containsKey("Wednesday")) {
			  Wednesdayend.setText(business.getEndMap().get("Wednesday"));
			  }
			  if(business.getEndMap().containsKey("Thursday")) {
			  Thursdayend.setText(business.getEndMap().get("Thursday"));
			  }
			  if(business.getEndMap().containsKey("Friday")) {
			  Fridayend.setText(business.getEndMap().get("Friday"));
			  }
			  if(business.getEndMap().containsKey("Saturday")) {
			  Saturdayend.setText(business.getEndMap().get("Saturday"));
			  }
			  if(business.getEndMap().containsKey("Sunday")) {
			  Sundayend.setText(business.getEndMap().get("Sunday"));
			  }
		  }
		  }
		  
		  NameField.setText("");
		  EmailField.setText("");
		  PhoneField.setText("");
		  AddressField.setText("");
		  pack();
	  }
}
