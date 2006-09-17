package no.stelvio.integration.framework.hibernate.helper;

import net.sf.hibernate.mapping.RootClass;

import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.integration.framework.hibernate.cfg.Configuration;

/**
 * Add description
 *
 * @author personf8e9850ed756
 * @version $Revision: $, $Date: $
 * @todo javadoc
 */
public interface RecordConverter {
	Object recordToClass(Configuration config, String record, RootClass rootClass)
			throws ServiceFailedException;

	String classToRecord(Configuration config, RootClass rootClass, Object instance, StringBuffer buff)
			throws ServiceFailedException;
}
