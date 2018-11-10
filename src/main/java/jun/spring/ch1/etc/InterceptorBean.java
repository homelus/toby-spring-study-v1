package jun.spring.ch1.etc;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class InterceptorBean implements MethodBeforeAdvice, AfterReturningAdvice {

    public void print() {
        System.out.println("Hello Interceptor Bean");
    }

    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        System.out.println("Method Intercept After Returning");
    }

    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("Method Intercept Before Executing");
    }
}
