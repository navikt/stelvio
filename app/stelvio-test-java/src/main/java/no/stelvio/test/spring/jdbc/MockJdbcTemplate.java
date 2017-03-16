package no.stelvio.test.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/**
 * A mocked version of {@link JdbcTemplate} that should be used in unit tests.
 * 
 * @author person02f3de2754b4, Accenture
 */
public class MockJdbcTemplate extends JdbcTemplate { // implements Verifiable, BuilderNamespace {

	private final JUnit4Mockery context = new JUnit4Mockery();
	private final JdbcTemplate proxyJdbcTemplate = context.mock(JdbcTemplate.class);
	private final ResultSet proxyResultSet = context.mock(ResultSet.class);

	/**
	 * Sets the expected number of times a method should be called. Use the returned value to set the expected method.
	 * 
	 * @param expectations
	 *            a matcher for setting how many times a method should be called.
	 */
	public void expects(Expectations expectations) {
		context.checking(expectations);
	}

	/**
	 * Sets the expected return for a result set.
	 * 
	 * @param expectations
	 *            a matcher for setting how many times a method should be called
	 */
	public void expectsOnResultSet(Expectations expectations) {
		context.checking(expectations);
	}

	/**
	 * @see org.springframework.jdbc.support.JdbcAccessor#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		proxyJdbcTemplate.afterPropertiesSet();
	}

	/**
	 * Issue multiple updates on a single PreparedStatement.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#batchUpdate(java.lang.String,
	 *      org.springframework.jdbc.core.BatchPreparedStatementSetter)
	 * @param sql
	 *            defining PreparedStatement that will be reused. All statements in the batch will use the same sql
	 * @param pss
	 *            object to set parameters on the PreparedStatement created by this method
	 * @throws DataAccessException
	 *             if there is any problem issuing the update
	 * @return an array of the number of rows affected by each statement
	 */
	public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException {
		return proxyJdbcTemplate.batchUpdate(sql, pss);
	}

	/**
	 * Wxecute a SQL call using a CallableStatementCreator.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#call(org.springframework.jdbc.core.CallableStatementCreator,
	 *      java.util.List)
	 * 
	 * @param csc
	 *            object that provides SQL and any necessary parameters
	 * @param declaredParameters
	 *            list of declared SqlParameter objects
	 * @throws DataAccessException
	 *             if there is any problem issuing an update
	 * @return Map of extracted out parameters
	 */
	public Map<String, Object> call(CallableStatementCreator csc, List<SqlParameter> declaredParameters) throws DataAccessException {
		return proxyJdbcTemplate.call(csc, declaredParameters);
	}

	/**
	 * Compares two JdbcTemplate objects.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj
	 *            the object to compare with this objects JdbcTemplate
	 * @return <code>true</code> if the objects are equal, <code>false</code> if not
	 */
	public boolean equals(Object obj) {
		return proxyJdbcTemplate.equals(obj);
	}

	/**
	 * Issue a single SQL execute.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String)
	 * @param sql
	 *            static SQL to execute
	 * @throws DataAccessException
	 *             if there is any problem
	 */
	public void execute(String sql) throws DataAccessException {
		proxyJdbcTemplate.execute(sql);
	}

