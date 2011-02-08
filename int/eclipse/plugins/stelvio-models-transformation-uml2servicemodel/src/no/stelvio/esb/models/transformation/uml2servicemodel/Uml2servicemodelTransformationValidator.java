/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel;


import com.ibm.xtools.transform.authoring.TransformationValidator;

/**
 * A helper class that provides validation services to the transform and its
 * provider <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class Uml2servicemodelTransformationValidator extends
		TransformationValidator {

	/**
   * The singleton instance <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static Uml2servicemodelTransformationValidator INSTANCE = new Uml2servicemodelTransformationValidator();

	/**
   * The private constructor <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	private Uml2servicemodelTransformationValidator() {
    // empty
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	protected String getPluginID() {
    return "stelvio-models-transformation-uml2servicemodel"; //$NON-NLS-1$
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see com.ibm.xtools.transform.authoring.TransformationValidator#isSourceElementValid(java.lang.Object)
   * @generated not
   */
	protected boolean isSourceElementValid(Object source) {
    // Remove @generated tag to add more source validation checks
    //return super.isSourceElementValid(source);
    return true;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see com.ibm.xtools.transform.authoring.TransformationValidator#isTargetElementValid(java.lang.Object)
   * @generated not
   */
	protected boolean isTargetElementValid(Object target) {
    // Remove @generated tag to add more target validation checks
    //return super.isTargetElementValid(target);
		return true;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see com.ibm.xtools.transform.authoring.TransformationValidator#isAuxiliarySourceURIValid(java.lang.String)
   * @generated
   */
	protected boolean isAuxiliarySourceURIValid(String auxiliarySourceURI) {
    // Remove @generated tag to add more auxiliary source validation checks
    return super.isAuxiliarySourceURIValid(auxiliarySourceURI);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see com.ibm.xtools.transform.authoring.TransformationValidator#isAuxiliaryTargetURIValid(java.lang.String)
   * @generated
   */
	protected boolean isAuxiliaryTargetURIValid(String auxiliaryTargetURI) {
    // Remove @generated tag to add more auxiliary target validation checks
    return super.isAuxiliaryTargetURIValid(auxiliaryTargetURI);
  }

}
