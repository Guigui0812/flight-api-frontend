package fr.unilasalle.flight.webapp.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

public class MasterDetailLayout extends SplitLayout {
    private Div gridWrapper;

    private Div formWrapper;

    private HorizontalLayout gridButtonBar;

    private HorizontalLayout formButtonBar;


    public MasterDetailLayout() {
        this.addClassNames("master-detail-view");
        createGridLayout();
        createEditorLayout();
    }

    private void createGridLayout() {
        var primaryLayoutDiv = new Div();
        primaryLayoutDiv.setClassName("grid-layout");

        gridWrapper = new Div();
        gridWrapper.setClassName("grid-wrapper");
        primaryLayoutDiv.add(gridWrapper);

        gridButtonBar = new HorizontalLayout();
        gridButtonBar.setClassName("button-layout");
        primaryLayoutDiv.add(gridButtonBar);

        this.addToPrimary(primaryLayoutDiv);
    }

    private void createEditorLayout() {
        var secondaryLayoutDiv = new Div();
        secondaryLayoutDiv.setClassName("editor-layout");

        formWrapper = new Div();
        formWrapper.setClassName("editor-wrapper");
        secondaryLayoutDiv.add(formWrapper);

        formButtonBar = new HorizontalLayout();
        formButtonBar.setClassName("button-layout");
        secondaryLayoutDiv.add(formButtonBar);

        this.addToSecondary(secondaryLayoutDiv);
    }

    public void setGrid(Grid<?> grid) {
        gridWrapper.removeAll();
        gridWrapper.add(grid);
    }

    public void setGridButtons(Button... buttons) {
        gridButtonBar.removeAll();
        gridButtonBar.add(buttons);
    }

    public void setForm(FormLayout formLayout) {
        formWrapper.removeAll();
        formWrapper.add(formLayout);
    }

    public void setFormButtons(Button... buttons) {
        formButtonBar.removeAll();
        formButtonBar.add(buttons);
    }

}
