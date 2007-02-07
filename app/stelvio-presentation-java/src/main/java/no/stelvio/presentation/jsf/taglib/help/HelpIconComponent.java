package no.stelvio.presentation.jsf.taglib.help;

import java.io.IOException;
import java.text.MessageFormat;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import no.stelvio.domain.cm.Content;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.util.ViewIterator;

public class HelpIconComponent extends HtmlGraphicImage {

	String key;
	private static final String JAVASCRIPT_ENCODED = "no.stelvio.presentation.jsf.taglib.help.JS_HELPICON_ENCODED";
	private static final String DEFAULT_IMAGE_URL = "help.gif";
	public static final String COMPONENT_TYPE = "no.stelvio.HelpIconComponent";

	public static final String JAVASCRIPT_ONCLICK = "javascript:document.getElementById('{0}').innerHTML = '{1}'";
	
	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context.FacesContext)
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		
		
		Content helpContent = hentHjelpetekst(this.getKey());

		// TODO: gjøre dette ved hjelp av resources i stedet
		//addScriptToPage(context);
		//addDefaultHelpIconUrl(context);
		
		// TODO: sette id

	
		String onclick = MessageFormat.format(JAVASCRIPT_ONCLICK, 
											new Object[]{findComponent(HelpDisplayAreaComponent.HELP_DISPLAY_AREA_ID),
															helpContent.getText()});
//		String onclick = "javascript:document.getElementById('id').innerHTML = '" + helpContent.getText() + "'";
		this.setOnclick(onclick);
		this.setUrl("/images/hjelp.gif");
		
		super.encodeBegin(context);
	}
	
	private String findComponentClientId(String componentId) {
		String clientId = null;
		UIComponent component = findComponentById(componentId);
		if(component != null) {
			clientId = component.getClientId(FacesContext.getCurrentInstance());
		}
		return clientId;
	}
	
	
	private UIComponent findComponentById(String componentId) {
		UIComponent component = null;

		ViewIterator viewIterator = new ViewIterator(FacesContext.getCurrentInstance().getViewRoot());
		for(; viewIterator.hasNext();) {
			UIComponent nextComponent = (UIComponent) viewIterator.next();
			if(componentId.equals(nextComponent.getId())) {
				component = nextComponent;
				break;
			}
		}
		
		return component;
	}
	
	/**
	 * Add the javascript for displaying the helpText to the page
	 */
	private void addScriptToPage(FacesContext context) {
		
		
		// check to see if the javascript has already been written, which could happen if
		// more than one helpicon is on the same page
		if(context.getExternalContext().getRequestMap().containsKey(JAVASCRIPT_ENCODED)) {
			return;
		}
		
		StelvioResourceHandler resourceHandler = new StelvioResourceHandler(HelpIconComponent.class, "showHelpText.js" );
		AddResource addResource = AddResourceFactory.getInstance(context);
		addResource.addJavaScriptAtPosition(context, 
											AddResource.HEADER_BEGIN, 
											resourceHandler, false);

		context.getExternalContext().getRequestMap().put(JAVASCRIPT_ENCODED, Boolean.TRUE);
	}


	
	private Content hentHjelpetekst(String key) {
		// TODO: kall service-lag
		Content testContent = new Content();
		testContent.setContentKey("TEST_NOKKEL");
		testContent.setText("Dette er en test...");
		
		return testContent;
	}
	
	private void addDefaultHelpIconUrl(FacesContext context) {
		// do not add default image if the url to the image is supplied with the tag
//		if(this.getUrl() != null) {
//			return;
//		}
		StelvioResourceHandler resourceHandler = new StelvioResourceHandler(HelpIconComponent.class, DEFAULT_IMAGE_URL );
		AddResource addResource = AddResourceFactory.getInstance(context);
		String uri = addResource.getResourceUri(context, resourceHandler, false);
		this.setUrl(uri);
	}
	
	
	
	/**
	 * 
	 */
	public HelpIconComponent() {
		
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
//	
//	String key;
//	private static final String JAVASCRIPT_ENCODED = "no.stelvio.presentation.jsf.taglib.help.JS_HELPICON_ENCODED";
//	private static final String DEFAULT_IMAGE_URL = "help.gif";
//	public static final String COMPONENT_TYPE = "no.stelvio.HelpIconComponent";
//
//	/* (non-Javadoc)
//	 * @see javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context.FacesContext)
//	 */
//	@Override
//	public void encodeBegin(FacesContext context) throws IOException {
//		// todo: legge inn javascript i side hvis det ikke finnes
//		
//		
//		System.out.println("*************** i encodeBegin i renderen ************");
//		Content helpContent = hentHjelpetekst(this.getKey());
//
//		// legg inn javascript
//		//addScriptToPage(context);
//		//addDefaultHelpIconUrl(context);
//		
//		this.setUrl("/images/hjelp.gif");
//		
//		// TODO: sette id
//		this.setOnclick("javascript:showHelp('" + helpContent.getText() + "', 'id')");
//		// TODO Auto-generated method stub
//		super.encodeBegin(context);
//	}
//	
//	@Override
//	public void encodeEnd(FacesContext context) throws IOException {
//		// todo: legge inn javascript i side hvis det ikke finnes
//		
//			
//			System.out.println("*************** i encodeEnd i renderen ************");
//			Content helpContent = hentHjelpetekst(this.getKey());
//
//			// legg inn javascript
//			//addScriptToPage(context);
//			//addDefaultHelpIconUrl(context);
//			
//			// TODO: sette id
//			this.setOnclick("javascript:showHelp('" + helpContent.getText() + "', 'id')");
//		
//		
//		super.encodeEnd(context);
//		HtmlOutputText tekst = new HtmlOutputText();
//		tekst.setValue("tekst fra hjelpekomponent");
//		tekst.encodeBegin(context);
//		tekst.encodeEnd(context);
//		
//	}
//	
//	/**
//	 * Add the javascript for displaying the helpText to the page
//	 */
//	private void addScriptToPage(FacesContext context) {
//		
//		
//		// check to see if the javascript has already been written, which could happen if
//		// more than one helpicon is on the same page
//		if(context.getExternalContext().getRequestMap().containsKey(JAVASCRIPT_ENCODED)) {
//			return;
//		}
//		
//		AddResource addResource = AddResourceFactory.getInstance(context);
//		addResource.addJavaScriptAtPosition(context, 
//											AddResource.HEADER_BEGIN, 
//											HelpIconComponent.class, 
//											"showHelpText.js");
//
//		context.getExternalContext().getRequestMap().put(JAVASCRIPT_ENCODED, Boolean.TRUE);
//	}
//
//
//	
//	private Content hentHjelpetekst(String key) {
//		// TODO: kall service-lag
//		Content testContent = new Content();
//		testContent.setContentKey("TEST_NOKKEL");
//		testContent.setText("Dette er en test...");
//		
//		return testContent;
//	}
//	
//	private void addDefaultHelpIconUrl(FacesContext context) {
//		// do not add default image if the url to the image is supplied with the tag
////		if(this.getUrl() != null) {
////			return;
////		}
//		AddResource addResource = AddResourceFactory.getInstance(context);
//		String uri = addResource.getResourceUri(context, HelpIconRenderer.class, DEFAULT_IMAGE_URL);
//		this.setUrl(uri);
//	}
//	
//	
//	
//	/**
//	 * 
//	 */
//	public HelpIconComponent() {
//		
//	}
//	
//	public String getKey() {
//		return key;
//	}
//
//	public void setKey(String key) {
//		this.key = key;
//	}
//	
}
