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

import com.ibm.xtools.transform.authoring.uml2.StereotypeCondition;
import com.ibm.xtools.transform.authoring.uml2.StereotypeFeatureAdapter;

import com.ibm.xtools.transform.core.AbstractRule;
import com.ibm.xtools.transform.core.TransformException;

import no.stelvio.esb.models.service.metamodel.Attachment;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelPackage;

import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.query.conditions.Condition;

import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An implementation of the 'UrlToAttachmentTransform' from the mapping. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class UrlToAttachmentTransform extends MapTransform {

	/**
   * The transform id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String URLTOATTACHMENT_TRANSFORM = "UrlToAttachment_Transform";//$NON-NLS-1$

	/**
   * The create rule id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String URLTOATTACHMENT_CREATE_RULE = URLTOATTACHMENT_TRANSFORM
      + "_Create_Rule";//$NON-NLS-1$

	/**
   * The 'UrlToAttachment DisplayName to Name rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String URLTOATTACHMENT_DISPLAY_NAME_TO_NAME_RULE = URLTOATTACHMENT_TRANSFORM
      + "_DisplayNameToName_Rule";//$NON-NLS-1$

	/**
   * The 'UrlToAttachment Type to Type rule' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String URLTOATTACHMENT_TYPE_TO_TYPE_RULE = URLTOATTACHMENT_TRANSFORM
      + "_TypeToType_Rule";//$NON-NLS-1$

	/**
   * The 'UrlToAttachment Body to FilePath rule' id <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String URLTOATTACHMENT_BODY_TO_FILE_PATH_RULE = URLTOATTACHMENT_TRANSFORM
      + "_BodyToFilePath_Rule";//$NON-NLS-1$

	/**
   * The 'UrlToAttachment to UUID rule' id <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * 
   * @generated
   */
	public static final String URLTOATTACHMENT__TO_UUID_RULE = URLTOATTACHMENT_TRANSFORM
      + "_ToUUID_Rule";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public UrlToAttachmentTransform(Registry registry,
			FeatureAdapter referenceAdapter) {
    this(URLTOATTACHMENT_TRANSFORM,
        Uml2servicemodelMessages.UrlToAttachment_Transform, registry,
        referenceAdapter);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public UrlToAttachmentTransform(String id, String name, Registry registry,
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
    add(getDisplayNameToName_Rule());
    add(getTypeToType_Rule());
    add(getBodyToFilePath_Rule());
    add(getToUUID_Rule());
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected Condition getAccept_Condition() {
    return new InstanceOfCondition(UMLPackage.Literals.COMMENT)
        .AND(new StereotypeCondition(new String[] { "Default::URL" }));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected CreateRule getCreate_Rule(FeatureAdapter referenceAdapter) {
    CreateRule rule = new CreateRule(URLTOATTACHMENT_CREATE_RULE,
        Uml2servicemodelMessages.UrlToAttachment_Transform_Create_Rule,
        this, referenceAdapter,
        ServiceMetamodelPackage.Literals.ATTACHMENT);
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getDisplayNameToName_Rule() {
    MoveRule rule = new MoveRule(
        URLTOATTACHMENT_DISPLAY_NAME_TO_NAME_RULE,
        Uml2servicemodelMessages.UrlToAttachment_Transform_DisplayNameToName_Rule,
        new StereotypeFeatureAdapter("Default::URL::displayName"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.ATTACHMENT__NAME));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getTypeToType_Rule() {
    MoveRule rule = new MoveRule(
        URLTOATTACHMENT_TYPE_TO_TYPE_RULE,
        Uml2servicemodelMessages.UrlToAttachment_Transform_TypeToType_Rule,
        new StereotypeFeatureAdapter("Default::URL::type"),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.ATTACHMENT__TYPE));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getBodyToFilePath_Rule() {
    MoveRule rule = new MoveRule(
        URLTOATTACHMENT_BODY_TO_FILE_PATH_RULE,
        Uml2servicemodelMessages.UrlToAttachment_Transform_BodyToFilePath_Rule,
        new DirectFeatureAdapter(UMLPackage.Literals.COMMENT__BODY),
        new DirectFeatureAdapter(
            ServiceMetamodelPackage.Literals.ATTACHMENT__FILE_PATH));
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected AbstractRule getToUUID_Rule() {
    CustomRule rule = new CustomRule(URLTOATTACHMENT__TO_UUID_RULE,
        Uml2servicemodelMessages.UrlToAttachment_Transform_ToUUID_Rule,
        new RuleExtension() {
          public void execute(EObject source, EObject target) {
            executeToUUID_Rule((Comment) source,
                (Attachment) target);
          }
        });
    return rule;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected void executeToUUID_Rule(Comment Comment_src,
			Attachment Attachment_tgt) {
    try {
      String xmiURI = Comment_src.eResource().getURIFragment(Comment_src);
      Attachment_tgt.setUUID(xmiURI);
    } catch (Exception e) {
      String message = MessageFormat
          .format(Uml2servicemodelMessages.exception_rule_custom,
              new String[] {
                  Uml2servicemodelMessages.UrlToAttachment_Transform,
                  Uml2servicemodelMessages.UrlToAttachment_Transform_ToUUID_Rule,
                  Comment_src == null ? null : Comment_src
                      .toString(),
                  Attachment_tgt == null ? null
                      : Attachment_tgt.getName() });
      throw new TransformException(message, e, null);
    }
  }

}
