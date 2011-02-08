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
 * A representation of the model object '<em><b>Message</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Message#getTypeRef <em>Type Ref</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Message#getName <em>Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Message#getUUID <em>UUID</em>}</li>
 * </ul>
 * </p>
 *
 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getMessage()
 * @model
 * @generated
 */
public interface Message extends EObject {
	/**
	 * Returns the value of the '<em><b>Type Ref</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getReferencedMessages <em>Referenced Messages</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Ref</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Ref</em>' reference.
	 * @see #setTypeRef(ComplexType)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getMessage_TypeRef()
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getReferencedMessages
	 * @model opposite="referencedMessages" keys="UUID"
	 * @generated
	 */
	ComplexType getTypeRef();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Message#getTypeRef <em>Type Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Ref</em>' reference.
	 * @see #getTypeRef()
	 * @generated
	 */
	void setTypeRef(ComplexType value);

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getMessage_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Message#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getMessage_UUID()
	 * @model id="true"
	 * @generated
	 */
	String getUUID();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Message#getUUID <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>UUID</em>' attribute.
	 * @see #getUUID()
	 * @generated
	 */
	void setUUID(String value);

} // Message
