/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel.transforms;

import com.ibm.icu.text.MessageFormat;

import com.ibm.xtools.transform.authoring.CreateRule;
import com.ibm.xtools.transform.authoring.CustomExtractor;
import com.ibm.xtools.transform.authoring.CustomRule;
import com.ibm.xtools.transform.authoring.DirectFeatureAdapter;
import com.ibm.xtools.transform.authoring.ExtractorExtension;
import com.ibm.xtools.transform.authoring.FeatureAdapter;
import com.ibm.xtools.transform.authoring.InstanceOfCondition;
import com.ibm.xtools.transform.authoring.MapTransform;
import com.ibm.xtools.transform.authoring.MoveRule;
import com.ibm.xtools.transform.authoring.Registry;
import com.ibm.xtools.transform.authoring.RuleExtension;
import com.ibm.xtools.transform.authoring.SubmapExtractor;

import com.ibm.xtools.transform.authoring.uml2.StereotypeCondition;
import com.ibm.xtools.transform.authoring.uml2.StereotypeFeatureAdapter;

import com.ibm.xtools.transform.core.AbstractContentExtractor;
import com.ibm.xtools.transform.core.AbstractRule;
import com.ibm.xtools.transform.core.TransformException;

import java.util.Collection;

import no.stelvio.esb.models.service.metamodel.Attachment;
import no.stelvio.esb.models.service.metamodel.Fault;
import no.stelvio.esb.models.service.metamodel.Message;
import no.stelvio.esb.models.service.metamodel.OperationMetadata;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;
import no.stelvio.esb.models.service.metamodel.ServiceOperation;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.OperationToOperationMetadataTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ParameterToFaultTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ParameterToMessageTransform;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.UrlToAttachmentTransform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'OperationToServiceOperationTransform' from the
 * mapping. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class OperationToServiceOperationTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATION_TRANSFORM = "OperationToServiceOperation_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATION_CREATE_RULE = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperation OwnedParameter To InputMessage Using
   * ParameterToMessage Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String OPERATIONTOSERVICEOPERATION_OWNED_PARAMETER_TO_INPUT_MESSAGE_USING_PARAMETERTOMESSAGE_EXTRACTOR = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_OwnedParameterToInputMessage_UsingParameterToMessage_Extractor";//$NON-NLS-1$

  /**
   * The 'OperationToServiceOperation Name to Name rule' id <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String OPERATIONTOSERVICEOPERATION_NAME_TO_NAME_RULE = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_NameToName_Rule";//$NON-NLS-1$

  /**
   * The 'OperationToServiceOperation to UUID rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATION__TO_UUID_RULE = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_ToUUID_Rule";//$NON-NLS-1$

	/**
	 * The 'OperationToServiceOperation to Namespace rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String OPERATIONTOSERVICEOPERATION__TO_NAMESPACE_RULE = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_ToNamespace_Rule";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperation to Version rule' id <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATION__TO_VERSION_RULE = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_ToVersion_Rule";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperation Tjenestemetadata$KortBeskrivelse to
   * Description rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATION_TJENESTEMETADATA$KORT_BESKRIVELSE_TO_DESCRIPTION_RULE = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_Tjenestemetadata$KortBeskrivelseToDescription_Rule";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperation Operation_Tjeneste To ServiceMetadata
   * Using OperationToOperationMetadata Extractor' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATION_OPERATION_TJENESTE_TO_SERVICE_METADATA_USING_OPERATIONTOOPERATIONMETADATA_EXTRACTOR = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_Operation_TjenesteToServiceMetadata_UsingOperationToOperationMetadata_Extractor";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperation OwnedParameter To OutputMessage Using
   * ParameterToMessage Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String OPERATIONTOSERVICEOPERATION_OWNED_PARAMETER_TO_OUTPUT_MESSAGE_USING_PARAMETERTOMESSAGE_EXTRACTOR = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_OwnedParameterToOutputMessage_UsingParameterToMessage_Extractor";//$NON-NLS-1$

  /**
   * The 'OperationToServiceOperation OwnedParameter To Faults Using
   * ParameterToFault Extractor' id <!-- begin-user-doc --> <!-- end-user-doc
   * -->
   * 
   * @generated
   */
  public static final String OPERATIONTOSERVICEOPERATION_OWNED_PARAMETER_TO_FAULTS_USING_PARAMETERTOFAULT_EXTRACTOR = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_OwnedParameterToFaults_UsingParameterToFault_Extractor";//$NON-NLS-1$

  /**
   * The 'OperationToServiceOperation OwnedComment To Attachments Using
   * UrlToAttachment Extractor' id <!-- begin-user-doc --> <!-- end-user-doc
   * -->
   * 
   * @generated
   */
  public static final String OPERATIONTOSERVICEOPERATION_OWNED_COMMENT_TO_ATTACHMENTS_USING_URLTOATTACHMENT_EXTRACTOR = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_OwnedCommentToAttachments_UsingUrlToAttachment_Extractor";//$NON-NLS-1$

  /**
   * The 'OperationToServiceOperation OwnedComment$Body to BehaviourRules
   * rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String OPERATIONTOSERVICEOPERATION_OWNED_COMMENT$BODY_TO_BEHAVIOUR_RULES_RULE = OPERATIONTOSERVICEOPERATION_TRANSFORM
      + "_OwnedComment$BodyToBehaviourRules_Rule";//$NON-NLS-1$

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public OperationToServiceOperationTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(OPERATIONTOSERVICEOPERATION_TRANSFORM,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public OperationToServiceOperationTransform(String id, String name,
			Registry registry, FeatureAdapter referenceAdapter) {
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
    add(getOwnedParameterToInputMessage_UsingParameterToMessage_Extractor(registry));
    add(getNameToName_Rule());
    add(getToUUID_Rule());
    add(getToNamespace_Rule());
    add(getToVersion_Rule());
    add(getTjenestemetadata$KortBeskrivelseToDescription_Rule());
    add(getOperation_TjenesteToServiceMetadata_UsingOperationToOperationMetadata_Extractor(registry));
    add(getOwnedParameterToOutputMessage_UsingParameterToMessage_Extractor(registry));
    add(getOwnedParameterToFaults_UsingParameterToFault_Extractor(registry));
    add(getOwnedCommentToAttachments_UsingUrlToAttachment_Extractor(registry));
    add(getOwnedComment$BodyToBehaviourRules_Rule());
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new InstanceOfCondition(UMLPackage.Literals.OPERATION)
        .AND(new StereotypeCondition(
            new String[] { "NAV UML Profile::Tjeneste" }));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        OPERATIONTOSERVICEOPERATION_CREATE_RULE,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.SERVICE_OPERATION);
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getOwnedParameterToInputMessage_UsingParameterToMessage_Extractor(
      Registry registry) {
    CustomExtractor extractor = new CustomExtractor(
        OPERATIONTOSERVICEOPERATION_OWNED_PARAMETER_TO_INPUT_MESSAGE_USING_PARAMETERTOMESSAGE_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_OwnedParameterToInputMessage_UsingParameterToMessage_Extractor,
        registry.get(
            ParameterToMessageTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_OPERATION__INPUT_MESSAGE)),
        new ExtractorExtension() {
          public Collection execute(EObject source) {
            return extendOwnedParameterToInputMessage_UsingParameterToMessage_Extractor((Operation) source);
          }
        });
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected Collection extendOwnedParameterToInputMessage_UsingParameterToMessage_Extractor(
      Operation Operation_src) {
    try {
      java.util.Collection l = new java.util.ArrayList();

      org.eclipse.emf.common.util.EList<Parameter> parameters = Operation_src
          .getOwnedParameters();
      for (Parameter parameter : parameters) {
        boolean isInParameter = parameter.getDirection().getValue() == org.eclipse.uml2.uml.ParameterDirectionKind.IN;
        boolean isException = parameter.isException();
        if (isInParameter && !isException) {
          l.add(parameter);
          return l;
        }
      }
      return l;
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor,
              new String[] {
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform_OwnedParameterToInputMessage_UsingParameterToMessage_Extractor,
                  Operation_src == null ? null
                      : Operation_src.getQualifiedName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getNameToName_Rule() {
    MoveRule rule = new MoveRule(
        OPERATIONTOSERVICEOPERATION_NAME_TO_NAME_RULE,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_NameToName_Rule,
        new DirectFeatureAdapter(
            UMLPackage.Literals.NAMED_ELEMENT__NAME),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.SERVICE_OPERATION__NAME));
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getToUUID_Rule() {
    CustomRule rule = new CustomRule(
        OPERATIONTOSERVICEOPERATION__TO_UUID_RULE,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_ToUUID_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToUUID_Rule((Operation) source,
                (ServiceOperation) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToUUID_Rule(Operation Operation_src,
      ServiceOperation ServiceOperation_tgt) {
    try {
      String xmiURI = Operation_src.eResource().getURIFragment(
          Operation_src);
      ServiceOperation_tgt.setUUID(xmiURI);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform_ToUUID_Rule,
                  Operation_src == null ? null
                      : Operation_src.getQualifiedName(),
                  ServiceOperation_tgt == null ? null
                      : ServiceOperation_tgt.getName() });
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
        OPERATIONTOSERVICEOPERATION__TO_NAMESPACE_RULE,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_ToNamespace_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToNamespace_Rule((Operation) source,
                (ServiceOperation) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToNamespace_Rule(Operation Operation_src,
      ServiceOperation ServiceOperation_tgt) {
    try {
      org.eclipse.uml2.uml.Package interfacePackage = Operation_src
          .getNearestPackage();
      org.eclipse.uml2.uml.Stereotype classPackageStereotype = interfacePackage
          .getAppliedStereotype("XSDProfile::schema");
      if (classPackageStereotype != null) {
        String targetNamespace = (String) interfacePackage.getValue(
            classPackageStereotype, "targetNamespace");
        ServiceOperation_tgt.setNamespace(targetNamespace);
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform_ToNamespace_Rule,
                  Operation_src == null ? null
                      : Operation_src.getQualifiedName(),
                  ServiceOperation_tgt == null ? null
                      : ServiceOperation_tgt.getName() });
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
        OPERATIONTOSERVICEOPERATION__TO_VERSION_RULE,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_ToVersion_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToVersion_Rule((Operation) source,
                (ServiceOperation) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToVersion_Rule(Operation Operation_src,
      ServiceOperation ServiceOperation_tgt) {
    try {
      org.eclipse.uml2.uml.Package interfacePackage = Operation_src
          .getNearestPackage();
      org.eclipse.uml2.uml.Stereotype classPackageStereotype = interfacePackage
          .getAppliedStereotype("XSDProfile::schema");
      if (classPackageStereotype != null) {
        String version = (String) interfacePackage.getValue(
            classPackageStereotype, "version");
        ServiceOperation_tgt.setVersion(version);
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform_ToVersion_Rule,
                  Operation_src == null ? null
                      : Operation_src.getQualifiedName(),
                  ServiceOperation_tgt == null ? null
                      : ServiceOperation_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getTjenestemetadata$KortBeskrivelseToDescription_Rule() {
    MoveRule rule = new MoveRule(
        OPERATIONTOSERVICEOPERATION_TJENESTEMETADATA$KORT_BESKRIVELSE_TO_DESCRIPTION_RULE,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_Tjenestemetadata$KortBeskrivelseToDescription_Rule,
        new StereotypeFeatureAdapter(
            "NAV UML Profile::Tjeneste::tjenestemetadata",
            "kortBeskrivelse"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.SERVICE_OPERATION__DESCRIPTION));
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getOperation_TjenesteToServiceMetadata_UsingOperationToOperationMetadata_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        OPERATIONTOSERVICEOPERATION_OPERATION_TJENESTE_TO_SERVICE_METADATA_USING_OPERATIONTOOPERATIONMETADATA_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_Operation_TjenesteToServiceMetadata_UsingOperationToOperationMetadata_Extractor,
        registry.get(
            OperationToOperationMetadataTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_OPERATION__SERVICE_METADATA)),
        new DirectFeatureAdapter(null));
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getOwnedParameterToOutputMessage_UsingParameterToMessage_Extractor(
      Registry registry) {
    CustomExtractor extractor = new CustomExtractor(
        OPERATIONTOSERVICEOPERATION_OWNED_PARAMETER_TO_OUTPUT_MESSAGE_USING_PARAMETERTOMESSAGE_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_OwnedParameterToOutputMessage_UsingParameterToMessage_Extractor,
        registry.get(
            ParameterToMessageTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_OPERATION__OUTPUT_MESSAGE)),
        new ExtractorExtension() {
          public Collection execute(EObject source) {
            return extendOwnedParameterToOutputMessage_UsingParameterToMessage_Extractor((Operation) source);
          }
        });
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected Collection extendOwnedParameterToOutputMessage_UsingParameterToMessage_Extractor(
      Operation Operation_src) {
    try {
      java.util.Collection l = new java.util.ArrayList();

      org.eclipse.emf.common.util.EList<Parameter> parameters = Operation_src
          .getOwnedParameters();
      for (Parameter parameter : parameters) {
        boolean isOutParameter = parameter.getDirection().getValue() == org.eclipse.uml2.uml.ParameterDirectionKind.OUT;
        boolean isException = parameter.isException();
        if (isOutParameter && !isException) {
          l.add(parameter);
          return l;
        }
      }
      return l;
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor,
              new String[] {
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform_OwnedParameterToOutputMessage_UsingParameterToMessage_Extractor,
                  Operation_src == null ? null
                      : Operation_src.getQualifiedName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getOwnedParameterToFaults_UsingParameterToFault_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        OPERATIONTOSERVICEOPERATION_OWNED_PARAMETER_TO_FAULTS_USING_PARAMETERTOFAULT_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_OwnedParameterToFaults_UsingParameterToFault_Extractor,
        registry.get(
            ParameterToFaultTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_OPERATION__FAULTS)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.BEHAVIORAL_FEATURE__OWNED_PARAMETER));
    extractor.setFilterCondition(new Condition() {
      public boolean isSatisfied(Object object) {
        return filterOwnedParameterToFaults_UsingParameterToFault_Extractor((Parameter) object);
      }
    });
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected boolean filterOwnedParameterToFaults_UsingParameterToFault_Extractor(
      Parameter ownedParameter_src) {
    try {
      boolean isOutParameter = ownedParameter_src.getDirection()
          .getValue() == org.eclipse.uml2.uml.ParameterDirectionKind.OUT;
      boolean isException = ownedParameter_src.isException();
      return isOutParameter && isException;
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor_source_filter,
              new String[] {
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperation_Transform_OwnedParameterToFaults_UsingParameterToFault_Extractor,
                  ownedParameter_src == null ? null
                      : ownedParameter_src
                          .getQualifiedName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getOwnedCommentToAttachments_UsingUrlToAttachment_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        OPERATIONTOSERVICEOPERATION_OWNED_COMMENT_TO_ATTACHMENTS_USING_URLTOATTACHMENT_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_OwnedCommentToAttachments_UsingUrlToAttachment_Extractor,
        registry.get(
            UrlToAttachmentTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_OPERATION__ATTACHMENTS)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.ELEMENT__OWNED_COMMENT));
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getOwnedComment$BodyToBehaviourRules_Rule() {
    MoveRule rule = new MoveRule(
        OPERATIONTOSERVICEOPERATION_OWNED_COMMENT$BODY_TO_BEHAVIOUR_RULES_RULE,
        Uml2servicemodelMessages.OperationToServiceOperation_Transform_OwnedComment$BodyToBehaviourRules_Rule,
        new DirectFeatureAdapter(
            UMLPackage.Literals.ELEMENT__OWNED_COMMENT, "[-1]/body"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.SERVICE_OPERATION__BEHAVIOUR_RULES));
    return rule;
  }

}
