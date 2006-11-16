package no.stelvio.common.security.acegi;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.*;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.util.Iterator;

public class EnhetsVoter  implements AccessDecisionVoter {
    //~ Instance fields ================================================================================================

    private String rolePrefix = "ROLE_";
    
    private String inputParameter1;

    //~ Methods ========================================================================================================

    public String getRolePrefix() {
        return rolePrefix;
    }

    /**
     * Allows the default role prefix of <code>ROLE_</code> to be overriden. May be set to an empty value,
     * although this is usually not desireable.
     *
     * @param rolePrefix the new prefix
     */
    public void setRolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
    }

    public boolean supports(ConfigAttribute attribute) {
        
    	return true;
    	/*
    	if ((attribute.getAttribute() != null) && attribute.getAttribute().startsWith(getRolePrefix())) {
            return true;
        } else {
            return false;
        }*/
    }

    /**
     * This implementation supports any type of class, because it does not query the presented secure object.
     *
     * @param clazz the secure object
     *
     * @return always <code>true</code>
     */
    public boolean supports(Class clazz) {
        return true;
    }

    public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
        
    	
    	//-------------------------------------------------------------------
    	// HER MÅ DET TYPISK SJEKKES MOT BRUKERKONTEKSTEN ISTEDENFOR AUTHENTICATION OBJEKTET.
    	// INFO DET SJEKKES MOT HENTES TYPISK FRA EN DATABASE. IKKE SJEKKE MOT CONFIGATRIBUTTENE I DET HELE TATT
    	//-------------------------------------------------------------------
//    	System.out.println("Objektet er av type: " + object.getClass().getName());
        ReflectiveMethodInvocation method = (ReflectiveMethodInvocation)object;
        //System.out.println("Metoden er:" + method.getMethod());
        
        Object[] args = method.getArguments();

        /* for(int i=0;i<args.length;i++)
        {
        	System.out.println("Argument er av type:" + args[i].getClass().getName());
        	System.out.println("Verdien er:" + args[i].toString());
        }*/
    	
    	
    	
    	int result = ACCESS_ABSTAIN;
        Iterator iter = config.getConfigAttributes();
        System.out.println("********************Inne i Enhetsvoter!");
       // System.out.println("Objektet er av type: " + object.getClass().toString());
        
       while (iter.hasNext()) 
       {
            ConfigAttribute attribute = (ConfigAttribute) iter.next();
            int index = assignableToInt(attribute.getAttribute());
            if(index > -1 && index < args.length)
            {
            	System.out.println("- Argumentet er en int.... " + attribute.getAttribute());
            	inputParameter1 = (String)args[index];
            	
            }
           
        }
       //System.out.println("- Enhetsvoter, parameter fra metode: " + inputParameter1);
        return ACCESS_GRANTED;
        //return result;
    }
    
    private int assignableToInt(String str)
    {
    	try
    	{
    		return Integer.parseInt(str);
    	}catch(Exception e)
    	{
    		return -1;
    	}
    	
    	
    }
    
    
}


