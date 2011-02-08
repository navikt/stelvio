/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel.transforms;

import com.ibm.xtools.transform.authoring.CreateRule;
import com.ibm.xtools.transform.authoring.DirectFeatureAdapter;
import com.ibm.xtools.transform.authoring.FeatureAdapter;
import com.ibm.xtools.transform.authoring.InstanceOfCondition;
import com.ibm.xtools.transform.authoring.MapTransform;
import com.ibm.xtools.transform.authoring.MoveRule;
import com.ibm.xtools.transform.authoring.Registry;
import com.ibm.xtools.transform.authoring.SubmapExtractor;

import com.ibm.xtools.transform.authoring.uml2.StereotypeCondition;
import com.ibm.xtools.transform.authoring.uml2.StereotypeFeatureAdapter;

import com.ibm.xtools.transform.core.AbstractContentExtractor;
import com.ibm.xtools.transform.core.AbstractRule;

import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.Fault;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ComplexTypeToComplexTypeTransform;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'FaultMetaDataToFaultTransform' from the mapping.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class FaultMetaDataToFaultTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String FAULTMETADATATOFAULT_TRANSFORM = "FaultMetaDataToFault_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String FAULTMETADATATOFAULT_CREATE_RULE = FAULTMETADATATOFAULT_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'FaultMetaDataToFault FeilId to Id rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String FAULTMETADATATOFAULT_FEIL_ID_TO_ID_RULE = FAULTMETADATATOFAULT_TRANSFORM
      + "_FeilIdToId_Rule";//$NON-NLS-1$

	/**
	 * The 'FaultMetaDataToFault Feiltype to FaultType rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String FAULTMETADATATOFAULT_FEILTYPE_TO_FAULT_TYPE_RULE = FAULTMETADATATOFAULT_TRANSFORM
      + "_FeiltypeToFaultType_Rule";//$NON-NLS-1$

	/**
   * The 'FaultMetaDataToFault EksternReferanse to ProducerFaultRef rule' id
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String FAULTMETADATATOFAULT_EKSTERN_REFERANSE_TO_PRODUCER_FAULT_REF_RULE = FAULTMETADATATOFAULT_TRANSFORM
      + "_EksternReferanseToProducerFaultRef_Rule";//$NON-NLS-1$

	/**
   * The 'FaultMetaDataToFault Class_FaultMetaData_complexType To TypeRef
   * Using ComplexTypeToComplexType Extractor' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String FAULTMETADATATOFAULT_CLASS_FAULT_META_DATA_COMPLEX_TYPE_TO_TYPE_REF_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR = FAULTMETADATATOFAULT_TRANSFORM
      + "_Class_FaultMetaData_complexTypeToTypeRef_UsingComplexTypeToComplexType_Extractor";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public FaultMetaDataToFaultTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(FAULTMETADATATOFAULT_TRANSFORM,
        Uml2servicemodelMessages.FaultMetaDataToFault_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public FaultMetaDataToFaultTransform(String id, String name,
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
    add(getFeilIdToId_Rule());
    add(getFeiltypeToFaultType_Rule());
    add(getEksternReferanseToProducerFaultRef_Rule());
    add(getClass_FaultMetaData_complexTypeToTypeRef_UsingComplexTypeToComplexType_Extractor(registry));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new InstanceOfCondition(UMLPackage.Literals.CLASS)
        .AND(new StereotypeCondition(new String[] {
            "NAV UML Profile::FaultMetaData",
            "XSDProfile::complexType" }));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        FAULTMETADATATOFAULT_CREATE_RULE,
        Uml2servicemodelMessages.FaultMetaDataToFault_Transform_Create_Rule,
        this, referenceAdapter, ServiceMetamodelPackage.Literals.FAULT);
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getFeilIdToId_Rule() {
    MoveRule rule = new MoveRule(
        FAULTMETADATATOFAULT_FEIL_ID_TO_ID_RULE,
        Uml2servicemodelMessages.FaultMetaDataToFault_Transform_FeilIdToId_Rule,
        new StereotypeFeatureAdapter(
            "NAV UML Profile::FaultMetaData::feilId"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.FAULT__ID));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getFeiltypeToFaultType_Rule() {
    MoveRule rule = new MoveRule(
        FAULTMETADATATOFAULT_FEILTYPE_TO_FAULT_TYPE_RULE,
        Uml2servicemodelMessages.FaultMetaDataToFault_Transform_FeiltypeToFaultType_Rule,
        new StereotypeFeatureAdapter(
            "NAV UML Profile::FaultMetaData::feiltype"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.FAULT__FAULT_TYPE));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getEksternReferanseToProducerFaultRef_Rule() {
    MoveRule rule = new MoveRule(
        FAULTMETADATATOFAULT_EKSTERN_REFERANSE_TO_PRODUCER_FAULT_REF_RULE,
        Uml2servicemodelMessages.FaultMetaDataToFault_Transform_EksternReferanseToProducerFaultRef_Rule,
        new StereotypeFeatureAdapter(
            "NAV UML Profile::FaultMetaData::eksternReferanse"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.FAULT__PRODUCER_FAULT_REF));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractContentExtractor getClass_FaultMetaData_complexTypeToTypeRef_UsingComplexTypeToComplexType_Extractor(
			Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        FAULTMETADATATOFAULT_CLASS_FAULT_META_DATA_COMPLEX_TYPE_TO_TYPE_REF_USING_COMPLEXTYPETOCOMPLEXTYPE_EXTRACTOR,
        Uml2servicemodelMessages.FaultMetaDataToFault_Transform_Class_FaultMetaData_complexTypeToTypeRef_UsingComplexTypeToComplexType_Extractor,
        registry.get(
            ComplexTypeToComplexTypeTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.FAULT__TYPE_REF)),
        new DirectFeatureAdapter(null));
    return extractor;
  }

}
