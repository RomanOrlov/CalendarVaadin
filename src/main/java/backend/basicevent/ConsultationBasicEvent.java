package backend.basicevent;

import backend.entity.Consultation;
import com.vaadin.ui.components.calendar.event.BasicEvent;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by user on 20.02.2016.
 */
public class ConsultationBasicEvent extends BasicEvent{
    private static final long serialVersionUID = 2820133201983036866L;

    // Для событий есть стаические поля исполнителя и формы

    String executor;

    String form;

    Date procbegintime;

    Date procendtime;

    String surname;

    String name;

    String patronymic;

    String cas_history_num;

    String diagnosis;

    Date birthday;



    public ConsultationBasicEvent(Consultation consultation, String executor, String form)
    {

        this.executor = executor;

        this.form = form;

        Date startday = consultation.getProcbegintime();
        GregorianCalendar calendar= new GregorianCalendar(startday.getYear(),startday.getMonth(),
                startday.getHours(),startday.getMinutes(),startday.getSeconds());
        calendar.add(calendar.HOUR,9);
        Date startNewDate = calendar.getTime();
        this.procbegintime = startNewDate;


        Date endday = consultation.getProcendtime();
        GregorianCalendar calendar1= new GregorianCalendar(endday.getYear(),endday.getMonth(),
                endday.getHours(),endday.getMinutes(),endday.getSeconds());
        calendar1.add(calendar.HOUR,18);
        Date endNewDate = calendar1.getTime();
        this.procendtime = endNewDate;

        this.name = consultation.getName();

        this.cas_history_num = consultation.getCas_history_num();

        this.surname = consultation.getSurname();

        this.diagnosis = consultation.getDiagnosis();

        this.patronymic = consultation.getPatronymic();

        this.birthday = consultation.getBirthday();

    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCas_history_num() {
        return cas_history_num;
    }

    public void setCas_history_num(String cas_history_num) {
        this.cas_history_num = cas_history_num;
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

    public Date getProcbegintime() {
        return procbegintime;
    }

    public void setProcbegintime(Date procbegintime) {
        this.procbegintime = procbegintime;
        fireEventChange();
    }

    public Date getProcendtime() {
        return procendtime;
    }

    public void setProcendtime(Date procendtime) {
        this.procendtime = procendtime;
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

    public String getForm() {
        return form;
    }
    public void setForm(String form) {
        this.form = form;
        fireEventChange();

    }







}
