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

	}

	public String toString() {
		return String.format("Activity{id=%d, day=%s, " +
						"startDate=%s, " +
						"startTime=%s, endDate=%s, endTime=%s, location=%s}",
				id, day, startDate, startTime, endDate, endTime, location);
	}
}
