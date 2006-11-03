package no.stelvio.common.test.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.jmock.builder.BuilderNamespace;
import org.jmock.builder.MatchBuilder;
import org.jmock.builder.NameMatchBuilder;
import org.jmock.cglib.Mock;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Verifiable;
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
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/**
 * @author person02f3de2754b4, Accenture
 */
public class MockJdbcTemplate extends JdbcTemplate implements Verifiable, BuilderNamespace {

	private final Mock mockJdbcTemplate = new Mock(JdbcTemplate.class);
	private final JdbcTemplate proxyJdbcTemplate = (JdbcTemplate) mockJdbcTemplate.proxy();

	private final Mock mockResultSet = new Mock(ResultSet.class);
	private final ResultSet proxyResultSet = (ResultSet) mockResultSet.proxy();
	
	/**
	 * Sets the expected return for a result set
	 *
	 * @param invocationMatcher a matcher for setting return from result set
	 * @return an instance of NameMatchBuilder to use for setting which method should be called.
	 * @see NameMatchBuilder
	 */
	public NameMatchBuilder expectsOnResultSet(InvocationMatcher invocationMatcher) {
		return mockResultSet.expects(invocationMatcher);
	}

	/**
	 * @see org.springframework.jdbc.support.JdbcAccessor#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		proxyJdbcTemplate.afterPropertiesSet();
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#batchUpdate(java.lang.String, org.springframework.jdbc.core.BatchPreparedStatementSetter)
	 */
	public int[] batchUpdate(String arg0, BatchPreparedStatementSetter arg1) throws DataAccessException {
		return proxyJdbcTemplate.batchUpdate(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#call(org.springframework.jdbc.core.CallableStatementCreator, java.util.List)
	 */
	public Map call(CallableStatementCreator arg0, List arg1) throws DataAccessException {
		return proxyJdbcTemplate.call(arg0, arg1);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return proxyJdbcTemplate.equals(obj);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String)
	 */
	public void execute(String arg0) throws DataAccessException {
		proxyJdbcTemplate.execute(arg0);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String, org.springframework.jdbc.core.CallableStatementCallback)
	 */
	public Object execute(String arg0, CallableStatementCallback arg1) throws DataAccessException {
		return proxyJdbcTemplate.execute(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String, org.springframework.jdbc.core.PreparedStatementCallback)
	 */
	public Object execute(String arg0, PreparedStatementCallback arg1) throws DataAccessException {
		return proxyJdbcTemplate.execute(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.CallableStatementCreator, org.springframework.jdbc.core.CallableStatementCallback)
	 */
	public Object execute(CallableStatementCreator arg0, CallableStatementCallback arg1) throws DataAccessException {
		return proxyJdbcTemplate.execute(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.PreparedStatementCallback)
	 */
	public Object execute(PreparedStatementCreator arg0, PreparedStatementCallback arg1) throws DataAccessException {
		return proxyJdbcTemplate.execute(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.StatementCallback)
	 */
	public Object execute(StatementCallback arg0) throws DataAccessException {
		return proxyJdbcTemplate.execute(arg0);
	}

	/**
	 * @see org.springframework.jdbc.support.JdbcAccessor#getDataSource()
	 */
	public DataSource getDataSource() {
		return proxyJdbcTemplate.getDataSource();
	}

	/**
	 * @see org.springframework.jdbc.support.JdbcAccessor#getExceptionTranslator()
	 */
	public SQLExceptionTranslator getExceptionTranslator() {
		return proxyJdbcTemplate.getExceptionTranslator();
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#getFetchSize()
	 */
	public int getFetchSize() {
		return proxyJdbcTemplate.getFetchSize();
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#isIgnoreWarnings() 
	 */
	public boolean isIgnoreWarnings() {
		return proxyJdbcTemplate.isIgnoreWarnings();
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#getNativeJdbcExtractor()
	 */
	public NativeJdbcExtractor getNativeJdbcExtractor() {
		return proxyJdbcTemplate.getNativeJdbcExtractor();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return proxyJdbcTemplate.hashCode();
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[], org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(String arg0, Object[] arg1, int[] arg2, ResultSetExtractor arg3) throws DataAccessException {
		return proxyJdbcTemplate.query(arg0, arg1, arg2, arg3);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[], org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String arg0, Object[] arg1, int[] arg2, RowCallbackHandler arg3) throws DataAccessException {
		proxyJdbcTemplate.query(arg0, arg1, arg2, arg3);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[], org.springframework.jdbc.core.RowMapper)
	 */
	public List query(String arg0, Object[] arg1, int[] arg2, RowMapper arg3) throws DataAccessException {
		return proxyJdbcTemplate.query(arg0, arg1, arg2, arg3);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(String arg0, Object[] arg1, ResultSetExtractor arg2) throws DataAccessException {
		return proxyJdbcTemplate.query(arg0, arg1, arg2);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String arg0, Object[] arg1, RowCallbackHandler arg2) throws DataAccessException {
		proxyJdbcTemplate.query(arg0, arg1, arg2);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.RowMapper)
	 */
	public List query(String arg0, Object[] arg1, RowMapper arg2) throws DataAccessException {
		return proxyJdbcTemplate.query(arg0, arg1, arg2);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(String arg0, PreparedStatementSetter arg1, ResultSetExtractor arg2) throws DataAccessException {
		return proxyJdbcTemplate.query(arg0, arg1, arg2);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String arg0, PreparedStatementSetter arg1, RowCallbackHandler arg2) throws DataAccessException {
		proxyJdbcTemplate.query(arg0, arg1, arg2);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.RowMapper)
	 */
	public List query(String arg0, PreparedStatementSetter arg1, RowMapper arg2) throws DataAccessException {
		return proxyJdbcTemplate.query(arg0, arg1, arg2);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(String arg0, ResultSetExtractor arg1) throws DataAccessException {
		return proxyJdbcTemplate.query(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String arg0, RowCallbackHandler arg1) throws DataAccessException {
		proxyJdbcTemplate.query(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.RowMapper)
	 */
	public List query(String arg0, RowMapper arg1) throws DataAccessException {
		return proxyJdbcTemplate.query(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(PreparedStatementCreator creator, ResultSetExtractor extractor) throws DataAccessException {
		try {
			return extractor.extractData(proxyResultSet);
		} catch (SQLException sqle) {
			throw new DataRetrievalFailureException("Could not extract data", sqle);
		}
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(PreparedStatementCreator arg0, RowCallbackHandler arg1) throws DataAccessException {
		proxyJdbcTemplate.query(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.RowMapper)
	 */
	public List query(PreparedStatementCreator arg0, RowMapper arg1) throws DataAccessException {
		return proxyJdbcTemplate.query(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String)
	 */
	public int queryForInt(String arg0) throws DataAccessException {
		return proxyJdbcTemplate.queryForInt(arg0);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String, java.lang.Object[])
	 */
	public int queryForInt(String arg0, Object[] arg1) throws DataAccessException {
		return proxyJdbcTemplate.queryForInt(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String)
	 */
	public List queryForList(String arg0) throws DataAccessException {
		return proxyJdbcTemplate.queryForList(arg0);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String, java.lang.Object[])
	 */
	public List queryForList(String arg0, Object[] arg1) throws DataAccessException {
		return proxyJdbcTemplate.queryForList(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String)
	 */
	public long queryForLong(String arg0) throws DataAccessException {
		return proxyJdbcTemplate.queryForLong(arg0);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String, java.lang.Object[])
	 */
	public long queryForLong(String arg0, Object[] arg1) throws DataAccessException {
		return proxyJdbcTemplate.queryForLong(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Class)
	 */
	public Object queryForObject(String arg0, Class arg1) throws DataAccessException {
		return proxyJdbcTemplate.queryForObject(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Object[], java.lang.Class)
	 */
	public Object queryForObject(String arg0, Object[] arg1, Class arg2) throws DataAccessException {
		return proxyJdbcTemplate.queryForObject(arg0, arg1, arg2);
	}

	/**
	 * @see org.springframework.jdbc.support.JdbcAccessor#setDataSource(javax.sql.DataSource)
	 */
	public void setDataSource(DataSource arg0) {
		proxyJdbcTemplate.setDataSource(arg0);
	}

	/**
	 * @see org.springframework.jdbc.support.JdbcAccessor#setExceptionTranslator(org.springframework.jdbc.support.SQLExceptionTranslator)
	 */
	public void setExceptionTranslator(SQLExceptionTranslator arg0) {
		proxyJdbcTemplate.setExceptionTranslator(arg0);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#setFetchSize(int)
	 */
	public void setFetchSize(int arg0) {
		proxyJdbcTemplate.setFetchSize(arg0);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#setIgnoreWarnings(boolean)
	 */
	public void setIgnoreWarnings(boolean arg0) {
		proxyJdbcTemplate.setIgnoreWarnings(arg0);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#setNativeJdbcExtractor(org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor)
	 */
	public void setNativeJdbcExtractor(NativeJdbcExtractor arg0) {
		proxyJdbcTemplate.setNativeJdbcExtractor(arg0);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return proxyJdbcTemplate.toString();
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String)
	 */
	public int update(String arg0) throws DataAccessException {
		return proxyJdbcTemplate.update(arg0);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, java.lang.Object[])
	 */
	public int update(String arg0, Object[] arg1) throws DataAccessException {
		return proxyJdbcTemplate.update(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, java.lang.Object[], int[])
	 */
	public int update(String arg0, Object[] arg1, int[] arg2) throws DataAccessException {
		return proxyJdbcTemplate.update(arg0, arg1, arg2);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter)
	 */
	public int update(String arg0, PreparedStatementSetter arg1) throws DataAccessException {
		return proxyJdbcTemplate.update(arg0, arg1);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(org.springframework.jdbc.core.PreparedStatementCreator)
	 */
	public int update(PreparedStatementCreator arg0) throws DataAccessException {
		return proxyJdbcTemplate.update(arg0);
	}

	/**
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.support.KeyHolder)
	 */
	public int update(PreparedStatementCreator arg0, KeyHolder arg1) throws DataAccessException {
		return proxyJdbcTemplate.update(arg0, arg1);
	}

	/**
	 * Sets the expected number of times a method should be called. Use the returned value to set the expected method.
	 *
	 * @param invocationMatcher a matcher for setting how many times a method should be called.
	 * @return an instance of NameMatchBuilder to use for setting which method should be called.
	 * @see NameMatchBuilder
	 */
	public NameMatchBuilder expects(InvocationMatcher invocationMatcher) {
		return mockJdbcTemplate.expects(invocationMatcher);
	}

	/**
	 * Verifies that the JdbcTemplate and ResultSet are used correctly.
	 */
	public void verify() {
		mockJdbcTemplate.verify();
		mockResultSet.verify();
	}

	/**
	 * @see BuilderNamespace#lookupID(String)
	 */
	public MatchBuilder lookupID(String arg0) {
		return mockJdbcTemplate.lookupID(arg0);
	}

	/**
	 * @see BuilderNamespace#registerMethodName(String, MatchBuilder)
	 */
	public void registerMethodName(String arg0, MatchBuilder arg1) {
		mockJdbcTemplate.registerMethodName(arg0, arg1);
	}

	/**
	 * @see BuilderNamespace#registerUniqueID(String, MatchBuilder)
	 */
	public void registerUniqueID(String arg0, MatchBuilder arg1) {
		mockJdbcTemplate.registerUniqueID(arg0, arg1);
	}

}
