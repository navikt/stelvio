/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package no.stelvio.esb.models.service.metamodel.impl;

import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.Fault;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fault</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.FaultImpl#getId <em>Id</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.FaultImpl#getFaultType <em>Fault Type</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.FaultImpl#getProducerFaultRef <em>Producer Fault Ref</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.FaultImpl#getTypeRef <em>Type Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FaultImpl extends EObjectImpl implements Fault {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getFaultType() <em>Fault Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFaultType()
	 * @generated
	 * @ordered
	 */
	protected static final String FAULT_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFaultType() <em>Fault Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFaultType()
	 * @generated
	 * @ordered
	 */
	protected String faultType = FAULT_TYPE_EDEFAULT;

	/**
	 * This is true if the Fault Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean faultTypeESet;

	/**
	 * The default value of the '{@link #getProducerFaultRef() <em>Producer Fault Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProducerFaultRef()
	 * @generated
	 * @ordered
	 */
	protected static final String PRODUCER_FAULT_REF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProducerFaultRef() <em>Producer Fault Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProducerFaultRef()
	 * @generated
	 * @ordered
	 */
	protected String producerFaultRef = PRODUCER_FAULT_REF_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FaultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ServiceMetamodelPackage.Literals.FAULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.FAULT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFaultType() {
		return faultType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFaultType(String newFaultType) {
		String oldFaultType = faultType;
		faultType = newFaultType;
		boolean oldFaultTypeESet = faultTypeESet;
		faultTypeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.FAULT__FAULT_TYPE, oldFaultType, faultType, !oldFaultTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetFaultType() {
		String oldFaultType = faultType;
		boolean oldFaultTypeESet = faultTypeESet;
		faultType = FAULT_TYPE_EDEFAULT;
		faultTypeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.FAULT__FAULT_TYPE, oldFaultType, FAULT_TYPE_EDEFAULT, oldFaultTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetFaultType() {
		return faultTypeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProducerFaultRef() {
		return producerFaultRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProducerFaultRef(String newProducerFaultRef) {
		String oldProducerFaultRef = producerFaultRef;
		producerFaultRef = newProducerFaultRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.FAULT__PRODUCER_FAULT_REF, oldProducerFaultRef, producerFaultRef));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ServiceMetamodelPackage.FAULT__TYPE_REF, oldTypeRef, typeRef));
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
	public void setTypeRef(ComplexType newTypeRef) {
		ComplexType oldTypeRef = typeRef;
		typeRef = newTypeRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.FAULT__TYPE_REF, oldTypeRef, typeRef));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ServiceMetamodelPackage.FAULT__ID:
				return getId();
			case ServiceMetamodelPackage.FAULT__FAULT_TYPE:
				return getFaultType();
			case ServiceMetamodelPackage.FAULT__PRODUCER_FAULT_REF:
				return getProducerFaultRef();
			case ServiceMetamodelPackage.FAULT__TYPE_REF:
				if (resolve) return getTypeRef();
				return basicGetTypeRef();
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
			case ServiceMetamodelPackage.FAULT__ID:
				setId((String)newValue);
				return;
			case ServiceMetamodelPackage.FAULT__FAULT_TYPE:
				setFaultType((String)newValue);
				return;
			case ServiceMetamodelPackage.FAULT__PRODUCER_FAULT_REF:
				setProducerFaultRef((String)newValue);
				return;
			case ServiceMetamodelPackage.FAULT__TYPE_REF:
				setTypeRef((ComplexType)newValue);
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
			case ServiceMetamodelPackage.FAULT__ID:
				setId(ID_EDEFAULT);
				return;
			case ServiceMetamodelPackage.FAULT__FAULT_TYPE:
				unsetFaultType();
				return;
			case ServiceMetamodelPackage.FAULT__PRODUCER_FAULT_REF:
				setProducerFaultRef(PRODUCER_FAULT_REF_EDEFAULT);
				return;
			case ServiceMetamodelPackage.FAULT__TYPE_REF:
				setTypeRef((ComplexType)null);
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
			case ServiceMetamodelPackage.FAULT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case ServiceMetamodelPackage.FAULT__FAULT_TYPE:
				return isSetFaultType();
			case ServiceMetamodelPackage.FAULT__PRODUCER_FAULT_REF:
				return PRODUCER_FAULT_REF_EDEFAULT == null ? producerFaultRef != null : !PRODUCER_FAULT_REF_EDEFAULT.equals(producerFaultRef);
			case ServiceMetamodelPackage.FAULT__TYPE_REF:
				return typeRef != null;
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
		result.append(" (id: ");
		result.append(id);
		result.append(", faultType: ");
		if (faultTypeESet) result.append(faultType); else result.append("<unset>");
		result.append(", producerFaultRef: ");
		result.append(producerFaultRef);
		result.append(')');
		return result.toString();
	}

} //FaultImpl
