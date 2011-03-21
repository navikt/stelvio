/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel.transforms;

import com.ibm.icu.text.MessageFormat;

import com.ibm.xtools.transform.authoring.CreateRule;
import com.ibm.xtools.transform.authoring.CustomDirectFeatureAdapter;
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
import com.ibm.xtools.transform.authoring.uml2.StereotypeFeatureAdapter;
import com.ibm.xtools.transform.core.AbstractContentExtractor;
import com.ibm.xtools.transform.core.AbstractRule;
import com.ibm.xtools.transform.core.TransformException;

import no.stelvio.esb.models.service.metamodel.Attribute;
import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ComplexTypeToComplexTypeTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.EnumerationToComplexTypeTransform;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.PropertyMappingToAttributeTransform;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'PropertyToAttributeTransform' from the mapping.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class PropertyToAttributeTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PROPERTYTOATTRIBUTE_TRANSFORM = "PropertyToAttribute_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PROPERTYTOATTRIBUTE_CREATE_RULE = PROPERTYTOATTRIBUTE_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'PropertyToAttribute Name to Name rule' id <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PROPERTYTOATTRIBUTE_NAME_TO_NAME_RULE = PROPERTYTOATTRIBUTE_TRANSFORM
      + "_NameToName_Rule";//$NON-NLS-1$

  /**
   * The 'PropertyToAttribute Type to TypeName rule' id <!-- begin-user-doc
   * --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PROPERTYTOATTRIBUTE_TYPE_TO_TYPE_NAME_RULE = PROPERTYTOATTRIBUTE_TRANSFORM
      + "_TypeToTypeName_Rule";//$NON-NLS-1$

  /**
   * The 'PropertyToAttribute Type To TypeRef Using ComplexTypeToComplexType
   * Extractor' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PROPERTYTOATTRIBUTE_TYPE_TO_TYPE_REF_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR = PROPERTYTOATTRIBUTE_TRANSFORM
      + "_TypeToTypeRef_UsingComplexTypeToComplexType_Extractor";//$NON-NLS-1$

  /**
   * The 'PropertyToAttribute UpperValue to IsList rule' id <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PROPERTYTOATTRIBUTE_UPPER_VALUE_TO_IS_LIST_RULE = PROPERTYTOATTRIBUTE_TRANSFORM
      + "_UpperValueToIsList_Rule";//$NON-NLS-1$

  /**
   * The 'PropertyToAttribute OwnedComment$Body to Description rule' id <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PROPERTYTOATTRIBUTE_OWNED_COMMENT$BODY_TO_DESCRIPTION_RULE = PROPERTYTOATTRIBUTE_TRANSFORM
      + "_OwnedComment$BodyToDescription_Rule";//$NON-NLS-1$

  /**
   * The 'PropertyToAttribute LowerValue to IsRequired rule' id <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PROPERTYTOATTRIBUTE_LOWER_VALUE_TO_IS_REQUIRED_RULE = PROPERTYTOATTRIBUTE_TRANSFORM
      + "_LowerValueToIsRequired_Rule";//$NON-NLS-1$

  /**
   * The 'PropertyToAttribute to UUID rule' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String PROPERTYTOATTRIBUTE__TO_UUID_RULE = PROPERTYTOATTRIBUTE_TRANSFORM
      + "_ToUUID_Rule";//$NON-NLS-1$

	/**
   * The 'PropertyToAttribute Type To TypeRef Using EnumerationToComplexType
   * Extractor' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PROPERTYTOATTRIBUTE_TYPE_TO_TYPE_REF_USING_ENUMERATIONTOCOMPLEXTYPE_EXTRACTOR = PROPERTYTOATTRIBUTE_TRANSFORM
      + "_TypeToTypeRef_UsingEnumerationToComplexType_Extractor";//$NON-NLS-1$

  /**
   * The 'PropertyToAttribute Property To Attribute Using
   * PropertyMappingToAttribute Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String PROPERTYTOATTRIBUTE_PROPERTY_TO_ATTRIBUTE_USING_PROPERTYMAPPINGTOATTRIBUTE_EXTRACTOR = PROPERTYTOATTRIBUTE_TRANSFORM
      + "_PropertyToAttribute_UsingPropertyMappingToAttribute_Extractor";//$NON-NLS-1$

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public PropertyToAttributeTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(PROPERTYTOATTRIBUTE_TRANSFORM,
        Uml2servicemodelMessages.PropertyToAttribute_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public PropertyToAttributeTransform(String id, String name,
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
    add(getTypeToTypeName_Rule());
    add(getTypeToTypeRef_UsingComplexTypeToComplexType_Extractor(registry));
    add(getUpperValueToIsList_Rule());
    add(getOwnedComment$BodyToDescription_Rule());
    add(getLowerValueToIsRequired_Rule());
    add(getToUUID_Rule());
    add(getTypeToTypeRef_UsingEnumerationToComplexType_Extractor(registry));
    add(getPropertyToAttribute_UsingPropertyMappingToAttribute_Extractor(registry));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new InstanceOfCondition(UMLPackage.Literals.PROPERTY);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        PROPERTYTOATTRIBUTE_CREATE_RULE,
        Uml2servicemodelMessages.PropertyToAttribute_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.ATTRIBUTE);
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getNameToName_Rule() {
    MoveRule rule = new MoveRule(
        PROPERTYTOATTRIBUTE_NAME_TO_NAME_RULE,
        Uml2servicemodelMessages.PropertyToAttribute_Transform_NameToName_Rule,
        new DirectFeatureAdapter(
            UMLPackage.Literals.NAMED_ELEMENT__NAME),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.ATTRIBUTE__NAME));
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getTypeToTypeName_Rule() {
    CustomRule rule = new CustomRule(
        PROPERTYTOATTRIBUTE_TYPE_TO_TYPE_NAME_RULE,
        Uml2servicemodelMessages.PropertyToAttribute_Transform_TypeToTypeName_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeTypeToTypeName_Rule((Property) source,
                (Attribute) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeTypeToTypeName_Rule(Property Property_src,
      Attribute Attribute_tgt) {
    try {
      if (Property_src.getType() != null)
        Attribute_tgt.setTypeName(Property_src.getType().getName());
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.PropertyToAttribute_Transform,
                  Uml2servicemodelMessages.PropertyToAttribute_Transform_TypeToTypeName_Rule,
                  Property_src == null ? null : Property_src
                      .getQualifiedName(),
                  Attribute_tgt == null ? null
                      : Attribute_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getTypeToTypeRef_UsingComplexTypeToComplexType_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PROPERTYTOATTRIBUTE_TYPE_TO_TYPE_REF_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR,
        Uml2servicemodelMessages.PropertyToAttribute_Transform_TypeToTypeRef_UsingComplexTypeToComplexType_Extractor,
        registry.get(
            ComplexTypeToComplexTypeTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.ATTRIBUTE__TYPE_REF)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.TYPED_ELEMENT__TYPE));
    extractor.setFilterCondition(new Condition() {
      public boolean isSatisfied(Object object) {
        return filterTypeToTypeRef_UsingComplexTypeToComplexType_Extractor((Type) object);
      }
    });
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected boolean filterTypeToTypeRef_UsingComplexTypeToComplexType_Extractor(
      Type type_src) {
    try {
      org.eclipse.uml2.uml.Stereotype stereotype = type_src
          .getAppliedStereotype("XSDProfile::complexType");
      return stereotype != null;
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor_source_filter,
              new String[] {
                  Uml2servicemodelMessages.PropertyToAttribute_Transform,
                  Uml2servicemodelMessages.PropertyToAttribute_Transform_TypeToTypeRef_UsingComplexTypeToComplexType_Extractor,
                  type_src == null ? null : type_src
                      .getQualifiedName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getUpperValueToIsList_Rule() {
    CustomRule rule = new CustomRule(
        PROPERTYTOATTRIBUTE_UPPER_VALUE_TO_IS_LIST_RULE,
        Uml2servicemodelMessages.PropertyToAttribute_Transform_UpperValueToIsList_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeUpperValueToIsList_Rule((Property) source,
                (Attribute) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeUpperValueToIsList_Rule(Property Property_src,
      Attribute Attribute_tgt) {
    try {
      int upperValue = Property_src.getUpper();
      if (upperValue == -1)
        Attribute_tgt.setIsList(true);
      else
        Attribute_tgt.setIsList(false);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.PropertyToAttribute_Transform,
                  Uml2servicemodelMessages.PropertyToAttribute_Transform_UpperValueToIsList_Rule,
                  Property_src == null ? null : Property_src
                      .getQualifiedName(),
                  Attribute_tgt == null ? null
                      : Attribute_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getOwnedComment$BodyToDescription_Rule() {
    CustomRule rule = new CustomRule(
        PROPERTYTOATTRIBUTE_OWNED_COMMENT$BODY_TO_DESCRIPTION_RULE,
        Uml2servicemodelMessages.PropertyToAttribute_Transform_OwnedComment$BodyToDescription_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeOwnedComment$BodyToDescription_Rule(
                (Property) source, (Attribute) target);
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
      Property Property_src, Attribute Attribute_tgt) {
    try {
      org.eclipse.emf.common.util.EList<org.eclipse.uml2.uml.Comment> comments = Property_src
          .getOwnedComments();
      for (org.eclipse.uml2.uml.Comment comment : comments) {
        org.eclipse.uml2.uml.Stereotype docStereotype = comment
            .getAppliedStereotype("Default::Documentation");
        if (docStereotype != null) {
          Attribute_tgt.setDescription(comment.getBody());
        }
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.PropertyToAttribute_Transform,
                  Uml2servicemodelMessages.PropertyToAttribute_Transform_OwnedComment$BodyToDescription_Rule,
                  Property_src == null ? null : Property_src
                      .getQualifiedName(),
                  Attribute_tgt == null ? null
                      : Attribute_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getLowerValueToIsRequired_Rule() {
    CustomRule rule = new CustomRule(
        PROPERTYTOATTRIBUTE_LOWER_VALUE_TO_IS_REQUIRED_RULE,
        Uml2servicemodelMessages.PropertyToAttribute_Transform_LowerValueToIsRequired_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeLowerValueToIsRequired_Rule((Property) source,
                (Attribute) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeLowerValueToIsRequired_Rule(Property Property_src,
      Attribute Attribute_tgt) {
    try {
      int lowerValue = Property_src.getLower();
      if (lowerValue == 0)
        Attribute_tgt.setIsRequired(false);
      else
        Attribute_tgt.setIsRequired(true);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.PropertyToAttribute_Transform,
                  Uml2servicemodelMessages.PropertyToAttribute_Transform_LowerValueToIsRequired_Rule,
                  Property_src == null ? null : Property_src
                      .getQualifiedName(),
                  Attribute_tgt == null ? null
                      : Attribute_tgt.getName() });
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
        PROPERTYTOATTRIBUTE__TO_UUID_RULE,
        Uml2servicemodelMessages.PropertyToAttribute_Transform_ToUUID_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToUUID_Rule((Property) source,
                (Attribute) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToUUID_Rule(Property Property_src,
      Attribute Attribute_tgt) {
    try {
      String xmiURI = Property_src.eResource().getURIFragment(
          Property_src);
      Attribute_tgt.setUUID(xmiURI);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.PropertyToAttribute_Transform,
                  Uml2servicemodelMessages.PropertyToAttribute_Transform_ToUUID_Rule,
                  Property_src == null ? null : Property_src
                      .getQualifiedName(),
                  Attribute_tgt == null ? null
                      : Attribute_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getTypeToTypeRef_UsingEnumerationToComplexType_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PROPERTYTOATTRIBUTE_TYPE_TO_TYPE_REF_USING_ENUMERATIONTOCOMPLEXTYPE_EXTRACTOR,
        Uml2servicemodelMessages.PropertyToAttribute_Transform_TypeToTypeRef_UsingEnumerationToComplexType_Extractor,
        registry.get(
            EnumerationToComplexTypeTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.ATTRIBUTE__TYPE_REF)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.TYPED_ELEMENT__TYPE));
    extractor.setFilterCondition(new Condition() {
      public boolean isSatisfied(Object object) {
        return filterTypeToTypeRef_UsingEnumerationToComplexType_Extractor((Type) object);
      }
    });
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected boolean filterTypeToTypeRef_UsingEnumerationToComplexType_Extractor(
      Type type_src) {
    try {
      org.eclipse.uml2.uml.Stereotype stereotype = type_src
          .getAppliedStereotype("XSDProfile::enumeration");
      return stereotype != null;
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor_source_filter,
              new String[] {
                  Uml2servicemodelMessages.PropertyToAttribute_Transform,
                  Uml2servicemodelMessages.PropertyToAttribute_Transform_TypeToTypeRef_UsingEnumerationToComplexType_Extractor,
                  type_src == null ? null : type_src
                      .getQualifiedName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getPropertyToAttribute_UsingPropertyMappingToAttribute_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PROPERTYTOATTRIBUTE_PROPERTY_TO_ATTRIBUTE_USING_PROPERTYMAPPINGTOATTRIBUTE_EXTRACTOR,
        Uml2servicemodelMessages.PropertyToAttribute_Transform_PropertyToAttribute_UsingPropertyMappingToAttribute_Extractor,
        registry.get(PropertyMappingToAttributeTransform.class,
            new CustomDirectFeatureAdapter(

            (EStructuralFeature) null)), new DirectFeatureAdapter(
            null));
    return extractor;
  }

}
