package no.stelvio.web.framework.taglib.support;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Helper methods for working with amounts in JSP-pages.
 *
 * @author personf8e9850ed756
 * @version $Revision: $, $Date: $
 */
public class AmountSupport {
	private static final String NON_BREAK_SPACE = "&nbsp;";
	private static final DecimalFormat DF = new DecimalFormat(",###");
	private static final Pattern DETECT_FRACTION = Pattern.compile("[,.]");
	private static final Pattern DETECT_GROUPING_SEPARATOR =
			Pattern.compile(String.valueOf(DF.getDecimalFormatSymbols().getGroupingSeparator()));

	public static String formatAmount(final Object value) {
		Object newValue = value;

		if (newValue instanceof String) {
			if (StringUtils.isBlank((String) newValue)) {
				return NON_BREAK_SPACE;
			}

			// Makes an array of strings from the string newValue split on ',' and '.'
			String[] matches = DETECT_FRACTION.split((CharSequence) newValue);

			newValue = new Integer(matches[0]);
		}

		if (newValue instanceof Number) {
			final String formattedValue = DF.format(newValue);

			// Replaces all thousands grouping separators with '&nbsp;'
			return DETECT_GROUPING_SEPARATOR.matcher(formattedValue).replaceAll(NON_BREAK_SPACE);
		} else if (null == newValue) {
			return NON_BREAK_SPACE;
		} else {
			return newValue.toString();
		}
	}
}
