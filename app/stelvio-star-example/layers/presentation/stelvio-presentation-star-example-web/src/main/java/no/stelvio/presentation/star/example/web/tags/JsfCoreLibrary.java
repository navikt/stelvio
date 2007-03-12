package no.stelvio.presentation.star.example.web.tags;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.sun.facelets.tag.AbstractTagLibrary;


/**
 * JsfCoreLibrary is an example for IBM developerWorks (c).
 * @author Rick Hightower from ArcMind Inc. http://www.arc-mind.com
 * @todo clean up code
 */
public final class JsfCoreLibrary extends AbstractTagLibrary {
    /** Namespace used to import this library in Facelets pages.  */
    public static final String NAMESPACE = "http://www.stelvio.no/jsf/core";

    /**  Current instance of library. */
    public static final JsfCoreLibrary INSTANCE = new JsfCoreLibrary();

    /**
     * Creates a new JstlCoreLibrary object.
     */
    public JsfCoreLibrary() {
        super(NAMESPACE);

        try {
            Method[] methods = JsfFunctions.class.getMethods();

	        for (Method method : methods) {
		        if (Modifier.isStatic(method.getModifiers())) {
			        this.addFunction(method.getName(), method);
		        }
	        }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
