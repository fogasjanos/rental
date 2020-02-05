package eu.fogas.rental.api.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fogas.rental.api.booking.model.BookingRequest;
import eu.fogas.rental.api.booking.model.Usage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class BookingControllerTest {
    private static final Date TODAY = new Date();
    private static final String EMPTY = "";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;

    @Test
    public void saveBooking_shouldReturnCreatedWithEmptyBody_whenUsageDomesticAndTargetCountriesNull() throws Exception {
        BookingRequest booking = BookingRequest.builder()
                .carId(1)
                .usage(Usage.DOMESTIC)
                .targetCountries(null)
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .build();

        mockMvc.perform(
                put("/bookings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isCreated())
                .andExpect(content().string(EMPTY));
    }

    @Test
    public void saveBooking_shouldReturnCreatedWithEmptyBody_whenUsageDomesticAndTargetCountriesEmpty() throws Exception {
        BookingRequest booking = BookingRequest.builder()
                .carId(1)
                .usage(Usage.DOMESTIC)
                .targetCountries(Collections.emptyList())
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .build();

        mockMvc.perform(
                put("/bookings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isCreated())
                .andExpect(content().string(EMPTY));
    }

    @Test
    public void saveBooking_shouldReturnCreatedWithEmptyBody_whenUsageForeignAndTargetCountriesNotEmpty() throws Exception {
        BookingRequest booking = BookingRequest.builder()
                .carId(1)
                .usage(Usage.FOREIGN)
                .targetCountries(Collections.singletonList("Hungary"))
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .build();

        mockMvc.perform(
                put("/bookings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isCreated())
                .andExpect(content().string(EMPTY));
    }

    @Test
    public void saveBooking_shouldReturnBadRequestErrorResponse_whenUsageForeignAndTargetCountriesEmpty() throws Exception {
        BookingRequest booking = BookingRequest.builder()
                .carId(1)
                .usage(Usage.FOREIGN)
                .targetCountries(Collections.emptyList())
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .build();

        mockMvc.perform(
                put("/bookings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("Invalid request!"))
                .andExpect(jsonPath("errors[0].field").value("targetCountries"))
                .andExpect(jsonPath("errors[0].message").value("The field targetCountries is required when usage is foreign!"));
    }

    @Test
    public void saveBooking_shouldReturnBadRequestErrorResponse_whenUsageForeignAndTargetCountriesIsNull() throws Exception {
        BookingRequest booking = BookingRequest.builder()
                .carId(1)
                .usage(Usage.FOREIGN)
                .targetCountries(null)
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .build();

        mockMvc.perform(
                put("/bookings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("Invalid request!"))
                .andExpect(jsonPath("errors[0].field").value("targetCountries"))
                .andExpect(jsonPath("errors[0].message").value("The field targetCountries is required when usage is foreign!"));
    }

    @Test
    public void saveBooking_shouldReturnBadRequestErrorResponse_whenUsageDomesticAndTargetCountriesIsNotEmpty() throws Exception {
        BookingRequest booking = BookingRequest.builder()
                .carId(1)
                .usage(Usage.DOMESTIC)
                .targetCountries(Collections.singletonList("Hungary"))
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .build();

        mockMvc.perform(
                put("/bookings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("Invalid request!"))
                .andExpect(jsonPath("errors[0].field").value("targetCountries"))
                .andExpect(jsonPath("errors[0].message").value("The field targetCountries should be empty or null when usage is domestic!"));
    }

    @Test
    public void saveBooking_shouldReturnUnsupportedMediaTypeErrorResponse_whenRequestContentTypeIsNotJson() throws Exception {
        BookingRequest booking = BookingRequest.builder()
                .carId(1)
                .usage(Usage.DOMESTIC)
                .targetCountries(null)
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .build();

        mockMvc.perform(
                put("/bookings")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("status").value("UNSUPPORTED_MEDIA_TYPE"))
                .andExpect(jsonPath("message").value("Unsupported media type: application/octet-stream"));
    }

    @Test
    public void getCarById_shouldReturnMethodNotAllowedErrorResponse_whenMethodIsNotPut() throws Exception {
        mockMvc.perform(
                get("/bookings"))

                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("message").value("GET method is not supported for this request. Supported methods are PUT "));
    }
}