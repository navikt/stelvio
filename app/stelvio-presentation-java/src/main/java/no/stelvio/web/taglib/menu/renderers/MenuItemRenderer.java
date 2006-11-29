/**
 * 
 */
package no.stelvio.web.taglib.menu.renderers;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import no.stelvio.web.taglib.menu.components.MenuItemComponent;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.component.html.ext.HtmlCommandLink;
import org.apache.myfaces.shared_impl.el.SimpleActionMethodBinding;


/**
 * TODO: Document me
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class MenuItemRenderer extends Renderer {
	/**
	 * TODO: Document me
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		MenuItemComponent menuItem = (MenuItemComponent) component;
		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("li", menuItem);
		
		component.getChildren().add(createMenuItem(menuItem));
		
		writer.close();
	}

	/**
	 * TODO: Document me
	 * @param menuItem
	 * @return
	 */
	public UIComponent createMenuItem(MenuItemComponent menuItem) {
		// Create standard output text if no action is defined for menu item
		if (StringUtils.isEmpty(menuItem.getAction())) {
			HtmlOutputText outputText = new HtmlOutputText();
			outputText.setValue(menuItem.getLeadText());
			
			if (!StringUtils.isEmpty(menuItem.getStyleClass())) {
				outputText.setStyleClass(menuItem.getStyleClass());
			}
			
			if (!StringUtils.isEmpty(menuItem.getInlineStyle())) {
				outputText.setStyle(menuItem.getInlineStyle());
			}
			
			return outputText;
		}
		// Create html command link if action is defined for menu item
		else {
			HtmlCommandLink link = new HtmlCommandLink();
			link.setValue(menuItem.getLeadText());
			link.setAction(new SimpleActionMethodBinding(menuItem.getAction()));
			
			if (!StringUtils.isEmpty(menuItem.getStyleClass())) {
				link.setStyleClass(menuItem.getStyleClass());
			}
			
			if (!StringUtils.isEmpty(menuItem.getInlineStyle())) {
				link.setStyle(menuItem.getInlineStyle());
			}
			
			return link;
		}
	}
	
	/**
	 * TODO: Document me
	 */
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("li");
		writer.close();
	}
}