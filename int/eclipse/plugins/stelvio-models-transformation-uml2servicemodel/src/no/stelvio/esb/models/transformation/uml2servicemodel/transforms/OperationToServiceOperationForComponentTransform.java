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
import no.stelvio.esb.models.service.metamodel.Changelog;
import no.stelvio.esb.models.service.metamodel.Fault;
import no.stelvio.esb.models.service.metamodel.Message;
import no.stelvio.esb.models.service.metamodel.OperationMetadata;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;
import no.stelvio.esb.models.service.metamodel.ServiceOperation;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.EndringsloggToChangelogTransform;
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
 * An implementation of the 'OperationToServiceOperationForComponentTransform'
 * from the mapping. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class OperationToServiceOperationForComponentTransform extends
		MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM = "OperationToServiceOperationForComponent_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_CREATE_RULE = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperationForComponent OwnedParameter To
   * InputMessage Using ParameterToMessage Extractor' id <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OWNED_PARAMETER_TO_INPUT_MESSAGE_USING_PARAMETERTOMESSAGE_EXTRACTOR = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_OwnedParameterToInputMessage_UsingParameterToMessage_Extractor";//$NON-NLS-1$

	/**
	 * The 'OperationToServiceOperationForComponent Name to Name rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_NAME_TO_NAME_RULE = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_NameToName_Rule";//$NON-NLS-1$

	/**
	 * The 'OperationToServiceOperationForComponent to UUID rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT__TO_UUID_RULE = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_ToUUID_Rule";//$NON-NLS-1$

	/**
	 * The 'OperationToServiceOperationForComponent to Namespace rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT__TO_NAMESPACE_RULE = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_ToNamespace_Rule";//$NON-NLS-1$

	/**
	 * The 'OperationToServiceOperationForComponent to Version rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT__TO_VERSION_RULE = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_ToVersion_Rule";//$NON-NLS-1$

	/**
	 * The 'OperationToServiceOperationForComponent
	 * Tjenestemetadata$KortBeskrivelse to Description rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TJENESTEMETADATA$KORT_BESKRIVELSE_TO_DESCRIPTION_RULE = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_Tjenestemetadata$KortBeskrivelseToDescription_Rule";//$NON-NLS-1$

	/**
	 * The 'OperationToServiceOperationForComponent Operation_Tjeneste To
	 * ServiceMetadata Using OperationToOperationMetadata Extractor' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OPERATION_TJENESTE_TO_SERVICE_METADATA_USING_OPERATIONTOOPERATIONMETADATA_EXTRACTOR = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_Operation_TjenesteToServiceMetadata_UsingOperationToOperationMetadata_Extractor";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperationForComponent OwnedParameter To
   * OutputMessage Using ParameterToMessage Extractor' id <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OWNED_PARAMETER_TO_OUTPUT_MESSAGE_USING_PARAMETERTOMESSAGE_EXTRACTOR = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_OwnedParameterToOutputMessage_UsingParameterToMessage_Extractor";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperationForComponent OwnedParameter To Faults
   * Using ParameterToFault Extractor' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OWNED_PARAMETER_TO_FAULTS_USING_PARAMETERTOFAULT_EXTRACTOR = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_OwnedParameterToFaults_UsingParameterToFault_Extractor";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperationForComponent OwnedComment To Attachments
   * Using UrlToAttachment Extractor' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OWNED_COMMENT_TO_ATTACHMENTS_USING_URLTOATTACHMENT_EXTRACTOR = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_OwnedCommentToAttachments_UsingUrlToAttachment_Extractor";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperationForComponent OwnedComment$Body to
   * BehaviourRules rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OWNED_COMMENT$BODY_TO_BEHAVIOUR_RULES_RULE = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_OwnedComment$BodyToBehaviourRules_Rule";//$NON-NLS-1$

	/**
   * The 'OperationToServiceOperationForComponent Endringslogg To Changelog
   * Using EndringsloggToChangelog Extractor' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOSERVICEOPERATIONFORCOMPONENT_ENDRINGSLOGG_TO_CHANGELOG_USING_ENDRINGSLOGGTOCHANGELOG_EXTRACTOR = OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM
      + "_EndringsloggToChangelog_UsingEndringsloggToChangelog_Extractor";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public OperationToServiceOperationForComponentTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TRANSFORM,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public OperationToServiceOperationForComponentTransform(String id,
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
    add(getEndringsloggToChangelog_UsingEndringsloggToChangelog_Extractor(registry));
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_CREATE_RULE,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_Create_Rule,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OWNED_PARAMETER_TO_INPUT_MESSAGE_USING_PARAMETERTOMESSAGE_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_OwnedParameterToInputMessage_UsingParameterToMessage_Extractor,
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
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_OwnedParameterToInputMessage_UsingParameterToMessage_Extractor,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_NAME_TO_NAME_RULE,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_NameToName_Rule,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT__TO_UUID_RULE,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_ToUUID_Rule,
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
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_ToUUID_Rule,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT__TO_NAMESPACE_RULE,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_ToNamespace_Rule,
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
      org.eclipse.emf.common.util.EList<org.eclipse.uml2.uml.Element> liste1 = interfacePackage
          .getOwnedElements();

      for (org.eclipse.uml2.uml.Element e : liste1) {
        org.eclipse.emf.common.util.EList<org.eclipse.uml2.uml.Stereotype> liste2 = e
            .getAppliedStereotypes();

        for (org.eclipse.uml2.uml.Stereotype s : liste2) {
          if ("Definition".equals(s.getName())) {
            // Vi er på en <Component> stereotypet "Definition"
            String targetNamespace = (String) e.getValue(s,
                "targetNamespace");
            ServiceOperation_tgt.setNamespace(targetNamespace);
            break;
          }
        }
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_ToNamespace_Rule,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT__TO_VERSION_RULE,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_ToVersion_Rule,
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
      String version = null;

      org.eclipse.uml2.uml.Package interfacePackage = Operation_src
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
        ServiceOperation_tgt.setVersion(version);
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_ToVersion_Rule,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_TJENESTEMETADATA$KORT_BESKRIVELSE_TO_DESCRIPTION_RULE,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_Tjenestemetadata$KortBeskrivelseToDescription_Rule,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OPERATION_TJENESTE_TO_SERVICE_METADATA_USING_OPERATIONTOOPERATIONMETADATA_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_Operation_TjenesteToServiceMetadata_UsingOperationToOperationMetadata_Extractor,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OWNED_PARAMETER_TO_OUTPUT_MESSAGE_USING_PARAMETERTOMESSAGE_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_OwnedParameterToOutputMessage_UsingParameterToMessage_Extractor,
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
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_OwnedParameterToOutputMessage_UsingParameterToMessage_Extractor,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OWNED_PARAMETER_TO_FAULTS_USING_PARAMETERTOFAULT_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_OwnedParameterToFaults_UsingParameterToFault_Extractor,
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
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform,
                  Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_OwnedParameterToFaults_UsingParameterToFault_Extractor,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OWNED_COMMENT_TO_ATTACHMENTS_USING_URLTOATTACHMENT_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_OwnedCommentToAttachments_UsingUrlToAttachment_Extractor,
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
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_OWNED_COMMENT$BODY_TO_BEHAVIOUR_RULES_RULE,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_OwnedComment$BodyToBehaviourRules_Rule,
        new DirectFeatureAdapter(
            UMLPackage.Literals.ELEMENT__OWNED_COMMENT, "[-1]/body"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.SERVICE_OPERATION__BEHAVIOUR_RULES));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractContentExtractor getEndringsloggToChangelog_UsingEndringsloggToChangelog_Extractor(
			Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        OPERATIONTOSERVICEOPERATIONFORCOMPONENT_ENDRINGSLOGG_TO_CHANGELOG_USING_ENDRINGSLOGGTOCHANGELOG_EXTRACTOR,
        Uml2servicemodelMessages.OperationToServiceOperationForComponent_Transform_EndringsloggToChangelog_UsingEndringsloggToChangelog_Extractor,
        registry.get(
            EndringsloggToChangelogTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_OPERATION__CHANGELOG)),
        new StereotypeFeatureAdapter(
            "NAV UML Profile::Tjeneste::endringslogg"));
    return extractor;
  }

}
