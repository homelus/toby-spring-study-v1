package jun.spring.v1.dao.support;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMakers implements ConnectionMaker {

    int count = 0;
    private ConnectionMaker realConnectionMaker;

    public CountingConnectionMakers(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        count++;
        return realConnectionMaker.makeConnection();
    }

    public int getCount() {
        return count;
    }
}
