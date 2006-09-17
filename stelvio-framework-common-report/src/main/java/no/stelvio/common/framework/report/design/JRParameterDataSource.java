package no.stelvio.common.framework.report.design;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import no.stelvio.common.framework.report.util.StringUtils;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 2444 $, $Date: 2005-09-01 10:47:54 +0200 (Thu, 01 Sep 2005) $
*/
public class JRParameterDataSource implements JRDataSource {
    private static final String PARAM_NAME = "ParamName";
    private static final String PARAM_VALUE = "ParamValue";
    private Map parameterMap;
    private Iterator parameterIterator;
    private Map.Entry entry;
    private Map propertyDisplayNames;
    private Map valueDisplayNames;

    /**
     * Creates a new JRParameterDataSource object.
     *
     * @param parameterMap DOCUMENT ME!
     * @param propertyDisplayNames DOCUMENT ME!
     */
    public JRParameterDataSource(Map parameterMap, Map propertyDisplayNames) {
        this.parameterMap = parameterMap;
        this.propertyDisplayNames = propertyDisplayNames;

        if (parameterMap != null) {
            this.parameterIterator = this.parameterMap.entrySet().iterator();
        }
    }

    /**
     * Creates a new JRParameterDataSource object.
     *
     * @param parameterMap DOCUMENT ME!
     * @param propertyDisplayNames DOCUMENT ME!
     * @param valueDisplayNames DOCUMENT ME!
     */
    public JRParameterDataSource(Map parameterMap, Map propertyDisplayNames, Map valueDisplayNames) {
        this.parameterMap = parameterMap;
        this.propertyDisplayNames = propertyDisplayNames;
        this.valueDisplayNames = valueDisplayNames;

        if (parameterMap != null) {
            this.parameterIterator = this.parameterMap.entrySet().iterator();
        }
    }

    /**
     * Creates a new JRParameterDataSource object.
     *
     * @param parameterMap DOCUMENT ME!
     * @param propertyDisplayNames DOCUMENT ME!
     * @param valueDisplayNames DOCUMENT ME!
     * @param parameterComparator DOCUMENT ME!
     */
    public JRParameterDataSource(Map parameterMap, Map propertyDisplayNames, Map valueDisplayNames, Comparator parameterComparator) {
        this.parameterMap = new TreeMap(parameterComparator);
        this.parameterMap.putAll(parameterMap);

        this.propertyDisplayNames = propertyDisplayNames;
        this.valueDisplayNames = valueDisplayNames;

        if (parameterMap != null) {
            this.parameterIterator = this.parameterMap.entrySet().iterator();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     *
     * @throws JRException
     */
    public boolean next() throws JRException {
        if ((this.parameterIterator != null) && this.parameterIterator.hasNext()) {
            this.entry = (Map.Entry) this.parameterIterator.next();

            // Skip properties with no displayname
            Object objValue = this.entry.getValue();

            if ((this.propertyDisplayNames.get(this.entry.getKey()) == null) || (objValue == null)) {
                return next();
            } else if (objValue != null) {
                String value = StringUtils.toString((String[]) objValue);

                if (value.length() == 0) {
                    return next();
                }
            }

            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param field
     *
     * @return
     *
     * @throws JRException
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Object getFieldValue(JRField field) throws JRException {
        if (this.entry != null) {
            if (field.getName().equals(PARAM_NAME)) {
                return this.propertyDisplayNames.get((String) this.entry.getKey());
            } else if (field.getName().equals(PARAM_VALUE)) {
                Object value = this.entry.getValue();
                String[] displayNamesArr;
                String mappedValue;

                if (value.getClass().isArray()) {
                    Object[] objArr = (Object[]) value;
                    displayNamesArr = new String[objArr.length];

                    String orgValue;

                    for (int i = 0; i < objArr.length; i++) {
                        orgValue = String.valueOf(objArr[i]);

                        if (this.valueDisplayNames != null) {
                            mappedValue = (String) this.valueDisplayNames.get(this.entry.getKey() + "." + orgValue);

                            if (mappedValue != null) {
                                displayNamesArr[i] = mappedValue;
                            } else {
                                displayNamesArr[i] = orgValue;
                            }
                        } else {
                            displayNamesArr[i] = orgValue;
                        }
                    }

                    value = StringUtils.toString(displayNamesArr);
                } else {
                    if (this.valueDisplayNames != null) {
                        mappedValue = (String) this.valueDisplayNames.get(this.entry.getKey() + "." + String.valueOf(value));

                        if (mappedValue != null) {
                            value = mappedValue;
                        }
                    }
                }

                return value;
            } else {
                throw new IllegalArgumentException("The datasource has two fields: ParamName and ParamValue");
            }
        }

        return "";
    }
}
