package no.nav.bpchelper.query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimpleExpression implements Criterion {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private String columnName;

    private Object value;

    private String operator;

    protected SimpleExpression(String columnName, Object value, String operator) {
	this.columnName = columnName;
	this.value = value;
	this.operator = operator;
    }

    public String toSqlString() {
	StringBuffer sb = new StringBuffer();
	sb.append(columnName);
	sb.append(operator);
	if (value instanceof String) {
	    sb.append("'").append(value).append("'");
	} else if (value instanceof Date) {
	    sb.append("TS('").append(DATE_FORMAT.format((Date) value)).append("')");
	} else if (value instanceof Calendar) {
	    sb.append("TS('").append(DATE_FORMAT.format(((Calendar) value).getTime())).append("')");
	} else {
	    sb.append(value);
	}
	return sb.toString();
    }
}
