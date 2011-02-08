/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel;


import com.ibm.xtools.transform.authoring.RootTransformation;
import com.ibm.xtools.transform.authoring.TransformationProvider;

import com.ibm.xtools.transform.core.AbstractTransform;
import com.ibm.xtools.transform.core.ITransformContext;
import com.ibm.xtools.transform.core.ITransformationDescriptor;

import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.MainTransform;

import org.eclipse.core.runtime.IStatus;


/**
 * An implementation of a transformation provider for the '
 * <em><b>Uml2servicemodelTransformation</b></em>'. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class Uml2servicemodelTransformationProvider extends
		TransformationProvider {

	/**
   * The transformation id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TRANSFORMATION = "no.stelvio.esb.models.transformation.uml2servicemodel.Uml2servicemodelTransformation";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see com.ibm.xtools.transform.core.AbstractTransformationProvider#createTransformation(com.ibm.xtools.transform.core.ITransformationDescriptor)
   * @generated
   */
	public AbstractTransform createTransformation(
			ITransformationDescriptor descriptor) {
    RootTransformation transform = null;
    if (TRANSFORMATION.equals(descriptor.getId())) {
      transform = createRootTransformation(descriptor);
    }
    return transform;
  }

	/**
   * Creates a root transformation. You may add more rules to the
   * transformation here <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @param transform
   *            The root transformation
   * @generated
   */
	protected RootTransformation createRootTransformation(
			ITransformationDescriptor descriptor) {
    return new RootTransformation(descriptor, new MainTransform());
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see com.ibm.xtools.transform.core.AbstractTransformationProvider#validateContext(com.ibm.xtools.transform.core.ITransformationDescriptor,
   *      com.ibm.xtools.transform.core.ITransformContext)
   * @generated
   */
	public IStatus validateContext(ITransformationDescriptor descriptor,
			ITransformContext context) {
    if (TRANSFORMATION.equals(descriptor.getId()))
      return Uml2servicemodelTransformationValidator.INSTANCE
          .isValid(context);
    return null;
  }

}
