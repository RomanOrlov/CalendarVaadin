import backend.ConsultationManager;
import backend.entity.Consultation;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by user on 22.02.2016.
 */
public class Tests {

    @Ignore
    @Test
    public void patients(){
        GregorianCalendar calendar= new GregorianCalendar(2016, 1, 1);
        Date startDay = calendar.getTime();
        calendar.add(calendar.MONTH, 1);
        Date endDay = calendar.getTime();
        Collection<? extends Consultation>  consultations =  new ConsultationManager().listConsultation(startDay,endDay);
        for (Consultation cs : consultations) {
            cs.getProcbegintime().setHours(9);
            cs.getProcendtime().setHours(18);
        }
        System.out.println(consultations);
    }
}
