package ivan.projects.hakatonbackend.repository

import ivan.projects.hakatonbackend.domain.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class UserRepositoryImpl(private val jdbcTemplate: JdbcTemplate) : UserRepository {

    companion object{
        private const val SELECT_USER_BY_ID = "SELECT * FROM Users WHERE id=?"
    }

    private val userRowMapper : RowMapper<User> = object : RowMapper<User> {
        override fun mapRow(rs: ResultSet, rowNum: Int): User? {
            return User(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("password")
            )
        }
    }

    override fun findUserById(id: String): User? {
        return jdbcTemplate.queryForObject(
            SELECT_USER_BY_ID,
            arrayOf(id),
            userRowMapper
        )
    }
}