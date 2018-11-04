package jun.spring.v1.dao;

import jun.spring.v1.dao.support.ConnectionMaker;
import jun.spring.v1.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    다른 오브젝트와 관계를 맺으려면 클래스 내 수정이 필요.
    코드안에 존재하는 오브젝트와 오브젝트의 관계를 분리해야 할 필요생김

    생성자에서 가지고 있던 책임을 클라이언트에게 넘기면 UserDao는 인터페이스와의 관계만 발생
    즉, ConnectionMaker 인터페이스가 변하면 영향을 직접적으로 받지만, 구현 클래스(DConnectionMaker) 등이 다른것으로 바뀌거나 내부 메소드변화가 생겨도 영향을 받지 않음.
    인터페이스에 대해서만 의존관계를 만들면 구현 클래스와의 관계는 느슨해지고 변화에 영향을 덜 받는다.(결합도가 낮다)
 */
public class UserDaoV3_3RelationSeparation {

    /*
        UserDaoJdbc(A) 가 ConnectionMaker(B) 에 의존함 => UserDaoJdbc 가 ConnectionMaker 를 사용하고 있음.
        의존관계 A -> B

        ConnectionMaker 가 바뀌면 UserDaoJdbc 에 영향을 미친다. 하지만 UserDaoJdbc 가 바뀌면 ConnectionMaker 에 영향을 미치지 않는다.

        여기서 중요한 점은 ConnectionMaker <인터페이스> 에 의존한다는 점이다.
        인터페이스가 변경되지 않는 이상 서로에게 영향을 미치지 않기 때문에 결합도가 낮다.

        ConnectionMaker 는 UserDaoJdbc 가 만들어지고 나서 런타임 시에 구체적인 의존관계를 알수 있으므로 의존 오브젝트라고 한다.
     */
    private ConnectionMaker connectionMaker;

    public UserDaoV3_3RelationSeparation (ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
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
