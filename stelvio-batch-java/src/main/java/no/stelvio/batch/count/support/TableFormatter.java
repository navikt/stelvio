package no.stelvio.batch.count.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Formats batch counting info, input data is {@link BatchCounter#getEventReport()}.
 *
 */
public class TableFormatter implements EventReportFormatter {

	/**
	 * Formats a report into tabular form
	 * @param eventReport Report to format
	 * @return Tabular string representation
	 */
	@Override
	public String format(Map<CounterEvent, ? extends EventCounter> eventReport) {
		List<Entry<CounterEvent, ? extends EventCounter>> events = sortEvents(eventReport);
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("================================ BATCH COUNTERS ================================================================================\n");		
		sb.append("|Name                          |Count     |Time(ms)  |Avg(ms)   |Type           |Description                                    |\n");
		sb.append("|-------------------------------------------------------------------------------------------------------------------------------|\n");		
		for (Entry<CounterEvent, ? extends EventCounter> event : events) {
			String format = "|%1$-30s|%2$10s|%3$10s|%4$10s|%5$-15s|%6$-47s|\n";
			sb.append(String.format(format, 
					event.getKey().getName(),
					event.getValue().getCount(),
					event.getValue().getTime(),
					event.getValue().getAvg(), 
					event.getKey().getType(),
					event.getKey().getDescription()));
		}
		sb.append("================================================================================================================================\n");
		return sb.toString();
	}

	private List<Entry<CounterEvent, ? extends EventCounter>> sortEvents(Map<CounterEvent, ? extends EventCounter> eventReport) {
		List<Entry<CounterEvent, ? extends EventCounter>> events = new ArrayList<>(eventReport.entrySet());

		events.sort((o1, o2) -> o1.getKey().getName().compareToIgnoreCase(o2.getKey().getName()));
		return events;
	}



}
