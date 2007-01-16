package no.stelvio.common.transferobject.support;

import java.util.Locale;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.transferobject.ContextContainer;

/**
 * Contains information to be stored on the {@link RequestContext}.
 * 
 * @author personff564022aedd
 * @deprecated Use {@link RequestContext}.
 * @version $Id$
 */
public class DefaultContextContainer implements ContextContainer {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 3882317926393219867L;

	private Locale locale;

	private String moduleId;

	private String processId;

	private String screenId;

	private String transactionId;

	private String userId;

	/**
	 * {@inheritDoc ContextContainer#getLocale()()}
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * {@inheritDoc ContextContainer#getModuleId()()()}
	 */
	public String getModuleId() {
		return moduleId;
	}

	/**
	 * {@inheritDoc ContextContainer#getProcessId())()}
	 */

	public String getProcessId() {
		return processId;
	}

	/**
	 * {@inheritDoc ContextContainer#getScreenId())()}
	 */

	public String getScreenId() {
		return screenId;
	}

	/**
	 * {@inheritDoc ContextContainer#getTransactionId()()()}
	 */

	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * {@inheritDoc ContextContainer#getUserId()()()}
	 */

	public String getUserId() {
		return userId;
	}

	/**
	 * {@inheritDoc ContextContainer#setLocale(Locale)()}
	 */

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * {@inheritDoc ContextContainer#setModuleId(String)()()}
	 */

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * {@inheritDoc ContextContainer#setProcessId(String)()()}
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * {@inheritDoc ContextContainer#setScreenId(String)()()}
	 */
	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	/**
	 * {@inheritDoc ContextContainer#setTransactionId(String)()()}
	 */
	public void setTransactionId(String txId) {
		this.transactionId = txId;
	}

	/**
	 * {@inheritDoc ContextContainer#setUserId(String)()()}
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
