package no.nav.bpchelper.cmdoptions;


import no.nav.bpchelper.actions.AbstractAction;
import no.nav.bpchelper.actions.ReportAction;
import no.nav.bpchelper.actions.RestartAction;
import no.nav.bpchelper.actions.StatusAction;
import no.nav.bpchelper.actions.TerminateAction;

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

	TERMINATE {
		@Override
		public AbstractAction getAction() {
			return new TerminateAction();
		}

	},
	
	RESTART {
		@Override
		public AbstractAction getAction() {
			return new RestartAction();
		}
	};

	public abstract AbstractAction getAction();
}
