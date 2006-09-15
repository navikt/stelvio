package no.nav.common.framework.test.spring.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.jmock.builder.BuilderNamespace;
import org.jmock.builder.MatchBuilder;
import org.jmock.builder.NameMatchBuilder;
import org.jmock.cglib.Mock;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Verifiable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Mock class mocking HibernateTemplate. Should be used when unit testing classes using a HibernateTemplate so the code
 * typically implemented as anonymous classes for the execute and executeFind methods also will be run when unit
 * testing. When using a regular mock for HibernateTemplate this code will not be run.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2379 $, $Date: 2005-06-24 11:40:20 +0200 (Fri, 24 Jun 2005) $
 * @see HibernateTemplate
 * @see HibernateTemplate#execute(HibernateCallback)
 * @see HibernateTemplate#executeFind(HibernateCallback)
 */
public class MockHibernateTemplate extends HibernateTemplate implements Verifiable, BuilderNamespace {
	private final Mock mockHibernateTemplate = new Mock(HibernateTemplate.class);
	private final HibernateTemplate proxyHibernateTemplate = (HibernateTemplate) mockHibernateTemplate.proxy();
	private final Mock mockSession = new Mock(Session.class);
	private final Session proxySession = (Session) mockSession.proxy();

	/**
	 * Sets the expected number of times a method should be called. Use the returned value to set the expected method.
	 *
	 * @param invocationMatcher a matcher for setting how many times a method should be called.
	 * @return an instance of NameMatchBuilder to use for setting which method should be called.
	 * @see NameMatchBuilder
	 */
	public NameMatchBuilder expects(InvocationMatcher invocationMatcher) {
		return mockHibernateTemplate.expects(invocationMatcher);
	}

	/**
	 * Use this when setting expectations on calls to <code>Session</code> methods.
	 *
	 * @param invocationMatcher a matcher for setting how many times a method should be called.
	 * @return an instance of NameMatchBuilder to use for setting which method should be called.
	 * @see NameMatchBuilder
	 */
	public NameMatchBuilder expectsOnSession(InvocationMatcher invocationMatcher) {
		return mockSession.expects(invocationMatcher);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		proxyHibernateTemplate.setSessionFactory(sessionFactory);
	}

	public SessionFactory getSessionFactory() {
		return proxyHibernateTemplate.getSessionFactory();
	}

	public void setEntityInterceptor(Interceptor entityInterceptor) {
		proxyHibernateTemplate.setEntityInterceptor(entityInterceptor);
	}

	public Interceptor getEntityInterceptor() {
		return proxyHibernateTemplate.getEntityInterceptor();
	}

	public void setJdbcExceptionTranslator(SQLExceptionTranslator jdbcExceptionTranslator) {
		proxyHibernateTemplate.setJdbcExceptionTranslator(jdbcExceptionTranslator);
	}

	public SQLExceptionTranslator getJdbcExceptionTranslator() {
		return proxyHibernateTemplate.getJdbcExceptionTranslator();
	}

	public void setFlushModeName(String constantName) {
		proxyHibernateTemplate.setFlushModeName(constantName);
	}

	public void setFlushMode(int flushMode) {
		proxyHibernateTemplate.setFlushMode(flushMode);
	}

	public int getFlushMode() {
		return proxyHibernateTemplate.getFlushMode();
	}

	public void afterPropertiesSet() {
		proxyHibernateTemplate.afterPropertiesSet();
	}

	public DataAccessException convertHibernateAccessException(HibernateException ex) {
		return proxyHibernateTemplate.convertHibernateAccessException(ex);
	}

	public void setAllowCreate(boolean allowCreate) {
		proxyHibernateTemplate.setAllowCreate(allowCreate);
	}

	public boolean isAllowCreate() {
		return proxyHibernateTemplate.isAllowCreate();
	}

	public void setCacheQueries(boolean cacheQueries) {
		proxyHibernateTemplate.setCacheQueries(cacheQueries);
	}

	public boolean isCacheQueries() {
		return proxyHibernateTemplate.isCacheQueries();
	}

	public void setCheckWriteOperations(boolean checkWriteOperations) {
		proxyHibernateTemplate.setCheckWriteOperations(checkWriteOperations);
	}

	public boolean isCheckWriteOperations() {
		return proxyHibernateTemplate.isCheckWriteOperations();
	}

	public Object execute(HibernateCallback action) throws DataAccessException {
		return commonExecute(action);
	}

	public List executeFind(HibernateCallback action) throws DataAccessException {
		return (List) commonExecute(action);
	}

