package fr.unilasalle.flight.webapp.ui.views.planes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.unilasalle.flight.webapp.MainLayout;
import fr.unilasalle.flight.webapp.data.dtos.PlaneDTO;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;

import java.util.List;

/**
 * The main view contains a button and a click listener.
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Planes")
public class PlanesView extends VerticalLayout {
    private final PlanesPresenter presenter;

    private final Grid<PlaneDTO> grid = new Grid<>(PlaneDTO.class, false);

    private final Button addBtn = new Button("Add", VaadinIcon.PLUS_CIRCLE_O.create());

    private final Dialog dialog = new Dialog();

    protected PlaneForm form = new PlaneForm();

    public PlanesView() {
        this.setSizeFull();

        this.setPadding(false);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);

        presenter = new PlanesPresenter(this);
        presenter.displayContent();
    }

    protected void constructContent(List<PlaneDTO> planes) {
        grid.setItems(planes);

        grid.addColumn(PlaneDTO::getOperator).setHeader("Operator").setAutoWidth(true);
        grid.addColumn(PlaneDTO::getModel).setHeader("Model").setAutoWidth(true);
        grid.addColumn(PlaneDTO::getRegistration).setHeader("Registration").setAutoWidth(true);
        grid.addColumn(PlaneDTO::getCapacity).setHeader("Capacity").setAutoWidth(true);

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

        form.addSaveListener(saveEvent -> presenter.save(saveEvent.getPlane()));
        form.addCancelListener(cancelEvent -> presenter.cancel());

        dialog.setHeaderTitle("New Plane");
        dialog.add(form);
        dialog.getFooter().add(form.getButtons());

        this.add(dialog);
    }

    protected void refreshGrid(List<PlaneDTO> planes) {
        grid.setItems(planes);
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
