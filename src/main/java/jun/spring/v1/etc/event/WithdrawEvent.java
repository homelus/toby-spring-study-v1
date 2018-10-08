package jun.spring.v1.etc.event;

import org.springframework.context.ApplicationEvent;

public class WithdrawEvent extends ApplicationEvent {

    private String bankCardID;
    private String withdrawDate;

    public WithdrawEvent(Object source, String bankCardID, String withdrawDate) {
        super(source);
        this.bankCardID = bankCardID;
        this.withdrawDate = withdrawDate;
    }

    public String getBankCardID() {
        return bankCardID;
    }

    public void setBankCardID(String bankCardID) {
        this.bankCardID = bankCardID;
    }

    public String getWithdrawDate() {
        return withdrawDate;
    }

    public void setWithdrawDate(String withdrawDate) {
        this.withdrawDate = withdrawDate;
    }
}
