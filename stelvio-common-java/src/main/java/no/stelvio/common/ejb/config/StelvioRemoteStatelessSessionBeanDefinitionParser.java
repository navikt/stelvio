package no.stelvio.common.ejb.config;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import no.stelvio.common.ejb.access.StelvioRemoteStatelessSessionProxyFactoryBean;

/**
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class StelvioRemoteStatelessSessionBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

	/**
	 * environment.
	 */
	public static final String ENVIRONMENT = "environment";

	/**
	 * JNDI environment.
	 */
	public static final String JNDI_ENVIRONMENT = "jndiEnvironment";

	/**
	 * XSD method interceptors name.
	 */
	public static final String XSD_METHOD_INTERCEPTORS_NAME = "interceptors";

	/**
	 * bean method interceptors name.
	 */
	public static final String BEAN_METHOD_INTERCEPTORS_NAME = "methodInterceptors";

	private static Log log = LogFactory.getLog(StelvioRemoteStatelessSessionBeanDefinitionParser.class);

	/**
	 * Returns the StelvioRemoteStatelessSessionProxyFactoryBean class.
	 * 
	 * @param element
	 *            an XML element
	 * @return the StelvioRemoteStatelessSessionProxyFactoryBean class
	 */
	protected Class getBeanClass(Element element) {
		return StelvioRemoteStatelessSessionProxyFactoryBean.class;
	}

	/**
	 * This code has been copied from the Spring supplied class. :
	 * <code>org.springframework.ejb.config.AbstractJndiLocatedBeanDefinitionParser</code> as this class is pacakage private,
	 * the algoritme can not be obtained through inheritance and must be duplicated
	 * 
	 * @param definitionBuilder
	 *            a bean definition builder
	 * @param element
	 *            the XML element process
	 */
	@Override
	protected void postProcess(BeanDefinitionBuilder definitionBuilder, Element element) {
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (ENVIRONMENT.equals(node.getLocalName())) {
				definitionBuilder.addPropertyValue(JNDI_ENVIRONMENT, DomUtils.getTextValue((Element) node));
			}
		}
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);

		Element methodInterceptorElement = DomUtils.getChildElementByTagName(element, XSD_METHOD_INTERCEPTORS_NAME);

		// Check to se that methodInterceptor element has been included
		if (methodInterceptorElement != null) {
			Element interceptorListElement = DomUtils.getChildElementByTagName(methodInterceptorElement, "list");
			// Is a list of interceptor set inside the methodInterceptor-element
			if (interceptorListElement != null) {
				List interceptorList = parserContext.getDelegate().parseListElement(interceptorListElement,
						builder.getRawBeanDefinition());
				builder.addPropertyValue(BEAN_METHOD_INTERCEPTORS_NAME, interceptorList);
			}
		} else {
			log.info("No interceptors were configured. Consider using Spring's remote-slsb element instead");
		}

		// Delegate the setting of attributes to the doParse(element, builder) in superclass
		doParse(element, builder);
	}
}
