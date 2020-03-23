package no.stelvio.batch.count.support;

import java.util.Map;

/**
 *
 */
public interface EventReportFormatter {
	
	String format(Map<CounterEvent, ? extends EventCounter> eventReport);

}
