/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.validate;

import org.apache.commons.lang.StringUtils;
import org.springmodules.validation.valang.functions.AbstractFunction;
import org.springmodules.validation.valang.functions.Function;

/**
 * @author person4f9bc5bd17cc
 *
 */
public class Modulus11Function extends AbstractFunction {
	public Modulus11Function(Function[] args, int line, int column) {
		super(args, line, column);
		definedExactNumberOfArguments(1);
	}
	
	@Override
	protected Object doGetResult(Object target) throws Exception {
		Object value = getArguments()[0].getResult(target);
		String fnr = value.toString();
		
		if (StringUtils.isEmpty(fnr) ||
				fnr.length() != 11 ||
				!StringUtils.isNumeric(fnr)) {
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
}