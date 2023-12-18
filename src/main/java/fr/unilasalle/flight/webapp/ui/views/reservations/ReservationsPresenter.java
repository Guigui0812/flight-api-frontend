package fr.unilasalle.flight.webapp.ui.views.reservations;

import fr.unilasalle.flight.webapp.data.clients.FlightsClient;
import fr.unilasalle.flight.webapp.data.clients.ReservationsClient;
import fr.unilasalle.flight.webapp.data.dtos.FlightDTO;
import fr.unilasalle.flight.webapp.data.dtos.PassengerDTO;
import fr.unilasalle.flight.webapp.data.dtos.PlaneDTO;
import fr.unilasalle.flight.webapp.data.dtos.ReservationDTO;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;

import java.util.ArrayList;
import java.util.List;

public class ReservationsPresenter {
    private final ReservationsClient reservationsClient;

    private final FlightsClient flightsClient;

    private final ReservationsView view;

    public ReservationsPresenter(ReservationsView view) {
        this.reservationsClient = new ReservationsClient();
        this.flightsClient = new FlightsClient();
        this.view = view;
    }

    public List<FlightDTO> loadFlights() {
        var allFlights = flightsClient.getFlights();
        return allFlights == null ? new ArrayList<>() : allFlights;
    }

    private List<ReservationDTO> loadReservations(FlightDTO flight) {
        var allReservations = reservationsClient.getReservations(flight);
        return allReservations == null ? new ArrayList<>() : allReservations;
    }

    public void save(ReservationDTO reservation, FlightDTO flight, PassengerDTO passenger){

        reservation.setFlight(flight);
        reservation.setPassenger(passenger);
        var error = reservationsClient.createReservation(reservation);
        if (error == null){
            view.hideErrorMessage();
            view.openDialog(false);

            view.showNotification(String.format("Reservation for flight number %s has been successfully added", reservation.getFlightNumber()));

            view.form.setReservation(new ReservationDTO());
            view.refreshGrid(loadReservations(reservation.getFlight()));
            return;
        }

        view.showErrorMessage((ErrorWrapperDTO) error);
    }

    protected void displayContent() {
        view.constructContent(loadReservations(new FlightDTO()));
        view.form.setReservation(new ReservationDTO());
    }

    protected void cancel() {
        view.openDialog(false);
    }
}