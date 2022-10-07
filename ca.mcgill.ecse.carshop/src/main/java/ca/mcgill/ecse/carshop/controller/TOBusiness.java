package ca.mcgill.ecse.carshop.controller;

import java.util.HashMap;

public class TOBusiness {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String[] startTimes;
    private String[] endTimes;
    private HashMap<String,String> startmap;
    private HashMap<String,String> endmap;
    private String Vacation;
    private String Holiday;

    TOBusiness(String name, String email, String phone, String address, HashMap<String, String> startmap, HashMap<String, String> endmap, String Vacation, String Holiday){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.startmap = startmap;
        this.endmap = endmap;
        this.Vacation = Vacation;
        this.Holiday = Holiday;

    }

    public String getName() {
        return name;
    }

    public HashMap<String,String> getStartMap() {
        return startmap;
    }

    public HashMap<String,String> getEndMap() {
        return endmap;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getVacation() {
        return Vacation;
    }

    public String getHoliday() {
        return Holiday;
    }

}