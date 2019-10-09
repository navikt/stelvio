package no.stelvio.presentation.jsf.context;

import org.springframework.validation.FieldError;

/**
 * FieldInListError handles Field errors when occuring in for instance a data table.
 * 
 * @author persone38597605f58 (Capgemini)
 * @author person7c5197dbb870 (Capgemini)
 */
public class FieldInListError extends FieldError {

	/** Seperator. */
	public static final String SEPERATOR = ":";

	/** Serial UID. */
	private static final long serialVersionUID = 7030378410304196479L;

	/**
	 * List index represents the index of the list having the object with the error. The index starts on 0, as any normal list
	 * in java.
	 */
	private int listIndex;

	/**
	 * Constructor setting all super's fields and the listIndex according to the affected element in a list of objects.
	 * 
	 * @param objectName
	 *            the name of the affected object
	 * @param field
	 *            the affected field of the object
	 * @param rejectedValue
	 *            the rejected field value
	 * @param bindingFailure
	 *            whether this error represents a binding failure (like a type mismatch); else, it is a validation failure
	 * @param codes
	 *            the codes to be used to resolve this message
	 * @param arguments
	 *            the array of arguments to be used to resolve this message
	 * @param defaultMessage
	 *            the default message to be used to resolve this message
	 * @param listIndex
	 *            the index of the element in the list where the failure has occured
	 */
	public FieldInListError(String objectName, String field, Object rejectedValue, boolean bindingFailure, String[] codes,
			Object[] arguments, String defaultMessage, int listIndex) {
		super(objectName, field, rejectedValue, bindingFailure, codes, arguments, defaultMessage);
		this.listIndex = listIndex;
	}

	/**
	 * Constructor setting the most common fields of super and the listIndex according to the affected element in a list of
	 * objects.
	 * 
	 * @param objectName
	 *            the name of the affected object. This parameter could for instance be 'this.getClass().getName()' if the
	 *            affected object was the executing class
	 * @param field
	 *            the affected field of the object (i.e 'employee')
	 * @param code
	 *            the code to be used to resolve this message. (i.e 'This field cannot be empty')
	 * @param arguments
	 *            the array of arguments to be used to resolve this message
	 * @param listIndex
	 *            the index of the element in the list where the failure has occured
	 */
	public FieldInListError(String objectName, String field, String code, Object[] arguments, int listIndex) {
		this(objectName, field, null, false, new String[] { code }, arguments, null, listIndex);
	}

	/**
	 * @return the listIndex
	 */
	public int getListIndex() {
		return listIndex;
	}

	/**
	 * {@inheritDoc}
	 * @see org.springframework.validation.FieldError#toString()
	 */
	public String toString() {
		return super.toString() + "\\\' on listIndex \\\'" + this.listIndex;
	}

	/**
	 * {@inheritDoc}
	 * @see org.springframework.validation.FieldError#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		return this == other || super.equals(other);
	}

	/**
	 * {@inheritDoc}
	 * @see org.springframework.validation.FieldError#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
