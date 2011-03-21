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
 * A representation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Attribute#getName <em>Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Attribute#getTypeRef <em>Type Ref</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Attribute#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Attribute#isIsList <em>Is List</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Attribute#getDescription <em>Description</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Attribute#isIsRequired <em>Is Required</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Attribute#getUUID <em>UUID</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.Attribute#getMappingToAttributeName <em>Mapping To Attribute Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getAttribute()
 * @model
 * @generated
 */
public interface Attribute extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * The default value is <code>"\"<missing attribute name>\""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #isSetName()
	 * @see #unsetName()
	 * @see #setName(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getAttribute_Name()
	 * @model default="\"<missing attribute name>\"" unsettable="true" required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #isSetName()
	 * @see #unsetName()
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetName()
	 * @see #getName()
	 * @see #setName(String)
	 * @generated
	 */
	void unsetName();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getName <em>Name</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Name</em>' attribute is set.
	 * @see #unsetName()
	 * @see #getName()
	 * @see #setName(String)
	 * @generated
	 */
	boolean isSetName();

	/**
	 * Returns the value of the '<em><b>Type Ref</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getReferencedAttributes <em>Referenced Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Ref</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Ref</em>' reference.
	 * @see #setTypeRef(ComplexType)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getAttribute_TypeRef()
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getReferencedAttributes
	 * @model opposite="referencedAttributes" keys="UUID"
	 * @generated
	 */
	ComplexType getTypeRef();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getTypeRef <em>Type Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Ref</em>' reference.
	 * @see #getTypeRef()
	 * @generated
	 */
	void setTypeRef(ComplexType value);

	/**
	 * Returns the value of the '<em><b>Type Name</b></em>' attribute.
	 * The default value is <code>"\"<missing data type name>\""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Name</em>' attribute.
	 * @see #isSetTypeName()
	 * @see #unsetTypeName()
	 * @see #setTypeName(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getAttribute_TypeName()
	 * @model default="\"<missing data type name>\"" unsettable="true" required="true"
	 * @generated
	 */
	String getTypeName();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getTypeName <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Name</em>' attribute.
	 * @see #isSetTypeName()
	 * @see #unsetTypeName()
	 * @see #getTypeName()
	 * @generated
	 */
	void setTypeName(String value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getTypeName <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTypeName()
	 * @see #getTypeName()
	 * @see #setTypeName(String)
	 * @generated
	 */
	void unsetTypeName();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getTypeName <em>Type Name</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Type Name</em>' attribute is set.
	 * @see #unsetTypeName()
	 * @see #getTypeName()
	 * @see #setTypeName(String)
	 * @generated
	 */
	boolean isSetTypeName();

	/**
	 * Returns the value of the '<em><b>Is List</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is List</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is List</em>' attribute.
	 * @see #isSetIsList()
	 * @see #unsetIsList()
	 * @see #setIsList(boolean)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getAttribute_IsList()
	 * @model default="false" unsettable="true" required="true"
	 * @generated
	 */
	boolean isIsList();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#isIsList <em>Is List</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is List</em>' attribute.
	 * @see #isSetIsList()
	 * @see #unsetIsList()
	 * @see #isIsList()
	 * @generated
	 */
	void setIsList(boolean value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#isIsList <em>Is List</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIsList()
	 * @see #isIsList()
	 * @see #setIsList(boolean)
	 * @generated
	 */
	void unsetIsList();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#isIsList <em>Is List</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Is List</em>' attribute is set.
	 * @see #unsetIsList()
	 * @see #isIsList()
	 * @see #setIsList(boolean)
	 * @generated
	 */
	boolean isSetIsList();

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * The default value is <code>"\"no description\""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #isSetDescription()
	 * @see #unsetDescription()
	 * @see #setDescription(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getAttribute_Description()
	 * @model default="\"no description\"" unsettable="true" required="true"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #isSetDescription()
	 * @see #unsetDescription()
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDescription()
	 * @see #getDescription()
	 * @see #setDescription(String)
	 * @generated
	 */
	void unsetDescription();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getDescription <em>Description</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Description</em>' attribute is set.
	 * @see #unsetDescription()
	 * @see #getDescription()
	 * @see #setDescription(String)
	 * @generated
	 */
	boolean isSetDescription();

	/**
	 * Returns the value of the '<em><b>Is Required</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Required</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Required</em>' attribute.
	 * @see #isSetIsRequired()
	 * @see #unsetIsRequired()
	 * @see #setIsRequired(boolean)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getAttribute_IsRequired()
	 * @model default="false" unsettable="true" required="true"
	 * @generated
	 */
	boolean isIsRequired();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#isIsRequired <em>Is Required</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Required</em>' attribute.
	 * @see #isSetIsRequired()
	 * @see #unsetIsRequired()
	 * @see #isIsRequired()
	 * @generated
	 */
	void setIsRequired(boolean value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#isIsRequired <em>Is Required</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIsRequired()
	 * @see #isIsRequired()
	 * @see #setIsRequired(boolean)
	 * @generated
	 */
	void unsetIsRequired();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#isIsRequired <em>Is Required</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Is Required</em>' attribute is set.
	 * @see #unsetIsRequired()
	 * @see #isIsRequired()
	 * @see #setIsRequired(boolean)
	 * @generated
	 */
	boolean isSetIsRequired();

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getAttribute_UUID()
	 * @model id="true"
	 * @generated
	 */
	String getUUID();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getUUID <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>UUID</em>' attribute.
	 * @see #getUUID()
	 * @generated
	 */
	void setUUID(String value);

	/**
	 * Returns the value of the '<em><b>Mapping To Attribute Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mapping To Attribute Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mapping To Attribute Name</em>' attribute.
	 * @see #isSetMappingToAttributeName()
	 * @see #unsetMappingToAttributeName()
	 * @see #setMappingToAttributeName(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getAttribute_MappingToAttributeName()
	 * @model unsettable="true"
	 * @generated
	 */
	String getMappingToAttributeName();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getMappingToAttributeName <em>Mapping To Attribute Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mapping To Attribute Name</em>' attribute.
	 * @see #isSetMappingToAttributeName()
	 * @see #unsetMappingToAttributeName()
	 * @see #getMappingToAttributeName()
	 * @generated
	 */
	void setMappingToAttributeName(String value);

	/**
	 * Unsets the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getMappingToAttributeName <em>Mapping To Attribute Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMappingToAttributeName()
	 * @see #getMappingToAttributeName()
	 * @see #setMappingToAttributeName(String)
	 * @generated
	 */
	void unsetMappingToAttributeName();

	/**
	 * Returns whether the value of the '{@link no.stelvio.esb.models.service.metamodel.Attribute#getMappingToAttributeName <em>Mapping To Attribute Name</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Mapping To Attribute Name</em>' attribute is set.
	 * @see #unsetMappingToAttributeName()
	 * @see #getMappingToAttributeName()
	 * @see #setMappingToAttributeName(String)
	 * @generated
	 */
	boolean isSetMappingToAttributeName();

} // Attribute
