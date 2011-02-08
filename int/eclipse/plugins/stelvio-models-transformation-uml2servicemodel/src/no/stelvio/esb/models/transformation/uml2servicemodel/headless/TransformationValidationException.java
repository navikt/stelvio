package no.stelvio.esb.models.transformation.uml2servicemodel.headless;

import java.util.List;

public class TransformationValidationException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3454685816511793814L;
	
	private List<String> validationErrors = null;
	
	public TransformationValidationException(List<String> validationErrors)
	{
		super("Transformation validation failed, see validation errors for more information.");
		this.validationErrors = validationErrors;
	}
	
	public List<String> getValidationErrors()
	{
		return validationErrors;
	}

}
