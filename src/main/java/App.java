import CalendarVaadin.CalendarTest;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;

/**
 * Created by user on 20.02.2016.
 */

@Theme("mytheme")
@Widgetset("CalendarVaadin.MyAppWidgetset")

public class App extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        new Navigator(this,this);
        getUI().getNavigator().addView("CalendarTest", CalendarTest.class);
        getUI().getNavigator().navigateTo("CalendarTest");
    }

    @WebServlet(urlPatterns = "/*", name = "AppServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = App.class, productionMode = false)
    public static class AppServlet extends VaadinServlet {}




}