	/**
	 * Common code for running a HibernateCallback action that will handle the exception like it is done in
	 * HibernateTemplate. Is used from the execute and executeFind methods.
	 *
	 * @param action the HibernateCallback action to run with the provided mock session.
	 * @return whatever is returned from the HibernateCallback action.
	 * @see HibernateCallback
	 * @see HibernateTemplate#execute(HibernateCallback)
	 * @see HibernateTemplate#executeFind(HibernateCallback)
	 */
	private Object commonExecute(HibernateCallback action) {
		try {
			return action.doInHibernate(proxySession);
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		} catch (SQLException ex) {
			throw convertJdbcAccessException(ex);
		} catch (RuntimeException ex) {
			// callback code threw application exception
			throw ex;
		}
	}

	public Object get(Class entityClass, Serializable id) throws DataAccessException {
		return proxyHibernateTemplate.get(entityClass, id);
	}

	public Object get(Class entityClass, Serializable id, LockMode lockMode) throws DataAccessException {
		return proxyHibernateTemplate.get(entityClass, id, lockMode);
	}

	public Object load(Class entityClass, Serializable id) throws DataAccessException {
		return proxyHibernateTemplate.load(entityClass, id);
	}

	public Object load(Class entityClass, Serializable id, LockMode lockMode) throws DataAccessException {
		return proxyHibernateTemplate.load(entityClass, id, lockMode);
	}

	public List loadAll(Class entityClass) throws DataAccessException {
		return proxyHibernateTemplate.loadAll(entityClass);
	}

	public void refresh(Object entity) throws DataAccessException {
		proxyHibernateTemplate.refresh(entity);
	}

	public void refresh(Object entity, LockMode lockMode) throws DataAccessException {
		proxyHibernateTemplate.refresh(entity, lockMode);
	}

	public boolean contains(Object entity) throws DataAccessException {
		return proxyHibernateTemplate.contains(entity);
	}

	public void evict(Object entity) throws DataAccessException {
		proxyHibernateTemplate.evict(entity);
	}

	public void lock(Object entity, LockMode lockMode) throws DataAccessException {
		proxyHibernateTemplate.lock(entity, lockMode);
	}

	public Serializable save(Object entity) throws DataAccessException {
		return proxyHibernateTemplate.save(entity);
	}

	public void save(Object entity, Serializable id) throws DataAccessException {
		proxyHibernateTemplate.save(entity, id);
	}

	public void saveOrUpdate(Object entity) throws DataAccessException {
		proxyHibernateTemplate.saveOrUpdate(entity);
	}

	public void update(Object entity) throws DataAccessException {
		proxyHibernateTemplate.update(entity);
	}

	public void update(Object entity, LockMode lockMode) throws DataAccessException {
		proxyHibernateTemplate.update(entity, lockMode);
	}

	public void delete(Object entity) throws DataAccessException {
		proxyHibernateTemplate.delete(entity);
	}

	public void delete(Object entity, LockMode lockMode) throws DataAccessException {
		proxyHibernateTemplate.delete(entity, lockMode);
	}

	public void deleteAll(Collection entities) throws DataAccessException {
		proxyHibernateTemplate.deleteAll(entities);
	}

	public void flush() throws DataAccessException {
		proxyHibernateTemplate.flush();
	}

	public void clear() throws DataAccessException {
		proxyHibernateTemplate.clear();
	}

	public List find(String queryString) throws DataAccessException {
		return proxyHibernateTemplate.find(queryString);
	}

	public List find(String queryString, Object value) throws DataAccessException {
		return proxyHibernateTemplate.find(queryString, value);
	}

	public List find(String queryString, Object[] values) throws DataAccessException {
		return proxyHibernateTemplate.find(queryString, values);
	}

	public List findByNamedParam(String queryString, String paramName, Object value) throws DataAccessException {
		return proxyHibernateTemplate.findByNamedParam(queryString, paramName, value);
	}

	public List findByNamedParam(String queryString, String[] paramNames, Object[] values) throws DataAccessException {
		return proxyHibernateTemplate.findByNamedParam(queryString, paramNames, values);
	}

	public List findByValueBean(String queryString, Object valueBean) throws DataAccessException {
		return proxyHibernateTemplate.findByValueBean(queryString, valueBean);
	}

	public List findByNamedQuery(String queryName) throws DataAccessException {
		return proxyHibernateTemplate.findByNamedQuery(queryName);
	}

	public List findByNamedQuery(String queryName, Object value) throws DataAccessException {
		return proxyHibernateTemplate.findByNamedQuery(queryName, value);
	}

	public List findByNamedQuery(String queryName, Object[] values) throws DataAccessException {
		return proxyHibernateTemplate.findByNamedQuery(queryName, values);
	}

