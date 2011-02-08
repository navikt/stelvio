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
	private static final IPath xsltFilePath = new Path(
			"src/main/resources/xslt/web-publish-frontpage.xsl");
	
	private static final String paramOutputDirPath = "outputDirPath";
	private static final String paramCurrentFilePath = "currentFilePath";

	protected static void run(File inputFile, File outputDirectory) throws TransformerException, IOException 
	{
		publishFrontpage(inputFile, outputDirectory);
	}
	
	private static void publishFrontpage(File inputFile, File outputDirectory) throws TransformerException, IOException
	{
		File frontpageFile = PublishFileUtils.createFrontpageFile(outputDirectory);
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		File xsltFile = PublishFileUtils.getBoundleFileForPath(xsltFilePath);
		Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFile));

		transformer.setParameter(paramOutputDirPath,
				outputDirectory.getAbsolutePath());
		transformer.setParameter(paramCurrentFilePath,
				frontpageFile.getAbsolutePath());
		
		transformer.transform(new StreamSource(inputFile),
				new StreamResult(new FileOutputStream(frontpageFile)));

		logger.debug("Creating new frontpage: " + frontpageFile.getAbsolutePath());
	}
}
