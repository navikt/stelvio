package no.stelvio.presentation.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.support.ErrorDefinition;

/**
 * Action class for error pages. Retrieves the exception that has occured from the 
 * current servlet request object and offers methods to display the information
 * from the exception in an error page
 * 
 * @author person6045563b8dec
 * @version $Id$
 *
 */
public class ErrorAction {
	private ErrorDefinitionResolver errorDefinitionResolver;
	private ErrorDefinition errorDefinition;

	private Throwable throwable;

	/**
     * Initializes the action class. The exception that has occured i retrieved from the
     * current http request object.
     *
     */
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		Object exceptionAttribute = request.getAttribute("javax.servlet.error.exception");
		if(exceptionAttribute instanceof Throwable) {
			throwable = (Throwable) exceptionAttribute;
		}

		errorDefinition = errorDefinitionResolver.resolve(throwable);
	}

	/**
     * Returns the error id of the error that has occured
     * @return the error id
     */
	public String getErrorId() {	
		return errorDefinition.getId();
	}

	/**
     * Returns the short description of the exception that has occured
     * 
     * @return short description 
     */
	public String getShortDescription() {
		errorDefinition.getLongDescription();
		errorDefinition.getMessage();
		errorDefinition.getSeverity();
		return errorDefinition.getShortDescription();
	}


	/**
     * Returns the message of the exception that has occured
     * 
     * @return the message of the exception
     */
	public String getMessage() {
		return errorDefinition.getMessage();
	}

	/**
	 * Returns the long description of the exception that has
	 * occured
	 * @return long description
	 */
	public String getLongDescription() {
		return errorDefinition.getLongDescription(); 
	}

	
	/**
	 * Returns the stack trace of the exception that has occured
	 * as a String
	 * 
	 * @return the stack trace of the exception
	 */
	public String getStackTrace() {
		
		StringWriter writer = new StringWriter();
		throwable.printStackTrace(new PrintWriter((Writer) writer));
		return writer.toString();
	}

	/**
	 * Sets the error definition resolver object
	 * 
	 * @param errorDefinitionResolver the object to resolve the error
	 */
	public void setErrorDefinitionResolver(
			ErrorDefinitionResolver errorDefinitionResolver) {
		this.errorDefinitionResolver = errorDefinitionResolver;
	}

}
