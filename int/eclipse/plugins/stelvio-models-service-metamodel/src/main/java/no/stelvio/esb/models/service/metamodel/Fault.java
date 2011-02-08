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
 * A representation of the model object '<em><b>Fault</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Fault#getId <em>Id</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Fault#getFaultType <em>Fault Type</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Fault#getProducerFaultRef <em>Producer Fault Ref</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Fault#getTypeRef <em>Type Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getFault()
 * @model
 * @generated
 */
public interface Fault extends EObject {
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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getFault_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Fault#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Fault Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fault Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fault Type</em>' attribute.
	 * @see #isSetFaultType()
	 * @see #unsetFaultType()
	 * @see #setFaultType(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getFault_FaultType()
	 * @model unsettable="true"
	 * @generated
	 */
	String getFaultType();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Fault#getFaultType <em>Fault Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fault Type</em>' attribute.
	 * @see #isSetFaultType()
	 * @see #unsetFaultType()
	 * @see #getFaultType()
	 * @generated
	 */
	void setFaultType(String value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.Fault#getFaultType <em>Fault Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFaultType()
	 * @see #getFaultType()
	 * @see #setFaultType(String)
	 * @generated
	 */
	void unsetFaultType();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.Fault#getFaultType <em>Fault Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Fault Type</em>' attribute is set.
	 * @see #unsetFaultType()
	 * @see #getFaultType()
	 * @see #setFaultType(String)
	 * @generated
	 */
	boolean isSetFaultType();

	/**
	 * Returns the value of the '<em><b>Producer Fault Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Producer Fault Ref</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Producer Fault Ref</em>' attribute.
	 * @see #setProducerFaultRef(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getFault_ProducerFaultRef()
	 * @model
	 * @generated
	 */
	String getProducerFaultRef();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Fault#getProducerFaultRef <em>Producer Fault Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Producer Fault Ref</em>' attribute.
	 * @see #getProducerFaultRef()
	 * @generated
	 */
	void setProducerFaultRef(String value);

	/**
	 * Returns the value of the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Ref</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Ref</em>' reference.
	 * @see #setTypeRef(ComplexType)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getFault_TypeRef()
	 * @model keys="UUID"
	 * @generated
	 */
	ComplexType getTypeRef();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Fault#getTypeRef <em>Type Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Ref</em>' reference.
	 * @see #getTypeRef()
	 * @generated
	 */
	void setTypeRef(ComplexType value);

} // Fault
