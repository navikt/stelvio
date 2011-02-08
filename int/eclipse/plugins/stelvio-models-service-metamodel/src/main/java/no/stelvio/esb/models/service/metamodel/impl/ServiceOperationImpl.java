/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package no.stelvio.esb.models.service.metamodel.impl;

import java.util.Collection;

import no.stelvio.esb.models.service.metamodel.Attachment;
import no.stelvio.esb.models.service.metamodel.Fault;
import no.stelvio.esb.models.service.metamodel.Message;
import no.stelvio.esb.models.service.metamodel.OperationMetadata;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;
import no.stelvio.esb.models.service.metamodel.ServiceOperation;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getName <em>Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getUUID <em>UUID</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getServiceMetadata <em>Service Metadata</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getInputMessage <em>Input Message</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getOutputMessage <em>Output Message</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getFaults <em>Faults</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getAttachments <em>Attachments</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl#getBehaviourRules <em>Behaviour Rules</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServiceOperationImpl extends EObjectImpl implements ServiceOperation {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getUUID() <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUUID()
	 * @generated
	 * @ordered
	 */
	protected static final String UUID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUUID() <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUUID()
	 * @generated
	 * @ordered
	 */
	protected String uuid = UUID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getServiceMetadata() <em>Service Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceMetadata()
	 * @generated
	 * @ordered
	 */
	protected OperationMetadata serviceMetadata;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected static final String NAMESPACE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected String namespace = NAMESPACE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInputMessage() <em>Input Message</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputMessage()
	 * @generated
	 * @ordered
	 */
	protected Message inputMessage;

	/**
	 * The cached value of the '{@link #getOutputMessage() <em>Output Message</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputMessage()
	 * @generated
	 * @ordered
	 */
	protected Message outputMessage;

	/**
	 * The cached value of the '{@link #getFaults() <em>Faults</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFaults()
	 * @generated
	 * @ordered
	 */
	protected EList<Fault> faults;

	/**
	 * The cached value of the '{@link #getAttachments() <em>Attachments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttachments()
	 * @generated
	 * @ordered
	 */
	protected EList<Attachment> attachments;

	/**
	 * The default value of the '{@link #getBehaviourRules() <em>Behaviour Rules</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBehaviourRules()
	 * @generated
	 * @ordered
	 */
	protected static final String BEHAVIOUR_RULES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBehaviourRules() <em>Behaviour Rules</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBehaviourRules()
	 * @generated
	 * @ordered
	 */
	protected String behaviourRules = BEHAVIOUR_RULES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ServiceOperationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ServiceMetamodelPackage.Literals.SERVICE_OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUUID() {
		return uuid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUUID(String newUUID) {
		String oldUUID = uuid;
		uuid = newUUID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__UUID, oldUUID, uuid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationMetadata getServiceMetadata() {
		return serviceMetadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetServiceMetadata(OperationMetadata newServiceMetadata, NotificationChain msgs) {
		OperationMetadata oldServiceMetadata = serviceMetadata;
		serviceMetadata = newServiceMetadata;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__SERVICE_METADATA, oldServiceMetadata, newServiceMetadata);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceMetadata(OperationMetadata newServiceMetadata) {
		if (newServiceMetadata != serviceMetadata) {
			NotificationChain msgs = null;
			if (serviceMetadata != null)
				msgs = ((InternalEObject)serviceMetadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ServiceMetamodelPackage.SERVICE_OPERATION__SERVICE_METADATA, null, msgs);
			if (newServiceMetadata != null)
				msgs = ((InternalEObject)newServiceMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ServiceMetamodelPackage.SERVICE_OPERATION__SERVICE_METADATA, null, msgs);
			msgs = basicSetServiceMetadata(newServiceMetadata, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__SERVICE_METADATA, newServiceMetadata, newServiceMetadata));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNamespace(String newNamespace) {
		String oldNamespace = namespace;
		namespace = newNamespace;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__NAMESPACE, oldNamespace, namespace));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Message getInputMessage() {
		return inputMessage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInputMessage(Message newInputMessage, NotificationChain msgs) {
		Message oldInputMessage = inputMessage;
		inputMessage = newInputMessage;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__INPUT_MESSAGE, oldInputMessage, newInputMessage);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInputMessage(Message newInputMessage) {
		if (newInputMessage != inputMessage) {
			NotificationChain msgs = null;
			if (inputMessage != null)
				msgs = ((InternalEObject)inputMessage).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ServiceMetamodelPackage.SERVICE_OPERATION__INPUT_MESSAGE, null, msgs);
			if (newInputMessage != null)
				msgs = ((InternalEObject)newInputMessage).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ServiceMetamodelPackage.SERVICE_OPERATION__INPUT_MESSAGE, null, msgs);
			msgs = basicSetInputMessage(newInputMessage, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__INPUT_MESSAGE, newInputMessage, newInputMessage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Message getOutputMessage() {
		return outputMessage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOutputMessage(Message newOutputMessage, NotificationChain msgs) {
		Message oldOutputMessage = outputMessage;
		outputMessage = newOutputMessage;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__OUTPUT_MESSAGE, oldOutputMessage, newOutputMessage);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutputMessage(Message newOutputMessage) {
		if (newOutputMessage != outputMessage) {
			NotificationChain msgs = null;
			if (outputMessage != null)
				msgs = ((InternalEObject)outputMessage).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ServiceMetamodelPackage.SERVICE_OPERATION__OUTPUT_MESSAGE, null, msgs);
			if (newOutputMessage != null)
				msgs = ((InternalEObject)newOutputMessage).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ServiceMetamodelPackage.SERVICE_OPERATION__OUTPUT_MESSAGE, null, msgs);
			msgs = basicSetOutputMessage(newOutputMessage, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__OUTPUT_MESSAGE, newOutputMessage, newOutputMessage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Fault> getFaults() {
		if (faults == null) {
			faults = new EObjectContainmentEList<Fault>(Fault.class, this, ServiceMetamodelPackage.SERVICE_OPERATION__FAULTS);
		}
		return faults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Attachment> getAttachments() {
		if (attachments == null) {
			attachments = new EObjectContainmentEList<Attachment>(Attachment.class, this, ServiceMetamodelPackage.SERVICE_OPERATION__ATTACHMENTS);
		}
		return attachments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBehaviourRules() {
		return behaviourRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBehaviourRules(String newBehaviourRules) {
		String oldBehaviourRules = behaviourRules;
		behaviourRules = newBehaviourRules;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_OPERATION__BEHAVIOUR_RULES, oldBehaviourRules, behaviourRules));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ServiceMetamodelPackage.SERVICE_OPERATION__SERVICE_METADATA:
				return basicSetServiceMetadata(null, msgs);
			case ServiceMetamodelPackage.SERVICE_OPERATION__INPUT_MESSAGE:
				return basicSetInputMessage(null, msgs);
			case ServiceMetamodelPackage.SERVICE_OPERATION__OUTPUT_MESSAGE:
				return basicSetOutputMessage(null, msgs);
			case ServiceMetamodelPackage.SERVICE_OPERATION__FAULTS:
				return ((InternalEList<?>)getFaults()).basicRemove(otherEnd, msgs);
			case ServiceMetamodelPackage.SERVICE_OPERATION__ATTACHMENTS:
				return ((InternalEList<?>)getAttachments()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ServiceMetamodelPackage.SERVICE_OPERATION__NAME:
				return getName();
			case ServiceMetamodelPackage.SERVICE_OPERATION__UUID:
				return getUUID();
			case ServiceMetamodelPackage.SERVICE_OPERATION__SERVICE_METADATA:
				return getServiceMetadata();
			case ServiceMetamodelPackage.SERVICE_OPERATION__DESCRIPTION:
				return getDescription();
			case ServiceMetamodelPackage.SERVICE_OPERATION__VERSION:
				return getVersion();
			case ServiceMetamodelPackage.SERVICE_OPERATION__NAMESPACE:
				return getNamespace();
			case ServiceMetamodelPackage.SERVICE_OPERATION__INPUT_MESSAGE:
				return getInputMessage();
			case ServiceMetamodelPackage.SERVICE_OPERATION__OUTPUT_MESSAGE:
				return getOutputMessage();
			case ServiceMetamodelPackage.SERVICE_OPERATION__FAULTS:
				return getFaults();
			case ServiceMetamodelPackage.SERVICE_OPERATION__ATTACHMENTS:
				return getAttachments();
			case ServiceMetamodelPackage.SERVICE_OPERATION__BEHAVIOUR_RULES:
				return getBehaviourRules();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ServiceMetamodelPackage.SERVICE_OPERATION__NAME:
				setName((String)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__UUID:
				setUUID((String)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__SERVICE_METADATA:
				setServiceMetadata((OperationMetadata)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__VERSION:
				setVersion((String)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__NAMESPACE:
				setNamespace((String)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__INPUT_MESSAGE:
				setInputMessage((Message)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__OUTPUT_MESSAGE:
				setOutputMessage((Message)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__FAULTS:
				getFaults().clear();
				getFaults().addAll((Collection<? extends Fault>)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__ATTACHMENTS:
				getAttachments().clear();
				getAttachments().addAll((Collection<? extends Attachment>)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__BEHAVIOUR_RULES:
				setBehaviourRules((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ServiceMetamodelPackage.SERVICE_OPERATION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__UUID:
				setUUID(UUID_EDEFAULT);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__SERVICE_METADATA:
				setServiceMetadata((OperationMetadata)null);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__NAMESPACE:
				setNamespace(NAMESPACE_EDEFAULT);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__INPUT_MESSAGE:
				setInputMessage((Message)null);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__OUTPUT_MESSAGE:
				setOutputMessage((Message)null);
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__FAULTS:
				getFaults().clear();
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__ATTACHMENTS:
				getAttachments().clear();
				return;
			case ServiceMetamodelPackage.SERVICE_OPERATION__BEHAVIOUR_RULES:
				setBehaviourRules(BEHAVIOUR_RULES_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ServiceMetamodelPackage.SERVICE_OPERATION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ServiceMetamodelPackage.SERVICE_OPERATION__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case ServiceMetamodelPackage.SERVICE_OPERATION__SERVICE_METADATA:
				return serviceMetadata != null;
			case ServiceMetamodelPackage.SERVICE_OPERATION__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case ServiceMetamodelPackage.SERVICE_OPERATION__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case ServiceMetamodelPackage.SERVICE_OPERATION__NAMESPACE:
				return NAMESPACE_EDEFAULT == null ? namespace != null : !NAMESPACE_EDEFAULT.equals(namespace);
			case ServiceMetamodelPackage.SERVICE_OPERATION__INPUT_MESSAGE:
				return inputMessage != null;
			case ServiceMetamodelPackage.SERVICE_OPERATION__OUTPUT_MESSAGE:
				return outputMessage != null;
			case ServiceMetamodelPackage.SERVICE_OPERATION__FAULTS:
				return faults != null && !faults.isEmpty();
			case ServiceMetamodelPackage.SERVICE_OPERATION__ATTACHMENTS:
				return attachments != null && !attachments.isEmpty();
			case ServiceMetamodelPackage.SERVICE_OPERATION__BEHAVIOUR_RULES:
				return BEHAVIOUR_RULES_EDEFAULT == null ? behaviourRules != null : !BEHAVIOUR_RULES_EDEFAULT.equals(behaviourRules);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", UUID: ");
		result.append(uuid);
		result.append(", description: ");
		result.append(description);
		result.append(", version: ");
		result.append(version);
		result.append(", namespace: ");
		result.append(namespace);
		result.append(", behaviourRules: ");
		result.append(behaviourRules);
		result.append(')');
		return result.toString();
	}

} //ServiceOperationImpl
