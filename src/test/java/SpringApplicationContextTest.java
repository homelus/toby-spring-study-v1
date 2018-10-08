import jun.spring.v1.etc.event.DepositEventPublisher;
import jun.spring.v1.etc.event.WithdrawEventPublisher;
import jun.spring.v1.etc.resource.CustomResourceLoader;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.io.IOException;
import java.util.Locale;

public class SpringApplicationContextTest {

    /*
        Application Context 제공
        1. Bean Factory methods for accessing application components. Inherited from ListenableBeanFactory
        2. The ability to load file resource in a generic fashion. Inherited from the ResourceLoader
        3. The ability to publish events to registered listeners. Inherited from the ApplicationEventPublisher
        4. The ability to resolve messages, supporting internationalization. Inherited from the Message Source
        5. Inheritance from a parent context. Definitions in a descendant context will always take priority.

     */
    @Test
    public void applicationContextTest() throws IOException {

        ApplicationContext context = new GenericXmlApplicationContext("applicationContext_ETC.xml");

        // 2. Resource Test
        CustomResourceLoader customResourceLoader = (CustomResourceLoader) context.getBean("customResourceLoader");
        customResourceLoader.showResourceData("test.txt");

        System.out.println();

        // 3. Publish Test
        DepositEventPublisher depositPublisher = (DepositEventPublisher) context.getBean("depositEventPublisher");
        WithdrawEventPublisher withdrawPublisher = (WithdrawEventPublisher) context.getBean("withdrawEventPublisher");
        depositPublisher.publishDepositEvent("TEST1", "2018/10/08");
        withdrawPublisher.publishWithdrawEvent("TEST2", "2018/10/09");

        System.out.println();

        // 4. Message Source Test
        String message = context.getMessage("msg.text", null, Locale.US);
        System.out.println(message);

    }

}
