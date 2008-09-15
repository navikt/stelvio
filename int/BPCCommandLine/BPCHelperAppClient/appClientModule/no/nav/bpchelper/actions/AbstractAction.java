package no.nav.bpchelper.actions;

import java.util.Map;

public abstract class AbstractAction implements Action {
	private Map<String, String> filter;

	public Map<String, String> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, String> filter) {
		this.filter = filter;
	}
}