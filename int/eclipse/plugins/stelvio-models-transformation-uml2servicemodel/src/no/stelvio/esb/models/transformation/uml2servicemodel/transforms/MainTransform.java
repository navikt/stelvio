/**
 * <copyright>
 * </copyright>
 */
package no.stelvio.esb.models.transformation.uml2servicemodel.transforms;


import no.stelvio.esb.models.transformation.uml2servicemodel.l10n.Uml2servicemodelMessages;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ComplexTypeToComplexTypeTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.DiagramToDiagramTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.EnumerationToComplexTypeTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.FaultMetaDataToFaultTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.InterfaceToServiceInterfaceTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.NokkelindikatorToOperationMetadataTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.OperationToOperationMetadataTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.OperationToServiceOperationTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.PackageToServicePackageTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ParameterToComplexTypeTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ParameterToFaultTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.ParameterToMessageTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.PropertyToAttributeTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.TjenestekategoriToServiceCategoryTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.TjenestemetadataToOperationMetadataTransform;
import no.stelvio.esb.models.transformation.uml2servicemodel.transforms.UrlToAttachmentTransform;

/**
 * A main transform that acts as a switch on the other transforms of the
 * transformation <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class MainTransform extends
		com.ibm.xtools.transform.authoring.MainTransform {

	/**
   * The transformation id <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public static final String TRANSFORM = "mainTransform";//$NON-NLS-1$

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
	public MainTransform() {
    super(TRANSFORM, Uml2servicemodelMessages.mainTransform);
    add(PackageToServicePackageTransform.class);
    add(ComplexTypeToComplexTypeTransform.class);
    add(EnumerationToComplexTypeTransform.class);
    add(PropertyToAttributeTransform.class);
    add(ParameterToComplexTypeTransform.class);
    add(InterfaceToServiceInterfaceTransform.class);
    add(OperationToServiceOperationTransform.class);
    add(OperationToOperationMetadataTransform.class);
    add(TjenestemetadataToOperationMetadataTransform.class);
    add(NokkelindikatorToOperationMetadataTransform.class);
    add(TjenestekategoriToServiceCategoryTransform.class);
    add(ParameterToMessageTransform.class);
    add(FaultMetaDataToFaultTransform.class);
    add(ParameterToFaultTransform.class);
    add(DiagramToDiagramTransform.class);
    add(UrlToAttachmentTransform.class);
  }

}
