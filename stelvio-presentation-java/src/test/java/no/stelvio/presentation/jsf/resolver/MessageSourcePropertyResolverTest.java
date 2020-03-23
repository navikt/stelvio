/*
 * Copyright 2002-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.stelvio.presentation.jsf.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.apache.myfaces.test.mock.MockPropertyResolver;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import no.stelvio.presentation.jsf.mock.SpringDefinition;

/**
 * This class tests MessageSourcePropertyResolver.
 * 
 * @version $Id$
 */
@Deprecated
public class MessageSourcePropertyResolverTest {

	/**
	 * MessageSource for testing MessageSourcePropertyResolver.
	 */
	private MessageSource messageSource;

	/**
	 * Base resolver is a mock resolver from the Shale mock subproject.
	 */
	private MockPropertyResolver baseResolver;

	/**
	 * This is the object under test.
	 */
	private MessageSourcePropertyResolver messageSourcePropertyResolver;

	/**
	 * This is an sample JavaBean to ensure that the default resolver still works.
	 */
	private Locale notAResolver = new Locale("en", "US");

	/**
	 * Setup before testing starts. 
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		SpringDefinition.getContext();
	}

	/**
	 * Setup before each test.
	 */
	@Before
	public void setUp() {
		/* Initialise the MessageSource */
		ResourceBundleMessageSource mSource = new ResourceBundleMessageSource();
		mSource.setBasename("messages");
		this.messageSource = mSource;

		/* Initialise the Resolver chain */
		this.baseResolver = new MockPropertyResolver();
		this.messageSourcePropertyResolver = new MessageSourcePropertyResolver(this.baseResolver);

		LocaleContextHolder.setLocale(new Locale("en", "US"));
	}

	/**
	 * Test that the property resolver chain works with <code>MessageSource</code>.
	 */
	@Test
	public void testGetValueObjectObjectWithMessageSource() {
		Object testMessage = messageSourcePropertyResolver.getValue(this.messageSource, "testMessage");
		assertEquals("I am a test message", testMessage);
	}

	/**
	 * Test that the property resolver chain works with <code>MessageSource</code>.
	 * 
	 * The <code>MessageSourcePropertyResolver</code> checks the current locale associated with the current view. This is the
	 * standard way for JSF application to get their current locale.
	 */
	@Test
	public void testGetValueObjectObjectWithMessageSourceAndDifferentLocale() {
		LocaleContextHolder.setLocale(new Locale("en", "GB"));

		Object testMessage = messageSourcePropertyResolver.getValue(this.messageSource, "testMessage");

		assertEquals("London Calling", testMessage);
	}

	/**
	 * Test that the property resolver still works with JavaBeans.
	 * 
	 * The <code>MessageSourcePropertyResolver</code> works in a chain so it is responsible for calling the next item in the
	 * chain if it cannot handle the property resolution, i.e., the base object is not a <code>MessageSource</code>.
	 */
	@Test
	public void testGetValueObjectObjectWithJavaBean() {
		/* get the value of this.notAResolver.getCountry(); */
		Object testValue = messageSourcePropertyResolver.getValue(this.notAResolver, "country");
		assertEquals("US", testValue);
	}

	/**
	 * Test setValue with two arguments on messageSource. 
	 */
	@Test
	public void testSetValueObjectObjectObject() {
		try {
			messageSourcePropertyResolver.setValue(this.messageSource, "testMessage", "foo");
		} catch (Exception ex) {
			fail("setValue with two string arguments on messageSource. " + ex.getMessage());
		}
	}

	/**
	 * Test isReadOnly.
	 */
	@Test
	public void testIsReadOnlyObjectObject() {
		assertTrue(messageSourcePropertyResolver.isReadOnly(this.messageSource, "testMessage"));
	}

	/**
	 * Test getType.
	 */
	@Test
	public void testGetTypeObjectObject() {
		assertEquals(String.class, messageSourcePropertyResolver.getType(this.messageSource, "testMessage"));
	}

	/**
	 * Test getValue.
	 */
	@Test
	public void testGetValueObjectInt() {
		Object testMessage = messageSourcePropertyResolver.getValue(this.messageSource, 1);
		assertEquals("dumb idea", testMessage);
	}

	/**
	 * Test setValue.
	 */
	@Test
	public void testSetValueObjectIntObject() {
		try {
			messageSourcePropertyResolver.setValue(this.messageSource, 1, "someValueThatWillNeverBeSet");
		} catch (Exception ex) {
			fail("setValue with arguments 1 and the string someValueThatWillNeverBeSet on messageSource. " + ex.getMessage());
		}
	}

	/**
	 * Test isReadOnly.
	 */
	@Test
	public void testIsReadOnlyObjectInt() {
		assertTrue(messageSourcePropertyResolver.isReadOnly(this.messageSource, 1));
	}

	/**
	 * Test getType.
	 */
	@Test
	public void testGetTypeObjectInt() {
		assertEquals(String.class, messageSourcePropertyResolver.getType(this.messageSource, 1));
	}
}
