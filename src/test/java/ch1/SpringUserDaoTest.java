package ch1;

import jun.spring.ch1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.ch1.dao.UserDaoV5_MethodInjection;
import jun.spring.ch1.dao.UserDaoV6_DataSource;
import jun.spring.ch1.factory.SpringDaoFactory;
import jun.spring.ch1.factory.SpringDaoFactoryV2_DataSource;
import jun.spring.ch1.user.User;
import org.junit.Test;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class SpringUserDaoTest {

    /*
        오브젝트 팩토리(DaoFactory) vs 애플리케이션 컨텍스트 (= IoC 컨테이너, 스프링 컨테이너, 빈 팩토리)

        DaoFactory : Dao 오브젝트를 생성하고 관계를 맺어주는 제한적인 역할
        애플리케이션 컨텍스트 : 생성하고 관계를 맺는 코드는 없지만 생성정보와 연관관계 정보를 별도의 설정 정보를 통해 얻음

        애플리케이션 컨텍스트 장점(참조 SpringApplicationContextTest.java)
        1. 클라이언트는 구체적인 팩토리 클래스를 알 필요가 없다. (여러개의 팩토리인 경우 생성의 번거로움)
        2. 종합 IoC 서비스를 제공한다. (다양한 오브젝트의 생성 시점과 전략, 후처리, 정보조합, 설정 다변화, 인터셉팅 등)
        3. 빈을 검색하는 다양한 방법 제공
    */
    public void annotationConfigApplicationContextTest() throws SQLException, ClassNotFoundException {

        // 오브젝트 생성을 애플리케이션 컨텍스트에 요청
        // @Configuration 어노테이션이 표시된 자바 정보를 사용할 경우 AnnotationConfigApplicationContext 이용
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringDaoFactory.class);
        // ApplicationContext 가 관리하는 오브젝트를 요청하는 메소드를 이용
        UserDaoV3_3RelationSeparation userDao = context.getBean("userDao", UserDaoV3_3RelationSeparation.class);

        // 오브젝트 사용
        User user = new User();

        user.setId("jun");
        user.setName("윤현준");
        user.setPassword("hello");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());


        System.out.println(user2.getId() + " 조회 성공");
    }

    @Test
    public void genericXmlApplicationContextTest() throws SQLException, ClassNotFoundException {

        // 오브젝트 생성을 애플리케이션 컨텍스트에 요청
        // XML 에서 빈의 의존관계 정보를 이용해 IoC/DI 작업을 하는 ApplicationContext(경로 : classpath)
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        // 클래스 파일을 전달해서 상대 위치를 가져올 수 있는 ClassPathXmlApplicationContext 사용
//        ApplicationContext context = new ClassPathXmlApplicationContext("daoContext.xml", UserDaoV5_MethodInjection.class);

        UserDaoV5_MethodInjection userDao = context.getBean("userDao", UserDaoV5_MethodInjection.class);

        // 오브젝트 사용
        User user = new User();

        user.setId("jun");
        user.setName("윤현준");
        user.setPassword("hello");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");

    }

    @Test
    public void dataSourceTest() throws SQLException {

        // 오브젝트 생성을 팩토리에 요청
        UserDaoV6_DataSource userDao = new SpringDaoFactoryV2_DataSource().userDao();

        // 오브젝트 사용
        User user = new User();

        user.setId("jun");
        user.setName("윤현준");
        user.setPassword("hello");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());


        System.out.println(user2.getId() + " 조회 성공");

    }

    public void springTest() {

        SingletonBeanRegistry springBeanRegistry = new DefaultSingletonBeanRegistry();

    }



}
