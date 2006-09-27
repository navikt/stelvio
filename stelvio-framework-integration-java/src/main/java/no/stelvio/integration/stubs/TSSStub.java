package no.stelvio.integration.stubs;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.InputStreamResource;

import no.stelvio.integration.service.IntegrationService;
import no.stelvio.common.FrameworkError;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;


/**
 * TSS Stub.
 * 
 * @author person356941106810, Accenture
 * @author person02f3de2754b4, Accenture
 * @version $Id: TSSStub.java 2372 2005-06-23 14:53:27Z skb2930 $
 */
public class TSSStub extends IntegrationService {

	//--- Første samhandler er person
	private static final String id1 = "12345678901";
	private static final String kode1 = "FNR";
	private static final String type1 = "VE";

	//--- Andre samhandler er organisasjon som er kreditor
	private static final String id2 = "00957649262";
	private static final String kode2 = "ORG";
	private static final String type2 = "KRED";

	//	--- Tredje samhandler er organisasjon men IKKE kreditor
	private static final String id3 = "00986965610";
	private static final String kode3 = "ORG";
	private static final String type3 = "HFO";

	private static final String KRED = "KRED";
	private static final String TSS_ENHET_DO_LIST = "TSSEnhetDOList";
	String testDataFilename = null;
	XmlBeanFactory factory = null;
	TSSStubDataRepository repo = null;

	/**
	 * Stub for TSS.
	 *
	 * @see no.stelvio.integration.service.IntegrationService#doExecute(no.stelvio.common.service.ServiceRequest)
	 */
	protected ServiceResponse doExecute(final ServiceRequest request) throws ServiceFailedException {

		if (log.isDebugEnabled()) {
			log.debug("doExecute - start");
		}

		ServiceResponse response = new ServiceResponse();
		String mal = (String) request.getData("mal");

		if ("TSSEnhetDOHentSamhandler".equals(mal) || "TSSEnhetDOHentOverordnetSamhandler".equals(mal)) {

			String searchId = (String) request.getData("id");
			String searchIdType = (String) request.getData("idType");
			String searchSamhandlertype = (String) request.getData("samhandlertype");

			if (searchId == null || "".equals(searchId.trim())) {
				throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, "id");
			}

			if (searchIdType == null || "".equals(searchIdType.trim())) {
				throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, "idType");
			}

			if (searchSamhandlertype == null || "".equals(searchSamhandlertype.trim())) {
				throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, "samhandlertype");
			}
			String samhandlerName = repo.getSamhandler(searchId, searchIdType, searchSamhandlertype);
			Object samhandler = retrieveSamhandler(samhandlerName);
			response.setData("SAMHANDLER", samhandler);

		} else if ("TSSEnhetDOHentSamhandlerEksternId".equals(mal)) {

			String eksternSamhandlerId = (String) request.getData("eksternSamhandlerId");
			if (eksternSamhandlerId == null || "".equals(eksternSamhandlerId.trim())) {
				throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, "eksternSamhandlerId");
			}
			String samhandlerName = repo.getSamhandler(eksternSamhandlerId);
			Object samhandler = retrieveSamhandler(samhandlerName);
			response.setData("SAMHANDLER", samhandler);

		} else if ("TSSEnhetDOSoundexAdresse".equals(mal)) {

			List samhandlerList = retrieveMockSamhandlerList();
			response.setData(TSS_ENHET_DO_LIST, samhandlerList);

		} else if ("TSSEnhetDOSoundexNavn".equals(mal)) {

			List samhandlerList = retrieveMockSamhandlerList();
			response.setData(TSS_ENHET_DO_LIST, samhandlerList);

		} else if ("TSSEnhetDOKontonrNorsk".equals(mal)) {

			List samhandlerList = new ArrayList();

			String searchId = "idOffisiell";
			String searchIdType = "ORG";
			String searchSamhandlertype = KRED;

			String samhandlerName = repo.getSamhandler(searchId, searchIdType, searchSamhandlertype);

			Object samhandler = retrieveSamhandler(samhandlerName);
			samhandlerList.add(samhandler);
			response.setData(TSS_ENHET_DO_LIST, samhandlerList);

		} else if ("TSSEnhetDOKontonrUtland".equals(mal)) {

			List samhandlerList = new ArrayList();

			String searchId = "Nisse";
			String searchIdType = "UTPE";
			String searchSamhandlertype = KRED;

			String samhandlerName = repo.getSamhandler(searchId, searchIdType, searchSamhandlertype);
			Object samhandler = retrieveSamhandler(samhandlerName);
			samhandlerList.add(samhandler);
			response.setData(TSS_ENHET_DO_LIST, samhandlerList);

		} else {

			throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, "mal");
		}

		if (log.isDebugEnabled()) {
			log.debug("doExecute - exit");
		}

		return response;

	}

	/**
	 * Retrieve samhandler.
	 * 
	 * @param samhandlerName name of the samhandler.
	 * @return the samhandler.
	 */
	private Object retrieveSamhandler(String samhandlerName) {

		Object samhandler = null;
		if (samhandlerName != null && !"".equals(samhandlerName.trim())) {
			samhandler = factory.getBean(samhandlerName);
		}
		return samhandler;
	}

	/**
	 * Initializes the stub
	 */
	public void init() {
		InputStream stream =
			new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(testDataFilename));
		factory = new XmlBeanFactory(new InputStreamResource(stream));
		repo = (TSSStubDataRepository) factory.getBean("DataRepository");
	}

	/**
	 * Sets the name of the file containing the testdata
	 * @param string the filename of the testdata
	 */
	public void setTestDataFilename(String string) {
		testDataFilename = string;
	}

	/**
	 * Retrieve a list of samhandler objects.
	 * 
	 * @return the list.
	 */
	private List retrieveMockSamhandlerList() {
		List samhandlerList = new ArrayList();

		String samhandlerName = repo.getSamhandler(id1,kode1, type1);
		Object samhandler = retrieveSamhandler(samhandlerName);
		samhandlerList.add(samhandler);

		samhandlerName = repo.getSamhandler(id2,kode2, type2);
		Object samhandler2 = retrieveSamhandler(samhandlerName);
		samhandlerList.add(samhandler2);

		samhandlerName = repo.getSamhandler(id3,kode3, type3);
		Object samhandler3 = retrieveSamhandler(samhandlerName);
		samhandlerList.add(samhandler3);

		return samhandlerList;
	}
}
