package jun.spring.ch7.user.sqlservice;

import jun.spring.ch7.user.exception.SqlRetrievalFailureException;
import jun.spring.ch7.user.sqlservice.jaxb.SqlType;
import jun.spring.ch7.user.sqlservice.jaxb.Sqlmap;
import jun.spring.ch7.user.sqlservice.reader.SqlReader;
import jun.spring.ch7.user.sqlservice.registry.HashMapSqlRegistry;
import jun.spring.ch7.user.sqlservice.registry.SqlRegistry;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class OxmSqlService implements SqlService {

    private final BaseSqlService baseSqlService = new BaseSqlService();
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmap(Resource sqlmap) {
        oxmSqlReader.setSqlmap(sqlmap);
    }

    @PostConstruct
    public void loadSql() {
        this.baseSqlService.setSqlReader(this.oxmSqlReader);
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);

        this.baseSqlService.loadSql();
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return baseSqlService.getSql(key);
        } catch (SqlRetrievalFailureException e) {
            throw new SqlRetrievalFailureException(e);
        }

    }

    private class OxmSqlReader implements SqlReader {

        private Unmarshaller unmarshaller;
        private Resource sqlmap = new ClassPathResource("jaxb/sqlmap.xml");

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Source source = new StreamSource(sqlmap.getInputStream());
                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(source);

                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(this.sqlmap.getFilename() + "을 가져올 수 없습니다.", e);
            }
        }
    }

}
