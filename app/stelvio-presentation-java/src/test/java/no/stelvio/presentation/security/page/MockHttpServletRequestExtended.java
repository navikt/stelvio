/*
 * Copyright 2004-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.stelvio.web.security.page;

import java.io.BufferedReader;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
//import javax.servlet.ServletRequestAttributeEvent;
//import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.shale.test.mock.MockHttpServletRequest;

/**
 * <p>Mock implementation of <code>HttpServletContext</code>.</p>
 *
 * $Id$
 */

public class MockHttpServletRequestExtended extends MockHttpServletRequest {

	 // ------------------------------------------------------ Instance Variables

    private List<String> principalRoles = new ArrayList<String>();//Added
    private StringBuffer requestURL;	
    private boolean secure;
    // ------------------------------------------------------------ Constructors


    
	public MockHttpServletRequestExtended() {
        super();
    }
   	public MockHttpServletRequestExtended(HttpSession session, Principal principal, List<String> roles) {

   		super(session);
   		this.setUserPrincipal(principal);
   		this.setPrincipalRoles(roles);
   	}
   	
   	@Override
   	public boolean isSecure(){
   		return secure;
   	}
   	
   	public void setSecure(boolean secure){
   		this.secure = secure;
   	}
   	
    // ----------------------------------------------------- Mock Object Methods
    
   	public StringBuffer getRequestURL() {
		return requestURL;
	}
	public void setRequestURL(StringBuffer requestURL) {
		this.requestURL = requestURL;
	}
   	
    //Added
    public void setPrincipalRoles(List<String> roles){
    	principalRoles = roles;
    }
    public List<String> getPrincipalRoles(){
    	return principalRoles;
    }
    
    public void addPrincipalRole(String role){
    	if(principalRoles != null){
    		principalRoles.add(role);
    	}else{
    		principalRoles = new ArrayList<String>();
    		principalRoles.add(role);
    	}
    }

   



    /** {@inheritDoc} */
    public boolean isUserInRole(String role) {
    	return this.getPrincipalRoles().contains(role);
    }

}
