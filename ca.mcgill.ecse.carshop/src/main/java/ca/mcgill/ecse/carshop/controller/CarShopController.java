package ca.mcgill.ecse.carshop.controller;

import ca.mcgill.ecse.carshop.application.CarShopApplication;
import ca.mcgill.ecse.carshop.model.*;
import ca.mcgill.ecse.carshop.model.BusinessHour.DayOfWeek;
import ca.mcgill.ecse.carshop.model.Technician.TechnicianType;
import ca.mcgill.ecse.carshop.persistence.CarShopPersistence;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class CarShopController {

	public CarShopController() {
	}

	// @Author C-APare
	public static User getCurrentUser() {
		CarShop carshop = CarShopApplication.getCarShop();
		for (User user : carshop.getCustomers()) {
			if (user.getIsLoggedIn()) {
				return user;
			}
		}
		for (User user : carshop.getTechnicians()) {
			if (user.getIsLoggedIn()) {
				return user;
			}
		}
		if (carshop.getOwner() != null) {
			if (carshop.getOwner().getIsLoggedIn()) {
				return carshop.getOwner();
			}
		}
		return null;
	}

	// @Author davidbreton3
	public static void LogIn(String userName, String password) {

		CarShop carshop = CarShopApplication.getCarShop();
		boolean newUserLoggedIn = false;

		List<Technician> listOfTechnicians = carshop.getTechnicians();
		List<Customer> listOfCustomers = carshop.getCustomers();
		Owner owner = carshop.getOwner();

		for (Technician tech : listOfTechnicians) {
			if (tech.getPassword().equals(password) && tech.getUsername().equals(userName)) {
				tech.setIsLoggedIn(true);
				newUserLoggedIn = true;
			}
		}
		for (Customer cust : listOfCustomers) {
			if (cust.getPassword().equals(password) && cust.getUsername().equals(userName)) {
				cust.setIsLoggedIn(true);
				newUserLoggedIn = true;
			}
		}
		if (owner != null) {
			if (owner.getPassword().equals(password) && owner.getUsername().equals(userName)) {
				owner.setIsLoggedIn(true);
				newUserLoggedIn = true;
			}
		}
		if (newUserLoggedIn != true && userName.equals("owner") && password.equals("owner")) {
			if (carshop.getOwner() == null)
				carshop.setOwner(new Owner(userName, password, true, carshop));
			newUserLoggedIn = true;
		}

		if (newUserLoggedIn != true && userName.contains("Technician") && password.contains("Technician")
				&& userName.equals(password)) {
			String type = userName.substring(0, userName.length() - "-Technician".length());

			TechnicianType type2 = null;

			switch (type) {
			case "Tire":
				type2 = TechnicianType.Tire;
				break;
			case "Engine":
				type2 = TechnicianType.Engine;
				break;
			case "Electronics":
				type2 = TechnicianType.Electronics;
				break;
			case "Transmission":
				type2 = TechnicianType.Transmission;
				break;
			case "Fluids":
				type2 = TechnicianType.Fluids;
				break;
			}

			Technician newTechnician = new Technician(userName, password, true, type2, carshop);
			Garage newGarage = new Garage(carshop, newTechnician);
			if (carshop.getBusiness() != null) {
				for (BusinessHour businessHour : carshop.getBusiness().getBusinessHours()) {
					newGarage.addBusinessHour(businessHour);
				}
				newTechnician.setGarage(newGarage);
			}

			newUserLoggedIn = true;
		}
		if (!newUserLoggedIn) {
			throw new RuntimeException("Username/password not found");
		}
		CarShopPersistence.save(carshop);
	}

	// @Author davidbreton3
	public static void LogOut() {

		CarShop carshop = CarShopApplication.getCarShop();
		String password = getCurrentUser().getPassword();
		String userName = getCurrentUser().getUsername();

		try {
			List<Technician> listOfTechnicians = carshop.getTechnicians();
			List<Customer> listOfCustomers = carshop.getCustomers();
			Owner owner = carshop.getOwner();

			for (Technician tech : listOfTechnicians) {
				if (tech.getPassword().equals(password) && tech.getUsername().equals(userName)) {
					tech.setIsLoggedIn(false);
				}
			}
			for (Customer cust : listOfCustomers) {
				if (cust.getPassword().equals(password) && cust.getUsername().equals(userName)) {
					cust.setIsLoggedIn(false);
				}
			}
			if (owner != null) {
				if (owner.getPassword().equals(password) && owner.getUsername().equals(userName)) {
					owner.setIsLoggedIn(false);
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		CarShopPersistence.save(carshop);
	}

	// @Author davidbreton3
	public static void addNewGarageOpeningHours(String day, String startTimeString, String endTimeString, String type) {

		CarShop carshop = CarShopApplication.getCarShop();

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

		if (dayOfWeek == null) {
			throw new RuntimeException("Day of Week doesn't exist");
		}

		if (!(getCurrentUser() instanceof Technician)) {
			throw new RuntimeException("You are not authorized to perform this operation");
		}

		if (startTime == null | endTime == null | dayOfWeek == null) {
			throw new RuntimeException("Add new business hour with invalid parameters");
		}

		if (!(startTime.before(endTime))) {
			throw new RuntimeException("Start time must be before end time");
		}

		boolean dayOfWeekExist = false;

		for (BusinessHour businessHour : carshop.getBusiness().getBusinessHours()) {
			if (businessHour.getDayOfWeek().toString().equals(day)) {
				dayOfWeekExist = true;
				if (startTime.before(businessHour.getStartTime()) || endTime.after(businessHour.getEndTime())) {
					throw new RuntimeException("The opening hours are not within the opening hours of the business");
				}
			}
		}

		if (!dayOfWeekExist) {
			throw new RuntimeException("The opening hours are not within the opening hours of the business");
		}

		if ((getCurrentUser() instanceof Technician)) {
			if (!(((Technician) getCurrentUser()).getType().toString().equals(type))) {
				throw new RuntimeException("You are not authorized to perform this operation");
			}

			for (BusinessHour businessHour : ((Technician) getCurrentUser()).getGarage().getBusinessHours()) {
				if (businessHour.getDayOfWeek().toString().equals(day)) {
					throw new RuntimeException("The opening hours cannot overlap");
				}
			}
		}

		try {
			Garage garage = ((Technician) getCurrentUser()).getGarage();
			BusinessHour businessHour = new BusinessHour(dayOfWeek, startTime, endTime, carshop);
			boolean successful = garage.addBusinessHour(businessHour);
			if (!successful) {
				throw new RuntimeException("Opening Hour already exist");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		CarShopPersistence.save(carshop);
	}

	// @Author davidbreton3
	public static void removeGarageOpeningHours(String day, String startTimeString, String endTimeString, String type) {

		CarShop carshop = CarShopApplication.getCarShop();

		Time startTime = Time.valueOf(startTimeString + ":00");
		Time endTime = Time.valueOf(endTimeString + ":00");

		DayOfWeek dayOfWeek = DayOfWeek.valueOf(day);

		if (!(getCurrentUser() instanceof Technician)) {
			throw new RuntimeException("You are not authorized to perform this operation");
		}

		if ((getCurrentUser() instanceof Technician)) {
			if (!(((Technician) getCurrentUser()).getType().toString().equals(type))) {
				throw new RuntimeException("You are not authorized to perform this operation");
			}
		}

		BusinessHour businessHour = new BusinessHour(dayOfWeek, startTime, endTime, carshop);
		Garage garage = ((Technician) getCurrentUser()).getGarage();
		for (BusinessHour bs : garage.getBusinessHours()) {
			if (bs.getDayOfWeek().equals(businessHour.getDayOfWeek())) {
				if (bs.getEndTime().equals(businessHour.getEndTime())) {
					if (bs.getStartTime().equals(businessHour.getStartTime())) {
						boolean successful = garage.removeBusinessHour(bs);
						if (!successful) {
							throw new RuntimeException("BusinessHour doesn't exist");
						}
						break;
					}
				}
			}
		}
		CarShopPersistence.save(carshop);
	}

	// @Author cpare
	public static Appointment changeAppointmentService(Appointment appointment, String newService) {
		CarShop carshop = CarShopApplication.getCarShop();
		Service service = null;
		Date appointmentDate = appointment.getServiceBooking(0).getTimeSlot().getStartDate();
		if (appointmentDate.after(carshop.getSystemDate())) {
			for (BookableService bs : carshop.getBookableServices()) {
				if (bs.getName().equals(newService)) {
					service = (Service) bs;
				}
			}
			try {
				if (service != null) {
					Customer user = appointment.getCustomer();
					TimeSlot t = appointment.getServiceBooking(0).getTimeSlot();
					Appointment newApp = makeServiceAppointment(user.getUsername(), appointmentDate.toString(),
							newService, t.getStartTime().toString().substring(0, 5), carshop.getSystemTime(),
							carshop.getSystemDate());
					appointment.delete();
					return newApp;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return appointment;
			}
		}
		return appointment;
	}

	// @Author davidbreton3

	private static String getGoodString(String startTimeString) {
		return startTimeString.charAt(0) == '0' ? startTimeString.substring(1, 5) : startTimeString.substring(0, 5);
	}

	public static Appointment makeServiceAppointment(String userName, String dateString, String serviceName,
			String startTimeString, Time currentTime, Date currentDate) {
		Date date = Date.valueOf(dateString);
		Time startTime = Time.valueOf(startTimeString + ":00");

		User user = getCurrentUser();
		if (!(user instanceof Customer)) {
			throw new RuntimeException("Only customers can make an appointment");
		}
		CarShop carshop = CarShopApplication.getCarShop();
		BookableService bookableServiceAppointment = null;
		Date endDate = null;
		List<BookableService> bookableServices = carshop.getBookableServices();
		Time endTime = Time.valueOf(startTimeString + ":00");
		DayOfWeek day = null;
		boolean dayExist = false;

		switch (date.getDay()) {
		case 1:
			day = DayOfWeek.Monday;
			break;
		case 2:
			day = DayOfWeek.Tuesday;
			break;
		case 3:
			day = DayOfWeek.Wednesday;
			break;
		case 4:
			day = DayOfWeek.Thursday;
			break;
		case 5:
			day = DayOfWeek.Friday;
			break;
		case 6:
			day = DayOfWeek.Saturday;
			break;
		case 0:
			day = DayOfWeek.Sunday;
			break;
		}

		if (currentDate.after(date)) {
			throw new RuntimeException("There are no available slots for " + serviceName + " on " + date.toString()
					+ " at " + getGoodString(startTime.toString()));
		}

		if (currentDate.equals(date)) {
			if (currentTime.after(startTime)) {
				throw new RuntimeException("There are no available slots for " + serviceName + " on " + date.toString()
						+ " at " + getGoodString(startTime.toString()));
			}
		}
		for (BookableService bookableService : bookableServices) {
			if (bookableService.getName().equals(serviceName)) {
				bookableServiceAppointment = bookableService;
			}
		}

		if (bookableServiceAppointment == null) {
			throw new RuntimeException("No such Service");
		}

		if (!user.getUsername().equals(userName)) {
			throw new RuntimeException("A customer can only make their own appointments");
		}
		for (TimeSlot timeslot : carshop.getBusiness().getHolidays()) {
			if (date.after(timeslot.getStartDate()) && date.before(timeslot.getEndDate())) {
				throw new RuntimeException("There are no available slots for " + serviceName + " on " + date.toString()
						+ " at " + getGoodString(startTime.toString()));
			}
		}

		if (bookableServiceAppointment instanceof Service) {
			Service service = (Service) bookableServiceAppointment;
			endTime.setHours(startTime.getHours() + ((service.getDuration() + startTime.getMinutes()) / 60));
			endTime.setMinutes(startTime.getMinutes() + service.getDuration() % 60);
			endDate = date;
			for (Appointment appointment : carshop.getAppointments()) {
				for (ServiceBooking serviceBooking : appointment.getServiceBookings()) {

					if (serviceBooking.getTimeSlot().getStartDate().toString().equals(date.toString())) {
						if (serviceBooking.getService().getGarage().getTechnician().getUsername()
								.equals(service.getGarage().getTechnician().getUsername())) {
							if (serviceBooking.getTimeSlot().getStartTime().before(endTime)
									&& serviceBooking.getTimeSlot().getStartTime().after(startTime)) {
								throw new RuntimeException("There are no available slots for " + serviceName + " on "
										+ date.toString() + " at " + getGoodString(startTime.toString()));
							}
							if (serviceBooking.getTimeSlot().getEndTime().before(endTime)
									&& serviceBooking.getTimeSlot().getEndTime().after(startTime)) {

								throw new RuntimeException("There are no available slots for " + serviceName + " on "
										+ date.toString() + " at " + getGoodString(startTime.toString()));
							}
						}
					}
				}
			}
			if (service.getGarage().getBusinessHours() == null) {
				throw new RuntimeException("No garage business hours were set");
			}
			for (BusinessHour businessHour : service.getGarage().getBusinessHours()) {
				if (businessHour.getDayOfWeek().equals(day)) {
					dayExist = true;
					if (startTime.before(businessHour.getStartTime())) {

						throw new RuntimeException("There are no available slots for " + serviceName + " on "
								+ date.toString() + " at " + getGoodString(startTime.toString()));
					}
					// what??
					if (!(endTime.getHours() >= 24)) {
						if (endTime.after(businessHour.getEndTime())) {
							throw new RuntimeException("There are no available slots for " + serviceName + " on "
									+ date.toString() + " at " + getGoodString(startTime.toString()));
						}
					} else {

						throw new RuntimeException("There are no available slots for " + serviceName + " on "
								+ date.toString() + " at " + getGoodString(startTime.toString()));
					}
				}
			}
			if (dayExist == false) {

				throw new RuntimeException("There are no available slots for " + serviceName + " on " + date.toString()
						+ " at " + getGoodString(startTime.toString()));
			}
		} else {
			throw new RuntimeException("bookableService Is not a Service");
		}
		Appointment appointment = null;
		try {
			Customer customer = (Customer) user;
			appointment = new Appointment(customer, bookableServiceAppointment, carshop);
			TimeSlot timeSlot = new TimeSlot(date, startTime, endDate, endTime, carshop);
			// ServiceBooking serviceBooking =
			// new ServiceBooking((Service) bookableServiceAppointment, timeSlot,
			// appointment);
			appointment.addServiceBooking((Service) bookableServiceAppointment, timeSlot);
			customer.addAppointment(appointment);

		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
		CarShopPersistence.save(carshop);
		return appointment;
	}

	// @Author davidbreton3

	public static Appointment makeServiceComboAppointment(String userName, String dateString, String serviceName,
			String optServicesString, String startTimesString, Time currentTime, Date currentDate) {
		String[] startTimesStrings = startTimesString.split(",");
		String[] optServices = optServicesString.split(",");
		Date date = Date.valueOf(dateString);
		User user = getCurrentUser();
		CarShop carshop = CarShopApplication.getCarShop();
		BookableService bookableServiceAppointment = null;
		Time[] startTimes = new Time[startTimesStrings.length];
		Time[] endTimes = new Time[startTimesStrings.length];
		Date endDate = date;
		Appointment appointment = null;
		List<BookableService> bookableServices = carshop.getBookableServices();
		DayOfWeek day = null;
		boolean dayExist = false;

		switch (date.getDay()) {
		case 1:
			day = DayOfWeek.Monday;
			break;
		case 2:
			day = DayOfWeek.Tuesday;
			break;
		case 3:
			day = DayOfWeek.Wednesday;
			break;
		case 4:
			day = DayOfWeek.Thursday;
			break;
		case 5:
			day = DayOfWeek.Friday;
			break;
		case 6:
			day = DayOfWeek.Saturday;
			break;
		case 0:
			day = DayOfWeek.Sunday;
			break;
		}

		if (carshop.hasBookableServices() == false) {
			throw new RuntimeException("No bookable Service");
		}

		for (BookableService bookableService : bookableServices) {
			if (bookableService.getName().equals(serviceName)) {
				bookableServiceAppointment = bookableService;
			}
		}

		if (bookableServiceAppointment == null) {
			throw new RuntimeException("No such Service");
		}

		if (!(user instanceof Customer)) {
			throw new RuntimeException("Only customers can make an appointment");
		}

		if (!user.getUsername().equals(userName)) {
			throw new RuntimeException("A customer can only make their own appointments");
		}

		if (bookableServiceAppointment instanceof ServiceCombo) {
			ServiceCombo serviceCombo = (ServiceCombo) bookableServiceAppointment;
			int index = 0;
			for (ComboItem item : serviceCombo.getServices()) {

				boolean isOpt = false;

				for (String opt : optServices) {
					if (item.getService().getName().equals(opt)) {
						isOpt = true;
					}
				}

				if (item.getMandatory() | isOpt) {

					Service service = item.getService();

					if (index > startTimesStrings.length - 1) {
						throw new RuntimeException("To much service for the number of time given");
					}
					Time startTime = Time.valueOf(startTimesStrings[index] + ":00");
					Time endTime = Time.valueOf(startTimesStrings[index] + ":00");
					startTimes[index] = startTime;
					endTime.setHours(startTime.getHours() + ((startTime.getMinutes() + service.getDuration()) / 60));
					endTime.setMinutes((startTime.getMinutes() + service.getDuration()) % 60);
					endTimes[index] = endTime;
					endDate = date;
					for (TimeSlot timeslot : carshop.getBusiness().getHolidays()) {
						if (date.after(timeslot.getStartDate()) && date.before(timeslot.getEndDate())) {
							throw new RuntimeException("There are no available slots for " + serviceName + " on "
									+ date.toString() + " at " + getGoodString(startTime.toString()));
						}
					}

					if (currentDate.after(date)) {
						throw new RuntimeException("There are no available slots for " + serviceName + " on "
								+ date.toString() + " at " + getGoodString(startTime.toString()));
					}
					if (currentTime.after(startTime) && currentDate.equals(date)) {
						throw new RuntimeException("There are no available slots for " + serviceName + " on "
								+ date.toString() + " at " + getGoodString(startTime.toString()));
					}

					for (Appointment appointments : carshop.getAppointments()) {
						for (ServiceBooking serviceBooking : appointments.getServiceBookings()) {
							if (serviceBooking.getTimeSlot().getStartDate().toString().equals(date.toString())) {
								if (serviceBooking.getService().getGarage() == service.getGarage()) {
									if (serviceBooking.getTimeSlot().getStartTime().before(endTime)
											&& serviceBooking.getTimeSlot().getStartTime().after(startTime)) {
										throw new RuntimeException("There are no available slots for " + serviceName
												+ " on " + date.toString() + " at "
												+ getGoodString(startTime.toString()));
									}
									if (serviceBooking.getTimeSlot().getEndTime().after(startTime)
											&& serviceBooking.getTimeSlot().getEndTime().before(endTime)) {
										throw new RuntimeException("There are no available slots for " + serviceName
												+ " on " + date.toString() + " at "
												+ getGoodString(startTime.toString()));
									}
								}
							}
						}
					}

					for (BusinessHour businessHour : service.getGarage().getBusinessHours()) {
						if (day != null) {
							if (businessHour.getDayOfWeek() == day) {
								dayExist = true;
								if (startTime.before(businessHour.getStartTime())) {
									throw new RuntimeException("There are no available slots for " + serviceName
											+ " on " + date.toString() + " at " + getGoodString(startTime.toString()));
								}
								if (!(endTime.getHours() >= 24)) {
									if (endTime.after(businessHour.getEndTime())) {
										throw new RuntimeException("There are no available slots for " + serviceName
												+ " on " + date.toString() + " at "
												+ getGoodString(startTime.toString()));
									}
								} else {
									throw new RuntimeException("There are no available slots for " + serviceName
											+ " on " + date.toString() + " at " + getGoodString(startTime.toString()));
								}
							}
						}
					}
					if (dayExist == false) {
						throw new RuntimeException("There are no available slots for " + serviceName + " on "
								+ date.toString() + " at " + getGoodString(startTime.toString()));
					}
					index++;
				}
			}

		} else {
			throw new RuntimeException("bookableService Is not a Service");
		}

		for (Time time : startTimes) {
			for (int i = 0; i < startTimes.length; i++) {
				if (time.before(endTimes[i]) && time.after(startTimes[i])) {
					throw new RuntimeException("Time slots for two services are overlapping");
				}
			}
		}

		for (Time time : endTimes) {
			for (int i = 0; i < endTimes.length; i++) {
				if (time.after(startTimes[i]) && time.before(endTimes[i])) {
					throw new RuntimeException("Time slots for two services are overlapping");
				}
			}
		}
		Customer customer = (Customer) user;
		appointment = new Appointment(customer, bookableServiceAppointment, carshop);
		int index = 0;
		for (ComboItem service : ((ServiceCombo) bookableServiceAppointment).getServices()) {
			if (service.getMandatory()) {
				TimeSlot timeSlot = new TimeSlot(date, startTimes[index], endDate, endTimes[index], carshop);
				appointment.addServiceBooking(service.getService(), timeSlot);
				index++;
			} else {
				for (String optString : optServices) {
					if (service.getService().getName().equals(optString)) {
						TimeSlot timeSlot = new TimeSlot(date, startTimes[index], endDate, endTimes[index], carshop);
						appointment.addServiceBooking(service.getService(), timeSlot);
						index++;
					}
				}
			}
		}

		customer.addAppointment(appointment);
		CarShopPersistence.save(carshop);
		return appointment;
	}

	// @Author davidbreton3
	public static void CancelServiceAppointment(String userName, String dateString, String serviceName,
			String startTimeString, Date currentDate, Time currentTime) {
		Date date = Date.valueOf(dateString);
		User user = getCurrentUser();
		Time startTime = Time.valueOf(startTimeString + ":00");
		CarShop carshop = CarShopApplication.getCarShop();
		DayOfWeek day = null;
		Appointment appointmentToDelete = null;
		boolean dayExist = false;

		switch (date.getDay()) {
		case 1:
			day = DayOfWeek.Monday;
			break;
		case 2:
			day = DayOfWeek.Tuesday;
			break;
		case 3:
			day = DayOfWeek.Wednesday;
			break;
		case 4:
			day = DayOfWeek.Thursday;
			break;
		case 5:
			day = DayOfWeek.Friday;
			break;
		case 6:
			day = DayOfWeek.Saturday;
			break;
		case 7:
			day = DayOfWeek.Sunday;
			break;
		}

		if (!(user instanceof Customer)) {
			if (user instanceof Owner) {
				throw new RuntimeException("An owner cannot cancel an appointment");
			}
			if (user instanceof Technician) {
				throw new RuntimeException("A technician cannot cancel an appointment");
			}
		} else {
			if (currentDate.toString().equals(date.toString())) {
				throw new RuntimeException("Cannot cancel an appointment on the appointment date");
			}

			if (!userName.equals(((Customer) getCurrentUser()).getUsername())) {
				throw new RuntimeException("A customer can only cancel their own appointments");
			}

			for (Appointment appointment : carshop.getAppointments()) {
				BookableService bookableService = appointment.getBookableService();
				if (bookableService.getName().equals(serviceName)) {
					for (ServiceBooking serviceBooking : appointment.getServiceBookings()) {
						if (serviceBooking.getTimeSlot().getStartTime().equals(startTime)
								&& serviceBooking.getTimeSlot().getStartDate().toString().equals(date.toString())) {
							appointmentToDelete = appointment;
						}
					}
				}
			}

			if (appointmentToDelete == null) {
				throw new RuntimeException("This appointment doesn't exist");
			}

			try {
				Customer Currentcustomer = (Customer) user;
				appointmentToDelete.delete();
				Currentcustomer.removeAppointment(appointmentToDelete);
				carshop.removeAppointment(appointmentToDelete);
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
			CarShopPersistence.save(carshop);
		}
	}

	// @Author C-APare
	public static void createCustomer(String username, String password) {
		CarShop carshop = CarShopApplication.getCarShop();
		User current = getCurrentUser();
		if (current != null) {
			if (current.getUsername().equals("owner")) {
				throw new RuntimeException("You must log out of the owner account before creating a customer account");
			}
			if (current instanceof Technician) {
				throw new RuntimeException(
						"You must log out of the technician account before creating a customer account");
			} else
				throw new RuntimeException(
						"You must log out of the current account before creating a new customer account");
		}

		for (Customer customer : carshop.getCustomers()) {
			if (customer.getUsername().equals(username)) {
				throw new RuntimeException("The username already exists");
			}
		}

		if (username.equals("")) {
			throw new RuntimeException("The user name cannot be empty");
		} else if (password.equals("")) {
			throw new RuntimeException("The password cannot be empty");
		} else {
			try {
				Customer customer = new Customer(username, password, false, carshop);
				carshop.addCustomer(customer);
			} catch (Exception e) {
				throw new RuntimeException("The username already exists");
			}
			CarShopPersistence.save(carshop);
		}
	}

	// @Author C-APare
	public static void updateAccount(String newUsername, String newPassword) {
		CarShop carshop = CarShopApplication.getCarShop();
		if (newUsername.equals(""))
			throw new RuntimeException("The user name cannot be empty");
		if (newPassword.equals(""))
			throw new RuntimeException("The password cannot be empty");
		User current = getCurrentUser();
		if (current.getUsername().equals("owner")) {
			if (!newUsername.equals(current.getUsername())) {
				throw new RuntimeException("Changing username of owner is not allowed");
			} else {
				carshop.getOwner().setPassword(newPassword);
			}
		} else if (current instanceof Technician) {
			if (!newUsername.equals(current.getUsername())) {
				throw new RuntimeException("Changing username of technician is not allowed");
			} else {
				current.setPassword(newPassword);
			}
		} else {
			if (current.setUsername(newUsername)) {
				current.setPassword(newPassword);
			} else {
				throw new RuntimeException("Username not available");
			}
		}
		CarShopPersistence.save(carshop);
	}

	// @Author magix022
	private static void checkIfServiceAlreadyExists(String serviceName, CarShop carshop) throws IOException {
		for (BookableService service : carshop.getBookableServices()) {
			if (service.getName().equals(serviceName)) {
				throw new IOException("Service " + serviceName + " already exists");
			}
		}
	}

	// @Author magix022
	public static void createService(String name, Garage aGarage, int duration) throws Exception {
		if (duration <= 0) {
			throw new IOException("Duration must be positive");
		}
		CarShop carshop = CarShopApplication.getCarShop();

		checkIfServiceAlreadyExists(name, carshop);

		User currentUser = getCurrentUser();
		if (currentUser != carshop.getOwner()) {
			throw new Exception("You are not authorized to perform this operation");
		}

		try {
			Service service = new Service(name, carshop, duration, aGarage);

			carshop.addBookableService(service);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
		CarShopPersistence.save(carshop);
	}

	// @Author magix022
	public static void deleteService(String name) throws Exception { // delete service given its name, works for both
		// service and service combo
		CarShop carshop = CarShopApplication.getCarShop();

		User currentUser = getCurrentUser();
		if (currentUser != carshop.getOwner()) {
			throw new Exception("You are not authorized to perform this operation");
		}

		try {
			ArrayList<String> services = new ArrayList<>();
			for (BookableService s : carshop.getBookableServices()) {
				services.add(s.getName());
			}

			int index = services.indexOf(name);

			if (index < 0) {
				throw new IOException("No such service/serviceCombo exists");
			}

			carshop.getBookableService(index).delete();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
		CarShopPersistence.save(carshop);
	}

	// @Author magix022
	public static void updateServiceName(String oldName, String newName) throws Exception { // works for both Service
		// and ServiceCombo
		CarShop carshop = CarShopApplication.getCarShop();

		User currentUser = getCurrentUser();
		if (currentUser != carshop.getOwner()) {
			throw new Exception("You are not authorized to perform this operation");
		}

		for (BookableService service : carshop.getBookableServices()) {
			if (service.getName().equals(newName) && !oldName.equals(newName)) {
				throw new IOException("Service " + newName + " already exists");
			}
		}

		try {
			ArrayList<String> services = new ArrayList<>();
			for (BookableService s : carshop.getBookableServices()) {
				services.add(s.getName());
			}

			int index = services.indexOf(oldName);

			if (index < 0) {
				throw new IOException("No such service/serviceCombo exists");
			}

			carshop.getBookableService(index).setName(newName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		CarShopPersistence.save(carshop);
	}

	// @Author magix022
	public static void updateServiceDuration(String service, int duration) throws Exception { // works ONLY for Service
		CarShop carshop = CarShopApplication.getCarShop();

		User currentUser = getCurrentUser();
		if (currentUser != carshop.getOwner()) {
			throw new Exception("You are not authorized to perform this operation");
		}

		if (duration <= 0) {
			throw new IOException("Duration must be positive");
		}

		try {
			ArrayList<String> services = new ArrayList<>();
			for (BookableService s : carshop.getBookableServices()) {
				services.add(s.getName());
			}

			int index = services.indexOf(service);

			if (index < 0) {
				throw new IOException("No such service exists");
			}

			BookableService target = carshop.getBookableService(index);

			if (target instanceof Service) {
				((Service) target).setDuration(duration);
			} else {
				throw new IOException("No such service exists or this name is associated with a service combo");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		CarShopPersistence.save(carshop);
	}

	// @Author magix022
	public static void updateServiceGarage(String service, Garage garage) throws Exception { // Works ONLY for Service
		CarShop carshop = CarShopApplication.getCarShop();

		User currentUser = getCurrentUser();
		if (currentUser != carshop.getOwner()) {
			throw new Exception("You are not authorized to perform this operation");
		}

		try {
			ArrayList<String> services = new ArrayList<>();
			for (BookableService s : carshop.getBookableServices()) {
				services.add(s.getName());
			}

			int index = services.indexOf(service);

			if (index < 0) {
				throw new IOException("No such service exists");
			}

			BookableService target = carshop.getBookableService(index);

			if (target instanceof Service) {
				((Service) target).setGarage(garage);
			} else {
				throw new IOException("No such service exists or this name is associated with a service combo");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		CarShopPersistence.save(carshop);
	}

	// @Author PhanTTKhai
	public static void createServiceCombo(String username, String[] servicesString, String mainServiceString,
			String name, String[] mandatoryString) {
		ArrayList<Service> services = new ArrayList<>();
		Service mainService = null;
		CarShop carshop = CarShopApplication.getCarShop();
		if (!username.equals(carshop.getOwner().getUsername())) {
			throw new IllegalArgumentException("You are not authorized to perform this operation");
		}
		if (comboExist(name)) {
			throw new IllegalArgumentException("Service combo " + name + " already exists");
		}
		try {
			if (servicesString.length < 2) {
				throw new IllegalArgumentException("A service Combo must contain at least 2 services");
			}

			for (String s : servicesString) {
				services.add(findService(s));
			}
			mainService = findService(mainServiceString);

			if (!services.contains(mainService)) {
				throw new IllegalArgumentException("Main service must be included in the services");
			}
			if (mainService != null && name.startsWith(mainService.getName())) {
				ServiceCombo combo = new ServiceCombo(name, carshop);
				for (int i = 0; i < services.size(); i++) {
					new ComboItem(Boolean.parseBoolean(mandatoryString[i]), services.get(i), combo);
				}
				combo.setMainService(findServiceInCombo(mainServiceString, name));
				carshop.addBookableService(combo);
			} else {
				throw new IllegalArgumentException("Bad service combo name");
			}
			if (!findServiceInCombo(mainServiceString, name).getMandatory()) {
				throw new IllegalArgumentException("Main service must be mandatory");
			}
		} catch (RuntimeException e) {
			if (comboExist(name)) {
				findCombo(name).delete();
			}
			throw e;
		}
		CarShopPersistence.save(carshop);
	}

	// @Author PhanTTKhai
	public static void updateService(String username, String prevCombo, String[] servicesString,
			String mainServiceString, String name, String[] mandatoryString) {
		String mainService = null;
		CarShop carshop = CarShopApplication.getCarShop();
		if (!username.equals(carshop.getOwner().getUsername())) {
			throw new IllegalArgumentException("You are not authorized to perform this operation");
		}
		if (servicesString.length < 2) {
			throw new IllegalArgumentException("A service Combo must contain at least 2 services");
		}
		if (!prevCombo.equals(name) && comboExist(name)) {
			throw new IllegalArgumentException("Service " + name + " already exists");
		}
		for (int i = 0; i < servicesString.length; i++) {
			findService(servicesString[i]);
			if (servicesString[i].equals(mainServiceString)) {
				mainService = mainServiceString;
				if (!Boolean.parseBoolean(mandatoryString[i])) {
					throw new IllegalArgumentException("Main service must be mandatory");
				}
			}
		}
		if (mainService == null) {
			throw new IllegalArgumentException("Main service must be included in the services");
		}

		findCombo(prevCombo).delete();
		createServiceCombo(username, servicesString, mainServiceString, name, mandatoryString);
		CarShopPersistence.save(carshop);
	}

	// @Author PhanTTKhai
	public static ComboItem findServiceInCombo(String Name, String comboName) {
		List<ComboItem> servicesCombo = findCombo(comboName).getServices();
		for (ComboItem item : servicesCombo) {
			if (item.getService().getName().equals(Name)) {
				return item;
			}
		}
		throw new NoSuchElementException("Item not found in Combo");
	}

	// @Author PhanTTKhai
	public static boolean isInCombo(String name, String comboName) {
		List<ComboItem> servicesCombo = findCombo(comboName).getServices();
		for (ComboItem item : servicesCombo) {
			if (item.getService().getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	// @Author PhanTTKhai
	public static Service findService(String name) {
		for (BookableService s : CarShopApplication.getCarShop().getBookableServices()) {
			if ((s instanceof Service) && s.getName().equals(name)) {
				return (Service) s;
			}
		}
		throw new NoSuchElementException("Service " + name + " does not exist");
	}

	// @Author PhanTTKhai
	public static int numberOfCombo() {
		List<BookableService> services = CarShopApplication.getCarShop().getBookableServices();
		int num = 0;
		for (BookableService service : services) {
			if (service instanceof ServiceCombo) {
				num++;
			}
		}
		return num;
	}

	// @Author PhanTTKhai
	public static boolean comboExist(String comboName) {
		List<BookableService> services = CarShopApplication.getCarShop().getBookableServices();
		for (BookableService service : services) {
			if (service instanceof ServiceCombo && ((ServiceCombo) service).getName().equals(comboName)) {
				return true;
			}
		}
		return false;
	}

	// @Author PhanTTKhai
	public static ServiceCombo findCombo(String comboName) {
		List<BookableService> services = CarShopApplication.getCarShop().getBookableServices();
		for (BookableService service : services) {
			if (service instanceof ServiceCombo && ((ServiceCombo) service).getName().equals(comboName)) {
				return (ServiceCombo) service;
			}
		}
		throw new NoSuchElementException("Combo not found");
	}

	// @Author Emile Riberdy
	public static boolean cancelAppointment(String name, String date, String time) {
		CarShop carshop = CarShopApplication.getCarShop();
		for (Appointment app : carshop.getAppointments()) {
			if (app.getBookableService().getName().equals(name)) {
				if (app.getServiceBooking(0).getTimeSlot().getStartDate().toString().equals(date)) {
					if (app.getServiceBooking(0).getTimeSlot().getStartTime().toString().equals(time)) {
						if (app.cancel()) {
							CarShopPersistence.save(CarShopApplication.getCarShop());
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	// @Author Emile Riberdy
	public static boolean startAppointment(String name, String date, String time) {
		CarShop carshop = CarShopApplication.getCarShop();
		for (Appointment app : carshop.getAppointments()) {
			if (app.getBookableService().getName().equals(name)) {
				if (app.getServiceBooking(0).getTimeSlot().getStartDate().toString().equals(date)) {
					if (app.getServiceBooking(0).getTimeSlot().getStartTime().toString().equals(time)) {
						if (app.start()) {
							CarShopPersistence.save(CarShopApplication.getCarShop());
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	// @Author Emile Riberdy
	public static boolean appointmentDone(String name, String date, String time) {
		CarShop carshop = CarShopApplication.getCarShop();
		for (Appointment app : carshop.getAppointments()) {
			if (app.getBookableService().getName().equals(name)) {
				if (app.getServiceBooking(0).getTimeSlot().getStartDate().toString().equals(date)) {
					if (app.getServiceBooking(0).getTimeSlot().getStartTime().toString().equals(time)) {
						if (app.done()) {
							CarShopPersistence.save(CarShopApplication.getCarShop());
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	// @Author Emile Riberdy
	public static boolean addOptionalServiceToAppointment(Appointment appointment, String serviceName,
			String startTimeString) throws IOException {
		CarShop carshop = CarShopApplication.getCarShop();

		// Finding service to add
		List<ComboItem> serviceComboList = ((ServiceCombo) (appointment.getBookableService())).getServices();
		Service toAdd = null;
		boolean found = false;
		for (ComboItem s : serviceComboList) {
			if (s.getService().getName().equals(serviceName)) {
				if (s.getMandatory()) {
					throw new IOException("This service is mandatory");
				}
				toAdd = s.getService();
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IOException("Optional service does not exist");
		}

		try {
			// creation of time slot
			Time startTime = Time.valueOf(startTimeString);

			Time endTime = null;
			endTime.setHours((startTime.getHours() + toAdd.getDuration()) / 60);
			endTime.setMinutes((startTime.getMinutes() + toAdd.getDuration() % 60));

			Date appointmentDate = appointment.getServiceBooking(0).getTimeSlot().getStartDate();
			TimeSlot timeslot = new TimeSlot(appointmentDate, startTime, appointmentDate, endTime, carshop);

			ServiceBooking servicebooking = new ServiceBooking(toAdd, timeslot, appointment);

			if (appointment.addServiceBooking(servicebooking)) {
				CarShopPersistence.save(carshop);
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	// @Author Emile Riberdy
	public static boolean removeOptionalServiceFromAppointment(Appointment appointment, String serviceName)
			throws IOException {
		List<ComboItem> serviceComboList = ((ServiceCombo) (appointment.getBookableService())).getServices();
		boolean found1 = false;
		for (ComboItem s : serviceComboList) {
			if (s.getService().getName().equals(serviceName)) {
				if (s.getMandatory()) {
					throw new IOException("This service is mandatory");
				}
				found1 = true;
				break;
			}
		}
		if (!found1) {
			throw new IOException("Optional service does not exist");
		}

		List<ServiceBooking> serviceBookings = appointment.getServiceBookings();
		boolean found2 = false;
		ServiceBooking toRemove = null;
		for (ServiceBooking s : serviceBookings) {
			if (s.getService().getName().equals(serviceName)) {
				found2 = true;
				toRemove = s;
				break;
			}
		}
		if (!found2) {
			throw new IOException("This service is not currently selected in the appointment");
		}

		try {
			if (appointment.removeOptionalService(toRemove)) {
				CarShopPersistence.save(CarShopApplication.getCarShop());
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	// @Author Emile Riberdy
	public static boolean noShow(String name, String date, String time) {
		try {
			CarShop carshop = CarShopApplication.getCarShop();
			for (Appointment app : carshop.getAppointments()) {
				if (app.getBookableService().getName().equals(name)) {
					if (app.getServiceBooking(0).getTimeSlot().getStartDate().toString().equals(date)) {
						if (app.getServiceBooking(0).getTimeSlot().getStartTime().toString().equals(time)) {
							Customer customer = app.getCustomer();
							if (app.noShow()) {
								customer.setNoShow(customer.getNoShow() + 1);
								CarShopPersistence.save(CarShopApplication.getCarShop());
								return true;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return false;
	}

	// @Author cpare
	public static boolean changeAppointmentDateAndTime(Appointment appointment, String date, String time) {
		CarShop carshop = CarShopApplication.getCarShop();
		Date newDate = Date.valueOf(date);
		String[] stringList = time.split(",");
		ArrayList<Time> timeList = new ArrayList<>();
		for (String s : stringList) {
			timeList.add(Time.valueOf(s + ":00"));
		}
		for (TimeSlot t : carshop.getBusiness().getVacations()) {
			if (t.getStartDate().before(newDate) && t.getEndDate().after(newDate)) {
				return false;
			} else if (t.getStartDate().equals(newDate)) {
				for (int i = 0; i < stringList.length; i++) {
					Time endTime = Time.valueOf("00:00:00");
					int duration = appointment.getServiceBooking(i).getService().getDuration();
					endTime.setHours(timeList.get(i).getHours() + ((timeList.get(i).getMinutes() + duration) / 60));
					endTime.setMinutes((timeList.get(i).getMinutes() + duration) % 60);
					if (timeList.get(i).after(t.getStartTime()) || endTime.after(t.getStartTime())) {
						return false;
					}
				}
			} else if (t.getEndDate().equals(newDate)) {
				for (Time ti : timeList) {
					if (ti.before(t.getEndTime())) {
						return false;
					}
				}
			} else {

				for (TimeSlot tim : carshop.getBusiness().getHolidays()) {
					if ((tim.getStartDate().before(newDate) && tim.getEndDate().after(newDate))
							|| tim.getStartDate().equals(newDate) || tim.getEndDate().equals(newDate)) {
						return false;
					}
				}

				if (appointment.changeTime(timeList)) {
					if (appointment.changeDate(newDate)) {
						CarShopPersistence.save(CarShopApplication.getCarShop());
						return true;
					}
				}
			}
		}
		return false;
	}

	// @Author: cpare
	public static void updateBusiness(String name, String address, String phoneNumber, String email) {
		CarShop carshop = CarShopApplication.getCarShop();
		if (!(getCurrentUser() instanceof Owner)) {
			throw new RuntimeException("No permission to update business information");
		} else {
			Business business = carshop.getBusiness();
			if (!name.equals("")) {
				business.setName(name);
			}

			if (!address.equals("")) {
				business.setAddress(address);
			}

			if (!phoneNumber.equals("")) {
				business.setPhoneNumber(phoneNumber);
			}

			if (!email.equals("")) {
				boolean validEmail = false;
				String[] mail = email.split("@");
				if (mail.length == 2) {
					String[] mail2 = mail[1].split("\\.");
					if (mail2.length == 2) {
						if (mail2[1].equals("com") || mail2[1].equals("ca")) {
							validEmail = true;
						}
					}
				}
				if (validEmail) {
					business.setEmail(email);
				} else {
					throw new RuntimeException("Invalid email");
				}
			}
		}
		CarShopPersistence.save(carshop);
	}

	// @Author: cpare
	public static void addBusinessHour(String day, String newStartTime, String newEndTime) {
		CarShop carshop = CarShopApplication.getCarShop();
		if (getCurrentUser() instanceof Owner) {
			Business business = carshop.getBusiness();
			Time startTime = null;
			Time endTime = null;
			if (newStartTime.length() < 8) {
				startTime = Time.valueOf(newStartTime + ":00");
			} else {
				startTime = Time.valueOf(newStartTime);
			}

			if (newEndTime.length() < 8) {
				endTime = Time.valueOf(newEndTime + ":00");
			} else {
				endTime = Time.valueOf(newEndTime);
			}
			if (endTime.before(startTime)) {
				throw new RuntimeException("Start time must be before end time");
			}
			for (BusinessHour bs : business.getBusinessHours()) {
				if (bs.getDayOfWeek().equals(DayOfWeek.valueOf(day))) {
					if (startTime.after(bs.getStartTime()) && startTime.before(bs.getEndTime())) {

						throw new RuntimeException("The business hours cannot overlap");
					}
					if (endTime.after(bs.getStartTime()) && endTime.before(bs.getEndTime())) {
						throw new RuntimeException("The business hours cannot overlap");
					}
				}
			}
			business.addBusinessHour(new BusinessHour(DayOfWeek.valueOf(day), startTime, endTime, carshop));
		} else {
			throw new RuntimeException("No permission to update business information");
		}
		CarShopPersistence.save(carshop);
	}

	// @Author: cpare
	public static void updateBusinessHour(String oldDay, String oldTime, String Day, String newStartTime,
			String newEndTime) {
		CarShop carshop = CarShopApplication.getCarShop();
		Business business = carshop.getBusiness();
		if (!(getCurrentUser() instanceof Owner)) {
			throw new RuntimeException("No permission to update business information");
		}
		Time oldS = Time.valueOf(oldTime + ":00");
		for (BusinessHour bs : business.getBusinessHours()) {
			if (bs.getDayOfWeek().equals(DayOfWeek.valueOf(oldDay))) {
				if (bs.getStartTime().equals(oldS)) {
					BusinessHour old = bs;
					business.removeBusinessHour(old);
					try {
						addBusinessHour(Day, newStartTime, newEndTime);
					} catch (Exception e) {
						business.addBusinessHour(old);
						throw e;
					}
				}
			}
		}
		CarShopPersistence.save(carshop);
	}

	// @Author cpare
	public static void removeBusinessHour(String day, String time) {
		CarShop carshop = CarShopApplication.getCarShop();
		Business business = carshop.getBusiness();
		if (!(getCurrentUser() instanceof Owner)) {
			throw new RuntimeException("No permission to update business information");
		}
		for (BusinessHour bs : business.getBusinessHours()) {
			if (bs.getDayOfWeek().equals(DayOfWeek.valueOf(day))
					&& bs.getStartTime().equals(Time.valueOf(time + ":00"))) {
				business.removeBusinessHour(bs);
				break;
			}
		}
	}

	// @Author cpare
	public static void addSlot(String type, String startDate, String startTime, String endDate, String endTime) {
		if (type.equals("holiday")) {
			addHolidaySlot(startDate, startTime, endDate, endTime);
		} else if (type.equals("vacation")) {
			addVacationSlot(startDate, startTime, endDate, endTime);
		}
	}

	// @Author: cpare
	public static void addHolidaySlot(String startDate, String startTime, String endDate, String endTime) {
		CarShop carshop = CarShopApplication.getCarShop();
		if (!(getCurrentUser() instanceof Owner)) {
			throw new RuntimeException("No permission to update business information");
		} else {
			Business business = carshop.getBusiness();
			Time sT = Time.valueOf(startTime + ":00");
			Time eT = Time.valueOf(endTime + ":00");
			Date sD = Date.valueOf(startDate);
			Date eD = Date.valueOf(endDate);
			if (sD.before(carshop.getSystemDate())) {
				throw new RuntimeException("Holiday cannot start in the past");
			}
			if (sD.after(eD)) {
				throw new RuntimeException("Start time must be before end time");
			}
			TimeSlot ts = new TimeSlot(sD, sT, eD, eT, carshop);
			overlap(ts, "holiday");
			business.addHoliday(ts);
		}
		CarShopPersistence.save(carshop);
	}

	// @Author: cpare
	public static void addVacationSlot(String startDate, String startTime, String endDate, String endTime) {
		CarShop carshop = CarShopApplication.getCarShop();
		if (!(getCurrentUser() instanceof Owner)) {
			throw new RuntimeException("No permission to update business information");
		} else {
			Business business = carshop.getBusiness();
			Time sT = Time.valueOf(startTime + ":00");
			Time eT = Time.valueOf(endTime + ":00");
			Date sD = Date.valueOf(startDate);
			Date eD = Date.valueOf(endDate);
			if (sD.before(carshop.getSystemDate())) {
				throw new RuntimeException("Vacation cannot start in the past");
			}
			if (sD.after(eD)) {
				throw new RuntimeException("Start time must be before end time");
			}
			TimeSlot ts = new TimeSlot(sD, sT, eD, eT, carshop);
			overlap(ts, "vacation");
			business.addVacation(ts);
		}
		CarShopPersistence.save(carshop);
	}

	// @Author cpare
	public static void overlap(TimeSlot t, String type) {
		CarShop carshop = CarShopApplication.getCarShop();
		for (TimeSlot ts : carshop.getBusiness().getVacations()) {
			if (t.getStartDate().after(ts.getStartDate()) && t.getStartDate().before(ts.getEndDate())) {
				if (type.equals("vacation")) {
					throw new RuntimeException("Vacation times cannot overlap");
				} else {
					throw new RuntimeException("Holiday and vacation times cannot overlap ");
				}
			}
			if (t.getEndDate().after(ts.getStartDate()) && t.getEndDate().before(ts.getEndDate())) {
				if (type.equals("vacation")) {
					throw new RuntimeException("Vacation times cannot overlap");
				} else {
					throw new RuntimeException("Holiday and vacation times cannot overlap ");
				}
			}
			if (t.getStartDate().equals(ts.getStartDate())) {
				if (type.equals("vacation")) {
					throw new RuntimeException("Vacation times cannot overlap");
				} else {
					throw new RuntimeException("Holiday and vacation times cannot overlap ");
				}
			}
			if (t.getEndDate().equals(ts.getEndDate())) {
				if (type.equals("vacation")) {
					throw new RuntimeException("Vacation times cannot overlap");
				} else {
					throw new RuntimeException("Holiday and vacation times cannot overlap ");
				}
			}
			if (t.getEndDate().equals(ts.getStartDate())) {
				if (t.getEndTime().after(ts.getStartTime())) {
					if (type.equals("vacation")) {
						throw new RuntimeException("Vacation times cannot overlap");
					} else {
						throw new RuntimeException("Holiday and vacation times cannot overlap ");
					}
				}
			}
			if (t.getStartDate().equals(ts.getEndDate())) {
				if (t.getStartTime().before(ts.getEndTime())) {
					if (type.equals("vacation")) {
						throw new RuntimeException("Vacation times cannot overlap");
					} else {
						throw new RuntimeException("Holiday and vacation times cannot overlap ");
					}
				}
			}
		}
		for (TimeSlot ts : carshop.getBusiness().getHolidays()) {
			if (t.getStartDate().after(ts.getStartDate()) && t.getStartDate().before(ts.getEndDate())) {
				if (type.equals("holiday")) {
					throw new RuntimeException("Holiday times cannot overlap");
				} else {
					throw new RuntimeException("Holiday and vacation times cannot overlap ");
				}
			}
			if (t.getEndDate().after(ts.getStartDate()) && t.getEndDate().before(ts.getEndDate())) {
				if (type.equals("holiday")) {
					throw new RuntimeException("Holiday times cannot overlap");
				} else {
					throw new RuntimeException("Holiday and vacation times cannot overlap ");
				}
			}
			if (t.getStartDate().equals(ts.getStartDate())) {
				if (type.equals("holiday")) {
					throw new RuntimeException("Holiday times cannot overlap");
				} else {
					throw new RuntimeException("Holiday and vacation times cannot overlap ");
				}
			}
			if (t.getEndDate().equals(ts.getEndDate())) {
				if (type.equals("holiday")) {
					throw new RuntimeException("Holiday times cannot overlap");
				} else {
					throw new RuntimeException("Holiday and vacation times cannot overlap ");
				}
			}
			if (t.getEndDate().equals(ts.getStartDate())) {
				if (t.getEndTime().after(ts.getStartTime())) {
					if (type.equals("holiday")) {
						throw new RuntimeException("Holiday times cannot overlap");
					} else {
						throw new RuntimeException("Holiday and vacation times cannot overlap ");
					}
				}
			}
			if (t.getStartDate().equals(ts.getEndDate())) {
				if (t.getStartTime().before(ts.getEndTime())) {
					if (type.equals("holiday")) {
						throw new RuntimeException("Holiday times cannot overlap");
					} else {
						throw new RuntimeException("Holiday and vacation times cannot overlap ");
					}
				}
			}
		}
	}

	// @Author cpare
	public static void updateSlot(String type, String oldDate, String oldStartTime, String startDate, String startTime,
			String endDate, String endTime) {
		CarShop carshop = CarShopApplication.getCarShop();
		Business business = carshop.getBusiness();
		if (!(getCurrentUser() instanceof Owner)) {
			throw new RuntimeException("No permission to update business information");
		}
		if (type.equals("holiday")) {
			for (TimeSlot ts : business.getHolidays()) {
				if (ts.getStartDate().equals(Date.valueOf(oldDate))
						&& ts.getStartTime().equals(Time.valueOf(oldStartTime + ":00"))) {
					TimeSlot oldTs = ts;
					business.removeHoliday(oldTs);
					try {
						addSlot(type, startDate, startTime, endDate, endTime);
					} catch (Exception e) {
						business.addHoliday(oldTs);
						throw e;
					}
				}
			}
		}
		if (type.equals("vacation")) {
			for (TimeSlot ts : business.getVacations()) {
				if (ts.getStartDate().equals(Date.valueOf(oldDate))
						&& ts.getStartTime().equals(Time.valueOf(oldStartTime + ":00"))) {
					TimeSlot oldTs = ts;
					business.removeHoliday(oldTs);
					try {
						addSlot(type, startDate, startTime, endDate, endTime);
					} catch (Exception e) {
						business.addVacation(oldTs);
						throw e;
					}
					break;
				}
			}
		}
		CarShopPersistence.save(carshop);
	}

	// @Author cpare
	public static void deleteSlot(String type, String startDate, String startTime, String endDate, String endTime) {
		CarShop carshop = CarShopApplication.getCarShop();
		Business business = carshop.getBusiness();
		if (!(getCurrentUser() instanceof Owner)) {
			throw new RuntimeException("No permission to update business information");
		} else {
			if (type.equals("vacation")) {
				for (TimeSlot ts : carshop.getBusiness().getVacations()) {
					if (ts.getStartDate().toString().equals(startDate) && ts.getEndDate().toString().equals(endDate)) {
						if (ts.getStartTime().toString().equals(startTime + ":00")
								&& ts.getEndTime().toString().equals(endTime + ":00")) {
							ts.delete();
							break;
						}
					}
				}
			} else {
				for (TimeSlot ts : carshop.getBusiness().getHolidays()) {
					if (ts.getStartDate().toString().equals(startDate) && ts.getEndDate().toString().equals(endDate)) {
						if (ts.getStartTime().toString().equals(startTime + ":00")
								&& ts.getEndTime().toString().equals(endTime + ":00")) {
							ts.delete();
							break;
						}
					}
				}
			}
		}
	}

	public static void setupBusinessInfo(String name, String address, String phone, String email) {
		CarShop carshop = CarShopApplication.getCarShop();
		if (!(getCurrentUser() instanceof Owner)) {
			throw new RuntimeException("No permission to set up business information");
		}
		boolean validEmail = false;
		String[] mail = email.split("@");
		if (mail.length == 2) {
			String[] mail2 = mail[1].split("\\.");
			if (mail2.length == 2) {
				if (mail2[1].equals("com") || mail2[1].equals("ca")) {
					validEmail = true;
				}
			}
		}
		if (validEmail) {
		} else {
			throw new RuntimeException("Invalid email");
		}
		carshop.setBusiness(new Business(name, address, phone, email, carshop));
	}

	public static ArrayList<String> getNonComboServices() {
		CarShop carshop = CarShopApplication.getCarShop();
		ArrayList<String> servicesNon = new ArrayList<>();
		for (BookableService s : carshop.getBookableServices()) {
			if (s instanceof Service) {
				servicesNon.add(s.getName());
			}
		}
		return servicesNon;
	}

	public static ArrayList<String> getComboServicesName() {
		CarShop carshop = CarShopApplication.getCarShop();
		ArrayList<String> services = new ArrayList<>();
		for (BookableService s : carshop.getBookableServices()) {
			if (s instanceof ServiceCombo) {
				services.add(s.getName());
			}
		}
		return services;
	}

	public static String[] getTechniciansName() {
		CarShop carshop = CarShopApplication.getCarShop();
		int size = carshop.getTechnicians().size();
		String[] list = new String[size];
		for (int i = 0; i < size; i++) {
			list[i] = carshop.getTechnicians().get(i).getUsername();
		}
		return list;
	}

	public static Garage getChosenGarage(String TechnicianName) {
		CarShop carshop = CarShopApplication.getCarShop();
		for (Garage g : carshop.getGarages()) {
			if (g.getTechnician().getUsername().equals(TechnicianName)) {
				return g;
			}
		}
		return null;
	}

	public static void deleteCustomer() {
		CarShop carshop = CarShopApplication.getCarShop();
		User customer = getCurrentUser();
		if (customer instanceof Customer) {
			for (Appointment appointment : ((Customer) customer).getAppointments()) {
				appointment.delete();
			}
			customer.delete();
		} else
			throw new RuntimeException("Only customer can delete own account");
	}

	public static boolean isCustomer() {
		User customer = getCurrentUser();
		return customer instanceof Customer;
	}

	public static String getCurrentType() {
		User user = getCurrentUser();

		if (user instanceof Owner) {
			return "owner";
		} else if (user instanceof Technician) {
			return "technician";
		} else if (user instanceof Customer) {
			return "customer";
		} else {
			return null;
		}
	}

	public static TOBusiness getBusinessInfo() {
		CarShop carshop = CarShopApplication.getCarShop();
		if (carshop.getBusiness() == null) {
			return new TOBusiness("", "", "", "", null, null, "", "");
		} else {
			String name = carshop.getBusiness().getName();
			String email = carshop.getBusiness().getEmail();
			String phone = carshop.getBusiness().getPhoneNumber();
			String address = carshop.getBusiness().getAddress();
			HashMap<String, String> startmap = new HashMap<String, String>();
			HashMap<String, String> endmap = new HashMap<String, String>();
			String HolidayString = "";
			String VacationString = "";

			for (TimeSlot holiday : carshop.getBusiness().getHolidays()) {
				if (HolidayString.isEmpty()) {
					HolidayString += "from " + holiday.getStartDate() + " at " + holiday.getStartTime() + " to "
							+ holiday.getEndDate() + " at " + holiday.getEndTime();
				} else {
					HolidayString += " and" + " from " + holiday.getStartDate() + " at " + holiday.getStartTime()
							+ " to " + holiday.getEndDate() + " at " + holiday.getEndTime();
				}
			}
			for (TimeSlot vacation : carshop.getBusiness().getVacations()) {
				if (VacationString.isEmpty()) {
					VacationString += "from " + vacation.getStartDate() + " at " + vacation.getStartTime() + " to "
							+ vacation.getEndDate() + " at " + vacation.getEndTime();
				} else {
					VacationString += " and" + " from " + vacation.getStartDate() + " at " + vacation.getStartTime()
							+ " to " + vacation.getEndDate() + " at " + vacation.getEndTime();
				}
			}

			if (carshop.getBusiness().getBusinessHours() == null) {
				return new TOBusiness(name, email, phone, address, null, null, VacationString, HolidayString);
			} else {
				for (BusinessHour businesshour : carshop.getBusiness().getBusinessHours()) {
					startmap.put(businesshour.getDayOfWeek().toString(), businesshour.getStartTime().toString());
					endmap.put(businesshour.getDayOfWeek().toString(), businesshour.getEndTime().toString());
				}
				return new TOBusiness(name, email, phone, address, startmap, endmap, VacationString, HolidayString);
			}
		}
	}

	// @Author cpare
	public static String[] getAppointmentsByDay(String date) {
		CarShop carshop = CarShopApplication.getCarShop();
		ArrayList<String> temp = new ArrayList<String>();
		Date check = null;
		if (date.equals("")) {
			check = carshop.getSystemDate();
		} else {
			check = Date.valueOf(date);
		}
		for (Appointment app : carshop.getAppointments()) {
			if (app.getServiceBooking(0).getTimeSlot().getStartDate().equals(check)) {
				temp.add(app.getBookableService().getName() + ","
						+ app.getServiceBooking(0).getTimeSlot().getStartDate().toString() + ","
						+ app.getServiceBooking(0).getTimeSlot().getStartTime().toString());
			}
		}
		String[] list = new String[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			list[i] = temp.get(i);
		}
		int count = 0;
		for (String s : list) {
			String[] temp2 = s.split(",");
			String show = temp2[2] + " - " + temp2[0];
			list[count] = show;
			count++;
		}
		return list;
	}

	public static String getDateString() {
		return CarShopApplication.getCarShop().getSystemDate().toString();
	}

	public static String getTimeString() {
		return CarShopApplication.getCarShop().getSystemTime().toString();
	}

	// @Author cpare
	public static void setDateAndTime() {
		CarShop carshop = CarShopApplication.getCarShop();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ',' HH:mm:ss ',' z");
		Date date = new Date(System.currentTimeMillis());
		String[] temp = (formatter.format(date)).split(" , ");
		carshop.setSystemDate(Date.valueOf(temp[0]));
		carshop.setSystemTime(Time.valueOf(temp[1]));
	}

	public static ArrayList<String> getOptionalServicesName(String mainService) {
		CarShop carshop = CarShopApplication.getCarShop();
		ArrayList<String> optionalServices = new ArrayList<>();
		for (BookableService s : carshop.getBookableServices()) {
			if (mainService.equals(s.getName())) {
				if (!(s instanceof ServiceCombo)) {
					return optionalServices;
				}
				for (ComboItem item : ((ServiceCombo) s).getServices()) {
					if (!item.getMandatory()) {
						optionalServices.add(item.getService().getName());
					}
				}
				break;
			}
		}
		return optionalServices;
	}

	// @Author cpare
	public static String[] getGarageHours() {
		CarShop carshop = CarShopApplication.getCarShop();
		User user = getCurrentUser();
		ArrayList<String> temp = new ArrayList<String>();
		if (!(user instanceof Technician)) {

		} else {
			for (Garage g : carshop.getGarages()) {
				if (g.getTechnician().getUsername().equals(user.getUsername())) {
					for (BusinessHour bs : g.getBusinessHours()) {
						temp.add(bs.getDayOfWeek().toString());
						temp.add(bs.getStartTime().toString());
						temp.add(bs.getEndTime().toString());
					}
					break;
				}
			}
		}
		String[] out = new String[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			out[i] = temp.get(i);
		}
		return out;
	}

	// @Author cpare
	public static String[] getGarageBusinessHour(String day) {
		CarShop carshop = CarShopApplication.getCarShop();
		String[] out = { "", "" };
		if (!(getCurrentUser() instanceof Technician)) {

		} else {
			Technician user = (Technician) getCurrentUser();
			for (BusinessHour bs : user.getGarage().getBusinessHours()) {
				if (bs.getDayOfWeek().toString().equals(day)) {
					out[0] = bs.getStartTime().toString().substring(0, 5);
					out[1] = bs.getEndTime().toString().substring(0, 5);
				}
			}
		}
		return out;
	}

	// @Author cpare
	public static String getTechnicianType() {
		Technician user = (Technician) getCurrentUser();
		return user.getUsername().split("-")[0];
	}

	// @Author cpare
	public static String[] getAppointments() {
		CarShop carshop = CarShopApplication.getCarShop();
		Customer cus = (Customer) getCurrentUser();
		String[] list = new String[cus.getAppointments().size()];
		int i = 0;
		for (Appointment appointment : cus.getAppointments()) {
			list[i] = appointment.getBookableService().getName() + " - "
					+ appointment.getServiceBooking(0).getTimeSlot().getStartDate() + " - "
					+ appointment.getServiceBooking(0).getTimeSlot().getStartTime();
		}

		return list;
	}
}
