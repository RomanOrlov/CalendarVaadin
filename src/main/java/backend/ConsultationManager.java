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
            String sql = "SELECT\n" +
                    "procbegintime, procendtime,\n" +
                    "surname,name,patronymic,\n" +
                    "case_history_num,\n" +
                    "diagnosis,birthday\n" +
                    " FROM bas_people\n" +
                    " JOIN nbc_patients  on  bas_people.n = nbc_patients.bas_people_n\n" +
                    " LEFT JOIN  nbc_proc on  nbc_proc.nbc_patients_n = nbc_patients.n\n" +
                    " WHERE nbc_proc.proc_type = 4\n" +
                    "\n" +
                    "AND  nbc_proc.procbegintime between '%s' and '%s'\n" +
                    "AND nbc_proc.procendtime is not NULL";

            String to = Util.getDate(toDate);
            String from = Util.getDate(fromDate);
            sql = String.format(sql,from,to);
            BeanListHandler<Consultation> handler = new BeanListHandler<>(Consultation.class); //
            return qr.query(con,sql, handler);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }


}
