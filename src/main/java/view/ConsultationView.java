package view;

import backend.ConsultationManager;
import backend.basicevent.ConsultationBasicEvent;
import backend.entity.Consultation;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Label;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.BasicEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicWeekClickHandler;
import model.ConsultationModel;
import presenter.ConsultationPresenter;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ConsultationView extends GridLayout implements View{

    // 3 режима, - день, неделя, месяц.
    private enum Mode {
        DAY, WEEK, MONTH
    }

    // Контролируем время
    private GregorianCalendar gregorianCalendar;
    private Date currentMonthsFirstDate;

    // Визуальные компоненты:
    // Сам календарь
    private Calendar calendarComponent;
    // Отображение всего месяца
    private Button monthButton;
    // Отображение недели
    private Button weekButton;
    // Отображение одного дня
    private Button dayButton;
    // Прокрутка назад/вперёд
    private Button prevButton;
    private Button nextButton;
    // Добавить новую консультацию
    private Button addNewEvent;
    // Заголовок типа Jan 2016 , Oct 2017 (Будет правее кнопки prevButton)
    private final Label captionLabel = new Label("");
    // Чекбоксы для сокрытия различных видов консультаций + сокрытие выходных
    private CheckBox hideOhcno;
    private CheckBox hideZaohcno;
    private CheckBox hideRS;
    private CheckBox hideOncology;
    private CheckBox hideWeekendsButton;

    // Текущй режим отображения
    private Mode viewMode = Mode.WEEK;
    private BasicEventProvider dataSource;

    EventEditForm eventEditForm;

    public ConsultationView() {
        setSizeFull();
        setSpacing(true);
        setLocale(Locale.getDefault());
        initCalendar();
        initLayoutContent();
        addInitialEvents();
    }

    private void updateCaptionLabel() {
        DateFormatSymbols s = new DateFormatSymbols(getLocale());
        String month = s.getShortMonths()[gregorianCalendar.get(GregorianCalendar.MONTH)];
        captionLabel.setValue(month + " "
                + gregorianCalendar.get(GregorianCalendar.YEAR));
    }



    private CalendarEvent createNewEvent(Date startDate, Date endDate) {
        BasicEvent event = new BasicEvent();
        event.setCaption("");
        event.setStart(startDate);
        event.setEnd(endDate);
        event.setStyleName("color1");
        return event;
    }

    private ConsultationBasicEvent getNewEvent(String caption, Date start, Date end) {
        Consultation consultation = new Consultation(new Date(),0,"","","",start,end,"");
        ConsultationBasicEvent event = new ConsultationBasicEvent(caption,"new event",consultation);
        event.setCaption(caption);
        event.setStart(start);
        event.setEnd(end);

        return event;
    }



    // >> INITIALIZATION AND LAYOUT <<

    // Инициализируем компонент calendarComponent
    private void initCalendar() {
        // TODO Заменить на свой собственный, который будет тянуть из базы
        dataSource = new BasicEventProvider();

        calendarComponent = new Calendar(dataSource);
        eventEditForm = new EventEditForm(this,calendarComponent,dataSource);
        calendarComponent.setLocale(getLocale());
        calendarComponent.setSizeFull();
        // Чем меньше отображается часов, тем детальнее события.
        calendarComponent.setFirstVisibleHourOfDay(9);
        calendarComponent.setLastVisibleHourOfDay(18);
        calendarComponent.setTimeFormat(Calendar.TimeFormat.Format24H);

        Date today = getToday();
        gregorianCalendar = new GregorianCalendar(getLocale());
        gregorianCalendar.setTime(today);
        calendarComponent.getInternalCalendar().setTime(today);

        // Calendar getStartDate (and getEndDate) has some strange logic which
        // returns Monday of the current internal time if no start date has been
        // set
        calendarComponent.setStartDate(calendarComponent.getStartDate());
        calendarComponent.setEndDate(calendarComponent.getEndDate());
        int rollAmount = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
        gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
        currentMonthsFirstDate = gregorianCalendar.getTime();

        addCalendarEventListeners();
    }

    private void initLayoutContent() {
        initNavigationButtons();
        initHideWeekEndButton();
        initAddNewEventButton();

        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidth("100%");
        hl.setSpacing(true);
        hl.addComponent(prevButton);
        hl.addComponent(captionLabel);

        CssLayout group = new CssLayout();
        group.addStyleName("v-component-group");
        group.addComponent(dayButton);
        group.addComponent(weekButton);
        group.addComponent(monthButton);
        hl.addComponent(group);

        hl.addComponent(nextButton);
        hl.setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);
        hl.setComponentAlignment(captionLabel, Alignment.MIDDLE_CENTER);
        hl.setComponentAlignment(group, Alignment.MIDDLE_CENTER);
        hl.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);

        // monthButton.setVisible(viewMode == Mode.WEEK);
        // weekButton.setVisible(viewMode == Mode.DAY);

        HorizontalLayout controlPanel = new HorizontalLayout();
        controlPanel.setSpacing(true);
        controlPanel.setWidth("100%");
        controlPanel.setMargin(new MarginInfo(false, false, true, false));
        controlPanel.addComponent(hideWeekendsButton);
        controlPanel.addComponent(addNewEvent);
        controlPanel.setExpandRatio(addNewEvent, 1.0f);

        controlPanel.setComponentAlignment(hideWeekendsButton,
                Alignment.BOTTOM_LEFT);
        controlPanel.setComponentAlignment(addNewEvent, Alignment.BOTTOM_RIGHT);

        addComponent(controlPanel);
        addComponent(hl);
        addComponent(calendarComponent);
        setRowExpandRatio(getRows() - 1, 1.0f);
    }

    private void initHideWeekEndButton() {
        hideWeekendsButton = new CheckBox("Hide weekends");
        hideWeekendsButton.addStyleName("small");
        hideWeekendsButton.setImmediate(true);
        hideWeekendsButton
                .addValueChangeListener(new Property.ValueChangeListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        setWeekendsHidden(hideWeekendsButton.getValue());
                    }
                });
    }

    private void initNavigationButtons() {
        monthButton = new Button("Month", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                switchToMonthView();
            }
        });

        weekButton = new Button("Week", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                // simulate week click
                CalendarComponentEvents.WeekClickHandler handler = (CalendarComponentEvents.WeekClickHandler) calendarComponent
                        .getHandler(CalendarComponentEvents.WeekClick.EVENT_ID);
                handler.weekClick(new CalendarComponentEvents.WeekClick(calendarComponent, gregorianCalendar
                        .get(GregorianCalendar.WEEK_OF_YEAR), gregorianCalendar
                        .get(GregorianCalendar.YEAR)));
            }
        });

        dayButton = new Button("Day", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                // simulate day click
                BasicDateClickHandler handler = (BasicDateClickHandler) calendarComponent
                        .getHandler(CalendarComponentEvents.DateClickEvent.EVENT_ID);
                handler.dateClick(new CalendarComponentEvents.DateClickEvent(calendarComponent,
                        gregorianCalendar.getTime()));
            }
        });

        nextButton = new Button("Next", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                handleNextButtonClick();
            }
        });

        prevButton = new Button("Prev", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                handlePreviousButtonClick();
            }
        });
    }

    public void initAddNewEventButton() {
        addNewEvent = new Button("Add new event");
        addNewEvent.addStyleName("primary");
        addNewEvent.addStyleName("small");
        addNewEvent.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = -8307244759142541067L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Date start = getToday();
                start.setHours(0);
                start.setMinutes(0);
                start.setSeconds(0);

                Date end = getEndOfDay(gregorianCalendar, start);

                eventEditForm.showEventPopup(createNewEvent(start, end), true);
            }
        });
    }

    private void addCalendarEventListeners() {
        // Register week clicks by changing the schedules start and end dates.
        calendarComponent.setHandler(new BasicWeekClickHandler() {

            @Override
            public void weekClick(CalendarComponentEvents.WeekClick event) {
                // let BasicWeekClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.weekClick(event);
                updateCaptionLabel();
                switchToWeekView();
            }
        });

        calendarComponent.setHandler(new CalendarComponentEvents.EventClickHandler() {

            @Override
            public void eventClick(CalendarComponentEvents.EventClick event) {
                eventEditForm.showEventPopup(event.getCalendarEvent(), false);
            }
        });

        calendarComponent.setHandler(new BasicDateClickHandler() {

            @Override
            public void dateClick(CalendarComponentEvents.DateClickEvent event) {
                // let BasicDateClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.dateClick(event);
                switchToDayView();
            }
        });

        calendarComponent.setHandler(new CalendarComponentEvents.RangeSelectHandler() {

            @Override
            public void rangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
                handleRangeSelect(event);
            }
        });
    }

    // Класс нужен для тестов, для самого начала.
    private void addInitialEvents() {
        Date originalDate = gregorianCalendar.getTime();
        Date today = getToday();
        ConsultationManager consultationManager = new ConsultationManager();
        ConsultationModel consultationModel = new ConsultationModel();
        ConsultationPresenter consultationPresenter = new ConsultationPresenter(consultationModel,consultationManager);
        calendarComponent.setContainerDataSource(consultationModel.beanItemContainer);


        // Add a event that last a whole week
/*
        Date start = resolveFirstDateOfWeek(today, gregorianCalendar);
        Date end = resolveLastDateOfWeek(today, gregorianCalendar);
        CalendarTestEvent event = getNewEvent("Whole week event", start, end);
        event.setAllDay(true);
        event.setStyleName("color4");
        event.setDescription("Description for the whole week event.");
        dataSource.addEvent(event);

        // Add a allday event
        gregorianCalendar.setTime(start);
        gregorianCalendar.add(GregorianCalendar.DATE, 3);
        start = gregorianCalendar.getTime();
        end = start;
        event = getNewEvent("All-day event", start, end);
        event.setAllDay(true);
        event.setDescription("Some description.");
        event.setStyleName("color3");
        dataSource.addEvent(event);

        // Add a second allday event
        gregorianCalendar.add(GregorianCalendar.DATE, 1);
        start = gregorianCalendar.getTime();
        end = start;
        event = getNewEvent("Second all-day event", start, end);
        event.setAllDay(true);
        event.setDescription("Some description.");
        event.setStyleName("color2");
        dataSource.addEvent(event);

        gregorianCalendar.add(GregorianCalendar.DATE, -3);
        gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY, 9);
        gregorianCalendar.set(GregorianCalendar.MINUTE, 30);
        start = gregorianCalendar.getTime();
        gregorianCalendar.add(GregorianCalendar.HOUR_OF_DAY, 5);
        gregorianCalendar.set(GregorianCalendar.MINUTE, 0);
        end = gregorianCalendar.getTime();
        event = getNewEvent("Appointment", start, end);
        event.setStyleName("color1");
        event.setDescription("A longer description, which should display correctly.");
        dataSource.addEvent(event);

        gregorianCalendar.add(GregorianCalendar.DATE, 1);
        gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY, 11);
        gregorianCalendar.set(GregorianCalendar.MINUTE, 0);
        start = gregorianCalendar.getTime();
        gregorianCalendar.add(GregorianCalendar.HOUR_OF_DAY, 8);
        end = gregorianCalendar.getTime();
        event = getNewEvent("Training", start, end);
        event.setStyleName("color2");
        dataSource.addEvent(event);

        gregorianCalendar.add(GregorianCalendar.DATE, 4);
        gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY, 9);
        gregorianCalendar.set(GregorianCalendar.MINUTE, 0);
        start = gregorianCalendar.getTime();
        gregorianCalendar.add(GregorianCalendar.HOUR_OF_DAY, 9);
        end = gregorianCalendar.getTime();
        event = getNewEvent("Free time", start, end);
        dataSource.addEvent(event);*/

        gregorianCalendar.setTime(originalDate);
    }

    // >> INITIALIZATION AND LAYOUT <<

    // >> HANDLERS <<

    private void handleNextButtonClick() {
        switch (viewMode) {
            case MONTH:
                nextMonth();
                break;
            case WEEK:
                nextWeek();
                break;
            case DAY:
                nextDay();
                break;
        }
    }

    private void handlePreviousButtonClick() {
        switch (viewMode) {
            case MONTH:
                previousMonth();
                break;
            case WEEK:
                previousWeek();
                break;
            case DAY:
                previousDay();
                break;
        }
    }

    private void handleRangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
        Date start = event.getStart();
        Date end = event.getEnd();

        /*
         * If a range of dates is selected in monthly mode, we want it to end at
         * the end of the last day.
         */
        if (event.isMonthlyMode()) {
            end = getEndOfDay(gregorianCalendar, end);
        }
        eventEditForm.showEventPopup(createNewEvent(start, end), true);
    }

    private void setWeekendsHidden(boolean weekendsHidden) {
        if (weekendsHidden) {
            int firstToShow = (GregorianCalendar.MONDAY - gregorianCalendar
                    .getFirstDayOfWeek()) % 7;
            calendarComponent.setFirstVisibleDayOfWeek(firstToShow + 1);
            calendarComponent.setLastVisibleDayOfWeek(firstToShow + 5);
        } else {
            calendarComponent.setFirstVisibleDayOfWeek(1);
            calendarComponent.setLastVisibleDayOfWeek(7);
        }

    }

    // >> HANDLERS <<

    // >> NAVIGATION BETWEEN DAYS <<

    private void nextMonth() {
        rollMonth(1);
    }

    private void previousMonth() {
        rollMonth(-1);
    }

    private void nextWeek() {
        rollWeek(1);
    }

    private void previousWeek() {
        rollWeek(-1);
    }

    private void nextDay() {
        rollDate(1);
    }

    private void previousDay() {
        rollDate(-1);
    }

    private void rollMonth(int direction) {
        gregorianCalendar.setTime(currentMonthsFirstDate);
        gregorianCalendar.add(GregorianCalendar.MONTH, direction);
        resetTime(false);
        currentMonthsFirstDate = gregorianCalendar.getTime();
        calendarComponent.setStartDate(currentMonthsFirstDate);

        updateCaptionLabel();

        gregorianCalendar.add(GregorianCalendar.MONTH, 1);
        gregorianCalendar.add(GregorianCalendar.DATE, -1);
        resetCalendarTime(true);
    }

    private void rollWeek(int direction) {
        gregorianCalendar.add(GregorianCalendar.WEEK_OF_YEAR, direction);
        gregorianCalendar.set(GregorianCalendar.DAY_OF_WEEK,
                gregorianCalendar.getFirstDayOfWeek());
        resetCalendarTime(false);
        resetTime(true);
        gregorianCalendar.add(GregorianCalendar.DATE, 6);
        calendarComponent.setEndDate(gregorianCalendar.getTime());
    }

    private void rollDate(int direction) {
        gregorianCalendar.add(GregorianCalendar.DATE, direction);
        resetCalendarTime(false);
        resetCalendarTime(true);
    }

    // >> NAVIGATION BETWEEN DAYS <<

    // >> SWITCHES TO OTHER WIEW <<

    public void switchToDayView() {
        viewMode = Mode.DAY;
    }

    public void switchToWeekView() {
        viewMode = Mode.WEEK;
    }

    public void switchToMonthView() {
        viewMode = Mode.MONTH;

        int rollAmount = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
        gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);

        calendarComponent.setStartDate(gregorianCalendar.getTime());

        updateCaptionLabel();

        gregorianCalendar.add(GregorianCalendar.MONTH, 1);
        gregorianCalendar.add(GregorianCalendar.DATE, -1);

        calendarComponent.setEndDate(gregorianCalendar.getTime());

        gregorianCalendar.setTime(getToday());
    }

    // >> SWITCHES TO OTHER WIEW <<

    // >> WORKING WITH DATES <<

    private Date getToday() {
        return new Date();
    }

    private void resetCalendarTime(boolean resetEndTime) {
        resetTime(resetEndTime);
        if (resetEndTime) {
            calendarComponent.setEndDate(gregorianCalendar.getTime());
        } else {
            calendarComponent.setStartDate(gregorianCalendar.getTime());
            updateCaptionLabel();
        }
    }

    /*
     * Resets the calendar time (hour, minute second and millisecond) either to
     * zero or maximum value.
     */
    private void resetTime(boolean max) {
        if (max) {
            gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY,
                    gregorianCalendar.getMaximum(GregorianCalendar.HOUR_OF_DAY));
            gregorianCalendar.set(GregorianCalendar.MINUTE,
                    gregorianCalendar.getMaximum(GregorianCalendar.MINUTE));
            gregorianCalendar.set(GregorianCalendar.SECOND,
                    gregorianCalendar.getMaximum(GregorianCalendar.SECOND));
            gregorianCalendar.set(GregorianCalendar.MILLISECOND,
                    gregorianCalendar.getMaximum(GregorianCalendar.MILLISECOND));
        } else {
            gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
            gregorianCalendar.set(GregorianCalendar.MINUTE, 0);
            gregorianCalendar.set(GregorianCalendar.SECOND, 0);
            gregorianCalendar.set(GregorianCalendar.MILLISECOND, 0);
        }
    }

    private static Date getEndOfDay(java.util.Calendar calendar, Date date) {
        java.util.Calendar calendarClone = (java.util.Calendar) calendar
                .clone();

        calendarClone.setTime(date);
        calendarClone.set(java.util.Calendar.MILLISECOND,
                calendarClone.getActualMaximum(java.util.Calendar.MILLISECOND));
        calendarClone.set(java.util.Calendar.SECOND,
                calendarClone.getActualMaximum(java.util.Calendar.SECOND));
        calendarClone.set(java.util.Calendar.MINUTE,
                calendarClone.getActualMaximum(java.util.Calendar.MINUTE));
        calendarClone.set(java.util.Calendar.HOUR,
                calendarClone.getActualMaximum(java.util.Calendar.HOUR));
        calendarClone.set(java.util.Calendar.HOUR_OF_DAY,
                calendarClone.getActualMaximum(java.util.Calendar.HOUR_OF_DAY));

        return calendarClone.getTime();
    }

    private static Date getStartOfDay(java.util.Calendar calendar, Date date) {
        java.util.Calendar calendarClone = (java.util.Calendar) calendar
                .clone();

        calendarClone.setTime(date);
        calendarClone.set(java.util.Calendar.MILLISECOND, 0);
        calendarClone.set(java.util.Calendar.SECOND, 0);
        calendarClone.set(java.util.Calendar.MINUTE, 0);
        calendarClone.set(java.util.Calendar.HOUR, 0);
        calendarClone.set(java.util.Calendar.HOUR_OF_DAY, 0);

        return calendarClone.getTime();
    }

    private Date resolveFirstDateOfWeek(Date today,
                                        java.util.Calendar currentCalendar) {
        int firstDayOfWeek = currentCalendar.getFirstDayOfWeek();
        currentCalendar.setTime(today);
        while (firstDayOfWeek != currentCalendar
                .get(java.util.Calendar.DAY_OF_WEEK)) {
            currentCalendar.add(java.util.Calendar.DATE, -1);
        }
        return currentCalendar.getTime();
    }

    private Date resolveLastDateOfWeek(Date today,
                                       java.util.Calendar currentCalendar) {
        currentCalendar.setTime(today);
        currentCalendar.add(java.util.Calendar.DATE, 1);
        int firstDayOfWeek = currentCalendar.getFirstDayOfWeek();
        // Roll to weeks last day using firstdayofweek. Roll until FDofW is
        // found and then roll back one day.
        while (firstDayOfWeek != currentCalendar
                .get(java.util.Calendar.DAY_OF_WEEK)) {
            currentCalendar.add(java.util.Calendar.DATE, 1);
        }
        currentCalendar.add(java.util.Calendar.DATE, -1);
        return currentCalendar.getTime();
    }

    // >> WORKING WITH DATES <<
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}