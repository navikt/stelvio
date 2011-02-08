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
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ComplexTypeToComplexTypeTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.EnumerationToComplexTypeTransform;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * An implementation of the 'DiagramToDiagramTransform' from the mapping. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class DiagramToDiagramTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String DIAGRAMTODIAGRAM_TRANSFORM = "DiagramToDiagram_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String DIAGRAMTODIAGRAM_CREATE_RULE = DIAGRAMTODIAGRAM_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'DiagramToDiagram Type to TypeName rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String DIAGRAMTODIAGRAM_TYPE_TO_TYPE_NAME_RULE = DIAGRAMTODIAGRAM_TRANSFORM
      + "_TypeToTypeName_Rule";//$NON-NLS-1$

	/**
   * The 'DiagramToDiagram Name to Name rule' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String DIAGRAMTODIAGRAM_NAME_TO_NAME_RULE = DIAGRAMTODIAGRAM_TRANSFORM
      + "_NameToName_Rule";//$NON-NLS-1$

	/**
   * The 'DiagramToDiagram to UUID rule' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String DIAGRAMTODIAGRAM__TO_UUID_RULE = DIAGRAMTODIAGRAM_TRANSFORM
      + "_ToUUID_Rule";//$NON-NLS-1$

	/**
   * The 'DiagramToDiagram PersistedChildren$Element To ComplexTypes Using
   * ComplexTypeToComplexType Extractor' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String DIAGRAMTODIAGRAM_PERSISTED_CHILDREN$ELEMENT_TO_COMPLEX_TYPES_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR = DIAGRAMTODIAGRAM_TRANSFORM
      + "_PersistedChildren$ElementToComplexTypes_UsingComplexTypeToComplexType_Extractor";//$NON-NLS-1$

	/**
   * The 'DiagramToDiagram PersistedChildren$Element To ComplexTypes Using
   * EnumerationToComplexType Extractor' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String DIAGRAMTODIAGRAM_PERSISTED_CHILDREN$ELEMENT_TO_COMPLEX_TYPES_USING_ENUMERATIONTOCOMPLEXTYPE_EXTRACTOR = DIAGRAMTODIAGRAM_TRANSFORM
      + "_PersistedChildren$ElementToComplexTypes_UsingEnumerationToComplexType_Extractor";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public DiagramToDiagramTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(DIAGRAMTODIAGRAM_TRANSFORM,
        Uml2servicemodelMessages.DiagramToDiagram_Transform, registry,
        referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public DiagramToDiagramTransform(String id, String name, Registry registry,
			FeatureAdapter referenceAdapter) {
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
    add(getTypeToTypeName_Rule());
    add(getNameToName_Rule());
    add(getToUUID_Rule());
    add(getPersistedChildren$ElementToComplexTypes_UsingComplexTypeToComplexType_Extractor(registry));
    add(getPersistedChildren$ElementToComplexTypes_UsingEnumerationToComplexType_Extractor(registry));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new InstanceOfCondition(NotationPackage.Literals.DIAGRAM);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        DIAGRAMTODIAGRAM_CREATE_RULE,
        Uml2servicemodelMessages.DiagramToDiagram_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.DIAGRAM);
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getTypeToTypeName_Rule() {
    MoveRule rule = new MoveRule(
        DIAGRAMTODIAGRAM_TYPE_TO_TYPE_NAME_RULE,
        Uml2servicemodelMessages.DiagramToDiagram_Transform_TypeToTypeName_Rule,
        new DirectFeatureAdapter(NotationPackage.Literals.VIEW__TYPE),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.DIAGRAM__TYPE_NAME));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getNameToName_Rule() {
    MoveRule rule = new MoveRule(
        DIAGRAMTODIAGRAM_NAME_TO_NAME_RULE,
        Uml2servicemodelMessages.DiagramToDiagram_Transform_NameToName_Rule,
        new DirectFeatureAdapter(NotationPackage.Literals.DIAGRAM__NAME),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.DIAGRAM__NAME));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getToUUID_Rule() {
    CustomRule rule = new CustomRule(
        DIAGRAMTODIAGRAM__TO_UUID_RULE,
        Uml2servicemodelMessages.DiagramToDiagram_Transform_ToUUID_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToUUID_Rule(
                (Diagram) source,
                (no.stelvio.esb.models.service.metamodel.Diagram) target);
          }
        });
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected void executeToUUID_Rule(Diagram Diagram_src,
			no.stelvio.esb.models.service.metamodel.Diagram Diagram_tgt) {
    try {
      String xmiURI = Diagram_src.eResource().getURIFragment(Diagram_src);
      Diagram_tgt.setUUID(xmiURI);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.DiagramToDiagram_Transform,
                  Uml2servicemodelMessages.DiagramToDiagram_Transform_ToUUID_Rule,
                  Diagram_src == null ? null : Diagram_src
                      .getName(),
                  Diagram_tgt == null ? null : Diagram_tgt
                      .getName() });
      throw new TransformException(message, e, null);
    }
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractContentExtractor getPersistedChildren$ElementToComplexTypes_UsingComplexTypeToComplexType_Extractor(
			Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        DIAGRAMTODIAGRAM_PERSISTED_CHILDREN$ELEMENT_TO_COMPLEX_TYPES_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR,
        Uml2servicemodelMessages.DiagramToDiagram_Transform_PersistedChildren$ElementToComplexTypes_UsingComplexTypeToComplexType_Extractor,
        registry.get(
            ComplexTypeToComplexTypeTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.DIAGRAM__COMPLEX_TYPES)),
        new DirectFeatureAdapter(
            NotationPackage.Literals.VIEW__PERSISTED_CHILDREN,
            "element"));
    extractor.setFilterCondition(new Condition() {
      public boolean isSatisfied(Object object) {
        return filterPersistedChildren$ElementToComplexTypes_UsingComplexTypeToComplexType_Extractor((EObject) object);
      }
    });
    return extractor;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected boolean filterPersistedChildren$ElementToComplexTypes_UsingComplexTypeToComplexType_Extractor(
			EObject element_src) {
    try {
      if (element_src instanceof org.eclipse.uml2.uml.Class) {
        org.eclipse.uml2.uml.Class c = (org.eclipse.uml2.uml.Class) element_src;
        org.eclipse.uml2.uml.Stereotype stereotype = c
            .getAppliedStereotype("XSDProfile::complexType");
        return stereotype != null;
      }
      return false;
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor_source_filter,
              new String[] {
                  Uml2servicemodelMessages.DiagramToDiagram_Transform,
                  Uml2servicemodelMessages.DiagramToDiagram_Transform_PersistedChildren$ElementToComplexTypes_UsingComplexTypeToComplexType_Extractor,
                  element_src == null ? null : element_src
                      .toString() });
      throw new TransformException(message, e, null);
    }
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractContentExtractor getPersistedChildren$ElementToComplexTypes_UsingEnumerationToComplexType_Extractor(
			Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        DIAGRAMTODIAGRAM_PERSISTED_CHILDREN$ELEMENT_TO_COMPLEX_TYPES_USING_ENUMERATIONTOCOMPLEXTYPE_EXTRACTOR,
        Uml2servicemodelMessages.DiagramToDiagram_Transform_PersistedChildren$ElementToComplexTypes_UsingEnumerationToComplexType_Extractor,
        registry.get(
            EnumerationToComplexTypeTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.DIAGRAM__COMPLEX_TYPES)),
        new DirectFeatureAdapter(
            NotationPackage.Literals.VIEW__PERSISTED_CHILDREN,
            "element"));
    extractor.setFilterCondition(new Condition() {
      public boolean isSatisfied(Object object) {
        return filterPersistedChildren$ElementToComplexTypes_UsingEnumerationToComplexType_Extractor((EObject) object);
      }
    });
    return extractor;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected boolean filterPersistedChildren$ElementToComplexTypes_UsingEnumerationToComplexType_Extractor(
			EObject element_src) {
    try {
      if (element_src instanceof org.eclipse.uml2.uml.Class) {
        org.eclipse.uml2.uml.Class c = (org.eclipse.uml2.uml.Class) element_src;
        org.eclipse.uml2.uml.Stereotype stereotype = c
            .getAppliedStereotype("XSDProfile::enumeration");
        return stereotype != null;
      }
      return false;
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_extractor_source_filter,
              new String[] {
                  Uml2servicemodelMessages.DiagramToDiagram_Transform,
                  Uml2servicemodelMessages.DiagramToDiagram_Transform_PersistedChildren$ElementToComplexTypes_UsingEnumerationToComplexType_Extractor,
                  element_src == null ? null : element_src
                      .toString() });
      throw new TransformException(message, e, null);
    }
  }

}
