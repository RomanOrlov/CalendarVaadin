package backend.basicevent;

import com.vaadin.ui.components.calendar.event.BasicEvent;

import java.util.Date;

/**
 * Created by user on 20.02.2016.
 */
public class ConsultationBasicEvent extends BasicEvent{
    private static final long serialVersionUID = 2820133201983036866L;


    String executor;

    String surname;

    String name;

    String patronymic;

    int case_history_num;

    String diagnosis;

    Date birthday;


    public ConsultationBasicEvent(String caption,String description,Date start,Date end,String name,String surname,String patronymic,String diagnosis,
                                   int case_history_num,Date birthday,String executor)
    {
        super(caption,description,start,end);

        this.executor = executor;

        this.name = name;

        this.case_history_num = case_history_num;

        this.surname = surname;

        this.diagnosis = diagnosis;

        this.patronymic = patronymic;

        this.birthday = birthday;

    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getCas_history_num() {
        return case_history_num;
    }

    public void setCas_history_num(int cas_history_num) {
        this.case_history_num = cas_history_num;
        fireEventChange();
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
        fireEventChange();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        fireEventChange();
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
        fireEventChange();
    }


    public String getSurname() {
        return surname;

    }

    public void setSurname(String surname) {
        this.surname = surname;
        fireEventChange();
    }



    public String getExecutor() {
        return executor;
    }
    public void setExecutor(String executor) {
        this.executor = executor;
        fireEventChange();
    }

}
