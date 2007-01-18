package no.nav.service.pensjon.person.exception;

import no.stelvio.common.error.FunctionalUnrecoverableException;

public class PersonNotFoundException extends FunctionalUnrecoverableException {
	private static final long serialVersionUID = 1L;

	public PersonNotFoundException(String id) {
		super(id);
	}

	public PersonNotFoundException(Throwable cause, String id) {
		super(cause, id);
	}

	protected String messageTemplate(int numArgs) {
		return "person ble ikke funnet: {0}";
	}
}