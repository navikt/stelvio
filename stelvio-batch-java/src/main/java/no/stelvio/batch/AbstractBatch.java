package no.stelvio.batch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.support.TransactionTemplate;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.domain.BatchParameter;
import no.stelvio.batch.exception.BatchFunctionalException;
import no.stelvio.batch.exception.BatchSystemException;
import no.stelvio.batch.exception.InvalidBatchEntryException;
import no.stelvio.batch.exception.InvalidParameterFormatException;
import no.stelvio.batch.exception.NoSuchParameterException;
import no.stelvio.batch.exception.NullBatchException;
import no.stelvio.batch.repository.BatchRepository;
import no.stelvio.common.config.MissingPropertyException;
import no.stelvio.common.error.logging.ExceptionLogger;
import no.stelvio.common.log.InfoLogger;

/**
 * Abstract class for classes implementing scheduled batch logic.
 * 
 * <p>
 * NB!! Batch implementations of this component should be configured with PROTOTYPE scope in Spring as it has an instance
 * variable used by {@link #isStopRequested()}
 * </p>
 * 
 * @author person356941106810, Accenture
 * @author person1f201b37d484, Accenture
 * @author personf8e9850ed756, Accenture
 * @author person983601e0e117 (Accenture)
 * @author person6045563b8dec (Accenture)
 */
public abstract class AbstractBatch implements BatchBi {
	/** Default flush size. Default value (0) means flush size hint will be ignored and default value will be used */
	private static final int DEFAULT_FLUSH_SIZE = 0;

	private List<String> optionalParameters;
	
	private List<String> requiredParameters;
	
	/** The name of the batch to run. */
	private String batchName;

	/** Repository class used to update BatchDO status. */
	private BatchRepository batchRepository;

	/** ExceptionLogger used for error logging. */
	private ExceptionLogger exceptionLogger;

	/** InfoLogger used for informational logging. */
	private InfoLogger infoLogger;

	/** Flush size for session. */
	private int flushSize = DEFAULT_FLUSH_SIZE;

	/** Log for debugging minor events to debuglog. */
	private final Log log = LogFactory.getLog(this.getClass());

	/** Batch slice. */
	private int slice;

	/** Is the batch terminated, ie requested to stop. */
	// stopRequested is volatile as this variable is set by a different thread than the one executing the batch.
	private volatile boolean stopRequested;

	/** Batch timestamp, should be set when batch is about to execute. */
	private Date timeStamp;

	/** Helper class that makes working with transactions a lot easier. */
	private TransactionTemplate transactionTemplate = null;

	/**
	 * Initializes the batch with its name.
	 */
	protected AbstractBatch() {
	}

	@Override
	public String getBatchName() {
		return batchName;
	}

	@Override
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	
	/**
	 * Gets the batch repository used to access batch database table.
	 * 
	 * @return BatchRepository object's batch repository
	 */
	public BatchRepository getBatchRepository() {
		return batchRepository;
	}

	/**
	 * Sets the batch repository.
	 * 
	 * @param batchRepository
	 *            the batch respository for the object
	 */
	public void setBatchRepository(BatchRepository batchRepository) {
		this.batchRepository = batchRepository;
	}

	/**
	 * Returns the exception logger that is used by the batch.
	 * 
	 * @return exceptionLogger The exception logger
	 */
	public ExceptionLogger getExceptionLogger() {
		return this.exceptionLogger;
	}

	/**
	 * Sets the exceptionLogger.
	 * <p>
	 * NB!! Batch implementations of this component should be configured with PROTOTYPE scope in Spring, otherwise the exception
	 * logger may be shared between different batch jobs.
	 * </p>
	 * 
	 * @param exceptionLogger
	 *            The exceptionLogger to set
	 */
	public void setExceptionLogger(ExceptionLogger exceptionLogger) {
		this.exceptionLogger = exceptionLogger;
	}

	/**
	 * Returns the information logger that is used by the batch.
	 * 
	 * @return infoLogger The info logger
	 */
	public InfoLogger getInfoLogger() {
		return this.infoLogger;
	}

	/**
	 * Sets the info logger.
	 * <p>
	 * NB!! Batch implementations of this component should be configured with PROTOTYPE scope in Spring, otherwise the info
	 * logger may be shared between different batch jobs.
	 * </p>
	 * 
	 * @param infoLogger
	 *            The infoLogger to set
	 */
	public void setInfoLogger(InfoLogger infoLogger) {
		this.infoLogger = infoLogger;
	}

