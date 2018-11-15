package jun.spring.ch6.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        System.out.println("send Mail " +  simpleMailMessage.getSubject());
    }

    @Override
    public void send(SimpleMailMessage... simpleMailMessages) throws MailException {
        System.out.println("send Mail Messages");
    }
}
