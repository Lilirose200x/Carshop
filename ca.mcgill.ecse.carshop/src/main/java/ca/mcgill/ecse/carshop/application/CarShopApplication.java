
package ca.mcgill.ecse.carshop.application;


import ca.mcgill.ecse.carshop.model.CarShop;

import ca.mcgill.ecse.carshop.persistence.CarShopPersistence;
import ca.mcgill.ecse.carshop.view.LoginWindow;


public class CarShopApplication {

    private static CarShop carshop;
    public static LoginWindow loginWindow;

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        loginWindow = new LoginWindow();
        System.out.println(new CarShopApplication().getGreeting());
    }

    public static CarShop getCarShop() {
        if (carshop == null) {
            //load carshop system from the disk
            carshop = CarShopPersistence.load();
        }
        return carshop;
    }
}