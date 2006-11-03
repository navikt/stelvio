package no.stelvio.common.codestable;

import java.util.Date;
import java.util.Locale;

//TODO: implement errorhandling

/**
 * Interface defining functionality for retrieving codestables that has defined a period
 * of validity. 
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public interface CodesTablePeriodic<T extends CodesTableItemPeriodic> extends CodesTable {
	
	public String getDecode(Object code, Date date);
	public String getDecode(Object code, Locale locale, Date date) ;
	
}

