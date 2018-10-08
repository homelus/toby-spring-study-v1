package jun.spring.v1.dao;

import jun.spring.v1.dao.support.ConnectionMaker;
import jun.spring.v1.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    스프링의 싱글톤 빈으로 사용되는 클래스의 개별적으로 바뀌는 정보는
    로컬 변수 or 파라미터로 사용해야 한다.

    하지만, 읽기 전용 or 자신이 사용하는 다른 싱글톤 빈 일 경우에는 사용해도 좋다.
    단순한 읽기 전용인 경우 static final or final 로 선언하는 편이 낫다.
 */
public class UserDaoSingletonSharedAntiExample {

    private ConnectionMaker connectionMaker;

    // 인스턴스 변수로 선언 - 멀티스레드 환경 사용에서 심각한 문제 발생
    private Connection c;
    private User user;

    public User get(String id) throws ClassNotFoundException, SQLException {

        this.c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        rs.next();
        this.user = new User();
        this.user.setId(rs.getString("id"));
        this.user.setName(rs.getString("name"));
        this.user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }


}
