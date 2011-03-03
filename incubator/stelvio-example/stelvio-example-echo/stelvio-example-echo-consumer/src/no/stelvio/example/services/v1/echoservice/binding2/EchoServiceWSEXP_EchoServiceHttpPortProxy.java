package no.stelvio.example.services.v1.echoservice.binding2;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import no.stelvio.example.services.v1.echoservice.ObjectFactory;

public class EchoServiceWSEXP_EchoServiceHttpPortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private no.stelvio.example.services.v1.echoservice.binding2.EchoServiceWSEXPEchoServiceHttpService _service = null;
        private no.stelvio.example.services.v1.echoservice.binding2.EchoService _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (no.stelvio.example.services.v1.echoservice.binding2.EchoServiceWSEXPEchoServiceHttpService)ctx.lookup("java:comp/env/service/EchoServiceWSEXP_EchoServiceHttpService");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null)
                _service = new no.stelvio.example.services.v1.echoservice.binding2.EchoServiceWSEXPEchoServiceHttpService();
            initCommon();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new no.stelvio.example.services.v1.echoservice.binding2.EchoServiceWSEXPEchoServiceHttpService(wsdlLocation, serviceName);
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getEchoServiceWSEXPEchoServiceHttpPort();
        }

        public no.stelvio.example.services.v1.echoservice.binding2.EchoService getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://www.stelvio.no/example/services/V1/EchoService/Binding2", "EchoServiceWSEXP_EchoServiceHttpPort");
                _dispatch = _service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);

                String proxyEndpointUrl = getEndpoint();
                BindingProvider bp = (BindingProvider) _dispatch;
                String dispatchEndpointUrl = (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
                if (!dispatchEndpointUrl.equals(proxyEndpointUrl))
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, proxyEndpointUrl);
            }
            return _dispatch;
        }

        public String getEndpoint() {
            BindingProvider bp = (BindingProvider) _proxy;
            return (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }

        public void setEndpoint(String endpointUrl) {
            BindingProvider bp = (BindingProvider) _proxy;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);

            if (_dispatch != null ) {
                bp = (BindingProvider) _dispatch;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
            }
        }
    }

    public EchoServiceWSEXP_EchoServiceHttpPortProxy() {
        _descriptor = new Descriptor();
    }

    public EchoServiceWSEXP_EchoServiceHttpPortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public String echo(String input) throws EchoEchoFault1Msg {
        return _getDescriptor().getProxy().echo(input);
    }

}