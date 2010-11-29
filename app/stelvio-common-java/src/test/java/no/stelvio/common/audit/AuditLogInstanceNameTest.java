package no.stelvio.common.audit;

import static org.junit.Assert.assertTrue;
import no.stelvio.common.util.ReflectUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;
import org.junit.Test;

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
		Log4JLogger l4jlogger = ((Log4JLogger) READ_LOG);
		Logger logger = (Logger) ReflectUtil.getFielValueFromInstance(l4jlogger, "logger");
		assertTrue(logger.getName().equals("audit.read"));
	}

	/**
	 * Test writeLogInitializedWithCorrectName.
	 */
	@Test
	public void writeLogInitializedWithCorrectName() {
		AuditItem item = new AuditItem();
		READ_LOG.info(item);
		Log4JLogger l4jlogger = ((Log4JLogger) WRITE_LOG);
		Logger logger = (Logger) ReflectUtil.getFielValueFromInstance(l4jlogger, "logger");
		assertTrue(logger.getName().equals("audit.write"));
	}

}
