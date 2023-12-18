package fr.unilasalle.flight.webapp.ui.views.planes;

import fr.unilasalle.flight.webapp.data.clients.PlanesClient;
import fr.unilasalle.flight.webapp.data.dtos.PlaneDTO;

import java.util.ArrayList;
import java.util.List;

public class PlanesPresenter {

    private final PlanesClient client;

    private final PlanesView view;

    public PlanesPresenter(PlanesView view) {
        this.client = new PlanesClient();
        this.view = view;
    }

    private List<PlaneDTO> loadPlanes() {
        var allPlanes = client.getPlanes();
        return allPlanes == null ? new ArrayList<>() : allPlanes;
    }

    protected void displayContent() {
        view.constructContent(loadPlanes());
        view.form.setPlane(new PlaneDTO());
    }

    protected void save(PlaneDTO plane) {
        var error = client.createPlane(plane);
        if (error == null) {
            view.hideErrorMessage();
            view.openDialog(false);

            view.showNotification(String.format("Plane number %s has been successfully added", plane.getRegistration()));

            view.form.setPlane(new PlaneDTO());
            view.refreshGrid(loadPlanes());

            return;
        }
        view.showErrorMessage(error);
    }

    protected void cancel() {
        view.openDialog(false);
    }
}
