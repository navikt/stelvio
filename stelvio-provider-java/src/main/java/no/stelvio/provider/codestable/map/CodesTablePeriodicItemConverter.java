package no.stelvio.provider.codestable.map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;
import no.stelvio.dto.codestable.AbstractCodesTablePeriodicItemDto;

/**
 * Custom converter implementation for converting <code>CodesPeriodicTableItem</code>s.
 * 
 * @author person19fa65691a36
 * @version $Id$
 */
public class CodesTablePeriodicItemConverter extends AbstractCodesTableItemConverter {

	private final Log log = LogFactory.getLog(CodesTablePeriodicItemConverter.class);

	@Override
	public Object convert(Object destination, Object source, Class<?> destClass, Class<?> sourceClass) {
		if (log.isDebugEnabled()) {
			log.debug("CodesTablePeriodicItemConverter convert called with : destination=" + destination + ", source=" + source
					+ ", destClass=" + destClass + ", sourceClass=" + sourceClass);
		}
		if (source == null) {
			return null;
		}
		if (source instanceof AbstractCodesTablePeriodicItemDto) {
			AbstractCodesTablePeriodicItemDto dto = (AbstractCodesTablePeriodicItemDto) source;
			CodesTablePeriodic<? extends CodesTablePeriodicItem<? extends Enum, ?>, ? extends Enum, ?> codesTable 
					= getCodesTableManager().getCodesTablePeriodic((Class)destClass);
			return codesTable.getCodesTableItem(dto.getCode());
		} else if (source instanceof AbstractCodesTablePeriodicItem) {
			AbstractCodesTablePeriodicItem<? extends Enum, ?> item = (AbstractCodesTablePeriodicItem<? extends Enum, ?>) source;
			AbstractCodesTablePeriodicItemDto dto;
			try {
				dto = (AbstractCodesTablePeriodicItemDto) destClass.newInstance();
				dto.setCode(item.getCodeAsString());
				return dto;
			} catch (IllegalAccessException | InstantiationException e) {
				return null;
			}
		} else {
			return null;
		}
	}
}
