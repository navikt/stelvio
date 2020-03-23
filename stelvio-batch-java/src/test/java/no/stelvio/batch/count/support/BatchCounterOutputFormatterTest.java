package no.stelvio.batch.count.support;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import no.stelvio.batch.count.support.CounterEvent.EventType;

import org.junit.Test;

/**
 *
 */
public class BatchCounterOutputFormatterTest {
	private TableFormatter formatter = new TableFormatter();
	private CounterEvent testEvent1 = CounterEvent.createCounterEvent(getClass(),
			"btest1", "tester 1. b event", EventType.FUNCTIONAL);
	private CounterEvent testEvent2 = CounterEvent.createCounterEvent(getClass(),
			"atest2", "tester 2. a event ", EventType.TECHNICAL);
	private CounterEvent testEvent3 = CounterEvent.createCounterEvent(getClass(),
			"atest1", "tester 1. a event med laaaaaaaaaaaaaaaaaaaaaaaang beskrivelse", 
			EventType.TECHNICAL);
	
	@Test
	public void shouldCreateFormattedOutputSortedAlphabetically() {
		Map<CounterEvent, SimpleEventCounter> eventReport = new HashMap<CounterEvent, SimpleEventCounter>();
		eventReport.put(testEvent1, new SimpleEventCounter(1, 1));
		eventReport.put(testEvent2, new SimpleEventCounter(1536, 895621));
		eventReport.put(testEvent3, new SimpleEventCounter(99999999, 188888888));
		
		assertEquals("\n"
				+ "================================ BATCH COUNTERS ================================================================================\n"
				+ "|Name                          |Count     |Time(ms)  |Avg(ms)   |Type           |Description                                    |\n"
				+ "|-------------------------------------------------------------------------------------------------------------------------------|\n"
				+ "|atest1                        |  99999999| 188888888|         1|TECHNICAL      |tester 1. a event med laaaaaaaaaaaaaaaaaaaaaaaang beskrivelse|\n"
				+ "|atest2                        |      1536|    895621|       583|TECHNICAL      |tester 2. a event                              |\n"
				+ "|btest1                        |         1|         1|         1|FUNCTIONAL     |tester 1. b event                              |\n"
				+ "================================================================================================================================\n", 
				formatter.format(eventReport));
	}
}
