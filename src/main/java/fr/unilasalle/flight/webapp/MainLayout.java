package fr.unilasalle.flight.webapp;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.unilasalle.flight.webapp.data.constants.Constants;
import fr.unilasalle.flight.webapp.ui.views.flights.FlightsView;
import fr.unilasalle.flight.webapp.ui.views.planes.PlanesView;
import fr.unilasalle.flight.webapp.ui.views.reservations.ReservationsView;
import kong.unirest.core.Unirest;

public class MainLayout extends AppLayout {

    public MainLayout() {
        configureUnirest();

        createHeader();
        createDrawer();
    }

    private void configureUnirest() {
        Unirest.config().reset();
        Unirest.config()
                .defaultBaseUrl(Constants.API_BASE_URL)
                .connectTimeout(1000)
                .setDefaultHeader("accept", "*/*")
                .setDefaultHeader("Content-Type", "application/json")
                .followRedirects(false)
                .enableCookieManagement(false);
    }

    private void createHeader() {
        H1 logo = new H1("Flight WebApp");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        var header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.NONE);

        addToNavbar(header);
    }

    private void createDrawer() {
        var nav = new SideNav();
        nav.addItem(
                new SideNavItem("Planes", PlanesView.class, VaadinIcon.AUTOMATION.create()),
                new SideNavItem("Flights", FlightsView.class, VaadinIcon.FLIGHT_TAKEOFF.create()),
                new SideNavItem("Reservations", ReservationsView.class, VaadinIcon.USER_CARD.create())
        );
        addToDrawer(nav);
    }
}