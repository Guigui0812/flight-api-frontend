package fr.unilasalle.flight.webapp.ui.views.flights;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.unilasalle.flight.webapp.MainLayout;
import fr.unilasalle.flight.webapp.data.dtos.FlightDTO;
import fr.unilasalle.flight.webapp.data.dtos.PlaneDTO;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;
import fr.unilasalle.flight.webapp.ui.components.MasterDetailLayout;

import java.util.List;

@Route(value = "flights", layout = MainLayout.class)
@PageTitle("Flights")
public class FlightsView extends MasterDetailLayout {
    private final FlightsPresenter presenter;

    private final Grid<FlightDTO> grid = new Grid<>(FlightDTO.class, false);

    private final Button addBtn = new Button("Add", VaadinIcon.PLUS_CIRCLE_O.create());

    private final VerticalLayout formLayout = new VerticalLayout();

    protected FlightForm form;

    public FlightsView() {
        this.setHeightFull();

        presenter = new FlightsPresenter(this);
        presenter.displayContent();
    }

    protected void constructContent(List<FlightDTO> flights, List<PlaneDTO> planes) {
        grid.setItems(flights);

        grid.addColumn(FlightDTO::getNumber).setHeader("Number").setAutoWidth(true);
        grid.addColumn(FlightDTO::getOrigin).setHeader("Origin").setAutoWidth(true);
        grid.addColumn(FlightDTO::getDestination).setHeader("Destination").setAutoWidth(true);
        grid.addColumn(FlightDTO::getDepartureDateTime).setHeader("Departure").setAutoWidth(true);
        grid.addColumn(FlightDTO::getArrivalDateTime).setHeader("Arrival").setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(flightDTO -> {
            var button = new Button(VaadinIcon.TRASH.create());
            button.addThemeVariants(ButtonVariant.LUMO_ERROR);
            button.addClickListener(e -> presenter.deleteFlight(flightDTO));
            return button;
        })).setAutoWidth(true);

        this.setGrid(grid);

        addBtn.addClickListener(e -> displayForm(true));

        this.setGridButtons(addBtn);

        form = new FlightForm(planes);
        form.setHeightFull();

        form.addSaveListener(saveEvent -> presenter.save(saveEvent.getFlight()));
        form.addCancelListener(closeEvent -> presenter.cancel());

        this.setForm(form);
        this.setFormButtons(form.getButtons());
    }

    protected void refreshGrid(List<FlightDTO> flights) {
        grid.setItems(flights);
    }

    protected void displayForm(boolean display) {
        formLayout.setVisible(display);
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
