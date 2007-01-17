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
 
import junit.framework.Test;
import junit.framework.TestSuite;

import no.stelvio.common.context.RequestContext;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.test.mock.MockPropertyResolver;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * This class tests MessageSourcePropertyResolver.
 * 
 * @author Rick Hightower
 * @version  $Id$
 */
public class MessageSourcePropertyResolverTest extends AbstractJsfTestCase {

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
	 * This is an sample JavaBean to ensure that the default resolver still
	 * works.
	 */
    private Locale notAResolver = new Locale("en", "US");

    /**
	 * Creates a new MessageSourcePropertyResolverTest object.
	 * @param name test name
	 */
    public MessageSourcePropertyResolverTest(final String name) {
        super(name);
    }

    // Return the tests included in this test case.
    /**
	 * DOCUMENT ME!
	 * @return DOCUMENT ME
	 */
    public static Test suite() {
        return (new TestSuite(MessageSourcePropertyResolverTest.class));
    }

    /**
	 * Setup the test harness.
	 */
    public void setUp() throws Exception{
        super.setUp();

        /** Initialise the MessageSource */
        ResourceBundleMessageSource mSource = new ResourceBundleMessageSource();
        mSource.setBasename("messages");
        this.messageSource = mSource;

        /* Initialise the Resolver chain */
        this.baseResolver = new MockPropertyResolver();
        this.messageSourcePropertyResolver = new MessageSourcePropertyResolver(this.baseResolver);
        
        RequestContext.setLocale(new Locale("en", "US"));
    }

    /**
	 * Test that the property resolver chain works with
	 * <code>MessageSource</code>.
	 */
    public void testGetValueObjectObjectWithMessageSource() {
        Object testMessage = messageSourcePropertyResolver.getValue(this.messageSource,
                "testMessage");
        assertEquals("I am a test message", testMessage);
    }

    /**
	 * Test that the property resolver chain works with
	 * <code>MessageSource</code>.
	 * 
	 * The <code>MessageSourcePropertyResolver</code> checks the current
	 * locale associated with the current view. This is the standard way 
	 * for JSF application to get their current locale.
	 */
    public void testGetValueObjectObjectWithMessageSourceAndDifferentLocale() {
        RequestContext.setLocale(new Locale("en", "GB"));

        Object testMessage = messageSourcePropertyResolver.getValue(this.messageSource, "testMessage");

        assertEquals("London Calling", testMessage);
    }

    /**
	 * Test that the property resolver still works with JavaBeans.
	 * 
	 * The <code>MessageSourcePropertyResolver</code> works in a chain so it
	 * is responsible for calling the next item in the chain if it cannot handle
	 * the property resolution, i.e., the base object is not a
	 * <code>MessageSource</code>.
	 */
    public void testGetValueObjectObjectWithJavaBean() {
        /* get the value of this.notAResolver.getCountry(); */
        Object testValue = messageSourcePropertyResolver.getValue(this.notAResolver, "country");
        assertEquals("US", testValue);
    }

    /**
	 * 
	 */
    public void testSetValueObjectObjectObject() {
        try {
            messageSourcePropertyResolver.setValue(this.messageSource, "testMessage", "foo");
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    /**
	 * 
	 */
    public void testIsReadOnlyObjectObject() {
        assertTrue(messageSourcePropertyResolver.isReadOnly(
                this.messageSource, "testMessage"));
    }

    /**
	 * 
	 */
    public void testGetTypeObjectObject() {
        assertEquals(String.class,
            messageSourcePropertyResolver.getType(this.messageSource,
                "testMessage"));
    }

    /**
	 * 
	 */
    public void testGetValueObjectInt() {
        Object testMessage = messageSourcePropertyResolver.getValue(this.messageSource, 1);
        assertEquals("dumb idea", testMessage);
    }

    /**
	 * 
	 */
    public void testSetValueObjectIntObject() {
        try {
            messageSourcePropertyResolver.setValue(this.messageSource,
                1, "someValueThatWillNeverBeSet");
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    /**
	 * 
	 */
    public void testIsReadOnlyObjectInt() {
        assertTrue(messageSourcePropertyResolver.isReadOnly(
                this.messageSource, 1));
    }

    /**
	 * 
	 */
    public void testGetTypeObjectInt() {
        assertEquals(String.class,
                messageSourcePropertyResolver.getType(this.messageSource,
                    1));
    }
}