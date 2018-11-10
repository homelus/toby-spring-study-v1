package jun.spring.ch1.etc.event;

import org.springframework.context.ApplicationListener;

public class WithdrawEventListener implements ApplicationListener<WithdrawEvent> {

    public void onApplicationEvent(WithdrawEvent withdrawEvent) {
        String bankCardID = withdrawEvent.getBankCardID();
        String withdrawDate = withdrawEvent.getWithdrawDate();

        System.out.println("Receive deposit event, bank info, card id : " + bankCardID + ", withdraw date : " + withdrawDate);
    }
}
