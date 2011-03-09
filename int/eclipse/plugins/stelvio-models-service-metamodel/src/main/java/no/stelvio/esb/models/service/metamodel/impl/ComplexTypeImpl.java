/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package no.stelvio.esb.models.service.metamodel.impl;

import java.util.Collection;

import no.stelvio.esb.models.service.metamodel.Attachment;
import no.stelvio.esb.models.service.metamodel.Attribute;
import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.Diagram;
import no.stelvio.esb.models.service.metamodel.Message;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Complex Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getUUID <em>UUID</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getReferencedAttributes <em>Referenced Attributes</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#isIsEnumeration <em>Is Enumeration</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#isIsFault <em>Is Fault</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getReferencedMessages <em>Referenced Messages</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getDiagrams <em>Diagrams</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getAttachments <em>Attachments</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl#getGeneralizations <em>Generalizations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComplexTypeImpl extends EObjectImpl implements ComplexType {
	/**
	 * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<Attribute> attributes;

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
	 * The cached value of the '{@link #getReferencedAttributes() <em>Referenced Attributes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencedAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<Attribute> referencedAttributes;

	/**
	 * The default value of the '{@link #isIsEnumeration() <em>Is Enumeration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsEnumeration()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_ENUMERATION_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsEnumeration() <em>Is Enumeration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsEnumeration()
	 * @generated
	 * @ordered
	 */
	protected boolean isEnumeration = IS_ENUMERATION_EDEFAULT;

	/**
	 * This is true if the Is Enumeration attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean isEnumerationESet;

	/**
	 * The default value of the '{@link #isIsFault() <em>Is Fault</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsFault()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_FAULT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsFault() <em>Is Fault</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsFault()
	 * @generated
	 * @ordered
	 */
	protected boolean isFault = IS_FAULT_EDEFAULT;

	/**
	 * This is true if the Is Fault attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean isFaultESet;

	/**
	 * The cached value of the '{@link #getReferencedMessages() <em>Referenced Messages</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencedMessages()
	 * @generated
	 * @ordered
	 */
	protected EList<Message> referencedMessages;

	/**
	 * The cached value of the '{@link #getDiagrams() <em>Diagrams</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagrams()
	 * @generated
	 * @ordered
	 */
	protected EList<Diagram> diagrams;

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
	 * The cached value of the '{@link #getGeneralizations() <em>Generalizations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneralizations()
	 * @generated
	 * @ordered
	 */
	protected EList<ComplexType> generalizations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComplexTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ServiceMetamodelPackage.Literals.COMPLEX_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Attribute> getAttributes() {
		if (attributes == null) {
			attributes = new EObjectContainmentEList<Attribute>(Attribute.class, this, ServiceMetamodelPackage.COMPLEX_TYPE__ATTRIBUTES);
		}
		return attributes;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.COMPLEX_TYPE__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.COMPLEX_TYPE__UUID, oldUUID, uuid));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.COMPLEX_TYPE__VERSION, oldVersion, version));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.COMPLEX_TYPE__NAMESPACE, oldNamespace, namespace));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.COMPLEX_TYPE__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Attribute> getReferencedAttributes() {
		if (referencedAttributes == null) {
			referencedAttributes = new EObjectWithInverseResolvingEList<Attribute>(Attribute.class, this, ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_ATTRIBUTES, ServiceMetamodelPackage.ATTRIBUTE__TYPE_REF);
		}
		return referencedAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsEnumeration() {
		return isEnumeration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsEnumeration(boolean newIsEnumeration) {
		boolean oldIsEnumeration = isEnumeration;
		isEnumeration = newIsEnumeration;
		boolean oldIsEnumerationESet = isEnumerationESet;
		isEnumerationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.COMPLEX_TYPE__IS_ENUMERATION, oldIsEnumeration, isEnumeration, !oldIsEnumerationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetIsEnumeration() {
		boolean oldIsEnumeration = isEnumeration;
		boolean oldIsEnumerationESet = isEnumerationESet;
		isEnumeration = IS_ENUMERATION_EDEFAULT;
		isEnumerationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.COMPLEX_TYPE__IS_ENUMERATION, oldIsEnumeration, IS_ENUMERATION_EDEFAULT, oldIsEnumerationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetIsEnumeration() {
		return isEnumerationESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsFault() {
		return isFault;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsFault(boolean newIsFault) {
		boolean oldIsFault = isFault;
		isFault = newIsFault;
		boolean oldIsFaultESet = isFaultESet;
		isFaultESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.COMPLEX_TYPE__IS_FAULT, oldIsFault, isFault, !oldIsFaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetIsFault() {
		boolean oldIsFault = isFault;
		boolean oldIsFaultESet = isFaultESet;
		isFault = IS_FAULT_EDEFAULT;
		isFaultESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.COMPLEX_TYPE__IS_FAULT, oldIsFault, IS_FAULT_EDEFAULT, oldIsFaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetIsFault() {
		return isFaultESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Message> getReferencedMessages() {
		if (referencedMessages == null) {
			referencedMessages = new EObjectWithInverseResolvingEList<Message>(Message.class, this, ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_MESSAGES, ServiceMetamodelPackage.MESSAGE__TYPE_REF);
		}
		return referencedMessages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Diagram> getDiagrams() {
		if (diagrams == null) {
			diagrams = new EObjectWithInverseResolvingEList.ManyInverse<Diagram>(Diagram.class, this, ServiceMetamodelPackage.COMPLEX_TYPE__DIAGRAMS, ServiceMetamodelPackage.DIAGRAM__COMPLEX_TYPES);
		}
		return diagrams;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Attachment> getAttachments() {
		if (attachments == null) {
			attachments = new EObjectContainmentEList<Attachment>(Attachment.class, this, ServiceMetamodelPackage.COMPLEX_TYPE__ATTACHMENTS);
		}
		return attachments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComplexType> getGeneralizations() {
		if (generalizations == null) {
			generalizations = new EObjectResolvingEList<ComplexType>(ComplexType.class, this, ServiceMetamodelPackage.COMPLEX_TYPE__GENERALIZATIONS);
		}
		return generalizations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_ATTRIBUTES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getReferencedAttributes()).basicAdd(otherEnd, msgs);
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_MESSAGES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getReferencedMessages()).basicAdd(otherEnd, msgs);
			case ServiceMetamodelPackage.COMPLEX_TYPE__DIAGRAMS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getDiagrams()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ServiceMetamodelPackage.COMPLEX_TYPE__ATTRIBUTES:
				return ((InternalEList<?>)getAttributes()).basicRemove(otherEnd, msgs);
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_ATTRIBUTES:
				return ((InternalEList<?>)getReferencedAttributes()).basicRemove(otherEnd, msgs);
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_MESSAGES:
				return ((InternalEList<?>)getReferencedMessages()).basicRemove(otherEnd, msgs);
			case ServiceMetamodelPackage.COMPLEX_TYPE__DIAGRAMS:
				return ((InternalEList<?>)getDiagrams()).basicRemove(otherEnd, msgs);
			case ServiceMetamodelPackage.COMPLEX_TYPE__ATTACHMENTS:
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
			case ServiceMetamodelPackage.COMPLEX_TYPE__ATTRIBUTES:
				return getAttributes();
			case ServiceMetamodelPackage.COMPLEX_TYPE__NAME:
				return getName();
			case ServiceMetamodelPackage.COMPLEX_TYPE__UUID:
				return getUUID();
			case ServiceMetamodelPackage.COMPLEX_TYPE__VERSION:
				return getVersion();
			case ServiceMetamodelPackage.COMPLEX_TYPE__NAMESPACE:
				return getNamespace();
			case ServiceMetamodelPackage.COMPLEX_TYPE__DESCRIPTION:
				return getDescription();
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_ATTRIBUTES:
				return getReferencedAttributes();
			case ServiceMetamodelPackage.COMPLEX_TYPE__IS_ENUMERATION:
				return isIsEnumeration();
			case ServiceMetamodelPackage.COMPLEX_TYPE__IS_FAULT:
				return isIsFault();
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_MESSAGES:
				return getReferencedMessages();
			case ServiceMetamodelPackage.COMPLEX_TYPE__DIAGRAMS:
				return getDiagrams();
			case ServiceMetamodelPackage.COMPLEX_TYPE__ATTACHMENTS:
				return getAttachments();
			case ServiceMetamodelPackage.COMPLEX_TYPE__GENERALIZATIONS:
				return getGeneralizations();
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
			case ServiceMetamodelPackage.COMPLEX_TYPE__ATTRIBUTES:
				getAttributes().clear();
				getAttributes().addAll((Collection<? extends Attribute>)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__NAME:
				setName((String)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__UUID:
				setUUID((String)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__VERSION:
				setVersion((String)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__NAMESPACE:
				setNamespace((String)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_ATTRIBUTES:
				getReferencedAttributes().clear();
				getReferencedAttributes().addAll((Collection<? extends Attribute>)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__IS_ENUMERATION:
				setIsEnumeration((Boolean)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__IS_FAULT:
				setIsFault((Boolean)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_MESSAGES:
				getReferencedMessages().clear();
				getReferencedMessages().addAll((Collection<? extends Message>)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__DIAGRAMS:
				getDiagrams().clear();
				getDiagrams().addAll((Collection<? extends Diagram>)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__ATTACHMENTS:
				getAttachments().clear();
				getAttachments().addAll((Collection<? extends Attachment>)newValue);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__GENERALIZATIONS:
				getGeneralizations().clear();
				getGeneralizations().addAll((Collection<? extends ComplexType>)newValue);
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
			case ServiceMetamodelPackage.COMPLEX_TYPE__ATTRIBUTES:
				getAttributes().clear();
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__UUID:
				setUUID(UUID_EDEFAULT);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__NAMESPACE:
				setNamespace(NAMESPACE_EDEFAULT);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_ATTRIBUTES:
				getReferencedAttributes().clear();
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__IS_ENUMERATION:
				unsetIsEnumeration();
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__IS_FAULT:
				unsetIsFault();
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_MESSAGES:
				getReferencedMessages().clear();
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__DIAGRAMS:
				getDiagrams().clear();
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__ATTACHMENTS:
				getAttachments().clear();
				return;
			case ServiceMetamodelPackage.COMPLEX_TYPE__GENERALIZATIONS:
				getGeneralizations().clear();
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
			case ServiceMetamodelPackage.COMPLEX_TYPE__ATTRIBUTES:
				return attributes != null && !attributes.isEmpty();
			case ServiceMetamodelPackage.COMPLEX_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ServiceMetamodelPackage.COMPLEX_TYPE__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case ServiceMetamodelPackage.COMPLEX_TYPE__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case ServiceMetamodelPackage.COMPLEX_TYPE__NAMESPACE:
				return NAMESPACE_EDEFAULT == null ? namespace != null : !NAMESPACE_EDEFAULT.equals(namespace);
			case ServiceMetamodelPackage.COMPLEX_TYPE__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_ATTRIBUTES:
				return referencedAttributes != null && !referencedAttributes.isEmpty();
			case ServiceMetamodelPackage.COMPLEX_TYPE__IS_ENUMERATION:
				return isSetIsEnumeration();
			case ServiceMetamodelPackage.COMPLEX_TYPE__IS_FAULT:
				return isSetIsFault();
			case ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_MESSAGES:
				return referencedMessages != null && !referencedMessages.isEmpty();
			case ServiceMetamodelPackage.COMPLEX_TYPE__DIAGRAMS:
				return diagrams != null && !diagrams.isEmpty();
			case ServiceMetamodelPackage.COMPLEX_TYPE__ATTACHMENTS:
				return attachments != null && !attachments.isEmpty();
			case ServiceMetamodelPackage.COMPLEX_TYPE__GENERALIZATIONS:
				return generalizations != null && !generalizations.isEmpty();
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
		result.append(", version: ");
		result.append(version);
		result.append(", namespace: ");
		result.append(namespace);
		result.append(", description: ");
		result.append(description);
		result.append(", isEnumeration: ");
		if (isEnumerationESet) result.append(isEnumeration); else result.append("<unset>");
		result.append(", isFault: ");
		if (isFaultESet) result.append(isFault); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //ComplexTypeImpl
