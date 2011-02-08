package no.stelvio.esb.models.transformation.servicemodel2html.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
	public static String getCurrentDateTime() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		String currentDateString = formatter.format(currentDate.getTime());
		return currentDateString;
	}
}
