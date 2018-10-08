package jun.spring.v1.dao;

import jun.spring.v1.dao.support.ConnectionMaker;
import jun.spring.v1.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    다른 관계를 맺으려면 클래스 내 수정이 필요.
    코드안에 존재하는 오브젝트와 오브젝트의 관계를 분리해야 할 필요생김

    생성자에서 가지고 있던 책임을 클라이언트에게 넘기면 UserDao는 인터페이스와의 관계만 발생
    즉, ConnectionMaker 인터페이스가 변하면 영향을 직접적으로 받지만, 구현 클래스(DConnectionMaker) 등이 다른것으로 바뀌거나 내부 메소드변화가 생겨도 영향을 받지 않음.
    인터페이스에 대해서만 의존관계를 만들면 구현 클래스와의 관계는 느슨해지고 변화에 영향을 덜 받는다.(결합도가 낮다)

    A. 의존관계 주입(Dependency Injection)
     a. 의존관계 : A, B 라는 두클래스 에서 A가 B에 의존(사용)한다면 B가 변하면 A에 영향을 미친다는 의미 (반면 B는 A에 영향을 미치지 않음 - 방향성이 있음)
    구체적인 의존 오브젝트 (런타임 시에 의존관계를 맺는 대상) 와 그것을 사용할 주체, 보통 클라이언트라고 부르는 오브젝트를 런타임 시에 연결해주는 작업.
    다음 세가지를 반드시 충족

    1. 인터페이스에만 의존 (런타임 시점의 의존관계는 드러나지 않음)
    2. 런타임 시점의 의존관계는 컨테이너나 팩토리 같은 제 3의 존재가 결정
    3. 의존관계는 사용할 오브젝트에 대한 레퍼런스를 외부에서 제공(주입)해줌으로써 만들어진다.

 */
public class UserDaoV3_3RelationSeparation {

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
