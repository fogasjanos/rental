package eu.fogas.rental.error.exception;

public class CarNotFoundException extends RentalBaseException {

    public CarNotFoundException(Long carId) {
        super("Car not found. Id: " + carId);
    }
}
