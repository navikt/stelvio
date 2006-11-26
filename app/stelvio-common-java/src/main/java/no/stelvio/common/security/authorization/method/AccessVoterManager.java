package no.stelvio.common.security.authorization.method;

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.AcegiMessageSource;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
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

/**
 * This class is an implementation of {@link AccessDecisionManager}.
 * <p>Handles configuration of a bean context defined list of  {@link AccessDecisionVoter}s and the access control 
 * behaviour if all  voters abstain from voting (defaults to deny access).</p> 
 * The list of {@link AccessDecisionVoter}s is retrieved from a {@link ConfigAttributeDefinition} 
 * which is derived from a {@link ObjectDefinitionSource}. The {@link ObjectDefinitionSource} is defined in a bean context 
 * and injected into a security interceptor.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AccessVoterManager implements AccessDecisionManager, InitializingBean,
    MessageSourceAware {
  
    private List<AccessDecisionVoter> decisionVoters;
    protected MessageSourceAccessor messages = AcegiMessageSource.getAccessor();
    private boolean allowIfAllAbstainDecisions = false;

    /**
     * {@inheritDoc}
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.messages, "A message source must be set");
    }

    /**
     * Checks if access should be granted if all voters abstain from voting.
     * @throws AccessDeniedException if no access should be granted.
     * 
     */
    public final void checkAllowIfAllAbstainDecisions(Object secureObject) {
        if (!this.isAllowIfAllAbstainDecisions()) {
        	if(secureObject instanceof ReflectiveMethodInvocation){
  			   ReflectiveMethodInvocation invoc = (ReflectiveMethodInvocation)secureObject;
  			   throw new MethodAccessDeniedException(invoc.getMethod());
  		   	}else{
  			   throw new MethodAccessDeniedException("Could not find method.");
  		   	}  
        }
    }

    /**
     * Iterates through the attributes in a ConfigAttributeDefinition and creates a list of 
     * <code>AccessDecisionVoter</code>s based on these. If no voters are found an <code>AlwaysAffirmativeVoter</code> 
     * will be added to the list so that access is always granted to the secure object.
     * @param config the {@link ConfigAttributeDefinition} containing attributes with the full class names of 
     * 				 the voters that should be used on the secure object (e.g. a method invocation).
     * @throws IllegalArgumentException if the attributes do not represent a class.
     */
    public void addDecisionVoters(ConfigAttributeDefinition config){
    	
    	this.decisionVoters = new ArrayList<AccessDecisionVoter>();
    	Iterator iterator = config.getConfigAttributes();     
    	ConfigAttribute configAttribute = null;
    	try {
    		 while(iterator.hasNext()){
             	configAttribute = (ConfigAttribute)iterator.next();	
             	Class clazz = Class.forName(configAttribute.getAttribute(), true, Thread.currentThread().getContextClassLoader());
             	
             	if(isAccessDecisionVoter(clazz)){
             		AccessDecisionVoter voter = (AccessDecisionVoter)clazz.newInstance();
             		if(!this.decisionVoters.contains(voter)){
             			this.decisionVoters.add(voter);
             		} 
             	}   
             }
    		 if(this.decisionVoters.size() == 0 ){
    			 this.decisionVoters.add(new AlwaysAffirmativeVoter());
    		 }
    		 
		 }catch (ClassNotFoundException ex) {
             throw new AccessDecisionVoterNotFoundException(ex,configAttribute.getAttribute()
            		 										, "Class '" + configAttribute + "' not found");
         }catch (InstantiationException ine){
        	 throw new AccessDecisionVoterNotFoundException(ine,configAttribute.getAttribute()
        			 										,"Class '" + configAttribute + "' cannot be instantiated."
        			 										+ "It is either an interface or abstract class.");
         }catch(IllegalAccessException ile){
        	 throw new AccessDecisionVoterNotFoundException(ile,configAttribute.getAttribute()
        			 										,"Could not create an instance of class '" 
        			 										+ configAttribute + "'");
         }
    }
    
    /**
     * Checks if the presented class is an <code>AccessDecisionVoter</code> implementation.
     * @param clazz the class to check
     * @return <code>true</code> if the class is an <code>AccessDecisionVoter</code>.
     */
    public boolean isAccessDecisionVoter(Class clazz){
    	return AccessDecisionVoter.class.isAssignableFrom(clazz) ? true : false;
    }
    
    /**
     * This concrete implementation simply polls all configured  {@link AccessDecisionVoter}s and grants access
     * if an <code>AccessDecisionVoter</code> voted affirmatively or denies access if there is a deny vote. If a voter 
     * abstain from voting the next one in the list will be invoked.<p>If every <code>AccessDecisionVoter</code> abstained from voting, the decision will
     * be based on the {@link #isAllowIfAllAbstainDecisions()} property (defaults to false).</p>
     *
     * @param authentication the caller invoking the method
     * @param object the secured object
     * @param config the configuration attributes associated with the method being invoked
     *
     * @throws MethodAccessDeniedException if access is denied
     */
    public void decide(Authentication authentication, Object object, ConfigAttributeDefinition config)
        throws MethodAccessDeniedException {
        
    	addDecisionVoters(config);     
    	Iterator voterIterator = this.getDecisionVoters().iterator();
        int result = AccessDecisionVoter.ACCESS_ABSTAIN; 
        while (voterIterator.hasNext()) 
        {
           AccessDecisionVoter voter = (AccessDecisionVoter) voterIterator.next();              
           result = voter.vote(authentication, object, config);
        	
           if(result == AccessDecisionVoter.ACCESS_GRANTED){
    		   return;
    	   }else if(result == AccessDecisionVoter.ACCESS_DENIED){
    		   if(object instanceof ReflectiveMethodInvocation){
    			   ReflectiveMethodInvocation invoc = (ReflectiveMethodInvocation)object;
    			   throw new MethodAccessDeniedException(invoc.getMethod());
    		   }else{
    			   throw new MethodAccessDeniedException("Secure object is not a ReflectiveMethodInvocation.");
    		   }
    	   }	   
        }
        // To get this far, every AccessDecisionVoter abstained
        checkAllowIfAllAbstainDecisions(object);
    }
    
    /**
     * Returns the list of <code>AccessDecisionVoter</code>s.
     * @return the voter list.
     */
    public List<AccessDecisionVoter> getDecisionVoters() {
        return this.decisionVoters;
    }

    /**
     * Returns whether or not access should be granted if all voters abstain from voting.
     * @return <code>true</code> if this property is explicitly set, <code>false</code> otherwise. 
     */
    public boolean isAllowIfAllAbstainDecisions() {
        return this.allowIfAllAbstainDecisions;
    }

    /**
     * Sets whether or not access should be granted if all voters abstain from voting.
     * @param allowIfAllAbstainDecisions <code>true</code> if access should be granted, <code>false</code> otherwise.
     */
    public void setAllowIfAllAbstainDecisions(boolean allowIfAllAbstainDecisions) {
        this.allowIfAllAbstainDecisions = allowIfAllAbstainDecisions;
    }
    
    /** 
     * Sets the message source.
     * @see org.springframework.context.MessageSourceAware#setMessageSource(org.springframework.context.MessageSource)
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    /**
     * {@inheritDoc}
     */
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
     * @param clazz the class to check.
     *
     * @return <code>true</code> if all voters support the presented class, <code>false</code> otherwise.
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
