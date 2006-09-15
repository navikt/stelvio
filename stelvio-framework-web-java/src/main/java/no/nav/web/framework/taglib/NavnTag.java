package no.nav.web.framework.taglib;

import javax.servlet.jsp.JspException;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.taglibs.standard.tag.common.core.NullAttributeException;
import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

/**
 * Tag to show special codes for dead or protected persons.
 *
 * @author Jonas Lindholm, Accenture
 * @version $Id: NavnTag.java 2552 2005-10-14 10:14:21Z fda2920 $
 */
public class NavnTag extends OutSupport {
	/** Logger to be used for writing <i>TRACE</i> and <i>DEBUG</i> messages. */
	private static final Log log = LogFactory.getLog(NavnTag.class);
	private static final String NAVN = "navn";

	private String defaultValue; // stores EL-based property
	private String dead;
	private String protect;
	private String maxlength;
	private Boolean isDead;
	private Boolean isProtect;
	private Integer maxLength;

	/** Constructs a default instance of NavnTag. */
	public NavnTag() {
		super();
		init();
	}

	/**
	 * Evaluates expression and chains to parent.
	 * 
	 * @return the status.
	 * @throws JspException if expression evaluation fails.
	 */
	public int doStartTag() throws JspException {
		// evaluate any expressions we were passed, once per invocation
		evaluateExpressions();

		// chain to the parent implementation
		return super.doStartTag();
	}

	/** Releases any resources we may have (or inherit). */
	public void release() {
		super.release();
		init();
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the value.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Sets the default value.
	 * 
	 * @param defaultValue the default value.
	 */
	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Sets if the person is dead or not.
	 * 
	 * @param dead the dead value.
	 */
	public void setDead(String dead) {
		this.dead = dead;
	}

	/**
	 * Sets if the person's details should be protected or not.
	 * 
	 * @param protect the protected value.
	 */
	public void setProtect(String protect) {
		this.protect = protect;
	}

	/**
	 * Sets the maximum name length. 
	 * 
	 * @param maxlength the maximum length.
	 */
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	/** (re)initializes state (during release() or construction). */
	private void init() {
		value = null;
		defaultValue = null;
		dead = null;
		protect = null;
		maxlength = null;
	}

	/**
	 * Evaluates expressions as necessary
	 * 
	 * @throws JspException iv expression evaluation fails.
	 */
	private void evaluateExpressions() throws JspException {
		String name;

		try {
			name = ((String) ExpressionUtil.evalNotNull(NAVN, "value", (String) value, String.class, this, pageContext)).trim();
		} catch (NullAttributeException ex) {
			// explicitly allow 'null' for value
			name = null;
		}

		try {
			isDead = (Boolean) ExpressionUtil.evalNotNull(NAVN, "dead", dead, java.lang.Boolean.class, this, pageContext);
		} catch (NullAttributeException ex) {
			isDead = Boolean.FALSE;
		}

		try {
			isProtect =
				(Boolean) ExpressionUtil.evalNotNull(NAVN, "protect", protect, java.lang.Boolean.class, this, pageContext);
		} catch (NullAttributeException ex) {
			isProtect = Boolean.FALSE;
		}

		try {
			def = (String) ExpressionUtil.evalNotNull(NAVN, "default", defaultValue, String.class, this, pageContext);
		} catch (NullAttributeException ex) {
			// explicitly allow 'null' for default
			def = null;
		}

		try {
			maxLength = (Integer) ExpressionUtil.evalNotNull(NAVN, "maxlength", maxlength, Integer.class, this, pageContext);
		} catch (NullAttributeException ex) {
			// explicitly allow 'null' for maxlength
			maxLength = null;
		}

		// Can't write escape characters for the value. The whole expresssion below would be visible in html then.
		escapeXml = false;

		// Add special sign for dead or protected person
		value = "";

		try {
			if (null != name) {
				// If dead and protected show both signs in the dead colour 
				if (BooleanUtils.toBoolean(isDead) && BooleanUtils.toBoolean(isProtect)) {
					value = "<span class='doed'>&#134;&nbsp;*&nbsp;";
				} else if (BooleanUtils.toBoolean(isDead)) {
					value = "<span class='doed'>&#134;&nbsp;";
				} else if (BooleanUtils.toBoolean(isProtect)) {
					value = "<span class='disk'>*&nbsp;";
				}

				if (null != maxlength) {
					name = StringUtils.left(name, maxLength.intValue());
				}

				value = value + name;

				if (BooleanUtils.toBoolean(isDead) || BooleanUtils.toBoolean(isProtect)) {
					value = value + "</span>";
				}
			}
		} catch (Exception ex) {
			// Print the name in any case. 
			value = name;

			if (log.isWarnEnabled()) {
				log.warn("evaluateExpressions - exception:" + ex.getStackTrace());
			}
		}
	}
}