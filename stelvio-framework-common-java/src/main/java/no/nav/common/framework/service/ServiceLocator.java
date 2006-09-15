package no.nav.common.framework.service;

/**
 * ServiceLocator is a generic interface that all classes implementing 
 * the <i>Service Locator</i> pattern must implement. 
 * <p/>
 * The <i>Service Locator</i> pattern is described in
 * <a href="http://www.corej2eepatterns.com/Patterns2ndEd/PatternRelationships.htm">
 * Core J2EE Patterns - Best Practices and Design Strategies</a> by
 * Alur, Crupi and Malks.
 * 
 * @author person7553f5959484
 * @version $Revision: 1180 $ $Author: psa2920 $ $Date: 2004-08-30 12:25:02 +0200 (Mon, 30 Aug 2004) $
 */
public interface ServiceLocator {

	/**
	 * Locate and access the named <i>Service</i>.
	 * 
	 * @param name the name or unique id of the service.
	 * @return an instance of the identified service.
	 * @throws ServiceNotFoundException if the service can not be found.
	 */
	Service lookup(String name) throws ServiceNotFoundException;
}
