/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package no.stelvio.esb.models.service.metamodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Metadata</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getId <em>Id</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getErrorHandling <em>Error Handling</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getTransactions <em>Transactions</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getProducerServiceRef <em>Producer Service Ref</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getStateManagement <em>State Management</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getProducerComponent <em>Producer Component</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#isIsUsedByBatch <em>Is Used By Batch</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getServiceCategory <em>Service Category</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getScope <em>Scope</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getResponseTime <em>Response Time</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getVolumeCapacity <em>Volume Capacity</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getUptime <em>Uptime</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getProceessingRules <em>Proceessing Rules</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getSecurity <em>Security</em>}</li>
 * </ul>
 * </p>
 *
 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata()
 * @model
 * @generated
 */
public interface OperationMetadata extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Error Handling</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Error Handling</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Error Handling</em>' attribute.
	 * @see #setErrorHandling(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_ErrorHandling()
	 * @model
	 * @generated
	 */
	String getErrorHandling();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getErrorHandling <em>Error Handling</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Error Handling</em>' attribute.
	 * @see #getErrorHandling()
	 * @generated
	 */
	void setErrorHandling(String value);

	/**
	 * Returns the value of the '<em><b>Transactions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transactions</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transactions</em>' attribute.
	 * @see #setTransactions(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_Transactions()
	 * @model
	 * @generated
	 */
	String getTransactions();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getTransactions <em>Transactions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transactions</em>' attribute.
	 * @see #getTransactions()
	 * @generated
	 */
	void setTransactions(String value);

	/**
	 * Returns the value of the '<em><b>Producer Service Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Producer Service Ref</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Producer Service Ref</em>' attribute.
	 * @see #setProducerServiceRef(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_ProducerServiceRef()
	 * @model
	 * @generated
	 */
	String getProducerServiceRef();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getProducerServiceRef <em>Producer Service Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Producer Service Ref</em>' attribute.
	 * @see #getProducerServiceRef()
	 * @generated
	 */
	void setProducerServiceRef(String value);

	/**
	 * Returns the value of the '<em><b>State Management</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>State Management</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>State Management</em>' attribute.
	 * @see #setStateManagement(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_StateManagement()
	 * @model
	 * @generated
	 */
	String getStateManagement();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getStateManagement <em>State Management</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>State Management</em>' attribute.
	 * @see #getStateManagement()
	 * @generated
	 */
	void setStateManagement(String value);

	/**
	 * Returns the value of the '<em><b>Producer Component</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Producer Component</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Producer Component</em>' attribute.
	 * @see #setProducerComponent(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_ProducerComponent()
	 * @model
	 * @generated
	 */
	String getProducerComponent();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getProducerComponent <em>Producer Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Producer Component</em>' attribute.
	 * @see #getProducerComponent()
	 * @generated
	 */
	void setProducerComponent(String value);

	/**
	 * Returns the value of the '<em><b>Is Used By Batch</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Used By Batch</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Used By Batch</em>' attribute.
	 * @see #isSetIsUsedByBatch()
	 * @see #unsetIsUsedByBatch()
	 * @see #setIsUsedByBatch(boolean)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_IsUsedByBatch()
	 * @model default="false" unsettable="true"
	 * @generated
	 */
	boolean isIsUsedByBatch();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#isIsUsedByBatch <em>Is Used By Batch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Used By Batch</em>' attribute.
	 * @see #isSetIsUsedByBatch()
	 * @see #unsetIsUsedByBatch()
	 * @see #isIsUsedByBatch()
	 * @generated
	 */
	void setIsUsedByBatch(boolean value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#isIsUsedByBatch <em>Is Used By Batch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIsUsedByBatch()
	 * @see #isIsUsedByBatch()
	 * @see #setIsUsedByBatch(boolean)
	 * @generated
	 */
	void unsetIsUsedByBatch();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#isIsUsedByBatch <em>Is Used By Batch</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Is Used By Batch</em>' attribute is set.
	 * @see #unsetIsUsedByBatch()
	 * @see #isIsUsedByBatch()
	 * @see #setIsUsedByBatch(boolean)
	 * @generated
	 */
	boolean isSetIsUsedByBatch();

	/**
	 * Returns the value of the '<em><b>Service Category</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Category</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Category</em>' containment reference.
	 * @see #setServiceCategory(ServiceCatagory)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_ServiceCategory()
	 * @model containment="true"
	 * @generated
	 */
	ServiceCatagory getServiceCategory();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getServiceCategory <em>Service Category</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Category</em>' containment reference.
	 * @see #getServiceCategory()
	 * @generated
	 */
	void setServiceCategory(ServiceCatagory value);

	/**
	 * Returns the value of the '<em><b>Scope</b></em>' attribute.
	 * The literals are from the enumeration {@link no.stelvio.esb.models.service.metamodel.Scope}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scope</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scope</em>' attribute.
	 * @see no.stelvio.esb.models.service.metamodel.Scope
	 * @see #isSetScope()
	 * @see #unsetScope()
	 * @see #setScope(Scope)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_Scope()
	 * @model unsettable="true"
	 * @generated
	 */
	Scope getScope();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getScope <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scope</em>' attribute.
	 * @see no.stelvio.esb.models.service.metamodel.Scope
	 * @see #isSetScope()
	 * @see #unsetScope()
	 * @see #getScope()
	 * @generated
	 */
	void setScope(Scope value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getScope <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetScope()
	 * @see #getScope()
	 * @see #setScope(Scope)
	 * @generated
	 */
	void unsetScope();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getScope <em>Scope</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Scope</em>' attribute is set.
	 * @see #unsetScope()
	 * @see #getScope()
	 * @see #setScope(Scope)
	 * @generated
	 */
	boolean isSetScope();

	/**
	 * Returns the value of the '<em><b>Response Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Response Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Response Time</em>' attribute.
	 * @see #setResponseTime(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_ResponseTime()
	 * @model
	 * @generated
	 */
	String getResponseTime();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getResponseTime <em>Response Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Response Time</em>' attribute.
	 * @see #getResponseTime()
	 * @generated
	 */
	void setResponseTime(String value);

	/**
	 * Returns the value of the '<em><b>Volume Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Capacity</em>' attribute.
	 * @see #setVolumeCapacity(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_VolumeCapacity()
	 * @model
	 * @generated
	 */
	String getVolumeCapacity();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getVolumeCapacity <em>Volume Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Capacity</em>' attribute.
	 * @see #getVolumeCapacity()
	 * @generated
	 */
	void setVolumeCapacity(String value);

	/**
	 * Returns the value of the '<em><b>Uptime</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uptime</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uptime</em>' attribute.
	 * @see #setUptime(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_Uptime()
	 * @model
	 * @generated
	 */
	String getUptime();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getUptime <em>Uptime</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uptime</em>' attribute.
	 * @see #getUptime()
	 * @generated
	 */
	void setUptime(String value);

	/**
	 * Returns the value of the '<em><b>Proceessing Rules</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Proceessing Rules</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Proceessing Rules</em>' attribute.
	 * @see #setProceessingRules(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_ProceessingRules()
	 * @model
	 * @generated
	 */
	String getProceessingRules();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getProceessingRules <em>Proceessing Rules</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Proceessing Rules</em>' attribute.
	 * @see #getProceessingRules()
	 * @generated
	 */
	void setProceessingRules(String value);

	/**
	 * Returns the value of the '<em><b>Security</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Security</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Security</em>' attribute.
	 * @see #setSecurity(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getOperationMetadata_Security()
	 * @model
	 * @generated
	 */
	String getSecurity();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getSecurity <em>Security</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Security</em>' attribute.
	 * @see #getSecurity()
	 * @generated
	 */
	void setSecurity(String value);

} // OperationMetadata
