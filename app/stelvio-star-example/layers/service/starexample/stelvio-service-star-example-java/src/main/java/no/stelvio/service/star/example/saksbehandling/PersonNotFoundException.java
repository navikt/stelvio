package no.stelvio.service.star.example.saksbehandling;

import no.stelvio.common.error.FunctionalUnrecoverableException;
import no.stelvio.domain.person.Pid;

public class PersonNotFoundException extends FunctionalUnrecoverableException {
	private static final long serialVersionUID = -4349385568920627868L;

	public PersonNotFoundException(Pid pid) {
		super(pid);
	}

	public PersonNotFoundException(Throwable cause, Pid pid) {
		super(cause, pid);
	}

	protected String messageTemplate(final int numArgs) {
		return "Did not find the person with pid: {0}";
	}
}
