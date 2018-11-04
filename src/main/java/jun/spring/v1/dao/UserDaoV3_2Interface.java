package jun.spring.v1.dao;

import jun.spring.v1.dao.support.client.DConnectionMaker;
import jun.spring.v1.dao.support.ConnectionMaker;
import jun.spring.v1.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    두개의 클래스가 서로 긴밀하게 연결되지 않도록 중간에 느슨한 연결고리 생성   - 인터페이스

    이 코드의 문제 DConnection 클래스의 생성자를 호출해서 생성하는 코드가 아직도 남아있다(코드 속에 미리 결정되어 있다)

    모델링의 의존관계 뿐만 아니라 런타임 의존관계까지 UserDaoJdbc 가 결정하고 관리하고 있음.
    IoC 방식은 UserDaoJdbc 로부터 런타임 의존관계 코드를 제거하고 제 3의 존재에게 결정 권한을 위임. (DI)

    다음 예제로
*/
public class UserDaoV3_2Interface {

    private ConnectionMaker connectionMaker;

    public UserDaoV3_2Interface () {
        connectionMaker = new DConnectionMaker();
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
