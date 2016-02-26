package presenter;

import backend.ConsultationManager;
import backend.basicevent.ConsultationBasicEvent;
import backend.entity.Consultation;
import model.ConsultationModel;

import java.util.*;

/**
 * Created by user on 20.02.2016.
 */
public class ConsultationPresenter {

    public static final ArrayList<String> PROCEDURES = new ArrayList<>(Arrays.asList("Радиохирургия","Заочная консультация","Очная консультация","Оннкология"));
    public static final ArrayList<String> EXECUTORS = new ArrayList<>(Arrays.asList("физик", "онколог", "планировщик", "врач", "лечащий врач"));
    public final ConsultationModel consultationModel;
    public final ConsultationManager consultationManager;
    private static ArrayList<Consultation> consultations = new ArrayList<>();

    public ConsultationPresenter(ConsultationModel consultationModel, ConsultationManager consultationManager) {
        this.consultationModel = consultationModel;
        this.consultationManager = consultationManager;
    }

    public void start() {
        GregorianCalendar calendar= new GregorianCalendar(2016, 1, 1);
        Date startDay = calendar.getTime();
        calendar.add(calendar.MONTH, 1);
        Date endDay = calendar.getTime();
        consultations = new ArrayList<>(consultationManager.listConsultation(startDay, endDay));
        Random random = new Random();
        for (Consultation consultation : consultations) {
            ConsultationBasicEvent basicEvent = new ConsultationBasicEvent (null, "Some description.", consultation);
            basicEvent.setExecutor(EXECUTORS.get(random.nextInt(EXECUTORS.size())));
            basicEvent.getStart().setHours(9);
            basicEvent.getEnd().setHours(18);
            consultationModel.beanItemContainer.addBean(basicEvent);
        }
    }
}
