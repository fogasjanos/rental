package eu.fogas.rental.api.car;

import eu.fogas.rental.api.car.model.Brand;
import eu.fogas.rental.api.car.model.Car;
import eu.fogas.rental.error.exception.CarNotAvailableException;
import eu.fogas.rental.error.exception.CarNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCarServiceTest {
    private static final LocalDate TODAY = LocalDate.now();
    private static final Car CAR = Car.builder()
            .carId(16)
            .brand(Brand.OPEL)
            .plate("BND-007")
            .build();

    @InjectMocks
    private DefaultCarService carService;
    @Mock
    private CarRepository carRepository;

    @Test(expected = CarNotFoundException.class)
    public void getCarById_shouldThrowCarNotFoundException_whenCarDoesNotExists() {
        long nonExistingId = 123_536L;
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

        carService.getCarById(nonExistingId);
    }

    @Test(expected = CarNotAvailableException.class)
    public void getCarById_shouldThrowCarNotAvailableException_whenCarAlreadyBooked() {
        long carId = CAR.getCarId();
        when(carRepository.findById(carId)).thenReturn(Optional.of(CAR));
        when(carRepository.findAvailableCarById(carId, TODAY)).thenReturn(Optional.empty());

        carService.getCarById(carId);
    }

    @Test
    public void getCarById_shouldReturnCar_whenCarIsAvailable() {
        long carId = CAR.getCarId();
        when(carRepository.findById(carId)).thenReturn(Optional.of(CAR));
        when(carRepository.findAvailableCarById(carId, TODAY)).thenReturn(Optional.of(CAR));

        Car result = carService.getCarById(carId);

        assertEquals(CAR, result);
        verify(carRepository).findById(carId);
        verify(carRepository).findAvailableCarById(carId, TODAY);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void getAvailableCars_shouldCallCarRepositoryFindAllAvailableCarsMethod() {
        when(carRepository.findAllAvailableCars(any())).thenReturn(Collections.emptyList());

        List<Car> result = carService.getAvailableCars();

        assertTrue(result.isEmpty());
        verify(carRepository).findAllAvailableCars(LocalDate.now());
        verifyNoMoreInteractions(carRepository);
    }
}