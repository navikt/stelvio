package no.stelvio.batch.count.support;

import java.util.Map;

/**
 * @author person47c121e3ccb5, BEKK
 *
 */
public interface EventReportFormatter {
	
	String format(Map<CounterEvent, ? extends EventCounter> eventReport);

}