	/**
	 * Execute a JDBC data access operation on the proxyJdbcTemplate.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String,
	 *      org.springframework.jdbc.core.CallableStatementCallback)
	 * @param callString
	 *            the SQL call String to execute
	 * @param action
	 *            calback object that specifies the action
	 * @return a result object returned by the action or null
	 * @throws DataAccessException
	 *             if there is any problem
	 */
	public <T> T execute(String callString, CallableStatementCallback<T> action) throws DataAccessException {
		return proxyJdbcTemplate.execute(callString, action);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String, org.springframework.jdbc.core.PreparedStatementCallback)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String,
	 *      org.springframework.jdbc.core.PreparedStatementCallback)
	 * 
	 * @param sql
	 *            SQL to execute
	 * @param action
	 *            callback object that specifies the action
	 * @return a result object returned by the action or null
	 * @throws DataAccessException
	 *             if there is any problem
	 */
	public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
		return proxyJdbcTemplate.execute(sql, action);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.CallableStatementCreator, org.springframework.jdbc.core.CallableStatementCallback)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.CallableStatementCreator,
	 *      org.springframework.jdbc.core.CallableStatementCallback)
	 * @param csc
	 *            object that can create a CallableStatement given a connection
	 * @param action
	 *            callback object that specifies the action
	 * @return a result object returned by the action or null
	 * @throws DataAccessException
	 *             if there is any problem
	 */
	public <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action) throws DataAccessException {
		return proxyJdbcTemplate.execute(csc, action);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.PreparedStatementCallback)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.core.PreparedStatementCallback)
	 * @param psc
	 *            object that can create a PreparedStatment given a connection
	 * @param action
	 *            callback object that specifies the action
	 * @return a result object returned by the action or null
	 * @throws DataAccessException
	 *             if there is any problem
	 */
	public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException {
		return proxyJdbcTemplate.execute(psc, action);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.StatementCallback)</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.StatementCallback)
	 * @param action
	 *            callback object that specifies the action
	 * @return a result object returned by the action or null
	 * @throws DataAccessException
	 *             if there is any problem
	 */
	public <T> T execute(StatementCallback<T> action) throws DataAccessException {
		return proxyJdbcTemplate.execute(action);
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.support.JdbcAccessor#getDataSource()</code>.
	 * 
	 * @see org.springframework.jdbc.support.JdbcAccessor#getDataSource()
	 * @return Return the DataSource used by this template.
	 */
	public DataSource getDataSource() {
		return proxyJdbcTemplate.getDataSource();
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.support.JdbcAccessor#getExceptionTranslator()</code>.
	 * 
	 * @see org.springframework.jdbc.support.JdbcAccessor#getExceptionTranslator()
	 * @return Return the exception translator for this instance.
	 */
	public SQLExceptionTranslator getExceptionTranslator() {
		return proxyJdbcTemplate.getExceptionTranslator();
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.core.JdbcTemplate#getFetchSize()</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#getFetchSize()
	 * @return Return the fetch size specified for the JdbcTemplate.
	 */
	public int getFetchSize() {
		return proxyJdbcTemplate.getFetchSize();
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.core.JdbcTemplate#isIgnoreWarnings() </code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#isIgnoreWarnings()
	 * @return Return whether or not we ignore SQLWarnings
	 */
	public boolean isIgnoreWarnings() {
		return proxyJdbcTemplate.isIgnoreWarnings();
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.core.JdbcTemplate#getNativeJdbcExtractor()</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#getNativeJdbcExtractor()
	 * @return Return the current NativeJdbcExtractor implementation.
	 */
	public NativeJdbcExtractor getNativeJdbcExtractor() {
		return proxyJdbcTemplate.getNativeJdbcExtractor();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return proxyJdbcTemplate.hashCode();
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[], org.springframework.jdbc.core.ResultSetExtractor)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[],
	 *      org.springframework.jdbc.core.ResultSetExtractor)
	 * @param sql
	 *            - SQL query to execute
	 * @param args
	 *            - arguments to bind to the query
	 * @param argTypes
	 *            - SQL types of the arguments (constants from java.sql.Types)
	 * @param rse
	 *            - object that will extract results
	 * @return an arbitrary result object, as returned by the ResultSetExtractor
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse) throws DataAccessException {
		return proxyJdbcTemplate.query(sql, args, argTypes, rse);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[], org.springframework.jdbc.core.RowCallbackHandler)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[],
	 *      org.springframework.jdbc.core.RowCallbackHandler)
	 * @param sql
	 *            - SQL query to execute
	 * @param args
	 *            - arguments to bind to the query
	 * @param argTypes
	 *            - SQL types of the arguments (constants from java.sql.Types)
	 * @param rch
	 *            - object that will extract results, one row at a time
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
		proxyJdbcTemplate.query(sql, args, argTypes, rch);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[], org.springframework.jdbc.core.RowMapper)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[],
	 *      org.springframework.jdbc.core.RowMapper)
	 * @param sql
	 *            - SQL query to execute
	 * @param args
	 *            - arguments to bind to the query
	 * @param argTypes
	 *            - SQL types of the arguments (constants from java.sql.Types)
	 * @param rowMapper
	 *            - object that will map one object per row
	 * @return the result List, containing mapped objects
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
		return proxyJdbcTemplate.query(sql, args, argTypes, rowMapper);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.ResultSetExtractor)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[],
	 *      org.springframework.jdbc.core.ResultSetExtractor)
	 * @param sql
	 *            - SQL query to execute
	 * @param args
	 *            - arguments to bind to the query (leaving it to the PreparedStatement to guess the corresponding SQL type)
	 * @param rse
	 *            - object that will extract results
	 * @return an arbitrary result object, as returned by the ResultSetExtractor
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) throws DataAccessException {
		return proxyJdbcTemplate.query(sql, args, rse);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.RowCallbackHandler)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[],
	 *      org.springframework.jdbc.core.RowCallbackHandler)
	 * @param sql
	 *            - SQL query to execute
	 * @param args
	 *            - arguments to bind to the query (leaving it to the PreparedStatement to guess the corresponding SQL type)
	 * @param rch
	 *            - object that will extract results, one row at a time
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException {
		proxyJdbcTemplate.query(sql, args, rch);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.RowMapper)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[],
	 *      org.springframework.jdbc.core.RowMapper)
	 * @param sql
	 *            - SQL query to execute
	 * @param args
	 *            - arguments to bind to the query (leaving it to the PreparedStatement to guess the corresponding SQL type)
	 * @param rowMapper
	 *            - object that will map one object per row
	 * @return the result List, containing mapped objects
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
		return proxyJdbcTemplate.query(sql, args, rowMapper);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.ResultSetExtractor)</code>
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.ResultSetExtractor)
	 * @param sql
	 *            - SQL query to execute
	 * @param pss
	 *            - object that knows how to set values on the prepared statement. If this is null, the SQL will be assumed to
	 *            contain no bind parameters. Even if there are no bind parameters, this object may be used to set fetch size
	 *            and other performance options.
	 * @param rse
	 *            - object that will extract results
	 * @throws DataAccessException
	 *             if the query fails
	 * @return an arbitrary result object, as returned by the ResultSetExtractor
	 */
	public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
		return proxyJdbcTemplate.query(sql, pss, rse);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.RowCallbackHandler)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.RowCallbackHandler)
	 * @param sql
	 *            - SQL query to execute
	 * @param pss
	 *            - object that knows how to set values on the prepared statement. If this is null, the SQL will be assumed to
	 *            contain no bind parameters. Even if there are no bind parameters, this object may be used to set fetch size
	 *            and other performance options.
	 * @param rch
	 *            - object that will extract results, one row at a time
	 * @throws DataAccessException
	 *             if the query fails
	 * 
	 */
	public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException {
		proxyJdbcTemplate.query(sql, pss, rch);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.RowMapper)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.RowMapper)
	 * @param sql
	 *            - SQL query to execute
	 * @param pss
	 *            - object that knows how to set values on the prepared statement. If this is null, the SQL will be assumed to
	 *            contain no bind parameters. Even if there are no bind parameters, this object may be used to set fetch size
	 *            and other performance options.
	 * @param rowMapper
	 *            - object that will map one object per row
	 * @throws DataAccessException
	 *             if the query fails
	 * @return the result List, containing mapped objects
	 */
	public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException {
		return proxyJdbcTemplate.query(sql, pss, rowMapper);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.ResultSetExtractor)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.ResultSetExtractor)
	 * @param sql
	 *            - SQL query to execute
	 * @param rse
	 *            - object that will extract all rows of results
	 * @return an arbitrary result object, as returned by the ResultSetExtractor
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
		return proxyJdbcTemplate.query(sql, rse);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.RowCallbackHandler)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.RowCallbackHandler)
	 * @param sql
	 *            - SQL query to execute
	 * @param rch
	 *            - object that will extract results, one row at a time
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
		proxyJdbcTemplate.query(sql, rch);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.RowMapper)</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.RowMapper)
	 * @param sql
	 *            - SQL query to execute
	 * @param rowMapper
	 *            - object that will map one object per row
	 * @throws DataAccessException
	 *             if the query fails
	 * @return the result List, containing mapped objects
	 * 
	 */
	public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		return proxyJdbcTemplate.query(sql, rowMapper);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.ResultSetExtractor)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.core.ResultSetExtractor)
	 * @throws DataAccessException
	 *             if the query fails
	 * @param creator
	 *            - object that can create a PreparedStatement given a Connection
	 * @param extractor
	 *            - object that will extract results
	 * @return an arbitrary result object, as returned by the ResultSetExtractor
	 */
	public <T> T query(PreparedStatementCreator creator, ResultSetExtractor<T> extractor) throws DataAccessException {
		try {
			return extractor.extractData(proxyResultSet);
		} catch (SQLException sqle) {
			throw new DataRetrievalFailureException("Could not extract data", sqle);
		}
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.RowCallbackHandler)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.core.RowCallbackHandler)
	 * @param psc
	 *            - object that can create a PreparedStatement given a Connection
	 * @param rch
	 *            - object that will extract results, one row at a time
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws DataAccessException {
		proxyJdbcTemplate.query(psc, rch);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.RowMapper)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.core.RowMapper)
	 * @param psc
	 *            - object that can create a PreparedStatement given a Connection
	 * @param rowMapper
	 *            - object that will map one object per row
	 * @return the result List, containing mapped objects
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws DataAccessException {
		return proxyJdbcTemplate.query(psc, rowMapper);
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String)</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String)
	 * @param sql
	 *            - SQL query to execute
	 * @throws DataAccessException
	 *             if the query fails
	 * @return the int value, or 0 in case of SQL NULL
	 */
	public int queryForInt(String sql) throws DataAccessException {
		return proxyJdbcTemplate.queryForInt(sql);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String, java.lang.Object[])</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String, java.lang.Object[])
	 * @param sql
	 *            - SQL query to execute
	 * @param args
	 *            - arguments to bind to the query (leaving it to the PreparedStatement to guess the corresponding SQL type)
	 * @throws DataAccessException
	 *             if the query fails
	 * @return the int value, or 0 in case of SQL NULL
	 * 
	 */
	public int queryForInt(String sql, Object[] args) throws DataAccessException {
		return proxyJdbcTemplate.queryForInt(sql, args);
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String)</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String)
	 * @param sql
	 *            - SQL query to execute
	 * @return the result List, containing mapped objects
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
		return proxyJdbcTemplate.queryForList(sql);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String, java.lang.Object[])</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String, java.lang.Object[])
	 * @param sql
	 *            - SQL query to execute
	 * @param args
	 *            - arguments to bind to the query (leaving it to the PreparedStatement to guess the corresponding SQL type)
	 * @return the result List, containing mapped objects
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public List<Map<String, Object>> queryForList(String sql, Object[] args) throws DataAccessException {
		return proxyJdbcTemplate.queryForList(sql, args);
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String)</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String)
	 * @param sql
	 *            - SQL query to execute
	 * @return the long value, or 0 in case of SQL NULL
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public long queryForLong(String sql) throws DataAccessException {
		return proxyJdbcTemplate.queryForLong(sql);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String, java.lang.Object[])</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String, java.lang.Object[])
	 * @param sql
	 *            - SQL query to execute
	 * @param args
	 *            - arguments to bind to the query (leaving it to the PreparedStatement to guess the corresponding SQL type)
	 * @return the long value, or 0 in case of SQL NULL
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public long queryForLong(String sql, Object[] args) throws DataAccessException {
		return proxyJdbcTemplate.queryForLong(sql, args);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Class)</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Class)
	 * @param sql
	 *            - SQL query to execute
	 * @param requiredType
	 *            - the type that the result object is expected to match
	 * @return the result object of the required type, or null in case of SQL NULL
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
		return proxyJdbcTemplate.queryForObject(sql, requiredType);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Object[], java.lang.Class)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Object[], java.lang.Class)
	 * @param sql
	 *            - SQL query to execute
	 * @param args
	 *            - arguments to bind to the query (leaving it to the PreparedStatement to guess the corresponding SQL type)
	 * @param requiredType
	 *            - the type that the result object is expected to match
	 * @return the result object of the required type, or null in case of SQL NULL
	 * @throws DataAccessException
	 *             if the query fails
	 */
	public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
		return proxyJdbcTemplate.queryForObject(sql, args, requiredType);
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.support.JdbcAccessor#setDataSource(javax.sql.DataSource)</code>.
	 * 
	 * @see org.springframework.jdbc.support.JdbcAccessor#setDataSource(javax.sql.DataSource)
	 * @param dataSource
	 *            the datasource to set
	 */
	public void setDataSource(DataSource dataSource) {
		proxyJdbcTemplate.setDataSource(dataSource);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.support.JdbcAccessor#setExceptionTranslator(org.springframework.jdbc.support.SQLExceptionTranslator)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.support.JdbcAccessor#setExceptionTranslator(org.springframework.jdbc.support.SQLExceptionTranslator)
	 * @param et
	 *            the SQLExceptionTranslator to set
	 */
	public void setExceptionTranslator(SQLExceptionTranslator et) {
		proxyJdbcTemplate.setExceptionTranslator(et);
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.core.JdbcTemplate#setFetchSize(int)</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#setFetchSize(int)
	 * @param size
	 *            the fetch size to set
	 */
	public void setFetchSize(int size) {
		proxyJdbcTemplate.setFetchSize(size);
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.core.JdbcTemplate#setIgnoreWarnings(boolean)</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#setIgnoreWarnings(boolean)
	 * @param ignore
	 *            the value to set for ignore warnings
	 */
	public void setIgnoreWarnings(boolean ignore) {
		proxyJdbcTemplate.setIgnoreWarnings(ignore);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#setNativeJdbcExtractor(org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#setNativeJdbcExtractor(org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor)
	 * @param nje
	 *            the NativeJdbcExtractor to set
	 */
	public void setNativeJdbcExtractor(NativeJdbcExtractor nje) {
		proxyJdbcTemplate.setNativeJdbcExtractor(nje);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return proxyJdbcTemplate.toString();
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String)</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String)
	 * @param sql
	 *            - static SQL to execute
	 * @return the number of rows affected
	 * @throws DataAccessException
	 *             - if there is any problem issuing the update
	 */
	public int update(String sql) throws DataAccessException {
		return proxyJdbcTemplate.update(sql);
	}

	/**
	 * Mocks the call to <code>org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, java.lang.Object[])</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, java.lang.Object[])
	 * @param sql
	 *            - static SQL to execute
	 * @param args
	 *            - arguments to bind to the query (leaving it to the PreparedStatement to guess the corresponding SQL type)
	 * @return the number of rows affected
	 * @throws DataAccessException
	 *             - if there is any problem issuing the update
	 */
	public int update(String sql, Object[] args) throws DataAccessException {
		return proxyJdbcTemplate.update(sql, args);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, java.lang.Object[], int[])</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, java.lang.Object[], int[])
	 * @param sql
	 *            - static SQL to execute
	 * @param args
	 *            - arguments to bind to the query (leaving it to the PreparedStatement to guess the corresponding SQL type)
	 * @param argTypes
	 *            - SQL types of the arguments (constants from java.sql.Types)
	 * @return the number of rows affected
	 * @throws DataAccessException
	 *             - if there is any problem issuing the update
	 */
	public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return proxyJdbcTemplate.update(sql, args, argTypes);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String,
	 *      org.springframework.jdbc.core.PreparedStatementSetter)
	 * @param sql
	 *            - static SQL to execute
	 * @param pss
	 *            - helper that sets bind parameters. If this is null we run an update with static SQL.
	 * @return the number of rows affected
	 * @throws DataAccessException
	 *             - if there is any problem issuing the update
	 */
	public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
		return proxyJdbcTemplate.update(sql, pss);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#update(org.springframework.jdbc.core.PreparedStatementCreator)</code>.
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(org.springframework.jdbc.core.PreparedStatementCreator)
	 * @param psc
	 *            - object that provides SQL and any necessary parameters
	 * @return the number of rows affected
	 * @throws DataAccessException
	 *             - if there is any problem issuing the update
	 */
	public int update(PreparedStatementCreator psc) throws DataAccessException {
		return proxyJdbcTemplate.update(psc);
	}

	/**
	 * Mocks the call to
	 * <code>org.springframework.jdbc.core.JdbcTemplate#update(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.support.KeyHolder)</code>
	 * .
	 * 
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.support.KeyHolder)
	 * @param psc
	 *            - object that provides SQL and any necessary parameters
	 * @param generatedKeyHolder
	 *            - KeyHolder that will hold the generated keys
	 * @return the number of rows affected
	 * @throws DataAccessException
	 *             - if there is any problem issuing the update
	 */
	public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws DataAccessException {
		return proxyJdbcTemplate.update(psc, generatedKeyHolder);
	}

	/**
	 * Verifies that the JdbcTemplate and ResultSet are used correctly.
	 */
	public void verify() {
		context.assertIsSatisfied();
	}

}
