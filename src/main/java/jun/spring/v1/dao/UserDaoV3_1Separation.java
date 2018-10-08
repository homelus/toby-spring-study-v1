package jun.spring.v1.dao;

import jun.spring.v1.dao.support.SimpleConnectionMaker;
import jun.spring.v1.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    확장의 단점
    1. 다른 목적으로 이미 사용중이라면 다중 상속은 지원하지 않기 때문에 확장이 어렵다.
    2. 슈퍼클래스 내부 변경 시 함께 수정하거나 개발해야 하므로 관계가 생각보다 밀접하다.
    3. DB 커넥션을 생성하는 코드를 다른 DAO 클래스에 적용할 수 없음.

    -> 계승이 아닌 구성(클래스의 분리)
    1. 하위 클래스가 아닌 독립적인 클래스로 생성
    2. 외부의 오브젝트를 내부에서 만들어서 사용

    하지만 Userdao 수정 없이 DB 커넥션을 변경할 수 있는 방법이 없음.
    EX) Connection c = simpleConnectionMaker.openConnection();

 */
public class UserDaoV3_1Separation {

    private SimpleConnectionMaker simpleConnectionMaker;

    public UserDaoV3_1Separation () {
        simpleConnectionMaker = new SimpleConnectionMaker();
    }

    public void add(User user) throws ClassNotFoundException, SQLException {

        Connection c = simpleConnectionMaker.makeNewConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {

        Connection c = simpleConnectionMaker.makeNewConnection();

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
