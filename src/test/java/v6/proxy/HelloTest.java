package v6.proxy;

import jun.spring.ch6.example.Hello;
import jun.spring.ch6.example.HelloTarget;
import jun.spring.ch6.example.HelloUppercase;
import jun.spring.ch6.example.UppercaseHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HelloTest {

    String defaultName = "Jun";

    @Test
    public void simpleProxy() {

        Hello hello = new HelloTarget();

        assertThat(hello.sayHello(defaultName), is("Hello Jun"));
        assertThat(hello.sayHi(defaultName), is("Hi Jun"));
        assertThat(hello.sayThankYou(defaultName), is("Thank You Jun"));

        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertThat(proxiedHello.sayHello(defaultName), is("HELLO JUN"));
        assertThat(proxiedHello.sayHi(defaultName), is("HI JUN"));
        assertThat(proxiedHello.sayThankYou(defaultName), is("THANK YOU JUN"));
    }

    @Test
    public void dynamicProxyTest() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {Hello.class}, new UppercaseHandler(new HelloTarget()));

        assertThat(proxiedHello.sayHello(defaultName), is("HELLO JUN"));
        assertThat(proxiedHello.sayHi(defaultName), is("HI JUN"));
        assertThat(proxiedHello.sayThankYou(defaultName), is("THANK YOU JUN"));
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello(defaultName), is("HELLO JUN"));
        assertThat(proxiedHello.sayHi(defaultName), is("HI JUN"));
        assertThat(proxiedHello.sayThankYou(defaultName), is("THANK YOU JUN"));
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello(defaultName), is("HELLO JUN"));
        assertThat(proxiedHello.sayHi(defaultName), is("HI JUN"));
        assertThat(proxiedHello.sayThankYou(defaultName), is("Thank You Jun"));
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            String ret = (String) methodInvocation.proceed();
            return ret.toUpperCase();
        }
    }

}
