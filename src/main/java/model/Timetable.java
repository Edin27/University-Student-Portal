package model;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Timetable {


	public enum Status {
		CHOSEN,
		UNCHOSEN
	}
	private String studentEmail;

	private List<TimeSlot> timeSlots;

	private Map<String, Integer> courseActivityCount;


	public Timetable(String studentEmail) {
		this.studentEmail = studentEmail;
		this.timeSlots = new ArrayList<>();
		this.courseActivityCount = new HashMap<>();
	}

	public boolean isChosen(String courseCode, int activityId) {
		for (TimeSlot slot : timeSlots) {
			if (slot.getCourseCode().equals(courseCode) &&
					slot.getActivityId() == activityId) {
				return slot.getStatus() == Status.CHOSEN;
			}
		}
		return false; // If no corresponding time period is found, false is returned by default
	}

	public void addTimeSlot(
			String courseCode,
			DayOfWeek day,
			LocalDate startDate,
			LocalTime startTime,
			LocalDate endDate,
			LocalTime endTime,
			int activityId,
			String activityType
	) {
		timeSlots.add(new TimeSlot(courseCode, day, startDate, startTime,
				endDate, endTime, activityId, activityType));

		if ("Lecture".equalsIgnoreCase(activityType)) {
			courseActivityCount.put(courseCode,
					courseActivityCount.getOrDefault(courseCode, 0) + 1);
		}
	}


	public int numChosenTutorials(String courseCode) {
		int count = 0;
		for (TimeSlot slot : timeSlots) {
			if (slot.courseCode.equals(courseCode) &&
					slot.status == Status.CHOSEN &&
					"Tutorial".equalsIgnoreCase(slot.activityType)) {
				count++;
			}
		}
		return count;
	}

	public int numChosenLabs(String courseCode) {
		int count = 0;
		for (TimeSlot slot : timeSlots) {
			if (slot.courseCode.equals(courseCode) &&
					slot.status == Status.CHOSEN &&
					"Lab".equalsIgnoreCase(slot.activityType)) {
				count++;
			}
		}
		return count;
	}


	public String[] checkPossibleConflicts(
			LocalDate newStartDate,
			LocalTime newStartTime,
			LocalDate newEndDate,
			LocalTime newEndTime,
			DayOfWeek newDay
	) {

		for (TimeSlot slot : timeSlots) {
			if (slot.possibleOverlaps(newStartDate, newStartTime, newEndDate, newEndTime,
					newDay)) {
				return new String[]{slot.getCourseCode(), String.valueOf(slot.activityId)};
			}
		}
		return null;
	}

	public String[] checkConflicts(LocalDate newStartDate,
								   LocalTime newStartTime,
								   LocalDate newEndDate,
								   LocalTime newEndTime,
								   DayOfWeek newDay,
								   Timetable.Status newStatus) {
		for (TimeSlot slot : timeSlots) {
			if (slot.overlaps(newStartDate, newStartTime, newEndDate, newEndTime,
					newDay, newStatus)) {
				return new String[]{slot.getCourseCode(), String.valueOf(slot.activityId)};
			}
		}
		return null;
	}

	public boolean hasStudentEmail(String email) {
		return this.studentEmail.equals(email);
	}

	public boolean chooseActivityByTime(
			String courseCode,
			String activityType,
			LocalDate startDate,
			LocalTime startTime,
			LocalDate endDate,
			LocalTime endTime
	) {
		for (TimeSlot slot : timeSlots) {
			if (slot.courseCode.equals(courseCode) &&
					slot.activityType.equalsIgnoreCase(activityType) &&
					slot.startDate.equals(startDate) &&
					slot.startTime.equals(startTime) &&
					slot.endDate.equals(endDate) &&
					slot.endTime.equals(endTime)) {
				if (slot.status == Status.UNCHOSEN) {
					slot.status = Status.CHOSEN;
					courseActivityCount.put(courseCode,
							courseActivityCount.getOrDefault(courseCode, 0) + 1);
					return true;
				}
				return false; // already CHOSEN
			}
		}
		return false; // No matching time period found
	}

	public boolean removeSlotsForCourse(String courseCode) {
		boolean removedSlot =
				timeSlots.removeIf(slot -> slot.getCourseCode().equals(courseCode));
		courseActivityCount.remove(courseCode);
		return removedSlot;
	}

	public List<TimeSlot> getTimeSlots() { return timeSlots; }

	public String getStudentEmail() {return studentEmail;}

	@Override
	public String toString() {
		return "Timetable for " + studentEmail + ":\n" + timeSlots.toString();
	}

	public static class TimeSlot {
		public String activityType;
		String courseCode;
		DayOfWeek day;
		LocalDate startDate;
		LocalTime startTime;
		LocalDate endDate;
		LocalTime endTime;
		int activityId;
		Status status;

		public TimeSlot(
				String courseCode,
				DayOfWeek day,
				LocalDate startDate,
				LocalTime startTime,
				LocalDate endDate,
				LocalTime endTime,
				int activityId,
				String activityType
		) {
			this.courseCode = courseCode;
			this.day = day;
			this.startDate = startDate;
			this.startTime = startTime;
			this.endDate = endDate;
			this.endTime = endTime;
			this.activityId = activityId;
			this.activityType = activityType;
			this.status = "Lecture".equalsIgnoreCase(activityType) ? Status.CHOSEN : Status.UNCHOSEN;
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public int getActivityId() {
			return activityId;
		}

		public boolean possibleOverlaps(
				LocalDate otherStartDate,
				LocalTime otherStartTime,
				LocalDate otherEndDate,
				LocalTime otherEndTime,
				DayOfWeek otherDay
		) {
			return day.equals(otherDay) &&
					!(this.endDate.isBefore(otherStartDate) || this.startDate.isAfter(otherEndDate)) &&
					!(this.endTime.isBefore(otherStartTime) || this.startTime.isAfter(otherEndTime));

		}

		public boolean overlaps(
				LocalDate otherStartDate,
				LocalTime otherStartTime,
				LocalDate otherEndDate,
				LocalTime otherEndTime,
				DayOfWeek otherDay,
				Status chosenOrNot
		) {
			return day.equals(otherDay) &&
					status == Status.CHOSEN && chosenOrNot == Status.CHOSEN &&
					!(this.endDate.isBefore(otherStartDate) || this.startDate.isAfter(otherEndDate)) &&
					!(this.endTime.isBefore(otherStartTime) || this.startTime.isAfter(otherEndTime));

		}

		public String getCourseCode() {
			return courseCode;
		}

		public LocalDate getStartDate() {
			return startDate;
		}

		public LocalDate getEndDate() {
			return endDate;
		}

		public DayOfWeek getDay() {
			return day;
		}

		public LocalTime getStartTime() {
			return startTime;
		}

		public LocalTime getEndTime() {
			return endTime;
		}

		public String getActivityType() {
			return activityType;
		}



		@Override
		public String toString() {
			return String.format("Time: %s -> %s\n   Course code: %s\n   Activity: %s\n" +
							"   Status: %s\n",
					startTime, endTime, courseCode, activityType, status);
		}
	}
}