package no.stelvio.service.codestable;

import javax.ejb.CreateException;

/**
 * Local Home interface for no.stelvio.service.codestable.CodesTableFactoryBean bean
 */
public interface CodesTableFactoryLocalHome extends javax.ejb.EJBLocalHome {

	public static final String COMP_NAME = "java:comp/env/ejb/CodesTableFactoryBean";

	public static final String JNDI_NAME = "ejb/no/nav/service/pensjon/CodesTableFactoryLocalHome";

	/* Default create */
	public CodesTableFactoryLocal create()
			throws CreateException;

}