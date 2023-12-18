package fr.unilasalle.flight.webapp.data.clients;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import fr.unilasalle.flight.webapp.data.constants.Constants;
import fr.unilasalle.flight.webapp.data.dtos.PlaneDTO;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;
import jakarta.enterprise.context.Dependent;

import java.util.List;

@Dependent
public class PlanesClient {
    private final Gson gson = new Gson();

    public List<PlaneDTO> getPlanes() {
        try {
            HttpResponse<String> response = Unirest.get(Constants.API_BASE_URL + "/planes").asString();
            return gson.fromJson(response.getBody(), new TypeToken<List<PlaneDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ErrorWrapperDTO createPlane(PlaneDTO plane) {
        try {
            String jsonBody = gson.toJson(plane);
            HttpResponse<String> response = Unirest.post(Constants.API_BASE_URL + "/planes")
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asString();

            return gson.fromJson(response.getBody(), ErrorWrapperDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}