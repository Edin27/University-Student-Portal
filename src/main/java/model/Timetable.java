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
		return false; // 如果没有找到对应的时间段，默认返回false
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

		// 如果是 Lecture（CHOSEN），更新计数
		if ("Lecture".equalsIgnoreCase(activityType)) {
			courseActivityCount.put(courseCode,
					courseActivityCount.getOrDefault(courseCode, 0) + 1);
		}
	}


	public int numChosenTutorials(String courseCode) {
		int count = 0;
		for (TimeSlot slot : timeSlots) {
			if (slot.getCourseCode().equals(courseCode) &&
					slot.getStatus() == Status.CHOSEN &&
					slot.getActivityId() == 1) { // 假设 0 表示 Tutorial
				count++;
			}
		}
		return count;
	}

	public int numChosenLabs(String courseCode) {
		int count = 0;
		for (TimeSlot slot : timeSlots) {
			if (slot.getCourseCode().equals(courseCode) &&
					slot.getStatus() == Status.CHOSEN &&
					slot.getActivityId() == 2) { // 假设 1 表示 Lab
				count++;
			}
		}
		return count;
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

	public boolean chooseActivityByTime(
			String courseCode,
			String activityType, // 替换 activityId 为 activityType
			LocalDate startDate,
			LocalTime startTime,
			LocalDate endDate,
			LocalTime endTime
	) {
		for (TimeSlot slot : timeSlots) {
			if (slot.courseCode.equals(courseCode) &&
					slot.activityType.equalsIgnoreCase(activityType) && // 使用 activityType 匹配
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
				return false; // 已经是 CHOSEN 状态
			}
		}
		return false; // 没有找到匹配的时间段
	}

	public boolean chooseActivity(String courseCode, int activityId) {
		for (TimeSlot slot : timeSlots) {
			if (slot.getCourseCode().equals(courseCode) &&
					slot.getActivityId() == activityId) {
				if (slot.getStatus() == Status.UNCHOSEN) {
					slot.setStatus(Status.CHOSEN);
					courseActivityCount.put(courseCode,
							courseActivityCount.getOrDefault(courseCode, 0) + 1);
					return true;
				}
				return false; // 已经是CHOSEN状态
			}
		}
		return false; // 没有找到对应的时间段
	}

	public boolean hasSlotsForCourse(String courseCode) {
		return timeSlots.stream().anyMatch(slot -> slot.getCourseCode().equals(courseCode));
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
				String activityType // 改为接受 activityType
		) {
			this.courseCode = courseCode;
			this.day = day;
			this.startDate = startDate;
			this.startTime = startTime;
			this.endDate = endDate;
			this.endTime = endTime;
			this.activityId = activityId;
			this.activityType = activityType;
			// 根据 activityType 设置默认状态
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
			return courseCode + " (Activity " + activityType + ") on " + day +
					" from " + startTime + " to " + endTime + " " +status;
		}
	}
}