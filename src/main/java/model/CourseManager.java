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

	private static volatile CourseManager courseManagerInstance;
	private final Collection<Course> courses = new ArrayList<>();


	private CourseManager() {
	}

	public static CourseManager getCourseManager() {
		CourseManager result = courseManagerInstance;
		if (courseManagerInstance == null) {
			synchronized (CourseManager.class) {
				result = courseManagerInstance;
				if (result == null) {
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
		List<Tutorial> tutorials = new ArrayList<>();
		List<Lab> labs = new ArrayList<>();

		while (true) {
			view.displayInfo("===Add Course - Activities===");
			view.displayInfo("[0] Add Activity");
			view.displayInfo("[-1] Return to manage courses");
			String input = view.getInput("Please choose an option: ");
			try {
				int optionNo = Integer.parseInt(input);
				if (optionNo == 0) {
					int id = 1;
					//TODO:check this part and think of how to generate id for activities
//					Activity activity = addActivity(view);
//					if (activity != null){
//						if(activity instanceof Lecture){lectures.add(activity);}
//					}
				} else if (optionNo == -1) {
					break;
				} else {
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


	private Activity addActivity(View view) {
		String activityType = view.getInput("Enter the activity id [Lecture: 0; " +
				"Tutorial: 1; Lab: 2]: ");
		String startDate = view.getInput("Enter the start date [yyyy-mm-dd]: ");
		String startTime = view.getInput("Enter the start time [hh:mm]: ");
		String endDate = view.getInput("Enter the end date [yyyy-mm-dd]:");
		String endTime = view.getInput("Enter the end time [hh:mm]: ");
		String location = view.getInput("Enter the activity location: ");
		String day = view.getInput("Enter the activity day [mon,tue,wed,thu,fri]: ");

		Integer actType = null;
		LocalDate sDate = null;
		LocalTime sTime = null;
		LocalDate eDate = null;
		LocalTime eTime = null;
		DayOfWeek frequencyDay = null;

		if (isAnyNullOrEmpty(activityType, startDate, startTime, endDate, endTime,
				location, day)) {
			String errorMessage = "Required activity info not provided";
			view.displayError(errorMessage);
			//TODO: add logger
			return null;
		} else {
			actType = checkActType(view, activityType);
			if(isAnyNullOrEmpty(actType)){
				return null;
			}

			sDate = checkDate(view, startDate);
			sTime = checkTime(view, startTime);
			eDate = checkDate(view, endDate);
			eTime = checkTime(view,endTime);
			if(isAnyNullOrEmpty(sDate, sTime, eDate, eTime)){
				return null;
			}

			//TODO: check location and whether it has computers for requiresComputers
			// courses?

			frequencyDay = checkDay(view, day);
			if(isAnyNullOrEmpty(frequencyDay)){
				return null;
			}
		}if(actType == 0){

		}else if(actType == 1){

		}else if(actType==2){

		}

	}

	private Integer checkActType(View view, String activityType){
		Integer actType = null;
		String errorMessage = "Activity type provided is invalid";
		try {
			actType = Integer.parseInt(activityType);
		} catch (NumberFormatException e) {
			view.displayError(errorMessage);
			//TODO: add logger
		}
		if (actType < 0 || actType > 2) {
			view.displayError(errorMessage);
			//TODO: add logger
		}
		return actType;
	}


	private LocalDate checkDate(View view, String date) {
		LocalDate startEndDate = null;
		try {
			startEndDate = LocalDate.parse(date);
		} catch (DateTimeParseException e1) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				startEndDate = LocalDate.parse(date, formatter);
			} catch (DateTimeParseException e2) {
				String errorMessage = "Start date or end date provided is invalid";
				view.displayError(errorMessage);
				//TODO: add logger
			}
		}
		return startEndDate;
	}

	private LocalTime checkTime(View view, String time) {
		LocalTime startEndTime = null;
		try {
			startEndTime = LocalTime.parse(time);
		} catch (DateTimeParseException e1) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm");
				startEndTime = LocalTime.parse(time, formatter);
			} catch (DateTimeParseException e2) {
				String errorMessage = "Start time or end time provided is invalid";
				view.displayError(errorMessage);
				//TODO: add logger
			}
		}
		return startEndTime;
	}

	private DayOfWeek checkDay(View view, String day){
		String toLower = day.toLowerCase(Locale.ENGLISH);

		switch (toLower) {
			case "monday":
			case "mon":
				return DayOfWeek.MONDAY;
			case "tuesday":
			case "tue":
				return DayOfWeek.TUESDAY;
			case "wednesday":
			case "wed":
				return DayOfWeek.WEDNESDAY;
			case "thursday":
			case "thu":
				return DayOfWeek.THURSDAY;
			case "friday":
			case "fri":
				return DayOfWeek.FRIDAY;
			default:
				String errorMessage = "Day provided is invalid";
				view.displayError(errorMessage);
				//TODO: add logger
				return null;
		}

	}

	private Lecture addLecture(View view, LocalDate startDate, LocalTime startTime,
							   LocalDate endDate, LocalTime endTime, String location,
							   DayOfWeek day){
		Boolean recordedLec = view.getYesNoInput("Is this lecture recorded?");
		if(isAnyNullOrEmpty(recordedLec)){
			String errorMessage = "Lecture info required not provided";
			view.displayError(errorMessage);
			//TODO: add logger
			return null;
		}else{
			Lecture newLecture = new Lecture();//TODO: in addCourse, give an assumptionof what id is.
		}

	}


}








