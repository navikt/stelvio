package com.datapower.schemas.appliance.management._1_0.wsdl;

import javax.xml.ws.BindingProvider;

import org.junit.Test;

import com.datapower.schemas.appliance.management._1.GetDomainListRequest;
import com.datapower.schemas.appliance.management._1.GetDomainListResponse;

public class AppMgmtProtocolTest {
	private AppMgmtProtocol appMgmtProtocol;

	public AppMgmtProtocolTest() {
		appMgmtProtocol = new AppMgmtProtocol_Service().getAppMgmtProtocol();
		((BindingProvider) appMgmtProtocol).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				"https://dp-tst-01.adeo.no:5550/service/mgmt/amp/1.0");
	}

	@Test
	public void testGetDomainList() throws Fault {
		GetDomainListResponse response = appMgmtProtocol.getDomainList(new GetDomainListRequest());
		System.out.println(response.getDomain());
	}
}
