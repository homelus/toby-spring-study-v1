package jun.spring.ch1.dao;

import jun.spring.ch1.dao.support.ConnectionMaker;
import jun.spring.ch1.factory.SpringDaoFactory;
import jun.spring.ch1.user.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    의존관계 검색 (Dependency Lookup)
    의존 관계를 외부로부터 주입받지 않고 스스로 검색해서 자신이 필요로 하는 의존 오브젝트를 능동적으로 찾는다.

    의존관계 주입 vs 검색 ?
    의존관계 주입이 더 깔끔하다.
    애플리케이션 컴포넌트가 컨테이너와 같이 성격이 다른 오브젝트에 의존하는 것은 바람직하지 않다.
    의존관계 검색 방식에서는 자신이 스프링의 빈일 필요가 없지만, 의존관계 주입에서는 반드시 컨테이너가 만드는 빈이어야 한다.
 */
public class UserDaoV4_DependencyLookup {

    private ConnectionMaker connectionMaker;

    // 외부로 부터 주입이 아닌 스스로 IoC 컨테이너인 DaoFactory or Application Context 에게 요청
    public UserDaoV4_DependencyLookup() {

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringDaoFactory.class);
        this.connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);

//        SpringDaoFactory daoFactory = new SpringDaoFactory();
//        this.connectionMaker = daoFactory.connectionMaker();
    }

    public void add(User user) throws ClassNotFoundException, SQLException {

        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {

        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

}
