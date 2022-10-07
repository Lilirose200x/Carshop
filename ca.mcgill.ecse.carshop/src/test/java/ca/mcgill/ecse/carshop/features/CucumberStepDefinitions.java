
package ca.mcgill.ecse.carshop.features;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse.carshop.application.CarShopApplication;
import ca.mcgill.ecse.carshop.controller.CarShopController;
import ca.mcgill.ecse.carshop.model.*;
import ca.mcgill.ecse.carshop.model.BusinessHour.DayOfWeek;
import ca.mcgill.ecse.carshop.model.Technician.TechnicianType;
import ca.mcgill.ecse.carshop.persistence.CarShopPersistence;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {

	private CarShop carshop;
	private String error;
	private int errorCntr;
	private User currentUser;
	private String currentUserUsername;
	private String currentUserPassword;
	private Appointment currentAppointment;
	private int numberOfAppointment = 0;
	private Business business;
	private boolean exception;
	private static String filename = "data.carshop";

	@Before
	public static void setUp() {
		CarShopPersistence.setFilename(filename);
		// remove test file
		File f = new File(filename);
		f.delete();
		// clear all data
		CarShopApplication.getCarShop().delete();
	}

	// SignUpCustomerAccount and UpdateAccount
	// Author: Charles-Antoine Pare
	@Given("an owner account exists in the system with username {string} and password {string}")
	public void createOwner(String username, String password) {
		carshop.setOwner(new Owner(username, password, false, carshop));
	}

	@Given("the following customers exist in the system:")
	public void theFollowingCustomersExist(List<Map<String, String>> list) {
		for (Map<String, String> map : list) {
			carshop.addCustomer(map.get("username"), map.get("password"), false);
		}
	}

	@Given("the following technicians exist in the system:")
	public void theFollowingTechniciansExist(List<Map<String, String>> list) {
		for (Map<String, String> map : list) {
			TechnicianType type = TechnicianType.valueOf(map.get("type"));
			carshop.addTechnician(map.get("username"), map.get("password"), false, type);
		}
	}

	@Given("each technician has their own garage")
	public void eachTechnicianHasTheirOwnGarage() {
		for (Technician technician : carshop.getTechnicians()) {
			carshop.addGarage(technician);
		}
	}

	@Given("there is no existing username {string}")
	public void thereIsNoExistingUsername(String username) {
		for (Customer customer : carshop.getCustomers()) {
			if (customer.getUsername().equals(username)) {
				customer.delete();
			}
		}
	}

	@Given("there is an existing username {string}")
	public void thereIsAnExistingUsername(String username) {
		boolean exists = false;
		if (username.equals("owner")) {
			if (carshop.getOwner() == null) {
				carshop.setOwner(new Owner(username, username, false, carshop));
			}
		} else if (username.contains("Technician")) {
			for (Technician technician : carshop.getTechnicians()) {
				if (technician.getUsername().equals(username)) {
					exists = true;
				}
			}
			if (!exists) {
				String type = username.replace("-Technician", "");
				TechnicianType type2 = TechnicianType.valueOf(type);

				carshop.addTechnician(username, username, false, type2);
			}
		} else {
			for (Customer customer : carshop.getCustomers()) {
				if (customer.getUsername().equals(username)) {
					exists = true;
				}
			}
			if (!exists) {
				carshop.addCustomer(username, username, false);
			}
		}
	}

	@When("the user provides a new username {string} and a password {string}")
	public void UserProvidesNewUserAndPass(String username, String password) {
		try {
			CarShopController.createCustomer(username, password);
		} catch (Exception e) {
			error = e.getMessage();
			errorCntr++;
		}
	}

	@When("the user tries to update account with a new username {string} and password {string}")
	public void userTriesUpdateAccountNewUserAndPass(String username, String password) {
		try {
			CarShopController.updateAccount(username, password);
		} catch (Exception e) {
			error = e.getMessage();
			errorCntr++;
		}
	}

	@Then("a new customer account shall be created")
	public void newAccountCreated() {
		assertEquals(0, errorCntr);
	}

	@Then("no new account shall be created")
	public void noNewAccountCreated() {
		assertEquals(1, errorCntr);
	}

	@Then("the account shall have username {string} and password {string}")
	public void AccountShallHaveUsernameAndPass(String username, String password) {
		for (Customer customer : carshop.getCustomers()) {
			if (customer.getUsername().equals(username)) {
				assertEquals(password, customer.getPassword());
			}
		}
		for (Technician technician : carshop.getTechnicians()) {
			if (technician.getUsername().equals(username)) {
				assertEquals(password, technician.getPassword());
			}
		}
		if (username.equals("owner"))
			assertEquals(password, carshop.getOwner().getPassword());
	}

	@Then("the account shall not be updated")
	public void accountShallNotBeUpdated() {
		assertEquals(1, errorCntr);
	}

	@Then("an error message {string} shall be raised")
	public void errorMessageRaised(String errorMsg) {
		assertTrue(error.contains(errorMsg));
	}

	// General step and background
	// Author: Emile Riberdy
	@Given("a Carshop system exists")
	public void thereIsACarShopSystem() {
		carshop = CarShopApplication.getCarShop();
		error = " ";
		errorCntr = 0;
	}

	@Given("an owner account exists in the system")
	public void anOwnerExists() {
		carshop.setOwner(new Owner("owner", "owner", false, carshop));
	}

	@Given("a business exists in the system")
	public void thereIsABusiness() {
		carshop.setBusiness(
				new Business("BusinessName", "BusinessAddress", "111-111-1111", "business@email.com", carshop));
	}

	@Then("an error message with content {string} shall be raised")
	public void errorRaised(String errorMsg) {
		assertTrue(error.contains(errorMsg));
	}

	// AddService begin
	// Author: Emile Riberdy
	@Given("the following services exist in the system:")
	public void givenServicesExist(List<Map<String, String>> table) {
		try {
			ArrayList<String> garages = new ArrayList<>();
			for (Garage g : carshop.getGarages()) {
				garages.add(g.getTechnician().getType().toString());
			}

			for (int i = 0; i < table.size(); i++) {
				String serviceName = table.get(i).get("name");
				int serviceDuration = Integer.parseInt(table.get(i).get("duration"));

				int index = garages.indexOf(table.get(i).get("garage"));
				Garage serviceGarage = carshop.getGarages().get(index);

				carshop.addBookableService(new Service(serviceName, carshop, serviceDuration, serviceGarage));
			}
		} catch (Exception e) {
			error = e.getMessage();
			errorCntr++;
		}
	}

	@Given("the Owner with username {string} is logged in")
	public void ownerWithUsernameIsLoggedIn(String username) {
		userLoggedInToAnAccount(username);
	}

	@When("{string} initiates the addition of the service {string} with duration {string} belonging to the garage of {string} technician")
	public void whenAServiceIsCreated(String userName, String service, String duration, String garage) {
		try {
			ArrayList<String> garages = new ArrayList<>();
			for (Garage g : carshop.getGarages()) {
				garages.add(g.getTechnician().getType().toString());
			}

			int index = garages.indexOf(garage);

			Garage gar = carshop.getGarage(index);
			CarShopController.createService(service, gar, Integer.parseInt(duration));
		} catch (Exception e) {
			error = e.getMessage();
			errorCntr++;
		}
	}

	@Then("the service {string} shall belong to the garage of {string} technician")
	public void serviceBelongsToTechnician(String service, String technician) {
		try {
			ArrayList<String> services = new ArrayList<>();
			for (BookableService s : carshop.getBookableServices()) {
				services.add(s.getName());
			}
			int index = services.indexOf(service);

			assertEquals(((Service) carshop.getBookableService(index)).getGarage().getTechnician().getType().toString(),
					technician);
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	@Then("the number of services in the system shall be {string}")
	public void numberOfServicesShallBe(String number) {
		assertEquals(carshop.getBookableServices().size(), Integer.parseInt(number));
	}

	@Then("the service {string} shall not exist in the system")
	public void theServiceDoNotExist(String service) {
		try {
			ArrayList<String> services = new ArrayList<>();
			for (BookableService s : carshop.getBookableServices()) {
				services.add(s.getName());
			}
			assertTrue(!services.contains(service));
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	@Then("the service {string} shall still preserve the following properties:")
	public void serviceShallHaveProperties(String service, List<Map<String, String>> table) {

		String[] expected = new String[3];
		expected[0] = table.get(0).get("name");
		expected[1] = table.get(0).get("duration");
		expected[2] = table.get(0).get("garage");

		ArrayList<String> services = new ArrayList<>();
		for (BookableService s : carshop.getBookableServices()) {
			services.add(s.getName());
		}

		int index = services.indexOf(service);

		Service target = ((Service) carshop.getBookableService(index));

		String[] actual = new String[3];
		actual[0] = target.getName();
		actual[1] = "" + target.getDuration();
		actual[2] = target.getGarage().getTechnician().getType().toString();

		assertArrayEquals(expected, actual);
	}

	// Add service end

	// Update service begins
	// Author: Emile Riberdy
	@When("{string} initiates the update of the service {string} to name {string}, duration {string}, belonging to the garage of {string} technician")
	public void whenUpdateService(String user, String serviceOld, String serviceNew, String duration, String garage) {
		try {
			CarShopController.updateServiceName(serviceOld, serviceNew);
			CarShopController.updateServiceDuration(serviceNew, Integer.parseInt(duration));

			ArrayList<String> garageString = new ArrayList<>();
			for (Garage g : carshop.getGarages()) {
				garageString.add(g.getTechnician().getType().toString());
			}
			int index = garageString.indexOf(garage);
			Garage target = carshop.getGarage(index);

			CarShopController.updateServiceGarage(serviceNew, target);

		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	@Then("the service {string} shall be updated to name {string}, duration {string}")
	public void serviceShallBeUpdated(String oldName, String newName, String duration) {
		boolean isUpdated = false;

		for (BookableService s : carshop.getBookableServices()) {
			if (s.getName().equals(oldName) && !oldName.equals(newName)) {
				fail();
			}
			if (s.getName().equals(newName)) {
				if (((Service) s).getDuration() == Integer.parseInt(duration)) {
					isUpdated = true;
				}
			}
		}

		assertTrue(isUpdated);
	}

	@After
	public void tearDown() {
		error = "";
		errorCntr = 0;
		currentUser = null;
		currentAppointment = null;
		;
		business = null;
		exception = false;
		carshop.delete();
		currentUserUsername = null;
		currentUserPassword = null;
		numberOfAppointment = 0;

	}

	@Then("the service {string} shall exist in the system")
	public void theServiceExist(String service) {
		try {
			ArrayList<String> services = new ArrayList<>();
			for (BookableService s : carshop.getBookableServices()) {
				services.add(s.getName());
			}
			assertTrue(services.contains(service));
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	@Given("the user with username {string} is logged in")
	public void userLoggedInToAnAccount(String username) {
		if (username.equals("owner")) {
			if (carshop.getOwner() != null) {
				carshop.getOwner().setIsLoggedIn(true);
				currentUser = carshop.getOwner();
			}

		} else if (username.contains("-Technician")) {
			for (Technician technician : carshop.getTechnicians()) {
				if (username.equals(technician.getUsername())) {
					technician.setIsLoggedIn(true);
					currentUser = technician;
				}
			}
		} else {
			for (Customer customer : carshop.getCustomers()) {
				if (username.equals(customer.getUsername())) {
					customer.setIsLoggedIn(true);
					currentUser = customer;
				}
			}
		}
	}

	// UpdateGarage OpeningHours begin
	// Author: David Breton

	@Given("a business exists with the following information:")
	public void thereIsABusinessWithThisInformation(List<Map<String, String>> map) {
		Business business = new Business(map.get(0).get("name"), map.get(0).get("address"),
				map.get(0).get("phone number"), map.get(0).get("email"), carshop);
	}

	@Given("the user is logged in to an account with username {string}")
	public void userLoggedInToAnAccountWithUsername(String username) {
		if (username.equals("owner")) {
			if (carshop.getOwner() != null) {
				carshop.getOwner().setIsLoggedIn(true);
			}

		} else if (username.contains("Technician")) {
			for (Technician technician : carshop.getTechnicians()) {
				if (username.equals(technician.getUsername())) {
					technician.setIsLoggedIn(true);
				}
			}
		} else {
			for (Customer customer : carshop.getCustomers()) {
				if (username.equals(customer.getUsername())) {
					customer.setIsLoggedIn(true);
				}
			}
		}
	}

	@When("the user tries to add new business hours on {string} from {string} to {string} to garage belonging to the technician with type {string}")
	public void addNewBusinessHourToGarage(String day, String startTime, String endTime, String type) {

		try {
			CarShopController.addNewGarageOpeningHours(day, startTime, endTime, type);
		} catch (Exception e) {
			error = e.getMessage();
			errorCntr++;
			System.out.println(error);
		}
	}

	@Then("the garage belonging to the technician with type {string} should have opening hours on {string} from {string} to {string}")
	public void checkForValidityOfOpeningHoursInGarage(String type, String day, String startTime, String endTime) {
		boolean success = false;
		for (Technician tec : carshop.getTechnicians()) {
			if (tec.getType().toString().equals(type)) {
				for (BusinessHour businessHour : tec.getGarage().getBusinessHours()) {
					if (businessHour.getDayOfWeek().toString().equals(day)) {
						if (businessHour.getEndTime().toString().equals(endTime + ":00")
								&& businessHour.getStartTime().toString().equals(startTime + ":00")) {
							success = true;
						}
					}
				}
			}
		}
		assertTrue(success);
	}

	@Given("there are opening hours on {string} from {string} to {string} for garage belonging to the technician with type {string}")
	public void ThereAreOpeningHoursToGarage(String day, String startTimeString, String endTimeString, String type) {

		Time startTime = Time.valueOf(startTimeString + ":00");
		Time endTime = Time.valueOf(endTimeString + ":00");

		DayOfWeek dayOfWeek = null;
		switch (day) {
		case "Monday":
			dayOfWeek = DayOfWeek.Monday;
			break;
		case "Tuesday":
			dayOfWeek = DayOfWeek.Tuesday;
			break;
		case "Wednesday":
			dayOfWeek = DayOfWeek.Wednesday;
			break;
		case "Thursday":
			dayOfWeek = DayOfWeek.Thursday;
			break;
		case "Friday":
			dayOfWeek = DayOfWeek.Friday;
			break;
		case "Saturday":
			dayOfWeek = DayOfWeek.Saturday;
			break;
		case "Sunday":
			dayOfWeek = DayOfWeek.Sunday;
			break;
		}

		BusinessHour businessHour = new BusinessHour(dayOfWeek, startTime, endTime, carshop);

		for (Technician tec : carshop.getTechnicians()) {
			if (tec.getType().toString().equals(type)) {
				tec.getGarage().addBusinessHour(businessHour);
			}
		}
	}

	@When("the user tries to remove opening hours on {string} from {string} to {string} to garage belonging to the technician with type {string}")
	public void removeBusinessHourToGarage(String day, String startTime, String endTime, String type) {
		try {
			CarShopController.removeGarageOpeningHours(day, startTime, endTime, type);
		} catch (Exception e) {
			error = e.getMessage();
			errorCntr++;
		}
	}

	@Then("the garage belonging to the technician with type {string} should not have opening hours on {string} from {string} to {string}")
	public void checkForValidityOfOpeningHoursInGarageNot(String type, String day, String startTime, String endTime) {
		boolean success = true;
		for (Technician tec : carshop.getTechnicians()) {
			if (tec.getType().toString().equals(type)) {
				for (BusinessHour businessHour : tec.getGarage().getBusinessHours()) {
					if (businessHour.getDayOfWeek().toString().equals(day)
							&& businessHour.getEndTime().toString().equals(startTime + ":00")
							&& businessHour.getStartTime().toString().equals(endTime + ":00")) {
						success = false;
					}
				}
			}
		}
		assertTrue(success);
	}

	// @Author cpare
	@Given("the business has the following opening hours")
	public void thereIsABusinessWithTheFollowingHours(List<Map<String, String>> map) {
		for (Map<String, String> list : map) {
			carshop.getBusiness().addBusinessHour(new BusinessHour(DayOfWeek.valueOf(list.get("day")),
					Time.valueOf(list.get("startTime") + ":00"), Time.valueOf(list.get("endTime") + ":00"), carshop));
		}
	}

	@Given("the business has the following opening hours:")
	public void thereIsABusinessWithTheFollowingHours2(List<Map<String, String>> map) {
		for (Map<String, String> list : map) {
			carshop.getBusiness().addBusinessHour(new BusinessHour(DayOfWeek.valueOf(list.get("day")),
					Time.valueOf(list.get("startTime") + ":00"), Time.valueOf(list.get("endTime") + ":00"), carshop));
		}
	}

	@Given("the business has the following holidays")
	public void businessHolidays(List<Map<String, String>> list) {
		for (Map<String, String> mapu : list) {
			Date startDate = Date.valueOf(mapu.get("startDate"));
			Date endDate = Date.valueOf(mapu.get("endDate"));
			Time startTime = Time.valueOf(mapu.get("startTime") + ":00");
			Time endTime = Time.valueOf(mapu.get("endTime") + ":00");
			TimeSlot timeSlot = new TimeSlot(startDate, startTime, endDate, endTime, carshop);
			carshop.getBusiness().addHoliday(timeSlot);
		}
	}

	@Given("a {string} time slot exists with start time {string} at {string} and end time {string} at {string}")
	public void timeSlotExistsWith(String slot, String startDate, String startTime, String endDate, String endTime) {
		TimeSlot ts = new TimeSlot(Date.valueOf(startDate), Time.valueOf(startTime + ":00"), Date.valueOf(endDate),
				Time.valueOf(endTime + ":00"), carshop);
		if (slot.equals("holiday")) {
			carshop.getBusiness().addHoliday(ts);
		}
		if (slot.equals("vacation")) {
			carshop.getBusiness().addVacation(ts);
		}
	}

	@Given("{string} has 0 no-show records")
	public void noShowRecords(String customer) {
		for (Customer cus : carshop.getCustomers()) {
			if (cus.getUsername().equals(customer)) {
				cus.setNoShow(0);
			}
		}
	}

//
//	@Given("the following services exist in the system:")
//	public void followingServicesExist(List<Map<String, String>> list) {
//		for (Map<String, String> map : list) {
//			carshop.addBookableService(new Service(map.get("name"), carshop, Integer.parseInt(map.get("duration")),
//					new Garage(carshop, (Technician) Technician.getWithUsername(map.get("garage") + "-Technician"))));
//		}
//	}
//
	@Given("the following service combos exist in the system:")
	public void followingServiceCombosExist(List<Map<String, String>> list) {
		for (Map<String, String> map : list) {
			ServiceCombo sc = new ServiceCombo(map.get("name"), carshop);
			String[] services = map.get("services").split(",");
			String[] mandatory = map.get("mandatory").split(",");
			for (int i = 0; i < services.length; i++) {
				ComboItem ci = null;
				for (BookableService bs : carshop.getBookableServices()) {
					if (bs.getName().equals(services[i])) {
						ci = new ComboItem(Boolean.parseBoolean(mandatory[i]), (Service) bs, sc);
					}
				}
				if (services[i].equals(map.get("mainService"))) {
					sc.setMainService(ci);
				} else {
					sc.addService(ci);
				}
			}

		}
	}

	@When("{string} makes a {string} appointment for the date {string} and time {string} at {string}")
	public void makesAppointmentForDateAndTime(String user, String serviceName, String date, String time,
			String currentDateAndTime) {
		for (Customer cus : carshop.getCustomers()) {
			if (cus.getUsername().equals(user)) {
				cus.setIsLoggedIn(true);
			}
		}
		try {
			carshop.setSystemDate(Date.valueOf(currentDateAndTime.substring(0, 10)));
			carshop.setSystemTime(Time.valueOf(currentDateAndTime.substring(11) + ":00"));
			currentAppointment = CarShopController.makeServiceAppointment(user, date, serviceName, time,
					carshop.getSystemTime(), carshop.getSystemDate());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			error += e.getMessage();
			errorCntr++;
		}
	}

	@When("{string} attempts to change the service in the appointment to {string} at {string}")
	public void attemptsToChangeTheService(String user, String newService, String currentDateAndTime) {
		carshop.setSystemDate(Date.valueOf(currentDateAndTime.substring(0, 10)));
		carshop.setSystemTime(Time.valueOf(currentDateAndTime.substring(11) + ":00"));
		try {
			if (user.equals(currentAppointment.getCustomer().getUsername())) {
				currentAppointment = CarShopController.changeAppointmentService(currentAppointment, newService);
			}
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	@When("{string} attempts to update the date to {string} and time to {string} at {string}")
	public void attemptsToUpdateDateAndTime(String user, String date, String time, String currentDateAndTime) {
		updateDateAndTime(user, date, time, currentDateAndTime);

	}

	@When("{string} attempts to cancel the appointment at {string}")
	public void attemptsToCancelAppointment(String user, String currentDateAndTime) {
		carshop.setSystemDate(Date.valueOf(currentDateAndTime.substring(0, 10)));
		carshop.setSystemTime(Time.valueOf(currentDateAndTime.substring(11) + ":00"));
		errorCntr++;
		if (currentAppointment.getCustomer().getUsername().equals(user)) {
			if (CarShopController.cancelAppointment(currentAppointment.getBookableService().getName(), currentAppointment.getServiceBooking(0).getTimeSlot().getStartDate().toString(), currentAppointment.getServiceBooking(0).getTimeSlot().getStartTime().toString())) {
				errorCntr--;
			}
		}
	}

	@When("{string} attempts to add the optional service {string} to the service combo with start time {string} in the appointment at {string}")
	public void addOptionalServiceAtTime(String user, String service, String time, String currentDateAndTime) {
		carshop.setSystemDate(Date.valueOf(currentDateAndTime.substring(0, 10)));
		carshop.setSystemTime(Time.valueOf(currentDateAndTime.substring(11) + ":00"));
		errorCntr++;
		if (currentAppointment.getCustomer().getUsername().equals(user)) {
			try {
				if (CarShopController.addOptionalServiceToAppointment(currentAppointment, service, time)) {
					errorCntr--;
				}
			} catch (Exception e) {
				error = e.getMessage();
			}

		}
	}

	@When("{string} attempts to update the date to {string} and start time to {string} at {string}")
	public void updateDateAndTime(String user, String date, String startTimes, String currentDateAndTime) {
		carshop.setSystemDate(Date.valueOf(currentDateAndTime.substring(0, 10)));
		carshop.setSystemTime(Time.valueOf(currentDateAndTime.substring(11) + ":00"));

		if (currentAppointment.getCustomer().getUsername().equals(user)) {

			CarShopController.changeAppointmentDateAndTime(currentAppointment, date, startTimes);

		}
	}

	@When("{string} makes a {string} appointment with service {string} for the date {string} and start time {string} at {string}")
	public void makesAppointmentWithService(String user, String name, String service, String date, String startTimes,
			String currentDateAndTime) {
		for (Customer cus : carshop.getCustomers()) {
			if (cus.getUsername().equals(user)) {
				cus.setIsLoggedIn(true);
			}
		}
		carshop.setSystemDate(Date.valueOf(currentDateAndTime.substring(0, 10)));
		carshop.setSystemTime(Time.valueOf(currentDateAndTime.substring(11) + ":00"));
		currentAppointment = CarShopController.makeServiceComboAppointment(user, date, name, service, startTimes,
				carshop.getSystemTime(), carshop.getSystemDate());
	}

	@When("the owner starts the appointment at {string}")
	public void ownerStartsAppointmentAt(String currentDateAndTime) {
		carshop.getOwner().setIsLoggedIn(true);
		carshop.setSystemDate(Date.valueOf(currentDateAndTime.substring(0, 10)));
		carshop.setSystemTime(Time.valueOf(currentDateAndTime.substring(11) + ":00"));
		CarShopController.startAppointment(currentAppointment.getBookableService().getName(), currentAppointment.getServiceBooking(0).getTimeSlot().getStartDate().toString(), currentAppointment.getServiceBooking(0).getTimeSlot().getStartTime().toString());
	}

	@When("the owner ends the appointment at {string}")
	public void ownerEndsAppointmentAt(String currentDateAndTime) {
		carshop.getOwner().setIsLoggedIn(true);
		carshop.setSystemDate(Date.valueOf(currentDateAndTime.substring(0, 10)));
		carshop.setSystemTime(Time.valueOf(currentDateAndTime.substring(11) + ":00"));
		CarShopController.appointmentDone(currentAppointment.getBookableService().getName(), currentAppointment.getServiceBooking(0).getTimeSlot().getStartDate().toString(), currentAppointment.getServiceBooking(0).getTimeSlot().getStartTime().toString());
	}

	@When("the owner attempts to register a no-show for the appointment at {string}")
	public void ownerRegistersNoShow(String currentDateAndTime) {
		carshop.getOwner().setIsLoggedIn(true);
		carshop.setSystemDate(Date.valueOf(currentDateAndTime.substring(0, 10)));
		carshop.setSystemTime(Time.valueOf(currentDateAndTime.substring(11) + ":00"));
		CarShopController.noShow(currentAppointment.getBookableService().getName(), currentAppointment.getServiceBooking(0).getTimeSlot().getStartDate().toString(), currentAppointment.getServiceBooking(0).getTimeSlot().getStartTime().toString());
	}

	@When("the owner attempts to end the appointment at {string}")
	public void ownerAttemptsToEndAppointmentAt(String currentDateAndTime) {
		carshop.getOwner().setIsLoggedIn(true);
		carshop.setSystemDate(Date.valueOf(currentDateAndTime.substring(0, 10)));
		carshop.setSystemTime(Time.valueOf(currentDateAndTime.substring(11) + ":00"));
		CarShopController.appointmentDone(currentAppointment.getBookableService().getName(), currentAppointment.getServiceBooking(0).getTimeSlot().getStartDate().toString(), currentAppointment.getServiceBooking(0).getTimeSlot().getStartTime().toString());
	}

	@Then("the service in the appointment shall be {string}")
	public void serviceInAppointmentShall(String service) {
		assertTrue(service.equals(currentAppointment.getBookableService().getName()));
	}

	@Then("the appointment shall be for the date {string} with start time {string} and end time {string}")
	public void appointmentShallBeFor(String date, String startTime, String endTime) {
		boolean found = false;
		String[] start = startTime.split(",");
		String[] end = endTime.split(",");
		int i = 0;
		for (ServiceBooking sb : currentAppointment.getServiceBookings()) {
			if (sb.getTimeSlot().getStartDate().equals(Date.valueOf(date))
					&& sb.getTimeSlot().getEndDate().equals(Date.valueOf(date))) {
				if (sb.getTimeSlot().getStartTime().equals(Time.valueOf(start[i] + ":00"))) {
					if (sb.getTimeSlot().getEndTime().equals(Time.valueOf(end[i] + ":00"))) {
						found = true;
					}
				}
			}
			i++;
		}
		assertTrue(found);
	}

	@Then("the username associated with the appointment shall be {string}")
	public void usernameAssociatedWithAppointment(String customer) {
		assertTrue(customer.equals(currentAppointment.getCustomer().getUsername()));
	}

//	@Then("the user {string} shall have 0 no-show records")
//	public void userShallHaveNoShow(String customer) {
//		for (Customer cus : carshop.getCustomers()) {
//			if (cus.getUsername().equals(customer)) {
//				assertEquals(0, cus.getNoShow());
//			}
//		}
//	}

	@Then("the user {string} shall have {int} no-show records")
	public void userShallHaveNoShow(String customer, int noShow) {
		for (Customer cus : carshop.getCustomers()) {
			if (cus.getUsername().equals(customer)) {
				assertEquals(noShow, cus.getNoShow());
			}
		}
	}

	@Then("the system shall have 2 appointments")
	public void systemShallHaveAppointments() {
		assertEquals(2, carshop.getAppointments().size());
	}

	@Then("the system shall have {int} appointment")
	public void systemShallHaveAppointments2(int num) {
		assertEquals(num, carshop.getAppointments().size());
	}

	@Then("the service combo in the appointment shall be {string}")
	public void serviceComboInAppointment(String service) {
		assertTrue(currentAppointment.getBookableService().getName().equals(service));
	}

	@Then("the service combo shall have {string} selected services")
	public void serviceComboShallHaveServices(String services) {
		String[] list = services.split(",");
		ServiceCombo sc = (ServiceCombo) currentAppointment.getBookableService();
		int count = 0;
		for (ComboItem ci : sc.getServices()) {
			for (String s : list) {
				if (s.equals(ci.getService().getName())) {
					count++;
				}
			}
		}
		assertEquals(list.length, count);
	}

//	@Then("the appointment shall be for the date {string} with start time {string} and end time {string}")
//	public void appointmentShallbe(String datee, String startTimes, String endTimes) {
//		int count = 0;
//		Date date = Date.valueOf(datee);
//		String[] start = startTimes.split(",");
//		String[] end = endTimes.split(",");
//		Time[] startTime = new Time[start.length];
//		Time[] endTime = new Time[end.length];
//		for (int i = 0; i < startTime.length; i++) {
//			startTime[i] = Time.valueOf(start[i] + ":00");
//			endTime[i] = Time.valueOf(end[i] + ":00");
//		}
//		for (ServiceBooking sb : currentAppointment.getServiceBookings()) {
//			if (sb.getTimeSlot().getStartDate().equals(date) && sb.getTimeSlot().getEndDate().equals(date)) {
//				for (int i = 0; i < startTime.length; i++) {
//					if (startTime[i].equals(sb.getTimeSlot().getStartTime())) {
//						if (endTime[i].equals(sb.getTimeSlot().getEndTime())) {
//							count++;
//						}
//					}
//
//				}
//			}
//		}
//		assertEquals(start.length, count);
//	}

	@Then("the appointment shall be in progress")
	public void appointmentShallBeInProgress() {
		assertTrue(currentAppointment.getStatusFullName().equals("inProgress"));
	}

	@Then("the appointment shall be booked")
	public void appointmentShallBeBooked() {
		boolean check = false;
		for (Appointment a : carshop.getAppointments()) {
			if (a.equals(currentAppointment)) {
				check = true;
				break;
			}
		}
		assertTrue(check);
	}

	// UpdateGarage OpeningHours end

	// Author: David Breton
	// LogIn

	@When("the user tries to log in with username {string} and password {string}")
	public void attemptToLogIn(String username, String password) {
		try {
			currentUserUsername = username;
			currentUserPassword = password;
			CarShopController.LogIn(username, password);
		} catch (Exception e) {
			error = e.getMessage();
			errorCntr++;
		}
	}

	@Then("the user should be successfully logged in")
	public void isSuccessfullyLogIn() {

		boolean isLoggedIn = false;
		for (User user : carshop.getCustomers()) {
			if (user.getPassword().equals(currentUserPassword) && user.getUsername().equals(currentUserUsername)
					&& user.getIsLoggedIn()) {
				isLoggedIn = true;
			}
		}
		for (User user : carshop.getTechnicians()) {
			if (user.getPassword().equals(currentUserPassword) && user.getUsername().equals(currentUserUsername)
					&& user.getIsLoggedIn()) {
				isLoggedIn = true;
			}
		}
		if (carshop.getOwner() != null) {
			User user = (User) carshop.getOwner();
			if (user.getPassword().equals(currentUserPassword) && user.getUsername().equals(currentUserUsername)
					&& user.getIsLoggedIn()) {
				isLoggedIn = true;
			}
		}
		assertTrue(isLoggedIn);
	}

	@Then("the user shall be successfully logged in")
	public void isSuccessfullyLogIn2() {

		boolean isLoggedIn = false;
		for (User user : carshop.getCustomers()) {
			if (user.getPassword().equals(currentUserPassword) && user.getUsername().equals(currentUserUsername)
					&& user.getIsLoggedIn()) {
				isLoggedIn = true;
			}
		}
		for (User user : carshop.getTechnicians()) {
			if (user.getPassword().equals(currentUserPassword) && user.getUsername().equals(currentUserUsername)
					&& user.getIsLoggedIn()) {
				isLoggedIn = true;
			}
		}
		if (carshop.getOwner() != null) {
			User user = (User) carshop.getOwner();
			if (user.getPassword().equals(currentUserPassword) && user.getUsername().equals(currentUserUsername)
					&& user.getIsLoggedIn()) {
				isLoggedIn = true;
			}
		}
		assertTrue(isLoggedIn);
	}

	@Then("the user should not be logged in")
	public void isNotSuccessfullyLogIn() {
		boolean isLoggedIn = false;
		for (User user : carshop.getCustomers()) {
			if (user.getPassword().equals(currentUserPassword) && user.getUsername().equals(currentUserUsername)
					&& user.getIsLoggedIn()) {
				isLoggedIn = true;
			}
		}
		for (User user : carshop.getTechnicians()) {
			if (user.getPassword().equals(currentUserPassword) && user.getUsername().equals(currentUserUsername)
					&& user.getIsLoggedIn()) {
				isLoggedIn = true;
			}
		}
		if (carshop.getOwner() != null) {
			User user = (User) carshop.getOwner();
			if (user.getPassword().equals(currentUserPassword) && user.getUsername().equals(currentUserUsername)
					&& user.getIsLoggedIn()) {
				isLoggedIn = true;
			}
		}
		assertTrue(!isLoggedIn);
	}

	@Then("a new account shall be created")
	public void checkIfAccountCreated() {
		boolean accountExist = false;
		List<Technician> listOfTechnicians = carshop.getTechnicians();
		List<Customer> listOfCustomers = carshop.getCustomers();
		Owner owner = carshop.getOwner();

		for (Technician tech : listOfTechnicians) {
			if (tech.getPassword().equals(currentUserPassword) && tech.getUsername().equals(currentUserUsername)) {
				accountExist = true;
			}
		}
		for (Customer cust : listOfCustomers) {
			if (cust.getPassword().equals(currentUserPassword) && cust.getUsername().equals(currentUserUsername)) {
				accountExist = true;
			}
		}
		if (owner != null) {
			if (owner.getPassword().equals(currentUserPassword) && owner.getUsername().equals(currentUserUsername)) {
				accountExist = true;
			}
		}
		assertTrue(accountExist);
	}

	@Then("the account shall have username {string}, password {string} and technician type {string}")
	public void technicianAccountValidity(String username, String password, String type) {
		boolean TechAccountExist = false;
		List<Technician> listOfTechnicians = carshop.getTechnicians();
		for (Technician tech : listOfTechnicians) {
			if (tech.getPassword().equals(currentUserPassword) && tech.getUsername().equals(currentUserUsername)
					&& tech.getType().toString().equals(type)) {
				TechAccountExist = true;
			}
		}
		assertTrue(TechAccountExist);
	}

	@Then("the corresponding garage for the technician shall be created")
	public void technicianGarageValidity() {
		boolean TechGarageExist = false;
		List<Technician> listOfTechnicians = carshop.getTechnicians();
		for (Technician tech : listOfTechnicians) {
			if (tech.getPassword().equals(currentUserPassword) && tech.getUsername().equals(currentUserUsername)) {
				if (tech.getGarage() != null) {
					TechGarageExist = true;
				}
			}
		}
		assertTrue(TechGarageExist);
	}

	@Then("the garage should have the same opening hours as the business")
	public void technicianGarageHourValidity() {
		boolean TechGarageHourExist = false;
		List<Technician> listOfTechnicians = carshop.getTechnicians();
		for (Technician tech : listOfTechnicians) {
			if (tech.getPassword().equals(currentUserPassword) && tech.getUsername().equals(currentUserUsername)) {
				if (tech.getGarage() != null) {
					if (carshop.getBusiness() == null && tech.getGarage().getBusinessHours().size() == 0) {
						TechGarageHourExist = true;
					} else {
						if (tech.getGarage().getBusinessHours().size() == carshop.getBusiness().getBusinessHours()
								.size()) {
							TechGarageHourExist = true;
							boolean BusinessHourExist = false;
							for (BusinessHour businessHour : tech.getGarage().getBusinessHours()) {
								for (BusinessHour businessHour2 : carshop.getBusiness().getBusinessHours()) {
									if (businessHour == businessHour2) {
										BusinessHourExist = true;
									}
								}
								if (BusinessHourExist = false) {
									TechGarageHourExist = false;
								}
								BusinessHourExist = false;
							}
						}
					}
				}
			}
		}
		assertTrue(TechGarageHourExist);
	}

	// Combo part
	// Author: Khai
	// Upadate Service Combo and Define
	@When("{string} initiates the definition of a service combo {string} with main service {string}, services {string} and mandatory setting {string}")
	public void initiateServiceComboWithOwner(String user, String name, String mainServiceName, String servicesString,
			String mandatoryString) {
		try {
			initServiceCombo(name, mainServiceName, servicesString, mandatoryString);
		} catch (RuntimeException e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	@Then("the service combo {string} shall not exist in the system")
	public void theComboShallNotExist(String combo) {
		assertFalse(CarShopController.comboExist(combo));
	}

	private void initServiceCombo(String name, String mainServiceName, String servicesString, String mandatoryString) {
		String[] services = servicesString.split(",");
		String[] mandatory = mandatoryString.split(",");
		CarShopController.createServiceCombo(currentUser.getUsername(), services, mainServiceName, name, mandatory);
	}

	@Then("the service combo {string} shall exist in the system")
	public void comboShallExist(String comboName) {
		assertTrue(CarShopController.comboExist(comboName));
	}

	@Then("the service combo {string} shall contain the services {string} with mandatory setting {string}")
	public void comboShallContain(String comboName, String servicesString, String mandatoryString) {
		String[] services = servicesString.split(",");
		String[] mandatory = mandatoryString.split(",");
		for (int i = 0; i < services.length; i++) {
			assertTrue(CarShopController.isInCombo(services[i], comboName));
			assertEquals(Boolean.parseBoolean(mandatory[i]),
					CarShopController.findServiceInCombo(services[i], comboName).getMandatory());
		}
	}

	@Then("the service combo {string} shall preserve the following properties:")
	public void comboPreserves(String comboName, List<Map<String, String>> list) {
		for (Map<String, String> mapu : list) {
			comboShallContain(comboName, mapu.get("services"), mapu.get("mandatory"));
			assertEquals(mapu.get("mainService"),
					CarShopController.findCombo(comboName).getMainService().getService().getName());
		}
	}

	@Then("the main service of the service combo {string} shall be {string}")
	public void mainServiceShallBe(String comboName, String mainName) {
		assertEquals(mainName, CarShopController.findCombo(comboName).getMainService().getService().getName());
	}

	@Then("the service {string} in service combo {string} shall be mandatory")
	public void mainShallbeMandatory(String mainName, String comboName) {
		assertTrue(CarShopController.findCombo(comboName).getMainService().getMandatory());
	}

	@Then("the number of service combos in the system shall be {string}")
	public void numberOfServicesIs(String numberString) {
		assertEquals(Integer.parseInt(numberString), CarShopController.numberOfCombo());
	}

	@When("{string} initiates the update of service combo {string} to name {string}, main service {string} and services {string} and mandatory setting {string}")
	public void updateServiceCombo(String user, String prev, String newName, String mainService, String services,
			String mandarins) {

		try {
			CarShopController.updateService(user, prev, services.split(","), mainService, newName,
					mandarins.split(","));
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	@Then("the service combo {string} shall be updated to name {string}")
	public void nameShallBeUpdated(String preName, String newName) {
		if (!preName.equals(newName)) {
			assertFalse(CarShopController.comboExist(preName));
		}
		assertTrue(CarShopController.comboExist(newName));
	}
//Author: davidbreton3
	// MakeAppointment

	@Given("all garages has the following opening hours")
	public void garagesHasTheFollowingOpeningHours(List<Map<String, String>> list) {
		for (Map<String, String> map : list) {
			for (Garage garage : carshop.getGarages()) {
				garage.addBusinessHour(new BusinessHour(DayOfWeek.valueOf(map.get("day")),
						Time.valueOf(map.get("startTime") + ":00"), Time.valueOf(map.get("endTime") + ":00"), carshop));
			}
		}
	}

	@Given("{string} is logged in to their account")
	public void customerLoggedIn(String username) {
		userLoggedInToAnAccountWithUsername(username);
	}

	@Given("the following appointments exist in the system:")
	public void AppointmentInTheSystem(List<Map<String, String>> list) {
		for (Map<String, String> mapu : list) {
			String customerString = mapu.get("customer");
			String serviceName = mapu.get("serviceName");
			Date date = Date.valueOf(mapu.get("date"));
			String[] timeSlots = mapu.get("timeSlots").split(",");
			String[] optServices = mapu.get("optServices").split(",");
			String mainServices = serviceName.replace("-combo", "");
			String mainService = mainServices.replace("-basic", "");
			String[] services = new String[optServices.length + 1];
			services[0] = mainService;
			for (int i = 0; i < optServices.length; i++) {
				if (services[i + 1] != serviceName) {
					services[i + 1] = optServices[i];
				}
			}
			Time[] listOfStartTimes = new Time[timeSlots.length];
			Time[] listOfEndTimes = new Time[timeSlots.length];
			List<BookableService> bookableServices = carshop.getBookableServices();
			BookableService bookableServiceAppointment = null;

			for (BookableService bookableService : bookableServices) {
				if (bookableService.getName().equals(serviceName)) {
					bookableServiceAppointment = bookableService;
				}
			}
			for (int i = 0; i < timeSlots.length; i++) {
				String[] TimeStrings = timeSlots[i].split("-");
				Time startTime = Time.valueOf(TimeStrings[0] + ":00");
				Time endTime = Time.valueOf(TimeStrings[1] + ":00");
				listOfStartTimes[i] = startTime;
				listOfEndTimes[i] = endTime;
			}
			Appointment appointment = null;
			Customer cus = null;
			for (Customer customer : carshop.getCustomers()) {
				if (customer.getUsername().equals(customerString)) {
					cus = customer;
					appointment = new Appointment(customer, bookableServiceAppointment, carshop);
					int index = 0;
					for (String s : services) {
						for (ComboItem service : ((ServiceCombo) bookableServiceAppointment).getServices()) {
							if (service.getService().getName().equals(s)) {
								TimeSlot timeSlot = new TimeSlot(date, listOfStartTimes[index], date,
										listOfEndTimes[index], carshop);
								appointment.addServiceBooking((Service) service.getService(), timeSlot);
								index++;
							}
						}
					}
				}

			}
			if (cus != null) {
				cus.addAppointment(appointment);
				numberOfAppointment = carshop.getAppointments().size();
			}
		}
	}

	@When("{string} schedules an appointment on {string} for {string} at {string}")
	public void scheduleAppointment(String username, String date, String serviceName, String startTime) {
		try {
			CarShopController.makeServiceAppointment(username, date, serviceName, startTime, carshop.getSystemTime(),
					carshop.getSystemDate());
		} catch (Exception e) {
				error += e.getMessage();
			errorCntr++;
		}
	}

	@When("{string} schedules an appointment on {string} for {string} with {string} at {string}")
	public void scheduleComboAppointment(String username, String date, String serviceName, String optServices,
			String startTimes) {
		try {
			CarShopController.makeServiceComboAppointment(username, date, serviceName, optServices, startTimes,
					carshop.getSystemTime(), carshop.getSystemDate());
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	@When("{string} attempts to cancel their {string} appointment on {string} at {string}")
	public void cancelAppointment(String username, String serviceName, String date, String time) {
		try {
			CarShopController.CancelServiceAppointment(username, date, serviceName, time, carshop.getSystemDate(),
					carshop.getSystemTime());
		} catch (Exception e) {
			error += e.getMessage();
			System.out.println(e.getMessage());
			errorCntr++;
		}
	}

	@When("{string} attempts to cancel {string}'s {string} appointment on {string} at {string}")
	public void cancelAppointmentWrongUser(String GoddUserName, String WrongUser, String serviceName, String date,
			String time) {
		try {
			CarShopController.CancelServiceAppointment(WrongUser, date, serviceName, time, carshop.getSystemDate(),
					carshop.getSystemTime());
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	@Then("{string} shall have a {string} appointment on {string} from {string} to {string}")
	public void checkAppointment(String userName, String serviceName, String date, String startTime, String endTime) {
		boolean appointmentExist = false;
		boolean isSameDate = false;
		for (Customer customer : carshop.getCustomers()) {
			if (customer.getUsername().equals(userName)) {
				for (Appointment appointment : customer.getAppointments()) {
					BookableService bookableService = appointment.getBookableService();
					if (bookableService.getName().equals(serviceName)) {
						String startTimes = null;
						String endTimes = null;
						for (ServiceBooking serviceBooking : appointment.getServiceBookings()) {
							if (serviceBooking.getTimeSlot().getStartDate().toString().equals(date)) {
								isSameDate = true;
								String startTimeString = serviceBooking.getTimeSlot().getStartTime().toString();
								String endTimeString = serviceBooking.getTimeSlot().getEndTime().toString();
								startTimeString = startTimeString.charAt(0) == '0' ? startTimeString.substring(1, 5)
										: startTimeString.substring(0, 5);
								endTimeString = endTimeString.charAt(0) == '0' ? endTimeString.substring(1, 5)
										: endTimeString.substring(0, 5);
								startTimes = startTimes != null ? startTimes + "," + startTimeString : startTimeString;
								endTimes = endTimes != null ? endTimes + "," + endTimeString : endTimeString;
							}
						}
						if (isSameDate) {
							if (startTimes.equals(startTime) && endTimes.equals(endTime)) {
								appointmentExist = true;
							}
						}
					}
				}
			}
		}

		assertTrue(appointmentExist);
	}

	@Then("there shall be {int} more appointment in the system")
	public void numberOfAppointmentCheck(int numberOfAppointmentsInt) {
		boolean check = false;
		if (carshop.getAppointments().size() == numberOfAppointment + numberOfAppointmentsInt) {
			check = true;
		}
		assertTrue(check);
	}

	@Then("there shall be {int} less appointment in the system")
	public void numberOfAppointmentCancelCheck(int numberOfAppointmentsInt) {
		boolean check = false;
		if (carshop.getAppointments().size() == numberOfAppointment - numberOfAppointmentsInt) {
			check = true;
		}
		assertTrue(check);
	}

	@Then("the system shall report {string}")
	public void SystemRepport(String err) {
		assertTrue(error.contains(err));
	}

	@Then("{string}'s {string} appointment on {string} at {string} shall be removed from the system")
	public void removeCheck(String userName, String serviceName, String date, String startTime) {
		boolean appointmentExist = false;
		boolean isSameDate = false;
		for (Customer customer : carshop.getCustomers()) {
			if (customer.getUsername().equals(userName)) {
				for (Appointment appointment : customer.getAppointments()) {
					BookableService bookableService = appointment.getBookableService();
					if (bookableService.getName().equals(serviceName)) {
						String startTimes = null;
						for (ServiceBooking serviceBooking : appointment.getServiceBookings()) {
							if (serviceBooking.getTimeSlot().getStartDate().toString().equals(date)) {
								isSameDate = true;
								String startTimeString = serviceBooking.getTimeSlot().getStartTime().toString();
								startTimeString = startTimeString.charAt(0) == '0' ? startTimeString.substring(1, 5)
										: startTimeString.substring(0, 5);
								startTimes = startTimes != null ? startTimes + "," + startTimeString : startTimeString;
							}
						}
						if (isSameDate) {
							if (startTimes.equals(startTime)) {
								appointmentExist = true;
							}
						}
					}
				}
			}
		}
		assertTrue(!appointmentExist);
	}

	@Then("{string} shall have a {string} appointment on {string} at {string} with the following properties")
	public void checkAppointmentWithProperties(String userName, String serviceName, String date, String time,
			List<Map<String, String>> list) {
		for (Map<String, String> mapu : list) {
			String[] optServices = mapu.get("optServices").split(",");
			String[] timeSlots = mapu.get("timeSlots").split(",");
			String timeSlot = "";
			int numberOfopt = 0;
			int index = 0;
			int timeslotEquals = 0;
			boolean isTrue = false;
			for (Customer customer : carshop.getCustomers()) {
				if (customer.getUsername().equals(userName)) {
					for (Appointment appointment : customer.getAppointments()) {
						if (appointment.getBookableService().getName().equals(serviceName)) {
							for (ServiceBooking serviceBooking : appointment.getServiceBookings()) {
								if (serviceBooking.getTimeSlot().getStartDate().toString().equals(date)) {
									String startTimeString = serviceBooking.getTimeSlot().getStartTime().toString();
									String endTimeString = serviceBooking.getTimeSlot().getEndTime().toString();
									startTimeString = startTimeString.charAt(0) == '0' ? startTimeString.substring(1, 5)
											: startTimeString.substring(0, 5);
									endTimeString = endTimeString.charAt(0) == '0' ? endTimeString.substring(1, 5)
											: endTimeString.substring(0, 5);
									timeSlot = startTimeString + "-" + endTimeString;
									for (String opt : optServices) {
										if (serviceBooking.getService().getName().equals(opt)) {
											numberOfopt++;
										}
									}
									if (timeSlot.equals(timeSlots[index])) {
										timeslotEquals++;
									}
									index++;
								}
							}
						}
					}
				}
			}
		}
	}

	// @Author cpare
	@Given("no business exists")
	public void noBusinessExists() {
		if (carshop.hasBusiness()) {
			carshop.getBusiness().delete();
		}
	}

	// @Author cpare
	@When("the user tries to set up the business information with new {string} and {string} and {string} and {string}")
	public void the_user_tries_to_set_up_the_business_information_with_new_and_and_and(String name, String address,
			String phone, String email) {
		try {
			CarShopController.setupBusinessInfo(name, address, phone, email);
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
			System.out.println(error);
		}
	}

	// @Author cpare
	@Then("a new business with new {string} and {string} and {string} and {string} shall {string} created")
	public void a_new_business_with_new_and_and_and_shall_created(String name, String address, String phone,
			String email, String result) {
		if (result.equals("be")) {
			Business business = carshop.getBusiness();
			boolean temp = false;
			if (business.getName().equals(name)) {
				if (business.getAddress().equals(address)) {
					if (business.getPhoneNumber().equals(phone)) {
						if (business.getEmail().equals(email)) {
							temp = true;
						}
					}
				}
			}
			assertTrue(temp);
		} else {
			assertTrue(carshop.getBusiness() == null);
		}
	}

	// @Author: cpare
	@Then("an error message {string} shall {string} raised")
	public void an_error_message_shall_raised(String result, String resultError) {
		if (resultError.equals("be")) {
			assertTrue(error.contains(result));
		} else {
			assertEquals(" ", error);
		}
	}

	// @Author: cpare
	@Then("an error message {string} shall {string} be raised")
	public void an_error_message_shall_be_raised(String result, String resultError) {
		if (resultError.equals("")) {
				assertTrue(error.contains(result));
		} else {
			assertEquals(" ", error);
		}
	}

	// @Author cpare
	@Given("the business has a business hour on {string} with start time {string} and end time {string}")
	public void the_business_has_a_business_hour_on_with_start_time_and_end_time(String day, String startTime,
			String endTime) {
		carshop.getBusiness().addBusinessHour(new BusinessHour(DayOfWeek.valueOf(day), Time.valueOf(startTime + ":00"),
				Time.valueOf(endTime + ":00"), carshop));

	}

	// @Author cpare
	@When("the user tries to add a new business hour on {string} with start time {string} and end time {string}")
	public void the_user_tries_to_add_a_new_business_hour_on_with_start_time_and_end_time(String Day,
			String newStartTime, String newEndTime) {
		try {
			CarShopController.addBusinessHour(Day, newStartTime, newEndTime);
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
			System.out.println(e.getMessage());
		}
	}

	//@Author ns
	@When("the user tries to access the business information")
	public void the_user_tries_to_access_the_business_information() {
		Business business = carshop.getBusiness();
		error = business.getName() + business.getAddress() + business.getPhoneNumber() + business.getEmail();
	}

	//@Author ns
	@Then("the {string} and {string} and {string} and {string} shall be provided to the user")
	public void the_and_and_and_shall_be_provided_to_the_user(String name, String address, String phoneNumber,
			String email) {
		String info = name + address + phoneNumber + email;
		assertEquals(error, info);
	}

	// @Author cpare
	@Given("the system's time and date is {string}")
	public void the_system_s_time_and_date_is(String string) {
		carshop.setSystemDate(Date.valueOf(string.substring(0, 10)));
		carshop.setSystemTime(Time.valueOf(string.substring(11) + ":00"));
	}

	//@Author cpare
	@When("the user tries to update the business information with new {string} and {string} and {string} and {string}")
	public void userTriesToUpdateBusinessInformation(String name, String address, String phoneNumber, String email) {
		try {
			CarShopController.updateBusiness(name, address, phoneNumber, email);
		} catch (Exception e) {
			error = e.getMessage();
			errorCntr++;
			System.out.println(error);
		}
	}

	// @Author cpare
	@Then("the business information shall {string} updated with new {string} and {string} and {string} and {string}")
	public void the_business_information_shall_updated_with_new_and_and_and(String result, String name, String address,
			String phoneNumber, String email) {
		if (result.equals("be")) {
			a_new_business_with_new_and_and_and_shall_created(name, address, phoneNumber, email, result);
		} else {
			assertEquals(1, errorCntr);
		}
	}

	// @Author cpare
	@Then("a new business hour shall {string} created")
	public void a_new_business_hour_shall_created(String result) {
		if (result.equals("be")) {
			assertEquals(0, errorCntr);
		} else {
			assertEquals(1, errorCntr);
		}
	}

	// @Author cpare
	@When("the user tries to change the business hour {string} at {string} to be on {string} starting at {string} and ending at {string}")
	public void userTriesToChangeBusinessHour(String oldDay, String oldTime, String Day, String newStartTime,
			String newEndTime) {
		try {
			CarShopController.updateBusinessHour(oldDay, oldTime, Day, newStartTime, newEndTime);
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
			System.out.println(error);
		}
	}

	// @Author cpare
	@Then("the business hour shall {string} be updated")
	public void the_business_hour_shall_be_updated(String string) {
		if (string.equals("be")) {
			assertEquals(0, errorCntr);
		} else {
			assertEquals(1, errorCntr);
		}
	}

	// @Author: cpare
	@When("the user tries to remove the business hour starting {string} at {string}")
	public void userTriesToRemoveBusinessHour(String day, String time) {
		try {
			CarShopController.removeBusinessHour(day, time);
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	// @Author: cpare
	@Then("the business hour starting {string} at {string} shall {string} exist")
	public void businessHourShallExist(String day, String time, String result) {
		boolean found = false;

		for (BusinessHour bs : carshop.getBusiness().getBusinessHours()) {
			if (bs.getDayOfWeek().equals(DayOfWeek.valueOf(day))
					&& bs.getStartTime().equals(Time.valueOf(time + ":00"))) {
				found = true;
			}
		}
		if (result.equals("not")) {
			assertTrue(!found);
		} else {
			assertTrue(found);
		}
	}

	// @Author cpare
	@When("the user tries to add a new {string} with start date {string} at {string} and end date {string} at {string}")
	public void userTriesToAddNewTimeSlot(String type, String startDate, String startTime, String endDate,
			String endTime) {
		try {
			CarShopController.addSlot(type, startDate, startTime, endDate, endTime);
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
			System.out.println(error);
		}
	}

	// @Author cpare
	@Then("a new {string} shall {string} be added with start date {string} at {string} and end date {string} at {string}")
	public void a_new_shall_be_added_with_start_date_at_and_end_date_at(String type, String result, String startDate,
			String startTime, String endDate, String endTime) {
		if (result.equals("not be")) {
			assertEquals(1, errorCntr);
		} else {
			boolean found = false;
			if (type.equals("vacation")) {
				for (TimeSlot ts : carshop.getBusiness().getVacations()) {
					if (ts.getStartDate().toString().equals(startDate) && ts.getEndDate().toString().equals(endDate)) {
						if (ts.getStartTime().toString().equals(startTime + ":00")
								&& ts.getEndTime().toString().equals(endTime + ":00")) {
							found = true;
							break;
						}
					}
				}
			} else {
				for (TimeSlot ts : carshop.getBusiness().getHolidays()) {
					if (ts.getStartDate().toString().equals(startDate) && ts.getEndDate().toString().equals(endDate)) {
						if (ts.getStartTime().toString().equals(startTime + ":00")
								&& ts.getEndTime().toString().equals(endTime + ":00")) {
							found = true;
							break;
						}
					}
				}
			}
			assertTrue(found);
		}
	}

	// @Author cpare
	@When("the user tries to change the {string} on {string} at {string} to be with start date {string} at {string} and end date {string} at {string}")
	public void userTriesToChangeVHTime(String type, String oldDate, String oldStartTime, String startDate,
			String startTime, String endDate, String endTime) {
		try {
			CarShopController.updateSlot(type, oldDate, oldStartTime, startDate, startTime, endDate, endTime);
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	//@Author cpare
	@Then("the {string} shall {string} updated with start date {string} at {string} and end date {string} at {string}")
	public void vhShallBeUpdated(String type, String result, String startDate, String startTime, String endDate,
			String endTime) {
		if (result.equals("not be")) {
			assertEquals(1, errorCntr);
		} else {
			a_new_shall_be_added_with_start_date_at_and_end_date_at(type, result, startDate, startTime, endDate,
					endTime);
		}
	}

	// @Author cpare
	@When("the user tries to remove an existing {string} with start date {string} at {string} and end date {string} at {string}")
	public void userTriesToRemoveVH(String type, String startDate, String startTime, String endDate, String endTime) {
		try {
			CarShopController.deleteSlot(type, startDate, startTime, endDate, endTime);
		} catch (Exception e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	// @Author cpare
	@Then("the {string} with start date {string} at {string} shall {string} exist")
	public void vhShallNotExists(String type, String startDate, String startTime, String result) {
		if (result.equals("")) {
			assertEquals(1, errorCntr);
		}
		boolean found = false;
		if (type.equals("vacation")) {
			for (TimeSlot ts : carshop.getBusiness().getVacations()) {
				if (ts.getStartDate().toString().equals(startDate)
						&& ts.getStartTime().toString().equals(startTime + ":00")) {
					found = true;
					break;
				}
			}
		} else {
			for (TimeSlot ts : carshop.getBusiness().getHolidays()) {
				if (ts.getStartDate().toString().equals(startDate)
						&& ts.getStartTime().toString().equals(startTime + ":00")) {
					found = true;
					break;
				}
			}
		}
		assertTrue(found);
	}
	
	
}
