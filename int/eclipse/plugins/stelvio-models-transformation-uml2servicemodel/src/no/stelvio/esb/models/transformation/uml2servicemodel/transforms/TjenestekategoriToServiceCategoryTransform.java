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
 * An implementation of the 'TjenestekategoriToServiceCategoryTransform' from
 * the mapping. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class TjenestekategoriToServiceCategoryTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEKATEGORITOSERVICECATEGORY_TRANSFORM = "TjenestekategoriToServiceCategory_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEKATEGORITOSERVICECATEGORY_CREATE_RULE = TJENESTEKATEGORITOSERVICECATEGORY_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'TjenestekategoriToServiceCategory Grupperingsnavn to GroupingName
   * rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TJENESTEKATEGORITOSERVICECATEGORY_GRUPPERINGSNAVN_TO_GROUPING_NAME_RULE = TJENESTEKATEGORITOSERVICECATEGORY_TRANSFORM
      + "_GrupperingsnavnToGroupingName_Rule";//$NON-NLS-1$

	/**
	 * The 'TjenestekategoriToServiceCategory Funksjon to Function rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String TJENESTEKATEGORITOSERVICECATEGORY_FUNKSJON_TO_FUNCTION_RULE = TJENESTEKATEGORITOSERVICECATEGORY_TRANSFORM
      + "_FunksjonToFunction_Rule";//$NON-NLS-1$

	/**
	 * The 'TjenestekategoriToServiceCategory Kategori to Name rule' id <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String TJENESTEKATEGORITOSERVICECATEGORY_KATEGORI_TO_NAME_RULE = TJENESTEKATEGORITOSERVICECATEGORY_TRANSFORM
      + "_KategoriToName_Rule";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public TjenestekategoriToServiceCategoryTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(
        TJENESTEKATEGORITOSERVICECATEGORY_TRANSFORM,
        Uml2servicemodelMessages.TjenestekategoriToServiceCategory_Transform,
        registry, referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public TjenestekategoriToServiceCategoryTransform(String id, String name,
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
    add(getGrupperingsnavnToGroupingName_Rule());
    add(getFunksjonToFunction_Rule());
    add(getKategoriToName_Rule());
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new ProfileClassCondition("NAV UML Profile::Tjenestekategori");
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(
        TJENESTEKATEGORITOSERVICECATEGORY_CREATE_RULE,
        Uml2servicemodelMessages.TjenestekategoriToServiceCategory_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.SERVICE_CATAGORY);
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getGrupperingsnavnToGroupingName_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEKATEGORITOSERVICECATEGORY_GRUPPERINGSNAVN_TO_GROUPING_NAME_RULE,
        Uml2servicemodelMessages.TjenestekategoriToServiceCategory_Transform_GrupperingsnavnToGroupingName_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestekategori::grupperingsnavn"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.SERVICE_CATAGORY__GROUPING_NAME));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getFunksjonToFunction_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEKATEGORITOSERVICECATEGORY_FUNKSJON_TO_FUNCTION_RULE,
        Uml2servicemodelMessages.TjenestekategoriToServiceCategory_Transform_FunksjonToFunction_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestekategori::funksjon"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.SERVICE_CATAGORY__FUNCTION));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getKategoriToName_Rule() {
    MoveRule rule = new MoveRule(
        TJENESTEKATEGORITOSERVICECATEGORY_KATEGORI_TO_NAME_RULE,
        Uml2servicemodelMessages.TjenestekategoriToServiceCategory_Transform_KategoriToName_Rule,
        new ProfileClassFeatureAdapter(
            "NAV UML Profile::Tjenestekategori::kategori"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.SERVICE_CATAGORY__NAME));
    return rule;
  }

}
