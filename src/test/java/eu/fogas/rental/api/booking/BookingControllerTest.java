package eu.fogas.rental.api.booking;

import eu.fogas.rental.api.booking.model.BookingRequest;
import eu.fogas.rental.api.booking.model.Country;
import eu.fogas.rental.api.booking.model.Usage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.EnumSet;

import static eu.fogas.rental.api.booking.model.Country.HUNGARY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    private static final Date TODAY = new Date();
    private static final String EMPTY = "";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockitoBean
    private BookingService bookingService;

    @Test
    public void saveBooking_shouldReturnCreatedWithEmptyBody_whenUsageDomesticAndTargetCountriesNull() throws Exception {
        BookingRequest booking = new BookingRequest(1, Usage.DOMESTIC, null, TODAY, TODAY);

        mockMvc.perform(
                        put("/bookings")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isCreated())
                .andExpect(content().string(EMPTY));
    }

    @Test
    public void saveBooking_shouldReturnCreatedWithEmptyBody_whenUsageDomesticAndTargetCountriesEmpty() throws Exception {
        BookingRequest booking = new BookingRequest(1, Usage.DOMESTIC, EnumSet.noneOf(Country.class), TODAY, TODAY);

        mockMvc.perform(
                        put("/bookings")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isCreated())
                .andExpect(content().string(EMPTY));
    }

    @Test
    public void saveBooking_shouldReturnCreatedWithEmptyBody_whenUsageForeignAndTargetCountriesNotEmpty() throws Exception {
        BookingRequest booking = new BookingRequest(1, Usage.FOREIGN, EnumSet.of(HUNGARY), TODAY, TODAY);

        mockMvc.perform(
                        put("/bookings")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isCreated())
                .andExpect(content().string(EMPTY));
    }

    @Test
    public void saveBooking_shouldReturnBadRequestErrorResponse_whenUsageForeignAndTargetCountriesEmpty() throws Exception {
        BookingRequest booking = new BookingRequest(1, Usage.FOREIGN, EnumSet.noneOf(Country.class), TODAY, TODAY);

        mockMvc.perform(
                        put("/bookings")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("message").value("Invalid request!"))
                .andExpect(jsonPath("errors[0].field").value("targetCountries"))
                .andExpect(jsonPath("errors[0].message").value("The field targetCountries is required when usage is foreign!"));
    }

    @Test
    public void saveBooking_shouldReturnBadRequestErrorResponse_whenUsageForeignAndTargetCountriesIsNull() throws Exception {
        BookingRequest booking = new BookingRequest(1, Usage.FOREIGN, null, TODAY, TODAY);

        mockMvc.perform(
                        put("/bookings")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("message").value("Invalid request!"))
                .andExpect(jsonPath("errors[0].field").value("targetCountries"))
                .andExpect(jsonPath("errors[0].message").value("The field targetCountries is required when usage is foreign!"));
    }

    @Test
    public void saveBooking_shouldReturnBadRequestErrorResponse_whenUsageDomesticAndTargetCountriesIsNotEmpty() throws Exception {
        BookingRequest booking = new BookingRequest(1, Usage.DOMESTIC, EnumSet.of(HUNGARY), TODAY, TODAY);

        mockMvc.perform(
                        put("/bookings")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("message").value("Invalid request!"))
                .andExpect(jsonPath("errors[0].field").value("targetCountries"))
                .andExpect(jsonPath("errors[0].message").value("The field targetCountries should be empty or null when usage is domestic!"));
    }

    @Test
    public void saveBooking_shouldReturnUnsupportedMediaTypeErrorResponse_whenRequestContentTypeIsNotJson() throws Exception {
        BookingRequest booking = new BookingRequest(1, Usage.DOMESTIC, null, TODAY, TODAY);

        mockMvc.perform(
                        put("/bookings")
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .content(mapper.writeValueAsString(booking)))

                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("status").value("415 UNSUPPORTED_MEDIA_TYPE"))
                .andExpect(jsonPath("message").value("Unsupported media type: application/octet-stream;charset=UTF-8"));
    }

    @Test
    public void getCarById_shouldReturnMethodNotAllowedErrorResponse_whenMethodIsNotPut() throws Exception {
        mockMvc.perform(
                        get("/bookings"))

                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("status").value("405 METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("message").value("GET method is not supported for this request. Supported methods are PUT "));
    }
}