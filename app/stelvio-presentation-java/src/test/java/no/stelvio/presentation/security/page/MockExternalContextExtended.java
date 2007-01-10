package no.stelvio.presentation.security.page;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shale.test.mock.MockExternalContext;


public class MockExternalContextExtended extends MockExternalContext {

	/**
     * <p>Construct a wrapper instance.</p>
     *
     * @param context <code>ServletContext</code> for this application
     * @param request <code>HttpServetRequest</code> for this request
     * @param response <code>HttpServletResponse</code> for this request
     */
    public MockExternalContextExtended(ServletContext context,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        super(context,request,response);
    }
    
    public void redirect(String requestURI)
    throws IOException {
    	
    	((HttpServletResponse)this.getResponse()).sendRedirect(requestURI);
    }
   
    
}
