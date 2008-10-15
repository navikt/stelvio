package no.nav.bpchelper.actions;

public interface Action {
	/**
	 * Execute action
	 * 
	 * @return the number of processes affected by the action
	 */
	int execute();
}
