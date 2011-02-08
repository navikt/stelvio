/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel.transforms;

import com.ibm.xtools.transform.authoring.CreateRule;
import com.ibm.xtools.transform.authoring.CustomDirectFeatureAdapter;
import com.ibm.xtools.transform.authoring.DirectFeatureAdapter;
import com.ibm.xtools.transform.authoring.FeatureAdapter;
import com.ibm.xtools.transform.authoring.InstanceOfCondition;
import com.ibm.xtools.transform.authoring.MapTransform;
import com.ibm.xtools.transform.authoring.Registry;
import com.ibm.xtools.transform.authoring.SubmapExtractor;

import com.ibm.xtools.transform.core.AbstractContentExtractor;

import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ComplexTypeToComplexTypeTransform;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'ParameterToComplexTypeTransform' from the mapping.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ParameterToComplexTypeTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PARAMETERTOCOMPLEXTYPE_TRANSFORM = "ParameterToComplexType_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PARAMETERTOCOMPLEXTYPE_CREATE_RULE = PARAMETERTOCOMPLEXTYPE_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'ParameterToComplexType Type To ComplexType Using
   * ComplexTypeToComplexType Extractor' id <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  public static final String PARAMETERTOCOMPLEXTYPE_TYPE_TO_COMPLEX_TYPE_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR = PARAMETERTOCOMPLEXTYPE_TRANSFORM
      + "_TypeToComplexType_UsingComplexTypeToComplexType_Extractor";//$NON-NLS-1$

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public ParameterToComplexTypeTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(PARAMETERTOCOMPLEXTYPE_TRANSFORM,
        Uml2servicemodelMessages.ParameterToComplexType_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public ParameterToComplexTypeTransform(String id, String name,
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
    add(getTypeToComplexType_UsingComplexTypeToComplexType_Extractor(registry));
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
        PARAMETERTOCOMPLEXTYPE_CREATE_RULE,
        Uml2servicemodelMessages.ParameterToComplexType_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.COMPLEX_TYPE);
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getTypeToComplexType_UsingComplexTypeToComplexType_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PARAMETERTOCOMPLEXTYPE_TYPE_TO_COMPLEX_TYPE_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR,
        Uml2servicemodelMessages.ParameterToComplexType_Transform_TypeToComplexType_UsingComplexTypeToComplexType_Extractor,
        registry.get(ComplexTypeToComplexTypeTransform.class,
            new CustomDirectFeatureAdapter(

            (EStructuralFeature) null)), new DirectFeatureAdapter(
            UMLPackage.Literals.TYPED_ELEMENT__TYPE));
    return extractor;
  }

}
