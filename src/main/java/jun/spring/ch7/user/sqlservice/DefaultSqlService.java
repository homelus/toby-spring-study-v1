package jun.spring.ch7.user.sqlservice;

public class DefaultSqlService extends BaseSqlService {

    // 생성자에서 디폴트 의존 오브젝트를 직접 만들어 스스로 DI 한다.
    public DefaultSqlService() {
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
