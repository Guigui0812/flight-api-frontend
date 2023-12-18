package fr.unilasalle.flight.webapp.data.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.unilasalle.flight.webapp.data.constants.Constants;
import fr.unilasalle.flight.webapp.data.dtos.FlightDTO;
import fr.unilasalle.flight.webapp.data.dtos.ReservationDTO;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;
import kong.unirest.core.GenericType;
import kong.unirest.core.Unirest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReservationsClient {

    private final Gson gson;

    public ReservationsClient() {
        // Créez un objet Gson avec des adaptateurs personnalisés pour LocalDate et LocalTime
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .create();
    }

    public List<ReservationDTO> getReservations(FlightDTO flight) {
        List<ReservationDTO> reservations = new ArrayList<>();

        try {
            String endpoint = Constants.API_BASE_URL + "/reservations?flightId=" + flight.getId();
            List<ReservationDTO> response = Unirest.get(endpoint)
                    .asObject(new GenericType<List<ReservationDTO>>() {})
                    .getBody();

            reservations.addAll(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public boolean deleteReservation(ReservationDTO reservation) {
        try {
            String endpoint = Constants.API_BASE_URL + "/reservations/" + reservation.getId();
            Unirest.delete(endpoint).asString();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object createReservation(ReservationDTO reservation) {
        try {
            String jsonBody = gson.toJson(reservation);
            System.out.println(jsonBody);
            String endpoint = Constants.API_BASE_URL + "/reservations";
            ErrorWrapperDTO response = Unirest.post(endpoint)
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asObject(ErrorWrapperDTO.class)
                    .getBody();

            if (response.getErrors().isEmpty()){
                return null;
            }

            System.out.println(response.getErrors());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Adaptateur de type personnalisé pour LocalDate
    private static class LocalDateTypeAdapter implements com.google.gson.JsonSerializer<LocalDate>,
            com.google.gson.JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT,
                                     com.google.gson.JsonDeserializationContext context) {
            return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        }

        @Override
        public com.google.gson.JsonElement serialize(LocalDate src, java.lang.reflect.Type typeOfSrc,
                                                     com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }

    // Adaptateur de type personnalisé pour LocalTime
    private static class LocalTimeTypeAdapter implements com.google.gson.JsonSerializer<LocalTime>,
            com.google.gson.JsonDeserializer<LocalTime> {

        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        @Override
        public LocalTime deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT,
                                     com.google.gson.JsonDeserializationContext context) {
            return LocalTime.parse(json.getAsString(), formatter);
        }

        @Override
        public com.google.gson.JsonElement serialize(LocalTime src, java.lang.reflect.Type typeOfSrc,
                                                     com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src.format(formatter));
        }
    }
}
