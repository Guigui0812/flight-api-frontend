package fr.unilasalle.flight.webapp.data.dtos;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReservationDTO implements Serializable {
    private Long id;

    private FlightDTO flight;

    private PassengerDTO passenger;


    public String getFlightNumber() {
        return flight.getNumber();
    }

    public String getPassengerName() {
        return String.format("%s %s", passenger.getSurname(), passenger.getFirstName());
    }

    // Gson TypeAdapters for LocalDate and LocalTime
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    // Deserialize from JSON
    public static ReservationDTO fromJson(String json) {
        return gson.fromJson(json, ReservationDTO.class);
    }

    // Serialize to JSON
    public String toJson() {
        return gson.toJson(this);
    }

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(src));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
    }

    private static class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        @Override
        public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(src));
        }

        @Override
        public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalTime.parse(json.getAsString(), formatter);
        }
    }
}