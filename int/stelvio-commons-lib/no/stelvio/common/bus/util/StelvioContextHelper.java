package no.stelvio.common.bus.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import no.stelvio.common.context.StelvioContext;
import no.stelvio.common.context.StelvioContextRepository;

/**
 * <p>
 * This is a utility class that provides helper methods around StelvioContext, useful for different purposes, such as getting a
 * userId, the WBISessionId or other funtionality around StelvioContext
 * </p>
 * 
 * @usage
 * <p>
 * no.stelvio.common.bus.util.StelvioContextHelper stelvioCtx = new StelvioContextHelper(); String myUserId =
 * stelvioCtx.getUserId();
 * </p>
 * 
 * @author persona2c5e3b49756 Schnell, test@example.com
 * @author test@example.com
 * 
 */
public class StelvioContextHelper {
	private final static String className = StelvioContextHelper.class.getName();
	private final Logger log = Logger.getLogger(className);

	private StelvioContext context;

	/**
	 * <p>
	 * Constructor
	 * </p>
	 * 
	 */
	public StelvioContextHelper() {
		context = StelvioContextRepository.getContext();
	}

	public String getApplicationId() {
		return context.getApplicationId();
	}

	public String getCorrelationId() {
		return context.getCorrelationId();
	}

	public String getLanguageId() {
		return context.getLanguageId();
	}

	public String getNavUserId() {
		return context.getNavUserId();
	}

	public String getUserId() {
		return context.getUserId();
	}

	/**
	 * <p>
	 * Pretty print the Stelvio workArea context.
	 * </p>
	 * 
	 * @param sModulIdent
	 *            for logging
	 * 
	 * @return void
	 */
	public void printStelvioContext(String sModulIdent) {
		log.logp(Level.FINE, className, "printStelvioContext()", "-- " + sModulIdent);
		log.logp(Level.FINE, className, "printStelvioContext()", "--- userId:" + getUserId());
		log.logp(Level.FINE, className, "printStelvioContext()", "--- navUserId:" + getNavUserId());
		log.logp(Level.FINE, className, "printStelvioContext()", "--- correlationId:" + getCorrelationId());
		log.logp(Level.FINE, className, "printStelvioContext()", "--- languageId:" + getLanguageId());
		log.logp(Level.FINE, className, "printStelvioContext()", "--- applicationId:" + getApplicationId());
		log.logp(Level.FINE, className, "printStelvioContext()", "-- " + sModulIdent);
	}
}
