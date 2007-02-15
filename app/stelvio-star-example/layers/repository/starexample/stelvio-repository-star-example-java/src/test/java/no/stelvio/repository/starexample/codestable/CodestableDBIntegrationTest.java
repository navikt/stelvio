package no.stelvio.repository.starexample.codestable;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import no.stelvio.domain.star.example.codestable.HenvendelseTypeCti;
import no.stelvio.repository.codestable.CodesTableRepository;


public class CodestableDBIntegrationTest extends TestCase {
	
	
	
	public void testFetchAllHenvendelseTypeCti(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("modules/rep-codestable-context.xml");
		CodesTableRepository repository = (CodesTableRepository) ctx.getBean("rep.codestable.codestableRepository");
		repository.findCodesTableItems(HenvendelseTypeCti.class);
	}
}
