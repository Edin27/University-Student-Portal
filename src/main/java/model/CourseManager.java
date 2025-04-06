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
	private final Collection<Course> courses = new ArrayList<>();
	private final Collection<Timetable> timetables = new ArrayList<>();
	private final Map<String, Timetable> timetabless = new HashMap<>();

	String fullActivityDetailsAsString;
	int requiredTutorials = 0;
	int requiredLabs = 0;

    public CourseManager(View view) {
        this.view = view;
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
			Log.AddLog(Log.ActionName.ADD_COURSE, String.format("%s, %s, %s, %b, %s, " +
							"%s, %s, %s, %d, %d" , code, name, description,
							requiresComputers, COName, COEmail, CSName, CSEmail,
							reqTutorials, reqLabs), Log.Status.FAILURE);
			view.displayError(errorMessage);
			return false;
		}


		//check whether course code is valid
		if (!checkCourseCode(code)) {
			String errorMessage = "Provided course code is invalid";
			Log.AddLog(Log.ActionName.ADD_COURSE, String.format("%s, %s, %s, %b, %s, " +
							"%s, %s, %s, %d, %d" , code, name, description,
					requiresComputers, COName, COEmail, CSName, CSEmail,
					reqTutorials, reqLabs), Log.Status.FAILURE);
			view.displayError(errorMessage);
			return false;
		}


		//check whether course code already added
		//if course already exists, display error
		if (hasCourse(code)) {
			String errorMessage = "Course with that code already exists";
			Log.AddLog(Log.ActionName.ADD_COURSE, String.format("%s, %s, %s, %b, %s, " +
							"%s, %s, %s, %d, %d" , code, name, description,
					requiresComputers, COName, COEmail, CSName, CSEmail,
					reqTutorials, reqLabs), Log.Status.FAILURE);
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
			view.displayInfo("[-1] Create course and return to manage courses");
			String input = view.getInput("Please choose an option: ");
			try {
				int optionNo = Integer.parseInt(input);
				if (optionNo == 0) {
					Activity activity = newCourse.addActivity(id);
					if (activity != null){
						id+=1;
					}
				} else if (optionNo == -1) {
					if (lectures.isEmpty() && tutorials.isEmpty() && labs.isEmpty()) {
						view.displayWarning("Add at least one activity before exiting!");
					} else {
						break;
					}
				} else {
					view.displayError("Invalid option: " + input);
				}
			} catch (NumberFormatException e) {
				view.displayError("Invalid option: " + input);
			}
		}
		courses.add(newCourse);
		Log.AddLog(Log.ActionName.ADD_COURSE, String.format("%s, %s, %s, %b, %s, %s, " +
					"%s, %s, %d, %d",code, name, description, requiresComputers, COName
					, COEmail, CSName, CSEmail, reqTutorials, reqLabs), Log.Status.SUCCESS);
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
				boolean removedTimeSlot = timetable.removeSlotsForCourse(courseCode);
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
			Log.AddLog(Log.ActionName.REMOVE_COURSE, courseCode, Log.Status.FAILURE);
			view.displayError(errorMessage);
		}
		return emailsToNotifyCourseRemoved;
	}


	public boolean addCourseToStudentTimetable(String email,String courseCode){

		if(!hasCourse(courseCode)){
			String errorMessage = "Incorrect course code provided";
			Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, email + courseCode,
					Log.Status.FAILURE);
			view.displayError(errorMessage);
			return false;
		}
		for (Course course : courses) {
			fullActivityDetailsAsString = course.getActivityAsString();
		}

		Timetable timetable = timetabless.get(email);
		if (timetable == null) {
			timetable = new Timetable(email);
			timetabless.put(email, timetable);
		}

		// 解析活动详情字符串
		String[] activityStrings = fullActivityDetailsAsString.split("Activity\\{");


		for (int i = 1; i < activityStrings.length; i++) {
			String activityContent = activityStrings[i].replace("}", "").trim();
			// 拆分 key=value 项
			String[] parts = activityContent.split(",");
			Map<String, String> values = new HashMap<>();
			for (String part : parts) {
				String[] keyValue = part.trim().split("=");
				if (keyValue.length == 2) {
					values.put(keyValue[0].trim(), keyValue[1].trim());
				}
			}

			int activityId = Integer.parseInt(values.get("id"));
			String day = values.get("day");
			LocalDate startDate = LocalDate.parse(values.get("startDate"));
			LocalDate endDate = LocalDate.parse(values.get("endDate"));
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalTime startTime = LocalTime.parse(values.get("startTime"), timeFormatter);
			LocalTime endTime = LocalTime.parse(values.get("endTime"), timeFormatter);

			// 检查时间冲突
			String[] conflicting = timetable.checkConflicts(startDate, startTime, endDate, endTime);
			boolean unrecordedLecture1 = true;
			boolean unrecordedLecture2 = true;

			if (conflicting != null) {
				for (Course course : courses) {
					unrecordedLecture1 =
							course.isUnrecordedLecture(activityId);
				}
				for (Course course : courses) {
					unrecordedLecture2 =
							course.isUnrecordedLecture(Integer.parseInt(conflicting[1]));
				}
				if (unrecordedLecture1 || unrecordedLecture2) {
					String errorMessage = "You have at least one clash with an " +
							"unrecorded lecture. The course cannot be added to your timetable";
					Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE,email + courseCode,
							Log.Status.FAILURE);
					view.displayError(errorMessage);
					return false;
				} else {
					String warningMessage = "You have at least one clash with another " +
							"activity";
					Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, email + courseCode,
							Log.Status.FAILURE);
					view.displayWarning(warningMessage);
				}
			}

			timetable.addTimeSlot(courseCode, DayOfWeek.valueOf(day.toUpperCase()),
					startDate, startTime, endDate, endTime, activityId);
		}

		boolean requiredTutorial = checkChosenTutorials();

		if(requiredTutorial){
			String warningMessage = "You have to choose "+requiredTutorials+" tutorials" +
					" " + "for this course" ;
			Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, email + courseCode,
					Log.Status.FAILURE);
			view.displayError(warningMessage);
		}

		boolean requiredLab = checkChosenLabs();

		if(requiredLab){
			String warningMessage = "You have to choose "+requiredLabs+" labs for this course " ;
			Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, email + courseCode,
					Log.Status.FAILURE);
			view.displayError(warningMessage);
		}

		String successMessage = "The course was successfully added to your timetable" ;
		Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, email + courseCode,
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


	private boolean checkChosenTutorials(){
		for(Course course:courses){
			requiredTutorials = course.getRequiredTutorials();
		}
		return requiredTutorials > 0;
	}

	private boolean checkChosenLabs(){
		for(Course course:courses){
			requiredLabs = course.getRequiredLabs();
		}
		return requiredLabs > 0;
	}

	public Timetable getTimetableByEmail(String email) {
		return timetabless.get(email);  // 如果没有找到，则返回 null
	}


}








