import java.util.ArrayList;
import java.util.List;

import com.ibm.websphere.bo.BOInstanceValidator;
import com.ibm.websphere.sca.ServiceManager;
import commonj.sdo.DataObject;

public class BusinessObjectValidator {
	public static boolean validateBusinessObject(DataObject businessObject) {
		return getValidationService().validate(businessObject, null);
	}

	public static void assertValidBusinessObject(DataObject businessObject) {
		List<DataObject> diagnostics = new ArrayList<DataObject>();
		if (!getValidationService().validate(businessObject, diagnostics)) {
			throw new AssertionError(diagnostics.iterator().next().get("message"));
		}
	}

	private static BOInstanceValidator getValidationService() {
		return (BOInstanceValidator) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOInstanceValidator");
	}
}
