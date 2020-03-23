package com.groundside.jsf.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.text.MessageFormat;

public class MessageResources {
    ClassLoader    _loader = null;
    String         _bundleName = null;
    ResourceBundle _bundle = null;
    Locale         _defaultLocale = Locale.getDefault();

    public MessageResources(ClassLoader loader, String bundle) {
        _loader = loader;
        _bundleName = bundle;
    }
    
    
    public String getResource(Locale locale, String key, Object parameters[]) {
        String resource = null;
        if (_bundle == null) {
            _bundle = ResourceBundle.getBundle(_bundleName,locale,_loader);
        }
        
        try  {
            resource = _bundle.getString(key);
            
            if ((resource != null) && (parameters != null) && (resource.contains("{0}"))){
                MessageFormat format = new MessageFormat(resource);
                resource = format.format(parameters);
            }  
            
            return resource;
        } catch (MissingResourceException mrex)  {
            return null;
        } 
    }
    
    public String getResource(String key, Object parameters[]) {
        return getResource(_defaultLocale,key,parameters);
    }
}
