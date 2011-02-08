/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel;


import com.ibm.xtools.transform.authoring.TransformationGUI;

import com.ibm.xtools.transform.core.ITransformationDescriptor;


/**
 * <!-- begin-user-doc --> A class that supplies the GUI of the transformation
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class Uml2servicemodelTransformationGUI extends TransformationGUI {

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see com.ibm.xtools.transform.ui.AbstractTransformGUI#showInSourceTree(com.ibm.xtools.transform.core.ITransformationDescriptor,
   *      java.lang.Object)
   * @generated
   */
	public boolean showInSourceTree(
			ITransformationDescriptor transformationDescriptor,
			Object suggestedSource) {
    return Uml2servicemodelTransformationValidator.INSTANCE
        .isSourceElementValid(suggestedSource);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see com.ibm.xtools.transform.ui.AbstractTransformGUI#showInTargetContainerTree(com.ibm.xtools.transform.core.ITransformationDescriptor,
   *      java.lang.Object)
   * @generated
   */
	public boolean showInTargetContainerTree(
			ITransformationDescriptor transformationDescriptor,
			Object suggestedTargetContainer) {
    return Uml2servicemodelTransformationValidator.INSTANCE
        .isTargetElementValid(suggestedTargetContainer);
  }
}
