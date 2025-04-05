package model;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Timetable {
	private String studentEmail;

	private List<TimeSlot> timeSlots;

	private Map<String, Integer> courseActivityCount;


	public Timetable(String studentEmail) {
		this.studentEmail = studentEmail;
		this.timeSlots = new ArrayList<>();
		this.courseActivityCount = new HashMap<>();
	}

	public void addTimeSlot(
			String courseCode,
			DayOfWeek day,
			LocalDate startDate,
			LocalTime startTime,
			LocalDate endDate,
			LocalTime endTime,
			int activityId
	) {
		timeSlots.add(new TimeSlot(courseCode, day, startDate, startTime, endDate, endTime, activityId));
	}


	public int numChosenActivities(String courseCode) {
		return courseActivityCount.getOrDefault(courseCode, 0);
	}


	public String[] checkConflicts(
			LocalDate newStartDate,
			LocalTime newStartTime,
			LocalDate newEndDate,
			LocalTime newEndTime
	) {
		for (TimeSlot slot : timeSlots) {
			if (slot.overlaps(newStartDate, newStartTime, newEndDate, newEndTime)) {
				return new String[]{slot.getCourseCode(), String.valueOf(slot.activityId)};
			}
		}
		return null;
	}

	public boolean hasStudentEmail(String email) {
		return this.studentEmail.equals(email);
	}

	public boolean chooseActivity(String courseCode, int activityId) {
		if (hasSlotsForCourse(courseCode)) {
			courseActivityCount.put(courseCode, courseActivityCount.get(courseCode) + 1);
			return true;
		}
		return false;
	}

	public boolean hasSlotsForCourse(String courseCode) {
		return timeSlots.stream().anyMatch(slot -> slot.getCourseCode().equals(courseCode));
	}

	public void removeSlotsForCourse(String courseCode) {
		timeSlots.removeIf(slot -> slot.getCourseCode().equals(courseCode));
		courseActivityCount.remove(courseCode);
	}

	public List<TimeSlot> getTimeSlots() { return timeSlots; }

	public String getStudentEmail() {return studentEmail;}

	@Override
	public String toString() {
		return "Timetable for " + studentEmail + ":\n" + timeSlots.toString();
	}

	public static class TimeSlot {
		private String courseCode;
		private DayOfWeek day;
		private LocalDate startDate;
		private LocalTime startTime;
		private LocalDate endDate;
		private LocalTime endTime;
		private int activityId;

		public TimeSlot(
				String courseCode,
				DayOfWeek day,
				LocalDate startDate,
				LocalTime startTime,
				LocalDate endDate,
				LocalTime endTime,
				int activityId
		) {
			this.courseCode = courseCode;
			this.day = day;
			this.startDate = startDate;
			this.startTime = startTime;
			this.endDate = endDate;
			this.endTime = endTime;
			this.activityId = activityId;
		}

		public boolean overlaps(
				LocalDate otherStartDate,
				LocalTime otherStartTime,
				LocalDate otherEndDate,
				LocalTime otherEndTime
		) {
			return !(this.endDate.isBefore(otherStartDate) ||
					this.startDate.isAfter(otherEndDate)) &&
					!(this.endTime.isBefore(otherStartTime) ||
							this.startTime.isAfter(otherEndTime));
		}

		public String getCourseCode() {
			return courseCode;
		}

		@Override
		public String toString() {
			return courseCode + " (Activity " + activityId + ") on " + day +
					" from " + startTime + " to " + endTime;
		}
	}
}