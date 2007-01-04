package no.nav.presentation.pensjon.psak.henvendelse;

import no.nav.domain.pensjon.henvendelse.Tidsperiode;

import org.springmodules.validation.valang.functions.AbstractFunction;
import org.springmodules.validation.valang.functions.Function;

/**
 * Usage: checkTidsperiode(Tidsperiode)
 * @author personff564022aedd
 */
public class TidsperiodeValidatorFunction extends AbstractFunction {

	public TidsperiodeValidatorFunction(Function[] args, int line, int column) {
		super(args, line, column);
		definedExactNumberOfArguments(1);
	}

	@Override
	protected Object doGetResult(Object target) throws Exception {
		return Boolean.valueOf(!Tidsperiode.IKKE_SATT.equals((Tidsperiode)getArguments()[0].getResult(target)));
	}
}
