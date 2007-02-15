package no.stelvio.presentation.star.example.henvendelse;

import org.springmodules.validation.valang.functions.AbstractFunction;
import org.springmodules.validation.valang.functions.Function;

import no.stelvio.domain.star.example.henvendelse.Fagomrade;
import no.stelvio.domain.star.example.henvendelse.Spesifikasjon;

/**
 * Usage: checkParameters(Spesifikasjon, Fagomrade)
 * @author personff564022aedd
 */
public class SpesifikasjonValidatorFunction extends AbstractFunction {

	public SpesifikasjonValidatorFunction(Function[] args, int line, int column) {
		super(args, line, column);
		definedExactNumberOfArguments(2);
	}

	@Override
	protected Object doGetResult(Object target) throws Exception {
		
		Spesifikasjon spesifikasjon = (Spesifikasjon)getArguments()[0].getResult(target);
		Fagomrade fagomrade = (Fagomrade)getArguments()[1].getResult(target);
		if (Fagomrade.ALLE.equals(fagomrade)) {
			
			if(!Spesifikasjon.ANTALL.equals(spesifikasjon)) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

}
