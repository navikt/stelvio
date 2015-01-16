/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel.transforms;

import com.ibm.icu.text.MessageFormat;

import com.ibm.xtools.transform.authoring.CreateRule;
import com.ibm.xtools.transform.authoring.CustomRule;
import com.ibm.xtools.transform.authoring.DirectFeatureAdapter;
import com.ibm.xtools.transform.authoring.FeatureAdapter;
import com.ibm.xtools.transform.authoring.InstanceOfCondition;
import com.ibm.xtools.transform.authoring.MapTransform;
import com.ibm.xtools.transform.authoring.MoveRule;
import com.ibm.xtools.transform.authoring.Registry;
import com.ibm.xtools.transform.authoring.RuleExtension;
import com.ibm.xtools.transform.authoring.SubmapExtractor;

import com.ibm.xtools.transform.core.AbstractContentExtractor;
import com.ibm.xtools.transform.core.AbstractRule;
import com.ibm.xtools.transform.core.TransformException;

import no.stelvio.esb.models.service.metamodel.ServiceInterface;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;
import no.stelvio.esb.models.service.metamodel.ServiceOperation;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.OperationToServiceOperationForComponentTransform;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'InterfaceToServiceInterfaceForComponentTransform'
 * from the mapping. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class InterfaceToServiceInterfaceForComponentTransform extends
		MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String INTERFACETOSERVICEINTERFACEFORCOMPONENT_TRANSFORM = "InterfaceToServiceInterfaceForComponent_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String INTERFACETOSERVICEINTERFACEFORCOMPONENT_CREATE_RULE = INTERFACETOSERVICEINTERFACEFORCOMPONENT_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
	 * The 'InterfaceToServiceInterfaceForComponent Name to Name rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String INTERFACETOSERVICEINTERFACEFORCOMPONENT_NAME_TO_NAME_RULE = INTERFACETOSERVICEINTERFACEFORCOMPONENT_TRANSFORM
      + "_NameToName_Rule";//$NON-NLS-1$

	/**
   * The 'InterfaceToServiceInterfaceForComponent OwnedComment$Body to
   * Description rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String INTERFACETOSERVICEINTERFACEFORCOMPONENT_OWNED_COMMENT$BODY_TO_DESCRIPTION_RULE = INTERFACETOSERVICEINTERFACEFORCOMPONENT_TRANSFORM
      + "_OwnedComment$BodyToDescription_Rule";//$NON-NLS-1$

	/**
	 * The 'InterfaceToServiceInterfaceForComponent to UUID rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String INTERFACETOSERVICEINTERFACEFORCOMPONENT__TO_UUID_RULE = INTERFACETOSERVICEINTERFACEFORCOMPONENT_TRANSFORM
      + "_ToUUID_Rule";//$NON-NLS-1$

	/**
	 * The 'InterfaceToServiceInterfaceForComponent to Namespace rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String INTERFACETOSERVICEINTERFACEFORCOMPONENT__TO_NAMESPACE_RULE = INTERFACETOSERVICEINTERFACEFORCOMPONENT_TRANSFORM
      + "_ToNamespace_Rule";//$NON-NLS-1$

	/**
	 * The 'InterfaceToServiceInterfaceForComponent to Version rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String INTERFACETOSERVICEINTERFACEFORCOMPONENT__TO_VERSION_RULE = INTERFACETOSERVICEINTERFACEFORCOMPONENT_TRANSFORM
      + "_ToVersion_Rule";//$NON-NLS-1$

	/**
   * The 'InterfaceToServiceInterfaceForComponent OwnedOperation To
   * ServiceOperations Using OperationToServiceOperationForComponent
   * Extractor' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String INTERFACETOSERVICEINTERFACEFORCOMPONENT_OWNED_OPERATION_TO_SERVICE_OPERATIONS_USING_OPERATIONTOSERVICEOPERATIONFORCOMPONENT_EXTRACTOR = INTERFACETOSERVICEINTERFACEFORCOMPONENT_TRANSFORM
      + "_OwnedOperationToServiceOperations_UsingOperationToServiceOperationForComponent_Extractor";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public InterfaceToServiceInterfaceForComponentTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(
        INTERFACETOSERVICEINTERFACEFORCOMPONENT_TRANSFORM,
        Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public InterfaceToServiceInterfaceForComponentTransform(String id,
			String name, Registry registry, FeatureAdapter referenceAdapter) {
    super(id, name, registry, referenceAdapter);
    addTransformElements(registry);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	private void addTransformElements(Registry registry) {
    // You may add more transform element before the generated ones here
    // Remember to remove the @generated tag or add NOT to it
    addGeneratedTransformElements(registry);
    // You may add more transform element after the generated ones here
    // Remember to remove the @generated tag or add NOT to it
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	private void addGeneratedTransformElements(Registry registry) {
    add(getNameToName_Rule());
    add(getOwnedComment$BodyToDescription_Rule());
    add(getToUUID_Rule());
    add(getToNamespace_Rule());
    add(getToVersion_Rule());
    add(getOwnedOperationToServiceOperations_UsingOperationToServiceOperationForComponent_Extractor(registry));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new InstanceOfCondition(UMLPackage.Literals.INTERFACE);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        INTERFACETOSERVICEINTERFACEFORCOMPONENT_CREATE_RULE,
        Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.SERVICE_INTERFACE);
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getNameToName_Rule() {
    MoveRule rule = new MoveRule(
        INTERFACETOSERVICEINTERFACEFORCOMPONENT_NAME_TO_NAME_RULE,
        Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_NameToName_Rule,
        new DirectFeatureAdapter(
            UMLPackage.Literals.NAMED_ELEMENT__NAME),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.SERVICE_INTERFACE__NAME));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getOwnedComment$BodyToDescription_Rule() {
    CustomRule rule = new CustomRule(
        INTERFACETOSERVICEINTERFACEFORCOMPONENT_OWNED_COMMENT$BODY_TO_DESCRIPTION_RULE,
        Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_OwnedComment$BodyToDescription_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeOwnedComment$BodyToDescription_Rule(
                (Interface) source, (ServiceInterface) target);
          }
        });
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected void executeOwnedComment$BodyToDescription_Rule(
			Interface Interface_src, ServiceInterface ServiceInterface_tgt) {
    try {
      org.eclipse.emf.common.util.EList<org.eclipse.uml2.uml.Comment> comments = Interface_src
          .getOwnedComments();
      for (org.eclipse.uml2.uml.Comment comment : comments) {
        org.eclipse.uml2.uml.Stereotype docStereotype = comment
            .getAppliedStereotype("Default::Documentation");
        if (docStereotype != null) {
          ServiceInterface_tgt.setDescription(comment.getBody());
        }
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform,
                  Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_OwnedComment$BodyToDescription_Rule,
                  Interface_src == null ? null
                      : Interface_src.getQualifiedName(),
                  ServiceInterface_tgt == null ? null
                      : ServiceInterface_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getToUUID_Rule() {
    CustomRule rule = new CustomRule(
        INTERFACETOSERVICEINTERFACEFORCOMPONENT__TO_UUID_RULE,
        Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_ToUUID_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToUUID_Rule((Interface) source,
                (ServiceInterface) target);
          }
        });
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected void executeToUUID_Rule(Interface Interface_src,
			ServiceInterface ServiceInterface_tgt) {
    try {
      String xmiURI = Interface_src.eResource().getURIFragment(
          Interface_src);
      ServiceInterface_tgt.setUUID(xmiURI);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform,
                  Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_ToUUID_Rule,
                  Interface_src == null ? null
                      : Interface_src.getQualifiedName(),
                  ServiceInterface_tgt == null ? null
                      : ServiceInterface_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getToNamespace_Rule() {
    CustomRule rule = new CustomRule(
        INTERFACETOSERVICEINTERFACEFORCOMPONENT__TO_NAMESPACE_RULE,
        Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_ToNamespace_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToNamespace_Rule((Interface) source,
                (ServiceInterface) target);
          }
        });
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected void executeToNamespace_Rule(Interface Interface_src,
			ServiceInterface ServiceInterface_tgt) {
    try {
      org.eclipse.uml2.uml.Element eier = Interface_src.getOwner();
      if (eier != null) {
        org.eclipse.emf.common.util.EList<org.eclipse.uml2.uml.Stereotype> liste = eier
            .getAppliedStereotypes();
        for (org.eclipse.uml2.uml.Stereotype s : liste) {
          if ("Definition".equals(s.getName())) {
            String targetNamespace = (String) eier.getValue(s,
                "targetNamespace");
            ServiceInterface_tgt.setNamespace(targetNamespace);
            break;
          }
        }
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform,
                  Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_ToNamespace_Rule,
                  Interface_src == null ? null
                      : Interface_src.getQualifiedName(),
                  ServiceInterface_tgt == null ? null
                      : ServiceInterface_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getToVersion_Rule() {
    CustomRule rule = new CustomRule(
        INTERFACETOSERVICEINTERFACEFORCOMPONENT__TO_VERSION_RULE,
        Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_ToVersion_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToVersion_Rule((Interface) source,
                (ServiceInterface) target);
          }
        });
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected void executeToVersion_Rule(Interface Interface_src,
			ServiceInterface ServiceInterface_tgt) {
    try {
      String version = null;
      org.eclipse.uml2.uml.Package interfacePackage = Interface_src
          .getNearestPackage();
      if (interfacePackage != null) {
        org.eclipse.uml2.uml.Stereotype stereotype = interfacePackage
            .getAppliedStereotype("XSDProfile::schema");
        if (stereotype != null) {
          version = (String) interfacePackage.getValue(stereotype,
              "version");
        } else {
          // NearestPackage er normalt sett en vX-mappe uten "schema",
          // så vi må enda en package opp (getNestingPackage)
          interfacePackage = interfacePackage.getNestingPackage();
          if (interfacePackage != null) {
            stereotype = interfacePackage
                .getAppliedStereotype("XSDProfile::schema");
            if (stereotype != null) {
              version = (String) interfacePackage.getValue(
                  stereotype, "version");
            }
          }
        }
      }

      if (version != null) {
        ServiceInterface_tgt.setVersion(version);
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform,
                  Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_ToVersion_Rule,
                  Interface_src == null ? null
                      : Interface_src.getQualifiedName(),
                  ServiceInterface_tgt == null ? null
                      : ServiceInterface_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractContentExtractor getOwnedOperationToServiceOperations_UsingOperationToServiceOperationForComponent_Extractor(
			Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        INTERFACETOSERVICEINTERFACEFORCOMPONENT_OWNED_OPERATION_TO_SERVICE_OPERATIONS_USING_OPERATIONTOSERVICEOPERATIONFORCOMPONENT_EXTRACTOR,
        Uml2servicemodelMessages.InterfaceToServiceInterfaceForComponent_Transform_OwnedOperationToServiceOperations_UsingOperationToServiceOperationForComponent_Extractor,
        registry.get(
            OperationToServiceOperationForComponentTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_INTERFACE__SERVICE_OPERATIONS)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.INTERFACE__OWNED_OPERATION));
    return extractor;
  }

}
