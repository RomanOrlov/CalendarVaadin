package backend;

import backend.entity.Consultation;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

/**
 * Created by user on 20.02.2016.
 */


public class ConsultationManager {



    public Collection<? extends Consultation> listConsultation (Date fromDate,Date toDate) // дата будет браться от 01.01.2016 и 20.02.2016
    {

        try (
                Connection con = DB.getConnection()
        ) {
            QueryRunner qr = new QueryRunner();
            String sql = "select\n" +
                    "nbc_proc.procbegintime, nbc_proc.procendtime,\n" +
                    "bas_people.surname,bas_people.name,bas_people.patronymic,\n" +
                    "nbc_patients.case_history_num,\n" +
                    " nbc_patients.diagnosis,bas_people.birthday\n" +
                    " from bas_people\n" +
                    " join nbc_patients  on  bas_people.n = nbc_patients.bas_people_n\n" +
                    " left join   nbc_proc on  nbc_proc.nbc_patients_n = nbc_patients.n\n" +
                    " where nbc_proc.proc_type = 4\n" +
                    "\n" +
                    "and  nbc_proc.procbegintime between '%s' and '%s'\n" +
                    "and nbc_proc.procendtime is not NULL";
            String from = Util.getDate(fromDate);
            String to =  Util.getDate(toDate);
            Object[] params = new Object[]{from,to};
            BeanListHandler<Consultation> handler = new BeanListHandler<>(Consultation.class);
            return qr.query(con,sql, handler,params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}
