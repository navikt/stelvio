package no.nav.integration.framework.jca.service;

import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

import no.nav.common.framework.service.ServiceFailedException;
import no.nav.integration.framework.jca.cics.service.InteractionProperties;


/**
 * This interface is to be used when creating new mappers from
 * JCA provider record types.
 * 
 * @author person5b7fd84b3197, Accenture
 */
public interface RecordMapper {

	/**
	 * Converts a bean instance into a JCA RecordType.
	 *
	 * @param recordName the name of the configured record.
	 * @param interProps properties for configuroing the <code>InteractionSpec</code> for new CICS records.
	 * @param bean the bean to convert to a record type.
	 * @return The generated record type.
	 * @throws ServiceFailedException if conversion fails.
	 */
	Record classToRecord( String recordName, InteractionProperties interProps, Object bean ) throws ServiceFailedException;
	
	/**
	 * Converts a JCA RecordType into a bean instance.
	 *
	 * @param recordName the name of the configured record.
	 * @param interProps properties for configuroing the <code>InteractionSpec</code> for new CICS records.
	 * @param record the JCA RecordType.
	 * @return The generated bean instance.
	 * @throws ServiceFailedException if conversion fails.
	 */
	Object recordToClass( String recordName, InteractionProperties interProps, Record record ) throws ServiceFailedException;

	/**
	 * Creates an InteractionSpec for the spesific JCA ResourceAdapter.
	 *
	 * A class for configuring the InteractionSpec for new CICS records. Will be configured in the Spring configuration file for each copy book.
	 *
	 * @param interProps properties for configuroing the <code>InteractionSpec</code> for new CICS records.
	 * @return the created <code>InteractionSpec</code>.
	 * @throws ServiceFailedException if creation fails.
	 * @see InteractionProperties
	 * @see InteractionSpec
	 * @deprecated Use {@link #createInteractionSpec(InteractionProperties)}.
	 */
	InteractionSpec createInteraction(InteractionProperties interProps) throws ServiceFailedException;

	/**
	 * Creates an InteractionSpec for the spesific JCA ResourceAdapter.
	 *
	 * A class for configuring the InteractionSpec for new CICS records. Will be configured in the Spring configuration file for each copy book.
	 *
	 * @param interProps properties for configuroing the <code>InteractionSpec</code> for new CICS records.
	 * @return the created <code>InteractionSpec</code>.
	 * @throws ServiceFailedException if creation fails.
	 * @see InteractionProperties
	 * @see InteractionSpec
	 */
	InteractionSpec createInteractionSpec(InteractionProperties interProps) throws ServiceFailedException;
}
