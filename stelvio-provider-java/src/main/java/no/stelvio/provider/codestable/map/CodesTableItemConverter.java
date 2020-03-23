package no.stelvio.provider.codestable.map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.dto.codestable.AbstractCodesTableItemDto;

/**
 * Custom converter implementation for converting <code>CodesTableItem</code>s.
 * 
 * @version $Id$
 */
public class CodesTableItemConverter extends AbstractCodesTableItemConverter {

	private final Log log = LogFactory.getLog(CodesTableItemConverter.class);

	@Override
	public Object convert(Object destination, Object source, Class<?> destClass, Class<?> sourceClass) {
		if (log.isDebugEnabled()) {
			log.debug("CodesTableItemConverter convert called with : destination=" + destination + ", source=" + source
					+ ", destClass=" + destClass + ", sourceClass=" + sourceClass);
		}
		if (source == null) {
			return null;
		}
		if (source instanceof AbstractCodesTableItemDto) {
			AbstractCodesTableItemDto dto = (AbstractCodesTableItemDto) source;
			CodesTable<? extends CodesTableItem<? extends Enum, ?>, ? extends Enum, ?> codesTable = getCodesTableManager()
					.getCodesTable((Class)destClass);
			return codesTable.getCodesTableItem(dto.getCode());
		} else if (source instanceof AbstractCodesTableItem) {
			AbstractCodesTableItem<? extends Enum, ?> item = (AbstractCodesTableItem<? extends Enum, ?>) source;
			AbstractCodesTableItemDto dto;
			try {
				dto = (AbstractCodesTableItemDto) destClass.newInstance();
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
