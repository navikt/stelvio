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
import com.ibm.xtools.transform.authoring.MoveRule;
import com.ibm.xtools.transform.authoring.Registry;
import com.ibm.xtools.transform.authoring.SubmapExtractor;

import com.ibm.xtools.transform.authoring.uml2.StereotypeCondition;
import com.ibm.xtools.transform.authoring.uml2.StereotypeFeatureAdapter;

import com.ibm.xtools.transform.core.AbstractContentExtractor;
import com.ibm.xtools.transform.core.AbstractRule;

import no.stelvio.esb.models.service.metamodel.OperationMetadata;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.NokkelindikatorToOperationMetadataTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.TjenestemetadataToOperationMetadataTransform;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'OperationToOperationMetadataTransform' from the
 * mapping. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class OperationToOperationMetadataTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOOPERATIONMETADATA_TRANSFORM = "OperationToOperationMetadata_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String OPERATIONTOOPERATIONMETADATA_CREATE_RULE = OPERATIONTOOPERATIONMETADATA_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
	 * The 'OperationToOperationMetadata Tjenestemetadata To OperationMetadata
	 * Using TjenestemetadataToOperationMetadata Extractor' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String OPERATIONTOOPERATIONMETADATA_TJENESTEMETADATA_TO_OPERATION_METADATA_USING_TJENESTEMETADATATOOPERATIONMETADATA_EXTRACTOR = OPERATIONTOOPERATIONMETADATA_TRANSFORM
      + "_TjenestemetadataToOperationMetadata_UsingTjenestemetadataToOperationMetadata_Extractor";//$NON-NLS-1$

	/**
   * The 'OperationToOperationMetadata OwnedComment$Body to ProceessingRules
   * rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public static final String OPERATIONTOOPERATIONMETADATA_OWNED_COMMENT$BODY_TO_PROCEESSING_RULES_RULE = OPERATIONTOOPERATIONMETADATA_TRANSFORM
      + "_OwnedComment$BodyToProceessingRules_Rule";//$NON-NLS-1$

  /**
	 * The 'OperationToOperationMetadata Nokkelindikator To OperationMetadata
	 * Using NokkelindikatorToOperationMetadata Extractor' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String OPERATIONTOOPERATIONMETADATA_NOKKELINDIKATOR_TO_OPERATION_METADATA_USING_NOKKELINDIKATORTOOPERATIONMETADATA_EXTRACTOR = OPERATIONTOOPERATIONMETADATA_TRANSFORM
      + "_NokkelindikatorToOperationMetadata_UsingNokkelindikatorToOperationMetadata_Extractor";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public OperationToOperationMetadataTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(
        OPERATIONTOOPERATIONMETADATA_TRANSFORM,
        Uml2servicemodelMessages.OperationToOperationMetadata_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public OperationToOperationMetadataTransform(String id, String name,
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
    add(getTjenestemetadataToOperationMetadata_UsingTjenestemetadataToOperationMetadata_Extractor(registry));
    add(getOwnedComment$BodyToProceessingRules_Rule());
    add(getNokkelindikatorToOperationMetadata_UsingNokkelindikatorToOperationMetadata_Extractor(registry));
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
        OPERATIONTOOPERATIONMETADATA_CREATE_RULE,
        Uml2servicemodelMessages.OperationToOperationMetadata_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.OPERATION_METADATA);
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getTjenestemetadataToOperationMetadata_UsingTjenestemetadataToOperationMetadata_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        OPERATIONTOOPERATIONMETADATA_TJENESTEMETADATA_TO_OPERATION_METADATA_USING_TJENESTEMETADATATOOPERATIONMETADATA_EXTRACTOR,
        Uml2servicemodelMessages.OperationToOperationMetadata_Transform_TjenestemetadataToOperationMetadata_UsingTjenestemetadataToOperationMetadata_Extractor,
        registry.get(
            TjenestemetadataToOperationMetadataTransform.class,
            new CustomDirectFeatureAdapter(

            (EStructuralFeature) null)),
        new StereotypeFeatureAdapter(
            "NAV UML Profile::Tjeneste::tjenestemetadata"));
    return extractor;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractRule getOwnedComment$BodyToProceessingRules_Rule() {
    MoveRule rule = new MoveRule(
        OPERATIONTOOPERATIONMETADATA_OWNED_COMMENT$BODY_TO_PROCEESSING_RULES_RULE,
        Uml2servicemodelMessages.OperationToOperationMetadata_Transform_OwnedComment$BodyToProceessingRules_Rule,
        new DirectFeatureAdapter(
            UMLPackage.Literals.ELEMENT__OWNED_COMMENT, "[-1]/body"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__PROCEESSING_RULES));
    return rule;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AbstractContentExtractor getNokkelindikatorToOperationMetadata_UsingNokkelindikatorToOperationMetadata_Extractor(
      Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        OPERATIONTOOPERATIONMETADATA_NOKKELINDIKATOR_TO_OPERATION_METADATA_USING_NOKKELINDIKATORTOOPERATIONMETADATA_EXTRACTOR,
        Uml2servicemodelMessages.OperationToOperationMetadata_Transform_NokkelindikatorToOperationMetadata_UsingNokkelindikatorToOperationMetadata_Extractor,
        registry.get(NokkelindikatorToOperationMetadataTransform.class,
            new CustomDirectFeatureAdapter(

            (EStructuralFeature) null)),
        new StereotypeFeatureAdapter(
            "NAV UML Profile::Tjeneste::nokkelindikator"));
    return extractor;
  }

}
