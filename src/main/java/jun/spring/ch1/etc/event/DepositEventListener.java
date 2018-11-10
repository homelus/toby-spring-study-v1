package jun.spring.ch1.etc.event;

import org.springframework.context.ApplicationListener;

public class DepositEventListener implements ApplicationListener<DepositEvent> {

    public void onApplicationEvent(DepositEvent depositEvent) {
        String bankCardID = depositEvent.getBankCardID();
        String depositDate = depositEvent.getDepositDate();

        System.out.println("Receive deposit event, bank info, card id : " + bankCardID + ", deposit date : " + depositDate);
    }
}
