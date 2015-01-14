/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel.transforms;

import java.util.Collection;

import no.stelvio.esb.models.service.metamodel.Attachment;
import no.stelvio.esb.models.service.metamodel.Attribute;
import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;
import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ComplexTypeToComplexTypeTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.PropertyToAttributeTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.UrlToAttachmentTransform;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;

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
import com.ibm.xtools.transform.core.AbstractContentExtractor;
import com.ibm.xtools.transform.core.AbstractRule;
import com.ibm.xtools.transform.core.TransformException;

/**
 * An implementation of the 'ComplexTypeToComplexTypeTransform' from the
 * mapping. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ComplexTypeToComplexTypeTransform extends MapTransform {
	//private Logger logger = Logger.getLogger(ComplexTypeToComplexTypeTransform.class);

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM = "ComplexTypeToComplexType_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String COMPLEXTYPETOCOMPLEXTYPE_CREATE_RULE = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'ComplexTypeToComplexType Name to Name rule' id <!-- begin-user-doc
   * --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String COMPLEXTYPETOCOMPLEXTYPE_NAME_TO_NAME_RULE = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_NameToName_Rule";//$NON-NLS-1$

  /**
   * The 'ComplexTypeToComplexType to Version rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String COMPLEXTYPETOCOMPLEXTYPE__TO_VERSION_RULE = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_ToVersion_Rule";//$NON-NLS-1$

	/**
   * The 'ComplexTypeToComplexType to Namespace rule' id <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String COMPLEXTYPETOCOMPLEXTYPE__TO_NAMESPACE_RULE = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_ToNamespace_Rule";//$NON-NLS-1$

	/**
   * The 'ComplexTypeToComplexType to UUID rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String COMPLEXTYPETOCOMPLEXTYPE__TO_UUID_RULE = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_ToUUID_Rule";//$NON-NLS-1$

	/**
   * The 'ComplexTypeToComplexType Attribute To Attributes Using
   * PropertyToAttribute Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String COMPLEXTYPETOCOMPLEXTYPE_ATTRIBUTE_TO_ATTRIBUTES_USING_PROPERTYTOATTRIBUTE_EXTRACTOR = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_AttributeToAttributes_UsingPropertyToAttribute_Extractor";//$NON-NLS-1$

  /**
	 * The 'ComplexTypeToComplexType to IsEnumeration rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String COMPLEXTYPETOCOMPLEXTYPE__TO_IS_ENUMERATION_RULE = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_ToIsEnumeration_Rule";//$NON-NLS-1$

	/**
   * The 'ComplexTypeToComplexType to IsFault rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String COMPLEXTYPETOCOMPLEXTYPE__TO_IS_FAULT_RULE = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_ToIsFault_Rule";//$NON-NLS-1$

	/**
   * The 'ComplexTypeToComplexType OwnedComment To Attachments Using
   * UrlToAttachment Extractor' id <!-- begin-user-doc --> <!-- end-user-doc
   * -->
   * 
   * @generated
   */
  public static final String COMPLEXTYPETOCOMPLEXTYPE_OWNED_COMMENT_TO_ATTACHMENTS_USING_URLTOATTACHMENT_EXTRACTOR = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_OwnedCommentToAttachments_UsingUrlToAttachment_Extractor";//$NON-NLS-1$

  /**
   * The 'ComplexTypeToComplexType OwnedComment to Description rule' id <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String COMPLEXTYPETOCOMPLEXTYPE_OWNED_COMMENT_TO_DESCRIPTION_RULE = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_OwnedCommentToDescription_Rule";//$NON-NLS-1$

  /**
   * The 'ComplexTypeToComplexType Generalization$Target To Generalizations
   * Using ComplexTypeToComplexType Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String COMPLEXTYPETOCOMPLEXTYPE_GENERALIZATION$TARGET_TO_GENERALIZATIONS_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR = COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM
      + "_Generalization$TargetToGeneralizations_UsingComplexTypeToComplexType_Extractor";//$NON-NLS-1$

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public ComplexTypeToComplexTypeTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(COMPLEXTYPETOCOMPLEXTYPE_TRANSFORM,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public ComplexTypeToComplexTypeTransform(String id, String name,
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
    add(getToVersion_Rule());
    add(getToNamespace_Rule());
    add(getToUUID_Rule());
    add(getAttributeToAttributes_UsingPropertyToAttribute_Extractor(registry));
    add(getToIsEnumeration_Rule());
    add(getToIsFault_Rule());
    add(getOwnedCommentToAttachments_UsingUrlToAttachment_Extractor(registry));
    add(getOwnedCommentToDescription_Rule());
    add(getGeneralization$TargetToGeneralizations_UsingComplexTypeToComplexType_Extractor(registry));
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
        COMPLEXTYPETOCOMPLEXTYPE_CREATE_RULE,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.COMPLEX_TYPE);
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getNameToName_Rule() {
    MoveRule rule = new MoveRule(
        COMPLEXTYPETOCOMPLEXTYPE_NAME_TO_NAME_RULE,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_NameToName_Rule,
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
  protected AbstractRule getToVersion_Rule() {
    CustomRule rule = new CustomRule(
        COMPLEXTYPETOCOMPLEXTYPE__TO_VERSION_RULE,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_ToVersion_Rule,
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
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform,
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_ToVersion_Rule,
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
        COMPLEXTYPETOCOMPLEXTYPE__TO_NAMESPACE_RULE,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_ToNamespace_Rule,
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
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform,
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_ToNamespace_Rule,
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
        COMPLEXTYPETOCOMPLEXTYPE__TO_UUID_RULE,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_ToUUID_Rule,
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
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform,
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_ToUUID_Rule,
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
  protected AbstractContentExtractor getAttributeToAttributes_UsingPropertyToAttribute_Extractor(
      Registry registry) {
    CustomExtractor extractor = new CustomExtractor(
        COMPLEXTYPETOCOMPLEXTYPE_ATTRIBUTE_TO_ATTRIBUTES_USING_PROPERTYTOATTRIBUTE_EXTRACTOR,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_AttributeToAttributes_UsingPropertyToAttribute_Extractor,
        registry.get(
            PropertyToAttributeTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.COMPLEX_TYPE__ATTRIBUTES)),
        new ExtractorExtension() {
          public Collection execute(EObject source) {
            return extendAttributeToAttributes_UsingPropertyToAttribute_Extractor((org.eclipse.uml2.uml.Class) source);
          }
        });
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected Collection extendAttributeToAttributes_UsingPropertyToAttribute_Extractor(
      org.eclipse.uml2.uml.Class Class_src) {
    try {
      return Class_src.getAllAttributes();
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor,
              new String[] {
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform,
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_AttributeToAttributes_UsingPropertyToAttribute_Extractor,
                  Class_src == null ? null : Class_src
                      .getQualifiedName() });
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
        COMPLEXTYPETOCOMPLEXTYPE__TO_IS_ENUMERATION_RULE,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_ToIsEnumeration_Rule,
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
      ComplexType_tgt.setIsEnumeration(false);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform,
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_ToIsEnumeration_Rule,
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
        COMPLEXTYPETOCOMPLEXTYPE__TO_IS_FAULT_RULE,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_ToIsFault_Rule,
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
      org.eclipse.uml2.uml.Stereotype faultMetaDataStereotype = Class_src
          .getAppliedStereotype("NAV UML Profile::FaultMetaData");
      if (faultMetaDataStereotype != null)
        ComplexType_tgt.setIsFault(true);
      else
        ComplexType_tgt.setIsFault(false);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform,
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_ToIsFault_Rule,
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
  protected AbstractContentExtractor getOwnedCommentToAttachments_UsingUrlToAttachment_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        COMPLEXTYPETOCOMPLEXTYPE_OWNED_COMMENT_TO_ATTACHMENTS_USING_URLTOATTACHMENT_EXTRACTOR,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_OwnedCommentToAttachments_UsingUrlToAttachment_Extractor,
        registry.get(
            UrlToAttachmentTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.COMPLEX_TYPE__ATTACHMENTS)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.ELEMENT__OWNED_COMMENT));
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getOwnedCommentToDescription_Rule() {
    CustomRule rule = new CustomRule(
        COMPLEXTYPETOCOMPLEXTYPE_OWNED_COMMENT_TO_DESCRIPTION_RULE,
        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_OwnedCommentToDescription_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeOwnedCommentToDescription_Rule(
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
  protected void executeOwnedCommentToDescription_Rule(
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
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform,
                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_OwnedCommentToDescription_Rule,
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
  protected AbstractContentExtractor getGeneralization$TargetToGeneralizations_UsingComplexTypeToComplexType_Extractor(Registry registry) {
	    SubmapExtractor extractor = new SubmapExtractor(
	        COMPLEXTYPETOCOMPLEXTYPE_GENERALIZATION$TARGET_TO_GENERALIZATIONS_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR,
	        Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_Generalization$TargetToGeneralizations_UsingComplexTypeToComplexType_Extractor,
	        registry.get(
	            ComplexTypeToComplexTypeTransform.class,
	            new DirectFeatureAdapter(
	                ServiceMetamodelPackage.Literals.COMPLEX_TYPE__GENERALIZATIONS)),
	        new DirectFeatureAdapter(
	            UMLPackage.Literals.CLASSIFIER__GENERALIZATION,
	            "target"));
		
		// TEST: Ikke map primitive typer
		extractor.setFilterCondition(new Condition() {
	      public boolean isSatisfied(Object object) {
	        return filterTargetToGeneralizations_UsingComplexTypeToComplexType_Extractor((Element) object);
	      }
	    });
		
	    return extractor;
	  }
	  
	  
	  protected boolean filterTargetToGeneralizations_UsingComplexTypeToComplexType_Extractor(
	      Element element_src) {
	    try {
	      //org.eclipse.uml2.uml.Stereotype stereotype = element_src.getAppliedStereotype("XSDProfile::complexType");
	      //return stereotype != null;
	    	
	    	/*
	    	if (element_src != null) {
	    		logger.debug("-----");
	    		
	    		// Stereotypes:
	    		int i = 1;
		    	for (Stereotype stereotype : element_src.getAppliedStereotypes()) {
		    		logger.debug("TKN - stereotype-name "+i+": " + stereotype.getName());
		    		logger.debug("TKN - stereotype-label "+i+": " + stereotype.getLabel());
		    		logger.debug("TKN - stereotype-qualified name "+i+": " + stereotype.getQualifiedName());
		    	}
		    	
		    	// Modell:
		    	if (element_src.getModel() != null) {
		    		logger.debug("TKN - Modell-Name: " + element_src.getModel().getName());
		    		logger.debug("TKN - Modell-Label: " + element_src.getModel().getLabel());
		    		logger.debug("TKN - Modell-getQualifiedName: " + element_src.getModel().getQualifiedName());
		    		if (element_src.getModel().getNamespace() != null) {
		    			logger.debug("TKN - Modell-getNamespace-getQualifiedName: " + element_src.getModel().getNamespace().getQualifiedName());
		    		}
		    	}
		    	
		    	// Keywords:
		    	i = 1;
		    	for (String keyword : element_src.getKeywords()) {
		    		logger.debug("TKN - Nokkelord " + i + ": " + keyword);
		    		i++;
		    	}
	    	}
	    	else {
	    		logger.debug("TKN - element_src er null");
	    	}
	    	*/

	    	for (Stereotype stereotype : element_src.getAppliedStereotypes()) {
	    		if (stereotype.getName() == "complexType") {
	    			return true;
	    		}
	    	}
	    	return false;
	    } catch (Exception e) {
	      String message = MessageFormat
	          .format(Uml2servicemodelMessages.exception_extractor_source_filter,
	              new String[] {
	                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform,
	                  Uml2servicemodelMessages.ComplexTypeToComplexType_Transform_Generalization$TargetToGeneralizations_UsingComplexTypeToComplexType_Extractor,
	                  element_src == null ? null
	                      : element_src.toString() });
	      throw new TransformException(message, e, null);
	    }
	  }
}
