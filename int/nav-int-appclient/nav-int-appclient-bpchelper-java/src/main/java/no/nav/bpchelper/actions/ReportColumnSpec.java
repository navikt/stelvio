package no.nav.bpchelper.actions;

public interface ReportColumnSpec<T> {
	String getLabel();
	
	String getValue(T instance);
}
