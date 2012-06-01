package no.stelvio.common.cache.support;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;

import no.stelvio.common.cache.NavAnsattServiceBi;
import no.stelvio.common.cache.TestRequest;

public class StringCacheKeyGeneratorNavAnsatt implements NavAnsattServiceBi {

	private int hitCount;

	@Cacheable(cacheName = "navAnsattCache", keyGenerator=@KeyGenerator(name = "StringCacheKeyGenerator"))
	public String hentNAVAnsattFagomradeListe(TestRequest testRequest) {
		hitCount++;
		
		return testRequest.getAnsattId() + " - " + testRequest.getEnhetsId() + " - #" + hitCount;
	}

}
