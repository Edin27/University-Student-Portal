package model;

import external.Log;
import view.View;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class Course {
	protected final View view;
	private String courseCode;
	private String name;
	private String description;
	private boolean requiresComputers;
	private String courseOrganiserName;
	private String couresOrganiserEmail;
	private String courseSecretaryName;
	private String courseSecretaryEmail;
	private int requiredTutorials;
	private int requiredLabs;
	private List<Activity> lectures = new ArrayList<>();
	private List<Activity> tutorials = new ArrayList<>();
	private List<Activity> labs = new ArrayList<>();


	public Course(View view, String code, String name, String description,
				  boolean requiresComputers, String COName, String COEmail,
				  String CSName, String CSEmail, int reqTutorials, int reqLabs,
				  List<Activity> lectures, List<Activity> tutorials, List<Activity> labs){
		this.view = view;
		this.courseCode = code;
		this.name = name;
		this.description = description;
		this.requiresComputers = requiresComputers;
		this.courseOrganiserName = COName;
		this.couresOrganiserEmail = COEmail;
		this.courseSecretaryName = CSName;
		this.courseSecretaryEmail = CSEmail;
		this.requiredTutorials = reqTutorials;
		this.requiredLabs = reqLabs;
		this.lectures = lectures;
		this.tutorials = tutorials;
		this.labs = labs;
	}


	public boolean hasCode(String code){
		if (code.equals(courseCode)){
			return true;
		}
		else{
			return false;
		}
	}

	public String getActivityAsString() {
		StringBuilder sb = new StringBuilder();
		for (Activity lecture : lectures) {
			sb.append(lecture.toString()).append("\n");
		}
		for (Activity tutorial : tutorials) {
			sb.append(tutorial.toString()).append("\n");
		}
		for (Activity lab : labs) {
			sb.append(lab.toString()).append("\n");
		}
		String ActivityDetailAsString = sb.toString();
		return ActivityDetailAsString;
	}

	public List<Activity> getLectures() {
		return lectures;
	}

	public List<Activity> getTutorials() {
		return tutorials;
	}

	public List<Activity> getLabs() {
		return labs;
	}

	public String getCourseOrganiserName() { return courseOrganiserName; }

	public String getCourseOrganiserEmail() {
		return couresOrganiserEmail;
	}

	public String getCourseSecretaryName() { return courseSecretaryName; }

	public String getCourseSecretaryEmail() {
		return courseSecretaryEmail;
	}

	public String getDescription() { return description; }

	public boolean getRequiresComputers() { return requiresComputers; }

	public int getRequiredTutorials() { return requiredTutorials; }

	public int getRequiredLabs() { return requiredLabs; }

	public String getName() {
		return name;
	}

	public String getCourseCode() {
		return courseCode;
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

	public Activity addActivity(int id) {
		String activityType = view.getInput("Enter the activity type [Lecture: 0; " +
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
			Log.AddLog(Log.ActionName.CHOOSE_TUTORIAL_OR_lAB,
					activityType+" "+startDate+" "+startTime+" "+endDate+" "+endTime+" "+location+" "+day, Log.Status.FAILURE);
			return null;
		} else {
			actType = checkActType(activityType);
			if(isAnyNullOrEmpty(actType)){
				return null;
			}

			sDate = checkDate(startDate);
			sTime = checkTime(startTime);
			eDate = checkDate(endDate);
			eTime = checkTime(endTime);
			if(isAnyNullOrEmpty(sDate, sTime, eDate, eTime)){
				return null;
			}

			frequencyDay = checkDay(day);
			if(isAnyNullOrEmpty(frequencyDay)){
				return null;
			}
		}
		Activity activity = null;
		if(actType == 0){
			activity = addLecture(id, sDate, sTime, eDate, eTime, location,
					frequencyDay);

		}else if(actType == 1 || actType == 2){
			activity = addTutorialLab(actType, id, sDate, sTime, eDate, eTime, location,
					frequencyDay);

		}
		return activity;
	}

	private Integer checkActType(String activityType){
		Integer actType = null;
		String errorMessage = "Activity type provided is invalid";
		try {
			actType = Integer.parseInt(activityType);
			if (actType < 0 || actType > 2) {
				view.displayError(errorMessage);
				Log.AddLog(Log.ActionName.CHOOSE_TUTORIAL_OR_lAB, activityType, Log.Status.FAILURE);
			}
		} catch (NumberFormatException e) {
			view.displayError(errorMessage);
			Log.AddLog(Log.ActionName.CHOOSE_TUTORIAL_OR_lAB, activityType, Log.Status.FAILURE);
		}

		return actType;
	}


	private LocalDate checkDate(String date) {
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
				Log.AddLog(Log.ActionName.CHOOSE_TUTORIAL_OR_lAB, date, Log.Status.FAILURE);
			}
		}
		return startEndDate;
	}

	private LocalTime checkTime(String time) {
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
				Log.AddLog(Log.ActionName.CHOOSE_TUTORIAL_OR_lAB, time, Log.Status.FAILURE);
			}
		}
		return startEndTime;
	}

	private DayOfWeek checkDay(String day){
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
				Log.AddLog(Log.ActionName.CHOOSE_TUTORIAL_OR_lAB, day, Log.Status.FAILURE);
				return null;
		}

	}

	private Activity addLecture(int id, LocalDate startDate, LocalTime startTime,
								LocalDate endDate, LocalTime endTime, String location,
								DayOfWeek day){
		Boolean recorded = view.getYesNoInput("Is this lecture recorded?");
		if(isAnyNullOrEmpty(recorded)){
			String errorMessage = "Lecture info required not provided";
			view.displayError(errorMessage);
			Log.AddLog(Log.ActionName.CHOOSE_TUTORIAL_OR_lAB, recorded.toString(), Log.Status.FAILURE);
			return null;
		}else{
			Lecture newLecture = new Lecture(id, startDate, startTime, endDate,endTime, location, day, recorded);
			lectures.add(newLecture);
			return newLecture;
		}
	}

	private Activity addTutorialLab(int actType, int id, LocalDate startDate,
									LocalTime startTime,
									LocalDate endDate, LocalTime endTime, String location,
									DayOfWeek day){
		String capacity = view.getInput("Enter the Tutorial or Lab capacity: ");
		String errorMessage = "Tutorial or Lab info required not provided";
		Activity tutorialLab = null;
		if(isAnyNullOrEmpty(capacity)){
			view.displayError(errorMessage);
			Log.AddLog(Log.ActionName.CHOOSE_TUTORIAL_OR_lAB, capacity, Log.Status.FAILURE);
			return null;
		}else{
			Integer capacityInt = null;
			try {
				capacityInt = Integer.parseInt(capacity);
			} catch (NumberFormatException e) {
				view.displayError(errorMessage);
				Log.AddLog(Log.ActionName.CHOOSE_TUTORIAL_OR_lAB, capacity, Log.Status.FAILURE);
				return null;
			}
			if (capacityInt < 0 ) {
				view.displayError(errorMessage);
				Log.AddLog(Log.ActionName.CHOOSE_TUTORIAL_OR_lAB, capacity, Log.Status.FAILURE);
				return null;
			}else{
				if(actType == 1){
					Tutorial tutorial = new Tutorial(id, startDate, startTime, endDate, endTime, location, day, capacityInt);
					tutorials.add(tutorial);
					return tutorial;
				}else if(actType == 2){
					Lab lab = new Lab(id, startDate, startTime, endDate, endTime, location, day, capacityInt);
					labs.add(lab);
					return lab;
				}
			}
		}
		return null;
	}

	public boolean isUnrecordedLecture(int activityId) {
		for (Activity activity : lectures) {
			if (activity instanceof Lecture) {
				Lecture lecture = (Lecture) activity;
				if (lecture.getId() == activityId && !lecture.getRecorded()) {
					return true;
				}
			}
		}
		return false;
	}

}
