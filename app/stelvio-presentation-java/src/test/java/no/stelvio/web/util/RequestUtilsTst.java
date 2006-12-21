package no.stelvio.web.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.eq;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

/**
 * Description
 *
 * @author Jonas Lindholm
 * @version $Revision: 2348 $ $Author: psa2920 $ $Date: 2005-06-21 16:26:50 +0200 (Tue, 21 Jun 2005) $
 */
public class RequestUtilsTst {
	private final MockServletContext context = new MockServletContext();
	private final MockHttpServletRequest request = new MockHttpServletRequest(context);

	/** Test with two execute request parameters */
//    @Test
    public void getProcessId() {
		addRequestParameter("para1", "value1");
		addRequestParameter("para2", "value2");
		addRequestParameter("executeWindow1", "executeValue1");
		addRequestParameter("executeWindow2", "executeValue2");

		String processId = RequestUtils.getProcessId(request);

		assertThat(processId, eq("Window2"));
	}

	/** Test for a imagebutton request */
//    @Test
	public void getProcessId2() {
		addRequestParameter("para1", "value1");
		addRequestParameter("para2", "value2");
		addRequestParameter("executeSok.y", "nada");

		String processId = RequestUtils.getProcessId(request);

        assertThat(processId, eq("Sok"));
	}

	/** Test for a nested button */
//    @Test
	public void getProcessId3() {
		addRequestParameter("para1", "value1");
		addRequestParameter("para2", "value2");
		addRequestParameter("executeVelg[1]", "nada");

		String processId = RequestUtils.getProcessId(request);

        assertThat(processId, eq("Velg"));
	}

	/** Test default method */
//    @Test
	public void getProcessId4() {
		String processId = RequestUtils.getProcessId(request);

        assertThat(processId, eq("Load"));
	}

//    @Test
	public void getMethodNumber() {
		addRequestParameter("para1", "value1");
		addRequestParameter("para2", "value2");
		//addRequestParameter("executeWindow1", "executeValue1");
		//addRequestParameter("executeWindow2", "executeValue2");
		//addRequestParameter("person[0].executeVelg", "nada");
		addRequestParameter("executeVelg[1]", "nada");

		Integer methodNumber = RequestUtils.getMethodNumber(request);

        assertThat(methodNumber, eq(1));
	}

//    @Test
	public void getMethod() {
		String processId = RequestUtils.getProcessId(request);
        assertThat(processId, eq("Load"));
	}

    private void addRequestParameter(String key, String value) {
		request.addParameter(key, value);
	}
}
