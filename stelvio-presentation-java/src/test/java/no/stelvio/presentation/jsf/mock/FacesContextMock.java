package no.stelvio.presentation.jsf.mock;

import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

/**
 * FacesContextMock.
 * 
 * @author person096a015479bb, Capgemini
 * Just a mock.
 *
 */
public class FacesContextMock extends FacesContext {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMessage(String clientId, FacesMessage message) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator getClientIdsWithMessages() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getResponseComplete() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResponseStream(ResponseStream responseStream) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExternalContext getExternalContext() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseComplete() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Application getApplication() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderResponse() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void release() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Severity getMaximumSeverity() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator getMessages() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator getMessages(String clientId) {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getRenderResponse() {
		return false;
	}		
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseStream getResponseStream() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RenderKit getRenderKit() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseWriter getResponseWriter() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResponseWriter(ResponseWriter responseWriter) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIViewRoot getViewRoot() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setViewRoot(UIViewRoot root) {
	}
}
