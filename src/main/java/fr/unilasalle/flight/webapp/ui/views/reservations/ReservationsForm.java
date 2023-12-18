package fr.unilasalle.flight.webapp.ui.views.reservations;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import fr.unilasalle.flight.webapp.data.dtos.FlightDTO;
import fr.unilasalle.flight.webapp.data.dtos.PassengerDTO;
import fr.unilasalle.flight.webapp.data.dtos.ReservationDTO;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;
import fr.unilasalle.flight.webapp.ui.components.ErrorMessage;
import lombok.Getter;

import java.util.List;

public class ReservationsForm extends FormLayout {

    private final TextField firstName = new TextField("First Name");

    private final TextField lastName = new TextField("Last Name");

    private final TextField email = new TextField("Email");

    private final ComboBox<FlightDTO> flight = new ComboBox<>("Flight");

    private final ErrorMessage errorMessage = new ErrorMessage();

    private final Button save = new Button("Add", VaadinIcon.CHECK_CIRCLE_O.create());

    private final Button cancel = new Button("Cancel", VaadinIcon.CLOSE_CIRCLE_O.create());

    private final Binder<ReservationDTO> binder = new Binder<>(ReservationDTO.class);

    public ReservationsForm(List<FlightDTO> flights) {

        flight.setItems(flights);
        flight.setItemLabelGenerator(FlightDTO::getNumber);
        constructUI();
        binder.bindInstanceFields(this);
    }

    private void constructUI() {
        this.add(firstName, lastName, email, flight, errorMessage);
        errorMessage.setVisible(false);
        initButtons();
    }

    private void initButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> fireEvent(new ReservationsForm.SaveEvent(this, binder.getBean())));
        cancel.addClickListener(event -> fireEvent(new ReservationsForm.CancelEvent(this)));
    }

    public void setReservation(ReservationDTO reservation) {
        binder.setBean(reservation);
    }

    public ReservationDTO getReservation() {
        return binder.getBean();
    }

    @Getter
    public abstract static class ReservationsFormEvent extends ComponentEvent<ReservationsForm> {
        private final ReservationDTO reservation;

        protected ReservationsFormEvent(ReservationsForm source, ReservationDTO reservation) {
            super(source, false);
            this.reservation = reservation;
        }
    }

    public static class SaveEvent extends ReservationsForm.ReservationsFormEvent {
        SaveEvent(ReservationsForm source, ReservationDTO reservation) {

            super(source, reservation);

            PassengerDTO passenger = new PassengerDTO();
            passenger.setId(1L);
            passenger.setFirstName(source.firstName.getValue());
            passenger.setSurname(source.lastName.getValue());
            passenger.setEmail(source.email.getValue());

            reservation.setPassenger(passenger);
        }
    }

    public static class CancelEvent extends ReservationsForm.ReservationsFormEvent {
        CancelEvent(ReservationsForm source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<ReservationsForm.SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<ReservationsForm.CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }

    public FlightDTO getFlightNumber() {
        return flight.getValue();
    }

    public void displayError(ErrorWrapperDTO errorWrapper) {
        errorMessage.setVisible(true);
        errorMessage.setErrorWrapper(errorWrapper);
    }

    public void hideError() {
        errorMessage.setVisible(false);
    }

    public Button[] getButtons() {
        return new Button[]{save, cancel};
    }
}
