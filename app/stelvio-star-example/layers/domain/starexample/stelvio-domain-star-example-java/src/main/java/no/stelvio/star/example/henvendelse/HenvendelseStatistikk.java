package no.nav.domain.pensjon.henvendelse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Data object containing statistics for Henvendelse in a results table.
 * The implementation contains convenience methods for adding and reading rows in this table.
 * It is assumed that the results table includes labels in the first row and column.
 * 
 * TODO: i18n må gjøres for labels på server eller klient side.
 * 
 * @author personff564022aedd
 */
public class HenvendelseStatistikk {

	// TODO: i18n på labels..
	private static final String SUM_LABEL = "Sum";
	private static final String UKE_PREFIX = "Uke ";
	
	public static final boolean TIDSENHET_UKE = true;
	public static final boolean TIDSENHET_DAG = false;	
	
	// 2d "array" that contains the statistics results including labels 
	private List<StatistikkRad> resultRows = new ArrayList<StatistikkRad>();

	private StatistikkRad header;
	
	// todo fjerne denne...
	private int noOfColumns;
	
	/* if true, each number in tabell is per week (uke), else per day (dag) */
	private boolean tidsenhetUke;
	
	private HenvendelseStatistikk() {
		System.out.println("empty constructor called..");
	}
	
	/**
	 * 
	 * @param antallKolonner
	 * @param antallRader
	 */
	public HenvendelseStatistikk(int antallKolonner, boolean tidsenhetUke) {
		if (antallKolonner < 1) {
			throw new IllegalArgumentException("Invalid dimension for result table");
		}
		this.noOfColumns = antallKolonner;
		this.tidsenhetUke = tidsenhetUke;
		
		Calendar now = Calendar.getInstance();
		now.get(Calendar.WEEK_OF_YEAR);
		
		// ---  init column labels and add to result  ---
		// the first column header is a blank
		String[] labels = new String[noOfColumns];
		labels[0] = " ";
		// the next headers contain a date or week number
		for (int i = 1; i < noOfColumns - 1; i++ ) {			
			// generate a label and adjust calendar for the next
			if (isTidsenhetUke()) {
				labels[i] = UKE_PREFIX + (now.get(Calendar.WEEK_OF_YEAR));
				now.add(Calendar.WEEK_OF_YEAR, -1);
			} else {
				// TODO: finn bedre formateringsmetode.. 
				String dag = now.get(Calendar.DAY_OF_MONTH) > 9 ? "" + now.get(Calendar.DAY_OF_MONTH) : "0" + now.get(Calendar.DAY_OF_MONTH);
				String maaned = now.get(Calendar.MONTH) + 1 > 9 ? "." + (now.get(Calendar.MONTH) + 1) : ".0" + (now.get(Calendar.MONTH) + 1);
				labels[i] = dag + maaned + "." + now.get(Calendar.YEAR);
				now.add(Calendar.DATE, -1);
			}
		}
		// the final column header contains "Sum"
		labels[antallKolonner - 1] = SUM_LABEL;
		
		// Set row containing labels
		header = new StatistikkRad(labels, null);
		
		addRow(labels);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isTidsenhetUke() {
		return tidsenhetUke;
	}

	/**
	 * 
	 * @param entries
	 */
	public void addRow(String[] entries) {
		if (entries == null || entries.length != noOfColumns) {
			throw new IllegalArgumentException("Entries must fit table dimension, required noOfColoumns is: " + noOfColumns);
		}
		resultRows.add(new StatistikkRad(entries, this));
	}
	
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		if (resultRows != null) {
			for(StatistikkRad row : resultRows) {
				for(String entry : row.getColumns()) {
					stringBuffer.append(entry);
					stringBuffer.append("\t");
				}
			}
		}
		return stringBuffer.toString();
	}

	public StatistikkRad getColumnHeaders() {
		return resultRows.get(0);
	}
	
	public List<StatistikkRad> getResultRows() {
		return resultRows.subList(1, resultRows.size());
	}

	public StatistikkRad getHeader() {
		return header;
	}
	
	/**
	 * @return true if noOfColumns is six, else false
	 */
	public boolean getRenderSixColumns() {
		return noOfColumns == 6;
	}
	
	/**
	 * @return true if noOfColumns is seven, else false
	 */
	public boolean getRenderSevenColumns() {
		return noOfColumns == 7;
	}
}
