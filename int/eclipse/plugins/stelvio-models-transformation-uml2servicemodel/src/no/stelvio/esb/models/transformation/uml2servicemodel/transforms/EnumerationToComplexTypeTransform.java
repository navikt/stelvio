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

import com.ibm.xtools.transform.authoring.uml2.StereotypeCondition;

import com.ibm.xtools.transform.core.AbstractContentExtractor;
import com.ibm.xtools.transform.core.AbstractRule;
import com.ibm.xtools.transform.core.TransformException;

import no.stelvio.esb.models.service.metamodel.Attribute;
import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.PropertyToAttributeTransform;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'EnumerationToComplexTypeTransform' from the
 * mapping. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class EnumerationToComplexTypeTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String ENUMERATIONTOCOMPLEXTYPE_TRANSFORM = "EnumerationToComplexType_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String ENUMERATIONTOCOMPLEXTYPE_CREATE_RULE = ENUMERATIONTOCOMPLEXTYPE_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'EnumerationToComplexType Attribute To Attributes Using
   * PropertyToAttribute Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String ENUMERATIONTOCOMPLEXTYPE_ATTRIBUTE_TO_ATTRIBUTES_USING_PROPERTYTOATTRIBUTE_EXTRACTOR = ENUMERATIONTOCOMPLEXTYPE_TRANSFORM
      + "_AttributeToAttributes_UsingPropertyToAttribute_Extractor";//$NON-NLS-1$

  /**
   * The 'EnumerationToComplexType Name to Name rule' id <!-- begin-user-doc
   * --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String ENUMERATIONTOCOMPLEXTYPE_NAME_TO_NAME_RULE = ENUMERATIONTOCOMPLEXTYPE_TRANSFORM
      + "_NameToName_Rule";//$NON-NLS-1$

  /**
   * The 'EnumerationToComplexType OwnedComment$Body to Description rule' id
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String ENUMERATIONTOCOMPLEXTYPE_OWNED_COMMENT$BODY_TO_DESCRIPTION_RULE = ENUMERATIONTOCOMPLEXTYPE_TRANSFORM
      + "_OwnedComment$BodyToDescription_Rule";//$NON-NLS-1$

  /**
	 * The 'EnumerationToComplexType to IsEnumeration rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String ENUMERATIONTOCOMPLEXTYPE__TO_IS_ENUMERATION_RULE = ENUMERATIONTOCOMPLEXTYPE_TRANSFORM
      + "_ToIsEnumeration_Rule";//$NON-NLS-1$

	/**
   * The 'EnumerationToComplexType to Namespace rule' id <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String ENUMERATIONTOCOMPLEXTYPE__TO_NAMESPACE_RULE = ENUMERATIONTOCOMPLEXTYPE_TRANSFORM
      + "_ToNamespace_Rule";//$NON-NLS-1$

	/**
   * The 'EnumerationToComplexType to Version rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String ENUMERATIONTOCOMPLEXTYPE__TO_VERSION_RULE = ENUMERATIONTOCOMPLEXTYPE_TRANSFORM
      + "_ToVersion_Rule";//$NON-NLS-1$

	/**
   * The 'EnumerationToComplexType to UUID rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String ENUMERATIONTOCOMPLEXTYPE__TO_UUID_RULE = ENUMERATIONTOCOMPLEXTYPE_TRANSFORM
      + "_ToUUID_Rule";//$NON-NLS-1$

	/**
   * The 'EnumerationToComplexType to IsFault rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String ENUMERATIONTOCOMPLEXTYPE__TO_IS_FAULT_RULE = ENUMERATIONTOCOMPLEXTYPE_TRANSFORM
      + "_ToIsFault_Rule";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public EnumerationToComplexTypeTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(ENUMERATIONTOCOMPLEXTYPE_TRANSFORM,
        Uml2servicemodelMessages.EnumerationToComplexType_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public EnumerationToComplexTypeTransform(String id, String name,
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
    add(getAttributeToAttributes_UsingPropertyToAttribute_Extractor(registry));
    add(getNameToName_Rule());
    add(getOwnedComment$BodyToDescription_Rule());
    add(getToIsEnumeration_Rule());
    add(getToNamespace_Rule());
    add(getToVersion_Rule());
    add(getToUUID_Rule());
    add(getToIsFault_Rule());
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new InstanceOfCondition(UMLPackage.Literals.CLASS);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        ENUMERATIONTOCOMPLEXTYPE_CREATE_RULE,
        Uml2servicemodelMessages.EnumerationToComplexType_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.COMPLEX_TYPE);
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getAttributeToAttributes_UsingPropertyToAttribute_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        ENUMERATIONTOCOMPLEXTYPE_ATTRIBUTE_TO_ATTRIBUTES_USING_PROPERTYTOATTRIBUTE_EXTRACTOR,
        Uml2servicemodelMessages.EnumerationToComplexType_Transform_AttributeToAttributes_UsingPropertyToAttribute_Extractor,
        registry.get(
            PropertyToAttributeTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.COMPLEX_TYPE__ATTRIBUTES)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.CLASSIFIER__ATTRIBUTE));
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getNameToName_Rule() {
    MoveRule rule = new MoveRule(
        ENUMERATIONTOCOMPLEXTYPE_NAME_TO_NAME_RULE,
        Uml2servicemodelMessages.EnumerationToComplexType_Transform_NameToName_Rule,
        new DirectFeatureAdapter(
            UMLPackage.Literals.NAMED_ELEMENT__NAME),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.COMPLEX_TYPE__NAME));
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getOwnedComment$BodyToDescription_Rule() {
    CustomRule rule = new CustomRule(
        ENUMERATIONTOCOMPLEXTYPE_OWNED_COMMENT$BODY_TO_DESCRIPTION_RULE,
        Uml2servicemodelMessages.EnumerationToComplexType_Transform_OwnedComment$BodyToDescription_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeOwnedComment$BodyToDescription_Rule(
                (org.eclipse.uml2.uml.Class) source,
                (ComplexType) target);
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
      org.eclipse.uml2.uml.Class Class_src, ComplexType ComplexType_tgt) {
    try {
      org.eclipse.emf.common.util.EList<org.eclipse.uml2.uml.Comment> comments = Class_src
          .getOwnedComments();
      for (org.eclipse.uml2.uml.Comment comment : comments) {
        org.eclipse.uml2.uml.Stereotype docStereotype = comment
            .getAppliedStereotype("Default::Documentation");
        if (docStereotype != null) {
          ComplexType_tgt.setDescription(comment.getBody());
        }
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform,
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform_OwnedComment$BodyToDescription_Rule,
                  Class_src == null ? null : Class_src
                      .getQualifiedName(),
                  ComplexType_tgt == null ? null
                      : ComplexType_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getToIsEnumeration_Rule() {
    CustomRule rule = new CustomRule(
        ENUMERATIONTOCOMPLEXTYPE__TO_IS_ENUMERATION_RULE,
        Uml2servicemodelMessages.EnumerationToComplexType_Transform_ToIsEnumeration_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToIsEnumeration_Rule(
                (org.eclipse.uml2.uml.Class) source,
                (ComplexType) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToIsEnumeration_Rule(
      org.eclipse.uml2.uml.Class Class_src, ComplexType ComplexType_tgt) {
    try {
      ComplexType_tgt.setIsEnumeration(true);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform,
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform_ToIsEnumeration_Rule,
                  Class_src == null ? null : Class_src
                      .getQualifiedName(),
                  ComplexType_tgt == null ? null
                      : ComplexType_tgt.getName() });
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
        ENUMERATIONTOCOMPLEXTYPE__TO_NAMESPACE_RULE,
        Uml2servicemodelMessages.EnumerationToComplexType_Transform_ToNamespace_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToNamespace_Rule(
                (org.eclipse.uml2.uml.Class) source,
                (ComplexType) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToNamespace_Rule(
      org.eclipse.uml2.uml.Class Class_src, ComplexType ComplexType_tgt) {
    try {
      org.eclipse.uml2.uml.Package classPackage = Class_src.getPackage();
      org.eclipse.uml2.uml.Stereotype classPackageStereotype = classPackage
          .getAppliedStereotype("XSDProfile::schema");
      if (classPackageStereotype != null) {
        String targetNamespace = (String) classPackage.getValue(
            classPackageStereotype, "targetNamespace");
        ComplexType_tgt.setNamespace(targetNamespace);
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform,
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform_ToNamespace_Rule,
                  Class_src == null ? null : Class_src
                      .getQualifiedName(),
                  ComplexType_tgt == null ? null
                      : ComplexType_tgt.getName() });
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
        ENUMERATIONTOCOMPLEXTYPE__TO_VERSION_RULE,
        Uml2servicemodelMessages.EnumerationToComplexType_Transform_ToVersion_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToVersion_Rule(
                (org.eclipse.uml2.uml.Class) source,
                (ComplexType) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToVersion_Rule(org.eclipse.uml2.uml.Class Class_src,
      ComplexType ComplexType_tgt) {
    try {
      org.eclipse.uml2.uml.Package classPackage = Class_src.getPackage();
      org.eclipse.uml2.uml.Stereotype classPackageStereotype = classPackage
          .getAppliedStereotype("XSDProfile::schema");
      if (classPackageStereotype != null) {
        String version = (String) classPackage.getValue(
            classPackageStereotype, "version");
        ComplexType_tgt.setVersion(version);
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform,
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform_ToVersion_Rule,
                  Class_src == null ? null : Class_src
                      .getQualifiedName(),
                  ComplexType_tgt == null ? null
                      : ComplexType_tgt.getName() });
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
        ENUMERATIONTOCOMPLEXTYPE__TO_UUID_RULE,
        Uml2servicemodelMessages.EnumerationToComplexType_Transform_ToUUID_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToUUID_Rule((org.eclipse.uml2.uml.Class) source,
                (ComplexType) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToUUID_Rule(org.eclipse.uml2.uml.Class Class_src,
      ComplexType ComplexType_tgt) {
    try {
      String xmiURI = Class_src.eResource().getURIFragment(Class_src);
      ComplexType_tgt.setUUID(xmiURI);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform,
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform_ToUUID_Rule,
                  Class_src == null ? null : Class_src
                      .getQualifiedName(),
                  ComplexType_tgt == null ? null
                      : ComplexType_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getToIsFault_Rule() {
    CustomRule rule = new CustomRule(
        ENUMERATIONTOCOMPLEXTYPE__TO_IS_FAULT_RULE,
        Uml2servicemodelMessages.EnumerationToComplexType_Transform_ToIsFault_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToIsFault_Rule(
                (org.eclipse.uml2.uml.Class) source,
                (ComplexType) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToIsFault_Rule(org.eclipse.uml2.uml.Class Class_src,
      ComplexType ComplexType_tgt) {
    try {
      ComplexType_tgt.setIsFault(false);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform,
                  Uml2servicemodelMessages.EnumerationToComplexType_Transform_ToIsFault_Rule,
                  Class_src == null ? null : Class_src
                      .getQualifiedName(),
                  ComplexType_tgt == null ? null
                      : ComplexType_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

}
