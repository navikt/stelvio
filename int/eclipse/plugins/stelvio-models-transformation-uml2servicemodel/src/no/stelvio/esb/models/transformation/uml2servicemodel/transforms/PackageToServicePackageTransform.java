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
import no.stelvio.esb.models.service.metamodel.Diagram;
import no.stelvio.esb.models.service.metamodel.ServiceInterface;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;
import no.stelvio.esb.models.service.metamodel.ServicePackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ComplexTypeToComplexTypeTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.DiagramToDiagramTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.EnumerationToComplexTypeTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.InterfaceToServiceInterfaceForComponentTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.InterfaceToServiceInterfaceTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.PackageToServicePackageTransform;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'PackageToServicePackageTransform' from the mapping.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class PackageToServicePackageTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PACKAGETOSERVICEPACKAGE_TRANSFORM = "PackageToServicePackage_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PACKAGETOSERVICEPACKAGE_CREATE_RULE = PACKAGETOSERVICEPACKAGE_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'PackageToServicePackage PackagedElement To ServiceInterface Using
   * InterfaceToServiceInterface Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String PACKAGETOSERVICEPACKAGE_PACKAGED_ELEMENT_TO_SERVICE_INTERFACE_USING_INTERFACETOSERVICEINTERFACE_EXTRACTOR = PACKAGETOSERVICEPACKAGE_TRANSFORM
      + "_PackagedElementToServiceInterface_UsingInterfaceToServiceInterface_Extractor";//$NON-NLS-1$

  /**
   * The 'PackageToServicePackage Name to Name rule' id <!-- begin-user-doc
   * --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PACKAGETOSERVICEPACKAGE_NAME_TO_NAME_RULE = PACKAGETOSERVICEPACKAGE_TRANSFORM
      + "_NameToName_Rule";//$NON-NLS-1$

  /**
   * The 'PackageToServicePackage PackagedElement To ChildPackages Using
   * PackageToServicePackage Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String PACKAGETOSERVICEPACKAGE_PACKAGED_ELEMENT_TO_CHILD_PACKAGES_USING_PACKAGETOSERVICEPACKAGE_EXTRACTOR = PACKAGETOSERVICEPACKAGE_TRANSFORM
      + "_PackagedElementToChildPackages_UsingPackageToServicePackage_Extractor";//$NON-NLS-1$

  /**
   * The 'PackageToServicePackage to UUID rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PACKAGETOSERVICEPACKAGE__TO_UUID_RULE = PACKAGETOSERVICEPACKAGE_TRANSFORM
      + "_ToUUID_Rule";//$NON-NLS-1$

	/**
   * The 'PackageToServicePackage OwnedComment$Body to Description rule' id
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PACKAGETOSERVICEPACKAGE_OWNED_COMMENT$BODY_TO_DESCRIPTION_RULE = PACKAGETOSERVICEPACKAGE_TRANSFORM
      + "_OwnedComment$BodyToDescription_Rule";//$NON-NLS-1$

  /**
   * The 'PackageToServicePackage PackagedElement To ComplexTypes Using
   * ComplexTypeToComplexType Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String PACKAGETOSERVICEPACKAGE_PACKAGED_ELEMENT_TO_COMPLEX_TYPES_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR = PACKAGETOSERVICEPACKAGE_TRANSFORM
      + "_PackagedElementToComplexTypes_UsingComplexTypeToComplexType_Extractor";//$NON-NLS-1$

  /**
   * The 'PackageToServicePackage PackagedElement To ComplexTypes Using
   * EnumerationToComplexType Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String PACKAGETOSERVICEPACKAGE_PACKAGED_ELEMENT_TO_COMPLEX_TYPES_USING_ENUMERATIONTOCOMPLEXTYPE_EXTRACTOR = PACKAGETOSERVICEPACKAGE_TRANSFORM
      + "_PackagedElementToComplexTypes_UsingEnumerationToComplexType_Extractor";//$NON-NLS-1$

  /**
   * The 'PackageToServicePackage EAnnotations$Contents To Diagrams Using
   * DiagramToDiagram Extractor' id <!-- begin-user-doc --> <!-- end-user-doc
   * -->
   * 
   * @generated
   */
  public static final String PACKAGETOSERVICEPACKAGE_EANNOTATIONS$CONTENTS_TO_DIAGRAMS_USING_DIAGRAMTODIAGRAM_EXTRACTOR = PACKAGETOSERVICEPACKAGE_TRANSFORM
      + "_EAnnotations$ContentsToDiagrams_UsingDiagramToDiagram_Extractor";//$NON-NLS-1$

  /**
   * The 'PackageToServicePackage PackagedElement$OwnedElement To
   * ServiceInterface Using InterfaceToServiceInterfaceForComponent Extractor'
   * id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PACKAGETOSERVICEPACKAGE_PACKAGED_ELEMENT$OWNED_ELEMENT_TO_SERVICE_INTERFACE_USING_INTERFACETOSERVICEINTERFACEFORCOMPONENT_EXTRACTOR = PACKAGETOSERVICEPACKAGE_TRANSFORM
      + "_PackagedElement$OwnedElementToServiceInterface_UsingInterfaceToServiceInterfaceForComponent_Extractor";//$NON-NLS-1$

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public PackageToServicePackageTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(PACKAGETOSERVICEPACKAGE_TRANSFORM,
        Uml2servicemodelMessages.PackageToServicePackage_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public PackageToServicePackageTransform(String id, String name,
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
    add(getPackagedElementToServiceInterface_UsingInterfaceToServiceInterface_Extractor(registry));
    add(getNameToName_Rule());
    add(getPackagedElementToChildPackages_UsingPackageToServicePackage_Extractor(registry));
    add(getToUUID_Rule());
    add(getOwnedComment$BodyToDescription_Rule());
    add(getPackagedElementToComplexTypes_UsingComplexTypeToComplexType_Extractor(registry));
    add(getPackagedElementToComplexTypes_UsingEnumerationToComplexType_Extractor(registry));
    add(getEAnnotations$ContentsToDiagrams_UsingDiagramToDiagram_Extractor(registry));
    add(getPackagedElement$OwnedElementToServiceInterface_UsingInterfaceToServiceInterfaceForComponent_Extractor(registry));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new InstanceOfCondition(UMLPackage.Literals.PACKAGE);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        PACKAGETOSERVICEPACKAGE_CREATE_RULE,
        Uml2servicemodelMessages.PackageToServicePackage_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.SERVICE_PACKAGE);
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getPackagedElementToServiceInterface_UsingInterfaceToServiceInterface_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PACKAGETOSERVICEPACKAGE_PACKAGED_ELEMENT_TO_SERVICE_INTERFACE_USING_INTERFACETOSERVICEINTERFACE_EXTRACTOR,
        Uml2servicemodelMessages.PackageToServicePackage_Transform_PackagedElementToServiceInterface_UsingInterfaceToServiceInterface_Extractor,
        registry.get(
            InterfaceToServiceInterfaceTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_PACKAGE__SERVICE_INTERFACE)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.PACKAGE__PACKAGED_ELEMENT));
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getNameToName_Rule() {
    MoveRule rule = new MoveRule(
        PACKAGETOSERVICEPACKAGE_NAME_TO_NAME_RULE,
        Uml2servicemodelMessages.PackageToServicePackage_Transform_NameToName_Rule,
        new DirectFeatureAdapter(
            UMLPackage.Literals.NAMED_ELEMENT__NAME),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.SERVICE_PACKAGE__NAME));
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getPackagedElementToChildPackages_UsingPackageToServicePackage_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PACKAGETOSERVICEPACKAGE_PACKAGED_ELEMENT_TO_CHILD_PACKAGES_USING_PACKAGETOSERVICEPACKAGE_EXTRACTOR,
        Uml2servicemodelMessages.PackageToServicePackage_Transform_PackagedElementToChildPackages_UsingPackageToServicePackage_Extractor,
        registry.get(
            PackageToServicePackageTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_PACKAGE__CHILD_PACKAGES)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.PACKAGE__PACKAGED_ELEMENT));
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getToUUID_Rule() {
    CustomRule rule = new CustomRule(
        PACKAGETOSERVICEPACKAGE__TO_UUID_RULE,
        Uml2servicemodelMessages.PackageToServicePackage_Transform_ToUUID_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToUUID_Rule(
                (org.eclipse.uml2.uml.Package) source,
                (ServicePackage) target);
          }
        });
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void executeToUUID_Rule(org.eclipse.uml2.uml.Package Package_src,
      ServicePackage ServicePackage_tgt) {
    try {
      String xmiURI = Package_src.eResource().getURIFragment(Package_src);
      ServicePackage_tgt.setUUID(xmiURI);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.PackageToServicePackage_Transform,
                  Uml2servicemodelMessages.PackageToServicePackage_Transform_ToUUID_Rule,
                  Package_src == null ? null : Package_src
                      .getQualifiedName(),
                  ServicePackage_tgt == null ? null
                      : ServicePackage_tgt.getName() });
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
        PACKAGETOSERVICEPACKAGE_OWNED_COMMENT$BODY_TO_DESCRIPTION_RULE,
        Uml2servicemodelMessages.PackageToServicePackage_Transform_OwnedComment$BodyToDescription_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeOwnedComment$BodyToDescription_Rule(
                (org.eclipse.uml2.uml.Package) source,
                (ServicePackage) target);
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
      org.eclipse.uml2.uml.Package Package_src,
      ServicePackage ServicePackage_tgt) {
    try {
      org.eclipse.emf.common.util.EList<org.eclipse.uml2.uml.Comment> comments = Package_src
          .getOwnedComments();
      for (org.eclipse.uml2.uml.Comment comment : comments) {
        org.eclipse.uml2.uml.Stereotype docStereotype = comment
            .getAppliedStereotype("Default::Documentation");
        if (docStereotype != null) {
          ServicePackage_tgt.setDescription(comment.getBody());
        }
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.PackageToServicePackage_Transform,
                  Uml2servicemodelMessages.PackageToServicePackage_Transform_OwnedComment$BodyToDescription_Rule,
                  Package_src == null ? null : Package_src
                      .getQualifiedName(),
                  ServicePackage_tgt == null ? null
                      : ServicePackage_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getPackagedElementToComplexTypes_UsingComplexTypeToComplexType_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PACKAGETOSERVICEPACKAGE_PACKAGED_ELEMENT_TO_COMPLEX_TYPES_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR,
        Uml2servicemodelMessages.PackageToServicePackage_Transform_PackagedElementToComplexTypes_UsingComplexTypeToComplexType_Extractor,
        registry.get(
            ComplexTypeToComplexTypeTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_PACKAGE__COMPLEX_TYPES)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.PACKAGE__PACKAGED_ELEMENT));
    extractor.setFilterCondition(new Condition() {
      public boolean isSatisfied(Object object) {
        return filterPackagedElementToComplexTypes_UsingComplexTypeToComplexType_Extractor((PackageableElement) object);
      }
    });
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected boolean filterPackagedElementToComplexTypes_UsingComplexTypeToComplexType_Extractor(
      PackageableElement packagedElement_src) {
    try {
      org.eclipse.uml2.uml.Stereotype stereotype = packagedElement_src
          .getAppliedStereotype("XSDProfile::complexType");
      return stereotype != null;
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor_source_filter,
              new String[] {
                  Uml2servicemodelMessages.PackageToServicePackage_Transform,
                  Uml2servicemodelMessages.PackageToServicePackage_Transform_PackagedElementToComplexTypes_UsingComplexTypeToComplexType_Extractor,
                  packagedElement_src == null ? null
                      : packagedElement_src
                          .getQualifiedName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getPackagedElementToComplexTypes_UsingEnumerationToComplexType_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PACKAGETOSERVICEPACKAGE_PACKAGED_ELEMENT_TO_COMPLEX_TYPES_USING_ENUMERATIONTOCOMPLEXTYPE_EXTRACTOR,
        Uml2servicemodelMessages.PackageToServicePackage_Transform_PackagedElementToComplexTypes_UsingEnumerationToComplexType_Extractor,
        registry.get(
            EnumerationToComplexTypeTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_PACKAGE__COMPLEX_TYPES)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.PACKAGE__PACKAGED_ELEMENT));
    extractor.setFilterCondition(new Condition() {
      public boolean isSatisfied(Object object) {
        return filterPackagedElementToComplexTypes_UsingEnumerationToComplexType_Extractor((PackageableElement) object);
      }
    });
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected boolean filterPackagedElementToComplexTypes_UsingEnumerationToComplexType_Extractor(
      PackageableElement packagedElement_src) {
    try {
      org.eclipse.uml2.uml.Stereotype stereotype = packagedElement_src
          .getAppliedStereotype("XSDProfile::enumeration");
      return stereotype != null;
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor_source_filter,
              new String[] {
                  Uml2servicemodelMessages.PackageToServicePackage_Transform,
                  Uml2servicemodelMessages.PackageToServicePackage_Transform_PackagedElementToComplexTypes_UsingEnumerationToComplexType_Extractor,
                  packagedElement_src == null ? null
                      : packagedElement_src
                          .getQualifiedName() });
      throw new TransformException(message, e, null);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getEAnnotations$ContentsToDiagrams_UsingDiagramToDiagram_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PACKAGETOSERVICEPACKAGE_EANNOTATIONS$CONTENTS_TO_DIAGRAMS_USING_DIAGRAMTODIAGRAM_EXTRACTOR,
        Uml2servicemodelMessages.PackageToServicePackage_Transform_EAnnotations$ContentsToDiagrams_UsingDiagramToDiagram_Extractor,
        registry.get(
            DiagramToDiagramTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_PACKAGE__DIAGRAMS)),
        new DirectFeatureAdapter(
            EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS,
            "contents"));
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getPackagedElement$OwnedElementToServiceInterface_UsingInterfaceToServiceInterfaceForComponent_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PACKAGETOSERVICEPACKAGE_PACKAGED_ELEMENT$OWNED_ELEMENT_TO_SERVICE_INTERFACE_USING_INTERFACETOSERVICEINTERFACEFORCOMPONENT_EXTRACTOR,
        Uml2servicemodelMessages.PackageToServicePackage_Transform_PackagedElement$OwnedElementToServiceInterface_UsingInterfaceToServiceInterfaceForComponent_Extractor,
        registry.get(
            InterfaceToServiceInterfaceForComponentTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.SERVICE_PACKAGE__SERVICE_INTERFACE)),
        new DirectFeatureAdapter(
            UMLPackage.Literals.PACKAGE__PACKAGED_ELEMENT,
            "ownedElement"));
    extractor.setAcceptCondition(new Condition() {
      public boolean isSatisfied(Object object) {
        return acceptPackagedElement$OwnedElementToServiceInterface_UsingInterfaceToServiceInterfaceForComponent_Extractor((org.eclipse.uml2.uml.Package) object);
      }
    });
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected boolean acceptPackagedElement$OwnedElementToServiceInterface_UsingInterfaceToServiceInterfaceForComponent_Extractor(
      org.eclipse.uml2.uml.Package Package_src) {
    try {
      org.eclipse.emf.common.util.EList<org.eclipse.uml2.uml.Element> liste1 = Package_src
          .getOwnedElements();
      for (org.eclipse.uml2.uml.Element e : liste1) {
        org.eclipse.emf.common.util.EList<org.eclipse.uml2.uml.Stereotype> liste2 = e
            .getAppliedStereotypes();
        for (org.eclipse.uml2.uml.Stereotype s : liste2) {
          if ("Definition".equals(s.getName())) {
            return true;
          }
        }
      }
      return false;
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor_accept,
              new String[] {
                  Uml2servicemodelMessages.PackageToServicePackage_Transform,
                  Uml2servicemodelMessages.PackageToServicePackage_Transform_PackagedElement$OwnedElementToServiceInterface_UsingInterfaceToServiceInterfaceForComponent_Extractor,
                  Package_src == null ? null : Package_src
                      .getQualifiedName() });
      throw new TransformException(message, e, null);
    }
  }

}
