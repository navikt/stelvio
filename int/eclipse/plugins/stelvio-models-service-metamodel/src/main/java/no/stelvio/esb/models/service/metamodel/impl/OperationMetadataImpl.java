/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package no.stelvio.esb.models.service.metamodel.impl;

import no.stelvio.esb.models.service.metamodel.OperationMetadata;
import no.stelvio.esb.models.service.metamodel.Scope;
import no.stelvio.esb.models.service.metamodel.ServiceCatagory;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Metadata</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getId <em>Id</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getErrorHandling <em>Error Handling</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getTransactions <em>Transactions</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getProducerServiceRef <em>Producer Service Ref</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getStateManagement <em>State Management</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getProducerComponent <em>Producer Component</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#isIsUsedByBatch <em>Is Used By Batch</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getServiceCategory <em>Service Category</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getScope <em>Scope</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getResponseTime <em>Response Time</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getVolumeCapacity <em>Volume Capacity</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getUptime <em>Uptime</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getProceessingRules <em>Proceessing Rules</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl#getSecurity <em>Security</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationMetadataImpl extends EObjectImpl implements OperationMetadata {
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
	 * The default value of the '{@link #getErrorHandling() <em>Error Handling</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getErrorHandling()
	 * @generated
	 * @ordered
	 */
	protected static final String ERROR_HANDLING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getErrorHandling() <em>Error Handling</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getErrorHandling()
	 * @generated
	 * @ordered
	 */
	protected String errorHandling = ERROR_HANDLING_EDEFAULT;

	/**
	 * The default value of the '{@link #getTransactions() <em>Transactions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransactions()
	 * @generated
	 * @ordered
	 */
	protected static final String TRANSACTIONS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTransactions() <em>Transactions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransactions()
	 * @generated
	 * @ordered
	 */
	protected String transactions = TRANSACTIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getProducerServiceRef() <em>Producer Service Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProducerServiceRef()
	 * @generated
	 * @ordered
	 */
	protected static final String PRODUCER_SERVICE_REF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProducerServiceRef() <em>Producer Service Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProducerServiceRef()
	 * @generated
	 * @ordered
	 */
	protected String producerServiceRef = PRODUCER_SERVICE_REF_EDEFAULT;

	/**
	 * The default value of the '{@link #getStateManagement() <em>State Management</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStateManagement()
	 * @generated
	 * @ordered
	 */
	protected static final String STATE_MANAGEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStateManagement() <em>State Management</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStateManagement()
	 * @generated
	 * @ordered
	 */
	protected String stateManagement = STATE_MANAGEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getProducerComponent() <em>Producer Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProducerComponent()
	 * @generated
	 * @ordered
	 */
	protected static final String PRODUCER_COMPONENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProducerComponent() <em>Producer Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProducerComponent()
	 * @generated
	 * @ordered
	 */
	protected String producerComponent = PRODUCER_COMPONENT_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsUsedByBatch() <em>Is Used By Batch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsUsedByBatch()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_USED_BY_BATCH_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsUsedByBatch() <em>Is Used By Batch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsUsedByBatch()
	 * @generated
	 * @ordered
	 */
	protected boolean isUsedByBatch = IS_USED_BY_BATCH_EDEFAULT;

	/**
	 * This is true if the Is Used By Batch attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean isUsedByBatchESet;

	/**
	 * The cached value of the '{@link #getServiceCategory() <em>Service Category</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceCategory()
	 * @generated
	 * @ordered
	 */
	protected ServiceCatagory serviceCategory;

	/**
	 * The default value of the '{@link #getScope() <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScope()
	 * @generated
	 * @ordered
	 */
	protected static final Scope SCOPE_EDEFAULT = Scope.VIRKSOMHET;

	/**
	 * The cached value of the '{@link #getScope() <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScope()
	 * @generated
	 * @ordered
	 */
	protected Scope scope = SCOPE_EDEFAULT;

	/**
	 * This is true if the Scope attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean scopeESet;

	/**
	 * The default value of the '{@link #getResponseTime() <em>Response Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResponseTime()
	 * @generated
	 * @ordered
	 */
	protected static final String RESPONSE_TIME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getResponseTime() <em>Response Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResponseTime()
	 * @generated
	 * @ordered
	 */
	protected String responseTime = RESPONSE_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeCapacity() <em>Volume Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final String VOLUME_CAPACITY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVolumeCapacity() <em>Volume Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeCapacity()
	 * @generated
	 * @ordered
	 */
	protected String volumeCapacity = VOLUME_CAPACITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getUptime() <em>Uptime</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUptime()
	 * @generated
	 * @ordered
	 */
	protected static final String UPTIME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUptime() <em>Uptime</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUptime()
	 * @generated
	 * @ordered
	 */
	protected String uptime = UPTIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getProceessingRules() <em>Proceessing Rules</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProceessingRules()
	 * @generated
	 * @ordered
	 */
	protected static final String PROCEESSING_RULES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProceessingRules() <em>Proceessing Rules</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProceessingRules()
	 * @generated
	 * @ordered
	 */
	protected String proceessingRules = PROCEESSING_RULES_EDEFAULT;

	/**
	 * The default value of the '{@link #getSecurity() <em>Security</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecurity()
	 * @generated
	 * @ordered
	 */
	protected static final String SECURITY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSecurity() <em>Security</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecurity()
	 * @generated
	 * @ordered
	 */
	protected String security = SECURITY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OperationMetadataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ServiceMetamodelPackage.Literals.OPERATION_METADATA;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getErrorHandling() {
		return errorHandling;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setErrorHandling(String newErrorHandling) {
		String oldErrorHandling = errorHandling;
		errorHandling = newErrorHandling;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__ERROR_HANDLING, oldErrorHandling, errorHandling));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTransactions() {
		return transactions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTransactions(String newTransactions) {
		String oldTransactions = transactions;
		transactions = newTransactions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__TRANSACTIONS, oldTransactions, transactions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProducerServiceRef() {
		return producerServiceRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProducerServiceRef(String newProducerServiceRef) {
		String oldProducerServiceRef = producerServiceRef;
		producerServiceRef = newProducerServiceRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__PRODUCER_SERVICE_REF, oldProducerServiceRef, producerServiceRef));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStateManagement() {
		return stateManagement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStateManagement(String newStateManagement) {
		String oldStateManagement = stateManagement;
		stateManagement = newStateManagement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__STATE_MANAGEMENT, oldStateManagement, stateManagement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProducerComponent() {
		return producerComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProducerComponent(String newProducerComponent) {
		String oldProducerComponent = producerComponent;
		producerComponent = newProducerComponent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__PRODUCER_COMPONENT, oldProducerComponent, producerComponent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsUsedByBatch() {
		return isUsedByBatch;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsUsedByBatch(boolean newIsUsedByBatch) {
		boolean oldIsUsedByBatch = isUsedByBatch;
		isUsedByBatch = newIsUsedByBatch;
		boolean oldIsUsedByBatchESet = isUsedByBatchESet;
		isUsedByBatchESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__IS_USED_BY_BATCH, oldIsUsedByBatch, isUsedByBatch, !oldIsUsedByBatchESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetIsUsedByBatch() {
		boolean oldIsUsedByBatch = isUsedByBatch;
		boolean oldIsUsedByBatchESet = isUsedByBatchESet;
		isUsedByBatch = IS_USED_BY_BATCH_EDEFAULT;
		isUsedByBatchESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.OPERATION_METADATA__IS_USED_BY_BATCH, oldIsUsedByBatch, IS_USED_BY_BATCH_EDEFAULT, oldIsUsedByBatchESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetIsUsedByBatch() {
		return isUsedByBatchESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ServiceCatagory getServiceCategory() {
		return serviceCategory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetServiceCategory(ServiceCatagory newServiceCategory, NotificationChain msgs) {
		ServiceCatagory oldServiceCategory = serviceCategory;
		serviceCategory = newServiceCategory;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__SERVICE_CATEGORY, oldServiceCategory, newServiceCategory);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceCategory(ServiceCatagory newServiceCategory) {
		if (newServiceCategory != serviceCategory) {
			NotificationChain msgs = null;
			if (serviceCategory != null)
				msgs = ((InternalEObject)serviceCategory).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ServiceMetamodelPackage.OPERATION_METADATA__SERVICE_CATEGORY, null, msgs);
			if (newServiceCategory != null)
				msgs = ((InternalEObject)newServiceCategory).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ServiceMetamodelPackage.OPERATION_METADATA__SERVICE_CATEGORY, null, msgs);
			msgs = basicSetServiceCategory(newServiceCategory, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__SERVICE_CATEGORY, newServiceCategory, newServiceCategory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Scope getScope() {
		return scope;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScope(Scope newScope) {
		Scope oldScope = scope;
		scope = newScope == null ? SCOPE_EDEFAULT : newScope;
		boolean oldScopeESet = scopeESet;
		scopeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__SCOPE, oldScope, scope, !oldScopeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetScope() {
		Scope oldScope = scope;
		boolean oldScopeESet = scopeESet;
		scope = SCOPE_EDEFAULT;
		scopeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ServiceMetamodelPackage.OPERATION_METADATA__SCOPE, oldScope, SCOPE_EDEFAULT, oldScopeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetScope() {
		return scopeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getResponseTime() {
		return responseTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResponseTime(String newResponseTime) {
		String oldResponseTime = responseTime;
		responseTime = newResponseTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__RESPONSE_TIME, oldResponseTime, responseTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVolumeCapacity() {
		return volumeCapacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeCapacity(String newVolumeCapacity) {
		String oldVolumeCapacity = volumeCapacity;
		volumeCapacity = newVolumeCapacity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__VOLUME_CAPACITY, oldVolumeCapacity, volumeCapacity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUptime() {
		return uptime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUptime(String newUptime) {
		String oldUptime = uptime;
		uptime = newUptime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__UPTIME, oldUptime, uptime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProceessingRules() {
		return proceessingRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProceessingRules(String newProceessingRules) {
		String oldProceessingRules = proceessingRules;
		proceessingRules = newProceessingRules;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__PROCEESSING_RULES, oldProceessingRules, proceessingRules));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSecurity() {
		return security;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSecurity(String newSecurity) {
		String oldSecurity = security;
		security = newSecurity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ServiceMetamodelPackage.OPERATION_METADATA__SECURITY, oldSecurity, security));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ServiceMetamodelPackage.OPERATION_METADATA__SERVICE_CATEGORY:
				return basicSetServiceCategory(null, msgs);
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
			case ServiceMetamodelPackage.OPERATION_METADATA__ID:
				return getId();
			case ServiceMetamodelPackage.OPERATION_METADATA__ERROR_HANDLING:
				return getErrorHandling();
			case ServiceMetamodelPackage.OPERATION_METADATA__TRANSACTIONS:
				return getTransactions();
			case ServiceMetamodelPackage.OPERATION_METADATA__PRODUCER_SERVICE_REF:
				return getProducerServiceRef();
			case ServiceMetamodelPackage.OPERATION_METADATA__STATE_MANAGEMENT:
				return getStateManagement();
			case ServiceMetamodelPackage.OPERATION_METADATA__PRODUCER_COMPONENT:
				return getProducerComponent();
			case ServiceMetamodelPackage.OPERATION_METADATA__IS_USED_BY_BATCH:
				return isIsUsedByBatch();
			case ServiceMetamodelPackage.OPERATION_METADATA__SERVICE_CATEGORY:
				return getServiceCategory();
			case ServiceMetamodelPackage.OPERATION_METADATA__SCOPE:
				return getScope();
			case ServiceMetamodelPackage.OPERATION_METADATA__RESPONSE_TIME:
				return getResponseTime();
			case ServiceMetamodelPackage.OPERATION_METADATA__VOLUME_CAPACITY:
				return getVolumeCapacity();
			case ServiceMetamodelPackage.OPERATION_METADATA__UPTIME:
				return getUptime();
			case ServiceMetamodelPackage.OPERATION_METADATA__PROCEESSING_RULES:
				return getProceessingRules();
			case ServiceMetamodelPackage.OPERATION_METADATA__SECURITY:
				return getSecurity();
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
			case ServiceMetamodelPackage.OPERATION_METADATA__ID:
				setId((String)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__ERROR_HANDLING:
				setErrorHandling((String)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__TRANSACTIONS:
				setTransactions((String)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__PRODUCER_SERVICE_REF:
				setProducerServiceRef((String)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__STATE_MANAGEMENT:
				setStateManagement((String)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__PRODUCER_COMPONENT:
				setProducerComponent((String)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__IS_USED_BY_BATCH:
				setIsUsedByBatch((Boolean)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__SERVICE_CATEGORY:
				setServiceCategory((ServiceCatagory)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__SCOPE:
				setScope((Scope)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__RESPONSE_TIME:
				setResponseTime((String)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__VOLUME_CAPACITY:
				setVolumeCapacity((String)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__UPTIME:
				setUptime((String)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__PROCEESSING_RULES:
				setProceessingRules((String)newValue);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__SECURITY:
				setSecurity((String)newValue);
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
			case ServiceMetamodelPackage.OPERATION_METADATA__ID:
				setId(ID_EDEFAULT);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__ERROR_HANDLING:
				setErrorHandling(ERROR_HANDLING_EDEFAULT);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__TRANSACTIONS:
				setTransactions(TRANSACTIONS_EDEFAULT);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__PRODUCER_SERVICE_REF:
				setProducerServiceRef(PRODUCER_SERVICE_REF_EDEFAULT);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__STATE_MANAGEMENT:
				setStateManagement(STATE_MANAGEMENT_EDEFAULT);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__PRODUCER_COMPONENT:
				setProducerComponent(PRODUCER_COMPONENT_EDEFAULT);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__IS_USED_BY_BATCH:
				unsetIsUsedByBatch();
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__SERVICE_CATEGORY:
				setServiceCategory((ServiceCatagory)null);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__SCOPE:
				unsetScope();
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__RESPONSE_TIME:
				setResponseTime(RESPONSE_TIME_EDEFAULT);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__VOLUME_CAPACITY:
				setVolumeCapacity(VOLUME_CAPACITY_EDEFAULT);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__UPTIME:
				setUptime(UPTIME_EDEFAULT);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__PROCEESSING_RULES:
				setProceessingRules(PROCEESSING_RULES_EDEFAULT);
				return;
			case ServiceMetamodelPackage.OPERATION_METADATA__SECURITY:
				setSecurity(SECURITY_EDEFAULT);
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
			case ServiceMetamodelPackage.OPERATION_METADATA__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case ServiceMetamodelPackage.OPERATION_METADATA__ERROR_HANDLING:
				return ERROR_HANDLING_EDEFAULT == null ? errorHandling != null : !ERROR_HANDLING_EDEFAULT.equals(errorHandling);
			case ServiceMetamodelPackage.OPERATION_METADATA__TRANSACTIONS:
				return TRANSACTIONS_EDEFAULT == null ? transactions != null : !TRANSACTIONS_EDEFAULT.equals(transactions);
			case ServiceMetamodelPackage.OPERATION_METADATA__PRODUCER_SERVICE_REF:
				return PRODUCER_SERVICE_REF_EDEFAULT == null ? producerServiceRef != null : !PRODUCER_SERVICE_REF_EDEFAULT.equals(producerServiceRef);
			case ServiceMetamodelPackage.OPERATION_METADATA__STATE_MANAGEMENT:
				return STATE_MANAGEMENT_EDEFAULT == null ? stateManagement != null : !STATE_MANAGEMENT_EDEFAULT.equals(stateManagement);
			case ServiceMetamodelPackage.OPERATION_METADATA__PRODUCER_COMPONENT:
				return PRODUCER_COMPONENT_EDEFAULT == null ? producerComponent != null : !PRODUCER_COMPONENT_EDEFAULT.equals(producerComponent);
			case ServiceMetamodelPackage.OPERATION_METADATA__IS_USED_BY_BATCH:
				return isSetIsUsedByBatch();
			case ServiceMetamodelPackage.OPERATION_METADATA__SERVICE_CATEGORY:
				return serviceCategory != null;
			case ServiceMetamodelPackage.OPERATION_METADATA__SCOPE:
				return isSetScope();
			case ServiceMetamodelPackage.OPERATION_METADATA__RESPONSE_TIME:
				return RESPONSE_TIME_EDEFAULT == null ? responseTime != null : !RESPONSE_TIME_EDEFAULT.equals(responseTime);
			case ServiceMetamodelPackage.OPERATION_METADATA__VOLUME_CAPACITY:
				return VOLUME_CAPACITY_EDEFAULT == null ? volumeCapacity != null : !VOLUME_CAPACITY_EDEFAULT.equals(volumeCapacity);
			case ServiceMetamodelPackage.OPERATION_METADATA__UPTIME:
				return UPTIME_EDEFAULT == null ? uptime != null : !UPTIME_EDEFAULT.equals(uptime);
			case ServiceMetamodelPackage.OPERATION_METADATA__PROCEESSING_RULES:
				return PROCEESSING_RULES_EDEFAULT == null ? proceessingRules != null : !PROCEESSING_RULES_EDEFAULT.equals(proceessingRules);
			case ServiceMetamodelPackage.OPERATION_METADATA__SECURITY:
				return SECURITY_EDEFAULT == null ? security != null : !SECURITY_EDEFAULT.equals(security);
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
		result.append(", errorHandling: ");
		result.append(errorHandling);
		result.append(", transactions: ");
		result.append(transactions);
		result.append(", producerServiceRef: ");
		result.append(producerServiceRef);
		result.append(", stateManagement: ");
		result.append(stateManagement);
		result.append(", producerComponent: ");
		result.append(producerComponent);
		result.append(", isUsedByBatch: ");
		if (isUsedByBatchESet) result.append(isUsedByBatch); else result.append("<unset>");
		result.append(", scope: ");
		if (scopeESet) result.append(scope); else result.append("<unset>");
		result.append(", responseTime: ");
		result.append(responseTime);
		result.append(", volumeCapacity: ");
		result.append(volumeCapacity);
		result.append(", uptime: ");
		result.append(uptime);
		result.append(", proceessingRules: ");
		result.append(proceessingRules);
		result.append(", security: ");
		result.append(security);
		result.append(')');
		return result.toString();
	}

} //OperationMetadataImpl
