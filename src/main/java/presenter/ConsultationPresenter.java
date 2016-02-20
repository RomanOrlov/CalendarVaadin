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


    public final ConsultationModel consultationModel;

    public final ConsultationManager consultationManager;

    public ConsultationBasicEvent consultationBasicEvent;


    private static ArrayList<Consultation> consultations = new ArrayList<>();

    ArrayList<String> executor = new ArrayList<>(Arrays.asList("физик", "онколог", "планировщик", "врач", "лечащий врач"));


    public ConsultationPresenter(ConsultationModel consultationModel, ConsultationManager consultationManager) {
        this.consultationModel = consultationModel;
        this.consultationManager = consultationManager;
    }

    public void start()
    {

        GregorianCalendar calendar= new GregorianCalendar(2016, 1, 1);
        Date startDay = calendar.getTime();
        calendar.add(calendar.MONTH, 1);
        Date endDay = calendar.getTime();


        consultations = new ArrayList<>(consultationManager.listConsultation(startDay, endDay));

        for (int i=0; i < consultations.size();i++)
        {
            Random random = new Random();
             int value = random.nextInt(executor.size());
            consultationBasicEvent = new ConsultationBasicEvent(consultations.get(i),executor.get(value),"Радиохирургия");
            consultationModel.consultationBasicEventBeanItemContainer.addBean(consultationBasicEvent);
        }

    }
}
