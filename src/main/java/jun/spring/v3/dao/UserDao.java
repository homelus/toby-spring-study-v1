package jun.spring.v3.dao;

import jun.spring.v1.user.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(final User user) throws SQLException {
        class AddStatement implements StatementStrategy{
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        }
        StatementStrategy strategy = new AddStatement();
        jdbcContextWithStatementStrategy(strategy);
    }

    public void deleteAll() throws SQLException {
        StatementStrategy strategy = new DeleteAllStatement();
        jdbcContextWithStatementStrategy(strategy);
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy strategy) throws SQLException {

        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();

            ps = strategy.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally  {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignore) {}
            }
            if (c != null) {
                try {
                    c.close();
                }  catch (SQLException ignore) {}
            }
        }
    }

    public User get(String id) throws SQLException {

        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        User user = null;

        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if (user == null)
            throw new EmptyResultDataAccessException(1);

        return user;
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();

            rs.next();
            int count = rs.getInt(1);
            return count;
        } catch (SQLException e) {
            throw e;
        } finally {
            if  (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignore) {}
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignore) {}
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException ignore) {}
            }
        }
    }

}
