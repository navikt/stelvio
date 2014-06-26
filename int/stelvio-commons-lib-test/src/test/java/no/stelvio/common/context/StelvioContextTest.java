package no.stelvio.common.context;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.ibm.websphere.workarea.UserWorkArea;

public class StelvioContextTest {
	@Test
	public void testDefaultWorkArea() {
		UserWorkArea workArea = Mockito.mock(UserWorkArea.class);
		addCommonBehavior(workArea);
		assertDefaults(workArea);
	}

	@Test
	public void testWrongWorkAreaName() {
		UserWorkArea workArea = Mockito.mock(UserWorkArea.class);
		Mockito.when(workArea.getName()).thenReturn("fooBar");
		addCommonBehavior(workArea);
		assertDefaults(workArea);
	}

	@Test
	public void testContextWorkArea() {
		UserWorkArea workArea = Mockito.mock(UserWorkArea.class);
		Mockito.when(workArea.getName()).thenReturn(UserWorkAreaContextAdapter.USER_WORK_AREA_NAME);
		addCommonBehavior(workArea);

		StelvioContext context = new StelvioContext(workArea);
		Assert.assertEquals("my_app_id", context.getApplicationId());
		Assert.assertEquals("my_correlation_id", context.getCorrelationId());
		Assert.assertEquals("my_language_id", context.getLanguageId());
		Assert.assertEquals(StelvioContext.DEFAULT_USER_NAME, context.getNavUserId());
		Assert.assertEquals("my_user_id", context.getUserId());
	}

	private void addCommonBehavior(UserWorkArea workArea) {
		Mockito.when(workArea.get("applicationId")).thenReturn("my_app_id");
		Mockito.when(workArea.get("correlationId")).thenReturn("my_correlation_id");
		Mockito.when(workArea.get("languageId")).thenReturn("my_language_id");
		Mockito.when(workArea.get("userId")).thenReturn("my_user_id");
	}

	private void assertDefaults(UserWorkArea workArea) {
		StelvioContext context = new StelvioContext(workArea);
		Assert.assertEquals(StelvioContext.DEFAULT_APPLICATION_NAME, context.getApplicationId());
		Assert.assertNotNull(context.getCorrelationId());
		Assert.assertEquals(StelvioContext.DEFAULT_LANGUAGE, context.getLanguageId());
		Assert.assertEquals(StelvioContext.DEFAULT_USER_NAME, context.getNavUserId());
		Assert.assertEquals(StelvioContext.DEFAULT_USER_NAME, context.getUserId());
	}
}
