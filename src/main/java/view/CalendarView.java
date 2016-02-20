package view;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.event.BasicEventProvider;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by user on 20.02.2016.
 */
public class CalendarView   extends GridLayout implements View {


    // The container must be ordered by the start time. You
// have to sort the BIC every time after you have added
// or modified events.
    // container.sort(new Object[]{"start"}, new boolean[]{true});


    private static final long serialVersionUID = -5436777475398410597L;

    private static final String DEFAULT_ITEMID = "DEFAULT";

    private enum Mode  // режим
    {
        MONTH, WEEK, DAY;
    }


    private GregorianCalendar calendar;

    /** Target calendar component that this test application is made for. */
    private Calendar calendarComponent;

    private Date currentMonthsFirstDate;

    private final Label captionLabel = new Label("");

    private Button monthButton;

    private Button weekButton;

    private Button dayButton;

    private Button nextButton;

    private Button prevButton;

    private ComboBox timeZoneSelect;

    private ComboBox formatSelect;

    private ComboBox localeSelect;

    private CheckBox hideWeekendsButton;

    private CheckBox readOnlyButton;

    private TextField captionField;

    private Window scheduleEventPopup;

    private final FormLayout scheduleEventFieldLayout = new FormLayout();
    private FieldGroup scheduleEventFieldGroup = new FieldGroup();

    private Button deleteEventButton;

    private Button applyEventButton;

    private Mode viewMode = Mode.WEEK;

    private BasicEventProvider dataSource;

    private Button addNewEvent;

    /*
     * When testBench is set to true, CalendarTest will have static content that
     * is more suitable for Vaadin TestBench testing. Calendar will use a static
     * date Mon 10 Jan 2000. Enable by starting the application with a
     * "testBench" parameter in the URL.
     */
    private boolean testBench = false;

    private String calendarHeight = null;

    private String calendarWidth = null;

    private CheckBox disabledButton;

    private Integer firstHour;

    private Integer lastHour;

    private Integer firstDay;

    private Integer lastDay;

    private Locale defaultLocale = Locale.US;

    private boolean showWeeklyView;

    private boolean useSecondResolution;

    private DateField startDateField;
    private DateField endDateField;

    public CalendarView () {
        setSizeFull();
        setHeight("1000px");
        setMargin(true);
        setSpacing(true);

        // handleURLParams(request.getParameterMap());


        initContent();
    }

    private void initContent() {
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }



}
