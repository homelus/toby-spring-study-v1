package jun.spring.v1.etc.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class DepositEventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    public void publishDepositEvent(String bankCardID, String depositDate) {
        System.out.println("Will publish deposit event, card id : " + bankCardID + ", date : " + depositDate);
        DepositEvent dEvent = new DepositEvent(this, bankCardID, depositDate);
        this.eventPublisher.publishEvent(dEvent);
    }
}
