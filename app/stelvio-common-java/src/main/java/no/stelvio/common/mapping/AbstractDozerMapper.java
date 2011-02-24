package no.stelvio.common.mapping;

import org.dozer.Mapper;

/**
 * Parent class for dozer mappers. Holds the reference to the actual
 * mapper implementation, normally injected. 
 * 
 * @author person19fa65691a36 (Accenture)
 */
public abstract class AbstractDozerMapper {

	/** The mapper. */
	protected Mapper dozerMapper;
	
	/**
	 * Gets the dozer mapper.
	 * @return dozerMapper - a dozer mapper implementation.
	 */
	public Mapper getDozerMapper() {
		return dozerMapper;
	}
	
	/**
	 * Sets the dozer mapper.
	 * @param dozerMapper - a dozer mapper implementation.
	 */
	public void setDozerMapper(Mapper dozerMapper) {
		this.dozerMapper = dozerMapper;
	}

}
