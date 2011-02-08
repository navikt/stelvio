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
import com.ibm.xtools.transform.authoring.MapTransform;
import com.ibm.xtools.transform.authoring.MoveRule;
import com.ibm.xtools.transform.authoring.Registry;
import com.ibm.xtools.transform.authoring.RuleExtension;
import com.ibm.xtools.transform.authoring.SubmapExtractor;

import com.ibm.xtools.transform.authoring.uml2.ProfileClassCondition;
import com.ibm.xtools.transform.authoring.uml2.ProfileClassFeatureAdapter;

import com.ibm.xtools.transform.core.AbstractContentExtractor;
import com.ibm.xtools.transform.core.AbstractRule;
import com.ibm.xtools.transform.core.TransformException;

import no.stelvio.esb.models.service.metamodel.OperationMetadata;
import no.stelvio.esb.models.service.metamodel.ServiceCatagory;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.TjenestekategoriToServiceCategoryTransform;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.query.conditions.Condition;

/**
 * An implementation of the 'TjenestemetadataToOperationMetadataTransform' from
 * the mapping. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class TjenestemetadataToOperationMetadataTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM = "TjenestemetadataToOperationMetadata_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_CREATE_RULE = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
	 * The 'TjenestemetadataToOperationMetadata Tjenesteid to Id rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_TJENESTEID_TO_ID_RULE = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_TjenesteidToId_Rule";//$NON-NLS-1$

	/**
   * The 'TjenestemetadataToOperationMetadata Transaksjonshandtering to
   * Transactions rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_TRANSAKSJONSHANDTERING_TO_TRANSACTIONS_RULE = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_TransaksjonshandteringToTransactions_Rule";//$NON-NLS-1$

	/**
   * The 'TjenestemetadataToOperationMetadata ProdusentTjenesteRef to
   * ProducerServiceRef rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_PRODUSENT_TJENESTE_REF_TO_PRODUCER_SERVICE_REF_RULE = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_ProdusentTjenesteRefToProducerServiceRef_Rule";//$NON-NLS-1$

	/**
   * The 'TjenestemetadataToOperationMetadata Tilstandsendring to
   * StateManagement rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_TILSTANDSENDRING_TO_STATE_MANAGEMENT_RULE = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_TilstandsendringToStateManagement_Rule";//$NON-NLS-1$

	/**
   * The 'TjenestemetadataToOperationMetadata Sikkerhet to Security rule' id
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_SIKKERHET_TO_SECURITY_RULE = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_SikkerhetToSecurity_Rule";//$NON-NLS-1$

	/**
   * The 'TjenestemetadataToOperationMetadata ErBruktIBatch to IsUsedByBatch
   * rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_ER_BRUKT_IBATCH_TO_IS_USED_BY_BATCH_RULE = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_ErBruktIBatchToIsUsedByBatch_Rule";//$NON-NLS-1$

	/**
   * The 'TjenestemetadataToOperationMetadata ProdusentKomponent to
   * ProducerComponent rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_PRODUSENT_KOMPONENT_TO_PRODUCER_COMPONENT_RULE = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_ProdusentKomponentToProducerComponent_Rule";//$NON-NLS-1$

	/**
	 * The 'TjenestemetadataToOperationMetadata Scope to Scope rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_SCOPE_TO_SCOPE_RULE = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_ScopeToScope_Rule";//$NON-NLS-1$

	/**
   * The 'TjenestemetadataToOperationMetadata Tjenestekategori To
   * ServiceCategory Using TjenestekategoriToServiceCategory Extractor' id
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_TJENESTEKATEGORI_TO_SERVICE_CATEGORY_USING_TJENESTEKATEGORITOSERVICECATEGORY_EXTRACTOR = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_TjenestekategoriToServiceCategory_UsingTjenestekategoriToServiceCategory_Extractor";//$NON-NLS-1$

	/**
   * The 'TjenestemetadataToOperationMetadata Feilhåndtering to ErrorHandling
   * rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEMETADATATOOPERATIONMETADATA_FEILHÅNDTERING_TO_ERROR_HANDLING_RULE = TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM
      + "_FeilhåndteringToErrorHandling_Rule";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public TjenestemetadataToOperationMetadataTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(
        TJENESTEMETADATATOOPERATIONMETADATA_TRANSFORM,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public TjenestemetadataToOperationMetadataTransform(String id, String name,
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
    add(getTjenesteidToId_Rule());
    add(getTransaksjonshandteringToTransactions_Rule());
    add(getProdusentTjenesteRefToProducerServiceRef_Rule());
    add(getTilstandsendringToStateManagement_Rule());
    add(getSikkerhetToSecurity_Rule());
    add(getErBruktIBatchToIsUsedByBatch_Rule());
    add(getProdusentKomponentToProducerComponent_Rule());
    add(getScopeToScope_Rule());
    add(getTjenestekategoriToServiceCategory_UsingTjenestekategoriToServiceCategory_Extractor(registry));
    add(getFeilhåndteringToErrorHandling_Rule());
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new ProfileClassCondition("NAV UML Profile::Tjenestemetadata");
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        TJENESTEMETADATATOOPERATIONMETADATA_CREATE_RULE,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.OPERATION_METADATA);
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getTjenesteidToId_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEMETADATATOOPERATIONMETADATA_TJENESTEID_TO_ID_RULE,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_TjenesteidToId_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestemetadata::tjenesteid"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__ID));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getTransaksjonshandteringToTransactions_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEMETADATATOOPERATIONMETADATA_TRANSAKSJONSHANDTERING_TO_TRANSACTIONS_RULE,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_TransaksjonshandteringToTransactions_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestemetadata::transaksjonshandtering"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__TRANSACTIONS));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getProdusentTjenesteRefToProducerServiceRef_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEMETADATATOOPERATIONMETADATA_PRODUSENT_TJENESTE_REF_TO_PRODUCER_SERVICE_REF_RULE,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_ProdusentTjenesteRefToProducerServiceRef_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestemetadata::produsentTjenesteRef"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__PRODUCER_SERVICE_REF));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getTilstandsendringToStateManagement_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEMETADATATOOPERATIONMETADATA_TILSTANDSENDRING_TO_STATE_MANAGEMENT_RULE,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_TilstandsendringToStateManagement_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestemetadata::tilstandsendring"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__STATE_MANAGEMENT));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getSikkerhetToSecurity_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEMETADATATOOPERATIONMETADATA_SIKKERHET_TO_SECURITY_RULE,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_SikkerhetToSecurity_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestemetadata::sikkerhet"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__SECURITY));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getErBruktIBatchToIsUsedByBatch_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEMETADATATOOPERATIONMETADATA_ER_BRUKT_IBATCH_TO_IS_USED_BY_BATCH_RULE,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_ErBruktIBatchToIsUsedByBatch_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestemetadata::erBruktIBatch"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__IS_USED_BY_BATCH));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getProdusentKomponentToProducerComponent_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEMETADATATOOPERATIONMETADATA_PRODUSENT_KOMPONENT_TO_PRODUCER_COMPONENT_RULE,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_ProdusentKomponentToProducerComponent_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestemetadata::produsentKomponent"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__PRODUCER_COMPONENT));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getScopeToScope_Rule() {
    CustomRule rule = new CustomRule(
        TJENESTEMETADATATOOPERATIONMETADATA_SCOPE_TO_SCOPE_RULE,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_ScopeToScope_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeScopeToScope_Rule((EObject) source,
                (OperationMetadata) target);
          }
        });
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected void executeScopeToScope_Rule(EObject Tjenestemetadata_src,
			OperationMetadata OperationMetadata_tgt) {
    try {
      if (Tjenestemetadata_src != null) {
        org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute> tjenestemetadataAttributes = Tjenestemetadata_src
            .eClass().getEAttributes();
        for (org.eclipse.emf.ecore.EAttribute eAttribute : tjenestemetadataAttributes) {
          if (eAttribute.getName().equals("scope")) {
            org.eclipse.emf.ecore.impl.EEnumLiteralImpl o = (org.eclipse.emf.ecore.impl.EEnumLiteralImpl) Tjenestemetadata_src
                .eGet(eAttribute);
            String scopeNavn = o.getName();

            no.stelvio.esb.models.service.metamodel.Scope scope = no.stelvio.esb.models.service.metamodel.Scope
                .get(scopeNavn);
            OperationMetadata_tgt.setScope(scope);
          }
        }
      }
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform,
                  Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_ScopeToScope_Rule,
                  Tjenestemetadata_src == null ? null
                      : Tjenestemetadata_src.toString(),
                  OperationMetadata_tgt == null ? null
                      : OperationMetadata_tgt.toString() });
      throw new TransformException(message, e, null);
    }
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractContentExtractor getTjenestekategoriToServiceCategory_UsingTjenestekategoriToServiceCategory_Extractor(
			Registry registry) {
    SubmapExtractor extractor = new SubmapExtractor(
        TJENESTEMETADATATOOPERATIONMETADATA_TJENESTEKATEGORI_TO_SERVICE_CATEGORY_USING_TJENESTEKATEGORITOSERVICECATEGORY_EXTRACTOR,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_TjenestekategoriToServiceCategory_UsingTjenestekategoriToServiceCategory_Extractor,
        registry.get(
            TjenestekategoriToServiceCategoryTransform.class,
            new DirectFeatureAdapter(
                ServiceMetamodelPackage.Literals.OPERATION_METADATA__SERVICE_CATEGORY)),
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestemetadata::tjenestekategori"));
    return extractor;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getFeilhåndteringToErrorHandling_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEMETADATATOOPERATIONMETADATA_FEILHÅNDTERING_TO_ERROR_HANDLING_RULE,
        Uml2servicemodelMessages.TjenestemetadataToOperationMetadata_Transform_FeilhåndteringToErrorHandling_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestemetadata::feilhåndtering"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.OPERATION_METADATA__ERROR_HANDLING));
    return rule;
  }

}
