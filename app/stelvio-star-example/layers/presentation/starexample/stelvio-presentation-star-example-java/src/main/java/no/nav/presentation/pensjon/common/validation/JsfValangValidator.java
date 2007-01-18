package no.nav.presentation.pensjon.common.validation;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springmodules.validation.valang.ValangValidator;

/**
 * Custom property editor for ValangValidator. 
 * 
 * @author persond3cb2ee15f42, Accenture
 * @author personb66fa0b5ff6e, Accenture
 */

public class JsfValangValidator extends ValangValidator implements MessageSourceAware
{
	
    //private Map customFunctions = null;

	//MessageSource that holds the internationalized messages
    private MessageSource messageSource;
    
    /**
     * Sets the messageSource for use in this class.
     * @param messageSource the messageSource to set
     */
    public void setMessageSource(MessageSource messageSource){
    	if( this.messageSource == null )
    		this.messageSource = messageSource;
    	System.out.println( "messageSource: " + messageSource );
    	System.out.println( "ny" );
    }
        
    /**
     * Method that is called when a validation for an object results 
     * in an error that requires that the user is notified.
     * 
     * @param target the object the valdidations has been performed for. 
     * @param errors the errors that the validation of the object resulted in.
     */
	public void validate(Object target, Errors errors) {
		super.validate(target, errors);
		
		//List feil = errors.getFieldErrors();
		List feil = errors.getAllErrors();

        for (Iterator iter = feil.iterator(); iter.hasNext();) {
        	FieldError fieldError = (FieldError)iter.next();
        	
			String fieldName = fieldError.getField();

			FacesContext context = FacesContext.getCurrentInstance();
			List components = context.getViewRoot().getChildren();
            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
                       
			//Retrieves an internationalized message from the messageSource
			String message = this.messageSource.getMessage(fieldError.getCode(), fieldError.getArguments(), fieldError.getDefaultMessage(), locale);
			
			for (Iterator iter2 = components.iterator(); iter2.hasNext();) {
				UIComponent component = (UIComponent)iter2.next();
				
				List children = component.getChildren();
				
				for (Iterator iter3 = children.iterator(); iter3.hasNext();) {
					UIComponent field = (UIComponent) iter3.next();
					String clientid2 = field.getClientId( context );
							
					if( fieldName.equals( field.getId() ))
					{
						FacesMessage msg = new FacesMessage( message, message );
						context.addMessage( clientid2, msg);
					}
				}	
			}	
        }	
	}
}