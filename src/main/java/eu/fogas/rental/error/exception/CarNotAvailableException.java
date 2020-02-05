package eu.fogas.rental.error.exception;

public class CarNotAvailableException extends RentalBaseException {

    public CarNotAvailableException(Long carId) {
        super("Car not available. Id: " + carId);
    }
}
