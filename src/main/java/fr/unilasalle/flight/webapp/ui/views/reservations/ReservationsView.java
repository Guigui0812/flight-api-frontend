package fr.unilasalle.flight.webapp.ui.views.reservations;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.unilasalle.flight.webapp.MainLayout;
import fr.unilasalle.flight.webapp.data.dtos.FlightDTO;
import fr.unilasalle.flight.webapp.data.dtos.PlaneDTO;
import fr.unilasalle.flight.webapp.data.dtos.ReservationDTO;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;
import fr.unilasalle.flight.webapp.ui.views.planes.PlaneForm;

import java.util.List;

@Route(value = "reservations", layout = MainLayout.class)
@PageTitle("Reservations")
public class ReservationsView extends VerticalLayout {
    private final ReservationsPresenter presenter;

    private final Grid<ReservationDTO> grid = new Grid<>(ReservationDTO.class, false);

    private final Button addBtn = new Button("Add", VaadinIcon.PLUS_CIRCLE_O.create());

    private final Dialog dialog = new Dialog();

    protected ReservationsForm form;

    public ReservationsView() {

        this.setSizeFull();

        this.setPadding(false);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);

        presenter = new ReservationsPresenter(this);
        form = new ReservationsForm(presenter.loadFlights());
        presenter.displayContent();
    }

    protected void constructContent(List<ReservationDTO> reservations) {
        grid.setItems(reservations);

        grid.addColumn(ReservationDTO::getPassengerName).setHeader("Passenger Name").setAutoWidth(true);
        grid.addColumn(ReservationDTO::getFlightNumber).setHeader("Flight Number").setAutoWidth(true);

        this.add(grid);

        var buttonBar = new HorizontalLayout();
        buttonBar.addClassName("button-layout");

        addBtn.addClickListener(e -> openDialog(true));

        buttonBar.add(addBtn);

        this.add(buttonBar);

        dialog.setHeight("50%");
        dialog.setWidth("50%");

        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);

        form.addSaveListener(saveEvent -> presenter.save(saveEvent.getReservation(), saveEvent.getReservation().getFlight(), saveEvent.getReservation().getPassenger()));
        form.addCancelListener(cancelEvent -> presenter.cancel());

        dialog.setHeaderTitle("New Plane");
        dialog.add(form);
        dialog.getFooter().add(form.getButtons());

        this.add(dialog);
    }

    protected void refreshGrid(List<ReservationDTO> reservations) {
        grid.setItems(reservations);
    }

    protected void openDialog(boolean opened) {
        dialog.setOpened(opened);
    }

    protected void showNotification(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    protected void showErrorMessage(ErrorWrapperDTO errorWrapper) {
        form.displayError(errorWrapper);
    }

    protected void hideErrorMessage() {
        form.hideError();
    }

}
