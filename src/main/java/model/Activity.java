package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

public abstract class Activity {
	private int id;
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalDate endDate;
	private LocalTime endTime;
	private String location;
	private DayOfWeek day;


	public Activity(int id, LocalDate startDate, LocalTime startTime, LocalDate endDate
			, LocalTime endTime, String location, DayOfWeek day){
		this.id = id;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.endTime = endTime;
		this.location = location;
		this.day = day;

	}

	public int getId() {
		return id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public String getLocation() {
		return location;
	}

	public DayOfWeek getDay() {
		return day;
	}

	public String toString() {
		return String.format("Activity{id=%d, day=%s, " +
						"startDate=%s, " +
						"startTime=%s, endDate=%s, endTime=%s, location=%s}",
				id, day, startDate, startTime, endDate, endTime, location);
	}

	public boolean hasId(int activityId){
		//check if each activity has a valid id
        return activityId == id;
    }


}
