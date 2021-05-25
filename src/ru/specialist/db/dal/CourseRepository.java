package ru.specialist.db.dal;

import java.io.Closeable;
import java.util.List;

public interface CourseRepository extends Closeable {
	
	Course findById(int id);
	List<Course> findAll();
	//List<Course> findByTitle(String title);
	
	//Course add(Course c); // c.setId(..)
	boolean update(Course c);
	//boolean delete(int id);
	

}
