package no.stelvio.esb.models.transformation.servicemodel2html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import no.stelvio.esb.models.transformation.servicemodel2html.utils.PublishFileUtils;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class WebPublishFrontpage {
	
	private static Logger logger = Logger.getLogger("no.stelvio.esb.models.transformation.servicemodel2html");
	private static final IPath xsltFrontpageFilePath = new Path("src/main/resources/xslt/web-publish-frontpage.xsl");
	private static final IPath xsltFrontpageServicemodelFilePath = new Path("src/main/resources/xslt/web-publish-frontpage-servicemodel.xsl");
	private static final IPath xsltFrontpageInformationmodelFilePath = new Path("src/main/resources/xslt/web-publish-frontpage-informationmodel.xsl");
	private static final String paramOutputDirPath = "outputDirPath";
	private static final String paramCurrentFilePath = "currentFilePath";
	private static final String paramInformationmodelFilePath = "informationmodelFilePath";
	private static final String paramServicemodelcurrentFilePath = "servicemodelFilePath";
	
	protected static void run(File inputFile, File outputDirectory) throws TransformerException, IOException 
	{
		publishFrontpage(inputFile, outputDirectory);
	}
	
	private static void publishFrontpage(File inputFile, File outputDirectory) throws TransformerException, IOException
	{
		File frontpageServicemodelFile = publishFrontpageForServicemodel(inputFile, outputDirectory);
		File frontpageInformationmodelFile = publishFrontpageForInformationmodel(inputFile, outputDirectory);
		
		File frontpageFile = PublishFileUtils.createFrontpageFile(outputDirectory);
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		File xsltFile = PublishFileUtils.getBoundleFileForPath(xsltFrontpageFilePath);
		Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFile));

		transformer.setParameter(paramOutputDirPath,
				outputDirectory.getAbsolutePath());
		transformer.setParameter(paramCurrentFilePath,
				frontpageFile.getAbsolutePath());
		transformer.setParameter(paramInformationmodelFilePath,
				frontpageServicemodelFile.getAbsolutePath());
		transformer.setParameter(paramServicemodelcurrentFilePath,
				frontpageInformationmodelFile.getAbsolutePath());
		
		transformer.transform(new StreamSource(inputFile),
				new StreamResult(new FileOutputStream(frontpageFile)));

		logger.debug("Creating new frontpage: " + frontpageFile.getAbsolutePath());
	}
	
	private static File publishFrontpageForInformationmodel(File inputFile,
			File outputDirectory) throws TransformerException, IOException
	{
		File frontpageInformationmodelFile = PublishFileUtils.createInformationmodelFrontpageFile(outputDirectory);
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		File xsltFile = PublishFileUtils.getBoundleFileForPath(xsltFrontpageInformationmodelFilePath);
		Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFile));

		transformer.setParameter(paramOutputDirPath,
				outputDirectory.getAbsolutePath());
		transformer.setParameter(paramCurrentFilePath,
				frontpageInformationmodelFile.getAbsolutePath());
		
		transformer.transform(new StreamSource(inputFile),
				new StreamResult(new FileOutputStream(frontpageInformationmodelFile)));

		logger.debug("Creating new frontpage for informationmodel: " + frontpageInformationmodelFile.getAbsolutePath());
		
		return frontpageInformationmodelFile;
	}

	private static File publishFrontpageForServicemodel(File inputFile,
			File outputDirectory) throws TransformerException, IOException
	{
		File frontpageServicemodelFile = PublishFileUtils.createServicemodelFrontpageFile(outputDirectory);
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		File xsltFile = PublishFileUtils.getBoundleFileForPath(xsltFrontpageServicemodelFilePath);
		Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFile));

		transformer.setParameter(paramOutputDirPath,
				outputDirectory.getAbsolutePath());
		transformer.setParameter(paramCurrentFilePath,
				frontpageServicemodelFile.getAbsolutePath());
		
		transformer.transform(new StreamSource(inputFile),
				new StreamResult(new FileOutputStream(frontpageServicemodelFile)));

		logger.debug("Creating new frontpage for servicemodel: " + frontpageServicemodelFile.getAbsolutePath());
		
		return frontpageServicemodelFile;
	}
}
