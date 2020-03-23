package no.stelvio.common.context.support;

/**
 * 
 * ComponentIdHolder used to configure a component Id.
 * 
 * <em> The component id is sometimes also refered to as application id. </em>
 * 
 * <p>
 * There is typically one component id per EAR or WAR. There should never be more than one ComponentIdHolder configured for an
 * ApplicationContext. It's important to be aware of this when more than one xml file make up an ApplicationContext. Normal use
 * of the ComponentIdHolder is to configure a ComponentIdHolder bean in the configuration project (module)
 * </p>
 * 
 * An example of how to configure a componentId for an application (EAR/WAR) by using the ComponentIdHolder is shown below.
 * ComponentIdHolder should always be singleton scoped:
 * 
 * <pre>
 * &lt;bean class=&quot;no.stelvio.common.context.support.ComponentIdHolder&quot; scope=&quot;singleton&quot;&gt;
 *    &lt;constructor-arg value=&quot;PSAK&quot;/&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 *
 */
public class ComponentIdHolder {

	private String componentId = null;

	/**
	 * Constructs a ComponentIdHolder holding the provided componentId.
	 * 
	 * @param componentId
	 *            a component identifier
	 */
	public ComponentIdHolder(String componentId) {
		this.componentId = componentId;
	}

	/**
	 * Returns the ComponentId as a String.
	 * 
	 * @return string value of component id
	 */
	public final String getComponentId() {
		return componentId;
	}

}
