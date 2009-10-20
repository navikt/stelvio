package no.stelvio.common.context;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ibm.websphere.workarea.NoWorkArea;
import com.ibm.websphere.workarea.NotOriginator;
import com.ibm.websphere.workarea.UserWorkArea;

/**
 * @author test@example.com
 */
public class StelvioContextRepository {
	public static StelvioContext getContext() {
		UserWorkArea workArea = lookupUserWorkArea();
		return new StelvioContext(workArea);
	}

	public static StelvioContext createContext(StelvioContextData contextData) {
		UserWorkArea userWorkArea = lookupUserWorkArea();
		userWorkArea.begin(UserWorkAreaContextAdapter.USER_WORK_AREA_NAME);
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
			if (UserWorkAreaContextAdapter.USER_WORK_AREA_NAME.equals(userWorkArea.getName())) {
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
