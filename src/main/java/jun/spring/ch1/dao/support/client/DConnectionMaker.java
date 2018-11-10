package jun.spring.ch1.dao.support.client;

import jun.spring.ch1.dao.support.ConnectionMaker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {

    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        // D사의 독자적으로 Connection 생성 코드
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mariadb://localhost/spring_test", "root", "1234");
    }
}
