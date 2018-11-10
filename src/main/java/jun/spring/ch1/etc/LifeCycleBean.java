package jun.spring.ch1.etc;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class LifeCycleBean implements InitializingBean, DisposableBean {

    public LifeCycleBean() {
        System.out.println("Bean 생성자 호출");
    }

    public void print() {
        System.out.println("Bean's LifeCycle");
    }

    @PostConstruct
    public void init() {
        System.out.println("PostConstruct Annotation : 생성 후처리");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("PreDestroy Annotation : 종료 전처리");
    }

    public void destroy() throws Exception {
        System.out.println("DisposableBean Interface : 소멸");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean Interface : 생성 및 초기화");
    }
}
