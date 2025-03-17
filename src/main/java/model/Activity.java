package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Activity {
	int id;
	LocalDate startDate;
	LocalTime startTime;
	LocalDate endDate;
	LocalTime endTime;
	String location;
	DayOfWeek day;

	public Activity(int id, LocalDate startDate, LocalTime startTime, LocalDate endDate
			, LocalTime endTime, String location, DayOfWeek day){

	}


}
