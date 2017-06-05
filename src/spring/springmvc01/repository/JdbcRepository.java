package spring.springmvc01.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import spring.springmvc01.bean.Pan;

@Repository
public class JdbcRepository implements PanRepository {

	private static final String INSERT_USERS = "INSERT INTO users(username,password,enabled) VALUES(?,?,?)";
	private static final String QUERY_USERS = "SELECT username,password,enabled FROM users WHERE username = ?";
	private static final String QUERY_ROLES = "SELECT authority FROM authorities WHERE username = ?";

	@Autowired
	private JdbcOperations jdbcOperations;

	@Override
	public void save(Pan pan) {
		jdbcOperations.update(INSERT_USERS, pan.getName(), pan.getPassWord(), pan.getEnabled());
	}

	@Override
	public Pan findHomeByUserName(String username) {
		return jdbcOperations.queryForObject(QUERY_USERS, new PanRowMapper(), username);

	}
	
	@Override
	public List<String> findRolesByUserName(String username) {
		return jdbcOperations.queryForList(QUERY_ROLES, String.class,username);

	}

	public static final class PanRowMapper implements RowMapper<Pan> {

		@Override
		public Pan mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Pan(rs.getString("username"), rs.getString("password"), rs.getInt("enabled"));
		}

	}

}
