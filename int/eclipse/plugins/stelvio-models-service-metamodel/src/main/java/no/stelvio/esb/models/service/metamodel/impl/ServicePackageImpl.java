/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package no.stelvio.esb.models.service.metamodel.impl;

import java.util.Collection;

import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.Diagram;
import no.stelvio.esb.models.service.metamodel.ServiceInterface;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;
import no.stelvio.esb.models.service.metamodel.ServicePackage;

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
 * An implementation of the model object '<em><b>Service Package</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl#getName <em>Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl#getServiceInterface <em>Service Interface</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl#getComplexTypes <em>Complex Types</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl#getChildPackages <em>Child Packages</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl#getUUID <em>UUID</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl#getDiagrams <em>Diagrams</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServicePackageImpl extends EObjectImpl implements ServicePackage {
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
	 * The cached value of the '{@link #getServiceInterface() <em>Service Interface</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceInterface()
	 * @generated
	 * @ordered
	 */
	protected EList<ServiceInterface> serviceInterface;

	/**
	 * The cached value of the '{@link #getComplexTypes() <em>Complex Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComplexTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<ComplexType> complexTypes;

	/**
	 * The cached value of the '{@link #getChildPackages() <em>Child Packages</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildPackages()
	 * @generated
	 * @ordered
	 */
	protected EList<ServicePackage> childPackages;

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
	 * The cached value of the '{@link #getDiagrams() <em>Diagrams</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagrams()
	 * @generated
	 * @ordered
	 */
	protected EList<Diagram> diagrams;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ServicePackageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ServiceMetamodelPackage.Literals.SERVICE_PACKAGE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_PACKAGE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ServiceInterface> getServiceInterface() {
		if (serviceInterface == null) {
			serviceInterface = new EObjectContainmentEList<ServiceInterface>(ServiceInterface.class, this, ServiceMetamodelPackage.SERVICE_PACKAGE__SERVICE_INTERFACE);
		}
		return serviceInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComplexType> getComplexTypes() {
		if (complexTypes == null) {
			complexTypes = new EObjectContainmentEList<ComplexType>(ComplexType.class, this, ServiceMetamodelPackage.SERVICE_PACKAGE__COMPLEX_TYPES);
		}
		return complexTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ServicePackage> getChildPackages() {
		if (childPackages == null) {
			childPackages = new EObjectContainmentEList<ServicePackage>(ServicePackage.class, this, ServiceMetamodelPackage.SERVICE_PACKAGE__CHILD_PACKAGES);
		}
		return childPackages;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_PACKAGE__UUID, oldUUID, uuid));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.SERVICE_PACKAGE__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Diagram> getDiagrams() {
		if (diagrams == null) {
			diagrams = new EObjectContainmentEList<Diagram>(Diagram.class, this, ServiceMetamodelPackage.SERVICE_PACKAGE__DIAGRAMS);
		}
		return diagrams;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ServiceMetamodelPackage.SERVICE_PACKAGE__SERVICE_INTERFACE:
				return ((InternalEList<?>)getServiceInterface()).basicRemove(otherEnd, msgs);
			case ServiceMetamodelPackage.SERVICE_PACKAGE__COMPLEX_TYPES:
				return ((InternalEList<?>)getComplexTypes()).basicRemove(otherEnd, msgs);
			case ServiceMetamodelPackage.SERVICE_PACKAGE__CHILD_PACKAGES:
				return ((InternalEList<?>)getChildPackages()).basicRemove(otherEnd, msgs);
			case ServiceMetamodelPackage.SERVICE_PACKAGE__DIAGRAMS:
				return ((InternalEList<?>)getDiagrams()).basicRemove(otherEnd, msgs);
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
			case ServiceMetamodelPackage.SERVICE_PACKAGE__NAME:
				return getName();
			case ServiceMetamodelPackage.SERVICE_PACKAGE__SERVICE_INTERFACE:
				return getServiceInterface();
			case ServiceMetamodelPackage.SERVICE_PACKAGE__COMPLEX_TYPES:
				return getComplexTypes();
			case ServiceMetamodelPackage.SERVICE_PACKAGE__CHILD_PACKAGES:
				return getChildPackages();
			case ServiceMetamodelPackage.SERVICE_PACKAGE__UUID:
				return getUUID();
			case ServiceMetamodelPackage.SERVICE_PACKAGE__DESCRIPTION:
				return getDescription();
			case ServiceMetamodelPackage.SERVICE_PACKAGE__DIAGRAMS:
				return getDiagrams();
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
			case ServiceMetamodelPackage.SERVICE_PACKAGE__NAME:
				setName((String)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__SERVICE_INTERFACE:
				getServiceInterface().clear();
				getServiceInterface().addAll((Collection<? extends ServiceInterface>)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__COMPLEX_TYPES:
				getComplexTypes().clear();
				getComplexTypes().addAll((Collection<? extends ComplexType>)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__CHILD_PACKAGES:
				getChildPackages().clear();
				getChildPackages().addAll((Collection<? extends ServicePackage>)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__UUID:
				setUUID((String)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__DIAGRAMS:
				getDiagrams().clear();
				getDiagrams().addAll((Collection<? extends Diagram>)newValue);
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
			case ServiceMetamodelPackage.SERVICE_PACKAGE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__SERVICE_INTERFACE:
				getServiceInterface().clear();
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__COMPLEX_TYPES:
				getComplexTypes().clear();
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__CHILD_PACKAGES:
				getChildPackages().clear();
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__UUID:
				setUUID(UUID_EDEFAULT);
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case ServiceMetamodelPackage.SERVICE_PACKAGE__DIAGRAMS:
				getDiagrams().clear();
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
			case ServiceMetamodelPackage.SERVICE_PACKAGE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ServiceMetamodelPackage.SERVICE_PACKAGE__SERVICE_INTERFACE:
				return serviceInterface != null && !serviceInterface.isEmpty();
			case ServiceMetamodelPackage.SERVICE_PACKAGE__COMPLEX_TYPES:
				return complexTypes != null && !complexTypes.isEmpty();
			case ServiceMetamodelPackage.SERVICE_PACKAGE__CHILD_PACKAGES:
				return childPackages != null && !childPackages.isEmpty();
			case ServiceMetamodelPackage.SERVICE_PACKAGE__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case ServiceMetamodelPackage.SERVICE_PACKAGE__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case ServiceMetamodelPackage.SERVICE_PACKAGE__DIAGRAMS:
				return diagrams != null && !diagrams.isEmpty();
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
		result.append(')');
		return result.toString();
	}

} //ServicePackageImpl
