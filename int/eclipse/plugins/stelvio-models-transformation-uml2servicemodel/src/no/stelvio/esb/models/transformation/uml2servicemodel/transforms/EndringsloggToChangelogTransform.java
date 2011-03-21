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
 * An implementation of the 'EndringsloggToChangelogTransform' from the mapping.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class EndringsloggToChangelogTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String ENDRINGSLOGGTOCHANGELOG_TRANSFORM = "EndringsloggToChangelog_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String ENDRINGSLOGGTOCHANGELOG_CREATE_RULE = ENDRINGSLOGGTOCHANGELOG_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
	 * The 'EndringsloggToChangelog Versjon to Version rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String ENDRINGSLOGGTOCHANGELOG_VERSJON_TO_VERSION_RULE = ENDRINGSLOGGTOCHANGELOG_TRANSFORM
      + "_VersjonToVersion_Rule";//$NON-NLS-1$

	/**
   * The 'EndringsloggToChangelog Dato to Date rule' id <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String ENDRINGSLOGGTOCHANGELOG_DATO_TO_DATE_RULE = ENDRINGSLOGGTOCHANGELOG_TRANSFORM
      + "_DatoToDate_Rule";//$NON-NLS-1$

	/**
	 * The 'EndringsloggToChangelog Endring to Description rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String ENDRINGSLOGGTOCHANGELOG_ENDRING_TO_DESCRIPTION_RULE = ENDRINGSLOGGTOCHANGELOG_TRANSFORM
      + "_EndringToDescription_Rule";//$NON-NLS-1$

	/**
	 * The 'EndringsloggToChangelog Produsent to Editor rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String ENDRINGSLOGGTOCHANGELOG_PRODUSENT_TO_EDITOR_RULE = ENDRINGSLOGGTOCHANGELOG_TRANSFORM
      + "_ProdusentToEditor_Rule";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public EndringsloggToChangelogTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(ENDRINGSLOGGTOCHANGELOG_TRANSFORM,
        Uml2servicemodelMessages.EndringsloggToChangelog_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public EndringsloggToChangelogTransform(String id, String name,
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
    add(getVersjonToVersion_Rule());
    add(getDatoToDate_Rule());
    add(getEndringToDescription_Rule());
    add(getProdusentToEditor_Rule());
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new ProfileClassCondition("NAV UML Profile::Endringslogg");
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        ENDRINGSLOGGTOCHANGELOG_CREATE_RULE,
        Uml2servicemodelMessages.EndringsloggToChangelog_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.CHANGELOG);
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getVersjonToVersion_Rule() {
    MoveRule rule = new MoveRule(
        ENDRINGSLOGGTOCHANGELOG_VERSJON_TO_VERSION_RULE,
        Uml2servicemodelMessages.EndringsloggToChangelog_Transform_VersjonToVersion_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Endringslogg::versjon"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.CHANGELOG__VERSION));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getDatoToDate_Rule() {
    MoveRule rule = new MoveRule(
        ENDRINGSLOGGTOCHANGELOG_DATO_TO_DATE_RULE,
        Uml2servicemodelMessages.EndringsloggToChangelog_Transform_DatoToDate_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Endringslogg::dato"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.CHANGELOG__DATE));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getEndringToDescription_Rule() {
    MoveRule rule = new MoveRule(
        ENDRINGSLOGGTOCHANGELOG_ENDRING_TO_DESCRIPTION_RULE,
        Uml2servicemodelMessages.EndringsloggToChangelog_Transform_EndringToDescription_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Endringslogg::endring"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.CHANGELOG__DESCRIPTION));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getProdusentToEditor_Rule() {
    MoveRule rule = new MoveRule(
        ENDRINGSLOGGTOCHANGELOG_PRODUSENT_TO_EDITOR_RULE,
        Uml2servicemodelMessages.EndringsloggToChangelog_Transform_ProdusentToEditor_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Endringslogg::produsent"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.CHANGELOG__EDITOR));
    return rule;
  }

}
