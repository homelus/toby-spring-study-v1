package jun.spring.v1.dao.support;

import java.sql.Connection;
import java.sql.SQLException;

/*
    자신의 관심사인 DB 연결횟수 카운팅 작업만 진행. 나머지는 DI 받은 실제 Connection 에게 위임.
 */
public class CountingConnectionMakers implements ConnectionMaker {

    int count = 0;
    private ConnectionMaker realConnectionMaker;

    // realConnection 정보를 DI 받아옴.
    public CountingConnectionMakers(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    // 자신이 할 일만 하고 DI 받은 realConnection 에게 중요 기능을 위임.
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        count++;
        return realConnectionMaker.makeConnection();
    }

    // 카운트를 조회
    public int getCount() {
        return count;
    }
}
