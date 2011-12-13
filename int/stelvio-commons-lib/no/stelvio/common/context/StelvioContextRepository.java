package no.stelvio.common.context;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import no.stelvio.common.util.SCAUtils;

import com.ibm.websphere.workarea.NoWorkArea;
import com.ibm.websphere.workarea.NotOriginator;
import com.ibm.websphere.workarea.UserWorkArea;

/**
 * @author test@example.com
 */
public class StelvioContextRepository {
	
	private final static String className = StelvioContextRepository.class.getName();
	private final static Logger log = Logger.getLogger(className);

	public static StelvioContext getContext() {
		UserWorkArea workArea = lookupUserWorkArea();
		return new StelvioContext(workArea);
	}

	public static StelvioContext createOrUpdateContext(StelvioContextData contextData) {
		String workAreaName = UserWorkAreaContextAdapter.USER_WORK_AREA_NAME;
		Long threadId = Thread.currentThread().getId();
		String threadName = Thread.currentThread().getName();
		String moduleName = SCAUtils.getModuleName();
		workAreaName = workAreaName.concat("_"+threadName+"_"+threadId+"_"+moduleName);
				
		UserWorkArea userWorkArea = lookupUserWorkArea();
		userWorkArea.begin(workAreaName);
						
		log.logp(Level.FINE, className, "createOrUpdateContext()", "- userWorkArea.begin: workAreaName="+workAreaName);
		
		UserWorkAreaContextAdapter userWorkAreaContextAdapter = new UserWorkAreaContextAdapter(userWorkArea);
		userWorkAreaContextAdapter.setApplicationId(contextData.getApplicationId());
		userWorkAreaContextAdapter.setCorrelationId(contextData.getCorrelationId());
		userWorkAreaContextAdapter.setLanguageId(contextData.getLanguageId());
		userWorkAreaContextAdapter.setUserId(contextData.getUserId());
		return new StelvioContext(userWorkArea);
	}

	public static void removeContext() {
		try {
			UserWorkArea userWorkArea = lookupUserWorkArea();
			if (userWorkArea.getName()!=null && (userWorkArea.getName()).startsWith(UserWorkAreaContextAdapter.USER_WORK_AREA_NAME)) {
				userWorkArea.complete();
			}
		} catch (NoWorkArea e) {
			throw new RuntimeException(e);
		} catch (NotOriginator e) {
			throw new RuntimeException(e);
		}
	}

	private static UserWorkArea lookupUserWorkArea() {
		try {
			InitialContext initialContext = new InitialContext();
			UserWorkArea workArea = (UserWorkArea) initialContext.lookup("java:comp/websphere/UserWorkArea");
			return workArea;
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
}
