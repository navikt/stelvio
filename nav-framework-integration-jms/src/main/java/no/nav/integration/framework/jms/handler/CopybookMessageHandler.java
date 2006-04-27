package no.nav.integration.framework.jms.handler;

import java.io.InputStream;
import java.util.List;

import javax.jms.Message;
import javax.jms.TextMessage;

import net.sf.hibernate.MappingException;
import net.sf.hibernate.mapping.RootClass;

import no.nav.integration.framework.jms.MessageHandler;
import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceResponse;

import no.trygdeetaten.integration.framework.hibernate.cfg.Configuration;
import no.trygdeetaten.integration.framework.hibernate.helper.RecordConverter;
import no.trygdeetaten.integration.framework.hibernate.helper.RecordHelper;

/**
 * Oversetter fra en "Oppdrag string" til ett DO objekt via en hbm mapping.
 * 
 * @author person5204c0b677af, Accenture
 * @version $Revision: 2343 $ $Author: skb2930 $ $Date: 2005-06-21 11:15:49 +0200 (Tue, 21 Jun 2005) $ 
 */
public class CopybookMessageHandler implements MessageHandler {

	/** Liste med hbm filer som kan brukes. Settes av Spring */
	private List hibernateMappings;
	/** Navnet på den hbm mappingen som skal brukes. Settes av Spring */
	private String className;
	/** De innleste hbm mappingene blir lagt inn her */
	private Configuration configuration;
    /** The <code>RecordConverter</code> to use when converting from record to instance and vice versa. */
	private RecordConverter recordConverter;

	/**
	 * @see no.nav.integration.framework.jms.MessageHandler#handleMessage(javax.jms.Message)
	 */
	public ServiceResponse handleMessage(Message msg) throws ServiceFailedException {

		TextMessage message = (TextMessage) msg;

		RootClass clazz = RecordHelper.getServiceRootClass(configuration, className);
		Object minDO = null;
		try {
			minDO = recordConverter.recordToClass(configuration, message.getText(), clazz);
		} catch (Throwable e) {
			throw new ServiceFailedException(FrameworkError.UNCONFIGURED_ERROR, e);
		}

		ServiceResponse resp = new ServiceResponse();
		resp.setData(className, minDO);
		return resp;
	}

	/**
	 * @see no.nav.integration.framework.jms.MessageHandler#init()
	 */
	public void init() {

		ClassLoader resourceLoader = Thread.currentThread().getContextClassLoader();
		configuration = new Configuration();
		String mapping = null;
		for (int i = 0; i < hibernateMappings.size(); i++) {
			mapping = (String) hibernateMappings.get(i);
			InputStream stream = resourceLoader.getResourceAsStream(mapping);
			try {
				configuration.addInputStream(stream);
			} catch (MappingException e) {
				throw new SystemException(FrameworkError.JCA_GET_CONFIG_ERROR, e, mapping);
			}
		}
	}

	/**
	 * Setter en liste med hbm filer som kan brukes.
	 * @param map lista med hbm filer
	 */
	public void setHibernateMappings(List map) {
		hibernateMappings = map;
	}

	/**
	 * Setter navnet på den hbm mappinga som skal brukes
	 * @param string String hbm fil
	 */
	public void setClassName(String string) {
		className = string;
	}

	/**
	 * Sets the <code>RecordConverter</code> to use when converting from record to instance and vice versa.
	 *
	 * @param recordConverter the <code>RecordConverter</code> to use.
	 * @see RecordConverter
	 */
	public void setRecordConverter(final RecordConverter recordConverter) {
		this.recordConverter = recordConverter;
	}
}
