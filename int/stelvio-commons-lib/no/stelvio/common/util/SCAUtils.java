package no.stelvio.common.util;

import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.scdl.Component;
import com.ibm.ws.sca.internal.scdl.impl.ManagedComponentImpl;
import com.ibm.ws.sca.internal.scdl.impl.ManagedModuleImpl;

public class SCAUtils {

	public static String getComponentName() {

		Component component = ServiceManager.INSTANCE.getComponent();
		return component.getName();
	}

	
	public static String getModuleName() {
		
		String moduleName = "MODULE_UNKNOWN";
		Component component = ServiceManager.INSTANCE.getComponent();
		
		if (component!=null) {
			Object componentObj = (Object) ServiceManager.INSTANCE.getComponent();

			if (componentObj instanceof ManagedComponentImpl) {
				ManagedComponentImpl managedComponentImpl = (ManagedComponentImpl) componentObj ;
				Object aggregate = managedComponentImpl.getAggregate();
				if (aggregate instanceof ManagedModuleImpl) {
					ManagedModuleImpl aggregateModul = (ManagedModuleImpl) aggregate;
					moduleName = aggregateModul.getName();
				}
			}		
		}
		return moduleName;
	}
}
