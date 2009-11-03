package no.nav.bpchelper.actions;

import java.lang.reflect.Method;
import java.util.Locale;

import no.nav.bpchelper.utils.ReflectionUtils;

import com.ibm.bpc.clientcore.converter.SimpleConverter;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class ProcessInstancePropertyAccessor implements ReportColumnSpec<ProcessInstanceBean> {
	private static final Locale LOCALE = Locale.getDefault();

	private final String propertyName;

	private final String label;

	private final SimpleConverter converter;

	private final Method getMethod;

	public ProcessInstancePropertyAccessor(String propertyName) {
		this.propertyName = propertyName;
		this.label = ProcessInstanceBean.getLabel(propertyName, LOCALE);
		this.converter = ProcessInstanceBean.getConverter(propertyName);
		this.getMethod = ReflectionUtils.getReadMethod(ProcessInstanceBean.class, propertyName);
	}

	public String getValue(ProcessInstanceBean processInstance) {
		Object value = ReflectionUtils.invokeMethod(getMethod, processInstance);
		if (converter != null) {
			return converter.getAsString(value, LOCALE);
		} else {
			return String.valueOf(value);
		}
	}

	public SimpleConverter getConverter() {
		return converter;
	}

	public String getLabel() {
		return label;
	}

	public String getPropertyName() {
		return propertyName;
	}
}
