package no.nav.esso;

import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class OneDayPwLoginModuleTest {

	@Test
	@Ignore
	public void testSuccessfulLogin() {
		try {
			whenNew(OneDayPwValidator.class).withAnyArguments().thenReturn(new MockValidator(MockValidator.VALID_USER, null, null));
			OneDayPwLoginModule module = new OneDayPwLoginModule();
			module.init(null, null, null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
