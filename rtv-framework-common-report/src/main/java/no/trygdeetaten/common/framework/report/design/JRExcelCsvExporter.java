package no.trygdeetaten.common.framework.report.design;

import net.sf.jasperreports.engine.export.JRCsvExporter;

import org.apache.commons.lang.StringUtils;


/**
 * DOCUMENT ME!
 *
 * @author OHB2930
*/
public class JRExcelCsvExporter extends JRCsvExporter {
    /**
     * Creates a new JRExcelCsvExporter object.
     */
    public JRExcelCsvExporter() {
        super();
    }

    /* (non-Javadoc)
         * @see net.sf.jasperreports.engine.export.JRCsvExporter#prepareText(java.lang.String)
         */
    protected String prepareText(String source) {
        String str = null;

        if (source != null) {
            boolean putQuotes = false;
            boolean quirk = false;

            if ((source.indexOf(delimiter) >= 0)) {
                putQuotes = true;
            }

            if (source.startsWith("0")) {
                putQuotes = true;
                quirk = true;
            }

            StringBuffer sbuffer = new StringBuffer();
            String[] strArr = StringUtils.split(source, ",\"\n");
            String token = null;

            for (int i = 0; i < strArr.length; i++) {
                token = strArr[i];

                if (",".equals(token)) {
                    putQuotes = true;
                    sbuffer.append(",");
                } else if ("\"".equals(token)) {
                    putQuotes = true;
                    sbuffer.append("\"\"");
                } else if ("\n".equals(token)) {
                    sbuffer.append(" ");
                } else {
                    sbuffer.append(token);
                }
            }

            str = sbuffer.toString();

            if (putQuotes) {
                str = "\"" + str + "\"";
            }

            if (quirk) {
                str = "=" + str;
            }
        }

        return str;
    }
}
