package jun.spring.ch1.dao;

import jun.spring.ch1.user.User;

import java.sql.*;

/*
    왜 이 코드에 문제가 많을까?
    잘 동작하는 코드를 굳이 수정하고 개선해야 하는 이유는?
    개선했을 때의 장점은?
    장점이 주는 유익은?
    객체지향 설계의 원칙과는 어떤 상관이 있는가?
    개선과 현재를 비교했을 때 스프링을 사용한 개발에서의 차이점은?
*/
public class UserDaoV1 {

    // 예외는 잡아서 모두 던진다.
    public void add(User user) throws ClassNotFoundException, SQLException {

        Class.forName("org.mariadb.jdbc.Driver");
        // DB를 위한 커넥션을 가져온다.
        Connection c = DriverManager.getConnection("jdbc:mariadb://localhost/spring_test", "root", "1234");

        // SQL을 담는 PreparedStatement를 만든다.
        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        // statement 를 실행
        ps.executeUpdate();

        // 생성된 리소스는 작업을 마친후 반드시 닫는다.
        ps.close();
        c.close();
    }

    // 예외는 잡아서 모두 던진다.
    public User get(String id) throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        // DB를 위한 커넥션을 가져온다.
        Connection c = DriverManager.getConnection("jdbc:mariadb://localhost/spring_test", "root", "1234");

        // SQL을 담는 PreparedStatement를 만든다.
        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        // statement를 실행 & ResultSet 으로 결과를 받는다.
        ResultSet rs = ps.executeQuery();

        // 결과값을 오브젝트에 담는다.
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        // 생성된 리소스는 작업을 마친후 반드시 닫는다.
        rs.close();
        ps.close();
        c.close();

        return user;
    }

}
