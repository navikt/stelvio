package no.nav.maven.plugin.sca.deployment;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Handler implements Serializable {
	private static final long serialVersionUID = 4173832810003432051L;

	private String description;

	private String displayName;

	private String handlerClass;

	private String handlerName;

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(description).append(displayName).append(handlerClass).append(handlerName)
				.toString();
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
