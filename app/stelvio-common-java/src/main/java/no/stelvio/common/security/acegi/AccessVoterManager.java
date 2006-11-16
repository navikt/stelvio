package no.stelvio.common.security.acegi;

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.AcegiMessageSource;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;
import org.acegisecurity.vote.*;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AccessVoterManager implements AccessDecisionManager, InitializingBean,
    MessageSourceAware {
    //~ Instance fields ================================================================================================

    private List<AccessDecisionVoter> decisionVoters;
    protected MessageSourceAccessor messages = AcegiMessageSource.getAccessor();
    private boolean allowIfAllAbstainDecisions = false;

    //~ Methods ========================================================================================================

    public void afterPropertiesSet() throws Exception {
        //checkIfValidList(this.decisionVoters);
        Assert.notNull(this.messages, "A message source must be set");
    }

    protected final void checkAllowIfAllAbstainDecisions() {
        if (!this.isAllowIfAllAbstainDecisions()) {
            throw new AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied",
                    "Access is denied"));
        }
    }

    /*private void checkIfValidList(List listToCheck) {
        if ((listToCheck == null) || (listToCheck.size() == 0)) {
            throw new IllegalArgumentException("A list of AccessDecisionVoters is required");
        }
    }*/

	//~ Methods ========================================================================================================
    /**
     * TODO document me!
     * If no voters are specified an <code>AlwaysAffirmativeVoter</code> will be added to the list.
     */
    public void addDecisionVoters(ConfigAttributeDefinition config){
    	
    	this.decisionVoters = new ArrayList<AccessDecisionVoter>();
    	Iterator iterator = config.getConfigAttributes();     
    	ConfigAttribute configAttribute = null;
    	try {
    		 while(iterator.hasNext()){
             	configAttribute = (ConfigAttribute)iterator.next();	
             	Class clazz = Class.forName(configAttribute.getAttribute(), true, Thread.currentThread().getContextClassLoader());
             	System.out.println("configAttribute '" + configAttribute + "'is AccessDecisionVoter: " + isAccessDecisionVoter(clazz));
             	if(isAccessDecisionVoter(clazz)){
             		AccessDecisionVoter voter = (AccessDecisionVoter)clazz.newInstance();
             		if(!this.decisionVoters.contains(voter)){
             			this.decisionVoters.add(voter);
             		}else System.out.println("The list already contain this voter.");
             	}   
             }
    		 if(this.decisionVoters.size() == 0 ){
    			 this.decisionVoters.add(new AlwaysAffirmativeVoter());
    		 }
    		 
		 }catch (ClassNotFoundException ex) {
             throw new IllegalArgumentException("Class '" + configAttribute + "' not found");
         }catch (InstantiationException ine){
        	 throw new IllegalArgumentException("Class '" + configAttribute + "' cannot be instantiated. It is either am interface or abstract class.",ine);
         }catch(IllegalAccessException ile){
        	 throw new IllegalArgumentException("Could not create an instance of class '" + configAttribute + "'",ile);
         }
    }
    public boolean isAccessDecisionVoter(Class clazz){
    	return AccessDecisionVoter.class.isAssignableFrom(clazz) ? true : false;
    }
    
    /**
     * TODO Update these comments
     * This concrete implementation simply polls all configured  {@link AccessDecisionVoter}s and grants access
     * if any <code>AccessDecisionVoter</code> voted affirmatively. Denies access only if there was a deny vote AND no
     * affirmative votes.<p>If every <code>AccessDecisionVoter</code> abstained from voting, the decision will
     * be based on the {@link #isAllowIfAllAbstainDecisions()} property (defaults to false).</p>
     *
     * @param authentication the caller invoking the method
     * @param object the secured object
     * @param config the configuration attributes associated with the method being invoked
     *
     * @throws AccessDeniedException if access is denied
     */
    public void decide(Authentication authentication, Object object, ConfigAttributeDefinition config)
        throws AccessDeniedException,AcegiConfigurationException {
        
    	//populate the provider list with respect to the config attributes
    	addDecisionVoters(config);     
    	Iterator voterIterator = this.getDecisionVoters().iterator();
        System.out.println("------------------ AccessVoterManager -------------------");
        int result = AccessDecisionVoter.ACCESS_ABSTAIN; 
        while (voterIterator.hasNext()) 
        {
           AccessDecisionVoter voter = (AccessDecisionVoter) voterIterator.next();              
           result = voter.vote(authentication, object, config);
        	
           if(result == AccessDecisionVoter.ACCESS_GRANTED){
        	   System.out.println("------------------ End AccessVoterManager -------------------");
    		   return;
    	   }else if(result == AccessDecisionVoter.ACCESS_DENIED){
    		   System.out.println("------------------ End AccessVoterManager -------------------");
    		   throw new AccessDeniedException(messages.getMessage("AccessVoterManager.accessDenied",
               "Access is denied"));
    	   }	   
        }
        // To get this far, every AccessDecisionVoter abstained
        checkAllowIfAllAbstainDecisions();
        System.out.println("------------------ End AccessVoterManager -------------------");
    }
    
    
    
  
   
    public List getDecisionVoters() {
        return this.decisionVoters;
    }

    public boolean isAllowIfAllAbstainDecisions() {
        return allowIfAllAbstainDecisions;
    }

    public void setAllowIfAllAbstainDecisions(boolean allowIfAllAbstainDecisions) {
        this.allowIfAllAbstainDecisions = allowIfAllAbstainDecisions;
    }

   /* public void setDecisionVoters(List newList) {
        checkIfValidList(newList);

        Iterator iter = newList.iterator();

        while (iter.hasNext()) {
            Object currentObject = null;

            try {
                currentObject = iter.next();

                AccessDecisionVoter attemptToCast = (AccessDecisionVoter) currentObject;
            } catch (ClassCastException cce) {
                throw new IllegalArgumentException("AccessDecisionVoter " + currentObject.getClass().getName()
                    + " must implement AccessDecisionVoter");
            }
        }

        this.decisionVoters = newList;
    }*/

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public boolean supports(ConfigAttribute attribute) {
        if(this.decisionVoters != null){
			Iterator iter = this.decisionVoters.iterator();
		
		    while (iter.hasNext()) {
		        AccessDecisionVoter voter = (AccessDecisionVoter) iter.next();
		
		        if (voter.supports(attribute)) {
		            return true;
		        }
		    }
		    return false;
        }else{
        	return true;
        }
    }

    /**
     * Iterates through all <code>AccessDecisionVoter</code>s and ensures each can support the presented class.<p>If
     * one or more voters cannot support the presented class, <code>false</code> is returned.</p>
     *
     * @param clazz DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean supports(Class clazz) {
        if(this.decisionVoters != null){
	    	Iterator iter = this.decisionVoters.iterator();
	
	        while (iter.hasNext()) {
	            AccessDecisionVoter voter = (AccessDecisionVoter) iter.next();
	
	            if (!voter.supports(clazz)) {
	                return false;
	            }
	        }
	        return true;
        }else{
        	return true;
        }
    }   
}
