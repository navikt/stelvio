package no.nav.maven.plugins.descriptor.websphere;

import java.io.PrintStream;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;

public class WebSphereUtils {

	private WebSphereUtils() {}
	
	public static void printEList(EList list, String linePrefix, PrintStream out) {		
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			out.println(linePrefix + iter.next());			
		}
	}
}
