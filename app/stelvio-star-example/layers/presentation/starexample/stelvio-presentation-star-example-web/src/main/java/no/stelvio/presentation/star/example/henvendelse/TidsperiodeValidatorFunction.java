package no.stelvio.presentation.star.example.henvendelse;

import org.springmodules.validation.valang.functions.AbstractFunction;
import org.springmodules.validation.valang.functions.Function;

import no.stelvio.domain.star.example.henvendelse.Tidsperiode;

/**
 * Usage: checkTidsperiode(Tidsperiode)
 * 
 * @author personff564022aedd
 */
public class TidsperiodeValidatorFunction extends AbstractFunction {
	public TidsperiodeValidatorFunction(Function[] args, int line, int column) {
		super(args, line, column);
		definedExactNumberOfArguments(1);
	}

	@Override
	protected Object doGetResult(Object target) throws Exception {
		return !Tidsperiode.IKKE_SATT.equals(getArguments()[0].getResult(target));
	}
}
