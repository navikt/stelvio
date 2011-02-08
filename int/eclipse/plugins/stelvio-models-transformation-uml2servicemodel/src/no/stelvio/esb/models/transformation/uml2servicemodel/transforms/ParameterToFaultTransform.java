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

import no.stelvio.esb.models.service.metamodel.Fault;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.FaultMetaDataToFaultTransform;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'ParameterToFaultTransform' from the mapping. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ParameterToFaultTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PARAMETERTOFAULT_TRANSFORM = "ParameterToFault_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String PARAMETERTOFAULT_CREATE_RULE = PARAMETERTOFAULT_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'ParameterToFault Type To Fault Using FaultMetaDataToFault Extractor'
   * id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String PARAMETERTOFAULT_TYPE_TO_FAULT_USING_FAULTMETADATATOFAULT_EXTRACTOR = PARAMETERTOFAULT_TRANSFORM
      + "_TypeToFault_UsingFaultMetaDataToFault_Extractor";//$NON-NLS-1$

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public ParameterToFaultTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(PARAMETERTOFAULT_TRANSFORM,
        Uml2servicemodelMessages.ParameterToFault_Transform, registry,
        referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public ParameterToFaultTransform(String id, String name, Registry registry,
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
    add(getTypeToFault_UsingFaultMetaDataToFault_Extractor(registry));
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
        PARAMETERTOFAULT_CREATE_RULE,
        Uml2servicemodelMessages.ParameterToFault_Transform_Create_Rule,
        this, referenceAdapter, ServiceMetamodelPackage.Literals.FAULT);
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getTypeToFault_UsingFaultMetaDataToFault_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        PARAMETERTOFAULT_TYPE_TO_FAULT_USING_FAULTMETADATATOFAULT_EXTRACTOR,
        Uml2servicemodelMessages.ParameterToFault_Transform_TypeToFault_UsingFaultMetaDataToFault_Extractor,
        registry.get(FaultMetaDataToFaultTransform.class,
            new CustomDirectFeatureAdapter(

            (EStructuralFeature) null)), new DirectFeatureAdapter(
            UMLPackage.Literals.TYPED_ELEMENT__TYPE));
    return extractor;
  }

}
