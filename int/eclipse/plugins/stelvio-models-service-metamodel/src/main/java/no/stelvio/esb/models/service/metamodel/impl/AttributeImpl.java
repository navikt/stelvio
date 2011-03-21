/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package no.stelvio.esb.models.service.metamodel.impl;

import no.stelvio.esb.models.service.metamodel.Attribute;
import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.AttributeImpl#getName <em>Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.AttributeImpl#getTypeRef <em>Type Ref</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.AttributeImpl#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.AttributeImpl#isIsList <em>Is List</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.AttributeImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.AttributeImpl#isIsRequired <em>Is Required</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.AttributeImpl#getUUID <em>UUID</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.AttributeImpl#getMappingToAttributeName <em>Mapping To Attribute Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeImpl extends EObjectImpl implements Attribute {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = "\"<missing attribute name>\"";

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
	 * This is true if the Name attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean nameESet;

	/**
	 * The cached value of the '{@link #getTypeRef() <em>Type Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeRef()
	 * @generated
	 * @ordered
	 */
	protected ComplexType typeRef;

	/**
	 * The default value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeName()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_NAME_EDEFAULT = "\"<missing data type name>\"";

	/**
	 * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeName()
	 * @generated
	 * @ordered
	 */
	protected String typeName = TYPE_NAME_EDEFAULT;

	/**
	 * This is true if the Type Name attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean typeNameESet;

	/**
	 * The default value of the '{@link #isIsList() <em>Is List</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsList()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_LIST_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsList() <em>Is List</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsList()
	 * @generated
	 * @ordered
	 */
	protected boolean isList = IS_LIST_EDEFAULT;

	/**
	 * This is true if the Is List attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean isListESet;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = "\"no description\"";

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
	 * This is true if the Description attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean descriptionESet;

	/**
	 * The default value of the '{@link #isIsRequired() <em>Is Required</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsRequired()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_REQUIRED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsRequired() <em>Is Required</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsRequired()
	 * @generated
	 * @ordered
	 */
	protected boolean isRequired = IS_REQUIRED_EDEFAULT;

	/**
	 * This is true if the Is Required attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean isRequiredESet;

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
	 * The default value of the '{@link #getMappingToAttributeName() <em>Mapping To Attribute Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMappingToAttributeName()
	 * @generated
	 * @ordered
	 */
	protected static final String MAPPING_TO_ATTRIBUTE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMappingToAttributeName() <em>Mapping To Attribute Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMappingToAttributeName()
	 * @generated
	 * @ordered
	 */
	protected String mappingToAttributeName = MAPPING_TO_ATTRIBUTE_NAME_EDEFAULT;

	/**
	 * This is true if the Mapping To Attribute Name attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean mappingToAttributeNameESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AttributeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ServiceMetamodelPackage.Literals.ATTRIBUTE;
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
		boolean oldNameESet = nameESet;
		nameESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.ATTRIBUTE__NAME, oldName, name, !oldNameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetName() {
		String oldName = name;
		boolean oldNameESet = nameESet;
		name = NAME_EDEFAULT;
		nameESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.ATTRIBUTE__NAME, oldName, NAME_EDEFAULT, oldNameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetName() {
		return nameESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComplexType getTypeRef() {
		if (typeRef != null && typeRef.eIsProxy()) {
			InternalEObject oldTypeRef = (InternalEObject)typeRef;
			typeRef = (ComplexType)eResolveProxy(oldTypeRef);
			if (typeRef != oldTypeRef) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ServiceMetamodelPackage.ATTRIBUTE__TYPE_REF, oldTypeRef, typeRef));
			}
		}
		return typeRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComplexType basicGetTypeRef() {
		return typeRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTypeRef(ComplexType newTypeRef, NotificationChain msgs) {
		ComplexType oldTypeRef = typeRef;
		typeRef = newTypeRef;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.ATTRIBUTE__TYPE_REF, oldTypeRef, newTypeRef);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypeRef(ComplexType newTypeRef) {
		if (newTypeRef != typeRef) {
			NotificationChain msgs = null;
			if (typeRef != null)
				msgs = ((InternalEObject)typeRef).eInverseRemove(this, ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_ATTRIBUTES, ComplexType.class, msgs);
			if (newTypeRef != null)
				msgs = ((InternalEObject)newTypeRef).eInverseAdd(this, ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_ATTRIBUTES, ComplexType.class, msgs);
			msgs = basicSetTypeRef(newTypeRef, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.ATTRIBUTE__TYPE_REF, newTypeRef, newTypeRef));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypeName(String newTypeName) {
		String oldTypeName = typeName;
		typeName = newTypeName;
		boolean oldTypeNameESet = typeNameESet;
		typeNameESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.ATTRIBUTE__TYPE_NAME, oldTypeName, typeName, !oldTypeNameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetTypeName() {
		String oldTypeName = typeName;
		boolean oldTypeNameESet = typeNameESet;
		typeName = TYPE_NAME_EDEFAULT;
		typeNameESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.ATTRIBUTE__TYPE_NAME, oldTypeName, TYPE_NAME_EDEFAULT, oldTypeNameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetTypeName() {
		return typeNameESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsList() {
		return isList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsList(boolean newIsList) {
		boolean oldIsList = isList;
		isList = newIsList;
		boolean oldIsListESet = isListESet;
		isListESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.ATTRIBUTE__IS_LIST, oldIsList, isList, !oldIsListESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetIsList() {
		boolean oldIsList = isList;
		boolean oldIsListESet = isListESet;
		isList = IS_LIST_EDEFAULT;
		isListESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.ATTRIBUTE__IS_LIST, oldIsList, IS_LIST_EDEFAULT, oldIsListESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetIsList() {
		return isListESet;
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
		boolean oldDescriptionESet = descriptionESet;
		descriptionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.ATTRIBUTE__DESCRIPTION, oldDescription, description, !oldDescriptionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDescription() {
		String oldDescription = description;
		boolean oldDescriptionESet = descriptionESet;
		description = DESCRIPTION_EDEFAULT;
		descriptionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.ATTRIBUTE__DESCRIPTION, oldDescription, DESCRIPTION_EDEFAULT, oldDescriptionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDescription() {
		return descriptionESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsRequired() {
		return isRequired;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsRequired(boolean newIsRequired) {
		boolean oldIsRequired = isRequired;
		isRequired = newIsRequired;
		boolean oldIsRequiredESet = isRequiredESet;
		isRequiredESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.ATTRIBUTE__IS_REQUIRED, oldIsRequired, isRequired, !oldIsRequiredESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetIsRequired() {
		boolean oldIsRequired = isRequired;
		boolean oldIsRequiredESet = isRequiredESet;
		isRequired = IS_REQUIRED_EDEFAULT;
		isRequiredESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.ATTRIBUTE__IS_REQUIRED, oldIsRequired, IS_REQUIRED_EDEFAULT, oldIsRequiredESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetIsRequired() {
		return isRequiredESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.ATTRIBUTE__UUID, oldUUID, uuid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMappingToAttributeName() {
		return mappingToAttributeName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMappingToAttributeName(String newMappingToAttributeName) {
		String oldMappingToAttributeName = mappingToAttributeName;
		mappingToAttributeName = newMappingToAttributeName;
		boolean oldMappingToAttributeNameESet = mappingToAttributeNameESet;
		mappingToAttributeNameESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.ATTRIBUTE__MAPPING_TO_ATTRIBUTE_NAME, oldMappingToAttributeName, mappingToAttributeName, !oldMappingToAttributeNameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMappingToAttributeName() {
		String oldMappingToAttributeName = mappingToAttributeName;
		boolean oldMappingToAttributeNameESet = mappingToAttributeNameESet;
		mappingToAttributeName = MAPPING_TO_ATTRIBUTE_NAME_EDEFAULT;
		mappingToAttributeNameESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.ATTRIBUTE__MAPPING_TO_ATTRIBUTE_NAME, oldMappingToAttributeName, MAPPING_TO_ATTRIBUTE_NAME_EDEFAULT, oldMappingToAttributeNameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMappingToAttributeName() {
		return mappingToAttributeNameESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ServiceMetamodelPackage.ATTRIBUTE__TYPE_REF:
				if (typeRef != null)
					msgs = ((InternalEObject)typeRef).eInverseRemove(this, ServiceMetamodelPackage.COMPLEX_TYPE__REFERENCED_ATTRIBUTES, ComplexType.class, msgs);
				return basicSetTypeRef((ComplexType)otherEnd, msgs);
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
			case ServiceMetamodelPackage.ATTRIBUTE__TYPE_REF:
				return basicSetTypeRef(null, msgs);
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
			case ServiceMetamodelPackage.ATTRIBUTE__NAME:
				return getName();
			case ServiceMetamodelPackage.ATTRIBUTE__TYPE_REF:
				if (resolve) return getTypeRef();
				return basicGetTypeRef();
			case ServiceMetamodelPackage.ATTRIBUTE__TYPE_NAME:
				return getTypeName();
			case ServiceMetamodelPackage.ATTRIBUTE__IS_LIST:
				return isIsList();
			case ServiceMetamodelPackage.ATTRIBUTE__DESCRIPTION:
				return getDescription();
			case ServiceMetamodelPackage.ATTRIBUTE__IS_REQUIRED:
				return isIsRequired();
			case ServiceMetamodelPackage.ATTRIBUTE__UUID:
				return getUUID();
			case ServiceMetamodelPackage.ATTRIBUTE__MAPPING_TO_ATTRIBUTE_NAME:
				return getMappingToAttributeName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ServiceMetamodelPackage.ATTRIBUTE__NAME:
				setName((String)newValue);
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__TYPE_REF:
				setTypeRef((ComplexType)newValue);
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__TYPE_NAME:
				setTypeName((String)newValue);
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__IS_LIST:
				setIsList((Boolean)newValue);
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__IS_REQUIRED:
				setIsRequired((Boolean)newValue);
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__UUID:
				setUUID((String)newValue);
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__MAPPING_TO_ATTRIBUTE_NAME:
				setMappingToAttributeName((String)newValue);
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
			case ServiceMetamodelPackage.ATTRIBUTE__NAME:
				unsetName();
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__TYPE_REF:
				setTypeRef((ComplexType)null);
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__TYPE_NAME:
				unsetTypeName();
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__IS_LIST:
				unsetIsList();
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__DESCRIPTION:
				unsetDescription();
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__IS_REQUIRED:
				unsetIsRequired();
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__UUID:
				setUUID(UUID_EDEFAULT);
				return;
			case ServiceMetamodelPackage.ATTRIBUTE__MAPPING_TO_ATTRIBUTE_NAME:
				unsetMappingToAttributeName();
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
			case ServiceMetamodelPackage.ATTRIBUTE__NAME:
				return isSetName();
			case ServiceMetamodelPackage.ATTRIBUTE__TYPE_REF:
				return typeRef != null;
			case ServiceMetamodelPackage.ATTRIBUTE__TYPE_NAME:
				return isSetTypeName();
			case ServiceMetamodelPackage.ATTRIBUTE__IS_LIST:
				return isSetIsList();
			case ServiceMetamodelPackage.ATTRIBUTE__DESCRIPTION:
				return isSetDescription();
			case ServiceMetamodelPackage.ATTRIBUTE__IS_REQUIRED:
				return isSetIsRequired();
			case ServiceMetamodelPackage.ATTRIBUTE__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case ServiceMetamodelPackage.ATTRIBUTE__MAPPING_TO_ATTRIBUTE_NAME:
				return isSetMappingToAttributeName();
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
		if (nameESet) result.append(name); else result.append("<unset>");
		result.append(", typeName: ");
		if (typeNameESet) result.append(typeName); else result.append("<unset>");
		result.append(", isList: ");
		if (isListESet) result.append(isList); else result.append("<unset>");
		result.append(", description: ");
		if (descriptionESet) result.append(description); else result.append("<unset>");
		result.append(", isRequired: ");
		if (isRequiredESet) result.append(isRequired); else result.append("<unset>");
		result.append(", UUID: ");
		result.append(uuid);
		result.append(", mappingToAttributeName: ");
		if (mappingToAttributeNameESet) result.append(mappingToAttributeName); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //AttributeImpl
