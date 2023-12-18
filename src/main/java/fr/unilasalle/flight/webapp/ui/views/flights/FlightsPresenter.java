package fr.unilasalle.flight.webapp.ui.views.flights;

import fr.unilasalle.flight.webapp.data.clients.FlightsClient;
import fr.unilasalle.flight.webapp.data.clients.PlanesClient;
import fr.unilasalle.flight.webapp.data.dtos.FlightDTO;
import fr.unilasalle.flight.webapp.data.dtos.PlaneDTO;

import java.util.ArrayList;
import java.util.List;

public class FlightsPresenter {
    private final FlightsClient flightsClient;

    private final PlanesClient planesClient;

    private final FlightsView view;

    public FlightsPresenter(FlightsView view) {
        this.flightsClient = new FlightsClient();
        this.planesClient = new PlanesClient();
        this.view = view;
    }

    private List<FlightDTO> loadFlights() {
        var allFlights = flightsClient.getFlights();
        return allFlights == null ? new ArrayList<>() : allFlights;
    }

    private List<PlaneDTO> loadPlanes() {
        var allPlanes = planesClient.getPlanes();
        return allPlanes == null ? new ArrayList<>() : allPlanes;
    }

    protected void displayContent() {
        view.constructContent(loadFlights(), loadPlanes());
        view.form.setFlight(new FlightDTO());
        view.displayForm(false);
    }

    protected void deleteFlight(FlightDTO flight) {
        var error = flightsClient.deleteFlight(flight);
        if (error == null) {
            view.refreshGrid(loadFlights());
        } else {
            view.showNotification(String.format("Error while deleting flight : %s", error.getMessage()));
        }
    }

    protected void save(FlightDTO flight) {
        var error = flightsClient.createFlight(flight);
        if (error == null) {
            view.hideErrorMessage();
            view.displayForm(false);

            view.showNotification(String.format("Flight number %s has been successfully added", flight.getNumber()));

            view.form.setFlight(new FlightDTO());
            view.refreshGrid(loadFlights());

            return;
        }
        view.showErrorMessage(error);
    }

    protected void cancel() {
        view.displayForm(false);
    }
}
