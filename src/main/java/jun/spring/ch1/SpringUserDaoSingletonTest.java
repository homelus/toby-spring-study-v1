package jun.spring.ch1;

import jun.spring.ch1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.ch1.factory.DaoFactoryV1;
import jun.spring.ch1.factory.SpringDaoFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/*
    애플리케이션 컨텍스트 = 싱글톤을 저장하고 관리하는 싱글톤 레지스트리(별다른 설정이 없다면 빈 오브젝트는 모두 싱글톤)
    대규모 엔터프라이즈 서버환경에서 만들어지는 수많은 오브젝트 생성 비용을 절감하기 위해 싱글톤을 사용
    GC 기술이 발달하더라도 많은 부하는 서버가 감당하기 힘듦
 */
public class SpringUserDaoSingletonTest {

    public static void main(String[] args) {

        DaoFactoryV1 factory = new DaoFactoryV1();
        UserDaoV3_3RelationSeparation userDao1 = factory.userDao();
        UserDaoV3_3RelationSeparation userDao2 = factory.userDao();

        System.out.println("## 오브젝트 팩토리에서 생성한 오브젝트(userDao)의 동일성 비교");
        System.out.println(userDao1);
        System.out.println(userDao2);
        System.out.println("동일 여부 : " + (userDao1 == userDao2) + " \n");

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringDaoFactory.class);

        UserDaoV3_3RelationSeparation userDao3 = context.getBean("userDao", UserDaoV3_3RelationSeparation.class);
        UserDaoV3_3RelationSeparation userDao4 = context.getBean("userDao", UserDaoV3_3RelationSeparation.class);

        System.out.println("## 애플리케이션 컨텍스트에서 생성한 오브젝트(userDao)의 동일성 비교");
        System.out.println(userDao3);
        System.out.println(userDao4);
        System.out.println("동일 여부 : " + (userDao3 == userDao4) + " \n");

    }

}
