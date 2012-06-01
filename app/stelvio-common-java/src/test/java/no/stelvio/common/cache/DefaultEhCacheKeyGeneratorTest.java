package no.stelvio.common.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import net.sf.ehcache.CacheManager;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:common-java_test_cache_beans_cache_annotations.xml" })
public class DefaultEhCacheKeyGeneratorTest {

	@Autowired
	@Qualifier(value = "hashCodeCacheKeyGeneratorNAVAnsatt")
	private NavAnsattServiceBi hashCodeCacheKeyGeneratorNAVAnsattService;

	@Autowired
	@Qualifier(value = "stringCacheKeyGeneratorNavAnsatt")
	private NavAnsattServiceBi stringCacheKeyGeneratorNavAnsattService;

	@Autowired
	private CacheManager cacheManager;

	@After
	public void after() {
		cacheManager.clearAll();
	}

	@DirtiesContext
	@Test
	public void shouldReturnTheSameNumbersWhenTheCacheIsHitWhenUsingDefaultHashCodeKeyGenerator() {
		assertChosenCacheKeyGeneratorStrategy(hashCodeCacheKeyGeneratorNAVAnsattService);
	}

	@DirtiesContext
	@Test
	public void shouldReturnTheSameNumbersWhenTheCacheIsHitWhenUsingStringCacheKeyGenerator() {
		assertChosenCacheKeyGeneratorStrategy(stringCacheKeyGeneratorNavAnsattService);
	}
	
	private void assertChosenCacheKeyGeneratorStrategy(NavAnsattServiceBi navAnsattServiceBi) {
		TestRequest request;
		String response;
		request = new TestRequest("D101768", "1589");
		response = navAnsattServiceBi.hentNAVAnsattFagomradeListe(request);
		assertThat(response, is("D101768 - 1589 - #1"));

		request = new TestRequest("A100474", "1289");
		response = navAnsattServiceBi.hentNAVAnsattFagomradeListe(request);
		assertThat(response, is("A100474 - 1289 - #2"));

		request = new TestRequest("A100474", "1289");
		response = navAnsattServiceBi.hentNAVAnsattFagomradeListe(request);
		assertThat(response, is("A100474 - 1289 - #2"));

		request = new TestRequest("D101858", "0689");
		response = navAnsattServiceBi.hentNAVAnsattFagomradeListe(request);
		assertThat(response, is("D101858 - 0689 - #3"));

		request = new TestRequest("D101868", "0589");
		response = navAnsattServiceBi.hentNAVAnsattFagomradeListe(request);
		assertThat(response, is("D101868 - 0589 - #4"));

		request = new TestRequest("D101868", "0589");
		response = navAnsattServiceBi.hentNAVAnsattFagomradeListe(request);
		assertThat(response, is("D101868 - 0589 - #4"));

		request = new TestRequest("A100564", "0389");
		response = navAnsattServiceBi.hentNAVAnsattFagomradeListe(request);
		assertThat(response, is("A100564 - 0389 - #5"));
	}

}
