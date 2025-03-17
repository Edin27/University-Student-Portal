package model;

import java.util.*;

public class CourseManager {

	private static volatile CourseManager courseManagerInstance;
	private final Collection<Course> courses = new ArrayList<>();

	private CourseManager(){}
	public static CourseManager getCourseManager(){
		CourseManager result = courseManagerInstance;
		if(courseManagerInstance == null){
			synchronized (CourseManager.class){
				result = courseManagerInstance;
				if (result == null){
					courseManagerInstance = result = new CourseManager();
				}
			}
		}
		return result;
	}

	public Collection<Course> getCourses() {
		return courses;
	}

	public boolean addCourse(String code, String name, String description,
					 Boolean requiresComputers, String COName, String COEmail,
					 String CSName, String CSEmail, Integer reqTutorials,
							 Integer reqLabs){
		boolean added = false;

		//check required elements all provided
		boolean courseInfoProvided = false;

		//check whether course code is valid
		boolean courseCodeValid = false;

		//check whether course code already added
		boolean hasCode = false;
		while (!hasCode){
			for(Course course: courses){
				hasCode = course.hasCode(code);
			}
		}
		//if course already exists, display error




		Course newCourse = new Course(code, name, description, requiresComputers,
				COName, COEmail, CSName, CSEmail, reqTutorials, reqLabs);
		courses.add(newCourse);
		added = true;
		return added;
	}
}
