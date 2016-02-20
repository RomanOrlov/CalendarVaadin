package model;

import backend.basicevent.ConsultationBasicEvent;
import com.vaadin.data.util.BeanItemContainer;

/**
 * Created by user on 20.02.2016.
 */
public class ConsultationModel {



    public final BeanItemContainer<ConsultationBasicEvent> consultationBasicEventBeanItemContainer = new BeanItemContainer<>(ConsultationBasicEvent.class);



    // получаем контейнер
    public BeanItemContainer<? extends  ConsultationBasicEvent> getConsultationBAsicEventContainer() {
        return consultationBasicEventBeanItemContainer;
    }

}
