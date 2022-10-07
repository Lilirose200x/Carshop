package ca.mcgill.ecse.carshop.persistence;

import ca.mcgill.ecse.carshop.model.CarShop;

public class CarShopPersistence {
    private static String filename = "data.carshop";

    public static void setFilename(String filename) {
        CarShopPersistence.filename = filename;
    }

    public static void save(CarShop carshop) {
        PersistenceObjectStream.serialize(carshop);
    }

    public static CarShop load() {
        PersistenceObjectStream.setFilename(filename);
        CarShop carshop = (CarShop) PersistenceObjectStream.deserialize();
        if (carshop == null) {
            carshop = new CarShop();
        }
        return carshop;
    }
}
