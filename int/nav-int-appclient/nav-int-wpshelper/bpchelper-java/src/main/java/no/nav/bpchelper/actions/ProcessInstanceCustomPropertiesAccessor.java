package no.nav.bpchelper.actions;

import java.util.List;

import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class ProcessInstanceCustomPropertiesAccessor implements ReportColumnSpec<ProcessInstanceBean> {
	public String getLabel() {
		return "Custom Properties";
	}

	@SuppressWarnings("unchecked")
	public String getValue(ProcessInstanceBean instance) {
		StringBuilder sb = new StringBuilder();
		List<String> namesOfCustomProperties = (List<String>) instance.getNamesOfCustomProperties();
		for (String customPropertyName : namesOfCustomProperties) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(customPropertyName).append("=").append(instance.getCustomProperty(customPropertyName));
		}
		return sb.toString();
	}
}
