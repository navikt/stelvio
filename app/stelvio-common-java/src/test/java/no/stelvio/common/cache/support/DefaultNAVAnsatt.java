package no.stelvio.common.cache.support;

import no.stelvio.common.cache.NavAnsattServiceBi;
import no.stelvio.common.cache.TestRequest;

import org.springmodules.cache.annotations.Cacheable;

public class DefaultNAVAnsatt implements NavAnsattServiceBi {

	private int hitCount;

	@Cacheable(modelId = "navAnsattCache")
	public String hentNAVAnsattFagomradeListe(TestRequest testRequest) {
		hitCount++;
		
		return testRequest.getAnsattId() + " - " + testRequest.getEnhetsId() + " - #" + hitCount;
	}

}
