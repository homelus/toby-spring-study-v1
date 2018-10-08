package jun.spring.v1.dao;

import jun.spring.v1.user.User;

import java.sql.*;

/*
    관심사항
    1. DB 연결과정
    2. 파라미터의 정보를 Statement 에 바인딩하고 SQL을 DB를 통해 실행시키는 과정
    3. 공유 리소스를 시스템에 돌려주는 과정
 */
public class UserDaoV2_1Separation {

    public void add(User user) throws ClassNotFoundException, SQLException {

        // DB를 위한 커넥션을 가져온다.
        Connection c = getConnection();

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

    public User get(String id) throws ClassNotFoundException, SQLException {
        // DB를 위한 커넥션을 가져온다.
        Connection c = getConnection();

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

    // 중복된 코드를 별도의 메소드로 분리하여 DB 연결의 관심사를 해결(리팩토링 메소드 추출기법)
    // DB 연결 정보의 변경이 일어났을 경우 한 코드만 수정
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mariadb://localhost/spring_test", "root", "1234");
    }

}
