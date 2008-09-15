package no.nav.bpchelper.cmdoptions;

import no.nav.bpchelper.actions.AbstractAction;
import no.nav.bpchelper.actions.ReportAction;
import no.nav.bpchelper.actions.StatusAction;

public enum ActionOptionValues {
	STATUS {
		@Override
		public AbstractAction getAction() {
			return new StatusAction();
		}
		
	},
	
	REPORT {
		@Override
		public AbstractAction getAction() {
			return new ReportAction();
		}
		
	};
	
	public abstract AbstractAction getAction();
}
