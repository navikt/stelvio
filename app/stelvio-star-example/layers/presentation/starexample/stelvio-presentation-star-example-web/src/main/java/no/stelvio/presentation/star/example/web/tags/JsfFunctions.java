package no.stelvio.presentation.star.example.web.tags;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;


/**
 * Functions to aid developing JSF applications.
 */
public final class JsfFunctions {
	/**
	 * Stops creation of a new JsfFunctions object.
	 */
	private JsfFunctions() {
	}

	
	public static boolean hasMessage( String form, String id )
	{
		boolean tja = false;
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		//System.out.println( "hm = " + hm );
		///*
		String clientId = form + ":" + id;
		System.out.println( "client id = " + clientId );
		tja = context.getMessages( clientId ).hasNext();
		/*
		System.out.println( "kommer dette ut? context.getMessages( hm ).hasNext() = " + context.getMessages( hm ).hasNext() );
		*/
		return tja;
	}
	
	public static boolean hasMessages( String form, String id )
	{
		boolean tja = false;
		
		if( hasMessage( form, id ) )
		{
			return false;
		}
		FacesContext context = FacesContext.getCurrentInstance();
		
		tja = context.getMessages().hasNext();

		return tja;
	}
	
	/**
	 * Get the field label.
	 *
	 * @param fieldName
	 *            fieldName
	 * @param formId
	 *            form id
	 * @return Message from the Message Source.
	 */
	public static String getFieldLabel(final String fieldName,
			final String formId) {

		Locale locale = FacesContext.getCurrentInstance().getViewRoot()
				.getLocale();
		String bundleName = FacesContext.getCurrentInstance().getApplication()
				.getMessageBundle();

		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale,
				getClassLoader());

		/** Look for formId.fieldName, e.g., EmployeeForm.firstName. */

		String label = null;
		try {
			label = bundle.getString(formId + fieldName);
			return label;
		} catch (MissingResourceException e) {
			// do nothing on purpose.
		}

		try {
			/** Look for just fieldName, e.g., firstName. */
			label = bundle.getString(fieldName);
		} catch (MissingResourceException e) {
			/**
			 * Convert fieldName, e.g., firstName automatically becomes First
			 * Name.
			 */
			label = generateLabelValue(fieldName);
		}

		return label;

	}

	private static ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader == null) {
			return JsfFunctions.class.getClassLoader();
		}
		return classLoader;
	}

	/**
	 * Generate the field. Transforms firstName into First Name. This allows
	 * reasonable defaults for labels.
	 *
	 * @param fieldName
	 *            fieldName
	 *
	 * @return generated label name.
	 */
	public static String generateLabelValue(final String fieldName) {
		StringBuffer buffer = new StringBuffer(fieldName.length() * 2);
		char[] chars = fieldName.toCharArray();

		/* Change firstName to First Name. */
		for (int index = 0; index < chars.length; index++) {
			char cchar = chars[index];

			/* Make the first character uppercase. */
			if (index == 0) {
				cchar = Character.toUpperCase(cchar);
				buffer.append(cchar);

				continue;
			}

			/* Look for an uppercase character, if found add a space. */
			if (Character.isUpperCase(cchar)) {
				buffer.append(' ');
				buffer.append(cchar);

				continue;
			}

			buffer.append(cchar);
		}

		return buffer.toString();
	}
}
