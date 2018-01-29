package no.stelvio.common.cache.support;

import no.stelvio.common.cache.NavAnsattServiceBi;
import no.stelvio.common.cache.TestRequest;
import org.springframework.cache.annotation.Cacheable;

public class StringCacheKeyGeneratorNavAnsatt implements NavAnsattServiceBi{

	private int hitCount;

	@Cacheable("navAnsattCache")
	public String hentNAVAnsattFagomradeListe(TestRequest testRequest) {
		hitCount++;
		
		return testRequest.getAnsattId() + " - " + testRequest.getEnhetsId() + " - #" + hitCount;
	}

}
