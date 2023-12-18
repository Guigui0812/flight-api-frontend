package fr.unilasalle.flight.webapp.ui.views.flights;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import fr.unilasalle.flight.webapp.data.dtos.FlightDTO;
import fr.unilasalle.flight.webapp.data.dtos.PlaneDTO;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;
import fr.unilasalle.flight.webapp.ui.components.ErrorMessage;
import lombok.Getter;

import java.util.List;

public class FlightForm extends FormLayout {
    private final TextField number = new TextField("Number");

    private final TextField origin = new TextField("Origin");

    private final TextField destination = new TextField("Destination");

    private final DatePicker departureDate = new DatePicker("Departure Date");

    private final TimePicker departureTime = new TimePicker("Departure Time");

    private final DatePicker arrivalDate = new DatePicker("Arrival Date");

    private final TimePicker arrivalTime = new TimePicker("Arrival Time");

    private final ComboBox<PlaneDTO> plane = new ComboBox<>("Plane");

    private final Binder<FlightDTO> binder = new Binder<>(FlightDTO.class);

    private final ErrorMessage errorMessage = new ErrorMessage();

    private final Button save = new Button("Add", VaadinIcon.CHECK_CIRCLE_O.create());

    private final Button cancel = new Button("Cancel", VaadinIcon.CLOSE_CIRCLE_O.create());

    public FlightForm(List<PlaneDTO> planes) {
        constructUI();

        plane.setItems(planes);
        plane.setItemLabelGenerator(PlaneDTO::getRegistration);

        binder.bindInstanceFields(this);
    }

    private void constructUI() {
        this.add(number, origin, destination, departureDate, departureTime, arrivalDate, arrivalTime, plane, errorMessage);
        errorMessage.setVisible(false);
        initButtons();
    }

    private void initButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> fireEvent(new SaveEvent(this, binder.getBean())));
        cancel.addClickListener(event -> fireEvent(new CancelEvent(this)));
    }

    public void setFlight(FlightDTO flight) {
        binder.setBean(flight);
    }

    public FlightDTO getFlight() {
        return binder.getBean();
    }


    @Getter
    public abstract static class FlightFormEvent extends ComponentEvent<FlightForm> {
        private final FlightDTO flight;

        protected FlightFormEvent(FlightForm source, FlightDTO flight) {
            super(source, false);
            this.flight = flight;
        }
    }

    public static class SaveEvent extends FlightFormEvent {
        SaveEvent(FlightForm source, FlightDTO flight) {
            super(source, flight);
        }
    }

    public static class CancelEvent extends FlightFormEvent {
        CancelEvent(FlightForm source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
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
