package backend.entity;

import java.util.Date;

/**
 * Created by user on 20.02.2016.
 */
public class Consultation
{

    Date procbegintime;

    Date procendtime;

    String surname;

    String name;

    String patronymic;

    String cas_history_num;

    String diagnosis;

    Date birthday;


    public Consultation()
    {

    }



    public Consultation(Date birthday, String cas_history_num, String diagnosis, String patronymic, String name,
                        Date procbegintime, Date procendtime, String surname) {
        this.birthday = birthday;
        this.cas_history_num = cas_history_num;
        this.diagnosis = diagnosis;
        this.patronymic = patronymic;
        this.name = name;
        this.procbegintime = procbegintime;
        this.procendtime = procendtime;
        this.surname = surname;
    }


    public Date getProcbegintime ( )
    {
        return procbegintime;
    }

    public void setProcbegintime(Date procbegintime)
    {
        this.procbegintime = procbegintime;
    }


    public Date getProcendtime ( )
    {
        return procendtime;
    }

    public void setProcendtime (Date procendtime)
    {
        this.procendtime = procendtime;
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getCas_history_num() {
        return cas_history_num;
    }

    public void setCas_history_num(String cas_history_num) {
        this.cas_history_num = cas_history_num;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


    @Override
    public String toString() {
        return "Consultation{" +
                "procbegintime=" + procbegintime +
                ", procendtime=" + procendtime +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", cas_history_num='" + cas_history_num + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", birthday=" + birthday +
                '}';
    }

}
