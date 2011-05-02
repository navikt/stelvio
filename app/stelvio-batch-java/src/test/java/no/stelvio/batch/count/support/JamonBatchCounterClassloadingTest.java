/**
 * 
 */
package no.stelvio.batch.count.support;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.stelvio.batch.count.support.CounterEvent.EventType;

import org.junit.Test;

/**
 * @author person47c121e3ccb5, BEKK
 *
 */
public class JamonBatchCounterClassloadingTest {
	
	@Test
	public void shouldBeAbleToLoadCounterClassWithoutJamonOnClasspath() throws Exception {
		URLClassLoader classloaderForTest = removeJamonFromClassloader();
		Object event = getCounterEvent(classloaderForTest);
		
		Class<?> jamonCounterClass = classloaderForTest.loadClass("no.stelvio.batch.count.support.JamonBatchCounter");
		Constructor<?> constructor = jamonCounterClass.getConstructor(Set.class);
		Set events = new HashSet<CounterEvent>();
		events.add(event);
		Object counter = constructor.newInstance(events);
		
		assertEquals("no.stelvio.batch.count.support.JamonBatchCounter", counter.getClass().getName());
	
	}

	private Object getCounterEvent(URLClassLoader classloaderForTest) throws ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		Class<?> counterEventClass = classloaderForTest.loadClass("no.stelvio.batch.count.support.CounterEvent");
		Constructor<?> counterEventConstructor = counterEventClass.getDeclaredConstructor(String.class, String.class, EventType.class);
		counterEventConstructor.setAccessible(true);
		Object event = counterEventConstructor.newInstance("test", "test", EventType.TECHNICAL);
		return event;
	}

	private URLClassLoader removeJamonFromClassloader() {
		URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
		List<URL> urlsForTest = new ArrayList<URL>();
		for (URL url : classLoader.getURLs()) {
			if (!url.getPath().contains("jamon")) {
				urlsForTest.add(url);
			}
		}
		URLClassLoader classloaderForTest = new URLClassLoader(urlsForTest.toArray(new URL[urlsForTest.size()]), null);
		return classloaderForTest;
	}
}
