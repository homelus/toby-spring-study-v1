package jun.spring.v1.etc.event;

import org.springframework.context.ApplicationEvent;

public class DepositEvent extends ApplicationEvent {

    public DepositEvent(Object source, String bankCardID, String depositDate) {
        super(source);
        this.bankCardID  = bankCardID;
        this.depositDate = depositDate;
    }

    private String bankCardID;

    private String depositDate;

    public String getBankCardID() {
        return bankCardID;
    }

    public void setBankCardID(String bankCardID) {
        this.bankCardID = bankCardID;
    }

    public String getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

}
