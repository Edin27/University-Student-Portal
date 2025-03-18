package model;

import view.TextUserInterface;
import view.View;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

		List <Activity> activities = new ArrayList<>();
		while(true){
			view.displayInfo("===Add Course - Activities===");
			view.displayInfo("[0] Add Activity");
			view.displayInfo("[-1] Return to manage courses");
			String input = view.getInput("Please choose an option: ");
			try {
				int optionNo = Integer.parseInt(input);
				if (optionNo == 0) {
					//TODO: get ActInfo;
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

	private Activity addActivity(View view){
		String id = view.getInput("Enter the activity id [Lecture: 0; Tutorial: 1; " +
				"Lab: 2]: ");
		String startDate = view.getInput("Enter the start date [yyyy-mm-dd]: ");
		String startTime = view.getInput("Enter the start time [hh:mm]: ");
		String endDate = view.getInput("Enter the end date [yyyy-mm-dd]:");
		String endTime = view.getInput("Enter the end time [hh:mm]: ");
		String location = view.getInput("Enter the activity location: ");
		String day = view.getInput("Enter the activity day [Mon,Tues,Wed,Thurs,Fri]: ");

		Integer idInt = null;
		if(isAnyNullOrEmpty(id, startDate,startTime,endDate,endTime,location,day)){
			return null;
		}
		else{
			try{
				idInt = Integer.parseInt(id);
			}catch(NumberFormatException e){
				view.displayError("Invalid input: " + id);
				return null;
			}
			if (idInt < 0 || idInt > 2){
				view.displayError("Invalid input: " + id);
				return null;
			}
		}

	}

	private LocalDate checkDate(View view, String date){
		LocalDate startEndDate = null;
		try{
			startEndDate = LocalDate.parse(date);
		}catch(DateTimeParseException e1){
			try{
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy");
				startEndDate = LocalDate.parse(date, formatter);
			}catch(DateTimeParseException e2){
				view.displayError("Invalid date: " + date);
			}
		}
		return startEndDate;
	}

	private LocalTime checkTime(View view, String time){
		LocalTime startEndTime = null;
		try{
			startEndTime = LocalTime.parse(time);
		}catch(DateTimeParseException e1){
			try{
				
			}
		}
	}






}
