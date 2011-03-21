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

import com.ibm.xtools.transform.authoring.uml2.StereotypeCondition;
import com.ibm.xtools.transform.authoring.uml2.StereotypeFeatureAdapter;

import com.ibm.xtools.transform.core.AbstractRule;

import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'PropertyMappingToAttributeTransform' from the
 * mapping. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class PropertyMappingToAttributeTransform extends MapTransform {

	/**
	 * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String PROPERTYMAPPINGTOATTRIBUTE_TRANSFORM = "PropertyMappingToAttribute_Transform";//$NON-NLS-1$

	/**
	 * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String PROPERTYMAPPINGTOATTRIBUTE_CREATE_RULE = PROPERTYMAPPINGTOATTRIBUTE_TRANSFORM
			+ "_Create_Rule";//$NON-NLS-1$

	/**
	 * The 'PropertyMappingToAttribute TilFeltNavn to MappingToAttributeName
	 * rule' id <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String PROPERTYMAPPINGTOATTRIBUTE_TIL_FELT_NAVN_TO_MAPPING_TO_ATTRIBUTE_NAME_RULE = PROPERTYMAPPINGTOATTRIBUTE_TRANSFORM
			+ "_TilFeltNavnToMappingToAttributeName_Rule";//$NON-NLS-1$

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PropertyMappingToAttributeTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
		this(PROPERTYMAPPINGTOATTRIBUTE_TRANSFORM,
				Uml2servicemodelMessages.PropertyMappingToAttribute_Transform,
				registry, referenceAdapter);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PropertyMappingToAttributeTransform(String id, String name,
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
		add(getTilFeltNavnToMappingToAttributeName_Rule());
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Condition getAccept_Condition() {
		return new InstanceOfCondition(UMLPackage.Literals.PROPERTY)
				.AND(new StereotypeCondition(
						new String[] { "NAV UML Profile::Mapping" }));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
		CreateRule rule = new CreateRule(
				PROPERTYMAPPINGTOATTRIBUTE_CREATE_RULE,
				Uml2servicemodelMessages.PropertyMappingToAttribute_Transform_Create_Rule,
				this, referenceAdapter,
				ServiceMetamodelPackage.Literals.ATTRIBUTE);
		return rule;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AbstractRule getTilFeltNavnToMappingToAttributeName_Rule() {
		MoveRule rule = new MoveRule(
				PROPERTYMAPPINGTOATTRIBUTE_TIL_FELT_NAVN_TO_MAPPING_TO_ATTRIBUTE_NAME_RULE,
				Uml2servicemodelMessages.PropertyMappingToAttribute_Transform_TilFeltNavnToMappingToAttributeName_Rule,
				new StereotypeFeatureAdapter(
						"NAV UML Profile::Mapping::tilFeltNavn"),
				new DirectFeatureAdapter(
						ServiceMetamodelPackage.Literals.ATTRIBUTE__MAPPING_TO_ATTRIBUTE_NAME));
		return rule;
	}

}
