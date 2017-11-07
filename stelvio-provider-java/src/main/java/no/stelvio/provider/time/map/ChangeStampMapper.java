package no.stelvio.provider.time.map;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import no.stelvio.common.mapping.AbstractDozerMapper;
import no.stelvio.domain.time.ChangeStamp;
import no.stelvio.dto.exception.mapping.MappingDtoException;
import no.stelvio.dto.time.ChangeStampDto;

/**
 * Mapper used to map between the rich domain object {@link ChangeStamp} and the
 * Data Transfer Object {@link ChangeStampDto}.
 * 
 * @author person983601e0e117 (Accenture)
 */
public class ChangeStampMapper extends AbstractDozerMapper {

	private static final String CREATED_BY = "createdBy";
	private static final String UPDATED_BY = "updatedBy";
	private static final String CREATED_DATE = "createdDate";
	private static final String  UPDATED_DATE = "updatedDate";
	/**
	 * Maps from a <code>no.stelvio.domain.time.ChangeStamp</code> to a
	 * <code>no.stelvio.dto.time.ChangeStampDto</code>.
	 * 
	 * @param changeStamp
	 *            instance of a <code>no.stelvio.domain.time.ChangeStamp</code>
	 * @return instance of a <code>no.stelvio.dto.time.ChangeStampDto</code>
	 */
	public ChangeStampDto map(ChangeStamp changeStamp) {
		return dozerMapper.map(changeStamp, ChangeStampDto.class);
	}

	/**
	 * Maps from a <code>no.stelvio.dto.time.ChangeStampDto</code> to a
	 * <code>no.stelvio.domain.time.ChangeStamp</code>.
	 * 
	 * @param changeStampDto
	 *            instance of a <code>no.stelvio.dto.time.ChangeStampDto</code>
	 * @return instance of a <code>no.stelvio.domain.time.ChangeStamp</code>
	 */
	public ChangeStamp map(ChangeStampDto changeStampDto) {
		// Create ChangeStamp instance
		ChangeStamp cs = instansiateChangeStamp();

		// Set createdBy
		setField(cs, CREATED_BY, changeStampDto.getCreatedBy());

		// Set updatedBy
		setField(cs, UPDATED_BY, changeStampDto.getUpdatedBy());

		// Set createdDate
		setField(cs, CREATED_DATE, changeStampDto.getCreatedDate());

		// Set updatedDate
		setField(cs, UPDATED_DATE, changeStampDto.getUpdatedDate());

		return cs;
	}

	/**
	 * Instantiates a ChangeStamp.
	 * 
	 * @param changeStampReference
	 * @return a new ChangeStamp
	 */
	private ChangeStamp instansiateChangeStamp() {
		Constructor<ChangeStamp> csConstructor;
		try {
			ChangeStamp cs;
			csConstructor = ChangeStamp.class.getDeclaredConstructor();
			csConstructor.setAccessible(true);
			cs = (ChangeStamp) csConstructor.newInstance();
			return cs;
		} catch (SecurityException e) {
			throw createMappingDtoException(e);
		} catch (NoSuchMethodException e) {
			throw createMappingDtoException(e);
		} catch (IllegalArgumentException e) {
			throw createMappingDtoException(e);
		} catch (InstantiationException e) {
			throw createMappingDtoException(e);
		} catch (IllegalAccessException e) {
			throw createMappingDtoException(e);
		} catch (InvocationTargetException e) {
			throw createMappingDtoException(e);
		}
	}

	/**
	 * Creates a new MappingDtoException with a message.
	 * 
	 * @param e exception
	 * @return exception
	 */
	private MappingDtoException createMappingDtoException(Exception e) {
		return new MappingDtoException("Exception occured while mapping from '" + ChangeStampDto.class.getName() + "' to '"
				+ ChangeStamp.class.getName() + "'", e);
	}
	
	/**
	 * Sets the value of a ChangeStamp isntance's field. This ignores exceptions and prints
	 * a stack trace. 
	 * 
	 * @param changeStampInstance
	 *            an instance of ChangeStamp
	 * @param fieldName
	 *            the name of the field to set as a String literal
	 * @param value
	 *            the value to be set
	 */
	private void setField(Object changeStampInstance, String fieldName, Object value) {
		try {
			Field field = ChangeStamp.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(changeStampInstance, value);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

}
