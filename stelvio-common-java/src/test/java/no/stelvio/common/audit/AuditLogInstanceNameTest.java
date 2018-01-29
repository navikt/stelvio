package no.stelvio.common.audit;

import static org.junit.Assert.assertTrue;
import no.stelvio.common.util.ReflectUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.commons.logging.impl.SLF4JLocationAwareLog;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.slf4j.impl.Log4jLoggerAdapter;

/**
 * Test class for AuditLogInstanceName.
 * 
 * @author MA
 * 
 */
public class AuditLogInstanceNameTest {

	private static final Log READ_LOG = LogFactory.getLog(AuditLogInstanceNames.READ);
	private static final Log WRITE_LOG = LogFactory.getLog(AuditLogInstanceNames.WRITE);

	/**
	 * Test readLogInitializedWithCorrectName.
	 */
	@Test
	public void readLogInitializedWithCorrectName() {
		AuditItem item = new AuditItem();
		READ_LOG.info(item);
        SLF4JLocationAwareLog l4jlogger = ((SLF4JLocationAwareLog) READ_LOG);
        Log4jLoggerAdapter logger = (Log4jLoggerAdapter) ReflectUtil.getFielValueFromInstance(l4jlogger, "logger");
		assertTrue(logger.getName().equals("audit.read"));
	}

	/**
	 * Test writeLogInitializedWithCorrectName.
	 */
	@Test
	public void writeLogInitializedWithCorrectName() {
		AuditItem item = new AuditItem();
		WRITE_LOG.info(item);
        SLF4JLocationAwareLog l4jlogger = ((SLF4JLocationAwareLog) WRITE_LOG);
        Log4jLoggerAdapter logger = (Log4jLoggerAdapter) ReflectUtil.getFielValueFromInstance(l4jlogger, "logger");
		assertTrue(logger.getName().equals("audit.write"));
	}

}
