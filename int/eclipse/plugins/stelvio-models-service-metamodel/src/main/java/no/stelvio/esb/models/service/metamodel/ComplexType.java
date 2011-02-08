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
 * A representation of the model object '<em><b>Complex Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#getName <em>Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#getUUID <em>UUID</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#getVersion <em>Version</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#getDescription <em>Description</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#getReferencedAttributes <em>Referenced Attributes</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#isIsEnumeration <em>Is Enumeration</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#isIsFault <em>Is Fault</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#getReferencedMessages <em>Referenced Messages</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#getDiagrams <em>Diagrams</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ComplexType#getAttachments <em>Attachments</em>}</li>
 * </ul>
 * </p>
 *
 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType()
 * @model
 * @generated
 */
public interface ComplexType extends EObject {
	/**
	 * Returns the value of the '<em><b>Attributes</b></em>' containment reference list.
	 * The list contents are of type {@link no.stelvio.esb.models.service.metamodel.Attribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attributes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attributes</em>' containment reference list.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_Attributes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Attribute> getAttributes();

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getName <em>Name</em>}' attribute.
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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_UUID()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getUUID();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getUUID <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>UUID</em>' attribute.
	 * @see #getUUID()
	 * @generated
	 */
	void setUUID(String value);

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_Version()
	 * @model
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_Namespace()
	 * @model
	 * @generated
	 */
	String getNamespace();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getNamespace <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Namespace</em>' attribute.
	 * @see #getNamespace()
	 * @generated
	 */
	void setNamespace(String value);

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Referenced Attributes</b></em>' reference list.
	 * The list contents are of type {@link no.stelvio.esb.models.service.metamodel.Attribute}.
	 * It is bidirectional and its opposite is '{@link no.stelvio.esb.models.service.metamodel.Attribute#getTypeRef <em>Type Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referenced Attributes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referenced Attributes</em>' reference list.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_ReferencedAttributes()
	 * @see no.stelvio.esb.models.service.metamodel.Attribute#getTypeRef
	 * @model opposite="typeRef" keys="UUID"
	 * @generated
	 */
	EList<Attribute> getReferencedAttributes();

	/**
	 * Returns the value of the '<em><b>Is Enumeration</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Enumeration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Enumeration</em>' attribute.
	 * @see #isSetIsEnumeration()
	 * @see #unsetIsEnumeration()
	 * @see #setIsEnumeration(boolean)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_IsEnumeration()
	 * @model default="false" unsettable="true"
	 * @generated
	 */
	boolean isIsEnumeration();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#isIsEnumeration <em>Is Enumeration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Enumeration</em>' attribute.
	 * @see #isSetIsEnumeration()
	 * @see #unsetIsEnumeration()
	 * @see #isIsEnumeration()
	 * @generated
	 */
	void setIsEnumeration(boolean value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#isIsEnumeration <em>Is Enumeration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIsEnumeration()
	 * @see #isIsEnumeration()
	 * @see #setIsEnumeration(boolean)
	 * @generated
	 */
	void unsetIsEnumeration();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#isIsEnumeration <em>Is Enumeration</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Is Enumeration</em>' attribute is set.
	 * @see #unsetIsEnumeration()
	 * @see #isIsEnumeration()
	 * @see #setIsEnumeration(boolean)
	 * @generated
	 */
	boolean isSetIsEnumeration();

	/**
	 * Returns the value of the '<em><b>Is Fault</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Fault</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Fault</em>' attribute.
	 * @see #isSetIsFault()
	 * @see #unsetIsFault()
	 * @see #setIsFault(boolean)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_IsFault()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isIsFault();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#isIsFault <em>Is Fault</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Fault</em>' attribute.
	 * @see #isSetIsFault()
	 * @see #unsetIsFault()
	 * @see #isIsFault()
	 * @generated
	 */
	void setIsFault(boolean value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#isIsFault <em>Is Fault</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIsFault()
	 * @see #isIsFault()
	 * @see #setIsFault(boolean)
	 * @generated
	 */
	void unsetIsFault();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.ComplexType#isIsFault <em>Is Fault</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Is Fault</em>' attribute is set.
	 * @see #unsetIsFault()
	 * @see #isIsFault()
	 * @see #setIsFault(boolean)
	 * @generated
	 */
	boolean isSetIsFault();

	/**
	 * Returns the value of the '<em><b>Referenced Messages</b></em>' reference list.
	 * The list contents are of type {@link no.stelvio.esb.models.service.metamodel.Message}.
	 * It is bidirectional and its opposite is '{@link no.stelvio.esb.models.service.metamodel.Message#getTypeRef <em>Type Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referenced Messages</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referenced Messages</em>' reference list.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_ReferencedMessages()
	 * @see no.stelvio.esb.models.service.metamodel.Message#getTypeRef
	 * @model opposite="typeRef" keys="UUID"
	 * @generated
	 */
	EList<Message> getReferencedMessages();

	/**
	 * Returns the value of the '<em><b>Diagrams</b></em>' reference list.
	 * The list contents are of type {@link no.stelvio.esb.models.service.metamodel.Diagram}.
	 * It is bidirectional and its opposite is '{@link no.stelvio.esb.models.service.metamodel.Diagram#getComplexTypes <em>Complex Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagrams</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagrams</em>' reference list.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_Diagrams()
	 * @see no.stelvio.esb.models.service.metamodel.Diagram#getComplexTypes
	 * @model opposite="complexTypes" keys="UUID"
	 * @generated
	 */
	EList<Diagram> getDiagrams();

	/**
	 * Returns the value of the '<em><b>Attachments</b></em>' containment reference list.
	 * The list contents are of type {@link no.stelvio.esb.models.service.metamodel.Attachment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attachments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attachments</em>' containment reference list.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getComplexType_Attachments()
	 * @model containment="true"
	 * @generated
	 */
	EList<Attachment> getAttachments();

} // ComplexType
