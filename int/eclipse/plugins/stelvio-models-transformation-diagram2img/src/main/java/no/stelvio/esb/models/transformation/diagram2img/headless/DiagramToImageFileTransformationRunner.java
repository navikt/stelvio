package no.stelvio.esb.models.transformation.diagram2img.headless;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class DiagramToImageFileTransformationRunner implements IApplication
{
	private Logger logger = Logger.getLogger(DiagramToImageFileTransformationRunner.class);
	
	// Argument constants
	private final String ARGUMENT_INPUT_DIR_PATH = "-inputDirPath";
	private final String ARGUMENT_OUTPUT_DIR_PATH = "-outputDirPath";
	
	private final String LOG_DIR_NAME = "logs";
	private final String SITE_DIR_NAME = "site";
	
	// Argument values
	private File inputDirectory = null;
	private File outputDirectory = null;
	
	@Override
	public Object start(IApplicationContext context) throws Exception 
	{	
		// 0 a). read input arguments for input and output directory paths
		readArguments(context);
		
		// 0 b). validate that input directory exists and is directory
		validateArguments();
		IPath outputDirForSite = getOutputDirForSite();
		
		// 0 c). Configre the logger
		File logFile = getLogFile();
		configureLog(logFile);
		
		logger.debug("**************** Starting diagram-to-image transformation ****************");
		// 1. get uml models from input directory
		logger.debug("1a. Searching after UML models in the input directory: " + inputDirectory.getAbsolutePath());
		List<File> inputModelFiles = listFilesRecursive(inputDirectory, new UmlModelFilter());
		logger.debug("1b. Found following UML models: " + inputModelFiles);
		
		// 2. call the servicemodel-to-html transformation
		logger.debug("2. Rendering UML diagrams to image files");
		DiagramToImageFileTransformation diagramToImageFileRunner = new DiagramToImageFileTransformation();
		diagramToImageFileRunner.run(inputModelFiles, outputDirForSite.toFile());
		
		logger.debug("**************** Finished uml-to-html transformation ****************");

		return IApplication.EXIT_OK;
	}
	
	public void configureLog(File logFile) throws IOException
	{
		BasicConfigurator.configure();

		PatternLayout layout = new PatternLayout("%p %t %c - %m%n");
		
		RollingFileAppender fileAppender = new RollingFileAppender(layout, logFile.getAbsolutePath());
		fileAppender.setLayout(layout);
		
		BasicConfigurator.configure(fileAppender);
	}
	
	public File getLogFile()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		String currentDateTime = sdf.format(cal.getTime());
		String fileName = "result_" + currentDateTime;
		
		IPath filePath = getOutputDirForLogs();
		filePath = filePath.append(fileName);
		filePath = filePath.addFileExtension("log");

		return filePath.toFile();
	}
	
	public IPath getOutputDirForLogs()
	{
		IPath outputLogsDirPath = new Path(outputDirectory.getAbsolutePath());
		outputLogsDirPath = outputLogsDirPath.append(LOG_DIR_NAME); 
		
		return outputLogsDirPath;
	}
	
	public IPath getOutputDirForSite()
	{
		IPath outputSiteDirPath = new Path(outputDirectory.getAbsolutePath());
		outputSiteDirPath = outputSiteDirPath.append(SITE_DIR_NAME); 
		
		return outputSiteDirPath;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	public void readArguments(IApplicationContext context)
	{
		Map arguments = context.getArguments();
		String[] arg = (String[])arguments.get("application.args");
		
		String inputDirPath = null;
		String outputFilePath = null;
		try
		{
			for (int i = 0; i < arg.length; i++) 
			{
				if(arg[i].equals(ARGUMENT_INPUT_DIR_PATH))
				{
					inputDirPath = arg[i+1];
				}
				else if(arg[i].equals(ARGUMENT_OUTPUT_DIR_PATH))
				{
					outputFilePath = arg[i+1];
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new IllegalArgumentException("Missing input and output arguments");
		}
		
		inputDirectory = new File(inputDirPath);
		outputDirectory = new File(outputFilePath);
	}
	

	private void validateArguments() 
	{
    	if(inputDirectory == null)
    		throw new IllegalArgumentException("The input directory is null");
    	else if(!inputDirectory.exists() || !inputDirectory.isDirectory())
    		throw new IllegalArgumentException("The input directory " + inputDirectory.getAbsolutePath() + "  is either not existing or is not a directory.");
	}
    
	/**
	 * Recursive variant of regular listFiles()-method
	 * 
	 * @param dirToTraverse
	 * @param filter
	 * @return files matching filter in all subdirectories of dirToTraverse
	 */
	private List<File> listFilesRecursive(File dirToTraverse, FilenameFilter filter) {
		List<File> foundFiles = new ArrayList<File>();
		for (File f : dirToTraverse.listFiles(filter)) 
		{
			foundFiles.add(f);
		}
		File[] subdirs = dirToTraverse.listFiles(new FileFilter() 
		{
			public boolean accept(File pathname) 
			{
				return pathname.isDirectory() && !pathname.isHidden();
			}
		});
		for (File subdir : subdirs) {
			foundFiles.addAll(listFilesRecursive(subdir, filter));
		}
		return foundFiles;
	}

}

