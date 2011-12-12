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
		
		String moduleName = "NOT_FOUND";
		Object component = (Object) ServiceManager.INSTANCE.getComponent();

		if (component instanceof ManagedComponentImpl) {
			ManagedComponentImpl managedComponentImpl = (ManagedComponentImpl) component ;
			Object aggregate = managedComponentImpl.getAggregate();
			if (aggregate instanceof ManagedModuleImpl) {
				ManagedModuleImpl aggregateModul = (ManagedModuleImpl) aggregate;
				moduleName = aggregateModul.getName();
			}
		}		
		return moduleName;
	}
}
