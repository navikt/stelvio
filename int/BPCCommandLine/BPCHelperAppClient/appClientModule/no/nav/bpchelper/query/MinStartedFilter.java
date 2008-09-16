package no.nav.bpchelper.query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MinStartedFilter {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	private Date started;

	public MinStartedFilter(Date started) {
		super();
		this.started = started;
	}
	
	public String getWhereClause() {
		StringBuffer sb = new StringBuffer();
		sb.append("PROCESS_INSTANCE.STARTED");
		sb.append(">=");
		sb.append("TS('").append(DATE_FORMAT.format(started)).append("')");
		return sb.toString();
	}
}
