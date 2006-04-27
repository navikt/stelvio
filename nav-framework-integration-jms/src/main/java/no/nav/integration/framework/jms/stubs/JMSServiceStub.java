package no.nav.integration.framework.jms.stubs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import no.nav.integration.framework.jms.JMSService;
import no.nav.integration.framework.jms.handler.stubs.TPSXMLMessageHandlerStub;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceRequest;
import no.nav.common.framework.service.ServiceResponse;


/**
 * JMS Stub for TPS.
 * 
 * @author person356941106810, Accenture
 * @author person02f3de2754b4, Accenture
 * @version $Id: JMSServiceStub.java 2029 2005-03-03 09:42:45Z psa2920 $
 */
public class JMSServiceStub extends JMSService {

	protected static final String PERSON_DATA_LIST = "personDataList";
	protected static final String FNR = "fnr";
	/**
	 * Executes a service request, but stubs the calls that does not yet exist
	 * @see no.nav.integration.framework.service.IntegrationService#doExecute(no.nav.common.framework.service.ServiceRequest)
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
		// find out if we want to actually call the service or not
		ServiceResponse resp = null;
		String mal = (String) request.getData("mal");
		if (mal.startsWith("Opprett") || mal.startsWith("Endre")) {
			Object personData = request.getData("personData");
			if (personData != null) {

				resp = new ServiceResponse();
				Method meth = BeanUtils.findDeclaredMethodWithMinimalParameters(personData.getClass(), "setFnr");
				Method getMeth = BeanUtils.findDeclaredMethodWithMinimalParameters(personData.getClass(), "getFnr");
				try {
					Object result = getMeth.invoke(personData, null);
					if (result == null) {

						String fnr = "12345678901";
						// make sure it is a BOST nr
						long lFnr = Long.parseLong(fnr);
						if (lFnr < 41000000000l) {
							lFnr += 41000000000l;
						}
						fnr = Long.toString(lFnr);
						meth.invoke(personData, new Object[] { fnr });
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				resp.setData("personData", request.getData("personData"));
			}

		} else if ("PersonOpplysningerUtland".equals(mal)) {
			TPSXMLMessageHandlerStub stub = (TPSXMLMessageHandlerStub) super.messageHandler;
			String fnr = (String) request.getData(FNR);
			if (fnr == null || "".equals(fnr.trim())) {
				fnr = "Default";
			}
			Object bean = null;
			try {
				bean = stub.getHistorikkFactory().getBean(fnr + "PersonDataUtland");
			} catch (NoSuchBeanDefinitionException e) {
				// Do a Darth Vader and choke this
				if (log.isDebugEnabled()) {
					log.debug("Choked by Darth Vader ;)", e);
				}
			}

			if (bean == null) {
				bean = stub.getHistorikkFactory().getBean("DefaultPersonDataUtland");
			}
			resp = new ServiceResponse();
			resp.setData("personDataUtland", bean);
		} else if ("PersonOpplysningerGiroUtland".equals(mal)) {
			TPSXMLMessageHandlerStub stub = (TPSXMLMessageHandlerStub) super.messageHandler;
			String fnr = (String) request.getData(FNR);
			if (fnr == null || "".equals(fnr.trim())) {
				fnr = "Default";
			}
			Object bean = null;
			try {
				bean = stub.getHistorikkFactory().getBean(fnr + "PersonGiro");
			} catch (NoSuchBeanDefinitionException e) {
				// Do a Darth Vader and choke this
				if (log.isDebugEnabled()) {
					log.debug("Choked by Darth Vader ;)", e);
				}
			}

			if (bean == null) {
				bean = stub.getHistorikkFactory().getBean("DefaultPersonGiro");
			}
			resp = new ServiceResponse();
			resp.setData("personGiroUtland", bean);
		} else if ("Adresser".equals(mal)) {
			TPSXMLMessageHandlerStub stub = (TPSXMLMessageHandlerStub) super.messageHandler;
			String fnr = (String) request.getData(FNR);
			if (fnr == null || "".equals(fnr.trim())) {
				fnr = "Default";
			}
			Object adrUtl = null;
			Object adrTil = null;
			Object adrPos = null;
			try {
				adrUtl = stub.getHistorikkFactory().getBean(fnr + "AdresseUtland");
			} catch (NoSuchBeanDefinitionException e) {
				// Do a Darth Vader and choke this
				if (log.isDebugEnabled()) {
					log.debug("Choked by Darth Vader ;)", e);
				}
			}

			try {
				adrTil = stub.getHistorikkFactory().getBean(fnr + "AdresseTillegg");
			} catch (NoSuchBeanDefinitionException e) {
				// Do a Darth Vader and choke this
				if (log.isDebugEnabled()) {
					log.debug("Choked by Darth Vader ;)", e);
				}
			}

			try {
				adrPos = stub.getHistorikkFactory().getBean(fnr + "AdressePost");
			} catch (NoSuchBeanDefinitionException e) {
				// Do a Darth Vader and choke this
				if (log.isDebugEnabled()) {
					log.debug("Choked by Darth Vader ;)", e);
				}
			}

			if (adrUtl == null) {
				adrUtl = stub.getHistorikkFactory().getBean("DefaultAdresseUtland");
			}

			if (adrTil == null) {
				adrTil = stub.getHistorikkFactory().getBean("DefaultAdresseTillegg");
			}

			if (adrPos == null) {
				adrPos = stub.getHistorikkFactory().getBean("DefaultAdressePost");
			}
			resp = new ServiceResponse();
			resp.setData("adresseUtland", adrUtl);
			resp.setData("adresseTillegg", adrTil);
			resp.setData("adressePost", adrPos);
		} else if ("PersonDataSoundexAdresse".equals(mal)) {
			TPSXMLMessageHandlerStub stub = (TPSXMLMessageHandlerStub) super.messageHandler;
			List personDataList = new ArrayList();
			try {
				Object personData1 = stub.getHistorikkFactory().getBean("PersonBasertPaaSoundexAdresse1");
				personDataList.add(personData1);
				Object personData2 = stub.getHistorikkFactory().getBean("PersonBasertPaaSoundexAdresse2");
				personDataList.add(personData2);
				Object personData3 = stub.getHistorikkFactory().getBean("PersonBasertPaaSoundexAdresse3");
				personDataList.add(personData3);
			} catch (NoSuchBeanDefinitionException e) {
				e.printStackTrace(System.err);
			}
			resp = new ServiceResponse();
			resp.setData(PERSON_DATA_LIST, personDataList);
		} else if ("PersonDataSoundexNavn".equals(mal)) {
			TPSXMLMessageHandlerStub stub = (TPSXMLMessageHandlerStub) super.messageHandler;
			List personDataList = new ArrayList();
			try {
				Object personData1 = stub.getHistorikkFactory().getBean("PersonBasertPaaSoundexNavn1");
				personDataList.add(personData1);
				Object personData2 = stub.getHistorikkFactory().getBean("PersonBasertPaaSoundexNavn2");
				personDataList.add(personData2);
				Object personData3 = stub.getHistorikkFactory().getBean("PersonBasertPaaSoundexNavn3");
				personDataList.add(personData3);
			} catch (NoSuchBeanDefinitionException e) {
				e.printStackTrace(System.err);
			}
			resp = new ServiceResponse();
			resp.setData(PERSON_DATA_LIST, personDataList);
		} else if ("PersonDataKontonrNorsk".equals(mal)) {
			TPSXMLMessageHandlerStub stub = (TPSXMLMessageHandlerStub) super.messageHandler;
			List personDataList = new ArrayList();
			try {
				Object personData1 = stub.getHistorikkFactory().getBean("PersonBasertPaaSoundexNavn1");
				personDataList.add(personData1);
			} catch (NoSuchBeanDefinitionException e) {
				e.printStackTrace(System.err);
			}
			resp = new ServiceResponse();
			resp.setData(PERSON_DATA_LIST, personDataList);
		} else if ("PersonDataKontonrUtland".equals(mal)) {
			TPSXMLMessageHandlerStub stub = (TPSXMLMessageHandlerStub) super.messageHandler;
			List personDataList = new ArrayList();
			try {
				Object personData1 = stub.getHistorikkFactory().getBean("PersonBasertPaaSoundexAdresse1");
				personDataList.add(personData1);
			} catch (NoSuchBeanDefinitionException e) {
				e.printStackTrace(System.err);
			}
			resp = new ServiceResponse();
			resp.setData(PERSON_DATA_LIST, personDataList);
		} else if ("PersonDataUtenlandsreferanse".equals(mal)) {
			TPSXMLMessageHandlerStub stub = (TPSXMLMessageHandlerStub) super.messageHandler;
			List personDataList = new ArrayList();
			try {
				Object personData2 = stub.getHistorikkFactory().getBean("PersonBasertPaaSoundexNavn2");
				personDataList.add(personData2);
			} catch (NoSuchBeanDefinitionException e) {
				e.printStackTrace(System.err);
			}
			resp = new ServiceResponse();
			resp.setData(PERSON_DATA_LIST, personDataList);
		} else {
			String fnr = (String) request.getData(FNR);
			fnr = getWithoutBOST(fnr);
			request.setData(FNR, fnr);
			resp = super.doExecute(request);
		}
		return resp;
	}

	/**
	 * Manipulates the social security number.
	 * 
	 * @param fnr the original ssn.
	 * @return the manipulated ssn.
	 */
	private String getWithoutBOST(String fnr) {
		if (fnr != null) {
			int day = Integer.parseInt(fnr.substring(0, 2));
			day = day > 40 ? day - 40 : day;
			String newFnr = day < 10 ? "0" + day : Integer.toString(day);
			return newFnr + fnr.substring(2);
		}
		return null;
	}
}