	public List findByNamedQueryAndNamedParam(String queryName, String paramName, Object value)
			throws DataAccessException {
		return proxyHibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramName, value);
	}

	public List findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values)
			throws DataAccessException {
		return proxyHibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramNames, values);
	}

	public List findByNamedQueryAndValueBean(String queryName, Object valueBean) throws DataAccessException {
		return proxyHibernateTemplate.findByNamedQueryAndValueBean(queryName, valueBean);
	}

	public Iterator iterate(String queryString) throws DataAccessException {
		return proxyHibernateTemplate.iterate(queryString);
	}

	public void setAlwaysUseNewSession(final boolean alwaysUseNewSession) {
		proxyHibernateTemplate.setAlwaysUseNewSession(alwaysUseNewSession);
	}

	public boolean isAlwaysUseNewSession() {
		return proxyHibernateTemplate.isAlwaysUseNewSession();
	}

	public void setExposeNativeSession(final boolean exposeNativeSession) {
		proxyHibernateTemplate.setExposeNativeSession(exposeNativeSession);
	}

	public boolean isExposeNativeSession() {
		return proxyHibernateTemplate.isExposeNativeSession();
	}

	public void setQueryCacheRegion(final String queryCacheRegion) {
		proxyHibernateTemplate.setQueryCacheRegion(queryCacheRegion);
	}

	public String getQueryCacheRegion() {
		return proxyHibernateTemplate.getQueryCacheRegion();
	}

	public Object execute(final HibernateCallback action, final boolean exposeNativeSession) throws DataAccessException
	{
		return proxyHibernateTemplate.execute(action, exposeNativeSession);
	}

	public Object get(final String entityName, final Serializable id) throws DataAccessException {
		return proxyHibernateTemplate.get(entityName, id);
	}

	public Object get(final String entityName, final Serializable id, final LockMode lockMode)
			throws DataAccessException {
		return proxyHibernateTemplate.get(entityName, id, lockMode);
	}

	public Object load(final String entityName, final Serializable id) throws DataAccessException {
		return proxyHibernateTemplate.load(entityName, id);
	}

	public Object load(final String entityName, final Serializable id, final LockMode lockMode)
			throws DataAccessException {
		return proxyHibernateTemplate.load(entityName, id, lockMode);
	}

	public void load(final Object entity, final Serializable id) throws DataAccessException {
		proxyHibernateTemplate.load(entity, id);
	}

	public void initialize(final Object proxy) throws DataAccessException {
		proxyHibernateTemplate.initialize(proxy);
	}

	public void lock(final String entityName, final Object entity, final LockMode lockMode) throws DataAccessException {
		proxyHibernateTemplate.lock(entityName, entity, lockMode);
	}

	public Serializable save(final String entityName, final Object entity) throws DataAccessException {
		return proxyHibernateTemplate.save(entityName, entity);
	}

	public void save(final String entityName, final Object entity, final Serializable id) throws DataAccessException {
		proxyHibernateTemplate.save(entityName, entity, id);
	}

	public void update(final String entityName, final Object entity) throws DataAccessException {
		proxyHibernateTemplate.update(entityName, entity);
	}

	public void update(final String entityName, final Object entity, final LockMode lockMode) throws DataAccessException
	{
		proxyHibernateTemplate.update(entityName, entity, lockMode);
	}

	public void saveOrUpdate(final String entityName, final Object entity) throws DataAccessException {
		proxyHibernateTemplate.saveOrUpdate(entityName, entity);
	}

	public void saveOrUpdateAll(final Collection entities) throws DataAccessException {
		proxyHibernateTemplate.saveOrUpdateAll(entities);
	}

	public void persist(final Object entity) throws DataAccessException {
		proxyHibernateTemplate.persist(entity);
	}

	public void persist(final String entityName, final Object entity) throws DataAccessException {
		proxyHibernateTemplate.persist(entityName, entity);
	}

	public Object merge(final Object entity) throws DataAccessException {
		return proxyHibernateTemplate.merge(entity);
	}

	public Object merge(final String entityName, final Object entity) throws DataAccessException {
		return proxyHibernateTemplate.merge(entityName, entity);
	}

	public Iterator iterate(final String queryString, final Object value) throws DataAccessException {
		return proxyHibernateTemplate.iterate(queryString, value);
	}

	public Iterator iterate(final String queryString, final Object[] values) throws DataAccessException {
		return proxyHibernateTemplate.iterate(queryString, values);
	}

	public void closeIterator(final Iterator it) throws DataAccessException {
		proxyHibernateTemplate.closeIterator(it);
	}

	public void setFilterName(final String filter) {
		proxyHibernateTemplate.setFilterName(filter);
	}

	public void setFilterNames(final String[] filterNames) {
		proxyHibernateTemplate.setFilterNames(filterNames);
	}

	public String[] getFilterNames() {
		return proxyHibernateTemplate.getFilterNames();
	}

	public MatchBuilder lookupID(String id) {
		return mockHibernateTemplate.lookupID(id);
	}

	public void registerMethodName(String id, MatchBuilder invocation) {
		mockHibernateTemplate.registerMethodName(id, invocation);
	}

	public void registerUniqueID(String id, MatchBuilder invocation) {
		mockHibernateTemplate.registerUniqueID(id, invocation);
	}

	/** Verifies that the HibernateTemplate and Session is used correctly. */
	public void verify() {
		mockHibernateTemplate.verify();
		mockSession.verify();
	}
}
