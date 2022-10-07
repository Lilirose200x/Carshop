/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.carshop.model;
import java.sql.Time;
import java.sql.Date;
import java.io.Serializable;
import java.util.*;

// line 1 "../../../../../carShopStates.ump"
// line 81 "../../../../../carshopPersistence.ump"
// line 109 "../../../../../carshop.ump"
public class Appointment implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Appointment State Machines
  public enum Status { upcoming, inProgress, Done, Final }
  private Status status;

  //Appointment Associations
  private Customer customer;
  private BookableService bookableService;
  private List<ServiceBooking> serviceBookings;
  private CarShop carShop;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Appointment(Customer aCustomer, BookableService aBookableService, CarShop aCarShop)
  {
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer)
    {
      throw new RuntimeException("Unable to create appointment due to customer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddBookableService = setBookableService(aBookableService);
    if (!didAddBookableService)
    {
      throw new RuntimeException("Unable to create appointment due to bookableService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    serviceBookings = new ArrayList<ServiceBooking>();
    boolean didAddCarShop = setCarShop(aCarShop);
    if (!didAddCarShop)
    {
      throw new RuntimeException("Unable to create appointment due to carShop. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setStatus(Status.upcoming);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getStatusFullName()
  {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus()
  {
    return status;
  }

  public boolean cancel()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case upcoming:
        if (customerIsLoggedIn()&&moreThen24hours())
        {
          setStatus(Status.Done);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addOptionalService(ServiceBooking service)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case upcoming:
        if (isACombo()&&moreThen24hours())
        {
        // line 9 "../../../../../carShopStates.ump"
          doAddOptionalService(service);
          setStatus(Status.upcoming);
          wasEventProcessed = true;
          break;
        }
        break;
      case inProgress:
        if (isACombo())
        {
        // line 33 "../../../../../carShopStates.ump"
          doAddOptionalService(service);
          setStatus(Status.inProgress);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean removeOptionalService(ServiceBooking service)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case upcoming:
        if (isACombo()&&moreThen24hours())
        {
        // line 13 "../../../../../carShopStates.ump"
          doRemoveOptionalService(service);
          setStatus(Status.upcoming);
          wasEventProcessed = true;
          break;
        }
        break;
      case inProgress:
        if (isACombo())
        {
        // line 37 "../../../../../carShopStates.ump"
          doRemoveOptionalService(service);
          setStatus(Status.inProgress);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean changeDate(Date date)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case upcoming:
        if (moreThen24hours())
        {
        // line 17 "../../../../../carShopStates.ump"
          doChangeDate(date);
          setStatus(Status.upcoming);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean changeTime(List<Time> times)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case upcoming:
        if (moreThen24hours())
        {
        // line 21 "../../../../../carShopStates.ump"
          doChangeTime(times);
          setStatus(Status.upcoming);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean start()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case upcoming:
        if (ownerIsLoggedIn())
        {
          setStatus(Status.inProgress);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean noShow()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case upcoming:
        if (ownerIsLoggedIn())
        {
          setStatus(Status.Done);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean done()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case inProgress:
        if (ownerIsLoggedIn())
        {
          setStatus(Status.Done);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition8__()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Done:
        setStatus(Status.Final);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setStatus(Status aStatus)
  {
    status = aStatus;

    // entry actions and do activities
    switch(status)
    {
      case Done:
        // line 43 "../../../../../carShopStates.ump"
        deleteAppointment();
        __autotransition8__();
        break;
      case Final:
        delete();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_GetOne */
  public BookableService getBookableService()
  {
    return bookableService;
  }
  /* Code from template association_GetMany */
  public ServiceBooking getServiceBooking(int index)
  {
    ServiceBooking aServiceBooking = serviceBookings.get(index);
    return aServiceBooking;
  }

  public List<ServiceBooking> getServiceBookings()
  {
    List<ServiceBooking> newServiceBookings = Collections.unmodifiableList(serviceBookings);
    return newServiceBookings;
  }

  public int numberOfServiceBookings()
  {
    int number = serviceBookings.size();
    return number;
  }

  public boolean hasServiceBookings()
  {
    boolean has = serviceBookings.size() > 0;
    return has;
  }

  public int indexOfServiceBooking(ServiceBooking aServiceBooking)
  {
    int index = serviceBookings.indexOf(aServiceBooking);
    return index;
  }
  /* Code from template association_GetOne */
  public CarShop getCarShop()
  {
    return carShop;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCustomer(Customer aCustomer)
  {
    boolean wasSet = false;
    if (aCustomer == null)
    {
      return wasSet;
    }

    Customer existingCustomer = customer;
    customer = aCustomer;
    if (existingCustomer != null && !existingCustomer.equals(aCustomer))
    {
      existingCustomer.removeAppointment(this);
    }
    customer.addAppointment(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBookableService(BookableService aBookableService)
  {
    boolean wasSet = false;
    if (aBookableService == null)
    {
      return wasSet;
    }

    BookableService existingBookableService = bookableService;
    bookableService = aBookableService;
    if (existingBookableService != null && !existingBookableService.equals(aBookableService))
    {
      existingBookableService.removeAppointment(this);
    }
    bookableService.addAppointment(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfServiceBookings()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ServiceBooking addServiceBooking(Service aService, TimeSlot aTimeSlot)
  {
    return new ServiceBooking(aService, aTimeSlot, this);
  }

  public boolean addServiceBooking(ServiceBooking aServiceBooking)
  {
    boolean wasAdded = false;
    if (serviceBookings.contains(aServiceBooking)) { return false; }
    Appointment existingAppointment = aServiceBooking.getAppointment();
    boolean isNewAppointment = existingAppointment != null && !this.equals(existingAppointment);
    if (isNewAppointment)
    {
      aServiceBooking.setAppointment(this);
    }
    else
    {
      serviceBookings.add(aServiceBooking);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeServiceBooking(ServiceBooking aServiceBooking)
  {
    boolean wasRemoved = false;
    //Unable to remove aServiceBooking, as it must always have a appointment
    if (!this.equals(aServiceBooking.getAppointment()))
    {
      serviceBookings.remove(aServiceBooking);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addServiceBookingAt(ServiceBooking aServiceBooking, int index)
  {  
    boolean wasAdded = false;
    if(addServiceBooking(aServiceBooking))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfServiceBookings()) { index = numberOfServiceBookings() - 1; }
      serviceBookings.remove(aServiceBooking);
      serviceBookings.add(index, aServiceBooking);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveServiceBookingAt(ServiceBooking aServiceBooking, int index)
  {
    boolean wasAdded = false;
    if(serviceBookings.contains(aServiceBooking))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfServiceBookings()) { index = numberOfServiceBookings() - 1; }
      serviceBookings.remove(aServiceBooking);
      serviceBookings.add(index, aServiceBooking);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addServiceBookingAt(aServiceBooking, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCarShop(CarShop aCarShop)
  {
    boolean wasSet = false;
    if (aCarShop == null)
    {
      return wasSet;
    }

    CarShop existingCarShop = carShop;
    carShop = aCarShop;
    if (existingCarShop != null && !existingCarShop.equals(aCarShop))
    {
      existingCarShop.removeAppointment(this);
    }
    carShop.addAppointment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Customer placeholderCustomer = customer;
    this.customer = null;
    if(placeholderCustomer != null)
    {
      placeholderCustomer.removeAppointment(this);
    }
    BookableService placeholderBookableService = bookableService;
    this.bookableService = null;
    if(placeholderBookableService != null)
    {
      placeholderBookableService.removeAppointment(this);
    }
    while (serviceBookings.size() > 0)
    {
      ServiceBooking aServiceBooking = serviceBookings.get(serviceBookings.size() - 1);
      aServiceBooking.delete();
      serviceBookings.remove(aServiceBooking);
    }
    
    CarShop placeholderCarShop = carShop;
    this.carShop = null;
    if(placeholderCarShop != null)
    {
      placeholderCarShop.removeAppointment(this);
    }
  }

  // line 49 "../../../../../carShopStates.ump"
   private boolean ownerIsLoggedIn(){
    return getCarShop().getOwner().getIsLoggedIn();
  }

  // line 53 "../../../../../carShopStates.ump"
   private boolean customerIsLoggedIn(){
    return this.getCustomer().getIsLoggedIn();
  }

  // line 57 "../../../../../carShopStates.ump"
   private void doChangeDate(Date date){
    for(ServiceBooking s: serviceBookings){
  		s.getTimeSlot().setStartDate(date);
  		s.getTimeSlot().setEndDate(date);
  	}
  }

  // line 64 "../../../../../carShopStates.ump"
   private void doChangeTime(List<Time> times){
    int index = 0;
  	for(ServiceBooking s: serviceBookings){
  		s.getTimeSlot().setStartTime(times.get(index));
  		Time endTime = Time.valueOf("00:00:00");
  		int serviceDuration = s.getService().getDuration();
  		endTime.setHours((times.get(index).getHours()+(times.get(index).getMinutes()+serviceDuration)/60));
  		endTime.setMinutes((times.get(index).getMinutes()+serviceDuration)%60);
  		s.getTimeSlot().setEndTime(endTime);
  		index++;
  	}
  }

  // line 77 "../../../../../carShopStates.ump"
   private void doAddOptionalService(ServiceBooking service){
    this.addServiceBooking(service);
  }

  // line 81 "../../../../../carShopStates.ump"
   private void doRemoveOptionalService(ServiceBooking service){
    this.removeServiceBooking(service);
  }

  // line 85 "../../../../../carShopStates.ump"
   private boolean isACombo(){
    return (this.bookableService instanceof ServiceCombo);
  }

  // line 89 "../../../../../carShopStates.ump"
   private boolean moreThen24hours(){
    if(!(this.serviceBookings.get(0).getTimeSlot().getStartDate().after(carShop.getSystemDate()))) {
    	return false;
    }
    else {
    	if(this.serviceBookings.get(0).getTimeSlot().getStartDate().getMonth() == carShop.getSystemDate().getMonth()) {		//same month
    		if(this.serviceBookings.get(0).getTimeSlot().getStartDate().getDay() - carShop.getSystemDate().getDay() >= 2) { //day difference is greater or equal to 2
    			return true;
    		}
    		else if(this.serviceBookings.get(0).getTimeSlot().getStartDate().getDay() == carShop.getSystemDate().getDay()) {	//on same day
    			return false;
    		}
    		else {	//day difference is not greater then 2
    			if(this.serviceBookings.get(0).getTimeSlot().getStartTime().getMinutes() > carShop.getSystemTime().getMinutes()) {
    				return(this.serviceBookings.get(0).getTimeSlot().getStartTime().getHours() >= carShop.getSystemTime().getHours());
    			}
    			else
    				return(this.serviceBookings.get(0).getTimeSlot().getStartTime().getHours() > carShop.getSystemTime().getHours());
    		}
    	}
    	else {
    		int lastDayOfMonth;
    		switch(carShop.getSystemDate().getMonth()) {
    		case(1): lastDayOfMonth = 30;
    		case(2):
    			if(carShop.getSystemDate().getYear() % 4 == 0) {
    				lastDayOfMonth = 29;
    			}
    			else
    				lastDayOfMonth = 28;
    		case(3): lastDayOfMonth = 31;
    		case(4): lastDayOfMonth = 30;
    		case(5): lastDayOfMonth = 31;
    		case(6): lastDayOfMonth = 30;
    		case(7): lastDayOfMonth = 31;
    		case(8): lastDayOfMonth = 31;
    		case(9): lastDayOfMonth = 30;
    		case(10): lastDayOfMonth = 31;
    		case(11): lastDayOfMonth = 30;
    		case(12): lastDayOfMonth = 31;
    		default: lastDayOfMonth = 0;
    		}
    		
    		if(carShop.getSystemDate().getDay() == lastDayOfMonth && this.serviceBookings.get(0).getTimeSlot().getStartDate().getDay() == 1) {
    			if(this.serviceBookings.get(0).getTimeSlot().getStartTime().getMinutes() > carShop.getSystemDate().getMinutes()) {
    				return(this.serviceBookings.get(0).getTimeSlot().getStartTime().getHours() - carShop.getSystemTime().getHours() > 0);
    			}
    			else
    				return(this.serviceBookings.get(0).getTimeSlot().getStartTime().getHours() - carShop.getSystemTime().getHours() >= 0);
    		}
    		else
    			return true;
    	}
    }
  }

  // line 148 "../../../../../carShopStates.ump"
   private void deleteAppointment(){
    delete();
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 84 "../../../../../carshopPersistence.ump"
  private static final long serialVersionUID = -787794828861295940L ;

  
}