package jun.spring.ch1.etc.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class WithdrawEventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        eventPublisher = applicationEventPublisher;
    }

    public void publishWithdrawEvent(String bankCardID, String withdrawDate) {
        System.out.println("Will publish withdraw event, card id : " + bankCardID + ", date : " + withdrawDate);
        WithdrawEvent wdEvent = new WithdrawEvent(this, bankCardID, withdrawDate);
        this.eventPublisher.publishEvent(wdEvent);
    }

}
