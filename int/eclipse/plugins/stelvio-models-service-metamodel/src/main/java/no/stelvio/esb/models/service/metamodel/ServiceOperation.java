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
 * A representation of the model object '<em><b>Service Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getName <em>Name</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getUUID <em>UUID</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getServiceMetadata <em>Service Metadata</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getDescription <em>Description</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getVersion <em>Version</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getInputMessage <em>Input Message</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getOutputMessage <em>Output Message</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getFaults <em>Faults</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getAttachments <em>Attachments</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getBehaviourRules <em>Behaviour Rules</em>}</li>
 *   <li>{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getChangelog <em>Changelog</em>}</li>
 * </ul>
 * </p>
 *
 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation()
 * @model
 * @generated
 */
public interface ServiceOperation extends EObject {
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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getName <em>Name</em>}' attribute.
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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_UUID()
	 * @model id="true"
	 * @generated
	 */
	String getUUID();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getUUID <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>UUID</em>' attribute.
	 * @see #getUUID()
	 * @generated
	 */
	void setUUID(String value);

	/**
	 * Returns the value of the '<em><b>Service Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Metadata</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Metadata</em>' containment reference.
	 * @see #setServiceMetadata(OperationMetadata)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_ServiceMetadata()
	 * @model containment="true"
	 * @generated
	 */
	OperationMetadata getServiceMetadata();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getServiceMetadata <em>Service Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Metadata</em>' containment reference.
	 * @see #getServiceMetadata()
	 * @generated
	 */
	void setServiceMetadata(OperationMetadata value);

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_Version()
	 * @model
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getVersion <em>Version</em>}' attribute.
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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_Namespace()
	 * @model
	 * @generated
	 */
	String getNamespace();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getNamespace <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Namespace</em>' attribute.
	 * @see #getNamespace()
	 * @generated
	 */
	void setNamespace(String value);

	/**
	 * Returns the value of the '<em><b>Input Message</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Message</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Message</em>' containment reference.
	 * @see #setInputMessage(Message)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_InputMessage()
	 * @model containment="true"
	 * @generated
	 */
	Message getInputMessage();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getInputMessage <em>Input Message</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input Message</em>' containment reference.
	 * @see #getInputMessage()
	 * @generated
	 */
	void setInputMessage(Message value);

	/**
	 * Returns the value of the '<em><b>Output Message</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Message</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Message</em>' containment reference.
	 * @see #setOutputMessage(Message)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_OutputMessage()
	 * @model containment="true"
	 * @generated
	 */
	Message getOutputMessage();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getOutputMessage <em>Output Message</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output Message</em>' containment reference.
	 * @see #getOutputMessage()
	 * @generated
	 */
	void setOutputMessage(Message value);

	/**
	 * Returns the value of the '<em><b>Faults</b></em>' containment reference list.
	 * The list contents are of type {@link no.stelvio.esb.models.service.metamodel.Fault}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Faults</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Faults</em>' containment reference list.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_Faults()
	 * @model containment="true"
	 * @generated
	 */
	EList<Fault> getFaults();

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
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_Attachments()
	 * @model containment="true"
	 * @generated
	 */
	EList<Attachment> getAttachments();

	/**
	 * Returns the value of the '<em><b>Behaviour Rules</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Behaviour Rules</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Behaviour Rules</em>' attribute.
	 * @see #setBehaviourRules(String)
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_BehaviourRules()
	 * @model
	 * @generated
	 */
	String getBehaviourRules();

	/**
	 * Sets the value of the '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getBehaviourRules <em>Behaviour Rules</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Behaviour Rules</em>' attribute.
	 * @see #getBehaviourRules()
	 * @generated
	 */
	void setBehaviourRules(String value);

	/**
	 * Returns the value of the '<em><b>Changelog</b></em>' containment reference list.
	 * The list contents are of type {@link no.stelvio.esb.models.service.metamodel.Changelog}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Changelog</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Changelog</em>' containment reference list.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage#getServiceOperation_Changelog()
	 * @model containment="true"
	 * @generated
	 */
	EList<Changelog> getChangelog();

} // ServiceOperation
