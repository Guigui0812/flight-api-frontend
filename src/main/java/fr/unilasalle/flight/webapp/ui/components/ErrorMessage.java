package fr.unilasalle.flight.webapp.ui.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import fr.unilasalle.flight.webapp.data.dtos.error.ErrorWrapperDTO;

public class ErrorMessage extends HorizontalLayout {
    private final Span span = new Span();

    public ErrorMessage() {
        constructUI();
    }

    public ErrorMessage(ErrorWrapperDTO errorWrapper) {
        this();
        this.setErrorWrapper(errorWrapper);
    }

    private void constructUI() {
        this.addClassNames("error-message");

        var icon = VaadinIcon.CLOSE_CIRCLE_O.create();
        icon.setSize("25px");

        this.add(icon, span);
    }

    public void setErrorWrapper(ErrorWrapperDTO errorWrapper) {
        if (errorWrapper.isEmpty()) {
            return;
        }

        var builder = new StringBuilder();

        if (!errorWrapper.isMessageEmpty()) {
            builder.append(errorWrapper.getMessage());
        } else {
            builder.append("The following errors occurred : ");
        }

        if (errorWrapper.hasEveryInformation()) {
            builder.append(" : ");
        }

        builder.append(String.join(",\n", errorWrapper.getErrors()));

        span.setText(builder.toString());
    }
}
