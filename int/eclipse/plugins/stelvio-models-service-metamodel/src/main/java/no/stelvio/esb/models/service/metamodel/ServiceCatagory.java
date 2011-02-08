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
 * A representation of the model object '<em><b>Service Catagory</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceCatagory#getGroupingName <em>Grouping Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceCatagory#getFunction <em>Function</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceCatagory#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceCatagory()
 * @model
 * @generated
 */
public interface ServiceCatagory extends EObject {
	/**
	 * Returns the value of the '<em><b>Grouping Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Grouping Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Grouping Name</em>' attribute.
	 * @see #setGroupingName(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceCatagory_GroupingName()
	 * @model
	 * @generated
	 */
	String getGroupingName();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceCatagory#getGroupingName <em>Grouping Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Grouping Name</em>' attribute.
	 * @see #getGroupingName()
	 * @generated
	 */
	void setGroupingName(String value);

	/**
	 * Returns the value of the '<em><b>Function</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function</em>' attribute.
	 * @see #setFunction(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceCatagory_Function()
	 * @model
	 * @generated
	 */
	String getFunction();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceCatagory#getFunction <em>Function</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Function</em>' attribute.
	 * @see #getFunction()
	 * @generated
	 */
	void setFunction(String value);

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceCatagory_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceCatagory#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ServiceCatagory
