package no.stelvio.presentation.jsf.renderkit.html;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import no.stelvio.presentation.jsf.model.SelectItemExtended;

import org.apache.myfaces.renderkit.html.ext.HtmlMenuRenderer;
import org.apache.myfaces.shared_tomahawk.component.EscapeCapable;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * Renderer for SelectItemExtended
 * 
 * @author persone38597605f58
 *
 */
@SuppressWarnings("unchecked")
public class HtmlMenuExtendedRenderer extends HtmlMenuRenderer {

	private static final char TABULATOR = '\t';

	/**
	 * {@inheritDoc}
	 */
	public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
		if (HtmlRendererUtils.isDisplayValueOnly(component)) {
			HtmlRendererUtils.renderDisplayValueOnlyForSelects(facesContext, component);
		} else {
			RendererUtils.checkParamValidity(facesContext, component, null);

			if (component instanceof UISelectMany) {
				internalRenderSelect(facesContext, (UISelectMany) component, isDisabled(facesContext, component), 1, true);
			} else if (component instanceof UISelectOne) {
				internalRenderSelect(facesContext, (UISelectOne) component, isDisabled(facesContext, component), 1, false);
			} else {
				throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
			}
		}
	}

	/**
	 * Renders the select item
	 * 
	 * @param facesContext the current FacesContext
	 * @param uiComponent the uiComponent to render
	 * @param disabled true if the item should be disabled, false otherwise
	 * @param size the size of the component
	 * @param selectMany true if multiple selection should be possible
	 * @throws IOException if an error occurs
	 */
	private static void internalRenderSelect(FacesContext facesContext, UIComponent uiComponent, boolean disabled, int size,
			boolean selectMany) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();

		writer.startElement(HTML.SELECT_ELEM, uiComponent);
		HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);
		writer.writeAttribute(HTML.NAME_ATTR, uiComponent.getClientId(facesContext), null);

		List selectItemList;
		Converter converter;
		if (selectMany) {
			writer.writeAttribute(HTML.MULTIPLE_ATTR, HTML.MULTIPLE_ATTR, null);
			selectItemList = org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils
					.getSelectItemList((UISelectMany) uiComponent);
			converter = HtmlRendererUtils.findUISelectManyConverterFailsafe(facesContext, uiComponent);
		} else {
			selectItemList = RendererUtils.getSelectItemList((UISelectOne) uiComponent);
			converter = HtmlRendererUtils.findUIOutputConverterFailSafe(facesContext, uiComponent);
		}

		if (size == 0) {
			// No size given (Listbox) --> size is number of select items
			writer.writeAttribute(HTML.SIZE_ATTR, Integer.toString(selectItemList.size()), null);
		} else {
			writer.writeAttribute(HTML.SIZE_ATTR, Integer.toString(size), null);
		}
		HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.SELECT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
		if (disabled) {
			writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
		}
		// render clientbehavior (for <f:ajax> support)
		if (uiComponent instanceof ClientBehaviorHolder)
        {
			Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) uiComponent).getClientBehaviors();
            HtmlRendererUtils.renderBehaviorizedEventHandlers(facesContext, writer, uiComponent, behaviors);
            HtmlRendererUtils.renderBehaviorizedFieldEventHandlers(facesContext, writer, uiComponent, behaviors);
        }		

		Set lookupSet = HtmlRendererUtils.getSubmittedOrSelectedValuesAsSet(selectMany, uiComponent, facesContext, converter);

		renderSelectOptions(facesContext, uiComponent, converter, lookupSet, selectItemList);
		// bug #970747: force separate end tag
		writer.writeText("", null);
		writer.endElement(HTML.SELECT_ELEM);
	}

	/**
	 * Render the select options
	 * 
	 * @param context the current FacesContext
	 * @param component the component to render
	 * @param converter the converter to apply
	 * @param lookupSet the lookupSet to use
	 * @param selectItemList the list of select items
	 * @throws IOException if an error occurs
	 */
	public static void renderSelectOptions(FacesContext context, UIComponent component, Converter converter, Set lookupSet,
			List selectItemList) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		for (Iterator it = selectItemList.iterator(); it.hasNext();) {
			SelectItem selectItem = (SelectItem) it.next();

			if (selectItem instanceof SelectItemGroup) {
				writer.startElement(HTML.OPTGROUP_ELEM, component);
				writer.writeAttribute(HTML.LABEL_ATTR, selectItem.getLabel(), null);
				SelectItem[] selectItems = ((SelectItemGroup) selectItem).getSelectItems();
				renderSelectOptions(context, component, converter, lookupSet, Arrays.asList(selectItems));
				writer.endElement(HTML.OPTGROUP_ELEM);
			} else {
				String itemStrValue = org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils.getConvertedStringValue(
						context, component, converter, selectItem);

				writer.write(TABULATOR);
				writer.startElement(HTML.OPTION_ELEM, component);
				if (itemStrValue != null) {
					writer.writeAttribute(HTML.VALUE_ATTR, itemStrValue, null);
				}

				if (lookupSet.contains(itemStrValue)) {
					writer.writeAttribute(HTML.SELECTED_ATTR, HTML.SELECTED_ATTR, null);
				}

				boolean disabled = selectItem.isDisabled();
				if (disabled) {
					writer.writeAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR, null);
				}

				String labelClass;
				boolean componentDisabled = isTrue(component.getAttributes().get("disabled"));

				if (componentDisabled || disabled) {
					labelClass = (String) component.getAttributes().get(JSFAttr.DISABLED_CLASS_ATTR);
				} else {
					labelClass = (String) component.getAttributes().get(JSFAttr.ENABLED_CLASS_ATTR);
				}
				if (labelClass != null) {
					writer.writeAttribute("class", labelClass, "labelClass");
				}

				if (selectItem instanceof SelectItemExtended) {

					String styleClass = ((SelectItemExtended) selectItem).getStyleClass();

					if (styleClass != null) {
						writer.writeAttribute(HTML.CLASS_ATTR, styleClass, JSFAttr.STYLE_CLASS_ATTR);
					}
				}

				boolean escape;
				if (component instanceof EscapeCapable) {
					escape = ((EscapeCapable) component).isEscape();
				} else {
					escape = RendererUtils.getBooleanAttribute(component, JSFAttr.ESCAPE_ATTR, true); // default is to escape
				}

				if (escape) {
					writer.writeText(selectItem.getLabel(), null);
				} else {
					writer.write(selectItem.getLabel());
				}

				writer.endElement(HTML.OPTION_ELEM);
			}
		}
	}
	
	private static boolean isTrue(Object obj) {
		if (!(obj instanceof Boolean))
			return false;

		return ((Boolean) obj).booleanValue();
	}
}