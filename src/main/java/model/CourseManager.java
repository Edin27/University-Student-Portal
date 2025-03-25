package model;

import view.TextUserInterface;
import view.View;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.*;

public class CourseManager {
	protected final View view;
	private static volatile CourseManager courseManagerInstance;
	private final Collection<Course> courses = new ArrayList<>();



	private CourseManager(View view) {
		this.view = view;
	}

	public static CourseManager getCourseManager(View view) {
		CourseManager result = courseManagerInstance;
		if (courseManagerInstance == null) {
			synchronized (CourseManager.class) {
				result = courseManagerInstance;
				if (result == null) {
					courseManagerInstance = result = new CourseManager(view);
				}
			}
		}
		return result;
	}

	public Collection<Course> getCourses() {
		return courses;
	}

	public boolean addCourse(String code,
							 String name, String description, Boolean requiresComputers,
							 String COName, String COEmail, String CSName, String CSEmail,
							 Integer reqTutorials, Integer reqLabs) {


		//check whether required elements all provided
		if (isAnyNullOrEmpty(code, name, description, requiresComputers, COName,
				COEmail, CSName, CSEmail, reqTutorials, reqLabs)) {
			String errorMessage = "Required course info not provided";
			//TODO: add logger


			view.displayError(errorMessage);
			return false;
		}


		//check whether course code is valid
		if (!checkCourseCode(code)) {
			String errorMessage = "Provided course code is invalid";
			//TODO: add logger

			view.displayError(errorMessage);
			return false;
		}


		//check whether course code already added
		boolean hasCode = false;
		while (!hasCode) {
			for (Course course : courses) {
				hasCode = course.hasCode(code);
			}
		}
		//if course already exists, display error
		if (hasCode) {
			String errorMessage = "Course with that code already exists";
			//TODO: add logger

			view.displayError(errorMessage);
			return false;
		}

		List<Activity> lectures = new ArrayList<>();
		List<Activity> tutorials = new ArrayList<>();
		List<Activity> labs = new ArrayList<>();
		Course newCourse = new Course(view, code, name, description, requiresComputers,
				COName, COEmail, CSName, CSEmail, reqTutorials, reqLabs, lectures,
				tutorials, labs);
		int id = 0;

		while (true) {
			view.displayInfo("===Add Course - Activities===");
			view.displayInfo("[0] Add Activity");
			view.displayInfo("[-1] Return to manage courses");
			String input = view.getInput("Please choose an option: ");
			try {
				int optionNo = Integer.parseInt(input);
				if (optionNo == 0) {
					//TODO:check this part and think of how to generate id for activities
					Activity activity = newCourse.addActivity(id);
					if (activity != null){
						if (activity instanceof Lecture){
							newCourse.getLectures().add(activity);
						}else if (activity instanceof Tutorial){
							newCourse.getTutorials().add(activity);
						}else if(activity instanceof Lab){
							newCourse.getLabs().add(activity);
						}
						id+=1;
					}
				} else if (optionNo == -1) {
					break;
				} else {
					view.displayError("Invalid option: " + input);
				}
			} catch (NumberFormatException e) {
				view.displayError("Invalid option: " + input);
			}
		}
		courses.add(newCourse);
		//TODO: add successful logger
		view.displaySuccess("Course has been successfully created");
		return true;
	}

	public boolean checkCourseCode(String courseCode) {
		boolean courseCodeIsValid = false;
		//TODO: Piazza what is the course code format?

		return courseCodeIsValid;
	}

	private boolean isAnyNullOrEmpty(Object... objects) {
		for (Object obj : objects) {
			if (obj == null) {
				return true;
			}
			if (obj instanceof String) {
				if (((String) obj).trim().isEmpty()) {
					return true;
				}

			}
		}
		return false;
	}

}








