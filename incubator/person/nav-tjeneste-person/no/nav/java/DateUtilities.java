package no.nav.java;

import java.sql.Date;
import no.stelvio.common.bus.util.DateUtil;

public class DateUtilities {
	public static String formatWIDString(String date) {
		return no.stelvio.common.bus.util.DateUtil.formatWIDString(Date.valueOf(date));
	}
	public static String parseTpsDateToWIDString(final String input){
		return DateUtil.formatWIDString(DateUtil.parseTpsDate(input));
	}
}