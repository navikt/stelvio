package no.nav.java;

import no.stelvio.common.context.SetStelvioContextComponent;
import no.stelvio.common.context.StelvioContextData;
import com.ibm.websphere.sca.scdl.OperationType;

public class SetPPEN015Context extends SetStelvioContextComponent {

	@Override
	protected StelvioContextData getContextData() {
		StelvioContextData context = new StelvioContextData();
		context.setApplicationId("PPEN015");
		context.setUserId("PPEN015");
		return context;
	}
	
	@Override
	public Object invoke(OperationType operationType, Object input) {
		return super.invoke(operationType, input);
	}

}
