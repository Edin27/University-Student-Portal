package model;

import external.Log;
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
	private final Collection<Timetable> timetables = new ArrayList<>();
	private final Map<String, Timetable> timetabless = new HashMap<>();
	String fullActivityDetailsAsString;

    public CourseManager(View view) {
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
			Log.AddLog(Log.ActionName.ADD_COURSE, "", Log.Status.FAILURE);
			view.displayError(errorMessage);
			return false;
		}


		//check whether course code is valid
		if (!checkCourseCode(code)) {
			String errorMessage = "Provided course code is invalid";
			Log.AddLog(Log.ActionName.ADD_COURSE, "", Log.Status.FAILURE);
			view.displayError(errorMessage);
			return false;
		}


		//check whether course code already added
		//if course already exists, display error
		if (hasCourse(code)) {
			String errorMessage = "Course with that code already exists";
			Log.AddLog(Log.ActionName.ADD_COURSE, "", Log.Status.FAILURE);
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
					if (activity != null){id+=1;}
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
		Log.AddLog(Log.ActionName.ADD_COURSE, "", Log.Status.SUCCESS);
		view.displaySuccess("Course has been successfully created");
		return true;
	}

	public boolean checkCourseCode(String courseCode) {
		boolean courseCodeIsValid = false;
		if(isAlphanumeric(courseCode)){
			courseCodeIsValid = true;
		}
		return courseCodeIsValid;
	}

	public static boolean isAlphanumeric(String courseCode) {
		return courseCode != null && courseCode.matches("^[a-zA-Z0-9]+$");
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


	public List<String> removeCourse(String courseCode) {
		List<String> emailsToNotifyCourseRemoved = new ArrayList<>();
		//check whether course code exists
		if (hasCourse(courseCode)){
			String organiserEmail = courses.stream()
					.filter(course -> course.getCourseCode().equals(courseCode))
					.map(Course::getCourseOrganiserEmail)
					.findFirst()
					.orElse(null);

			boolean removedCourse =
					courses.removeIf(course -> course.getCourseCode().equals(courseCode));

			for(Timetable timetable: timetables){
				boolean removedTimeSlot =
						timetable.getTimeSlots().removeIf(timeslot -> timeslot.getCourseCode().equals(courseCode));
				if(removedTimeSlot){
					emailsToNotifyCourseRemoved.add(timetable.getStudentEmail());
				}
			}
			if(removedCourse) {
				emailsToNotifyCourseRemoved.add(organiserEmail);
				Log.AddLog(Log.ActionName.REMOVE_COURSE, courseCode, Log.Status.SUCCESS);
				view.displaySuccess("Course has been successfully removed");
			}
		}
		else{
			String errorMessage = "Course code provided does not exist in the system";
			Log.AddLog(Log.ActionName.REMOVE_COURSE, courseCode,
					Log.Status.FAILURE);
			view.displayError(errorMessage);
		}
		return emailsToNotifyCourseRemoved;
	}


	public boolean addCourseToStudentTimetable(String email,String courseCode){

		if(!hasCourse(courseCode)){
			String errorMessage = "Incorrect course code provided";
			Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
					Log.Status.FAILURE);
			view.displayError(errorMessage);
			return false;
		}
		for (Course course : courses) {
			fullActivityDetailsAsString = course.getActivityAsString();
		}

		Timetable newtimetable = null;
		boolean hasEmail = false;
		for (Timetable timetable : timetables){
			hasEmail = timetable.hasStudentEmail(email);
		}

		if(!hasEmail){
			newtimetable = new Timetable(email);
		}
		String[] details = fullActivityDetailsAsString.split(" ");

		String activityId = details[0];
		String day = details[1];
		String startDateStr = details[2];
		String startTimeStr = details[3];
		String endDateStr = details[4];
		String endTimeStr = details[5];

		String StartDateStr =
				startDateStr.replace("startDate=", "").replace(",", "").trim();
		LocalDate startDate = LocalDate.parse(StartDateStr);
		String EndDateStr =
				endDateStr.replace("endDate=", "").replace(",", "").trim();
		LocalDate endDate = LocalDate.parse(EndDateStr);


		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		String startTimeStrCleaned = startTimeStr.replace("startTime=", "").replace(",", "").trim();
		LocalTime startTime = LocalTime.parse(startTimeStrCleaned, timeFormatter);
		String endTimeStrCleaned = endTimeStr.replace("endTime=", "").replace(",", "").trim();
		LocalTime endTime = LocalTime.parse(endTimeStrCleaned, timeFormatter);

		String[] conflictingCourTsexCtodeAndActivityId = null;
		for(Timetable timetable: timetables){
			conflictingCourTsexCtodeAndActivityId = timetable.checkConflicts(startDate,
					startTime,endDate,endTime);
		}

		boolean unrecordedLecture1 = true;
		boolean unrecordedLecture2 = true;

		if(conflictingCourTsexCtodeAndActivityId != null){
			for (Course course: courses){
				unrecordedLecture1 =
						course.isUnrecordedLecture(Integer.parseInt(activityId));
			}
			for (Course course: courses){
				unrecordedLecture2 =
						course.isUnrecordedLecture(Integer.parseInt(conflictingCourTsexCtodeAndActivityId[1]));
			}
			if(unrecordedLecture1 || unrecordedLecture2 ){
				String errorMessage = "You have at least one clash win an unrecorded lecture. The course cannot be added to your timetable";
				Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
						Log.Status.FAILURE);
				view.displayError(errorMessage);
				return false;
			}else{
				String warningMessage = "You have at least one clash with another " +
						"activity";
				Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
						Log.Status.FAILURE);
				view.displayWarning(warningMessage);
			}
		}

		for(Timetable timetable:timetables){
			timetable.addTimeSlot(courseCode,DayOfWeek.valueOf(day.toUpperCase()), startDate, startTime, endDate,
					endTime, Integer.parseInt(activityId));
		}

		boolean requiredTutorials = checkChosenTutorials(courseCode,newtimetable);

		if(requiredTutorials){
			String warningMessage = "You have to choose "+requiredTutorials+" tutorials" +
					" " + "for this course" ;
			Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
					Log.Status.FAILURE);
			view.displayError(warningMessage);
		}

		boolean requiredLabs = checkChosenLabs(courseCode,newtimetable);

		if(requiredLabs){
			String warningMessage = "You have to choose "+requiredLabs+" labs for this course " ;
			Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
					Log.Status.FAILURE);
			view.displayError(warningMessage);
		}

		String successMessage = "The course was successfully added to your timetable" ;
		Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
				Log.Status.SUCCESS);
		view.displaySuccess(successMessage);
		return true;
	}

	public Course findCourse(String code) {
		for (Course course : courses) {
			if (course.hasCode(code)) {
				return course;
			}
		}
		return null;
	}

	private boolean hasCourse(String courseCode) {
		for (Course course : courses) {
			if (course.hasCode(courseCode)) {
				return true;
			}
		}
		return false;
	}


	private boolean checkChosenTutorials(String courseCode, Timetable timetable){
		int requiredTutorials = 0;
		for(Course course:courses){
			requiredTutorials = course.getRequiredTutorials();
		}
		return requiredTutorials > 0;
	}

	private boolean checkChosenLabs(String courseCode, Timetable timetable){
		int requiredLabs = 0;
		for(Course course:courses){
			requiredLabs = course.getRequiredLabs();
		}
		return requiredLabs > 0;
	}
/*
	private Timetable getTimetable(String studentEmail){
		for (Timetable timetable : timetabless.values()){
			if(timetable.hasStudentEmail(studentEmail)){
				return timetable;
			}
		}

		Timetable newtimetable = new Timetable(studentEmail);
		timetabless.put(studentEmail, newtimetable);
		return newtimetable;
	}

	public void viewTimetable (String studentEmail){
		Timetable timetable = getTimetable(studentEmail);
		view.displayTimetable(timetable);
	}
 */

}