	/**
	 * Set flush size (used by architecture).
	 * 
	 * @param flushSize
	 *            number of objects in session before flush
	 */
	public void setFlushSize(int flushSize) {
		this.flushSize = flushSize;
	}

	@Override
	public int getSlice() {
		return slice;
	}

	@Override
	public void setSlice(int slice) {
		this.slice = slice;
	}

	@Override
	public boolean isStopRequested() {
		return stopRequested;
	}

	@Override
	public Date getTimeStamp() {
		return timeStamp;
	}

	@Override
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Returns the transaction template implementation.
	 * 
	 * @return the transaction template.
	 */
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	/**
	 * Sets the dependency to an implementation of transaction template.
	 * 
	 * @param transactionTemplate
	 *            the transaction template.
	 */
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	/**
	 * Apply flush size in configuration file. This method is a Template Method that calls {@link #flushUpdates()} The method is
	 * implemented as a template method, as the <code>AbstractBatch</code> has no way of knowing how many flushable resources
	 * a concrete implementation might have. {@link #flushUpdates()} must be implemented to flush the Batch implementations
	 * flushable resources (Typically <code>HibernateTemplate</code>s)
	 * 
	 * @param counter
	 *            number of objects handled
	 * @return int flush size
	 */
	public int applyFlushSize(int counter) {
		if (flushSize > 0) {
			if (counter % flushSize == 0) {
				flushUpdates();
			}
		}

		return this.flushSize;
	}

	/** 
	 * @throws BatchFunctionalException
	 *             if batch execution fails due to functional failures/shortcomings
	 * @throws BatchSystemException
	 *             if batch execution fails due to system failures/shortcomings
	 */
	public abstract int executeBatch(int slice) throws BatchSystemException, BatchFunctionalException;

	@Override
	public void stopBatch() {
		stopRequested = true;
	}

	/**
	 * Reads the batch configuration for the given batch.
	 * 
	 * @param batchName
	 *            the name of the batch to read config for
	 * @param slice
	 *            the number identifying the slice this batch will process
	 * @return the batch configuration or null if no config exists.
	 * @throws InvalidBatchEntryException
	 *             if the specified batch was not found or duplicate entries in database matches criteria.
	 */
	public BatchDO readBatchParameters(String batchName, int slice) throws InvalidBatchEntryException {
		// Method throws exception 0 or more than 1 batches match the batchName
		return getBatchRepository().findByNameAndSlice(batchName, slice);
	}

