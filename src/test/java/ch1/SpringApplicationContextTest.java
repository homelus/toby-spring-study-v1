package ch1;

import jun.spring.v1.etc.LazyBean;
import jun.spring.v1.etc.LifeCycleBean;
import jun.spring.v1.etc.StandardBean;
import jun.spring.v1.etc.annotation.SpecialAnnotation;
import jun.spring.v1.etc.event.DepositEventPublisher;
import jun.spring.v1.etc.event.WithdrawEventPublisher;
import jun.spring.v1.etc.resources.CustomResourceLoader;
import jun.spring.v1.factory.CountingDaoFactory;
import jun.spring.v1.factory.SpringDaoFactory;
import jun.spring.v1.factory.SpringDaoFactoryV1_Scope;
import jun.spring.v1.user.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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

        @see https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationContext.html
     */
    @Test
    public void applicationContextTest() throws IOException {

        ApplicationContext context = new GenericXmlApplicationContext("applicationContext_ETC.xml");

        // 2. Resource Test
        CustomResourceLoader customResourceLoader = (CustomResourceLoader) context.getBean("customResourceLoader");
        customResourceLoader.showResourceData("test.txt");

        System.out.println();

        // 3. Publish Test
//        ((GenericXmlApplicationContext) context).addApplicationListener(new WithdrawEventListener());
        DepositEventPublisher depositPublisher = (DepositEventPublisher) context.getBean("depositEventPublisher");
        WithdrawEventPublisher withdrawPublisher = (WithdrawEventPublisher) context.getBean("withdrawEventPublisher");
        depositPublisher.publishDepositEvent("TEST1", "2018/10/08");
        withdrawPublisher.publishWithdrawEvent("TEST2", "2018/10/09");

        System.out.println();

        // 4. Message Source Test
        String message = context.getMessage("msg.text", null, Locale.US);
        System.out.println(message);

    }

    @Test
    public void beanScopeTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringDaoFactoryV1_Scope.class);
        User singletonUser1 = context.getBean("singletonUser", User.class);
        User singletonUser2 = context.getBean("singletonUser", User.class);
        User prototypeUser1 = context.getBean("prototypeUser", User.class);
        User prototypeUser2 = context.getBean("prototypeUser", User.class);

        System.out.println("singleton 동일성 여부 : " + (singletonUser1 == singletonUser2));
        System.out.println("prototype 동일성 여부 : " + (prototypeUser1 == prototypeUser2));

    }

    @Test
    public void variousApplicationContextStrategies() {

        ApplicationContext context = new GenericXmlApplicationContext("applicationContext_ETC.xml");

        /* 1. 다중 팩토리 */
        ApplicationContext multipleFactoryContext = new AnnotationConfigApplicationContext(SpringDaoFactory.class, CountingDaoFactory.class);

        /* 2. 다양한 IoC 기능 제공 */
        // 생성 시점 수정
        System.out.println("컨텍스트 생성 완료");
        context.getBean("standardBean");
        context.getBean("lazyBean");

        System.out.println("\n");

        // 후 처리 예제
        LifeCycleBean bean = context.getBean("lifeCycleBean", LifeCycleBean.class);
        bean.print();
        ((GenericXmlApplicationContext) context).removeBeanDefinition("lifeCycleBean");

        System.out.println("\n");

        /*  3. 다양한 빈 검색 방법 예제 */
        StandardBean standardBean = context.getBean(StandardBean.class);
        standardBean.identity();
        System.out.println("빈 개수 : " + context.getBeanDefinitionCount());
        System.out.println((context.getBeanNamesForType(LazyBean.class))[0]);
        context.getBeansWithAnnotation(SpecialAnnotation.class);


    }

}
