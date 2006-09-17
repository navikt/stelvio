package no.stelvio.integration.framework.hibernate.formater;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Formatting between Cobol (i.e. Java String) and Java Long values.
 *
 * @author person5b7fd84b3197, Accenture
 * @version $Id: LongFormater.java 2876 2006-04-25 11:53:29Z psa2920 $
 */
public class LongFormater implements Formater {
	/** Long value for the null String. */
	private static final Long NULL = new Long(0);
	/** Lookup-tabell for å slå opp negative koder/dekoder */
	private static final Map NEGLOOKUPTABLE = new HashMap(10);
	/** Lookup-tabell for å slå opp positive koder/dekoder */
	private static final Map POSLOOKUPTABLE = new HashMap(10);

	/** Logger to be used for writing <i>TRACE</i> and <i>DEBUG</i> messages. */
	private Log log = LogFactory.getLog(this.getClass());

	// Setter opp lookup-tabellene.
	static {
		// Fra tall til kode
		POSLOOKUPTABLE.put(new Character('0'), new Character('{'));
		POSLOOKUPTABLE.put(new Character('1'), new Character('A'));
		POSLOOKUPTABLE.put(new Character('2'), new Character('B'));
		POSLOOKUPTABLE.put(new Character('3'), new Character('C'));
		POSLOOKUPTABLE.put(new Character('4'), new Character('D'));
		POSLOOKUPTABLE.put(new Character('5'), new Character('E'));
		POSLOOKUPTABLE.put(new Character('6'), new Character('F'));
		POSLOOKUPTABLE.put(new Character('7'), new Character('G'));
		POSLOOKUPTABLE.put(new Character('8'), new Character('H'));
		POSLOOKUPTABLE.put(new Character('9'), new Character('I'));
		// Fra kode til tall
		POSLOOKUPTABLE.put(String.valueOf('{'), new Character('0'));
		POSLOOKUPTABLE.put(String.valueOf('A'), new Character('1'));
		POSLOOKUPTABLE.put(String.valueOf('B'), new Character('2'));
		POSLOOKUPTABLE.put(String.valueOf('C'), new Character('3'));
		POSLOOKUPTABLE.put(String.valueOf('D'), new Character('4'));
		POSLOOKUPTABLE.put(String.valueOf('E'), new Character('5'));
		POSLOOKUPTABLE.put(String.valueOf('F'), new Character('6'));
		POSLOOKUPTABLE.put(String.valueOf('G'), new Character('7'));
		POSLOOKUPTABLE.put(String.valueOf('H'), new Character('8'));
		POSLOOKUPTABLE.put(String.valueOf('I'), new Character('9'));

		// Fra tall til kode
		NEGLOOKUPTABLE.put(new Character('0'), new Character('}'));
		NEGLOOKUPTABLE.put(new Character('1'), new Character('J'));
		NEGLOOKUPTABLE.put(new Character('2'), new Character('K'));
		NEGLOOKUPTABLE.put(new Character('3'), new Character('L'));
		NEGLOOKUPTABLE.put(new Character('4'), new Character('M'));
		NEGLOOKUPTABLE.put(new Character('5'), new Character('N'));
		NEGLOOKUPTABLE.put(new Character('6'), new Character('O'));
		NEGLOOKUPTABLE.put(new Character('7'), new Character('P'));
		NEGLOOKUPTABLE.put(new Character('8'), new Character('Q'));
		NEGLOOKUPTABLE.put(new Character('9'), new Character('R'));
		// Fra kode til tall
		NEGLOOKUPTABLE.put(String.valueOf('}'), new Character('0'));
		NEGLOOKUPTABLE.put(String.valueOf('J'), new Character('1'));
		NEGLOOKUPTABLE.put(String.valueOf('K'), new Character('2'));
		NEGLOOKUPTABLE.put(String.valueOf('L'), new Character('3'));
		NEGLOOKUPTABLE.put(String.valueOf('M'), new Character('4'));
		NEGLOOKUPTABLE.put(String.valueOf('N'), new Character('5'));
		NEGLOOKUPTABLE.put(String.valueOf('O'), new Character('6'));
		NEGLOOKUPTABLE.put(String.valueOf('P'), new Character('7'));
		NEGLOOKUPTABLE.put(String.valueOf('Q'), new Character('8'));
		NEGLOOKUPTABLE.put(String.valueOf('R'), new Character('9'));
	}

	/**
	 * Implamentation of Interface method.
	 *
	 * @param input - The object to convert
	 * @return String - The input object as string
	 * @see no.stelvio.integration.framework.hibernate.formater.Formater#formatInput(java.lang.Object)
	 */
	public String formatInput(Object input) {
		char[] numArr = {
		};

		if (null != input) {
			Long value = (Long) input;
			Character change;
			numArr = value.toString().toCharArray();
			char num = numArr[numArr.length - 1];
			if (isNegative(value)) {
				change = (Character) NEGLOOKUPTABLE.get(new Character(num));
				char[] tmpArr = new char[numArr.length - 1];
				System.arraycopy(numArr, 1, tmpArr, 0, tmpArr.length);
				numArr = tmpArr;
			} else {
				change = (Character) POSLOOKUPTABLE.get(new Character(num));
			}
			numArr[numArr.length - 1] = change.charValue();
		}
		return String.copyValueOf(numArr);
	}

	/**
	 * Implamentation of Interface method.
	 *
	 * @param output - String to convert
	 * @return Object - The converted string
	 * @see no.stelvio.integration.framework.hibernate.formater.Formater#formatOutput(java.lang.String)
	 */
	public Object formatOutput(String output) {
		Long value;

		if (StringUtils.isBlank(output)) {
			return NULL;
		}

		char[] numArr = output.toCharArray();
		char tegn = numArr[numArr.length - 1];
		if (log.isDebugEnabled()) {
			log.debug("Output: " + output);
			log.debug("Find the matching value to the following sign: " + tegn);

		}
		Character bytt = (Character) POSLOOKUPTABLE.get(String.valueOf(tegn));

		// Tallet er negativt og tegnet må derfor slås opp i negLookupTabell
		if (null == bytt) {
			bytt = (Character) NEGLOOKUPTABLE.get(String.valueOf(tegn));
			// This is a Hack
			if (null == bytt) {
				if(log.isWarnEnabled()){
					log.warn("Can not convert long value returned from CICS: " + output);	
				}
			}
			// Legger inn minus som første tegn og flytter de andre 'en plass til høyre'.
			char[] tmpArr = new char[numArr.length + 1];
			System.arraycopy(numArr, 0, tmpArr, 1, numArr.length);
			tmpArr[0] = '-';
			tmpArr[tmpArr.length - 1] = bytt.charValue();
			numArr = tmpArr;
		} else {
			numArr[numArr.length - 1] = bytt.charValue();
		}
		value = new Long(String.copyValueOf(numArr));

		return value;
	}

	/**
	 * Checks if a number is negative.
	 *
	 * @param value - The value to test
	 * @return boolean - True if input is negative
	 */
	private static boolean isNegative(Long value) {
		if (value.longValue() < 0) {
			return true;
		}
		return false;
	}

}
