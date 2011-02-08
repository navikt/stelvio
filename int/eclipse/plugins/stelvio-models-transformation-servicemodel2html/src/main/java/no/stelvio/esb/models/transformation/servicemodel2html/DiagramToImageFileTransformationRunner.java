package no.stelvio.esb.models.transformation.servicemodel2html;

import java.io.File;
import java.io.IOException;
import java.util.List;

import no.stelvio.esb.models.transformation.servicemodel2html.utils.PublishFileUtils;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Element;

import com.ibm.xtools.modeler.ui.UMLModeler;
import com.ibm.xtools.uml.ui.diagram.IUMLDiagramHelper;

public class DiagramToImageFileTransformationRunner {
	private static Logger logger = Logger
			.getLogger("no.stelvio.esb.models.transformation.servicemodel2html");

	public void run(List<File> umlModelFiles, File outputDirectory) throws IOException
	{
		for (File file : umlModelFiles) 
		{
			logger.debug("Searching after UML Diagrams in model: " + file.getAbsolutePath());
			Element element = UMLModeler.openModelResource(file.getAbsolutePath());
			renderToImageFileForElement(element,outputDirectory);
		}
	}
	
	private void renderToImageFileForElement(EObject umlElement, File outputDirectory) throws IOException
	{
		EList<EObject> elementContent = umlElement.eContents();
		for (EObject content : elementContent) 
		{
			if( true == content instanceof Diagram )
			{
				renderToImageFile((Diagram)content, outputDirectory);
			}
			renderToImageFileForElement(content, outputDirectory);
		}
	}
	
	private void renderToImageFile(Diagram diagram, File outputDirectory) throws IOException
	{	
		String diagramURIId = diagram.eResource().getURIFragment(diagram);
		IUMLDiagramHelper diagramHelper = UMLModeler.getUMLDiagramHelper();
		File diagramFile = PublishFileUtils.createDiagramFile(outputDirectory, diagramURIId);
		
		logger.debug("Rendering diagram " + diagram.getName() +  " to image file " + diagramFile.getAbsolutePath());
		
		diagramHelper.renderToImageFile(diagram, diagramFile.getAbsolutePath(), getImageFileFormat());
	}
	
	private ImageFileFormat getImageFileFormat()
	{
		if(PublishFileUtils.DIAGRAM_IMAGE_FILE_FORMAT.equals("png"))
		{
			return ImageFileFormat.PNG;
		}
		else if(PublishFileUtils.DIAGRAM_IMAGE_FILE_FORMAT.equals("gif"))
		{
			return ImageFileFormat.GIF;
		}
		else if(PublishFileUtils.DIAGRAM_IMAGE_FILE_FORMAT.equals("jpeg"))
		{
			return ImageFileFormat.JPEG;
		}
		else if(PublishFileUtils.DIAGRAM_IMAGE_FILE_FORMAT.equals("jpg"))
		{
			return ImageFileFormat.JPG;
		}
		else if(PublishFileUtils.DIAGRAM_IMAGE_FILE_FORMAT.equals("bmp"))
		{
			return ImageFileFormat.BMP;
		}
		else
			return ImageFileFormat.PNG;
	}
}
