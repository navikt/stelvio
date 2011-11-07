package stelvio;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.datatype.XMLGregorianCalendar;

public class MetronomeWSEXP_MetronomeHttpPortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private stelvio.MetronomeWSEXPMetronomeHttpService _service = null;
        private stelvio.Metronome _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (stelvio.MetronomeWSEXPMetronomeHttpService)ctx.lookup("java:comp/env/service/MetronomeWSEXP_MetronomeHttpService");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null)
                _service = new stelvio.MetronomeWSEXPMetronomeHttpService();
            initCommon();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new stelvio.MetronomeWSEXPMetronomeHttpService(wsdlLocation, serviceName);
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getMetronomeWSEXPMetronomeHttpPort();
        }

        public stelvio.Metronome getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://stelvio", "MetronomeWSEXP_MetronomeHttpPort");
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

    public MetronomeWSEXP_MetronomeHttpPortProxy() {
        _descriptor = new Descriptor();
    }

    public MetronomeWSEXP_MetronomeHttpPortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public void tick(XMLGregorianCalendar time) {
        _getDescriptor().getProxy().tick(time);
    }

}