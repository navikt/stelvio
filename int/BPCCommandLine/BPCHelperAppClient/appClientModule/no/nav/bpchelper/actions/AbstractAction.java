package no.nav.bpchelper.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.bpchelper.adapters.BFMConnectionAdapter;
import no.nav.bpchelper.query.Criteria;

public abstract class AbstractAction implements Action {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final BFMConnectionAdapter bfmConnection;
    
    private Criteria criteria;

    public AbstractAction() {
	bfmConnection = BFMConnectionAdapter.getInstance();
    }
    
    public Criteria getCriteria() {
	return criteria;
    }

    public void setCriteria(Criteria criteria) {
	this.criteria = criteria;
    }
}