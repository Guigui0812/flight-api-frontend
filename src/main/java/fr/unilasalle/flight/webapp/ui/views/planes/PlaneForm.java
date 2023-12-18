package fr.unilasalle.flight.webapp.ui.views.planes;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import fr.unilasalle.flight.webapp.data.dtos.PlaneDTO;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;
import fr.unilasalle.flight.webapp.ui.components.ErrorMessage;
import lombok.Getter;

public class PlaneForm extends FormLayout {
    private TextField operator = new TextField("Operator");

    private TextField model = new TextField("Model");

    private TextField registration = new TextField("Registration");

    private TextField capacity = new TextField("Capacity");

    private final Binder<PlaneDTO> binder = new Binder<>(PlaneDTO.class);

    private final ErrorMessage errorMessage = new ErrorMessage();

    private final Button save = new Button("Add", VaadinIcon.CHECK_CIRCLE_O.create());

    private final Button cancel = new Button("Cancel", VaadinIcon.CLOSE_CIRCLE_O.create());

    public PlaneForm() {
        constructUI();
        binder.bindInstanceFields(this);
    }

    private void constructUI() {
        this.add(operator, model, registration, capacity, errorMessage);
        errorMessage.setVisible(false);
        initButtons();
    }

    private void initButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> fireEvent(new PlaneForm.SaveEvent(this, binder.getBean())));
        cancel.addClickListener(event -> fireEvent(new PlaneForm.CancelEvent(this)));
    }

    public void setPlane(PlaneDTO plane) {
        binder.setBean(plane);
    }

    public PlaneDTO getPlane() {
        return binder.getBean();
    }

    @Getter
    public abstract static class PlaneFormEvent extends ComponentEvent<PlaneForm> {
        private final PlaneDTO plane;

        protected PlaneFormEvent(PlaneForm source, PlaneDTO plane) {
            super(source, false);
            this.plane = plane;
        }
    }

    public static class SaveEvent extends PlaneForm.PlaneFormEvent {
        SaveEvent(PlaneForm source, PlaneDTO plane) {
            super(source, plane);
        }
    }

    public static class CancelEvent extends PlaneForm.PlaneFormEvent {
        CancelEvent(PlaneForm source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<PlaneForm.SaveEvent> listener) {
        return addListener(PlaneForm.SaveEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<PlaneForm.CancelEvent> listener) {
        return addListener(PlaneForm.CancelEvent.class, listener);
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
