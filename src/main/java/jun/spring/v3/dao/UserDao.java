package jun.spring.v3.dao;

import jun.spring.v3.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private JdbcContext jdbcContext;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcContext = new JdbcContext();
        jdbcContext.setDataSource(dataSource);
        this.dataSource = dataSource;
    }

    public void add(final User user) throws SQLException {
        jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
    }

    public void deleteAll() throws SQLException {
        this.jdbcTemplate.update("delete from users");
    }

    public User get(String id) throws SQLException {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        });
    }

    public int getCount() throws SQLException {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id",
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user = new User();
                        user.setId(resultSet.getString("id"));
                        user.setName(resultSet.getString("name"));
                        user.setPassword(resultSet.getString("password"));
                        return user;
                    }
                });
    }
}