	@Override
	public Properties fetchBatchProperties(BatchDO batchDO) throws NullBatchException, InvalidParameterFormatException {
		if (batchDO == null) {
			throw new NullBatchException("Null was passed as param to getBatchProperties");
		}

		Properties props = new Properties();
		String params = batchDO.getParameters();

		// return null if there are no params
		if (params == null) {
			return null;
		}

		params = params.replace(';', '\n');
		ByteArrayInputStream stream = new ByteArrayInputStream(params.getBytes());

		try {
			props.load(stream);
		} catch (IOException e) {
			throw new InvalidParameterFormatException("The batch contains params that are not in the correct format.", params,
					e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Get batch properties returned:" + props);
		}
		return props;
	}
	
	/**
	 * Reads the batch properties from the batch configuration.
	 * 
	 * @param batchDO
	 *            the batch configuration
	 * @return the batch properties
	 * @throws NullBatchException
	 *             if batchDO is null.
	 * @throws InvalidParameterFormatException
	 *             if parameters are configured in the wrong format.
	 */
	protected Properties getBatchProperties(BatchDO batchDO) throws NullBatchException, InvalidParameterFormatException {
		if (batchDO == null) {
			throw new NullBatchException("Null was passed as param to getBatchProperties");
		}

		Properties props = new Properties();
		String params = batchDO.getParameters();

		// return null if there are no params
		if (params == null) {
			return null;
		}

		params = params.replace(';', '\n');
		ByteArrayInputStream stream = new ByteArrayInputStream(params.getBytes());

		try {
			props.load(stream);
		} catch (IOException e) {
			throw new InvalidParameterFormatException("The batch contains params that are not in the correct format.", params,
					e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Get batch properties returned:" + props);
		}
		return props;
	}

	/**
	 * Sets the properties on the batch configuration. This does NOT save the configuration.
	 * 
	 * @param batchDO
	 *            the batch configuration to update
	 * @param props
	 *            the the updated properties
	 * @throws NullBatchException
	 *             if batchDO is null.
	 */
	protected void setBatchProperties(BatchDO batchDO, Properties props) throws NullBatchException {
		if (props == null) {
			batchDO.setParameters(null);
			return;
		}

		if (batchDO == null) {
			throw new NullBatchException("Null Batch passed as argument to setBatchProperties");
		}

		Enumeration<?> names = props.propertyNames(); // ? should be String
		StringBuffer buf = new StringBuffer();
		String name;
		String value;

		while (names.hasMoreElements()) {
			name = (String) names.nextElement();
			buf.append(name);
			buf.append('=');

			value = props.getProperty(name);
			if (value != null) {
				buf.append(value);
			}

			if (names.hasMoreElements()) {
				buf.append(';');
			}
		}

		batchDO.setParameters(buf.toString());
	}

	/**
	 * Updates the batch with the current status of the batch. <b>MUST</b> be run inside a transaction together with the
	 * functional operation updating the database.
	 * 
	 * @param batchDO
	 *            the batch to update
	 */
	public void updateBatchStatus(BatchDO batchDO) {
		getBatchRepository().updateBatch(batchDO);
	}

	/**
	 * Method used to retrieve a given parameter value.
	 * 
	 * @param batchDO
	 *            batch configuration
	 * @param param
	 *            parameter passed to the concrete <code>BatchFunctionalException</code> implementation
	 * @return parameter defined as string
	 * @throws NullBatchException
	 *             if batchDO is null.
	 * @throws InvalidParameterFormatException
	 *             if parameters are configured in the wrong format.
	 * @throws NoSuchParameterException
	 *             if no parameter defined by the input is present in {@link BatchDO#getParameters()}
	 */
	protected String getParameter(BatchDO batchDO, BatchParameter param) throws NoSuchParameterException, NullBatchException,
			InvalidParameterFormatException {
		Properties props = getBatchProperties(batchDO);
		String property = props.getProperty(param.getParameterString());
		if (property == null) {
			throw new NoSuchParameterException("The batch does not define the parameter", batchDO, param.getParameterString());
		}
		return property;
	}

	/**
	 * Abstract method to be implemented by all subclasses. This is called by the template method {@link #applyFlushSize(int)}
	 * If the template method isn't called by the concrete batch, this method can be implemented with an empty body.
	 */
	protected abstract void flushUpdates();

	/**
	 * Method called to validate that required properties have been set by IOC-framework (Spring). Should be set up as an
	 * init-method to reduce the likelyhood of nullpointers in code. Application batches must configure:
	 * <ul>
	 * <li>Batch repository used to access batch table
	 * </ul>
	 * Batch implementations are also required to set the following properties. These should be validated in the actual batch
	 * implementation as these properties should normally be prototypes.
	 * <ul>
	 * <li>ExceptionLogger (replaces exceptionHandler if still using older version error handling)
	 * <li>InfoLogger (replaces applicationEventPublisher used to publish batch events)
	 * </ul>
	 */
	public void performSanityCheck() {
		List<String> propertyList = new ArrayList<>();
		if (getBatchRepository() == null) {
			propertyList.add("batchRepository");
		}

		if (!propertyList.isEmpty()) {
			// At least 1 prop is missing
			throw new MissingPropertyException("Required properties was not set by configuration", propertyList);
		}
	}

	@Override
	public List<String> getOptionalParameters() {
		return optionalParameters;
	}

	/**
	 * Set the optionalParameters.
	 * 
	 * @param optionalParameters the optionalParameters
	 */
	public void setOptionalParameters(List<String> optionalParameters) {
		this.optionalParameters = optionalParameters;
	}

	@Override
	public List<String> getRequiredParameters() {
		return requiredParameters;
	}

	/**
	 * Set the requiredParameters.
	 * 
	 * @param requiredParameters the requiredParameters
	 */
	public void setRequiredParameters(List<String> requiredParameters) {
		this.requiredParameters = requiredParameters;
	}
}