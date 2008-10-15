package no.nav.bpchelper.cmdoptions;

import no.nav.bpchelper.actions.AbstractAction;
import no.nav.bpchelper.actions.ReportAction;
import no.nav.bpchelper.actions.StatusAction;
import no.nav.bpchelper.actions.DeleteAction;

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

	};

	public abstract AbstractAction getAction();
}
