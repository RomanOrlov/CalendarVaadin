package backend.basicevent;

import backend.entity.Consultation;
import com.vaadin.ui.components.calendar.event.BasicEvent;

import java.util.Date;

/**
 * Created by user on 20.02.2016.
 */
public class ConsultationBasicEvent extends BasicEvent{

    private String executor;
    private String surname;
    private String name;
    private String patronymic;
    private int case_history_num;
    private String diagnosis;
    private Date birthday;

    public ConsultationBasicEvent(String caption, String description, Consultation consultation)
    {
        super(caption,description,consultation.getProcbegintime(),consultation.getProcendtime());
        this.executor = consultation.getExecutor();
        this.name = consultation.getName();
        this.case_history_num = consultation.getCase_history_num();
        this.surname = consultation.getSurname();
        this.diagnosis = consultation.getDiagnosis();
        this.patronymic = consultation.getPatronymic();
        this.birthday = consultation.getBirthday();
    }

    public ConsultationBasicEvent(Date birthday, int case_history_num, String diagnosis, String executor, String name, String patronymic, String surname) {
        this.birthday = birthday;
        this.case_history_num = case_history_num;
        this.diagnosis = diagnosis;
        this.executor = executor;
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
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
