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
			Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
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

		// Activity detail
		String[] activityStrings = fullActivityDetailsAsString.split("Activity\\{");


		for (int i = 1; i < activityStrings.length; i++) {
			String activityContent = activityStrings[i].replace("}", "").trim();
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

			// Check for time conflicts
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
					String errorMessage = "You have at least one clash win an unrecorded lecture. The course cannot be added to your timetable";
					Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
							Log.Status.FAILURE);
					view.displayError(errorMessage);
					return false;
				} else {
					String warningMessage = "You have at least one clash with another " +
							"activity";
					Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
							Log.Status.FAILURE);
					view.displayWarning(warningMessage);
				}
			}

			String activityType = null;
			for (Course course : courses) {
				for (Activity tutorial : course.getTutorials()) {
					if (tutorial.getId() == activityId) {
						activityType = "Tutorial";
						break;
					}
				}
				if (activityType == null) {
					for (Activity lab : course.getLabs()) {
						if (lab.getId() == activityId) {
							activityType = "Lab";
							break;
						}
					}
				}
				if (activityType == null) {
					for (Activity lecture : course.getLectures()) {
						if (lecture.getId() == activityId) {
							activityType = "Lecture";
							break;
						}
					}
				}
			}

			timetable.addTimeSlot(courseCode, DayOfWeek.valueOf(day.toUpperCase()),
					startDate, startTime, endDate, endTime, activityId, activityType);
		}

		for(Course course : courses) {
			requiredTutorials = course.getRequiredTutorials();
		}

		if (requiredTutorials > 0) {
			String warningMessage = "You have to choose " + requiredTutorials + " tutorials for this course ";
			Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
					Log.Status.FAILURE);
			view.displayWarning(warningMessage);
		}

		for(Course course:courses) {
			requiredLabs = course.getRequiredLabs();
		}

		if (requiredLabs > 0) {
			String warningMessage = "You have to choose " + requiredLabs + " labs for this course ";
			Log.AddLog(Log.ActionName.ADD_COURSE_TO_TIMETABLE, "",
					Log.Status.FAILURE);
			view.displayWarning(warningMessage);
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


	public boolean checkChosenTutorials(String courseCode, Timetable timetable) {
		int count = 0;
		for (Timetable.TimeSlot slot : timetable.getTimeSlots()) {
			if (slot.courseCode.equals(courseCode) &&
					slot.status == Timetable.Status.CHOSEN &&
					"Tutorial".equalsIgnoreCase(slot.activityType)) {
				count++;
			}
		}
		// Get how many Tutorials are needed from the course itself and compare
		return count >= this.requiredTutorials;
	}

	public boolean checkChosenLabs(String courseCode, Timetable timetable) {
		int count = 0;
		for (Timetable.TimeSlot slot : timetable.getTimeSlots()) {
			if (slot.courseCode.equals(courseCode) &&
					slot.status == Timetable.Status.CHOSEN &&
					"Lab".equalsIgnoreCase(slot.activityType)) {
				count++;
			}
		}
		return count >= this.requiredLabs;
	}

	public Timetable getTimetableByEmail(String email) {
		return timetabless.get(email);  // if not find, return null
	}

	public boolean chooseActivityForCourse(String studentEmail, String courseCode, String activityType) {
		if (!hasCourse(courseCode)) {
			view.displayError("Course code " + courseCode + " does not exist.");
			Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.FAILURE);
			return false;
		}

		Timetable timetable = timetabless.get(studentEmail);
		if (timetable == null) {
			view.displayError("No timetable found for student " + studentEmail);
			Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.FAILURE);
			return false;
		}

		if (!timetable.getStudentEmail().equals(studentEmail)) {
			view.displayError("Timetable does not belong to student " + studentEmail);
			Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.FAILURE);
			return false;
		}

		if (!"Tutorial".equalsIgnoreCase(activityType) && !"Lab".equalsIgnoreCase(activityType)) {
			view.displayError("Invalid activity type: " + activityType + ". Use 'Tutorial' or 'Lab'.");
			Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.FAILURE);
			return false;
		}

		Course course = findCourse(courseCode);
		if (course == null) {
			view.displayError("Course " + courseCode + " not found.");
			Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.FAILURE);
			return false;
		}

		// view available time period
		List<Timetable.TimeSlot> availableSlots = displayAvailableTimeSlots(timetable, course, courseCode, activityType);
		if (availableSlots.isEmpty()) {
			Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.FAILURE);
			return false;
		}

		// Get the time period selected by the user
		LocalDate startDate = null;
		LocalTime startTime = null;
		LocalDate endDate = null;
		LocalTime endTime = null;

		view.displayInfo("Please enter the time slot details you want to choose:");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

		try {
			String startDateStr = view.getInput("Start date (yyyy-MM-dd): ");
			startDate = LocalDate.parse(startDateStr, dateFormatter);

			String startTimeStr = view.getInput("Start time (HH:mm): ");
			startTime = LocalTime.parse(startTimeStr, timeFormatter);

			String endDateStr = view.getInput("End date (yyyy-MM-dd): ");
			endDate = LocalDate.parse(endDateStr, dateFormatter);

			String endTimeStr = view.getInput("End time (HH:mm): ");
			endTime = LocalTime.parse(endTimeStr, timeFormatter);
		} catch (DateTimeParseException e) {
			view.displayError("Invalid date or time format. Please use yyyy-MM-dd for dates and HH:mm for times.");
			Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.FAILURE);
			return false;
		}

		// Select time period
		boolean success = timetable.chooseActivityByTime(courseCode, activityType, startDate, startTime, endDate, endTime);
		if (!success) {
			view.displayError("Failed to choose " + activityType + " for course " + courseCode +
					" at specified time. It may already be chosen or not exist.");
			Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.FAILURE);
			return false;
		}

		requiredTutorials = course.getRequiredTutorials();
		requiredLabs = course.getRequiredLabs();

		// 检查是否满足要求
		boolean tutorialsSatisfied = checkChosenTutorials(courseCode, timetable);
		boolean labsSatisfied = checkChosenLabs(courseCode, timetable);

		// Check if the requirements are met
		if ("Tutorial".equalsIgnoreCase(activityType)) {
			if (tutorialsSatisfied) {
				view.displaySuccess("You have successfully chosen all required Tutorials for " + courseCode + "!");
			} else {
				view.displayWarning("You still need to choose " +
						(requiredTutorials - timetable.numChosenTutorials(courseCode)) +
						" more tutorials for " + courseCode);
				Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.FAILURE);
				return false; // back to main
			}
		} else if ("Lab".equalsIgnoreCase(activityType)) {
			if (labsSatisfied) {
				view.displaySuccess("You have successfully chosen all required Labs for " + courseCode + "!");
			} else {
				view.displayWarning("You still need to choose " +
						(requiredLabs - timetable.numChosenLabs(courseCode)) +
						" more labs for " + courseCode);
				Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.FAILURE);
				return false; // back to main
			}
		}

		// If both Tutorial and Lab meet the requirements, display the overall success information
		if (tutorialsSatisfied && labsSatisfied) {
			view.displaySuccess("All required activities for " + courseCode + " have been chosen!");
		}

		Log.AddLog(Log.ActionName.CHOOSE_ACTIVITY, courseCode, Log.Status.SUCCESS);
		return true;
	}

	private List<Timetable.TimeSlot> displayAvailableTimeSlots(
			Timetable timetable,
			Course course,
			String courseCode,
			String activityType
	) {
		view.displayInfo("Available " + activityType + "s for " + courseCode + ":");
		List<Timetable.TimeSlot> availableSlots = new ArrayList<>();
		int index = 1;

		List<Activity> activities = "Tutorial".equalsIgnoreCase(activityType) ? course.getTutorials() : course.getLabs();

		for (Timetable.TimeSlot slot : timetable.getTimeSlots()) {
			if (slot.courseCode.equals(courseCode) &&
					slot.status == Timetable.Status.UNCHOSEN &&
					slot.activityType.equalsIgnoreCase(activityType)) {
				for (Activity activity : activities) {
					if (activity.getId() == slot.activityId &&
							activity.getStartDate().equals(slot.startDate) &&
							activity.getStartTime().equals(slot.startTime) &&
							activity.getEndDate().equals(slot.endDate) &&
							activity.getEndTime().equals(slot.endTime)) {
						String slotInfo = "[" + index + "] " + slot.startDate + " " +
								slot.startTime + " - " + slot.endDate + " " +
								slot.endTime + " (ID: " + slot.activityId + ")";
						view.displayInfo(slotInfo);
						availableSlots.add(slot);
						index++;
						break;
					}
				}
			}
		}

		if (availableSlots.isEmpty()) {
			view.displayError("No available " + activityType.toLowerCase() + "s for " + courseCode);
		}

		return availableSlots;
	}


}








