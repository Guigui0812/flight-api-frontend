package fr.unilasalle.flight.webapp.data.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import fr.unilasalle.flight.webapp.data.constants.Constants;
import fr.unilasalle.flight.webapp.data.dtos.FlightDTO;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FlightsClient {

    private final Gson gson;

    public FlightsClient() {
        // Create a Gson instance with custom TypeAdapters for LocalDate and LocalTime
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .create();
    }

    public List<FlightDTO> getFlights() {
        try {
            // Perform a GET request and get the response as a String
            String jsonResponse = Unirest.get(Constants.API_BASE_URL + "/flights")
                    .asString().getBody();

            // Convert the JSON string to List<FlightDTO> using Gson
            return gson.fromJson(jsonResponse, new TypeToken<List<FlightDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ErrorWrapperDTO createFlight(FlightDTO flight) {
        try {
            // Perform a POST request with Gson for serialization
            System.out.println(flight);
            String jsonBody = gson.toJson(flight);
            HttpResponse<String> response = Unirest.post(Constants.API_BASE_URL + "/flights")
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asString();
            return gson.fromJson(String.valueOf(response.getStatus()), ErrorWrapperDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ErrorWrapperDTO();
    }

    public ErrorWrapperDTO deleteFlight(FlightDTO flight) {
        try {
            // Perform a DELETE request
            HttpResponse<String> response = Unirest.delete(Constants.API_BASE_URL + "/flights/{id}")
                    .routeParam("id", flight.getId().toString())
                    .asString();

            // Parse the response JSON using Gson
            return gson.fromJson(response.getBody(), ErrorWrapperDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ErrorWrapperDTO();
    }

    public FlightDTO getFlight(String flightNumber) {
        try {
            // Perform a GET request and get the response as a String
            String jsonResponse = Unirest.get(Constants.API_BASE_URL + "/flights/{flightNumber}")
                    .routeParam("flightNumber", flightNumber)
                    .asString().getBody();

            // Convert the JSON string to List<FlightDTO> using Gson
            return gson.fromJson(jsonResponse, FlightDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new FlightDTO();
    }

    // Custom TypeAdapter for LocalDate
    private static class LocalDateTypeAdapter implements com.google.gson.JsonSerializer<LocalDate>,
            com.google.gson.JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(com.google.gson.JsonElement json, Type typeOfT,
                                     com.google.gson.JsonDeserializationContext context) {
            return LocalDate.parse(json.getAsString());
        }

        @Override
        public com.google.gson.JsonElement serialize(LocalDate src, Type typeOfSrc,
                                                     com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src.toString());
        }
    }

    // Custom TypeAdapter for LocalTime
    private static class LocalTimeTypeAdapter implements com.google.gson.JsonSerializer<LocalTime>,
            com.google.gson.JsonDeserializer<LocalTime> {

        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        @Override
        public LocalTime deserialize(com.google.gson.JsonElement json, Type typeOfT,
                                     com.google.gson.JsonDeserializationContext context) {
            return LocalTime.parse(json.getAsString(), formatter);
        }

        @Override
        public com.google.gson.JsonElement serialize(LocalTime src, Type typeOfSrc,
                                                     com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src.format(formatter));
        }
    }
}
