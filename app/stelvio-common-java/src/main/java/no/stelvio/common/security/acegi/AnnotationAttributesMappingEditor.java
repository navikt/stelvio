package no.stelvio.common.security.acegi;

import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.SecurityConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.*;

/**
 * Property editor to assist with the setup of a {@link AnnotationAttributesMapping}.<p>The class creates and populates
 * a {@link AnnotationAttributesMapping}.</p>
 *
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */

public class AnnotationAttributesMappingEditor extends PropertyEditorSupport {

    private static final Log logger = LogFactory.getLog(AnnotationAttributesMappingEditor.class);


    /** 
     * Creates and populates a {@link AnnotationAttributesMapping} using a string in the following format:
     * <p>Id1 = package.subpackage.maybeanother.ClassName , package.subpackage.maybeanother.ClassName2, ....</p>
     * <p>Id2 = package.subpackage.maybeanother.ClassName2, package.subpackage.maybeanother.ClassName3, ....</p>
     * <p>.... = ....</p>
     * where the text before the '=' sign is considered as the key and the commaseparated list as a collection of ConfigAttributes.
     * <p></p>
     * @param s the string to parse.
     * @throws IllegalArgumentException if the value of a property is not in the correct format, i.e. string should be
     * a commaseparated list of possible full class name representations. 
     * {@inheritDoc}
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    public void setAsText(String s) throws IllegalArgumentException {
    	AnnotationAttributesMapping source = new AnnotationAttributesMapping();

        if ((s == null) || "".equals(s)) {
            // Leave value in property editor null
        } else {
        	
            // Use properties editor to tokenize the string
            PropertiesEditor propertiesEditor = new PropertiesEditor();
            propertiesEditor.setAsText(s);
                 
            Properties props = (Properties) propertiesEditor.getValue();

            // Now we have properties, process each one individually
            for (Iterator iter = props.keySet().iterator(); iter.hasNext();) {
            	List<ConfigAttribute> providers = new ArrayList<ConfigAttribute>();
            	String name = (String) iter.next();
                String value = props.getProperty(name);
                
                String regex = "[^\\w\\.\\,\\n\\s]";//The following format is allowed: letters.another.etc,moreletters.etc
                Pattern pattern = Pattern.compile(regex);
                
                if(pattern.matcher(value).find()){
                	
                	throw new IllegalArgumentException("Value '" + value + "' for Id '" + name + "'"
                			  + " is not allowed to contain anything other than word characters, dots and commas.");
                }else if(value.startsWith(".")){
                	
                	throw new IllegalArgumentException("Value '" + value + "' for Id '" + name + "'"
              			  + " is not allowed to start with a dot.");
                }else if(value.endsWith(".")){
                	
                	throw new IllegalArgumentException("Value '" + value + "' for Id '" + name + "'"
              			  + " is not allowed to end with a dot.");
                }
                
                String[] tokens = StringUtils.commaDelimitedListToStringArray(value);
                
                System.out.println("Name:" + name); 
                for (int i = 0; i < tokens.length; i++) {
                	String str = tokens[i].trim();
                	if(!str.equals("")){
                		providers.add(new SecurityConfig(str));
                	}
                	
                }              
               System.out.println("Providers:" + providers + " Size:" + providers.size());
               if (providers.size() > 0) {
            	   source.addProviders(name, providers);
               }
            }  
        }
        setValue(source);
    }
}

