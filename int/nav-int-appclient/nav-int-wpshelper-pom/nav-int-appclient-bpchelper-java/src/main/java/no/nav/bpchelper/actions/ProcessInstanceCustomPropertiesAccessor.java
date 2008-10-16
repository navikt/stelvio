package no.nav.bpchelper.actions;

import java.util.List;

import com.ibm.bpe.api.ProcessInstanceData;

public class ProcessInstanceCustomPropertiesAccessor implements ReportColumnSpec<ProcessInstanceData> {
	public String getLabel() {
		return "Custom Properties";
	}

	@SuppressWarnings("unchecked")
	public String getValue(ProcessInstanceData instance) {
		StringBuilder sb = new StringBuilder();
		List<String> namesOfCustomProperties = (List<String>)instance.getNamesOfCustomProperties();
		for (String customPropertyName : namesOfCustomProperties) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(customPropertyName).append("=").append(instance.getCustomProperty(customPropertyName));
		}
		return sb.toString();
	}
}
