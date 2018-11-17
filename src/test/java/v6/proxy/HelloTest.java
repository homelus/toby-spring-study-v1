package v6.proxy;

import jun.spring.ch6.example.Hello;
import jun.spring.ch6.example.HelloTarget;
import jun.spring.ch6.example.HelloUppercase;
import jun.spring.ch6.example.UppercaseHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
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

    @Test
    public void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };

        classMethodPointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {};
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {};
        checkAdviced(new HelloToby(), classMethodPointcut, true);

    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            String ret = (String) methodInvocation.proceed();
            return ret.toUpperCase();
        }
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) pfBean.getObject();

        if (adviced) {
            assertThat(proxiedHello.sayHello(defaultName), is("HELLO JUN"));
            assertThat(proxiedHello.sayHi(defaultName), is("HI JUN"));
            assertThat(proxiedHello.sayThankYou(defaultName), is("Thank You Jun"));
        } else {
            assertThat(proxiedHello.sayHello(defaultName), is("Hello Jun"));
            assertThat(proxiedHello.sayHi(defaultName), is("Hi Jun"));
            assertThat(proxiedHello.sayThankYou(defaultName), is("Thank You Jun"));
        }
    }
}
