package eu.fogas.rental.api.car;

import eu.fogas.rental.api.car.model.Brand;
import eu.fogas.rental.api.car.model.Car;
import eu.fogas.rental.error.exception.CarNotAvailableException;
import eu.fogas.rental.error.exception.CarNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class CarControllerTest {
    private static final Car SKODA = Car.builder()
            .carId(13)
            .brand(Brand.SKODA)
            .plate("SKO-116")
            .price(44_555L)
            .model("Octavia")
            .build();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarService carService;

    @Test
    public void getCars_shouldReturnEmptyList_whenNoCarsAvailable() throws Exception {
        mockMvc.perform(get("/cars"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getCars_shouldReturnListWithCars_whenCarsAvailable() throws Exception {
        when(carService.getAvailableCars()).thenReturn(Collections.singletonList(SKODA));

        mockMvc.perform(get("/cars"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].carId").value(SKODA.getCarId()))
                .andExpect(jsonPath("$.[0].brand").value(SKODA.getBrand().getLabel()))
                .andExpect(jsonPath("$.[0].plate").value(SKODA.getPlate()))
                .andExpect(jsonPath("$.[0].model").value(SKODA.getModel()))
                .andExpect(jsonPath("$.[0].price").value(SKODA.getPrice()));
    }

    @Test
    public void getCarById_shouldReturnCar_whenCarAvailable() throws Exception {
        long carId = SKODA.getCarId();
        when(carService.getCarById(carId)).thenReturn(SKODA);

        mockMvc.perform(get("/cars/" + carId))

                .andExpect(status().isOk())
                .andExpect(jsonPath("carId").value(SKODA.getCarId()))
                .andExpect(jsonPath("brand").value(SKODA.getBrand().getLabel()))
                .andExpect(jsonPath("plate").value(SKODA.getPlate()))
                .andExpect(jsonPath("model").value(SKODA.getModel()))
                .andExpect(jsonPath("price").value(SKODA.getPrice()));
    }

    @Test
    public void getCarById_shouldReturnConflictErrorResponse_whenCarNotAvailable() throws Exception {
        Long unavailableId = -1L;
        doThrow(new CarNotAvailableException(unavailableId)).when(carService).getCarById(anyLong());

        mockMvc.perform(get("/cars/999"))

                .andExpect(status().isConflict())
                .andExpect(jsonPath("status").value("CONFLICT"))
                .andExpect(jsonPath("message").value("Car not available. Id: " + unavailableId));
    }

    @Test
    public void getCarById_shouldReturnNotFoundErrorResponse_whenCarNotFound() throws Exception {
        Long missingId = -1L;
        doThrow(new CarNotFoundException(missingId)).when(carService).getCarById(anyLong());

        mockMvc.perform(get("/cars/999"))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value("NOT_FOUND"))
                .andExpect(jsonPath("message").value("Car not found. Id: " + missingId));
    }

    @Test
    public void getCarById_shouldReturnMethodNotAllowedErrorResponse_whenMethodIsNotGet() throws Exception {
        mockMvc.perform(post("/cars/999"))

                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("message").value("POST method is not supported for this request. Supported methods are GET "));
    }


    @Test
    public void getCarById_shouldReturnBadRequestErrorResponse_whenIdIsNotNumber() throws Exception {
        mockMvc.perform(get("/cars/alma"))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("Error code: 'typeMismatch'. Required type: 'long'. Value: 'alma'."));
    }
}