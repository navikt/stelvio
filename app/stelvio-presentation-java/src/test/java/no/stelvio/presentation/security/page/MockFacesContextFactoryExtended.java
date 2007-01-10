package no.stelvio.presentation.security.page;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shale.test.mock.MockExternalContext;
import org.apache.shale.test.mock.MockFacesContext;
import org.apache.shale.test.mock.MockFacesContextFactory;

public class MockFacesContextFactoryExtended extends MockFacesContextFactory {

	/** {@inheritDoc} */
	@Override
    public FacesContext getFacesContext(Object context, Object request,
                                        Object response,
                                        Lifecycle lifecycle) throws FacesException {

        MockExternalContextExtended externalContext =
          new MockExternalContextExtended((ServletContext) context,
                                  (HttpServletRequest) request,
                                  (HttpServletResponse) response);
        return new MockFacesContext(externalContext, lifecycle);

    }
}
