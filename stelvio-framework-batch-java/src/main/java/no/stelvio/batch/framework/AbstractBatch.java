package no.stelvio.batch.framework;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import no.stelvio.batch.framework.domain.BatchDO;
import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.config.ConfigurationException;
import no.stelvio.common.framework.context.RequestContext;
import no.stelvio.common.framework.error.Severity;
import no.stelvio.common.framework.error.SystemException;
import no.stelvio.common.framework.util.MessageFormatter;

/**
 * Abstract class for classes implementing scheduled batch logic.
 * 
 * @author person356941106810, Accenture
 * @author person1f201b37d484, Accenture
 * @author personf8e9850ed756, Accenture
 * @version $Id: AbstractBatch.java 2834 2006-03-10 10:41:36Z skb2930 $
 */
public abstract class AbstractBatch {

	/** Default flush size. Default value (0) means flush size hint will be ignored and default value will be used */
	private static final int DEFAULT_FLUSH_SIZE = 0;

	/** The name of the log. */
	private static final String BATCH = "BATCH";

	/** Log for writing major events to the enterprise log. */
	private static final Log ENTERPRISE_LOG = LogFactory.getLog(BATCH);

	/** Log for debugging minor events to debuglog. */
	private final Log log = LogFactory.getLog(this.getClass());

	/** The formatter used to format the message to log to the batch log. */
	private MessageFormatter msgFormatter = null;

	/** Helper class that makes working with Hibernate a lot easier. */
	private HibernateTemplate hibernateTemplate = null;

	/** Helper class that makes working with transactions a lot easier. */
	private TransactionTemplate transactionTemplate = null;

	/** Helper class that allows the execution of sql queries */
	private JdbcTemplate jdbcTemplate = null;

	/** The name of the batch to run. */
	private String batchName;

	/**
	 * Fetch Size for statements. Default value (0) means fetch size hint will be ignored and default db value will be used.
	 */
	private int fetchSize = 0;

	/**
	 * Flush size for session
	 */
	private int flushSize = DEFAULT_FLUSH_SIZE;

	/**
	 * Initializes the batch with its name.
	 *
	 * @param batchName the batch's name.
	 */
	protected AbstractBatch(String batchName) {
		this.batchName = batchName;
	}

	/**
	 * Executes a batch and returns the status.
	 *
	 * @return the status code of the batch.
	 * @throws SystemException if batch execution fails.
	 */
	public abstract int executeBatch() throws SystemException;

	/**
	 * Reads the batch configuration for the given batch.
	 *
	 * @param batchName the name of the batch to read config for
	 * @return the batch configuration or null if no config exists.
	 * @throws SystemException if the specified batch was not found.
	 */
	protected BatchDO readBatchParameters(String batchName) throws SystemException {

		final List list = getHibernateTemplate().findByNamedQueryAndNamedParam("BATCH_BY_BATCHNAME", "batchname", batchName);

		if (list.size() != 1) {
			throw new SystemException(FrameworkError.BATCH_PROPETIES_READ_ERROR,
			                           batchName + " has zero or more than 1 entries in the database");
		}

		return (BatchDO) list.get(0);
	}

	/**
	 * Reads the batch  properties from the batch configuration.
	 *
	 * @param batchDO the batch configuration
	 * @return the batch properties
	 * @throws SystemException if batchDO is null.
	 */
	protected Properties getBatchProperties(BatchDO batchDO) throws SystemException {
		if (batchDO == null) {
			throw new SystemException(FrameworkError.BATCH_PROPETIES_READ_ERROR, "Batch parameters cannot be null");
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
			throw new SystemException(
				FrameworkError.BATCH_PROPETIES_READ_ERROR,
				e,
				"Could not read batch properties; wrong format: " + params);
		}
		if (log.isDebugEnabled()) {
			log.debug("batch properties=" + props);
		}
		return props;
	}

