package no.stelvio.batch.controller.client.support;

import no.stelvio.batch.BatchStatus;
import no.stelvio.batch.controller.BatchControllerServiceBi;
import no.stelvio.batch.controller.client.BatchRunner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Generic class for reading the controllerclient configuration and starting the batch Use <code>BatchControllerServiceBi</code>
 * to execute the batch on the server side The controllerclient configuration is placed in btc-controllerclient-context.xml
 * file. This file is using the btc-controllerclient.properties file which contains the spesific variables for the batch Use
 * Log4j for loging
 * 
 * @author persond9e847e67144, NAV
 * 
 * @version $Id$
 */
public class DefaultBatchRunner implements BatchRunner {

	private static final String CORBA_LOGIN_USER_ID = "com.ibm.CORBA.loginUserid";
	private BatchControllerServiceBi controllerService;
	private static final String BATCH_CONTROLLERCLIENT_CONTEXT = "btc-controllerclient-context.xml";
	private static final String BATCH_CONTROLLERSERVICE = "batch.controllerService";
	
	/** Logger. */
	protected static final Log log = LogFactory.getLog(DefaultBatchRunner.class);

	/**
	 * Gets the com.ibm.CORBA.loginUserid from System.properties if this property is set, returns the string 'No user found'
	 * otherwise.
	 * 
	 * @return the com.ibm.CORBA.loginUserid if set, 'No user found' otherwise.
	 */
	protected String getCorbaUserId() {
		String prop = System.getProperty(CORBA_LOGIN_USER_ID);
		String userId = prop != null && !prop.equals("") ? prop : "No user found.";
		return userId;
	}

	/** {@inheritDoc} */
	public int runBatch(String batchName, int slice) {
		int result = -1;
		long time1 = System.currentTimeMillis();

		try {
			BatchControllerServiceBi controller = getControllerService();

			if (log.isInfoEnabled()) {
				log.info("Starting batch: " + batchName + " using slice: " + slice + " with user: " + getCorbaUserId());
			}
			result = controller.executeBatch(batchName, slice);

		} catch (Exception ex) {
			if (log.isFatalEnabled()) {
				log.fatal("Batch " + batchName + " slice: " + slice + "terminated with exception: " + ex.getMessage());
			}
			result = BatchStatus.BATCH_FATAL;
		} finally {
			long time2 = System.currentTimeMillis();
			long timeSecs = (time2 - time1) / 1000;

			if (log.isInfoEnabled()) {
				log.info("Batch: " + batchName + " slice: " + slice + " terminated with status: " + result);
				log.info("Total time (sec) = " + timeSecs);
			}
		}

		return result;
	}

	/** {@inheritDoc} */
	public boolean stopBatch(String batchName, int slice) {
		boolean result = false;
		try {
			BatchControllerServiceBi controller = getControllerService();
			if (log.isInfoEnabled()) {
				log.info("Stopping batch: " + batchName + " using slice: " + slice + " with user: " + getCorbaUserId());
			}
			result = controller.stopBatch(batchName, slice);
		} catch (Exception e) {
			if (log.isFatalEnabled()) {
				log.fatal("Error occured while trying to stop batch " + batchName + " slice: " + slice
						+ ". The exception was: " + e.getMessage());
			}
			result = false;
		}
		return result;
	}

	/** {@inheritDoc} */
	public BatchControllerServiceBi getControllerService() {

		if (controllerService == null) {
			try {
				ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext(
						BATCH_CONTROLLERCLIENT_CONTEXT);

				controllerService = (BatchControllerServiceBi) springContext.getBean(BATCH_CONTROLLERSERVICE);
			} catch (BeansException be) {
				if (log.isErrorEnabled()) {
					log.error("Can not find BatchControllerServiceBi, this may have "
							+ "been caused by a insufficient configuration");
					log.error("The error trace is : " + be.getMessage());
				}
			}
		}

		return controllerService;
	}

	/** {@inheritDoc} */
	public void setControllerService(BatchControllerServiceBi controllerService) {
		this.controllerService = controllerService;
	}

}
