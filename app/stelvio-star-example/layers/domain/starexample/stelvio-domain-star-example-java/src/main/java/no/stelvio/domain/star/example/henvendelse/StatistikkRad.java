package no.stelvio.domain.star.example.henvendelse;


/**
 * Represents a row in the HenvendelseStatistikk result table.
 * 
 * @author personff564022aedd
 */
public class StatistikkRad {

//	private List<StatistikkKolonne> columns;
	
	private String[] columnValues;
	
	// parent is a special row where the fields represents column labels
	// it is added to easy rendering of result table
	private HenvendelseStatistikk parent;
	
	public StatistikkRad(String[] columns, HenvendelseStatistikk parent) {
		this.columnValues = columns;
		this.parent = parent;
//		this.columns = new ArrayList<StatistikkKolonne>();
//		for (String s : columns) {
//			this.columns.add(new StatistikkKolonne(s));
//		}
	}
	
	public String[] getColumns() {
		System.out.println("get columns er kalt..");
		return columnValues;
	}
	
	public String getField1() {
		return columnValues.length > 0 ? columnValues[0] : null; 
	}
	public String getField2() {
		return columnValues.length > 1 ? columnValues[1] : null; 
	}
	public String getField3() {
		return columnValues.length > 2 ? columnValues[2] : null; 
	}
	public String getField4() {
		return columnValues.length > 3 ? columnValues[3] : null; 
	}
	public String getField5() {
		return columnValues.length > 4 ? columnValues[4] : null; 
	}
	public String getField6() {
		return columnValues.length > 5 ? columnValues[5] : null; 
	}
	public String getField7() {
		return columnValues.length > 6 ? columnValues[6] : null; 
	}

	public String getHeader1() {
		System.out.println("header1!");
		return parent != null && parent.getHeader() != null ? parent.getHeader().getField1() : null; 
	}
	public String getHeader2() {
		return parent != null && parent.getHeader() != null ? parent.getHeader().getField2() : null; 
	}
	public String getHeader3() {
		return parent != null && parent.getHeader() != null ? parent.getHeader().getField3() : null; 
	}
	public String getHeader4() {
		return parent != null && parent.getHeader() != null ? parent.getHeader().getField4() : null; 
	}
	public String getHeader5() {
		return parent != null && parent.getHeader() != null ? parent.getHeader().getField5() : null; 
	}
	public String getHeader6() {
		return parent != null && parent.getHeader() != null ? parent.getHeader().getField6() : null; 
	}
	public String getHeader7() {
		return parent != null && parent.getHeader() != null ? parent.getHeader().getField7() : null; 
	} 
	
	public String getRenderSeventhColumn() {
		return columnValues.length == 7 ? "true" : "false";
	}
}
