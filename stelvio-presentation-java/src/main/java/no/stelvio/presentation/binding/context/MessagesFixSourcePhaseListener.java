package no.stelvio.presentation.binding.context;

import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import no.stelvio.presentation.jsf.context.FieldInListError;

/**
 * Defines a MessagesFixSourcePhaseListener.
 * 
 * @author AW
 */
public class MessagesFixSourcePhaseListener implements PhaseListener {

	private static final long serialVersionUID = -465897753283836510L;

	private static final String REGEX = "^[0-9]+" + FieldInListError.SEPERATOR + ".*";

	/**
	 * Called after phase.
	 * 
	 * @param event the event
	 */
	@Override
	public void afterPhase(PhaseEvent event) {
		// do nothing
	}

	/**
	 * Called before phase.
	 * 
	 * @param event the event
	 */
	@Override
	public void beforePhase(PhaseEvent event) {
		if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {

			FacesContext facesContext = event.getFacesContext();

			Iterator<?> clientIdIterator = facesContext.getClientIdsWithMessages();
			while (clientIdIterator.hasNext()) {
				String clientId = (String) clientIdIterator.next();

				if (clientId != null) {
					String trimmedClientId = clientId;
					String prefix = null;
					if (isFieldInListError(clientId)) {
						int index = clientId.indexOf(FieldInListError.SEPERATOR);
						trimmedClientId = clientId.substring(index + 1);
						prefix = clientId.substring(0, index + 1);
					}

					UIComponent component = findComponent(facesContext.getViewRoot(), trimmedClientId);
					if (component != null) {
						String componentId;
						if (prefix != null) {
							StringBuffer fullComponentId = new StringBuffer(component.getClientId(facesContext));
							int indexOfFieldId = fullComponentId.lastIndexOf(trimmedClientId);
							fullComponentId.insert(indexOfFieldId, prefix);
							componentId = fullComponentId.toString();
						} else {
							componentId = component.getClientId(facesContext);
						}
						setCorrectComponentIdOnMessage(clientId, componentId, facesContext);
					}
				}
			}
		}
	}

	/**
	 * Is field in list.
	 * 
	 * @param clientId client id
	 * @return true if field in list
	 */
	public boolean isFieldInListError(String clientId) {
		return clientId.matches(REGEX);
	}

	/**
	 * Set correct componentId on message.
	 * 
	 * @param clientId client id
	 * @param componentId component id
	 * @param facesContext faces context
	 */
	private void setCorrectComponentIdOnMessage(String clientId, String componentId, FacesContext facesContext) {
		if (componentId != null && clientId != null) {
			Iterator<?> messageIterator = facesContext.getMessages(clientId);
			while (messageIterator.hasNext()) {
				FacesMessage message = (FacesMessage) messageIterator.next();
				facesContext.addMessage(componentId, message);
			}
		}
	}

	/**
	 * Find component.
	 * 
	 * @param viewRoot view root
	 * @param clientId client id
	 * @return component
	 */
	private UIComponent findComponent(UIViewRoot viewRoot, String clientId) {
		UIComponent component = viewRoot.getFacet(clientId);
		if (component == null) {
			component = findComponent(viewRoot.getChildren(), clientId);
		}
		return component;
	}

	/**
	 * Find component.
	 * 
	 * @param uiComponentsList component list
	 * @param componentId the component
	 * @return component with componentId
	 */
	private UIComponent findComponent(List<UIComponent> uiComponentsList, String componentId) {
		UIComponent component = null;
		for (UIComponent nextComponent : uiComponentsList) {
			if (componentId.equals(nextComponent.getId())) {
				component = nextComponent;
				break;
			}
			component = findComponent(nextComponent.getChildren(), componentId);
			if (component != null) {
				break;
			}
		}
		return component;
	}

	/**
	 * Get the phase id.
	 * 
	 * @return phase id
	 */
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
