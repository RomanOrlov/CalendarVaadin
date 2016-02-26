package view;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.BasicEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import presenter.ConsultationPresenter;

import java.util.ArrayList;
import java.util.Arrays;

public class EventEditForm {

    // Костыли на скорую руку
    private Calendar calendarComponent;
    private BasicEventProvider dataSource;
    private ConsultationView consultationView;
    public static final ArrayList<String> styles = new ArrayList<>(Arrays.asList("color1", "color2", "color3", "color4", "color5"));

    private Window scheduleEventPopup;
    private final FormLayout scheduleEventFieldLayout = new FormLayout();
    private FieldGroup scheduleEventFieldGroup = new FieldGroup();
    private NativeSelect selectProcedure;
    private NativeSelect selectStyle;
    private DateField startDateField;
    private DateField endDateField;
    private Button deleteEventButton;
    private Button applyEventButton;
    private boolean useSecondResolution;

    public EventEditForm(ConsultationView consultationView) {
        this.consultationView = consultationView;
        this.calendarComponent = consultationView.calendarComponent;
        this.dataSource = consultationView.dataSource;
    }

    //region Работа с FieldGroup
    // Осуществление привязки данных (binding) к полям класса
    private void initFormFields(Layout formLayout, Class<? extends CalendarEvent> eventClass) {

        startDateField = createDateField("Начало");
        startDateField.setRequired(true);
        endDateField = createDateField("Конец");
        endDateField.setRequired(true);

        selectProcedure = createNativeSelect(ConsultationPresenter.PROCEDURES, "Вид консультации");
        selectProcedure.setRequired(true);

        selectStyle = createNativeSelect(styles, "Цвет");

        final TextArea descriptionField = createTextArea("Описание");
        descriptionField.setRows(3);
        HorizontalLayout horizontalLayout = new HorizontalLayout(startDateField, endDateField);
        horizontalLayout.setSpacing(true);
        formLayout.addComponent(horizontalLayout);
        formLayout.addComponent(selectProcedure);
        formLayout.addComponent(descriptionField);
        formLayout.addComponent(selectStyle);

        scheduleEventFieldGroup.bind(startDateField, "start");
        scheduleEventFieldGroup.bind(selectStyle, "styleName");
        scheduleEventFieldGroup.bind(endDateField, "end");
        scheduleEventFieldGroup.bind(selectProcedure, "caption");
        scheduleEventFieldGroup.bind(descriptionField, "description");
    }

    // Обновление полей формы
    private void updateCalendarEventForm(CalendarEvent event) {
        BeanItem<CalendarEvent> item = new BeanItem<>(event);
        scheduleEventFieldLayout.removeAllComponents();
        scheduleEventFieldGroup = new FieldGroup();
        initFormFields(scheduleEventFieldLayout, event.getClass());
        scheduleEventFieldGroup.setBuffered(true);
        scheduleEventFieldGroup.setItemDataSource(item);
    }

    private void setFormDateResolution(Resolution resolution) {
        if (startDateField != null && endDateField != null) {
            startDateField.setResolution(resolution);
            endDateField.setResolution(resolution);
        }
    }

    @SuppressWarnings("unchecked")
    private BasicEvent getFormCalendarEvent() {
        BeanItem<CalendarEvent> item = (BeanItem<CalendarEvent>) scheduleEventFieldGroup
                .getItemDataSource();
        CalendarEvent event = item.getBean();
        return (BasicEvent) event;
    }
    //endregion Работа с FieldGroup

    //region PopUp - окно,Window. Создание, отображение, обновление <<
    private void createCalendarEventPopup() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        scheduleEventPopup = new Window(null, layout);
        scheduleEventPopup.setSizeFull();
        scheduleEventPopup.setModal(true);
        scheduleEventPopup.center();

        scheduleEventFieldLayout.addStyleName("light");
        scheduleEventFieldLayout.setMargin(false);
        layout.addComponent(scheduleEventFieldLayout);

