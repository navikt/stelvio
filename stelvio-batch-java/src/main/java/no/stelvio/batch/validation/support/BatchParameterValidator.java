package no.stelvio.batch.validation.support;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import no.stelvio.batch.BatchBi;
import no.stelvio.batch.domain.BatchDO;
import no.stelvio.common.error.InvalidArgumentException;

/**
 * Validation logic class.
 * 
 * @author persondfa1fa919e87(Accenture)
 */
public class BatchParameterValidator {

	private BatchBi batch;

	/**
	 * Creates a new instance of BatchParameterValidator.
	 * 
	 * @param b
	 *            batch
	 */
	public BatchParameterValidator(BatchBi b) {
		this.batch = b;
	}

	/**
	 * Gathers the input parameters for the validation.
	 */
	public void validate() {
		HashSet<String> requiredParameters = new HashSet<>();
		HashSet<String> optionalParameters = new HashSet<>();

		if (batch.getRequiredParameters() != null) {
			requiredParameters.addAll(batch.getRequiredParameters());
		}
		if (batch.getOptionalParameters() != null) {
			optionalParameters.addAll(batch.getOptionalParameters());
		}
		if (batch.getRequiredParameters() != null || batch.getOptionalParameters() != null) {
			Set<String> actualParameters = getActualParameters();
			validate(actualParameters, requiredParameters, optionalParameters);
		}
	}

	/**
	 * Returns the parameters defined for the executin of the batch.
	 * 
	 * @return parameters
	 */
	Set<String> getActualParameters() {
		BatchDO batchDO = batch.readBatchParameters(batch.getBatchName(), batch.getSlice());
		Properties props = batch.fetchBatchProperties(batchDO);
		return (Set<String>) (Set) props.keySet();
	}

	/**
	 * The actual validation logic.
	 * 
	 * @param actualParameters
	 *            the parameters used for running the batch
	 * @param requiredParameters
	 *            the required parameters for running the batch
	 * @param optionalParameters
	 *            the optonal parameters for running the batch
	 */
	void validate(Set<String> actualParameters, HashSet<String> requiredParameters, HashSet<String> optionalParameters) {

		Set<String> allDefinedParameters = new HashSet<>();
		allDefinedParameters.addAll(requiredParameters);
		allDefinedParameters.addAll(optionalParameters);

		StringBuffer errorMessage = new StringBuffer();

		for (String parameter : actualParameters) {
			if (!allDefinedParameters.contains(parameter)) {
				errorMessage.append("The parameter: ").append(parameter).append(" is not a defined parameter for this batch. ");
			}
		}

		if (errorMessage.length() > 0) {
			errorMessage.append("List of defined parameters: ");
			for (String parameter : allDefinedParameters) {
				errorMessage.append(parameter).append(", ");
			}
			errorMessage.append(". ");
		}

		for (String parameter : requiredParameters) {
			if (!actualParameters.contains(parameter)) {
				errorMessage.append("The parameter: ").append(parameter).append(" is a required parameter for this batch. ");
			}
		}

		if (errorMessage.length() > 0) {
			throw new InvalidArgumentException(errorMessage.toString());
		}

	}
}
