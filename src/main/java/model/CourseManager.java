package model;

import view.TextUserInterface;
import view.View;
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

	public boolean addCourse(SharedContext sharedContext, View view, String code,
							 String name, String description, Boolean requiresComputers,
							 String COName, String COEmail, String CSName, String CSEmail,
							 Integer reqTutorials, Integer reqLabs){


		//check whether required elements all provided
		if (isAnyNullOrEmpty(code, name, description, requiresComputers, COName,
				COEmail, CSName, CSEmail, reqTutorials, reqLabs)){
			String errorMessage = "Required course info not provided";
			//TODO: add logger


			view.displayError(errorMessage);
			return false;
		}



		//check whether course code is valid
		if (!checkCourseCode(code)){
			String errorMessage = "Provided course code is invalid";
			//TODO: add logger

			view.displayError(errorMessage);
			return false;
		}


		//check whether course code already added
		boolean hasCode = false;
		while (!hasCode){
			for(Course course: courses){
				hasCode = course.hasCode(code);
			}
		}
		//if course already exists, display error
		if (hasCode){
			String errorMessage = "Course with that code already exists";
			//TODO: add logger

			view.displayError(errorMessage);
			return false;
		}

		while(true){
			view.displayInfo("===Add Course - Activities===");
			view.displayInfo("[0] Add Activity");
			view.displayInfo("[-1] Return to manage courses");
			String input = view.getInput("Please choose an option: ");
			try {
				int optionNo = Integer.parseInt(input);
				if (optionNo == 0) {
					//TODO: get ActivityInfo;
				} else if (optionNo == -1){
					break;
				} else{
					view.displayError("Invalid option: " + input);
				}
			} catch (NumberFormatException e) {
				view.displayError("Invalid option: " + input);
			}
		}


		Course newCourse = new Course(code, name, description, requiresComputers,
				COName, COEmail, CSName, CSEmail, reqTutorials, reqLabs);
		courses.add(newCourse);
		return true;
	}

	public boolean checkCourseCode(String courseCode){
		boolean courseCodeIsValid = false;
		//TODO: Piazza what is the course code format?

		return courseCodeIsValid;
	}

	private boolean isAnyNullOrEmpty(Object... objects){
		for (Object obj : objects){
			if(obj == null){
				return true;
			}
			if(obj instanceof String){
				if(((String) obj).trim().isEmpty()){
					return true;
				}
			}
		}
		return false;
	}

	public boolean  addCourseToStudentTimetable(View view,String email,
												String courseCode){
		if(!hasCourse(courseCode)){
			view.displayError("Incorrect course code");
			return false;
		}else{
			for (Course course : courses) {
				String fullActivityDetailsAsString = course.getActivityAsString();
			}


			return true;
		}
	}

	private boolean hasCourse(String courseCode) {
		if (courseCode == null) return false;
		for (Course course : courses) {
			if (course.hasCode(courseCode)) {
				return true;
			}
		}
		return false;
	}
}


