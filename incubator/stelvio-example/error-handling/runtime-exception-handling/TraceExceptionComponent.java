import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceImplSync;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.scdl.Component;
import com.ibm.websphere.sca.scdl.OperationType;
import com.ibm.websphere.sca.scdl.Reference;

public class TraceExceptionComponent implements ServiceImplSync {
	/**
	 * Default constructor.
	 */
	public TraceExceptionComponent() {
		super();
	}

	@Override
	public Object invoke(OperationType operationtype, Object obj) throws ServiceBusinessException {
		ServiceManager serviceManager = ServiceManager.INSTANCE;
		Component component = serviceManager.getComponent();
		Service partnerService = null;
		List references = component.getReferences();
		for (Object ref : references) {
			Reference reference = (Reference) ref;
			if (!reference.getName().equals("self")) {
				partnerService = (Service) serviceManager.locateService(reference);
				break;
			}
		}

		try {
			return partnerService.invoke(operationtype, obj);
		} catch (RuntimeException e) {
			Logger logger = Logger.getLogger(component.getName());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.severe(sw.toString());
			throw e;
		}
	}
}