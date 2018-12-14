package ch7;

import jun.spring.sqlservice.jaxb.SqlType;
import jun.spring.sqlservice.jaxb.Sqlmap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:castor-unmarshaller-context.xml")
public class CastorTest {

    @Autowired
    private Unmarshaller unmarshaller;

    @Test
    public void unmarshallerSqlMap() throws IOException {
        // JAXB에 의존하는 부분이 없다. (OXM 추상화)
        Source xmlSource = new StreamSource(getClass().getClassLoader().getResourceAsStream("sqlmap.xml"));
        Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);

        checkSqlList(sqlmap);
    }

    private void checkSqlList(Sqlmap sqlmap) {
        List<SqlType> sqlList = sqlmap.getSql();

        assertThat(sqlList.size(), is(3));
        assertThat(sqlList.get(0).getKey(), is("add"));
        assertThat(sqlList.get(0).getValue(), is("insert"));
        assertThat(sqlList.get(1).getKey(), is("get"));
        assertThat(sqlList.get(1).getValue(), is("select"));
        assertThat(sqlList.get(2).getKey(), is("delete"));
        assertThat(sqlList.get(2).getValue(), is("delete"));
    }

}
