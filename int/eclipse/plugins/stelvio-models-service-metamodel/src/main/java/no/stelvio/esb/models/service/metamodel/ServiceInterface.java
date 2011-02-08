/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package no.stelvio.esb.models.service.metamodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service Interface</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getName <em>Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getServiceOperations <em>Service Operations</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getUUID <em>UUID</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getDescription <em>Description</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceInterface()
 * @model
 * @generated
 */
public interface ServiceInterface extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceInterface_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Service Operations</b></em>' containment reference list.
	 * The list contents are of type {@link no.stelvio.esb.models.service.metamodel.ServiceOperation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Operations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Operations</em>' containment reference list.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceInterface_ServiceOperations()
	 * @model containment="true"
	 * @generated
	 */
	EList<ServiceOperation> getServiceOperations();

	/**
	 * Returns the value of the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>UUID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>UUID</em>' attribute.
	 * @see #setUUID(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceInterface_UUID()
	 * @model id="true"
	 * @generated
	 */
	String getUUID();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getUUID <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>UUID</em>' attribute.
	 * @see #getUUID()
	 * @generated
	 */
	void setUUID(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceInterface_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Namespace</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Namespace</em>' attribute.
	 * @see #setNamespace(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceInterface_Namespace()
	 * @model
	 * @generated
	 */
	String getNamespace();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getNamespace <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Namespace</em>' attribute.
	 * @see #getNamespace()
	 * @generated
	 */
	void setNamespace(String value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceInterface_Version()
	 * @model
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

} // ServiceInterface
