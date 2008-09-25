package no.nav.bpchelper.cmdoptions;

import java.util.Properties;

import no.nav.bpchelper.actions.AbstractAction;
import no.nav.bpchelper.actions.ReportAction;
import no.nav.bpchelper.actions.StatusAction;
import no.nav.bpchelper.actions.TerminateAction;

public enum ActionOptionValues {
    STATUS {
	@Override
	public AbstractAction getAction(Properties properties) {
	    return new StatusAction(properties);
	}

    },

    REPORT {
	@Override
	public AbstractAction getAction(Properties properties) {
	    return new ReportAction(properties);
	}

    },
    
    TERMINATE {
	@Override
	public AbstractAction getAction(Properties properties) {
	    return new TerminateAction(properties);
	}

    };

    public abstract AbstractAction getAction(Properties properties);
}
