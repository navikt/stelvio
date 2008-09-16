package no.nav.bpchelper.actions;

import no.nav.bpchelper.query.Criteria;


public abstract class AbstractAction implements Action {
	private Criteria criteria;

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}
}