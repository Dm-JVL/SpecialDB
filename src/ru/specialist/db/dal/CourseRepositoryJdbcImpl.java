package ru.specialist.db.dal;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class CourseRowMaper {
	
	public Course map(ResultSet r) throws SQLException {
		Course c = new Course();
		c.setId(r.getInt("id"));
		c.setTitle(r.getString("title"));
		c.setLength(r.getInt("length"));
		c.setDescription(r.getString("description"));
		return c;
	}
}

public class CourseRepositoryJdbcImpl implements CourseRepository, Closeable{
	final String SQL_SELECT = "SELECT id, title, length, description FROM courses ";
	final String SQL_SELECT_ID = SQL_SELECT + " WHERE id = ?";
	final String SQL_SELECT_TITLE = SQL_SELECT + " WHERE title LIKE ?";
	final String SQL_UPDATE = 
			"UPDATE courses SET title = ?, length = ?, description = ? WHERE id = ?";
	
	public static final String DRIVER_NAME = 
			"com.mysql.jdbc.Driver"; 
	public static final String CONNECTION_STRING = 
		"jdbc:mysql://localhost:3306/web?user=root&password=demo";
	
	private Connection conn;
	
	public CourseRepositoryJdbcImpl() {
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(CONNECTION_STRING);
		}
		catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Course findById(int id) {
		try {
			PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_ID);
			cmd.setInt(1, id);
			ResultSet r = cmd.executeQuery();
			if (r.next())
				return new CourseRowMaper().map(r);
			else
				return null;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Course> findAll() {
		try {
			Statement cmd = conn.createStatement();
			ResultSet r = cmd.executeQuery(SQL_SELECT);
			var mapper = new CourseRowMaper();
			List<Course> result = new ArrayList<Course>();
			while(r.next())
				result.add( mapper.map(r) );
			return result;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean update(Course c) {
		try {
			PreparedStatement cmd = conn.prepareStatement(SQL_UPDATE);
			cmd.setInt(4, c.getId());
			cmd.setString(1, c.getTitle());
			cmd.setInt(2, c.getLength());
			cmd.setString(3, c.getDescription());
			
			return (cmd.executeUpdate() == 1);
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void close() {
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
	}

}
