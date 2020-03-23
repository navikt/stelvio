package no.stelvio.presentation.web.tag;

import org.apache.myfaces.view.facelets.tag.AbstractTagLibrary;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * JsfCoreLibrary is an example for IBM developerWorks (c).
 * 
 */
public final class JsfCoreLibrary extends AbstractTagLibrary {
	/** Namespace used to import this library in Facelets pages. */
	public static final String NAMESPACE = "http://www.nav.no/jsf/core";

	/** Current instance of library. */
	public static final JsfCoreLibrary INSTANCE = new JsfCoreLibrary();

	/**
	 * Creates a new JstlCoreLibrary object.
	 * 
	 */
	public JsfCoreLibrary() {
		super(NAMESPACE);

		try {
			Method[] methods = JsfFunctions.class.getMethods();

			for (int i = 0; i < methods.length; i++) {
				if (Modifier.isStatic(methods[i].getModifiers())) {
					this.addFunction(methods[i].getName(), methods[i]);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
