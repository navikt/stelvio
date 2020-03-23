package no.stelvio.common.security.authorization.method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test class for AnnotationAttributesMappingEditor.
 * 
 *
 */
public class AnnotationAttributesMappingEditorTest {

	/**
	 * Test that storing of multiple lines is correct.
	 */
	@Test
	public void testStoreMultipleLinesCorrect() {
		AnnotationAttributesMappingEditor editor = new AnnotationAttributesMappingEditor();
		String s = "Id1=test.provider,test.provider2\n";
		s += "Id2=test.provider3";
		editor.setAsText(s);
		AnnotationAttributesMapping map = (AnnotationAttributesMapping) editor.getValue();
		assertEquals(2, map.getProviderMap().size());
	}

	/**
	 * Test that comma separated list is stored with the correct annotation attribute.
	 */
	@Test
	public void testCommaSeparatedListIsStoredWithTheCorrectAnnotationAttribute() {
		AnnotationAttributesMappingEditor editor = new AnnotationAttributesMappingEditor();
		String annotAtrr1 = "Id1";
		String annotAtrr2 = "Id2";
		String commaSep1 = "test.provider,test.provider2";
		String commaSep2 = "test.provider3";
		String s = annotAtrr1 + "=" + commaSep1;
		s += "\n" + annotAtrr2 + "=" + commaSep2;

		editor.setAsText(s);
		AnnotationAttributesMapping map = (AnnotationAttributesMapping) editor.getValue();
		assertEquals("Line 1.", commaSep1.split(",").length, map.getProviders(annotAtrr1).size());
		assertEquals("Line 2.", commaSep2.split(",").length, map.getProviders(annotAtrr2).size());
	}

	/**
	 * Test correct string format.
	 */
	@Test
	public void testCorrectStringFormat() {
		String noEqualSign = "IdNoEqualSign\n" + "anotherline";
		String specialCharacters = "Id=something.another*.?";
		String startsWithDotStringAfterEqual = "Id=.something,something.again";
		String endsWithDotStringAfterEqual = "Id=something, ends.with.dot.";
		String multipleEqualsOn1Line = "id=somthing=another";

		AnnotationAttributesMappingEditor editor = new AnnotationAttributesMappingEditor();
		AnnotationAttributesMapping map = null;

		editor.setAsText(noEqualSign);
		map = (AnnotationAttributesMapping) editor.getValue();
		assertEquals("Line with no '=' sign.", 0, map.getProviderMap().size());

		try {
			editor.setAsText(specialCharacters);
			fail("Line with special character other than [.] and [,]." + " Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
			// should happen
		}

		try {
			editor.setAsText(startsWithDotStringAfterEqual);
			fail("Line that starts with a dot." + " Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
			// should happen
		}

		try {
			editor.setAsText(endsWithDotStringAfterEqual);
			fail("Line that ends with a dot." + " Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
			// should happen
		}

		try {
			editor.setAsText(multipleEqualsOn1Line);
			fail("Line with multiple '=' signs on 1 line." + " Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
			// should happen
		}
	}
}
