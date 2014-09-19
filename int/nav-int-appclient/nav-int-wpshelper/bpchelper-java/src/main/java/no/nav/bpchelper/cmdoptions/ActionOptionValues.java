package no.nav.bpchelper.cmdoptions;

import no.nav.bpchelper.actions.AbstractAction;
import no.nav.bpchelper.actions.ReportAction;
import no.nav.bpchelper.actions.ResumeAction;
import no.nav.bpchelper.actions.SamordningAction;
import no.nav.bpchelper.actions.StatusAction;
import no.nav.bpchelper.actions.DeleteAction;
import no.nav.bpchelper.actions.SuspendAction;

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

	},

	DELETE {
		@Override
		public AbstractAction getAction() {
			return new DeleteAction();
		}

	},
	
	SAMORDNING {
		@Override
		public AbstractAction getAction() {
			return new SamordningAction();
		}
	},
	
	SUSPEND {
		@Override
		public AbstractAction getAction() {
			return new SuspendAction();
		}
	},
	
	RESUME {
		@Override
		public AbstractAction getAction() {
			return new ResumeAction();
		}
	};
	
	

	public abstract AbstractAction getAction();
}
