package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class Lab extends Activity {
	public Lab(int id, LocalDate startDate, LocalTime startTime, LocalDate endDate,
			   LocalTime endTime, String location, DayOfWeek day) {
		super(id, startDate, startTime, endDate, endTime, location, day);
	}
}