	/**
	 * Sets the properties on the batch configuratrion. This does NOT save the configuration.
	 *
	 * @param batchDO the batch configuration to update
	 * @param props the the updated properties
	 * @throws SystemException if batchDO is null.
	 */
	protected void setBatchProperties(BatchDO batchDO, Properties props) throws SystemException {
		if (props == null) {
			batchDO.setParameters(null);
			return;
		}

		if (batchDO == null) {
			throw new SystemException(FrameworkError.BATCH_PROPETIES_WRITE_ERROR, "Batch parameters cannot be null");
		}

		Enumeration names = props.propertyNames();
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
	 * @param batchDO the batch to update
	 */
	protected void updateBatchStatus(BatchDO batchDO) {
		getHibernateTemplate().saveOrUpdate(batchDO);
	}

	/**
	 * Returns if logging of severity DEBUG is enabled for current batch class.
	 * 
	 * @return true if DEBUG is enabled, false otherwise.
	 */
	protected boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	/**
	 * Logs a message of severity DEBUG to the debug log.
	 *
	 * @param message the message to log.
	 */
	protected void debug(String message) {
		log.debug(message);
	}

	/**
	 * Logs a message of severity INFO to the enterprise log.
	 *
	 * @param message the message to log.
	 */
	protected void info(String message) {
		log(message, null);
	}

	/**
	 * Logs a message of severity WARN to the system and enterprise logs.
	 *
	 * @param message the message to log
	 */
	protected void warn(String message) {
		log(message, Severity.WARN);
		if (log.isWarnEnabled()) {
			log.warn(message);
		}
	}

	/**
	 * Logs a message of severity WARN to the system and enterprise logs.
	 *
	 * @param message the message to log.
	 * @param t the throwable to log in the system log.
	 */
	protected void warn(String message, Throwable t) {
		log(message, Severity.WARN);
		if (log.isWarnEnabled()) {
			log.warn(message, t);
		}
	}

	/**
	 * Helper method for logging a message to the batch log.
	 *
	 * @param message the message to log.
	 * @param severity the severity of the message to log.
	 */
	private void log(String message, Integer severity) {
		ENTERPRISE_LOG.info(msgFormatter.formatMessage(new Object[] { RequestContext.getUserId(), severity, BATCH, batchName, message }));
	}

	/**
	 * Returns the hibernate template implementation.
	 * 
	 * @return the hibernate template.
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * Sets the dependency to an implementation of hibernate template. 
	 * 
	 * @param hibernateTemplate the hibernate template.
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * Returns the jdbc template implementation.
	 * 
	 * @return the jdbc template.
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * Sets the dependency to an implementation of jdbc template.
	 * 
	 * @param template the jdbc template.
	 */
	public void setJdbcTemplate(JdbcTemplate template) {
		jdbcTemplate = template;
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
	 * @param transactionTemplate the transaction template.
	 */
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	/**
	 * Sets the formatter to use for formatting the log message for 469.
	 * 
	 * @param msgFormatter the message formatter.
	 */
	public void setMsgFormatter(MessageFormatter msgFormatter) {
		this.msgFormatter = msgFormatter;
	}

	/**
	 * Returns the batch name
	 *
	 * @return the name of the batch
	 */
	public String getBatchName() {
		return batchName;
	}

	/**
	 * Set fetch size (use by architecture callback - not to be used in code)
	 * @param fetchSize fetch size
	 */
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	/**
	 * Apply fetch size specified in configuration file to the input statement.
	 * @param statement The sql statement (java.sql.Statement) to apply the fetch size hint on.
	 * @param maxRows The maximal number of rows to retrieve from the result set.  If maxRows is smaller than 
	 *        the configured fetch size, the configured fetch size will not be used. A maxRows number smaller than
	 *        the fetch size indicates a small result set, so tuning the fetch size shouldn't be necessary in those
	 *        cases.
	 * @return fetchSize for information only - the fetch size used. Shouldn't be necessary to retrieve this number, 
	 *         except for debug log purposes.
	 * @throws SQLException if a db error occurs or the condition 0 <= fetchSize <= statement.getMaxRows isn't satisfied
	 */
	public int applyFetchSize(Statement statement, int maxRows) throws SQLException {
		if (maxRows < fetchSize) {
			return 0;
		}

		statement.setFetchSize(fetchSize);
		return fetchSize;
	}

	/**
	 * Apply fetch size specified in configuration file to the input query.
	 *
	 * @param query The Hibernate query (net.sf.hibernate.Query) to apply the fetch size hint on.
	 * @param maxRows The maximal number of rows to retrieve from the result set.  If maxRows is smaller than 
	 *        the configured fetch size, the configured fetch size will not be used. A maxRows number smaller than
	 *        the fetch size indicates a small result set, so tuning the fetch size shouldn't be necessary in those
	 *        cases.
	 * @return fetchSize for information only - the fetch size used. Shouldn't be necessary to retrieve this number, 
	 *         except for debug log purposes.
	 */
	public int applyFetchSize(Query query, int maxRows) {
		if (maxRows < fetchSize) {
			return 0;
		}

		query.setFetchSize(fetchSize);
		return fetchSize;
	}

	/**
	 * Set flush size (used by architecture)
	 * 
	 * @param flushSize number of objects in session before flush
	 */
	public void setFlushSize(int flushSize) {
		this.flushSize = flushSize;
	}

	/**
	 * Apply flush size in configuration file
	 * 
	 * @param counter number of objects handled
	 * @return int flush size
	 */
	public int applyFlushSize(int counter) {
		if (flushSize > 0) {
			if (counter % flushSize == 0) {
				hibernateTemplate.flush();
				hibernateTemplate.clear();
			}
		}

		return this.flushSize;
	}

	/**
	 * Returns the query details for the named query.
	 * 
	 * @param queryName the name of the query.
	 * @return the query details, containing HQL/SQL.
	 * @throws ConfigurationException if the named query does'nt exist.
	 * @deprecated Put the query into sql-queries and put it into a property on your bean class by using ${property name}
	 */
	protected String getNamedQueryString(final String queryName) throws ConfigurationException {
		throw new ConfigurationException(FrameworkError.HIBERNATE_QUERY_CREATE_ERROR, "deprecated usage: " + queryName);
	}
}
