package no.nav.common.framework.report.util;

import java.util.ArrayList;
import java.util.List;


/**
 * INSERT COMMENT
 *
 * @author Ole-Kristian Hagen, Ementor
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class StringUtils {
    private static final String STRING_BLANK = "";

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object[] splitNumAlpha(String str) {
        List result = new ArrayList();

        StringBuffer num;
        StringBuffer alpha;

        int strLength = str.length();
        char ch;

        for (int i = 0; i < strLength; i++) {
            ch = str.charAt(i);

            if (Character.isDigit(ch)) {
                num = new StringBuffer();

                int j = i;

                for (; (j < strLength); j++) {
                    ch = str.charAt(j);

                    if (Character.isDigit(ch)) {
                        num.append(ch);
                    } else {
                        j--;
                        break;
                    }
                }

                i = j;

                result.add(Integer.valueOf(num.toString()));
            } else {
                alpha = new StringBuffer();

                int j = i;

                for (; (j < strLength); j++) {
                    ch = str.charAt(j);

                    if (Character.isLetter(ch)) {
                        alpha.append(ch);
                    } else {
                        j--;
                        break;
                    }
                }

                i = j;

                result.add(alpha.toString());
            }
        }

        return (Object[]) result.toArray();
    }

    /**
     * DOCUMENT ME!
     *
     * @param strArr DOCUMENT ME!
     * @param asSQLText DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toString(String[] strArr, boolean asSQLText) {
        if ((strArr != null) && (strArr.length > 0)) {
            StringBuffer buf = new StringBuffer();

            for (int j = 0; j < strArr.length; j++) {
                if (j > 0) {
                    buf.append(',');
                }

                if (asSQLText) {
                    buf.append("'").append(nullAsBlank(strArr[j])).append("'");
                } else {
                    buf.append(nullAsBlank(strArr[j]));
                }
            }

            return buf.toString();
        } else {
            return "";
        }
    }

    private static String nullAsBlank(String str) {
        if (str == null) {
            return STRING_BLANK;
        } else {
            return str;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param strArr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toString(String[] strArr) {
        return toString(strArr, false);
    }
}
