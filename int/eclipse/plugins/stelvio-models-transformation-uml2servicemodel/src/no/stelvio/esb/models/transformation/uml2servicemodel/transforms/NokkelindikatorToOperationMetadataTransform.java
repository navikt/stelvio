/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel.transforms;

import com.ibm.xtools.transform.authoring.CreateRule;
import com.ibm.xtools.transform.authoring.DirectFeatureAdapter;
import com.ibm.xtools.transform.authoring.FeatureAdapter;
import com.ibm.xtools.transform.authoring.MapTransform;
import com.ibm.xtools.transform.authoring.MoveRule;
import com.ibm.xtools.transform.authoring.Registry;

import com.ibm.xtools.transform.authoring.uml2.ProfileClassCondition;
import com.ibm.xtools.transform.authoring.uml2.ProfileClassFeatureAdapter;

import com.ibm.xtools.transform.core.AbstractRule;

import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import org.eclipse.emf.query.conditions.Condition;

/**
 * An implementation of the 'NokkelindikatorToOperationMetadataTransform' from
 * the mapping. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class NokkelindikatorToOperationMetadataTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String NOKKELINDIKATORTOOPERATIONMETADATA_TRANSFORM = "NokkelindikatorToOperationMetadata_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String NOKKELINDIKATORTOOPERATIONMETADATA_CREATE_RULE = NOKKELINDIKATORTOOPERATIONMETADATA_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'NokkelindikatorToOperationMetadata Responstid to ResponseTime rule'
   * id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String NOKKELINDIKATORTOOPERATIONMETADATA_RESPONSTID_TO_RESPONSE_TIME_RULE = NOKKELINDIKATORTOOPERATIONMETADATA_TRANSFORM
      + "_ResponstidToResponseTime_Rule";//$NON-NLS-1$

	/**
   * The 'NokkelindikatorToOperationMetadata Volumbegrensning to
   * VolumeCapacity rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String NOKKELINDIKATORTOOPERATIONMETADATA_VOLUMBEGRENSNING_TO_VOLUME_CAPACITY_RULE = NOKKELINDIKATORTOOPERATIONMETADATA_TRANSFORM
      + "_VolumbegrensningToVolumeCapacity_Rule";//$NON-NLS-1$

	/**
	 * The 'NokkelindikatorToOperationMetadata Oppetid to Uptime rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String NOKKELINDIKATORTOOPERATIONMETADATA_OPPETID_TO_UPTIME_RULE = NOKKELINDIKATORTOOPERATIONMETADATA_TRANSFORM
      + "_OppetidToUptime_Rule";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public NokkelindikatorToOperationMetadataTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(
        NOKKELINDIKATORTOOPERATIONMETADATA_TRANSFORM,
        Uml2servicemodelMessages.NokkelindikatorToOperationMetadata_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public NokkelindikatorToOperationMetadataTransform(String id, String name,
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
    add(getResponstidToResponseTime_Rule());
    add(getVolumbegrensningToVolumeCapacity_Rule());
    add(getOppetidToUptime_Rule());
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new ProfileClassCondition("NAV UML Profile::Nokkelindikator");
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        NOKKELINDIKATORTOOPERATIONMETADATA_CREATE_RULE,
        Uml2servicemodelMessages.NokkelindikatorToOperationMetadata_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.OPERATION_METADATA);
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getResponstidToResponseTime_Rule() {
    MoveRule rule = new MoveRule(
        NOKKELINDIKATORTOOPERATIONMETADATA_RESPONSTID_TO_RESPONSE_TIME_RULE,
        Uml2servicemodelMessages.NokkelindikatorToOperationMetadata_Transform_ResponstidToResponseTime_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Nokkelindikator::responstid"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__RESPONSE_TIME));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getVolumbegrensningToVolumeCapacity_Rule() {
    MoveRule rule = new MoveRule(
        NOKKELINDIKATORTOOPERATIONMETADATA_VOLUMBEGRENSNING_TO_VOLUME_CAPACITY_RULE,
        Uml2servicemodelMessages.NokkelindikatorToOperationMetadata_Transform_VolumbegrensningToVolumeCapacity_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Nokkelindikator::volumbegrensning"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__VOLUME_CAPACITY));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getOppetidToUptime_Rule() {
    MoveRule rule = new MoveRule(
        NOKKELINDIKATORTOOPERATIONMETADATA_OPPETID_TO_UPTIME_RULE,
        Uml2servicemodelMessages.NokkelindikatorToOperationMetadata_Transform_OppetidToUptime_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Nokkelindikator::oppetid"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__UPTIME));
    return rule;
  }

}
