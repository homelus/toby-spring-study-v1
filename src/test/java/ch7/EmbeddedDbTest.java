package ch7;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EmbeddedDbTest {

    EmbeddedDatabase db;
    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:sqlRegistrySchema.sql")
                .addScript("classpath:data.sql")
                .build();

        jdbcTemplate = new JdbcTemplate(db);
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void initData() {
        assertThat(jdbcTemplate.queryForObject("select count(*) from sqlmap", int.class), is(2));

        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from sqlmap order by key_");
        assertThat((String) list.get(0).get("key_"), is("KEY1"));
        assertThat((String) list.get(0).get("sql_"), is("SQL1"));
        assertThat((String) list.get(1).get("key_"), is("KEY2"));
        assertThat((String) list.get(1).get("sql_"), is("SQL2"));
    }

    @Test
    public void insert() {
        jdbcTemplate.update("insert into sqlmap(key_, sql_) values(?,?)", "KEY3", "SQL3");

        assertThat(jdbcTemplate.queryForObject("select count(*) from sqlmap", int.class), is(3));
    }

}
