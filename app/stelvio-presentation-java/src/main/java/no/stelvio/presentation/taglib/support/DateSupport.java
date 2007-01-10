package no.stelvio.presentation.taglib.support;

import org.apache.commons.lang.StringUtils;

/**
 * Helper methods for working with dates in taglibs.
 *
 * @author personf8e9850ed756
 * @version $Revision: $, $Date: $
 */
public class DateSupport {
	public static final String NON_BREAK_SPACE = "&nbsp;";

	public static String blankToNonBreakSpace(final String formattedDate) {
		return StringUtils.isBlank(formattedDate) ? NON_BREAK_SPACE : formattedDate;
	}
}
