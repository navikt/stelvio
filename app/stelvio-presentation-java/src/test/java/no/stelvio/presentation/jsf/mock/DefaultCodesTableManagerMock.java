package no.stelvio.presentation.jsf.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;
import no.stelvio.common.codestable.support.DefaultCodesTableManager;
import no.stelvio.common.codestable.support.IdAsKeyCodesTablePeriodicItem;
import no.stelvio.presentation.jsf.codestable.KravStatusCode;
import no.stelvio.presentation.jsf.codestable.KravStatusCti;

/**
 * DefaultCodesTableManagerMock.
 * 
 * @author person37c6059e407e (Capgemini)
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class DefaultCodesTableManagerMock extends DefaultCodesTableManager {
	/**
	 * getCodesTablePeriodic.
	 * 
	 * @see no.stelvio.common.codestable.CodesTableManager#getCodesTable(java.lang.Class)
	 * @param <T>
	 *            Code table item type.
	 * @param <K>
	 *            Code type (Enum).
	 * @param <V>
	 *            String.
	 * @param codesTablePeriodicItemClass
	 *            periodic codetable item object.
	 * @return codesTablePeriodicItem.
	 */
	public <T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V> CodesTablePeriodic<T, K, V> getCodesTablePeriodic(
			Class<T> codesTablePeriodicItemClass) {

		List<T> list = new ArrayList<T>();

		if (codesTablePeriodicItemClass.equals(PoststedCtiMock.class)) {
			list.add(generateIdAsKeyCodesTablePeriodic(codesTablePeriodicItemClass, "1", "0001", "OSLO"));
			list.add(generateIdAsKeyCodesTablePeriodic(codesTablePeriodicItemClass, "2", "5000", "BERGEN"));
		} else if (codesTablePeriodicItemClass.equals(KravStatusCti.class)) {
			list.add(generateCodesTablePeriodic(codesTablePeriodicItemClass, KravStatusCode.TIL_BEHANDLING.name(),
					"Til behandling"));
			list.add(generateCodesTablePeriodic(codesTablePeriodicItemClass, KravStatusCode.AVBRUTT.name(), "Avbrutt"));
			list.add(generateCodesTablePeriodic(codesTablePeriodicItemClass, KravStatusCode.BEREGNET.name(), "Beregnet"));
			list.add(generateCodesTablePeriodic(codesTablePeriodicItemClass, KravStatusCode.FERDIG.name(), "Ferdig"));
			list.add(generateCodesTablePeriodic(codesTablePeriodicItemClass, KravStatusCode.KLAR_TIL_ATT.name(),
					"Klar til attestering"));
			list.add(generateCodesTablePeriodic(codesTablePeriodicItemClass, KravStatusCode.VENTER_AFP.name(), "Venter AFP"));
		}

		return new DefaultCodesTablePeriodicMock<T, K, V>(list);
	}

	/**
	 * Generate codestable periodic.
	 * 
	 * @param x
	 *            class
	 * @param sCode
	 *            code
	 * @param sDecode
	 *            decode
	 * @return codestable
	 */
	private <T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V> T generateCodesTablePeriodic(Class<T> x,
			String sCode, String sDecode) {
		return generateIdAsKeyCodesTablePeriodic(x, null, sCode, sDecode);
	}

	/**
	 * Generate Id as Key.
	 * 
	 * @param x
	 *            the x to set
	 * @param sId
	 *            the sId to set
	 * @param sCode
	 *            the sCode to set
	 * @param sDecode
	 *            to sDecode to set
	 * @return the object
	 */
	private <T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V> T generateIdAsKeyCodesTablePeriodic(Class<T> x,
			String sId, String sCode, String sDecode) {
		Constructor<T> cons;
		T cti;
		try {
			cons = x.getDeclaredConstructor();
			cons.setAccessible(true);
			cti = cons.newInstance();

			Field id = null;
			Field code = null;
			if (sId != null) {
				id = IdAsKeyCodesTablePeriodicItem.class.getDeclaredField("id");
				id.setAccessible(true);
				id.set(cti, sId);

				code = IdAsKeyCodesTablePeriodicItem.class.getDeclaredField("code");
			} else {
				code = CodesTablePeriodicItem.class.getDeclaredField("code");
			}
			code.setAccessible(true);
			code.set(cti, sCode);

			Field decode = AbstractCodesTableItem.class.getDeclaredField("decode");
			decode.setAccessible(true);
			decode.set(cti, sDecode);

			Field valid = AbstractCodesTableItem.class.getDeclaredField("valid");
			valid.setAccessible(true);
			valid.set(cti, true);

			Calendar then = Calendar.getInstance();
			then.set(Calendar.YEAR, 1900);
			Field fromDate = AbstractCodesTablePeriodicItem.class.getDeclaredField("fromDate");
			fromDate.setAccessible(true);
			fromDate.set(cti, then.getTime());

		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchFieldException e) {

			throw new IllegalStateException(e);
		}

		return cti;
	}

}
