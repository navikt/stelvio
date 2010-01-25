package no.nav.maven.plugin.sca.deployment;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Handler implements Serializable {
	private static final long serialVersionUID = 4173832810003432051L;

	private String description;

	private String displayName;

	private String handlerClass;

	private String handlerName;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("description", description).append(
				"displayName", displayName).append("handlerClass", handlerClass).append("handlerName", handlerName).toString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
}
