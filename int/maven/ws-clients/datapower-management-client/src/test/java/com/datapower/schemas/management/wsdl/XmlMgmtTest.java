package com.datapower.schemas.management.wsdl;

import java.util.List;

import javax.xml.ws.BindingProvider;

import org.junit.Test;

import com.datapower.schemas.appliance.management._1_0.wsdl.Fault;
import com.datapower.schemas.management.Request;
import com.datapower.schemas.management.Response;
import com.datapower.schemas.management.StatusEnum;
import com.datapower.schemas.management.StatusVersion;

public class XmlMgmtTest {
	private XmlMgmt xmlMgmt;

	public XmlMgmtTest() {
		xmlMgmt = new XmlMgmt_Service().getXmlMgmt();
		((BindingProvider) xmlMgmt).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				"https://dp-tst-01.adeo.no:5550/service/mgmt/current");
	}

	@Test
	public void testGetDomainList() throws Fault {
		Request request = new Request();
		request.setGetStatus(new Request.GetStatus());
		request.getGetStatus().setClazz(StatusEnum.VERSION);
		Response response = xmlMgmt.operation(request);
		List<Object> status = response.getStatus().getActiveUsersOrARPStatusOrBattery();
		for (Object object : status) {
			if (object instanceof StatusVersion) {
				StatusVersion statusVersion = (StatusVersion) object;
				System.out.println("Firmware version=" + statusVersion.getVersion());
			}
		}
	}
}
