package no.stelvio.presentation.jsf.mock;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.render.Renderer;

/**
 * UIComponentMock.
 * 
 * @author AW
 * @version $Id$
 */
public class UIComponentMock extends UIComponent {

	private Map<String, Object> attributes;

	private String id;

	/**
	 * Mock.
	 */
	public UIComponentMock() {

	}

	/**
	 * UIComponentMock.
	 * 
	 * @param attributes
	 *            inncomming map.
	 */
	public UIComponentMock(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFacesListener(FacesListener listener) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void broadcast(FacesEvent event) throws AbortProcessingException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decode(FacesContext context) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void encodeChildren(FacesContext context) throws IOException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void encodeEnd(FacesContext context) throws IOException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent findComponent(String expr) {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map getAttributes() {
		return attributes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildCount() {

		return 0;
	}

	@Override
	public List getChildren() {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClientId(FacesContext context) {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FacesContext getFacesContext() {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FacesListener[] getFacesListeners(Class clazz) {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent getFacet(String name) {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map getFacets() {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator getFacetsAndChildren() {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFamily() {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent getParent() {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Renderer getRenderer(FacesContext context) {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRendererType() {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getRendersChildren() {

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueBinding getValueBinding(String name) {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRendered() {

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processDecodes(FacesContext context) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processRestoreState(FacesContext context, Object state) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object processSaveState(FacesContext context) {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processUpdates(FacesContext context) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processValidators(FacesContext context) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void queueEvent(FacesEvent event) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeFacesListener(FacesListener listener) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(UIComponent parent) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRendered(boolean rendered) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRendererType(String rendererType) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueBinding(String name, ValueBinding binding) {

	}

	/**
	 * Is UIComponent transient.
	 * 
	 * @return false.
	 */
	public boolean isTransient() {
		return false;
	}

	/**
	 * Restore context state.
	 * 
	 * @param context
	 *            FacesContext.
	 * @param state
	 *            Object.
	 */
	public void restoreState(FacesContext context, Object state) {

	}

	/**
	 * Save context state.
	 * 
	 * @param context
	 *            FacesContext.
	 * @return state.
	 */
	public Object saveState(FacesContext context) {

		return null;
	}

	/**
	 * Set transient.
	 * 
	 * @param trans
	 *            boolean.
	 */
	public void setTransient(boolean trans) {

	}

	/**
	 * Set attributes of type Map<String,Object>.
	 * 
	 * @param attributes
	 *            Map<String,Object>
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

}
