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

import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.Message;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ComplexTypeToComplexTypeTransform;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'ParameterToMessageTransform' from the mapping. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ParameterToMessageTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PARAMETERTOMESSAGE_TRANSFORM = "ParameterToMessage_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PARAMETERTOMESSAGE_CREATE_RULE = PARAMETERTOMESSAGE_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'ParameterToMessage Name to Name rule' id <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PARAMETERTOMESSAGE_NAME_TO_NAME_RULE = PARAMETERTOMESSAGE_TRANSFORM
      + "_NameToName_Rule";//$NON-NLS-1$

  /**
   * The 'ParameterToMessage Type To TypeRef Using ComplexTypeToComplexType
   * Extractor' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PARAMETERTOMESSAGE_TYPE_TO_TYPE_REF_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR = PARAMETERTOMESSAGE_TRANSFORM
      + "_TypeToTypeRef_UsingComplexTypeToComplexType_Extractor";//$NON-NLS-1$

  /**
   * The 'ParameterToMessage to UUID rule' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String PARAMETERTOMESSAGE__TO_UUID_RULE = PARAMETERTOMESSAGE_TRANSFORM
      + "_ToUUID_Rule";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public ParameterToMessageTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(PARAMETERTOMESSAGE_TRANSFORM,
        Uml2servicemodelMessages.ParameterToMessage_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public ParameterToMessageTransform(String id, String name,
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
    add(getNameToName_Rule());
    add(getTypeToTypeRef_UsingComplexTypeToComplexType_Extractor(registry));
    add(getToUUID_Rule());
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new InstanceOfCondition(UMLPackage.Literals.PARAMETER);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        PARAMETERTOMESSAGE_CREATE_RULE,
        Uml2servicemodelMessages.ParameterToMessage_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.MESSAGE);
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getNameToName_Rule() {
    MoveRule rule = new MoveRule(
        PARAMETERTOMESSAGE_NAME_TO_NAME_RULE,
        Uml2servicemodelMessages.ParameterToMessage_Transform_NameToName_Rule,
        new DirectFeatureAdapter(
            UMLPackage.Literals.NAMED_ELEMENT__NAME),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.MESSAGE__NAME));
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getTypeToTypeRef_UsingComplexTypeToComplexType_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PARAMETERTOMESSAGE_TYPE_TO_TYPE_REF_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR,
        Uml2servicemodelMessages.ParameterToMessage_Transform_TypeToTypeRef_UsingComplexTypeToComplexType_Extractor,
        registry.get(
            ComplexTypeToComplexTypeTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.MESSAGE__TYPE_REF)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.TYPED_ELEMENT__TYPE));
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getToUUID_Rule() {
    CustomRule rule = new CustomRule(
        PARAMETERTOMESSAGE__TO_UUID_RULE,
        Uml2servicemodelMessages.ParameterToMessage_Transform_ToUUID_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToUUID_Rule((Parameter) source, (Message) target);
          }
        });
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToUUID_Rule(Parameter Parameter_src,
      Message Message_tgt) {
    try {
      String xmiURI = Parameter_src.eResource().getURIFragment(
          Parameter_src);
      Message_tgt.setUUID(xmiURI);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.ParameterToMessage_Transform,
                  Uml2servicemodelMessages.ParameterToMessage_Transform_ToUUID_Rule,
                  Parameter_src == null ? null
                      : Parameter_src.getQualifiedName(),
                  Message_tgt == null ? null : Message_tgt
                      .getName() });
      throw new TransformException(message, e, null);
    }
  }

}
