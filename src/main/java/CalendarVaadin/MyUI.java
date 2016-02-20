package CalendarVaadin;

/**
 * Created by user on 18.02.2016.
 */

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;

/**
 *
 */
@Theme("mytheme")
@Widgetset("CalendarVaadin.MyAppWidgetset")
public class MyUI extends UI {


    /*
    SuppressWarnings
    @SuppressWarnings("deprecation")
    @Override
    protected void init(VaadinRequest request) {
        GridLayout content = new GridLayout(1, 2);
        content.setSizeFull();
        setContent(content);

        final Calendar calendar = new Calendar();
        calendar.setLocale(Locale.US);

        calendar.setSizeFull();
        calendar.setStartDate(new Date(100, 1, 1));
        calendar.setEndDate(new Date(100, 2, 1));

        calendar.addActionHandler(new Action.Handler() {

            public final Action NEW_EVENT = new Action("Add event");
            public final Action EDIT_EVENT = new Action("Edit event");
            public final Action REMOVE_EVENT = new Action("Remove event");


            @Override
            public void handleAction(Action action, Object sender, Object target) {
                Date date = (Date) target;
                if (action == NEW_EVENT) {
                    BasicEvent event = new BasicEvent("New event",
                            "Hello world", date, date);
                    calendar.addEvent(event);
                }
            }


            @Override
            public Action[] getActions(Object target, Object sender) {
                CalendarDateRange date = (CalendarDateRange) target;

                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(2000, 1, 1, 12, 0, 0);

                if (date.inRange(cal.getTime())) {
                    return new Action[] { NEW_EVENT, };
                }

                cal.add(java.util.Calendar.DAY_OF_WEEK, 1);

                if (date.inRange(cal.getTime())) {
                    return new Action[] { REMOVE_EVENT };
                }

                return null;
            }


         });

        content.addComponent(calendar);

        content.addComponent(new Button("Set week view",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        calendar.setEndDate(new Date(100, 1, 7));
                    }
                }));

        content.setRowExpandRatio(0, 1);

    }

*/

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        CalendarTest calendarTest = new CalendarTest();
        setContent(calendarTest);
    }


@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
 public static class MyUIServlet extends VaadinServlet
{
}


}