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

package no.stelvio.presentation.security.page;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.myfaces.test.mock.MockHttpServletRequest;

/**
 * <p>
 * Mock implementation of <code>HttpServletContext</code>.
 * </p>
 * 
 * $Id$
 */
public class MockHttpServletRequestExtended extends MockHttpServletRequest {

	private List<String> principalRoles = new ArrayList<String>(); // Added
	private StringBuffer requestURL;
	private boolean secure;

	/**
	 * Creates a new instance of MockHttpServletRequestExtended.
	 */
	public MockHttpServletRequestExtended() {
		super();
	}

	/**
	 * Creates a new instance of MockHttpServletRequestExtended.
	 * 
	 * @param session
	 *            session
	 * @param principal
	 *            principal
	 * @param roles
	 *            roles
	 */
	public MockHttpServletRequestExtended(HttpSession session, Principal principal, List<String> roles) {
		super(session);
		this.setUserPrincipal(principal);
		this.setPrincipalRoles(roles);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSecure() {
		return secure;
	}

	/**
	 * Set secure.
	 * 
	 * @param secure
	 *            secure
	 */
	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	/**
	 * Get request url.
	 * 
	 * @return url
	 */
	public StringBuffer getRequestURL() {
		return requestURL;
	}

	/**
	 * Set request url.
	 * 
	 * @param requestURL
	 *            url
	 */
	public void setRequestURL(StringBuffer requestURL) {
		this.requestURL = requestURL;
	}

	/**
	 * Set principal roles.
	 * 
	 * @param roles
	 *            roles
	 */
	public final void setPrincipalRoles(List<String> roles) {
		principalRoles = roles;
	}

	/**
	 * Get principal roles.
	 * 
	 * @return roles
	 */
	public List<String> getPrincipalRoles() {
		return principalRoles;
	}

	/**
	 * Add principal role.
	 * 
	 * @param role
	 *            role
	 */
	public void addPrincipalRole(String role) {
		if (principalRoles != null) {
			principalRoles.add(role);
		} else {
			principalRoles = new ArrayList<String>();
			principalRoles.add(role);
		}
	}

	/** {@inheritDoc} */
	public boolean isUserInRole(String role) {
		return this.getPrincipalRoles().contains(role);
	}

}
