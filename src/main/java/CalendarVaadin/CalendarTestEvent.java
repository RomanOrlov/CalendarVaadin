package CalendarVaadin;

import com.vaadin.ui.components.calendar.event.BasicEvent;

/**
 * Created by user on 19.02.2016.
 */
public class CalendarTestEvent extends BasicEvent {

    private static final long serialVersionUID = 2820133201983036866L;
    private String where;
    private Object data;

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
        fireEventChange();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
        fireEventChange();
    }
}