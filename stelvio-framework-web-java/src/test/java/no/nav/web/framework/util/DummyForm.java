package no.nav.web.framework.util;

import org.apache.struts.action.ActionForm;

/**
 * Dummy form used with ReturnUtilTest Unittest.
 *
 * @author Jonas Lindholm
 * @version $Revision: 493 $ $Author: jla2920 $ $Date: 2004-06-08 10:15:26 +0200 (Tue, 08 Jun 2004) $
 */
public class DummyForm extends ActionForm {
	private String firstName;
	private String lastName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		firstName = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String name) {
		lastName = name;
	}
}
