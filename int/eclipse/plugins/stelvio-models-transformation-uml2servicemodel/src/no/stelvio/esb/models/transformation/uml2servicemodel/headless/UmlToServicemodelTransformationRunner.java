package no.stelvio.esb.models.transformation.uml2servicemodel.headless;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import no.stelvio.esb.models.transformation.uml2servicemodel.Activator;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.ibm.xtools.transform.core.ITransformContext;
import com.ibm.xtools.transform.core.TransformController;
import com.ibm.xtools.transform.core.config.ITransformConfig;
import com.ibm.xtools.transform.core.config.TransformConfigUtil;

public class UmlToServicemodelTransformationRunner
{
	private Logger logger = Logger.getLogger(UmlToServicemodelTransformationRunner.class);
	
	// Output variables
	private String outputTempModelName = "serviceModel";  //name of the temporary model containing the transformation output
	private File outputTempModelFile = null;
	
	// Input variables
	private List<File> inputUmlModels = null;
	
	// Transformation configuration file
	private String transformationConfigFilename = "src/main/resources/uml2servicemodel_config.tc";
	
	public File run(List<File> inputUmlModels) 
		throws IllegalArgumentException, TransformationValidationException, IOException
	{	
		logger.debug("**************** Starting uml-to-servicemodel transformation ****************");
		
		// 0. assign the input model list to the global variable
		logger.debug("Recieving UML input models: " + inputUmlModels);
		this.inputUmlModels = inputUmlModels;
		
		// 1. validate that all uml model files from input exists and get their uri's
		logger.debug("Validating that UML input models exists");
		validateInputUmlModels();
				
		// 2. load transformation configuratsion file
		URL fileUrl = Activator.getDefault().getBundle().getResource(transformationConfigFilename);		
		URL boundleFileUrl = FileLocator.toFileURL(fileUrl);
		
		logger.debug("Loading the transformaton configuration from file: " + boundleFileUrl.getPath());
		//Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		//URL fileURL = bundle.getEntry(transformationConfigFilename);
		ITransformConfig config = TransformConfigUtil.loadConfiguration(boundleFileUrl);

		// 3. get forward context from the transformation configuration file
		ITransformContext context = config.getForwardContext();

		// 4. source has to be set, even if not used. We are setting it to ArrayList containing one empty string
		logger.debug("Setting the source for the transformation configuration");
		setSource(context);
		
		// 5. set auxiliary properties (source, target and checks) to the context
		logger.debug("Setting the auxiliary properties for the transformation configuration");
		setAuxiliaryProperties(context);
		
		// 6. run the transformation with modified context (source and target)
		logger.debug("Validating the transformation configuration");
		validateContext(config, context);
		
		// 7. Running the transformation
		logger.debug("Executing the transformation");
		TransformController controller = TransformController.getInstance();
		IStatus status = controller.execute(config, context, false, false, null);
		logger.debug("Transformation execution went ok: " + status.isOK());
		if(!status.isOK())
			throw new RuntimeException("Transformation did not complete; " + status.getMessage());

		logger.debug("Transformation result is placed in file: " + outputTempModelFile.getAbsolutePath());
		
		logger.debug("**************** Finished uml-to-servicemodel transformation ****************");
		
		return outputTempModelFile;
	}
	
	private void validateContext(ITransformConfig config, ITransformContext context) throws TransformationValidationException
	{
		IStatus validationStatus = TransformController.getInstance().validateContext(config, context, false);
		List<String> errorMessages = new ArrayList<String>();
		
		if(!validationStatus.isOK())
		{
			for(IStatus status : validationStatus.getChildren())
				if(!status.isOK())
				{
					String errorMessage = "Transformation validation failed: " + status.getMessage() + ", " + status.getException();
					errorMessages.add(errorMessage);
				}
		}
		
		if(errorMessages.size() != 0)
			throw new TransformationValidationException(errorMessages);
	}

	private void validateInputUmlModels() throws IllegalArgumentException
	{
		if(inputUmlModels == null || inputUmlModels.size() == 0)
			throw new IllegalArgumentException("At least one UML model has to be defined as source before the transformation can be run.");
		
		for (File file : inputUmlModels) 
		{
			if(!file.exists())
				throw new IllegalArgumentException("Uml model file: " + file.getAbsolutePath() + " is not found");
		}
		
	}

	private void setSource(ITransformContext context)
	{
		ArrayList<String> sourceModelList = new ArrayList<String>();
		sourceModelList.add("");
		
		context.setPropertyValue(ITransformContext.SOURCE, sourceModelList);
	}
	
	private void setAuxiliaryProperties(ITransformContext context) throws IOException
	{
		File targetModelFile = getOrCreateOutputTempModelFile();
		String targetModelUri = targetModelFile.toURI().toString();
		
		List<String> sourceModelFileURIs = new ArrayList<String>();
		List<String> targetModelFileURIList = new ArrayList<String>();
		List<Boolean> targetCheckList = new ArrayList<Boolean>();
		
		for(File modelFile : inputUmlModels)
		{
			String sourceFileUri = modelFile.toURI().toString();
			sourceModelFileURIs.add(sourceFileUri);
			targetModelFileURIList.add(targetModelUri);
			targetCheckList.add(true);
		}

		context.setPropertyValue("AuxiliarySources", sourceModelFileURIs);
		context.setPropertyValue("AuxiliaryTargets", targetModelFileURIList);
		context.setPropertyValue("AuxiliaryTargetsChecks", targetCheckList);
	}
			
	private void createOutputTempModelFile() throws IOException 
	{
		outputTempModelFile = File.createTempFile(outputTempModelName, null);		
	}
	
	private File getOrCreateOutputTempModelFile() throws IOException
	{
		if(outputTempModelFile == null)
			createOutputTempModelFile();
		
		return outputTempModelFile;
	}
}