package no.stelvio.common.cache.support;

import com.googlecode.ehcache.annotations.Cacheable;

import no.stelvio.common.cache.NavAnsattServiceBi;
import no.stelvio.common.cache.TestRequest;

public class DefaultNAVAnsatt implements NavAnsattServiceBi {

	private int hitCount;

	@Cacheable(cacheName = "navAnsattCache")
	public String hentNAVAnsattFagomradeListe(TestRequest testRequest) {
		hitCount++;
		
		return testRequest.getAnsattId() + " - " + testRequest.getEnhetsId() + " - #" + hitCount;
	}

}
