package no.stelvio.dto.exception.mapping;

import no.stelvio.dto.exception.FunctionalUnrecoverableDtoException;

public abstract class AbstractMappingDtoException extends
		FunctionalUnrecoverableDtoException {

	public AbstractMappingDtoException(Object[] arguments) {
		super(arguments);
	}

	public AbstractMappingDtoException(Throwable cause, Object[] arguments) {
		super(cause, arguments);
	}

}