        applyEventButton = new Button("Apply", clickEvent -> {
            try {
                commitCalendarEvent();
            } catch (FieldGroup.CommitException e) {
                e.printStackTrace();
            }
        });
        applyEventButton.addStyleName("primary");
        Button cancel = new Button("Cancel", clickEvent -> discardCalendarEvent());
        deleteEventButton = new Button("Delete", clickEvent -> deleteCalendarEvent());
        scheduleEventPopup.addCloseListener(closeEvent -> discardCalendarEvent());

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addStyleName("v-window-bottom-toolbar");
        buttons.setWidth("100%");
        buttons.setSpacing(true);
        buttons.addComponent(deleteEventButton);
        buttons.addComponent(applyEventButton);
        buttons.setExpandRatio(applyEventButton, 1);
        buttons.setComponentAlignment(applyEventButton, Alignment.TOP_RIGHT);
        buttons.addComponent(cancel);
        layout.addComponent(buttons);
    }

    public void showEventPopup(CalendarEvent event, boolean newEvent) {
        if (event == null) {
            return;
        }
        // Появление кнопок и обновление оглавления окна
        updateCalendarEventPopup(newEvent);
        // Обновление полей формы
        updateCalendarEventForm(event);
        if (!consultationView.getUI().getWindows().contains(scheduleEventPopup)) {
            consultationView.getUI().addWindow(scheduleEventPopup);
        }
    }

    // Появление кнопок и обновление оглавления окна
    private void updateCalendarEventPopup(boolean newEvent) {
        if (scheduleEventPopup == null) {
            createCalendarEventPopup();
        }
        if (newEvent) {
            scheduleEventPopup.setCaption("Новая штука");
        } else {
            scheduleEventPopup.setCaption("Редактировать консультацию");
        }

        deleteEventButton.setVisible(!newEvent);
        deleteEventButton.setEnabled(!calendarComponent.isReadOnly());
        applyEventButton.setEnabled(!calendarComponent.isReadOnly());
    }
    //endregion PopUp - окно,Window. Создание, отображение, обновление

    //region ШАБЛОНЫ Чекбоксов, Текстовых полей и т.д <<
    private CheckBox createCheckBox(String caption) {
        CheckBox cb = new CheckBox(caption);
        cb.setImmediate(true);
        return cb;
    }

    private TextField createTextField(String caption) {
        TextField f = new TextField(caption);
        f.setNullRepresentation("");
        return f;
    }

    private TextArea createTextArea(String caption) {
        TextArea f = new TextArea(caption);
        f.setNullRepresentation("");
        return f;
    }

    private DateField createDateField(String caption) {
        DateField f = new DateField(caption);
        if (useSecondResolution) {
            f.setResolution(Resolution.SECOND);
        } else {
            f.setResolution(Resolution.MINUTE);
        }
        return f;
    }

    private NativeSelect createNativeSelect(ArrayList<String> strings, String caption) {
        NativeSelect nativeSelect = new NativeSelect(caption, strings);
        nativeSelect.setRequired(true);
        nativeSelect.setBuffered(true);
        return nativeSelect;
    }
    //endregion ШАБЛОНЫ Чекбоксов, Текстовых полей и т.д <<

    //region Изменение, удаление и сброс события (по факту сущности, реализующей BasicEvent)
    private void commitCalendarEvent() throws FieldGroup.CommitException {
        scheduleEventFieldGroup.commit();
        BasicEvent event = getFormCalendarEvent();
        if (event.getEnd() == null) {
            event.setEnd(event.getStart());
        }
        if (!dataSource.containsEvent(event)) {
            dataSource.addEvent(event);
        }
        consultationView.getUI().removeWindow(scheduleEventPopup);

    }

    /* Removes the event from the data source and fires change event. */
    private void deleteCalendarEvent() {
        BasicEvent event = getFormCalendarEvent();
        if (dataSource.containsEvent(event)) {
            dataSource.removeEvent(event);
        }
        consultationView.getUI().removeWindow(scheduleEventPopup);

    }

    private void discardCalendarEvent() {
        scheduleEventFieldGroup.discard();
        consultationView.getUI().removeWindow(scheduleEventPopup);
    }
    //endregion Изменение и сброс события (по факту сущности, реализующей BasicEvent)
}
