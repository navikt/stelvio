/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package no.stelvio.esb.models.service.metamodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see no.stelvio.esb.models.service.metamodel.ServiceMetamodelFactory
 * @model kind="package"
 * @generated
 */
public interface ServiceMetamodelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "metamodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://stelvio.no/int/models/service/metamodel";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "tjn";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ServiceMetamodelPackage eINSTANCE = no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl.init();

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.ServiceInterfaceImpl <em>Service Interface</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceInterfaceImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getServiceInterface()
	 * @generated
	 */
	int SERVICE_INTERFACE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_INTERFACE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Service Operations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_INTERFACE__SERVICE_OPERATIONS = 1;

	/**
	 * The feature id for the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_INTERFACE__UUID = 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_INTERFACE__DESCRIPTION = 3;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_INTERFACE__NAMESPACE = 4;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_INTERFACE__VERSION = 5;

	/**
	 * The number of structural features of the '<em>Service Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_INTERFACE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl <em>Service Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getServiceOperation()
	 * @generated
	 */
	int SERVICE_OPERATION = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__NAME = 0;

	/**
	 * The feature id for the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__UUID = 1;

	/**
	 * The feature id for the '<em><b>Service Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__SERVICE_METADATA = 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__DESCRIPTION = 3;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__VERSION = 4;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__NAMESPACE = 5;

	/**
	 * The feature id for the '<em><b>Input Message</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__INPUT_MESSAGE = 6;

	/**
	 * The feature id for the '<em><b>Output Message</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__OUTPUT_MESSAGE = 7;

	/**
	 * The feature id for the '<em><b>Faults</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__FAULTS = 8;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__ATTACHMENTS = 9;

	/**
	 * The feature id for the '<em><b>Behaviour Rules</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION__BEHAVIOUR_RULES = 10;

	/**
	 * The number of structural features of the '<em>Service Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION_FEATURE_COUNT = 11;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.FaultImpl <em>Fault</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.FaultImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getFault()
	 * @generated
	 */
	int FAULT = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT__ID = 0;

	/**
	 * The feature id for the '<em><b>Fault Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT__FAULT_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Producer Fault Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT__PRODUCER_FAULT_REF = 2;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT__TYPE_REF = 3;

	/**
	 * The number of structural features of the '<em>Fault</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl <em>Service Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getServicePackage()
	 * @generated
	 */
	int SERVICE_PACKAGE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_PACKAGE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Service Interface</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_PACKAGE__SERVICE_INTERFACE = 1;

	/**
	 * The feature id for the '<em><b>Complex Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_PACKAGE__COMPLEX_TYPES = 2;

	/**
	 * The feature id for the '<em><b>Child Packages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_PACKAGE__CHILD_PACKAGES = 3;

	/**
	 * The feature id for the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_PACKAGE__UUID = 4;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_PACKAGE__DESCRIPTION = 5;

	/**
	 * The feature id for the '<em><b>Diagrams</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_PACKAGE__DIAGRAMS = 6;

	/**
	 * The number of structural features of the '<em>Service Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_PACKAGE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.AttributeImpl <em>Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.AttributeImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getAttribute()
	 * @generated
	 */
	int ATTRIBUTE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__TYPE_REF = 1;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__TYPE_NAME = 2;

	/**
	 * The feature id for the '<em><b>Is List</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__IS_LIST = 3;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__DESCRIPTION = 4;

	/**
	 * The feature id for the '<em><b>Is Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__IS_REQUIRED = 5;

	/**
	 * The feature id for the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__UUID = 6;

	/**
	 * The number of structural features of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl <em>Complex Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getComplexType()
	 * @generated
	 */
	int COMPLEX_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__ATTRIBUTES = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__NAME = 1;

	/**
	 * The feature id for the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__UUID = 2;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__VERSION = 3;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__NAMESPACE = 4;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__DESCRIPTION = 5;

	/**
	 * The feature id for the '<em><b>Referenced Attributes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__REFERENCED_ATTRIBUTES = 6;

	/**
	 * The feature id for the '<em><b>Is Enumeration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__IS_ENUMERATION = 7;

	/**
	 * The feature id for the '<em><b>Is Fault</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__IS_FAULT = 8;

	/**
	 * The feature id for the '<em><b>Referenced Messages</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__REFERENCED_MESSAGES = 9;

	/**
	 * The feature id for the '<em><b>Diagrams</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__DIAGRAMS = 10;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE__ATTACHMENTS = 11;

	/**
	 * The number of structural features of the '<em>Complex Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_TYPE_FEATURE_COUNT = 12;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl <em>Operation Metadata</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getOperationMetadata()
	 * @generated
	 */
	int OPERATION_METADATA = 6;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__ID = 0;

	/**
	 * The feature id for the '<em><b>Error Handling</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__ERROR_HANDLING = 1;

	/**
	 * The feature id for the '<em><b>Transactions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__TRANSACTIONS = 2;

	/**
	 * The feature id for the '<em><b>Producer Service Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__PRODUCER_SERVICE_REF = 3;

	/**
	 * The feature id for the '<em><b>State Management</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__STATE_MANAGEMENT = 4;

	/**
	 * The feature id for the '<em><b>Producer Component</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__PRODUCER_COMPONENT = 5;

	/**
	 * The feature id for the '<em><b>Is Used By Batch</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__IS_USED_BY_BATCH = 6;

	/**
	 * The feature id for the '<em><b>Service Category</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__SERVICE_CATEGORY = 7;

	/**
	 * The feature id for the '<em><b>Scope</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__SCOPE = 8;

	/**
	 * The feature id for the '<em><b>Response Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__RESPONSE_TIME = 9;

	/**
	 * The feature id for the '<em><b>Volume Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__VOLUME_CAPACITY = 10;

	/**
	 * The feature id for the '<em><b>Uptime</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__UPTIME = 11;

	/**
	 * The feature id for the '<em><b>Proceessing Rules</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__PROCEESSING_RULES = 12;

	/**
	 * The feature id for the '<em><b>Security</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA__SECURITY = 13;

	/**
	 * The number of structural features of the '<em>Operation Metadata</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_METADATA_FEATURE_COUNT = 14;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.ServiceCatagoryImpl <em>Service Catagory</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceCatagoryImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getServiceCatagory()
	 * @generated
	 */
	int SERVICE_CATAGORY = 7;

	/**
	 * The feature id for the '<em><b>Grouping Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_CATAGORY__GROUPING_NAME = 0;

	/**
	 * The feature id for the '<em><b>Function</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_CATAGORY__FUNCTION = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_CATAGORY__NAME = 2;

	/**
	 * The number of structural features of the '<em>Service Catagory</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_CATAGORY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.MessageImpl <em>Message</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.MessageImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getMessage()
	 * @generated
	 */
	int MESSAGE = 8;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE__TYPE_REF = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE__NAME = 1;

	/**
	 * The feature id for the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE__UUID = 2;

	/**
	 * The number of structural features of the '<em>Message</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.DiagramImpl <em>Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.DiagramImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getDiagram()
	 * @generated
	 */
	int DIAGRAM = 9;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__TYPE_NAME = 0;

	/**
	 * The feature id for the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__UUID = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__NAME = 2;

	/**
	 * The feature id for the '<em><b>Complex Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__COMPLEX_TYPES = 3;

	/**
	 * The number of structural features of the '<em>Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.impl.AttachmentImpl <em>Attachment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.impl.AttachmentImpl
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getAttachment()
	 * @generated
	 */
	int ATTACHMENT = 10;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTACHMENT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTACHMENT__TYPE = 1;

	/**
	 * The feature id for the '<em><b>File Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTACHMENT__FILE_PATH = 2;

	/**
	 * The feature id for the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTACHMENT__UUID = 3;

	/**
	 * The number of structural features of the '<em>Attachment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTACHMENT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link no.stelvio.esb.models.service.metamodel.Scope <em>Scope</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see no.stelvio.esb.models.service.metamodel.Scope
	 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getScope()
	 * @generated
	 */
	int SCOPE = 11;


	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface <em>Service Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Service Interface</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceInterface
	 * @generated
	 */
	EClass getServiceInterface();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceInterface#getName()
	 * @see #getServiceInterface()
	 * @generated
	 */
	EAttribute getServiceInterface_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getServiceOperations <em>Service Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Service Operations</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceInterface#getServiceOperations()
	 * @see #getServiceInterface()
	 * @generated
	 */
	EReference getServiceInterface_ServiceOperations();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getUUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>UUID</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceInterface#getUUID()
	 * @see #getServiceInterface()
	 * @generated
	 */
	EAttribute getServiceInterface_UUID();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceInterface#getDescription()
	 * @see #getServiceInterface()
	 * @generated
	 */
	EAttribute getServiceInterface_Description();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getNamespace <em>Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Namespace</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceInterface#getNamespace()
	 * @see #getServiceInterface()
	 * @generated
	 */
	EAttribute getServiceInterface_Namespace();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceInterface#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceInterface#getVersion()
	 * @see #getServiceInterface()
	 * @generated
	 */
	EAttribute getServiceInterface_Version();

	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation <em>Service Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Service Operation</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation
	 * @generated
	 */
	EClass getServiceOperation();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getName()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EAttribute getServiceOperation_Name();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getUUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>UUID</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getUUID()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EAttribute getServiceOperation_UUID();

	/**
	 * Returns the meta object for the containment reference '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getServiceMetadata <em>Service Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Service Metadata</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getServiceMetadata()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EReference getServiceOperation_ServiceMetadata();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getDescription()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EAttribute getServiceOperation_Description();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getVersion()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EAttribute getServiceOperation_Version();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getNamespace <em>Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Namespace</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getNamespace()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EAttribute getServiceOperation_Namespace();

	/**
	 * Returns the meta object for the containment reference '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getInputMessage <em>Input Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Input Message</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getInputMessage()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EReference getServiceOperation_InputMessage();

	/**
	 * Returns the meta object for the containment reference '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getOutputMessage <em>Output Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Output Message</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getOutputMessage()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EReference getServiceOperation_OutputMessage();

	/**
	 * Returns the meta object for the containment reference list '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getFaults <em>Faults</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Faults</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getFaults()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EReference getServiceOperation_Faults();

	/**
	 * Returns the meta object for the containment reference list '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getAttachments <em>Attachments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attachments</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getAttachments()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EReference getServiceOperation_Attachments();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceOperation#getBehaviourRules <em>Behaviour Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Behaviour Rules</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceOperation#getBehaviourRules()
	 * @see #getServiceOperation()
	 * @generated
	 */
	EAttribute getServiceOperation_BehaviourRules();

	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.Fault <em>Fault</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fault</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Fault
	 * @generated
	 */
	EClass getFault();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Fault#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Fault#getId()
	 * @see #getFault()
	 * @generated
	 */
	EAttribute getFault_Id();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Fault#getFaultType <em>Fault Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fault Type</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Fault#getFaultType()
	 * @see #getFault()
	 * @generated
	 */
	EAttribute getFault_FaultType();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Fault#getProducerFaultRef <em>Producer Fault Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Producer Fault Ref</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Fault#getProducerFaultRef()
	 * @see #getFault()
	 * @generated
	 */
	EAttribute getFault_ProducerFaultRef();

	/**
	 * Returns the meta object for the reference '{@link no.stelvio.esb.models.service.metamodel.Fault#getTypeRef <em>Type Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type Ref</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Fault#getTypeRef()
	 * @see #getFault()
	 * @generated
	 */
	EReference getFault_TypeRef();

	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.ServicePackage <em>Service Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Service Package</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServicePackage
	 * @generated
	 */
	EClass getServicePackage();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServicePackage#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServicePackage#getName()
	 * @see #getServicePackage()
	 * @generated
	 */
	EAttribute getServicePackage_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link no.stelvio.esb.models.service.metamodel.ServicePackage#getServiceInterface <em>Service Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Service Interface</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServicePackage#getServiceInterface()
	 * @see #getServicePackage()
	 * @generated
	 */
	EReference getServicePackage_ServiceInterface();

	/**
	 * Returns the meta object for the containment reference list '{@link no.stelvio.esb.models.service.metamodel.ServicePackage#getComplexTypes <em>Complex Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Complex Types</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServicePackage#getComplexTypes()
	 * @see #getServicePackage()
	 * @generated
	 */
	EReference getServicePackage_ComplexTypes();

	/**
	 * Returns the meta object for the containment reference list '{@link no.stelvio.esb.models.service.metamodel.ServicePackage#getChildPackages <em>Child Packages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Child Packages</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServicePackage#getChildPackages()
	 * @see #getServicePackage()
	 * @generated
	 */
	EReference getServicePackage_ChildPackages();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServicePackage#getUUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>UUID</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServicePackage#getUUID()
	 * @see #getServicePackage()
	 * @generated
	 */
	EAttribute getServicePackage_UUID();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServicePackage#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServicePackage#getDescription()
	 * @see #getServicePackage()
	 * @generated
	 */
	EAttribute getServicePackage_Description();

	/**
	 * Returns the meta object for the containment reference list '{@link no.stelvio.esb.models.service.metamodel.ServicePackage#getDiagrams <em>Diagrams</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Diagrams</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServicePackage#getDiagrams()
	 * @see #getServicePackage()
	 * @generated
	 */
	EReference getServicePackage_Diagrams();

	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attribute
	 * @generated
	 */
	EClass getAttribute();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Attribute#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attribute#getName()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Name();

	/**
	 * Returns the meta object for the reference '{@link no.stelvio.esb.models.service.metamodel.Attribute#getTypeRef <em>Type Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type Ref</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attribute#getTypeRef()
	 * @see #getAttribute()
	 * @generated
	 */
	EReference getAttribute_TypeRef();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Attribute#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attribute#getTypeName()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_TypeName();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Attribute#isIsList <em>Is List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is List</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attribute#isIsList()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_IsList();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Attribute#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attribute#getDescription()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Description();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Attribute#isIsRequired <em>Is Required</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Required</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attribute#isIsRequired()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_IsRequired();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Attribute#getUUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>UUID</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attribute#getUUID()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_UUID();

	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.ComplexType <em>Complex Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Complex Type</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType
	 * @generated
	 */
	EClass getComplexType();

	/**
	 * Returns the meta object for the containment reference list '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getAttributes()
	 * @see #getComplexType()
	 * @generated
	 */
	EReference getComplexType_Attributes();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getName()
	 * @see #getComplexType()
	 * @generated
	 */
	EAttribute getComplexType_Name();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getUUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>UUID</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getUUID()
	 * @see #getComplexType()
	 * @generated
	 */
	EAttribute getComplexType_UUID();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getVersion()
	 * @see #getComplexType()
	 * @generated
	 */
	EAttribute getComplexType_Version();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getNamespace <em>Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Namespace</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getNamespace()
	 * @see #getComplexType()
	 * @generated
	 */
	EAttribute getComplexType_Namespace();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getDescription()
	 * @see #getComplexType()
	 * @generated
	 */
	EAttribute getComplexType_Description();

	/**
	 * Returns the meta object for the reference list '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getReferencedAttributes <em>Referenced Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Referenced Attributes</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getReferencedAttributes()
	 * @see #getComplexType()
	 * @generated
	 */
	EReference getComplexType_ReferencedAttributes();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ComplexType#isIsEnumeration <em>Is Enumeration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Enumeration</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#isIsEnumeration()
	 * @see #getComplexType()
	 * @generated
	 */
	EAttribute getComplexType_IsEnumeration();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ComplexType#isIsFault <em>Is Fault</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Fault</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#isIsFault()
	 * @see #getComplexType()
	 * @generated
	 */
	EAttribute getComplexType_IsFault();

	/**
	 * Returns the meta object for the reference list '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getReferencedMessages <em>Referenced Messages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Referenced Messages</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getReferencedMessages()
	 * @see #getComplexType()
	 * @generated
	 */
	EReference getComplexType_ReferencedMessages();

	/**
	 * Returns the meta object for the reference list '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getDiagrams <em>Diagrams</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Diagrams</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getDiagrams()
	 * @see #getComplexType()
	 * @generated
	 */
	EReference getComplexType_Diagrams();

	/**
	 * Returns the meta object for the containment reference list '{@link no.stelvio.esb.models.service.metamodel.ComplexType#getAttachments <em>Attachments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attachments</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ComplexType#getAttachments()
	 * @see #getComplexType()
	 * @generated
	 */
	EReference getComplexType_Attachments();

	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata <em>Operation Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Metadata</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata
	 * @generated
	 */
	EClass getOperationMetadata();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getId()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_Id();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getErrorHandling <em>Error Handling</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Error Handling</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getErrorHandling()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_ErrorHandling();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getTransactions <em>Transactions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transactions</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getTransactions()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_Transactions();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getProducerServiceRef <em>Producer Service Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Producer Service Ref</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getProducerServiceRef()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_ProducerServiceRef();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getStateManagement <em>State Management</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>State Management</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getStateManagement()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_StateManagement();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getProducerComponent <em>Producer Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Producer Component</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getProducerComponent()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_ProducerComponent();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#isIsUsedByBatch <em>Is Used By Batch</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Used By Batch</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#isIsUsedByBatch()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_IsUsedByBatch();

	/**
	 * Returns the meta object for the containment reference '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getServiceCategory <em>Service Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Service Category</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getServiceCategory()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EReference getOperationMetadata_ServiceCategory();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getScope <em>Scope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scope</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getScope()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_Scope();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getResponseTime <em>Response Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Response Time</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getResponseTime()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_ResponseTime();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getVolumeCapacity <em>Volume Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Capacity</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getVolumeCapacity()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_VolumeCapacity();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getUptime <em>Uptime</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uptime</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getUptime()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_Uptime();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getProceessingRules <em>Proceessing Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Proceessing Rules</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getProceessingRules()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_ProceessingRules();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.OperationMetadata#getSecurity <em>Security</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Security</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.OperationMetadata#getSecurity()
	 * @see #getOperationMetadata()
	 * @generated
	 */
	EAttribute getOperationMetadata_Security();

	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.ServiceCatagory <em>Service Catagory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Service Catagory</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceCatagory
	 * @generated
	 */
	EClass getServiceCatagory();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceCatagory#getGroupingName <em>Grouping Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grouping Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceCatagory#getGroupingName()
	 * @see #getServiceCatagory()
	 * @generated
	 */
	EAttribute getServiceCatagory_GroupingName();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceCatagory#getFunction <em>Function</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Function</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceCatagory#getFunction()
	 * @see #getServiceCatagory()
	 * @generated
	 */
	EAttribute getServiceCatagory_Function();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.ServiceCatagory#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.ServiceCatagory#getName()
	 * @see #getServiceCatagory()
	 * @generated
	 */
	EAttribute getServiceCatagory_Name();

	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.Message <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Message</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Message
	 * @generated
	 */
	EClass getMessage();

	/**
	 * Returns the meta object for the reference '{@link no.stelvio.esb.models.service.metamodel.Message#getTypeRef <em>Type Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type Ref</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Message#getTypeRef()
	 * @see #getMessage()
	 * @generated
	 */
	EReference getMessage_TypeRef();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Message#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Message#getName()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_Name();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Message#getUUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>UUID</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Message#getUUID()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_UUID();

	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.Diagram <em>Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Diagram
	 * @generated
	 */
	EClass getDiagram();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Diagram#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Diagram#getTypeName()
	 * @see #getDiagram()
	 * @generated
	 */
	EAttribute getDiagram_TypeName();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Diagram#getUUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>UUID</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Diagram#getUUID()
	 * @see #getDiagram()
	 * @generated
	 */
	EAttribute getDiagram_UUID();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Diagram#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Diagram#getName()
	 * @see #getDiagram()
	 * @generated
	 */
	EAttribute getDiagram_Name();

	/**
	 * Returns the meta object for the reference list '{@link no.stelvio.esb.models.service.metamodel.Diagram#getComplexTypes <em>Complex Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Complex Types</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Diagram#getComplexTypes()
	 * @see #getDiagram()
	 * @generated
	 */
	EReference getDiagram_ComplexTypes();

	/**
	 * Returns the meta object for class '{@link no.stelvio.esb.models.service.metamodel.Attachment <em>Attachment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attachment</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attachment
	 * @generated
	 */
	EClass getAttachment();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Attachment#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attachment#getName()
	 * @see #getAttachment()
	 * @generated
	 */
	EAttribute getAttachment_Name();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Attachment#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attachment#getType()
	 * @see #getAttachment()
	 * @generated
	 */
	EAttribute getAttachment_Type();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Attachment#getFilePath <em>File Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>File Path</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attachment#getFilePath()
	 * @see #getAttachment()
	 * @generated
	 */
	EAttribute getAttachment_FilePath();

	/**
	 * Returns the meta object for the attribute '{@link no.stelvio.esb.models.service.metamodel.Attachment#getUUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>UUID</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Attachment#getUUID()
	 * @see #getAttachment()
	 * @generated
	 */
	EAttribute getAttachment_UUID();

	/**
	 * Returns the meta object for enum '{@link no.stelvio.esb.models.service.metamodel.Scope <em>Scope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Scope</em>'.
	 * @see no.stelvio.esb.models.service.metamodel.Scope
	 * @generated
	 */
	EEnum getScope();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ServiceMetamodelFactory getServiceMetamodelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.ServiceInterfaceImpl <em>Service Interface</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceInterfaceImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getServiceInterface()
		 * @generated
		 */
		EClass SERVICE_INTERFACE = eINSTANCE.getServiceInterface();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_INTERFACE__NAME = eINSTANCE.getServiceInterface_Name();

		/**
		 * The meta object literal for the '<em><b>Service Operations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE_INTERFACE__SERVICE_OPERATIONS = eINSTANCE.getServiceInterface_ServiceOperations();

		/**
		 * The meta object literal for the '<em><b>UUID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_INTERFACE__UUID = eINSTANCE.getServiceInterface_UUID();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_INTERFACE__DESCRIPTION = eINSTANCE.getServiceInterface_Description();

		/**
		 * The meta object literal for the '<em><b>Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_INTERFACE__NAMESPACE = eINSTANCE.getServiceInterface_Namespace();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_INTERFACE__VERSION = eINSTANCE.getServiceInterface_Version();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl <em>Service Operation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceOperationImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getServiceOperation()
		 * @generated
		 */
		EClass SERVICE_OPERATION = eINSTANCE.getServiceOperation();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_OPERATION__NAME = eINSTANCE.getServiceOperation_Name();

		/**
		 * The meta object literal for the '<em><b>UUID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_OPERATION__UUID = eINSTANCE.getServiceOperation_UUID();

		/**
		 * The meta object literal for the '<em><b>Service Metadata</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE_OPERATION__SERVICE_METADATA = eINSTANCE.getServiceOperation_ServiceMetadata();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_OPERATION__DESCRIPTION = eINSTANCE.getServiceOperation_Description();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_OPERATION__VERSION = eINSTANCE.getServiceOperation_Version();

		/**
		 * The meta object literal for the '<em><b>Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_OPERATION__NAMESPACE = eINSTANCE.getServiceOperation_Namespace();

		/**
		 * The meta object literal for the '<em><b>Input Message</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE_OPERATION__INPUT_MESSAGE = eINSTANCE.getServiceOperation_InputMessage();

		/**
		 * The meta object literal for the '<em><b>Output Message</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE_OPERATION__OUTPUT_MESSAGE = eINSTANCE.getServiceOperation_OutputMessage();

		/**
		 * The meta object literal for the '<em><b>Faults</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE_OPERATION__FAULTS = eINSTANCE.getServiceOperation_Faults();

		/**
		 * The meta object literal for the '<em><b>Attachments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE_OPERATION__ATTACHMENTS = eINSTANCE.getServiceOperation_Attachments();

		/**
		 * The meta object literal for the '<em><b>Behaviour Rules</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_OPERATION__BEHAVIOUR_RULES = eINSTANCE.getServiceOperation_BehaviourRules();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.FaultImpl <em>Fault</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.FaultImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getFault()
		 * @generated
		 */
		EClass FAULT = eINSTANCE.getFault();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FAULT__ID = eINSTANCE.getFault_Id();

		/**
		 * The meta object literal for the '<em><b>Fault Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FAULT__FAULT_TYPE = eINSTANCE.getFault_FaultType();

		/**
		 * The meta object literal for the '<em><b>Producer Fault Ref</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FAULT__PRODUCER_FAULT_REF = eINSTANCE.getFault_ProducerFaultRef();

		/**
		 * The meta object literal for the '<em><b>Type Ref</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FAULT__TYPE_REF = eINSTANCE.getFault_TypeRef();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl <em>Service Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServicePackageImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getServicePackage()
		 * @generated
		 */
		EClass SERVICE_PACKAGE = eINSTANCE.getServicePackage();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_PACKAGE__NAME = eINSTANCE.getServicePackage_Name();

		/**
		 * The meta object literal for the '<em><b>Service Interface</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE_PACKAGE__SERVICE_INTERFACE = eINSTANCE.getServicePackage_ServiceInterface();

		/**
		 * The meta object literal for the '<em><b>Complex Types</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE_PACKAGE__COMPLEX_TYPES = eINSTANCE.getServicePackage_ComplexTypes();

		/**
		 * The meta object literal for the '<em><b>Child Packages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE_PACKAGE__CHILD_PACKAGES = eINSTANCE.getServicePackage_ChildPackages();

		/**
		 * The meta object literal for the '<em><b>UUID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_PACKAGE__UUID = eINSTANCE.getServicePackage_UUID();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_PACKAGE__DESCRIPTION = eINSTANCE.getServicePackage_Description();

		/**
		 * The meta object literal for the '<em><b>Diagrams</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE_PACKAGE__DIAGRAMS = eINSTANCE.getServicePackage_Diagrams();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.AttributeImpl <em>Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.AttributeImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getAttribute()
		 * @generated
		 */
		EClass ATTRIBUTE = eINSTANCE.getAttribute();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__NAME = eINSTANCE.getAttribute_Name();

		/**
		 * The meta object literal for the '<em><b>Type Ref</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ATTRIBUTE__TYPE_REF = eINSTANCE.getAttribute_TypeRef();

		/**
		 * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__TYPE_NAME = eINSTANCE.getAttribute_TypeName();

		/**
		 * The meta object literal for the '<em><b>Is List</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__IS_LIST = eINSTANCE.getAttribute_IsList();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__DESCRIPTION = eINSTANCE.getAttribute_Description();

		/**
		 * The meta object literal for the '<em><b>Is Required</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__IS_REQUIRED = eINSTANCE.getAttribute_IsRequired();

		/**
		 * The meta object literal for the '<em><b>UUID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__UUID = eINSTANCE.getAttribute_UUID();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl <em>Complex Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.ComplexTypeImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getComplexType()
		 * @generated
		 */
		EClass COMPLEX_TYPE = eINSTANCE.getComplexType();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPLEX_TYPE__ATTRIBUTES = eINSTANCE.getComplexType_Attributes();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLEX_TYPE__NAME = eINSTANCE.getComplexType_Name();

		/**
		 * The meta object literal for the '<em><b>UUID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLEX_TYPE__UUID = eINSTANCE.getComplexType_UUID();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLEX_TYPE__VERSION = eINSTANCE.getComplexType_Version();

		/**
		 * The meta object literal for the '<em><b>Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLEX_TYPE__NAMESPACE = eINSTANCE.getComplexType_Namespace();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLEX_TYPE__DESCRIPTION = eINSTANCE.getComplexType_Description();

		/**
		 * The meta object literal for the '<em><b>Referenced Attributes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPLEX_TYPE__REFERENCED_ATTRIBUTES = eINSTANCE.getComplexType_ReferencedAttributes();

		/**
		 * The meta object literal for the '<em><b>Is Enumeration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLEX_TYPE__IS_ENUMERATION = eINSTANCE.getComplexType_IsEnumeration();

		/**
		 * The meta object literal for the '<em><b>Is Fault</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLEX_TYPE__IS_FAULT = eINSTANCE.getComplexType_IsFault();

		/**
		 * The meta object literal for the '<em><b>Referenced Messages</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPLEX_TYPE__REFERENCED_MESSAGES = eINSTANCE.getComplexType_ReferencedMessages();

		/**
		 * The meta object literal for the '<em><b>Diagrams</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPLEX_TYPE__DIAGRAMS = eINSTANCE.getComplexType_Diagrams();

		/**
		 * The meta object literal for the '<em><b>Attachments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPLEX_TYPE__ATTACHMENTS = eINSTANCE.getComplexType_Attachments();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl <em>Operation Metadata</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.OperationMetadataImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getOperationMetadata()
		 * @generated
		 */
		EClass OPERATION_METADATA = eINSTANCE.getOperationMetadata();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__ID = eINSTANCE.getOperationMetadata_Id();

		/**
		 * The meta object literal for the '<em><b>Error Handling</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__ERROR_HANDLING = eINSTANCE.getOperationMetadata_ErrorHandling();

		/**
		 * The meta object literal for the '<em><b>Transactions</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__TRANSACTIONS = eINSTANCE.getOperationMetadata_Transactions();

		/**
		 * The meta object literal for the '<em><b>Producer Service Ref</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__PRODUCER_SERVICE_REF = eINSTANCE.getOperationMetadata_ProducerServiceRef();

		/**
		 * The meta object literal for the '<em><b>State Management</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__STATE_MANAGEMENT = eINSTANCE.getOperationMetadata_StateManagement();

		/**
		 * The meta object literal for the '<em><b>Producer Component</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__PRODUCER_COMPONENT = eINSTANCE.getOperationMetadata_ProducerComponent();

		/**
		 * The meta object literal for the '<em><b>Is Used By Batch</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__IS_USED_BY_BATCH = eINSTANCE.getOperationMetadata_IsUsedByBatch();

		/**
		 * The meta object literal for the '<em><b>Service Category</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_METADATA__SERVICE_CATEGORY = eINSTANCE.getOperationMetadata_ServiceCategory();

		/**
		 * The meta object literal for the '<em><b>Scope</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__SCOPE = eINSTANCE.getOperationMetadata_Scope();

		/**
		 * The meta object literal for the '<em><b>Response Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__RESPONSE_TIME = eINSTANCE.getOperationMetadata_ResponseTime();

		/**
		 * The meta object literal for the '<em><b>Volume Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__VOLUME_CAPACITY = eINSTANCE.getOperationMetadata_VolumeCapacity();

		/**
		 * The meta object literal for the '<em><b>Uptime</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__UPTIME = eINSTANCE.getOperationMetadata_Uptime();

		/**
		 * The meta object literal for the '<em><b>Proceessing Rules</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__PROCEESSING_RULES = eINSTANCE.getOperationMetadata_ProceessingRules();

		/**
		 * The meta object literal for the '<em><b>Security</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_METADATA__SECURITY = eINSTANCE.getOperationMetadata_Security();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.ServiceCatagoryImpl <em>Service Catagory</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceCatagoryImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getServiceCatagory()
		 * @generated
		 */
		EClass SERVICE_CATAGORY = eINSTANCE.getServiceCatagory();

		/**
		 * The meta object literal for the '<em><b>Grouping Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_CATAGORY__GROUPING_NAME = eINSTANCE.getServiceCatagory_GroupingName();

		/**
		 * The meta object literal for the '<em><b>Function</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_CATAGORY__FUNCTION = eINSTANCE.getServiceCatagory_Function();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE_CATAGORY__NAME = eINSTANCE.getServiceCatagory_Name();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.MessageImpl <em>Message</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.MessageImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getMessage()
		 * @generated
		 */
		EClass MESSAGE = eINSTANCE.getMessage();

		/**
		 * The meta object literal for the '<em><b>Type Ref</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSAGE__TYPE_REF = eINSTANCE.getMessage_TypeRef();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE__NAME = eINSTANCE.getMessage_Name();

		/**
		 * The meta object literal for the '<em><b>UUID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE__UUID = eINSTANCE.getMessage_UUID();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.DiagramImpl <em>Diagram</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.DiagramImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getDiagram()
		 * @generated
		 */
		EClass DIAGRAM = eINSTANCE.getDiagram();

		/**
		 * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM__TYPE_NAME = eINSTANCE.getDiagram_TypeName();

		/**
		 * The meta object literal for the '<em><b>UUID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM__UUID = eINSTANCE.getDiagram_UUID();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM__NAME = eINSTANCE.getDiagram_Name();

		/**
		 * The meta object literal for the '<em><b>Complex Types</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM__COMPLEX_TYPES = eINSTANCE.getDiagram_ComplexTypes();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.impl.AttachmentImpl <em>Attachment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.impl.AttachmentImpl
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getAttachment()
		 * @generated
		 */
		EClass ATTACHMENT = eINSTANCE.getAttachment();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTACHMENT__NAME = eINSTANCE.getAttachment_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTACHMENT__TYPE = eINSTANCE.getAttachment_Type();

		/**
		 * The meta object literal for the '<em><b>File Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTACHMENT__FILE_PATH = eINSTANCE.getAttachment_FilePath();

		/**
		 * The meta object literal for the '<em><b>UUID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTACHMENT__UUID = eINSTANCE.getAttachment_UUID();

		/**
		 * The meta object literal for the '{@link no.stelvio.esb.models.service.metamodel.Scope <em>Scope</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see no.stelvio.esb.models.service.metamodel.Scope
		 * @see no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelPackageImpl#getScope()
		 * @generated
		 */
		EEnum SCOPE = eINSTANCE.getScope();

	}

} //ServiceMetamodelPackage
