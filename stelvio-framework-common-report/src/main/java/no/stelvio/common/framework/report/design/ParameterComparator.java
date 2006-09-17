package no.stelvio.common.framework.report.design;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * DOCUMENT ME!
 *
 * @author OHB2930
*/
public class ParameterComparator implements Comparator {
    private ArrayList priorityList;

    /**
     * Creates a new ParameterComparator object.
     *
     * @param parameterPriorityList DOCUMENT ME!
     */
    public ParameterComparator(ArrayList parameterPriorityList) {
        super();
        priorityList = parameterPriorityList;
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        if (!(o1 instanceof String) || !(o2 instanceof String)) {
            throw new IllegalArgumentException("ParameterComparator can only compare objects of type String");
        }

        String s1 = (String) o1;
        String s2 = (String) o2;

        int indexOfs1 = priorityList.indexOf(s1);
        int indexOfs2 = priorityList.indexOf(s2);                

		if (indexOfs1 == -1) {
			return -1;
		}

        if (indexOfs1 > indexOfs2) {
            return 1;
        } else if (indexOfs1 < indexOfs2) {
            return -1;
        }

        return 0;
    }
}
