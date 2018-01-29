package no.stelvio.presentation.security.logout;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LogoutService is a logout component for Stelvio.
 * <p>
 * LogoutService is an interface defining functionality for logging a user out.
 * Implementations are standard POJO's. 
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * */
public interface LogoutService {
	
	/**
	 * Logs out a user from the current session using the {@link javax.faces.context.ExternalContext#redirect(String)}.
	 * 
	 * @throws IOException if an input/output error occurs
	 */
	void logout() throws IOException;
	/**
	 * Logs out a user from the current session using the {@link javax.servlet.http.HttpServletResponse#sendRedirect(String)}.
	 * 
	 * @param request request
	 * @param response resonse
	 * @throws IOException if an input/output error occurs
	 */
	void logout(HttpServletRequest request, HttpServletResponse response) throws IOException;
	
	/**
	 * Logs out a user from the current session using the {@link javax.faces.context.ExternalContext#redirect(String)}.
	 * After a successful logout, the user will be redirected to the location specified by the destinationUrl parameter.
	 * 
	 * @param destinationUrl the destination url after a successful logout.
	 * @throws IOException if an input/output error occurs
	 */
	void logoutToUrl(String destinationUrl) throws IOException;
	/**
	 * Logs out a user from the current session using the {@link javax.servlet.http.HttpServletResponse#sendRedirect(String)}.
	 * After a successful logout, the user will be redirected to the location specified by the destinationUrl parameter.
	 * 
	 * @param destinationUrl the destination url after a successful logout.
	 * @param request the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws IOException if an input/output error occurs 
	 */
	void logoutToUrl(String destinationUrl, HttpServletRequest request, HttpServletResponse response) throws IOException;
	
}
