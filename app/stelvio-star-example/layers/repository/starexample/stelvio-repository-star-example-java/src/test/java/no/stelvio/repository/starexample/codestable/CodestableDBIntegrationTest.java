package no.stelvio.repository.starexample.codestable;
import junit.framework.TestCase;
import no.nav.domain.pensjon.codestable.HenvendelseTypeCti;
import no.stelvio.repository.codestable.CodesTableRepository;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class CodestableDBIntegrationTest extends TestCase {
	
	
	
	public void testFetchAllHenvendelseTypeCti(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("modules/rep-codestable-context.xml");
		CodesTableRepository repository = (CodesTableRepository) ctx.getBean("rep.codestable.codestableRepository");
		repository.findCodesTableItems(HenvendelseTypeCti.class);
	}
}
