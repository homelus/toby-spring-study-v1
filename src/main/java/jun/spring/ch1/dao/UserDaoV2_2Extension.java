package jun.spring.ch1.dao;

import jun.spring.ch1.user.User;

import java.sql.*;

/*
    확장을 통한 분리
    1. 어떻게 데이터를 등록하고 가져올 것인가(Connection을 어떤 기능을 사용할 것인가)
    2. DB 연결 방법은 어떻게 할 것인가(어떤 방법으로 Connection을 만들것인가)
 */
public abstract class UserDaoV2_2Extension {

    public void add(User user) throws ClassNotFoundException, SQLException {

        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

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

    // 메소드를 필요에 맞게 구현해서 사용하도록 하는 방법 - 템플릿 메소드 패턴
    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

}

/*
    슈퍼클래스의 기능을 확장할 때, 서브 클래스에서 구체적인 오브젝트 생성 방법을 결정하는 방법 -  팩토리 메소드 패턴
    PS) 훅 메소드 - 필요에 따라 기존 기능을 오버라이드하는 메소드, Abstract 메소드 - 반드시 구현
*/
class NUserDao extends UserDaoV2_2Extension {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // N사 DB 생성코드
        return null;
    }
}

class DUserDao extends UserDaoV2_2Extension {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // D사 DB 생성코드
        return null;
    }
}

