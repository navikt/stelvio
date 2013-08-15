package no.stelvio.presentation.security.sso.accessmanager;

import javax.security.auth.Subject;

/**
 * Represent a mapper which can map a StelvioPrincipal to a Subject for a specific application server.
 * 
 * @author persondab2f89862d3
 * @see Subject
 */
public interface SubjectMapper {
	/**
	 * Creates a Subject from a StelvioPrincipal
	 * @param principal the StelvioPrincipal
	 * @return a Subject 
	 * @throws Exception if an exception occurs while creating a Subject, typically vendor specific 
	 * exceptions.
	 */
	Subject createSubject(StelvioPrincipal principal) throws Exception;
}
